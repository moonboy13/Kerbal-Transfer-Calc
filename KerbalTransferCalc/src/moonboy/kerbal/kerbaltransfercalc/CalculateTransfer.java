package moonboy.kerbal.kerbaltransfercalc;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

// Class to open the corresponding XML file for particular planets
// and store the information for it.
class PlanetInfo{
	// Class variables
	private double mass, inclination;
	private long sphereOfInfluence, semiMajorAxis, orbitalPeriod;
    private int argumentOfPeriapsis, radius;
	
	// Constructor
	PlanetInfo(Context parent, String planet){
		// Nesting if's cause case-switch doesn't work with strings
		if (planet == "Moho"){
			mass = 2.53e21;
			inclination = 7.0;
			sphereOfInfluence = 9646663L;
			semiMajorAxis = 9832684544L;
			orbitalPeriod = 2215754L;
			radius = 250;
			argumentOfPeriapsis = 15;
		} else if (planet == "Eve"){
			mass = 1.23e23;
			inclination = 2.1;
			sphereOfInfluence = 85109365L;
			semiMajorAxis = 9832684544L;
			orbitalPeriod = 5657995L;
			radius = 700;
			argumentOfPeriapsis = 0;
		} else if (planet == "Kerbin"){
			mass = 5.29e22;
			inclination = 0.0;
			sphereOfInfluence = 84159286L;
			semiMajorAxis = 13599840256L;
			orbitalPeriod = 9203545L;
			radius = 600;
			argumentOfPeriapsis = 0;
		} else if (planet == "Duna"){
			mass = 4.52e21;
			inclination = 0.06;
			sphereOfInfluence = 47921949L;
			semiMajorAxis = 20726155264L;
			orbitalPeriod = 17315400L;
			radius = 320;
			argumentOfPeriapsis = 0;
		} else if (planet == "Dres"){
			mass = 3.22e20;
			inclination = 5.0;
			sphereOfInfluence = 32832840L;
			semiMajorAxis = 40839348203L;
			orbitalPeriod = 47893063L;
			radius = 138;
			argumentOfPeriapsis = 90;
		} else if (planet == "Jool"){
			mass = 4.24e24;
			inclination = 1.304;
			sphereOfInfluence = 2455985185L;
			semiMajorAxis = 68773560320L;
			orbitalPeriod = 104661432L;
			radius = 6000;
			argumentOfPeriapsis = 0;
		} else { //Planet must be Eeloo in this case
			mass = 1.12e21;
			inclination = 6.15;
			sphereOfInfluence = 119082942L;
			semiMajorAxis = 90118820000L;
			orbitalPeriod = 156992048L;
			radius = 210;
			argumentOfPeriapsis = 260;
		}
	}

