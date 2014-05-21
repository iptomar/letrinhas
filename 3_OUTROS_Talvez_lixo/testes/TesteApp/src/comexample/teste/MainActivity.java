package comexample.teste;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button button =  (Button)findViewById(R.id.btCronometro);;

		button.setOnClickListener(new Button.OnClickListener() {
boolean isRunning = true;
			public void onClick(View v) {
				Chronometer chron = (Chronometer)findViewById(R.id.cronometro);;
				
				long tempoParado = 0;
				if(isRunning){ 
					chron.setBase(SystemClock.elapsedRealtime() + tempoParado);
					chron.start();
					tempoParado = 0;
					button.setText("Stop");
					isRunning = false;
				}
				else{
					button.setText("Start");
					chron.stop();
					tempoParado = 0;
					isRunning = true;
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
}
