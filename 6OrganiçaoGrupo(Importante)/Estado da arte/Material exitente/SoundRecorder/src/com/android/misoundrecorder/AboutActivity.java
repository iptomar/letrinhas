package com.android.misoundrecorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import android.util.Log;
import android.view.*;

public class AboutActivity extends Activity
{
	private TextView buildText;
	private TextView filemanagerText;
	
	@Override
	protected void onCreate(Bundle bundle)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(bundle);
		setContentView(R.layout.about);
		
		this.filemanagerText = (TextView) findViewById(R.id.project_name);
		this.filemanagerText.setText("Mi Sound Recorder"/*getString(R.string.app_name)*/ + " V" + getVersionName(this));
		
		this.buildText = (TextView)findViewById(R.id.buildText);
		this.buildText.setText("Build " + getVersionCode(this));

		TextView tv = ((TextView) findViewById(R.id.app_website));
		tv.setText("Update");
		tv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        Intent intent = new Intent(Intent.ACTION_VIEW, 
		        		Uri.parse("http://forum.xda-developers.com/showthread.php?p=28295194"));
		        startActivity(intent);
			}
		});
		
		/*tv.setText("Mi Projects");
		Pattern pattern = Pattern.compile("[a-zA-Z ]+&");
	    String scheme = "http://forum.xda-developers.com/showthread.php?p=28295194";
	    Linkify.addLinks(tv, pattern, scheme);*/
    
	    /*tv.setMovementMethod(LinkMovementMethod.getInstance());
	    tv.setText(Html.fromHtml("<a href='http://forum.xda-developers.com/showthread.php?p=28295194'>Mi Projects</a>"));*/
        
		((TextView) findViewById(R.id.copyrights_one)).setText("Mi Mp3 Recorder, All rights reserved.");
		((TextView) findViewById(R.id.copyrights_two)).setText("Copyright©2012 Mi");
		
		//txtWebsite.setText("http://forum.xda-developers.com/showthread.php?t=1523691");
		//Util.setAsLink(txtWebsite, "www.miprojects.co.cc");
				
		//overridePendingTransition(R.anim.go_right, R.anim.go_left);
  	}
	
	@Override
	public boolean onKeyDown(int key, KeyEvent e)
  	{
		if (key != KeyEvent.KEYCODE_SEARCH)
			return super.onKeyDown(key, e);
    	else
    		return true;
  	}
    
    public static int getVersionCode(Context context) {
		try {
		    return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}
		catch (Exception e) {
			Log.v("log", e.getMessage());
		    return 0;
		}
    }
    public static String getVersionName(Context context) {
		try {
		    return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		}
		catch (Exception e) {
		    Log.v("log", e.getMessage());
		    return "";
		}
    }
}