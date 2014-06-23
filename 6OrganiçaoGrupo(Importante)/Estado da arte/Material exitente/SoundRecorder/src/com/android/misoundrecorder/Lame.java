package com.android.misoundrecorder;

public class Lame {
    private static final String LAME_LIB = "lame";

    static {
        System.loadLibrary(LAME_LIB);
    }

    /**
     * @return 0 if encoder initialized successfully, -1 otherwise
     */
    public static native int initEncoder(int bitRate, int quality, int sampleRate, 
    		int numChannels, float scale, int isOGG);

    /**
     *  @return -1 if error occured, otherwise number of bytes encoded
     */
    public static native int encode(short[] leftChannel,
            short[] rightChannel, int channelSamples, byte[] mp3Buffer,
            int bufferSize);

    /**
     * @return -1 if error occured, otherwise number of bytes flushed
     */
    public static native int flushEncoder(byte[] mp3Buffer, int bufferSize);

    /**
     * @return -1 if error occured, otherwise 0
     */
    public static native int closeEncoder();
}