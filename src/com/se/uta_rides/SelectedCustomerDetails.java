package com.se.uta_rides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectedCustomerDetails extends Activity {
	String email, name, contact, location, date, time, seats;
	private TextView DisplayEmail, DisplayName, DisplayContact,
			DisplayLocation, DisplayDate, DisplayTime, DisplaySeats;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_customer);

		email = getIntent().getStringExtra(("email"));
		name = getIntent().getStringExtra(("name"));
		contact = getIntent().getStringExtra(("contact"));
		location = getIntent().getStringExtra(("location"));
		date = getIntent().getStringExtra(("date"));
		time = getIntent().getStringExtra(("time"));
		seats = getIntent().getStringExtra(("seats"));

		DisplayEmail = (TextView) findViewById(R.id.displayEmail);
		DisplayName = (TextView) findViewById(R.id.displayName);
		DisplayContact = (TextView) findViewById(R.id.displayContact);
		DisplayLocation = (TextView) findViewById(R.id.displayLocation);
		DisplayDate = (TextView) findViewById(R.id.displayDate);
		DisplayTime = (TextView) findViewById(R.id.displayTime);
		DisplaySeats = (TextView) findViewById(R.id.displaySeats);

		DisplayEmail.setText(email);
		DisplayName.setText(name);
		DisplayContact.setText(contact);
		DisplayLocation.setText(location);
		DisplayDate.setText(date);
		DisplayTime.setText(time);
		DisplaySeats.setText(seats);
	}
}