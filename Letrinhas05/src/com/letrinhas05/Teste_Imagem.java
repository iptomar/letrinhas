package com.letrinhas05;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letrinhas05.R;
import com.letrinhas05.util.SystemUiHider;
import com.letrinhas05.util.Teste;

public class Teste_Imagem extends Activity {

				// flags para verificar os diversos estados do teste
				boolean modo, gravado, recording, playing;
				// objetos
				ImageButton record, play, voltar, cancelar, avancar, hip1, hip2, hip3;		
				Chronometer chrono;
				private MediaRecorder gravador;
				private MediaPlayer reprodutor = new MediaPlayer();
				private String endereco;
				Teste[] lista;
				TextView auxiliar;
				/**
				 * Whether or not the system UI should be auto-hidden after
				 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
				 */
				private static final boolean AUTO_HIDE = true;
				/**
				 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
				 * user interaction before hiding the system UI.
				 */
				private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
				/*********************************************************************
				 * The flags to pass to {@link SystemUiHider#getInstance}.
				 */
				private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
				/**
				 * The instance of the {@link SystemUiHider} for this activity.
				 */
				private SystemUiHider mSystemUiHider;

				@Override
				protected void onCreate(Bundle savedInstanceState) {
					super.onCreate(savedInstanceState);
					setContentView(R.layout.teste_imagem);
				}

}
