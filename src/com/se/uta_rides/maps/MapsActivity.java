package com.se.uta_rides.maps;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.se.uta_rides.R;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnClickListener,
		OnMarkerClickListener {
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng latLng;
	Button buttonMapSearch;
	EditText textMapSearch;
	String searchLocation;
	String returnLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		Bundle extras = getIntent().getExtras();
		String sendLocation = extras.getString("sendLocation");

		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		if (status == ConnectionResult.SUCCESS) {
			SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			googleMap = supportMapFragment.getMap();
		} else {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();
		}

		buttonMapSearch = (Button) findViewById(R.id.buttonMapSearch);
		textMapSearch = (EditText) findViewById(R.id.textMapSearch);
		textMapSearch.setText(sendLocation);

		buttonMapSearch.setOnClickListener(this);
		googleMap.setOnMarkerClickListener(this);
	}

	@Override
	public void onClick(View v) {
		searchLocation = textMapSearch.getText().toString();

		if (searchLocation != null && !searchLocation.equals("")) {
			Toast.makeText(getApplicationContext(), searchLocation,
					Toast.LENGTH_SHORT);
			new GeocoderTask().execute(searchLocation);
		} else {
			Toast.makeText(getApplicationContext(),
					"Please enter location to be searched", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// @Override
	// protected void onPause() {
	// super.onPause();
	// finish();
	// }

	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("returnLocation", returnLocation);
		setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Toast.makeText(getApplicationContext(),
				marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
		returnLocation = marker.getPosition().latitude + ":"
				+ marker.getPosition().longitude + ":" + marker.getTitle();
		return false;
	}

	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... params) {
			Geocoder geoCoder = new Geocoder(getBaseContext());
			List<Address> address = null;

			try {
				address = geoCoder.getFromLocationName(params[0], 100);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return address;
		}

		@Override
		protected void onPostExecute(List<Address> address) {
			if (address == null || address.size() == 0) {
				Toast.makeText(getBaseContext(), "No search results :-(",
						Toast.LENGTH_SHORT).show();
			}

			googleMap.clear();

			try {
				System.out.println("mapping o mapping");
				System.out.println(address.size());
			} catch (NullPointerException e) {
				Log.e("MapsActivity - onPostExecute - ", e.toString());
			}

			for (int i = 0; i < address.size(); i++) {
				Address searchResult = (Address) address.get(i);
				System.out.println(searchLocation);

				latLng = new LatLng(searchResult.getLatitude(),
						searchResult.getLongitude());
				System.out.println(latLng);

				String addressText = String
						.format("%s, %s",
								searchResult.getMaxAddressLineIndex() > 0 ? searchResult
										.getAddressLine(0)
										+ " "
										+ searchResult.getAddressLine(1)
										+ " "
										+ searchResult.getAddressLine(2)
										: "", searchResult.getCountryName());
				System.out.println(addressText);

				markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);

				googleMap.addMarker(markerOptions);
				markerOptions.getPosition();

				// if (i == 0) {
				// googleMap.animateCamera(CameraUpdateFactory
				// .newLatLng(latLng));
				// }

				googleMap.moveCamera(CameraUpdateFactory.zoomTo((float) 11.5));
				googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			}
		}
	}
}