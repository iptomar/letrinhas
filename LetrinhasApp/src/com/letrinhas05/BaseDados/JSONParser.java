package com.letrinhas05.BaseDados;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class JSONParser {

    // static InputStream is = null;
    // static JSONObject jObj = null;
    //static String json = "";

    // constructor
    public JSONParser() {
    }
    
    private static String getString(String url, List<NameValuePair> params) throws IOException {
    	AndroidHttpClient client = AndroidHttpClient.newInstance("letrinhas");
		
		String paramString = URLEncodedUtils.format(params, "utf-8");
        // url += "?" + paramString;
        url += paramString.length() > 0 ? "?" + paramString : "";

        HttpGet httpGet = new HttpGet(url);
        
        HttpResponse response = client.execute(httpGet);
        
        HttpEntity res = response.getEntity();
        
        InputStream in = res.getContent();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        byte[] buf = new byte[4096];
        int len = -1;
        
        while ((len = in.read(buf)) != -1) {
        	out.write(buf, 0, len);
        }
        out.flush();
        in.close();
        client.close();
        String str = new String(out.toByteArray(), "utf-8");
        
        return str;
        
        
    }

    /**
     * Faz um HTTP request, envia um Http Get Returna um JSONObject
     * Recebe:
     * - String URL do servidor
     * - Uma lista de Parametros possiveis
     * *
     */
    public static JSONObject getJSONObject(String url, List<NameValuePair> params) {    	
    	try {
            return new JSONObject(getString(url, params));
    	} catch (Exception e) {
    		Log.e("letrinhas-network-utils", e.getMessage());
    		return null;
    	}
    }


    /**
     * Faz um HTTP request, envia um Http Get Returna um JSONObject
     * Recebe:
     * - String URL do servidor
     * - Uma lista de Parametros possiveis
     * *
     */
    public static JSONArray getJSONArray(String url, List<NameValuePair> params) {
    	try {
            return new JSONArray(getString(url, params));
    	} catch (Exception e) {
    		Log.e("letrinhas-network-utils", e.getMessage());
    		
    		return null;
    	}
    }


    /**
     * Faz um HTTP request, onde envia um Json em String para o servidor com a informacao
     * Recebe como paramentros o URL do servidor e ficheiro de Json em String
     * *
     */
    public void Post(String url, JSONObject json) {        // Método utf-8
        try {
        	@SuppressWarnings("unused")
			InputStream is;
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(json.toString()));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (Exception e) {
            Log.e("Erro no Buffer", "Erro a converter resultados" + e.toString());
        }
    }


}
