package com.se.uta_rides;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.parse.ParseInstallation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends BaseActivity implements OnClickListener {

	private EditText editSignUpEmail;
	private EditText editSignUpPassword;
	private EditText editSignUpConfirmPassword;
	private EditText editSignUpName;
	private EditText editPhoneNumber;
	private Button signUpButton;
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	String name="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		editSignUpEmail = (EditText) findViewById(R.id.editSignUpEmail);
		editSignUpPassword = (EditText) findViewById(R.id.editSignUpPassword);
		editSignUpConfirmPassword = (EditText) findViewById(R.id.editSignUpConfirmPassword);
		editSignUpName = (EditText) findViewById(R.id.editSignUpName);
		editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
		signUpButton = (Button) findViewById(R.id.signUpButton);

		signUpButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("Entered onClick");
		String email = editSignUpEmail.getText().toString();
		System.out.println("Printing email" + email);
		String password = editSignUpPassword.getText().toString();
		String confirmPassword = editSignUpConfirmPassword.getText().toString();
		String name = editSignUpName.getText().toString();
		String phoneNumber = editPhoneNumber.getText().toString();
		// int intPhone = Integer.parseInt(phoneNumber);
		
		//storing the email id of student into parse database.
		
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		System.out.println(installation);
		installation.put("User_id", email);
		installation.saveInBackground();
		System.out.println("saved into DB");
		
		//validation sequence for all the inputs
		if(email.isEmpty()){
			Toast.makeText(getApplicationContext(), "Enter values in email field",
					Toast.LENGTH_SHORT).show();
		}else if (password.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter values in password field",
					Toast.LENGTH_SHORT).show();
		}else if (confirmPassword.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter values in confirm password field",
					Toast.LENGTH_SHORT).show();
		}else if (name.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter values in name field",
					Toast.LENGTH_SHORT).show();
		}else if (phoneNumber.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter values in phone number field",
					Toast.LENGTH_SHORT).show();
		}else if (!confirmPassword.equals(password)) {
			Toast.makeText(getApplicationContext(), "Passwords don't match",
					Toast.LENGTH_SHORT).show();
		} else if (password.length() < 8) {
			Toast.makeText(getApplicationContext(),
					"Password must contain atleast 8 characters",
					Toast.LENGTH_SHORT).show();
		} else if (!email.contains("@mavs.uta.edu")) {
			Toast.makeText(getApplicationContext(), "Enter a valid Mavs ID",
					Toast.LENGTH_SHORT).show();
		} else if (!(phoneNumber.length() == 10)) {
			Toast.makeText(getApplicationContext(),
					"Enter a valid phone number", Toast.LENGTH_SHORT).show();
		} else {
			EnterValues enter = new EnterValues();
			enter.execute(email, password, name, phoneNumber);
			
			Toast.makeText(
					SignupActivity.this,
					"Please Login", Toast.LENGTH_SHORT)
					.show();
			
			Intent i = new Intent("com.se.uta_rides.LOGINACTIVITY");
			startActivity(i);
			finish();
		}
	}

	private class EnterValues extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String email = params[0];
			String pasword = params[1];
			// String confirmPassword = params[2];
			String name = params[2];
			String encodeName = "";
			try {
				encodeName = URLEncoder.encode(name, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String phoneNumber = params[3];
			// TODO Auto-generated method stub
			try {
				String params1 = "email='" + email + "'&&" + "pas='" + pasword
						+ "'&&" + "name='" + encodeName + "'&&" + "phno='"
						+ phoneNumber + "'";
				String fullUrl = "http://omega.uta.edu/create_user_profile.php?"
						+ params;
				System.out.println("fullurl - " + fullUrl);
				httpClient = new DefaultHttpClient();
				/*
				 * httppost = new HttpPost(
				 * "http://192.168.0.13/verify_password_local.php?" + params);
				 */
				httppost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/create_user_profile.php?"
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
			return null;
		}

	}
}