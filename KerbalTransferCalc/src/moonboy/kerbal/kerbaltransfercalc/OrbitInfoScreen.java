package moonboy.kerbal.kerbaltransfercalc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class OrbitInfoScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orbit_info_screen);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Setup the two spinners for the starting and final planet
		Spinner startSpinner = (Spinner) findViewById(R.id.current_planet);
		Spinner targetSpinner = (Spinner) findViewById(R.id.target_planet);
		ArrayAdapter<CharSequence> startAdapter = ArrayAdapter.createFromResource(this, R.array.initial_planet_array, 
				android.R.layout.simple_spinner_item);
		startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> targetAdapter = ArrayAdapter.createFromResource(this, R.array.target_planet_array, 
				android.R.layout.simple_spinner_item);
		targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		startSpinner.setAdapter(startAdapter);
		targetSpinner.setAdapter(targetAdapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orbit_info_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Validating the input
	public void validateInput(View view){
		// Boolean for controlling the next stage + variable initilization
		Boolean inputValid = true;
		Double curOrbitRadius=0.0;
		Double tarOrbitRadius=0.0;
		Double curDOY=0.0;
		int curYear=0;
		// Get the spinner values
		Spinner curPlanetSpinner = (Spinner) findViewById(R.id.current_planet);
		String curPlanet = curPlanetSpinner.getSelectedItem().toString();
		Spinner tarPlanetSpinner = (Spinner) findViewById(R.id.target_planet);
		String tarPlanet = tarPlanetSpinner.getSelectedItem().toString();
		// Get the orbital values
		EditText curOrbitEditText = (EditText) findViewById(R.id.start_orbit_radius);
		if( curOrbitEditText.getText().toString().length() == 0){
			inputValid=false;
			Toast.makeText(this, R.string.cur_orb_err, Toast.LENGTH_SHORT).show();
		} else {
			curOrbitRadius = Double.parseDouble(curOrbitEditText.getText().toString());
			EditText tarOrbitEditText = (EditText) findViewById(R.id.end_orbit_radius);
			if( tarOrbitEditText.getText().toString().length() == 0){
				inputValid=false;
				Toast.makeText(this, R.string.tar_orb_err, Toast.LENGTH_SHORT).show();
			} else{
				tarOrbitRadius = Double.parseDouble(tarOrbitEditText.getText().toString());
				// Get the date information
				EditText curYearEditText = (EditText) findViewById(R.id.cur_year);
				if( curYearEditText.getText().toString().length() == 0){
					inputValid=false;
					Toast.makeText(this, R.string.year_err, Toast.LENGTH_SHORT).show();
				} else {
					curYear = Integer.parseInt(curYearEditText.getText().toString());
					EditText curDOYEditText = (EditText) findViewById(R.id.cur_day);
					if (curDOYEditText.getText().toString().length() == 0){
						inputValid=false;
						Toast.makeText(this, R.string.doy_err, Toast.LENGTH_SHORT).show();
					} else {
						curDOY = Double.parseDouble(curDOYEditText.getText().toString());
					}
				}
			}
		}
		if (inputValid){
			submitData(curPlanet,tarPlanet,curOrbitRadius,tarOrbitRadius,curYear,curDOY);
		}
	}
	
	//Retrieving data and starting calculator
	void submitData(String curPlanet, String tarPlanet, Double curOrbitRadius, Double tarOrbitRadius,
			int curYear, Double curDOY){
		// Declare a new intent
		Intent calc_intent = new Intent(this, CalculateTransfer.class);
		// Add all the information to the new intent
		calc_intent.putExtra(MainActivity.CUR_PLANET, curPlanet); //String
		calc_intent.putExtra(MainActivity.TAR_PLANET, tarPlanet); //String
		calc_intent.putExtra(MainActivity.CUR_ORBIT, curOrbitRadius); //Double
		calc_intent.putExtra(MainActivity.TAR_ORBIT, tarOrbitRadius); //Double
		calc_intent.putExtra(MainActivity.CUR_YEAR, curYear); //Int
		calc_intent.putExtra(MainActivity.CUR_DOY, curDOY); //Double
		// Start the intent
		startActivity(calc_intent);
	}

}
