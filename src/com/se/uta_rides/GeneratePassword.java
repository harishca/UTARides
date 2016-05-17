package com.se.uta_rides;

import java.io.IOException;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import android.telephony.SmsManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GeneratePassword extends BaseActivity{
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	EditText txtphoneNo,Email;
	String password;
	String message;
	String phoneNo, result;
	public String email;
	String flag = "false";
	String scaddr = "UTA_RIDES";
	JSONArray jsonArray;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generatepassword);
        Button button = (Button) findViewById(R.id.resetButton);
        
        //txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        Email = (EditText) findViewById(R.id.emailaddress);
        
        
        //generate random numbers
        String uuid = UUID.randomUUID().toString();
        System.out.println("uuid = " + uuid);
    	password=uuid.substring(0, 7);
    	
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	email = Email.getText().toString();
            	//phoneNo = txtphoneNo.getText().toString();
            	
                // Perform action on click
            	EnterValues enter = new EnterValues();
    			enter.execute(email, password, flag);
    			Intent openActivity = new Intent("com.se.uta_rides.LOGINACTIVITY");
				startActivity(openActivity);
				finish();
            	}
            });
    }
	protected void sendSMSMessage() {
	      Log.i("Send SMS", "");
	      
	      
	      message = "New password is " +password;//txtMessage.getText().toString();

	      try {
	         SmsManager smsManager = SmsManager.getDefault();
	         System.out.println(phoneNo+message+scaddr);
	         smsManager.sendTextMessage(phoneNo,scaddr, message, null, null);
	         Toast.makeText(getApplicationContext(), "SMS sent.",
	         Toast.LENGTH_LONG).show();
	         /*Intent openActivity = new Intent("com.se.uta_rides.RESETPASSWORD");
	         openActivity.putExtra("email", email);
	         openActivity.putExtra("password", password);
				startActivity(openActivity);*/
	      } catch (Exception e) {
	         Toast.makeText(getApplicationContext(),
	         "SMS faild, please try again.",
	         Toast.LENGTH_LONG).show();
	         e.printStackTrace();
	         
	      }
	   }
        public class EnterValues extends AsyncTask<String, String, String> {

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
    				String fullUrl = "http://omega.uta.edu/~sxk7162/forget_password.php?"
    						+ params1;
    				System.out.println("fullurl - " + fullUrl);
    				httpClient = new DefaultHttpClient();
    				/*
    				 * httppost = new HttpPost(
    				 * "http://192.168.0.13/verify_password_local.php?" + params);
    				 */
    				httppost = new HttpPost(
    						"http://omega.uta.edu/~sxk7162/forget_password.php?"
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
    					phoneNo = jsonObject.getString("u_contact");
    					sendSMSMessage();
    					System.out.println("Printing the status!!!!!!!!!!!!" + phoneNo);

    				} catch (JSONException e) {
    					Log.e("Error", e.getMessage());
    					e.printStackTrace();
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
