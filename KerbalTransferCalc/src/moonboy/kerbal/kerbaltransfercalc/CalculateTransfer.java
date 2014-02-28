package moonboy.kerbal.kerbaltransfercalc;


import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

// Class to open the corresponding XML file for particular planets
// and store the information for it.
class PlanetInfo{
	// Class variables
	private double mass, inclination;
	private int sphereOfInfluence, semiMajorAxis, orbitalPeriod, argumentOfPeriapsis, radius;
	private XmlPullParser xpp;
	
	// Constructor
	PlanetInfo(Context parent, String planet){
		// Since a switch-case statement doesn't work for strings
		// nest the if's
		if (planet == "Moho"){
			xpp = parent.getResources().getXml(R.xml.moho);
		} else if (planet == "Eve"){
			xpp = parent.getResources().getXml(R.xml.eve);
		} else if (planet == "Kerbin"){
			xpp = parent.getResources().getXml(R.xml.kerbin);
		} else if (planet == "Duna"){
			xpp = parent.getResources().getXml(R.xml.duna);
		} else if (planet == "Dres"){
			xpp = parent.getResources().getXml(R.xml.dres);
		} else if (planet == "Jool"){
			xpp = parent.getResources().getXml(R.xml.jool);
		} else {
			xpp = parent.getResources().getXml(R.xml.eeloo);
		}
		try{
			int eventType = xpp.getEventType();
			while (eventType != xpp.END_DOCUMENT){
				// Find the start tag then use nested if's again to assign values
				if(eventType == xpp.START_TAG){
					if (xpp.getName()=="radius-km"){
						xpp.next();
						radius = Integer.parseInt(xpp.getText());
					} else if (xpp.getName() == "mass-kg"){
						xpp.next();
						mass = Double.parseDouble(xpp.getText());
					} else if (xpp.getName() == "soi-m"){
						xpp.next();
						sphereOfInfluence = Integer.parseInt(xpp.getText());
					} else if (xpp.getName() == "semimajor-m"){
						xpp.next();
						semiMajorAxis = Integer.parseInt(xpp.getText());
					} else if (xpp.getName() == "orbperiod-s"){
						xpp.next();
						orbitalPeriod = Integer.parseInt(xpp.getText());
					} else if (xpp.getName() == "inclination-deg"){
						xpp.next();
						inclination = Double.parseDouble(xpp.getText());
					} else if (xpp.getName() == "argofper-deg"){
						xpp.next();
						argumentOfPeriapsis = Integer.parseInt(xpp.getText());
					}
				}
				xpp.next();
			}
		}catch(XmlPullParserException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	// Methods to access values
	double getMass(){
		return mass;
	}
	double getInclination(){
		return inclination;
	}
	int getSOI(){
		return sphereOfInfluence;
	}
	int getSemiMajorAxis(){
		return semiMajorAxis;
	}
	int getPeriod(){
		return orbitalPeriod;
	}
	int getRadius(){
		return radius;
	}
    int getArgOfPeri(){
    	return argumentOfPeriapsis;
    }
}

// Class to contain the methods that will calculate the transfer orbital parameters
class TransferParameters{
	
}

public class CalculateTransfer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate_transfer);
		// Show the Up button in the action bar.
		setupActionBar();
		// Hide the action bar
		ActionBar actionbar = getActionBar();
		actionbar.hide();
		// Retrieve information from previous intent
		Intent previousIntent = getIntent();
		// Retrieve the information for the planets in question
		String curPlanet = previousIntent.getStringExtra(MainActivity.CUR_PLANET);
		PlanetInfo curInfo = new PlanetInfo(this,curPlanet);
		String tarPlanet = previousIntent.getStringExtra(MainActivity.TAR_PLANET);
		PlanetInfo tarInfo = new PlanetInfo(this,tarPlanet);
		// Assign the rest of the information to local variables
		double curOrbit = Double.parseDouble(previousIntent.getStringExtra(MainActivity.CUR_ORBIT));
		double tarOrbit = Double.parseDouble(previousIntent.getStringExtra(MainActivity.TAR_ORBIT));
		double curDOY = Double.parseDouble(previousIntent.getStringExtra(MainActivity.CUR_DOY));
		int curYear = Integer.parseInt(previousIntent.getStringExtra(MainActivity.CUR_YEAR));
		calcTrans(curInfo,tarInfo,curOrbit,tarOrbit,curDOY,curYear);
	}

	/**
	 * Set up the {@link droid.app.ActionBar}, if the API is available.
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
		getMenuInflater().inflate(R.menu.calculate_transfer, menu);
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

	// Method to handle the calculating of the orbital transfer
	void calcTrans(PlanetInfo curPlanet, PlanetInfo tarPlanet, double curOrbit, double tarOrbit,
			       double curDOY, int curYear){
		
	}
}
