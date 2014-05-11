package com.letrinhas04;

import com.letrinhas03.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom extends ArrayAdapter<String>{	
	private final Activity context;
	private final String[] nome;
	private final String[] nomeImg;
	String caminho;
	
	public Custom(Activity context,String[] nome, String[] nomeImg, String caminho) {
		super(context, R.layout.lista_simples, nome);
		this.context = context;
		this.nome = nome;
		this.nomeImg = nomeImg;
		this.caminho = caminho;
	}
	
	public String Caminho(){
		String cam = null;
		if(caminho == "escola"){
			cam = "Schools";
		}else if(caminho == "professores"){
			cam = "Professors";
		}
		Log.d("Custom", cam);
		return cam;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.lista_simples, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() +"/School-Data/"+Caminho()+"/"+nomeImg[position];
	    Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(nome[position]);
		imageView.setImageBitmap(bitmap);
		//imageView.setImageResource(nomeImg[position]);
		return rowView;
	}
}