package comexample.newtest;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MediaRecorder myAudioRecorder;
	private MediaPlayer mP = new MediaPlayer();
	private String outputFile;
	private Button startR,stopR,startP,stopP;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startR = (Button)findViewById(R.id.StartR);
	    stopR = (Button)findViewById(R.id.StopR);
	    startP = (Button)findViewById(R.id.StartP);
	    stopP = (Button)findViewById(R.id.StopP);
	    setUp();
	}

	/**
	    * 
	    */
	   	public void setUp(){
	   	  outputFile = Environment.getExternalStorageDirectory().
	      getAbsolutePath() + "/myrecording.3gpp";
	      
	   	  myAudioRecorder = new MediaRecorder();
	      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
	      myAudioRecorder.setOutputFile(outputFile);
	      
	      stopR.setEnabled(false);
	      stopP.setEnabled(false);
	      startP.setEnabled(false);
	   	}
	   /**
	    * serve para começar o record
	    * @param view
	    * @author Dário Jorge
	    */
	   public void start(View view){
	      try {
	    	  setUp();
	         myAudioRecorder.prepare();
	         myAudioRecorder.start();
	      } catch (IllegalStateException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	         System.out.println(e);
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	    	  System.out.println(e);
	         e.printStackTrace();
	      }
	      startR.setEnabled(false);
	      stopR.setEnabled(true);
	      Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
	   }

	   /**
	    * Serve para parar o recording do audio
	    * @param view
	    * @author Dário Jorge
	    */
	   public void stopR(View view){
		  try{
		      myAudioRecorder.stop();
		      myAudioRecorder.release();
		      stopR.setEnabled(false);
		      startP.setEnabled(true);
		      Toast.makeText(getApplicationContext(), "Audio recorded successfully",
		      Toast.LENGTH_LONG).show();
		   }catch(Exception e){
			   e.printStackTrace();
		   }
	   }
	   
	   /*
	    * (non-Javadoc)
	    * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	    * @author Dário Jorge
	    */
	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	   }
	   
	   /**
	    * 
	    * serve para a aplicação correr o som
	    * @param view
	    * @throws IllegalArgumentException
	    * @throws SecurityException
	    * @throws IllegalStateException
	    * @throws IOException
	    * @author Dário Jorge
	    */
	   public void play(View view) throws Exception{// caso algo dê errado no throws exception implementar o ---> IllegalArgumentException, SecurityException, IllegalStateException, IOException    || em vez de Exception
		   	   mP = new MediaPlayer();
			   mP.setDataSource(outputFile);
			   mP.prepare();
			   mP.start();
			   startP.setEnabled(false);
			   stopP.setEnabled(true);
			   Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
	   }
	   
	   /**
	    * serve para a aplicação parar o som
	    * @param view
	    * @throws Exception
	    * @author Dário Jorge
	    */
	   public void stopP(View view) throws Exception{// caso algo dê errado no throws exception implementar o ---> IllegalArgumentException, SecurityException, IllegalStateException, IOException    || em vez de Exception
					  mP.stop();
					  mP.release();
					 // mP = null;
					  startR.setEnabled(true);
					  stopP.setEnabled(false);
					  Toast.makeText(getApplicationContext(), "Audio Stoped", Toast.LENGTH_LONG).show();
	   }
}
