package conlon.kerbal.kerbaltransfercalc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

}