	// Methods to access values
	double getMass(){
		return mass;
	}
	double getInclination(){
		return inclination;
	}
	long getSOI(){
		return sphereOfInfluence;
	}
	long getSemiMajorAxis(){
		return semiMajorAxis;
	}
	long getPeriod(){
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
	// Constants Definitions
	static final int SEC_HOUR=3600, SEC_DAY=24*SEC_HOUR, SEC_YEAR=365*SEC_DAY; // These parameters will be useful outside the class to they are not private
	private static final double GRAV_CONST=6.67e-11, KERBOL_MASS=1.76e28,PI=3.1415,MU_KERBOL=GRAV_CONST*KERBOL_MASS;
	private static final double TWO_PI=2*PI, RAD_2_DEG=180.0/PI;
	// Class Variables
	private PlanetInfo curInfo, tarInfo;
	private double secondsFromStart, muCurrent, muTarget, curOrbitRadius, tarOrbitRadius;
	
	// Internal Methods for calculations
	/* 
	 * Calculate the Hohmann transfer time from the current planet's distance from Kerbol (r1) and the
	 * target planet's distance from Kerbol(r2)
	 */
	private double calcTransTime(PlanetInfo curPlanet, PlanetInfo tarPlanet){
		return PI*Math.sqrt(Math.hypot(curPlanet.getSemiMajorAxis(), tarPlanet.getSemiMajorAxis())/8.0*MU_KERBOL);
	}
	/*
	 * Calculate the necessary angle of separation between the two planets for a successful transfer from
	 * the Hohmann transfer time and the distance of the target planet from Kerbol
	 */
	private double calTransAngle(double tHo, PlanetInfo tarPlanet){
		return PI-(Math.sqrt(MU_KERBOL/tarPlanet.getSemiMajorAxis())*(tHo/tarPlanet.getSemiMajorAxis()));
	}
	/*
	 * Calculate the mean motion of a planet
	 */
	private double meanMotion(PlanetInfo planet){
		return PI+Math.sqrt(GRAV_CONST*(KERBOL_MASS+planet.getMass())/Math.pow(planet.getSemiMajorAxis(),3));
	}
	/* 
	 * Calculate the Hohmann transfer velocity
	 */
	private double hohmannVelocity(double muCurrent, PlanetInfo current, PlanetInfo target){
		return Math.sqrt(muCurrent/current.getSemiMajorAxis())*
				(Math.sqrt(2*target.getSemiMajorAxis()/(current.getSemiMajorAxis()+target.getSemiMajorAxis()))-1.0);
	}
	/*
	 * Calculate the del v needed for escape
	 */
	private double escapeBurn(double muCurrent, double curOrbit, PlanetInfo current, PlanetInfo target){
		double vHoh = hohmannVelocity(muCurrent,current,target);
		return Math.sqrt((curOrbit*(current.getSOI()*Math.pow(vHoh,2)-2*muCurrent)+2*current.getSOI()*muCurrent)/(curOrbit*current.getSOI()));
	}
	/*
	 * Calculate the del v needed for capture into the desired orbit at the target planet
	 */
	private double captureBurn(double muTarget, double muCurrent, double tarOrbit, PlanetInfo current, PlanetInfo target){
		double curVelo=hohmannVelocity(muCurrent, current, target)*current.getSemiMajorAxis()/target.getSemiMajorAxis();
		return Math.sqrt((target.getSOI()*(tarOrbit*Math.pow(curVelo, 2)-2*muTarget)+2*target.getSOI()*muTarget)/(tarOrbit*target.getSOI()));
	}
	/*
	 * Calculate the burn angle in degrees
	 */
	private double burnAngle(double muCurrent, double curOrbit, PlanetInfo current, PlanetInfo target){
		double vHoh = hohmannVelocity(muCurrent, current, target);
		double epsilon = Math.pow(vHoh, 2)/2 - muCurrent/curOrbit;
		double h = Math.sqrt(muCurrent*curOrbit);
		double e = Math.sqrt(1+(2*epsilon*Math.pow(h, 2)/Math.pow(muCurrent, 2)));
		double ejectionRad = PI - Math.acos(1/e);
		return ejectionRad*RAD_2_DEG;
	}
	
	
	// Class Constructor
	TransferParameters(String curPlanet, String tarPlanet, double curOrbit, double tarOrbit, double curDOY,
			           int curYear, Context parent){
		// Get the orbital parameters of the planets
		curInfo = new PlanetInfo(parent,curPlanet);
		tarInfo = new PlanetInfo(parent,tarPlanet);
		// Calculate how many seconds have passed since t0
		secondsFromStart=curYear*SEC_YEAR+curDOY*SEC_DAY;
		// Calculate the mu's for the planets
		muCurrent=GRAV_CONST*curInfo.getMass();
		muTarget=GRAV_CONST*tarInfo.getMass();
		// Add the radius of the planet to the orbital value to get the actual orbital radius
		curOrbitRadius=curOrbit+curInfo.getRadius();
		tarOrbitRadius=tarOrbit+tarInfo.getRadius();
	}
	
	// Method to calculate the transfer window
	double getTravelTime(){
		return calcTransTime(curInfo,tarInfo);
	}
	// This method is to calculate the time to the next transfer window
	double timeToTransfer(){
		// Declarations
		double curTrueAnomaly, tarTrueAnomaly, sep, diff, transTime, transAngle, deltaAngle, timeTrans;
		// Get the orbital positions and angular separation of the two planets
		curTrueAnomaly=meanMotion(curInfo)*secondsFromStart+curInfo.getArgOfPeri();
		while (curTrueAnomaly > TWO_PI){
			curTrueAnomaly-=TWO_PI;
		}
		tarTrueAnomaly=meanMotion(tarInfo)*secondsFromStart+tarInfo.getArgOfPeri();
		while (tarTrueAnomaly > TWO_PI){
			tarTrueAnomaly-=TWO_PI;
		}
		/*
		 * If I am traveling outwards in the Kerbol system I want my target to be ahead of my
		 * current planet. If I am traveling inwards I want my current planet to be ahead of my
		 * target planet.
		 */
		if (curInfo.getSemiMajorAxis() > tarInfo.getSemiMajorAxis()){
			sep=curTrueAnomaly-tarTrueAnomaly;
		} else {
			sep=tarTrueAnomaly-curTrueAnomaly;
		}
		if (sep < 0){ sep+=TWO_PI;}
		// Now figure out what the transfer angle should be and the difference between that and the current
		// separation
		transTime=calcTransTime(curInfo,tarInfo);
		transAngle=calTransAngle(transTime,tarInfo);
		diff=sep-transAngle;
		if (diff < 0){diff+=TWO_PI;} // if diff is negative then the planets need to do another lap to get into place
		// Calculate the rate of angle change between the planets with respect to time
		deltaAngle=Math.abs(meanMotion(curInfo)-meanMotion(tarInfo));
		// The time to transfer (in seconds) is the current angular distance divided by the deltaAngle
		timeTrans=diff/deltaAngle;
		return timeTrans;
	}
    // The rest are simple get methods to get the values
	double getEscapeBurn(){
		return escapeBurn(muCurrent, curOrbitRadius, curInfo, tarInfo);
	}
	double getCaptureBurn(){
		return captureBurn(muTarget, muCurrent, tarOrbitRadius, curInfo, tarInfo);
	}
	double getBurnAngle(){
		return burnAngle(muCurrent,curOrbitRadius,curInfo,tarInfo);
	}
}

public class CalculateTransfer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate_transfer);
		// Show the Up button in the action bar.
		setupActionBar();
		// Hide the action bar
		//ActionBar actionbar = getActionBar();
		//actionbar.hide();
		// Retrieve information from previous intent
		Intent previousIntent = getIntent();
		String curPlanet = previousIntent.getStringExtra(MainActivity.CUR_PLANET);
		String tarPlanet = previousIntent.getStringExtra(MainActivity.TAR_PLANET);
		Bundle extras = previousIntent.getExtras();
		double curOrbit = extras.getDouble(MainActivity.CUR_ORBIT);
		double tarOrbit = extras.getDouble(MainActivity.TAR_ORBIT);
		double curDOY = extras.getDouble(MainActivity.CUR_DOY);
		int curYear = extras.getInt(MainActivity.CUR_YEAR);
		calcTrans(this,curPlanet,tarPlanet,curOrbit,tarOrbit,curDOY,curYear);
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
	void calcTrans(Context parent, String curPlanet, String tarPlanet, double curOrbit, double tarOrbit,
			       double curDOY, int curYear){
		// Some variable declarations
		double secondsToTransfer, yearsToTransfer, daysToTransfer, hoursToTransfer, tempTime;
		double transferYear, transferDOY, delVEsc, delVCap, burnAngleDeg, travelTime;
		TransferParameters transfer = new TransferParameters(curPlanet,tarPlanet,curOrbit,tarOrbit,curDOY,curYear,parent);
		// Get the transfer time and convert it to a more usable time (both time to transfer and a date for transfer)
		secondsToTransfer=transfer.timeToTransfer();
		yearsToTransfer=secondsToTransfer/TransferParameters.SEC_YEAR;
		tempTime=secondsToTransfer%TransferParameters.SEC_YEAR;
		daysToTransfer=tempTime/TransferParameters.SEC_DAY;
		tempTime%=TransferParameters.SEC_DAY;
		hoursToTransfer=tempTime/TransferParameters.SEC_HOUR;
		transferYear=curYear+yearsToTransfer;
		transferDOY=curDOY+daysToTransfer;
		if (transferDOY > 365){
			transferDOY-=365;
			transferYear++;
		}
		// Get the rest of the parameters
		delVEsc=transfer.getEscapeBurn();
		delVCap=transfer.getCaptureBurn();
		burnAngleDeg=transfer.getBurnAngle();
		travelTime=transfer.getTravelTime();
		travelTime/=(double) TransferParameters.SEC_DAY;
		
		// Display this lovely information
		displayData(travelTime,transferYear,transferDOY,delVEsc,delVCap,yearsToTransfer,daysToTransfer,hoursToTransfer,burnAngleDeg,curPlanet,tarPlanet);
	}
	
	// Method to display the data
	public void displayData(double transTime, double winYear, double winDOY, double delVEsc, double delVCap, double yearsToTransfer, 
			double daysToTransfer, double hoursToTransfer, double burnAngle, String curPlanet, String tarPlanet){
		TextView planets = (TextView)findViewById(R.id.planets);
		planets.append(curPlanet+" to "+tarPlanet);
	}
}
