package com.se.uta_rides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ViewRequestsActivity extends Activity {
	JSONArray jsonArray;
	ListView listView;
	ListAdapter adapter;
	ProgressDialog mProgressDialog;
	HashMap<String, String> map;
	ArrayList<HashMap<String, String>> arrayList;
	ListView resultView;
	String result, email, aname, contact, location, date, time, seats;

	private static final String EMAIL = "u_email";
	private static final String NAME = "u_name";
	private static final String PHONE_NUMBER = "u_contact";
	private static final String LOCATION = "w_location";
	private static final String DATE = "w_date";
	private static final String TIME = "w_time";
	private static final String SEATS = "number_of_seats";
	// public static final String ID_EXTRA="ID";
	List<NameValuePair> newValuePairs;
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	JSONArray jArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_list);
		StrictMode.enableDefaults();
		resultView = (ListView) findViewById(R.id.listRequest);
		final String DEFAULT = "N/A";
		SharedPreferences sharedpreferences = getSharedPreferences("MyData",
				Context.MODE_PRIVATE);
		String name = sharedpreferences.getString("name", DEFAULT);
		System.out.println("Name++++++++ " + name);

		arrayList = new ArrayList<HashMap<String, String>>();
		
		/* Retrieves the Date and Time values sent from Search Activity */

		try {
			System.out.println("sending req to view wishlist");

			/* Create a query parameters to be sent to web services */
			httpClient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://omega.uta.edu/~sxk7162/view_wishlist.php");
			response = httpClient.execute(httppost);
			System.out.println(response);
			entity = response.getEntity();
			System.out.println("Entity Object" + entity.toString());
			if (entity != null) {
				isr = entity.getContent();
				System.out.println("byte - " + isr.available());
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("ViewRequestsActivity - ",
					" Error in UnsupportedEncodingException - " + e.toString());
		} catch (ClientProtocolException e) {
			Log.e("ViewRequestsActivity - ",
					" Error in ClientProtocolException - " + e.toString());
		} catch (IOException e) {
			Log.e("ViewRequestsActivity - ",
					" Error in IOException - " + e.toString());
		} catch (Exception e) {
			Log.e("ViewRequestsActivity - ",
					" Error in Connection" + e.toString());
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
			System.out.println("result from ISR : " + result);
		} catch (Exception e) {
			Log.e("ViewRequestsActivity - ",
					"Error converting result " + e.toString());
		}

		// System.out.println("ViewRequestsActivity - " + result);

		boolean flag = false;

		if (result != null) {
			try {
				jsonArray = new JSONArray(result);
			} catch (JSONException e) {
				Log.e("log_tag",
						"Error parsing data JSONException " + e.toString());
				flag = true;
			} catch (NullPointerException e) {
				Log.e("log_tag",
						"Error parsing data NullPointerException "
								+ e.toString());
				flag = true;
			}
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					email = jsonObject.getString(EMAIL);
					aname = jsonObject.getString(NAME);
					contact = jsonObject.getString(PHONE_NUMBER);
					location = jsonObject.getString(LOCATION);
					date = jsonObject.getString(DATE);
					time = jsonObject.getString(TIME);
					seats = jsonObject.getString(SEATS);

					HashMap<String, String> map = new HashMap<String, String>();

					map.put(EMAIL, email);
					map.put(NAME, aname);
					map.put(PHONE_NUMBER, contact);
					map.put(LOCATION, location);
					map.put(DATE, date);
					map.put(TIME, time);
					map.put(SEATS, seats);

					arrayList.add(map);
				}
				System.out.println("I did something here");
				/* Retrieve a List View to set the list of available Rides */
				resultView = (ListView) findViewById(R.id.listRequest);
				ListAdapter adapter = new SimpleAdapter(
						ViewRequestsActivity.this, arrayList,
						R.layout.activity_median, new String[] { NAME },
						new int[] { R.id.textMedian });
				System.out.println("Seems to work!!");
				resultView.setAdapter(adapter);
				resultView.setOnItemClickListener(onListClick);

			} catch (JSONException e) {
				Log.e("log_tag",
						"No clue of this JSONException - " + e.toString());
				flag = true;
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.e("log_tag",
						"No clue of this NullPointerException - "
								+ e.toString());
				flag = true;
				e.printStackTrace();
			}
		} else if (result == null) {
			Toast.makeText(getApplicationContext(), "No Requests",
					Toast.LENGTH_LONG).show();
			flag = false;
		}
		if (flag == true) {
			Toast.makeText(getApplicationContext(), "No Requests here",
					Toast.LENGTH_LONG).show();
		}
	}

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent i = new Intent(ViewRequestsActivity.this,
					SelectedCustomerDetails.class);
			System.out.println(String.valueOf(id));
			System.out.println(arrayList.get((int) id).get("u_name"));

			i.putExtra("email", arrayList.get((int) id).get(EMAIL));
			i.putExtra("name", arrayList.get((int) id).get(NAME));
			i.putExtra("contact", arrayList.get((int) id).get(PHONE_NUMBER));
			i.putExtra("location", arrayList.get((int) id).get(LOCATION));
			i.putExtra("date", arrayList.get((int) id).get(DATE));
			i.putExtra("time", arrayList.get((int) id).get(TIME));
			i.putExtra("seats", arrayList.get((int) id).get(SEATS));

			startActivity(i);
		}
	};
}
