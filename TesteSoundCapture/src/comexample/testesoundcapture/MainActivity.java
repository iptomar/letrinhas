package comexample.testesoundcapture;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
    static String mFileName;

    MediaRecorder mRecorder;

    MediaPlayer   mPlayer;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button rec =  (Button)findViewById(R.id.btStartRec);
		final Button play =  (Button)findViewById(R.id.btStartPlay);

		rec.setOnClickListener(new Button.OnClickListener() {
			boolean btChangeType = false;
			public void onClick(View v) {
				if(btChangeType == false){
				rec.setText("StopRec"); 
				btChangeType = true; stopRecording();
				}else{
					rec.setText("StartRec");
					btChangeType = false; 
					startRecording();
					}
				}	
		});
		
		play.setOnClickListener(new Button.OnClickListener() {
			boolean btChangeType = false;
			public void onClick(View v) {
				if(btChangeType == false){
					play.setText("StopPlay"); 
					btChangeType = true; 
					stopPlaying();
					}else{
						play.setText("StartPlay");
						btChangeType = false;
						startPlaying();
						}
					}	
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
