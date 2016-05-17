package com.se.uta_rides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {

	JSONArray jsonArray;
	EditText username = null;
	EditText password = null;
	TextView signup,Reset;
	private Button login;
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	String status, result, uname, pasw, resp;
	InputStream isr;
	JSONArray jArray;
	TextView txt_Error;
	InputStream res = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// signup = (TextView) findViewById(R.id.textView4);
		// signup.setMovementMethod(LinkMovementMethod.getInstance());
		setContentView(R.layout.activity_log_in);
		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		final String DEFAULT = "N/A";
		SharedPreferences sharedpreferences = getSharedPreferences("MyData",
				Context.MODE_PRIVATE);
		String name = sharedpreferences.getString("name", DEFAULT);
		String pass = sharedpreferences.getString("pass", DEFAULT);
		if (!name.equals(DEFAULT) && !pass.equals(DEFAULT)) {

			Intent openActivity = new Intent("com.se.uta_rides.LAUNCHACTIVITY");
			startActivity(openActivity);
			finish();
		}

		login = (Button) findViewById(R.id.button1);
		login.setOnClickListener(this);

		signup = (TextView) findViewById(R.id.textView4);
		SpannableString content = new SpannableString("signup");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		signup.setText(content);
		// signup.setAutoLinkMask(Linkify.class);
		signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent openActivity = new Intent(
						"com.se.uta_rides.SIGNUPACTIVITY");
				startActivity(openActivity);
				finish();
			}
		});
		Reset = (TextView) findViewById(R.id.textView5);
		SpannableString content1 = new SpannableString("ForgotPassword");
		content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
		Reset.setText(content1);
		//signup.setAutoLinkMask(Linkify.class);
		Reset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent openActivity = new Intent("com.se.uta_rides.GENERATEPASSWORD");
				startActivity(openActivity);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		System.out.println("Entered OnClick");
		uname = username.getText().toString().trim();
		pasw = password.getText().toString();

		if(uname.isEmpty()){
			Toast.makeText(getApplicationContext(),
					"Enter Username!",
					Toast.LENGTH_SHORT).show();
		}
		else if(pasw.isEmpty()){
			Toast.makeText(getApplicationContext(),
					"Enter the password!",
					Toast.LENGTH_SHORT).show();
		}else{

			UserValidation validates = new UserValidation();
			validates.execute(uname, pasw);
		}

	}

	private class UserValidation extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			String username = params[0];
			String pasword = params[1];
			// TODO Auto-generated method stub
			try {
				String params1 = "email='" + username.trim() + "'&&" + "pas='"
						+ pasword + "'";
				String fullUrl = "http://omega.uta.edu/verify_password_local.php?"
						+ params1;
				System.out.println("fullurl - " + fullUrl);
				httpClient = new DefaultHttpClient();
				/*
				 * httppost = new HttpPost(
				 * "http://192.168.0.13/verify_password_local.php?" + params);
				 */
				httppost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/verify_password.php?"
								+ params1);
				// httppost = new
				// HttpPost("http://omega.uta.edu/~sxk7162/db_mysql_o.php?");
				System.out.println("httpPost is done");
				response = httpClient.execute(httppost);
				System.out.println(response);
				entity = response.getEntity();
				if (entity != null) {
					isr = entity.getContent();
					System.out.println("byte - " + isr.available());
				}
			} catch (UnsupportedEncodingException e) {
				Log.e("log_tag", " Error in UnsupportedEncodingException - "
						+ e.toString());
			} catch (ClientProtocolException e) {
				Log.e("log_tag",
						" Error in ClientProtocolException - " + e.toString());
			} catch (IOException e) {
				Log.e("log_tag", " Error in IOException - " + e.toString());
			} catch (Exception e) {
				Log.e("log_tag", " Error in Connection" + e.toString());
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(isr, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				isr.close();

				result = sb.toString();
				System.out.println("result from ISR : " + result);
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			try {
				jsonArray = new JSONArray(result);
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}

			try {

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				status = jsonObject.getString("status");
				System.out.println("Printing the status!!!!!!!!!!!!" + status);

			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

			return status;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("Printing the result " + result);
			if (result.equals("true")) { // && (!username.equals("") &&
				// !password.equals(""))){
				Toast.makeText(getApplicationContext(), "Logging in...",
						Toast.LENGTH_SHORT).show();
				System.out.println("Exeucuting shared preferences");
				SharedPreferences sharedpreferences = getSharedPreferences(
						"MyData", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("name", username.getText().toString().trim());
				editor.putString("pass", password.getText().toString());
				editor.commit();
				Intent openActivity = new Intent(
						"com.se.uta_rides.LAUNCHACTIVITY");
				startActivity(openActivity);
				finish();
			} 
			else if(result.equals("verify")){
				//Toast.makeText(getApplicationContext(), "Please reset your password", 
				//	Toast.LENGTH_SHORT).show();
				System.out.println("inside verify!!!!!!!!!!!!!!");
				Intent openActivity = new Intent("com.se.uta_rides.PASSWORDRESETTING");
				System.out.println(username.getText().toString());
				System.out.println(password.getText().toString());
				openActivity.putExtra("email", username.getText().toString());
				openActivity.putExtra("password", password.getText().toString());
				startActivity(openActivity);

			}else {
				Toast.makeText(getApplicationContext(), "Wrong Credentials",
						Toast.LENGTH_SHORT).show();

			}

		}

	}

}
