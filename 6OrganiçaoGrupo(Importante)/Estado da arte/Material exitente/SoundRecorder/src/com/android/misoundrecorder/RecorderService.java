/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.misoundrecorder;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.net.Uri;
import android.os.*;
import android.os.PowerManager.WakeLock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.*;

public class RecorderService extends Service implements MediaRecorder.OnErrorListener {

    public final static String ACTION_NAME = "action_type";
    public final static int ACTION_INVALID = 0;
    public final static int ACTION_START_RECORDING = 1;
    public final static int ACTION_STOP_RECORDING = 2;
    public final static int ACTION_ENABLE_MONITOR_REMAIN_TIME = 3;
    public final static int ACTION_DISABLE_MONITOR_REMAIN_TIME = 4;
    public final static String ACTION_PARAM_PATH = "path";
    public final static String ACTION_PARAM_MAX_FILE_SIZE = "max_file_size";
    
    public final static String ACTION_PARAM_CHANNEL_CONFIG = "channel_config";
    public final static String ACTION_PARAM_SAMPLE_RATE = "sample_rate";
    public final static String ACTION_PARAM_BITRATE = "bitrate";
    public final static String ACTION_PARAM_QUALITY = "quality";
    public final static String ACTION_PARAM_SCALE = "scale";
    public final static String ACTION_PARAM_OGG = "isogg";
    
    public final static String RECORDER_SERVICE_BROADCAST_NAME = "com.android.misoundrecorder.broadcast";
    public final static String RECORDER_SERVICE_BROADCAST_STATE = "is_recording";
    public final static String RECORDER_SERVICE_BROADCAST_ERROR = "error_code";
    public final static int NOTIFICATION_ID = 62343234;
    private static AudioRecord aRecorder = null;

