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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Window;

public class SoundRecorderPreferenceActivity extends PreferenceActivity 
	implements OnSharedPreferenceChangeListener {
    private static final String ENABLE_SOUND_EFFECT = "pref_key_enable_sound_effect";

    private static final String BITRATE = "pref_key_bitrate";
    private static final String SAMPLE_RATE = "pref_key_sample_rate";
    private static final String QUALITY = "pref_key_quality";
    private static final String MODE = "pref_key_mode";
    private static final String LOCALIZATION = "pref_key_localization";
    //private static final String SCALE = "pref_key_scale";
    private static final String RECORD_TYPE = "pref_key_record_type";
    private static final String ABOUT = "pref_key_about";
    private static final String SAVE_PATH = "pref_key_save_path";
    
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen pScreen, Preference p)
    {
    	if (p.getKey().equals(ABOUT))
    		startActivity(new Intent(this, AboutActivity.class));
    	else
    		return super.onPreferenceTreeClick(pScreen, p);

    	return false;
	}
    
	@Override
	protected void onResume() {
	    super.onResume();
	    // Set up a listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    // Unregister the listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
    
    @Override
    protected void onCreate(Bundle icicle) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.preferences);
                
    	ListPreference pf = (ListPreference) findPreference(LOCALIZATION);
    	pf.setSummary(getString(R.string.summary_localization, pf.getEntry()));
        
        pf = ((ListPreference)findPreference(BITRATE));
        pf.setSummary(pf.getEntry());
	
		pf = ((ListPreference)findPreference(SAMPLE_RATE));
        pf.setSummary(pf.getEntry());
		
		pf = ((ListPreference)findPreference(QUALITY));
        pf.setSummary(pf.getEntry());
		
		pf = ((ListPreference)findPreference(MODE));
        pf.setSummary(pf.getEntry());
        
        //pf = ((ListPreference)findPreference(SCALE));
        //pf.setSummary(pf.getEntry());
        
        pf = ((ListPreference)findPreference(RECORD_TYPE));
        pf.setSummary(pf.getEntry());
        
        EditTextPreference ef = ((EditTextPreference)findPreference(SAVE_PATH));
        ef.setSummary(ef.getText());
    }

    public static String getLocalization(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(LOCALIZATION, "");
    }
    
    public static int getBitrate(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(settings.getString(BITRATE, "128"));
    }
    public static int getSampleRate(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(settings.getString(SAMPLE_RATE, "44100"));
    }
    public static int getQuality(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(settings.getString(QUALITY, "5"));
    }
    public static int getMode(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(settings.getString(MODE, "16"));
    }
    public static float getScale(Context context) {
        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return 1;	//Float.parseFloat(settings.getString(SCALE, "0"));
    }
    public static boolean getIsOgg(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(RECORD_TYPE, "0").equals("0") ? false : true;
    }
    public static String getSavePath(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return "/" + settings.getString(SAVE_PATH, "Sound Recorder");
    }

    public static boolean isEnabledSoundEffect(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(ENABLE_SOUND_EFFECT, true);
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences p, String key) 
    {
    	if (key.equals(LOCALIZATION)) {
    		ListPreference pf = (ListPreference) findPreference(LOCALIZATION);
    		pf.setSummary(getString(R.string.summary_localization, pf.getEntry()));
    	}
    	else if (key.equals(BITRATE)) {
    		ListPreference pf = ((ListPreference)findPreference(BITRATE));
            pf.setSummary(pf.getEntry());
    	}
    	else if (key.equals(SAMPLE_RATE)) {
    		ListPreference pf = ((ListPreference)findPreference(SAMPLE_RATE));
            pf.setSummary(pf.getEntry());
    	}
    	else if (key.equals(QUALITY)) {
    		ListPreference pf = ((ListPreference)findPreference(QUALITY));
            pf.setSummary(pf.getEntry());
    	}
    	else if (key.equals(MODE)) {
    		ListPreference pf = ((ListPreference)findPreference(MODE));
            pf.setSummary(pf.getEntry());
    	}
    	else if (key.equals(RECORD_TYPE)) {
    		ListPreference pf = ((ListPreference)findPreference(RECORD_TYPE));
            pf.setSummary(pf.getEntry());
    	}
    	/*else if (key.equals(SCALE)) {
    		ListPreference pf = ((ListPreference)findPreference(SCALE));
            pf.setSummary(pf.getEntry());
    	}*/
    	else if (key.equals(SAVE_PATH)) {
    		EditTextPreference pf = ((EditTextPreference)findPreference(SAVE_PATH));
            pf.setSummary(pf.getText());
    	}
    }
}
