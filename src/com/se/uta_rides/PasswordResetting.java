package com.se.uta_rides;



import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordResetting extends BaseActivity implements OnClickListener {
	EditText Npass,Cpass;
	String email,generatedpass;
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetpassword);
		Npass = (EditText) findViewById(R.id.newpassword);
		Cpass = (EditText) findViewById(R.id.confirmpassword);
		email = getIntent().getExtras().getString("email");
	    //generatedpass = getIntent().getExtras().getString("password");
		
		Button RButton = (Button) findViewById(R.id.Reset);

		RButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String password = Npass.getText().toString();
		String confirmPassword = Cpass.getText().toString();
		if(password.isEmpty()){
		Toast.makeText(getApplicationContext(),
				"Please enter password!",
				Toast.LENGTH_SHORT).show();
		}else if(confirmPassword.isEmpty()){
			Toast.makeText(getApplicationContext(),
					"Please enter confirm password!",
					Toast.LENGTH_SHORT).show();
		}else if(confirmPassword.equals(password)){
		String flag="true";
		EnterValues enter = new EnterValues();
		enter.execute(email,confirmPassword,flag);
		
		Toast.makeText(
				PasswordResetting.this,
				"Password resetted", Toast.LENGTH_SHORT)
				.show();
		
		Intent i = new Intent("com.se.uta_rides.LAUNCHACTIVITY");
		startActivity(i);
		finish();
		}
		else{
			Toast.makeText(
					PasswordResetting.this,
					"password doesn't match", Toast.LENGTH_SHORT)
					.show();
			
		}
	}
	

private class EnterValues extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String email = params[0];
		String pasword = params[1];
		// String confirmPassword = params[2];
		String flags= params[2];
		
		// TODO Auto-generated method stub
		try {
			String params1 = "email='" + email + "'&&" + "pas='" + pasword
					+ "'&&" + "flag='" + flags+"'" ;
			String fullUrl = "http://omega.uta.edu/forget_password.php?"
					+ params1;
			System.out.println("fullurl - " + fullUrl);
			httpClient = new DefaultHttpClient();
			
			httppost = new HttpPost(
					"http://omega.uta.edu/~sxk7162/forget_password.php?"
							+ params1);
		
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
