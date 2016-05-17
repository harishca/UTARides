package com.se.uta_rides;

/*<<<<<<< HEAD
//import com.parse.ParseObject;
=======
>>>>>>> 3c11cac54c60e2d7d3945a520d558f6d9291fece*/
import com.parse.ParseInstallation;
//import com.se.pushNotification.ParseApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/*CarOwnersDetailsActivity - Displays the Name and Contact of the selected Car Owner*/

public class CarOwnersDetailsActivity extends BaseActivity implements OnClickListener {
	private String carOwnerName, carOwnerNumber, carOwnerFromTime, carOwnerToTime;
	private TextView riderName, riderNumber, riderFromTime, riderToTime;
	Button sendNotify;
	ParseApplication parse = new ParseApplication();

/*<<<<<<< HEAD
	//ParseObject obj;

=======
>>>>>>> 3c11cac54c60e2d7d3945a520d558f6d9291fece*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carowner_details);

		/*Retrieves Name and Number text view*/
		riderName = (TextView) findViewById(R.id.riderName);
		riderNumber = (TextView) findViewById(R.id.riderNumber);
		riderFromTime = (TextView) findViewById(R.id.riderFromTime);
		riderToTime = (TextView) findViewById(R.id.riderToTime);
		
		/*Retrieves Name and Number from Search Activity*/
		carOwnerName = getIntent().getStringExtra("carOwnerName");
		carOwnerNumber = getIntent().getStringExtra("carOwnerNumber");
		carOwnerFromTime = getIntent().getStringExtra("carOwnerFromTime");
		carOwnerToTime = getIntent().getStringExtra("carOwnerToTime");
		
		/*Set the Name and Number*/
		riderName.setText(carOwnerName);
		riderNumber.setText(carOwnerNumber);
		riderFromTime.setText(carOwnerFromTime);
		riderToTime.setText(carOwnerToTime);
		sendNotify = (Button)findViewById(R.id.notifyButton);
		sendNotify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.notifyButton:
			
		}
	}
}