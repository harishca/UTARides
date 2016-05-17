package com.se.uta_rides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WidgetActivity extends AppWidgetProvider {
	private static String status = "false";
	private static final String STATUS1 = "status";
	ArrayList<HashMap<String, String>> arrayList;
	private RemoteViews remoteViews;
	private ComponentName watchWidget;
	final String DEFAULT = "No Name";
	HttpClient httpClient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	InputStream isr;
	JSONArray jArray;
	String result, status1;
	JSONArray jsonArray;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.activity_widget);
		watchWidget = new ComponentName(context, WidgetActivity.class);
		// remoteViews.setTextViewText( R.id.tbutton, "Time = " + format.format(
		// new Date()));
		Intent intentClick = new Intent(context, WidgetActivity.class);
		intentClick.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ""
				+ appWidgetIds[0]);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				appWidgetIds[0], intentClick, 0);
		remoteViews.setOnClickPendingIntent(R.id.imageButtonGreen,
				pendingIntent);
		appWidgetManager.updateAppWidget(watchWidget, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		HttpClient httpClient;
		HttpPost httppost;
		HttpResponse response;
		HttpEntity entity;
		InputStream isr;

		SharedPreferences sharedpreferences = context.getSharedPreferences(
				"MyData", Context.MODE_PRIVATE);
		String name = sharedpreferences.getString("name", DEFAULT);
		System.out.println("Name++++++++ " + name);
		String params = "email='" + name + "'" + "&&status='" + status + "'";
		String fullUrl = "http://omega.uta.edu/~sxk7162/update_carowner_availability.php?"
				+ params;
		System.out.println("fullurl - " + fullUrl);

		if (intent.getAction() == null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				// int widgetId =
				// extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
				// AppWidgetManager.INVALID_APPWIDGET_ID);

				remoteViews = new RemoteViews(context.getPackageName(),
						R.layout.activity_widget);
				SetAvailibility update1 = new SetAvailibility();
				AsyncTask<String, Void, String> checkTimings = update1.execute(
						name, status);
				try {

					if (name == "No Name") {
						Toast.makeText(context,
								"Log in to change availability", 2000).show();
					}

					else if (checkTimings.get() != "") {
						System.out.println("timings not set set it first");
						Toast.makeText(context,
								"Please set available timings in app", 2000)
								.show();
					}

					else if (status == "true") {

						remoteViews.setImageViewResource(R.id.imageButtonGreen,
								R.drawable.green_car_icon);
						remoteViews.setTextViewText(R.id.availabilityTextView,
								"Available");
						Toast.makeText(context,
								"Availability changed to Available", 2000)
								.show();
						SetAvailibility update = new SetAvailibility();
						update.execute(name, status);
						status = "false";

					} else {
						remoteViews.setImageViewResource(R.id.imageButtonGreen,
								R.drawable.red_car_icon);
						remoteViews.setTextViewText(R.id.availabilityTextView,
								"Busy");
						Toast.makeText(context, "Availability changed to busy",
								2000).show();
						SetAvailibility update = new SetAvailibility();
						update.execute(name, status);
						status = "true";

					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				watchWidget = new ComponentName(context, WidgetActivity.class);

				(AppWidgetManager.getInstance(context)).updateAppWidget(
						watchWidget, remoteViews);
				// Toast.makeText(context, "Clicked "+status, 2000).show();
			}
		} else {
			super.onReceive(context, intent);
		}
	}

	private class SetAvailibility extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String email = params[0];
			String status = params[1];
			// String confirmPassword = params[2];
			// TODO Auto-generated method stub
			try {
				String params1 = "email='" + email + "'&&" + "status='"
						+ status + "'";
				String fullUrl = "http://omega.uta.edu/update_carowner_availability.php?"
						+ params1;
				System.out.println("fullurl - " + fullUrl);
				httpClient = new DefaultHttpClient();
				/*
				 * httppost = new HttpPost(
				 * "http://192.168.0.13/verify_password_local.php?" + params);
				 */
				httppost = new HttpPost(
						"http://omega.uta.edu/~sxk7162/update_carowner_availability.php?"
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
			// return null;
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

			System.out.println("WidgetActivity - " + result);
			return result;

		}

	}

}
