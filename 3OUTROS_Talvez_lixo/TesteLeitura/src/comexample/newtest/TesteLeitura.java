package comexample.newtest;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TesteLeitura extends Activity {

   private MediaRecorder myAudioRecorder;
   private String outputFile;
   private Button iniciar,terminar;
   
/**
 * 
 */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      iniciar = (Button)findViewById(R.id.StartR);
      terminar = (Button)findViewById(R.id.StopR);
     
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
      
      terminar.setEnabled(false);
      
   	}
/**
 * 
 * @param view
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
      iniciar.setEnabled(false);
      terminar.setEnabled(true);
      Toast.makeText(getApplicationContext(), "Gravação iniciada!", Toast.LENGTH_LONG).show();
   }

 /**
  * 
  * @param view
  */
   public void stopR(View view){
	  try{
	      myAudioRecorder.stop();
	      myAudioRecorder.release();
	      terminar.setEnabled(false);
	      Toast.makeText(getApplicationContext(), "Gravação concluída!",
	      Toast.LENGTH_LONG).show();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   /**
    * 
    */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   
 
}