    private static String mFilePath = null;
    private static long mStartTime = 0;
    private RemainingTimeCalculator mRemainingTimeCalculator;
    private NotificationManager mNotifiManager;
    private Notification mLowStorageNotification;
    private TelephonyManager mTeleManager;
    private WakeLock mWakeLock;
    private KeyguardManager mKeyguardManager;

    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state != TelephonyManager.CALL_STATE_IDLE) {
                localStopRecording();
            }
        }
    };

    private final Handler mHandler = new Handler();

    private Runnable mUpdateRemainingTime = new Runnable() {
        public void run() {
            if (aRecorder != null && mNeedUpdateRemainingTime) {
                updateRemainingTime();
            }
        }
    };

    private boolean mNeedUpdateRemainingTime;

    @Override
    public void onCreate() {
        super.onCreate();
        aRecorder = null;
        mLowStorageNotification = null;
        mRemainingTimeCalculator = new RemainingTimeCalculator();
        mNeedUpdateRemainingTime = false;
        mNotifiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mTeleManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SoundRecorder");
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d("RecordService", "onStartCommand");
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(ACTION_NAME)) {
            switch (bundle.getInt(ACTION_NAME, ACTION_INVALID)) {
                case ACTION_START_RECORDING:
                	localStartRecording(
                            bundle.getString(ACTION_PARAM_PATH),
                            bundle.getLong(ACTION_PARAM_MAX_FILE_SIZE),
                            bundle.getInt(ACTION_PARAM_CHANNEL_CONFIG),
                            bundle.getInt(ACTION_PARAM_SAMPLE_RATE),
                            bundle.getInt(ACTION_PARAM_BITRATE),
                            bundle.getInt(ACTION_PARAM_QUALITY),
                            bundle.getFloat(ACTION_PARAM_SCALE),
                            bundle.getBoolean(ACTION_PARAM_OGG));
                    break;
                case ACTION_STOP_RECORDING:
                	localStopRecording();
                    break;
                case ACTION_ENABLE_MONITOR_REMAIN_TIME:
                    if (aRecorder != null) {
                        mNeedUpdateRemainingTime = true;
                        mHandler.post(mUpdateRemainingTime);
                    }
                    break;
                case ACTION_DISABLE_MONITOR_REMAIN_TIME:
                    mNeedUpdateRemainingTime = false;
                    if (aRecorder != null) {
                        showRecordingNotification();
                    }
                    break;
                default:
                    break;
            }
            return START_STICKY;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLowMemory() {
        localStopRecording();
        super.onLowMemory();
    }

    private static float amplitude = 0;
    private static int numSamples;
    
    private void localStartRecording(final String path, long maxFileSize,
    		int channelConfig, int sampleRate, int bitrate, int quality, float scale, boolean isOgg) {

    	if (aRecorder == null) {
    		Log.d("RecordService", "localStartRecording");
    		
            mRemainingTimeCalculator.reset();
            if (maxFileSize != -1)
                mRemainingTimeCalculator.setFileSizeLimit(new File(path), maxFileSize);
            mRemainingTimeCalculator.setBitRate(sampleRate);
						
            try {
            	numSamples = AudioRecord.getMinBufferSize(sampleRate, channelConfig, AudioFormat.ENCODING_PCM_16BIT);
				if (numSamples != AudioRecord.ERROR_BAD_VALUE) {
					aRecorder = new AudioRecord(AudioSource.MIC, sampleRate, channelConfig, AudioFormat.ENCODING_PCM_16BIT, numSamples);
            	}
				if (aRecorder == null) {
					sendErrorBroadcast(Recorder.INTERNAL_ERROR);
					Log.d("RecordService", "recorder = null");
					return;
				}

		        aRecorder.startRecording();
	        	
				record(path, bitrate, sampleRate, quality, channelConfig, scale, isOgg);
            } 
            catch (Exception exception) {
            	Log.d("RecordService", exception.toString());
                AudioManager audioMngr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                boolean isInCall = (audioMngr.getMode() == AudioManager.MODE_IN_CALL);
                if (isInCall)
                    sendErrorBroadcast(Recorder.IN_CALL_RECORD_ERROR);
                else
                    sendErrorBroadcast(Recorder.INTERNAL_ERROR);
                
                //recorder.reset();
                aRecorder.release();
                aRecorder = null;
                return;
            }

            mFilePath = path;
            mStartTime = System.currentTimeMillis();
            mWakeLock.acquire();
            mNeedUpdateRemainingTime = false;
            sendStateBroadcast();
            showRecordingNotification();
    	}
    }

    private void localStopRecording() {
    	if (aRecorder != null) {
            mNeedUpdateRemainingTime = false;
            try {
            	if (aRecorder != null)
            		aRecorder.stop();
            } catch (RuntimeException e) {
            }
            
            if (aRecorder != null) {
            	aRecorder.release();
	            aRecorder = null;
            }

            sendStateBroadcast();
            showStoppedNotification();
        }
        stopSelf();
    }
 
    public void record(final String path, final int bitrate, 
    		final int samplerate, final int quality, final int channelConfig, float scale, boolean isOgg) {
    	OutputStream stream = null; 
    	try {
    		stream = new FileOutputStream(new File(path), false);
    	}
    	catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
        final BufferedOutputStream out = new BufferedOutputStream(stream, numSamples);
        
        if (out != null) {
        	final boolean isMono = channelConfig == AudioFormat.CHANNEL_IN_MONO;
        	
        	Lame.initEncoder(bitrate, quality, samplerate, isMono ? 1 : 2, scale, isOgg ? 1 : 0);
        	
        	new Thread(new Runnable() {
        		@Override
    			public void run() {
    	            try {
    		            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
    	    		}
    	    		catch (Exception e) {
    	    		}
    	            
		            byte[] mp3Buf = new byte[(int) (1000 + numSamples * 2)];
		            short[] left = new short[8192];
		            short[] right = new short[8192];
		            int samplesRead;
		            int bytesEncoded;
		            
		            while (isRecording()) {
				        try {
			        		samplesRead = isMono ?
			        				readMono(left, numSamples)
			        				: readStereo(left, right, numSamples);
			            	
		                    if (samplesRead <= 0)
		                        break;
		                    	
	                    	// get amplitude
	                    	int len = 0;
	                        int peak = 0;
	                        int samp = 0;
	                        for (int i=0; i < samplesRead; i++) {
	                        	samp = Math.abs(left[i]);
	                        	if(samp > peak) 
	                        		peak = samp;
	                        	len += samp;
	                        }
	                        amplitude = len / samplesRead * 8;
				        	
	                        bytesEncoded = Lame.encode(left, isMono ? left : right, samplesRead, mp3Buf, mp3Buf.length);
	                        if (bytesEncoded == -1)
	                        	break;
	                        
	                        out.write(mp3Buf, 0, bytesEncoded);
				        }
				        catch (IOException e) {
				            Log.e("Recorder", e.toString());
				        }
		            }

			        try {
			        	bytesEncoded = Lame.flushEncoder(mp3Buf, mp3Buf.length);
			            out.write(mp3Buf, 0, bytesEncoded);
			            // write Xing VBR/INFO tag to mp3 file here
			            out.flush();
			            out.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        
			        Lame.closeEncoder();
    			}
    		}).start();
        }
    }
    
    private int readMono(short[] dst, int numSamples) throws IOException {
    	byte[] buf = new byte[numSamples * 2];
        int index = 0;
        
        if (aRecorder == null)
        	return -1;
        
        int bytesRead = aRecorder.read(buf, 0, numSamples * 2);

        for (int i = 0; i < bytesRead; i+=2) {
            dst[index] = byteToShortLE(buf[i], buf[i+1]);
            index++;
        }

        return index;
    } 
    
    private int readStereo(short[] left, short[] right, int numSamples) throws IOException {
    	byte[] buf = new byte[numSamples * 4];
        int index = 0;
        
        if (aRecorder == null)
        	return -1;
        
    	int bytesRead = aRecorder.read(buf, 0, numSamples * 4);

        for (int i = 0; i < bytesRead; i+=2) {
            short val = byteToShortLE(buf[0], buf[i+1]);
            if (i % 4 == 0) {
                left[index] = val;
            } else {
                right[index] = val;
                index++;
            }
        }

        return index;
    }
    
    private static short byteToShortLE(byte b1, byte b2) {
        return (short) (b1 & 0xFF | ((b2 & 0xFF) << 8));
    }
    
    private void showRecordingNotification() {
    	Notification notification = new Notification(R.drawable.stat_sys_call_record,
                getString(R.string.notification_recording), System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, SoundRecorder.class), 0);

        notification.setLatestEventInfo(this, getString(R.string.app_name),
                getString(R.string.notification_recording), pendingIntent);

        startForeground(NOTIFICATION_ID, notification);
    }

    private void showLowStorageNotification(int minutes) {
        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            // it's not necessary to show this notification in lock-screen
            return;
        }

        if (mLowStorageNotification == null) {
            mLowStorageNotification = new Notification(R.drawable.stat_sys_call_record_full,
                    getString(R.string.notification_recording), System.currentTimeMillis());
            mLowStorageNotification.flags = Notification.FLAG_ONGOING_EVENT;
        }

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, SoundRecorder.class), 0);

        mLowStorageNotification.setLatestEventInfo(this, getString(R.string.app_name),
                getString(R.string.notification_warning, minutes), pendingIntent);
        startForeground(NOTIFICATION_ID, mLowStorageNotification);
    }

    private void showStoppedNotification() {
        stopForeground(true);
        mLowStorageNotification = null;

        Notification notification = new Notification(R.drawable.stat_sys_call_record,
                getString(R.string.notification_stopped), System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        
        /*Intent intent = new Intent(this, RecordPreviewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("record_preview_type", 1);
        intent.putExtra("record_preview_highlight", mFilePath);*/
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("audio/*");
        intent.setDataAndType(Uri.fromFile(new File(mFilePath)), "audio/*");

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setLatestEventInfo(this, getString(R.string.app_name),
                getString(R.string.notification_stopped), pendingIntent);
        mNotifiManager.notify(NOTIFICATION_ID, notification);
    }

    private void sendStateBroadcast() {
        Intent intent = new Intent(RECORDER_SERVICE_BROADCAST_NAME);
        intent.putExtra(RECORDER_SERVICE_BROADCAST_STATE, aRecorder != null);
        sendBroadcast(intent);
    }

    private void sendErrorBroadcast(int error) {
        Intent intent = new Intent(RECORDER_SERVICE_BROADCAST_NAME);
        intent.putExtra(RECORDER_SERVICE_BROADCAST_ERROR, error);
        sendBroadcast(intent);
    }

    private void updateRemainingTime() {
        long t = mRemainingTimeCalculator.timeRemaining();
        if (t <= 0) {
            localStopRecording();
            return;
        } else if (t <= 1800
                && mRemainingTimeCalculator.currentLowerLimit() != RemainingTimeCalculator.FILE_SIZE_LIMIT) {
            // less than half one hour
            showLowStorageNotification((int) Math.ceil(t / 60.0));
        }

        if (aRecorder != null && mNeedUpdateRemainingTime) {
            mHandler.postDelayed(mUpdateRemainingTime, 500);
        }
    }

    public static boolean isRecording() {
        return aRecorder != null;
    }

    public static String getFilePath() {
        return mFilePath;
    }

    public static long getStartTime() {
        return mStartTime;
    }

    public static void startRecording(Context context, String path, long maxFileSize,
            int channelConfig, int samplerate, int bitrate, int quality, float scale, boolean isOgg) {
        Intent intent = new Intent(context, RecorderService.class);
        intent.putExtra(ACTION_NAME, ACTION_START_RECORDING);
        intent.putExtra(ACTION_PARAM_PATH, path);
        intent.putExtra(ACTION_PARAM_MAX_FILE_SIZE, maxFileSize);

        intent.putExtra(ACTION_PARAM_BITRATE, bitrate);
        intent.putExtra(ACTION_PARAM_CHANNEL_CONFIG, channelConfig);
        intent.putExtra(ACTION_PARAM_QUALITY, quality);
        intent.putExtra(ACTION_PARAM_SCALE, scale);
        intent.putExtra(ACTION_PARAM_OGG, isOgg);
        
        intent.putExtra(ACTION_PARAM_SAMPLE_RATE, samplerate);
        context.startService(intent);
    }

    public static void stopRecording(Context context) {
        Intent intent = new Intent(context, RecorderService.class);
        intent.putExtra(ACTION_NAME, ACTION_STOP_RECORDING);
        context.startService(intent);
    }

    public static int getMaxAmplitude() {
    	if (aRecorder != null)
    		return (int)amplitude;
		return 0;
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        sendErrorBroadcast(Recorder.INTERNAL_ERROR);
        localStopRecording();
    }
}
