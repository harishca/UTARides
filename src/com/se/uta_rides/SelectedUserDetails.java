package com.se.uta_rides;

import com.se.uta_rides.ParseApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SelectedUserDetails extends BaseActivity implements
		OnClickListener {

	String passedName = null;
	String passedEmail = null;
	String passedPhone = null;
	String passedStartTime = null;
	String passedEndTime = null;
	String dateAcquired = "";
	String timeAcquired = "";
	String locSearch = "";
	String latitudeSearch = "";
	String longitudeSearch = "";
	String numberofseatsrequired = "";
	private TextView passedViewName = null;
	private TextView passedViewEmail = null;
	private TextView passedViewPhone = null;
	private TextView passedViewStart = null;
	private TextView passedViewEnd = null;
	private Button book;
	String emailid_stu = "";
	ParseApplication parse = new ParseApplication();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected);

		final String DEFAULT = "N/A";
		SharedPreferences sharedpreferences = getSharedPreferences("MyData",
				Context.MODE_PRIVATE);
		emailid_stu = sharedpreferences.getString("name", DEFAULT);

		passedName = getIntent().getStringExtra("firstName");
		passedEmail = getIntent().getStringExtra("email");
		passedPhone = getIntent().getStringExtra("phoneNumber");
		passedStartTime = getIntent().getStringExtra("startTime");
		passedEndTime = getIntent().getStringExtra("endTime");
		dateAcquired = getIntent().getStringExtra("datesearch");
		timeAcquired = getIntent().getStringExtra("timesearch");
		locSearch = getIntent().getStringExtra("locsearch");
		numberofseatsrequired = getIntent().getStringExtra(
				"numberofseatsrequired");
		latitudeSearch = getIntent().getStringExtra("latitudeSearch");
		longitudeSearch = getIntent().getStringExtra("longitudeSearch");

		passedViewName = (TextView) findViewById(R.id.sName);
		passedViewEmail = (TextView) findViewById(R.id.sEmail);
		passedViewPhone = (TextView) findViewById(R.id.sPhone);
		passedViewStart = (TextView) findViewById(R.id.sStart);
		passedViewEnd = (TextView) findViewById(R.id.sEnd);

		passedViewName.setText("Name:" + passedName);
		passedViewEmail.setText("Email:" + passedEmail);
		passedViewPhone.setText("Call | Msg -> " + passedPhone);
		passedViewPhone.setTypeface(null, Typeface.BOLD_ITALIC);
		passedViewStart.setText("Begin:" + passedStartTime);
		passedViewEnd.setText("End:" + passedEndTime);

		book = (Button) findViewById(R.id.book);
		book.setOnClickListener(this);

		passedViewPhone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				callIntent.setData(Uri.parse("tel:" + passedPhone));
				startActivity(callIntent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.book:
			parse.sendNotificationtoCarowner(passedEmail, dateAcquired,
					timeAcquired, locSearch, emailid_stu,
					numberofseatsrequired, latitudeSearch, longitudeSearch);
		}

	}

}
