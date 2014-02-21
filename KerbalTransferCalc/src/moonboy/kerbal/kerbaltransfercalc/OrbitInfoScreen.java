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
		Spinner start_spinner = (Spinner) findViewById(R.id.current_planet);
		Spinner target_spinner = (Spinner) findViewById(R.id.target_planet);
		ArrayAdapter<CharSequence> start_adapter = ArrayAdapter.createFromResource(this, R.array.initial_planet_array, 
				android.R.layout.simple_spinner_item);
		start_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> target_adapter = ArrayAdapter.createFromResource(this, R.array.target_planet_array, 
				android.R.layout.simple_spinner_item);
		target_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		start_spinner.setAdapter(start_adapter);
		target_spinner.setAdapter(target_adapter);
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
		Boolean input_valid = true;
		Double cur_orbit_radius=0.0;
		Double tar_orbit_radius=0.0;
		Double cur_doy=0.0;
		int cur_year=0;
		// Get the spinner values
		Spinner cur_planet_spinner = (Spinner) findViewById(R.id.current_planet);
		String cur_planet = cur_planet_spinner.getSelectedItem().toString();
		Spinner tar_planet_spinner = (Spinner) findViewById(R.id.target_planet);
		String tar_planet = tar_planet_spinner.getSelectedItem().toString();
		// Get the orbital values
		EditText cur_orbit_editText = (EditText) findViewById(R.id.start_orbit_radius);
		if( cur_orbit_editText.getText().toString().length() == 0){
			input_valid=false;
			Toast.makeText(this, R.string.cur_orb_err, Toast.LENGTH_SHORT).show();
		} else {
			cur_orbit_radius = Double.parseDouble(cur_orbit_editText.getText().toString());
			EditText tar_orbit_editText = (EditText) findViewById(R.id.end_orbit_radius);
			if( tar_orbit_editText.getText().toString().length() == 0){
				input_valid=false;
				Toast.makeText(this, R.string.tar_orb_err, Toast.LENGTH_SHORT).show();
			} else{
				tar_orbit_radius = Double.parseDouble(tar_orbit_editText.getText().toString());
				// Get the date information
				EditText cur_year_editText = (EditText) findViewById(R.id.cur_year);
				if( cur_year_editText.getText().toString().length() == 0){
					input_valid=false;
					Toast.makeText(this, R.string.year_err, Toast.LENGTH_SHORT).show();
				} else {
					cur_year = Integer.parseInt(cur_year_editText.getText().toString());
					EditText cur_doy_editText = (EditText) findViewById(R.id.cur_day);
					if (cur_doy_editText.getText().toString().length() == 0){
						input_valid=false;
						Toast.makeText(this, R.string.doy_err, Toast.LENGTH_SHORT).show();
					} else {
						cur_doy = Double.parseDouble(cur_doy_editText.getText().toString());
					}
				}
			}
		}
		if (input_valid){
			submitData(cur_planet,tar_planet,cur_orbit_radius,tar_orbit_radius,cur_year,cur_doy);
		}
	}
	
	//Retrieving data and starting calculator
	void submitData(String cur_planet, String tar_planet, Double cur_orbit_radius, Double tar_orbit_radius,
			int cur_year, Double cur_doy){
		// Declare Keys
		// Keys for the values passed to the next intent
		String CUR_PLANET = "moonboy.kerbal.kerbaltransfercalc.CUR_PLANET";
		String TAR_PLANET = "moonboy.kerbal.kerbaltransfercalc.TAR_PLANET";
		String CUR_ORBIT = "moonboy.kerbal.kerbaltransfercalc.CUR_ORBIT";
		String TAR_ORBIT = "moonboy.kerbal.kerbaltransfercalc.TAR_ORBIT";
		String CUR_YEAR = "moonboy.kerbal.kerbaltransfercalc.CUR_YEAR";
		String CUR_DOY = "moonboy.kerbal.kerbaltransfercalc.CUR_DOY";
		// Declare a new intent
		Intent calc_intent = new Intent(this, CalculateTransfer.class);
		// Add all the information to the new intent
		calc_intent.putExtra(CUR_PLANET, cur_planet);
		calc_intent.putExtra(TAR_PLANET, tar_planet);
		calc_intent.putExtra(CUR_ORBIT, cur_orbit_radius);
		calc_intent.putExtra(TAR_ORBIT, tar_orbit_radius);
		calc_intent.putExtra(CUR_YEAR, cur_year);
		calc_intent.putExtra(CUR_DOY, cur_doy);
		// Start the intent
		startActivity(calc_intent);
	}

}
