package com.se.uta_rides.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class JSONfunctions {
	List<NameValuePair> newValuePairs;
	HttpClient httpClient;
	HttpPost httppost;
    HttpResponse response;
    HttpEntity entity;
	InputStream isr;
	String result = " ";
	JSONArray jArray;

	public JSONArray getJSONfromURL(String dateSearch, String timeSearch) {

		// Download JSON Data from server
		try {
			System.out.println("time stamp - " + dateSearch + " " + timeSearch);
			newValuePairs = new ArrayList<NameValuePair>(2);
			newValuePairs.add(new BasicNameValuePair("day_id", dateSearch));
			newValuePairs.add(new BasicNameValuePair("time", timeSearch));

			httpClient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://omega.uta.edu/~sxk7162/db_mysql_o.php");
			httppost.setEntity(new UrlEncodedFormEntity(newValuePairs));
			response = httpClient.execute(httppost);
			entity = response.getEntity();
			System.out.println("Entity value" + entity.getContentLength());
			System.out.println("Entity Object" + entity.toString());
			if (entity != null) {
				isr = entity.getContent();
				System.out.println("byte - " + isr.available());
			} else {
				return jArray;
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("log_tag",
					" Error in UnsupportedEncodingException - " + e.toString());
		} catch (ClientProtocolException e) {
			Log.e("log_tag",
					" Error in ClientProtocolException - " + e.toString());
		} catch (IOException e) {
			Log.e("log_tag", " Error in IOException - " + e.toString());
		} catch (Exception e) {
			Log.e("log_tag", " Error in Connection" + e.toString());
		}

		// Convert Response to String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					isr, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			isr.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jArray;
	}
}