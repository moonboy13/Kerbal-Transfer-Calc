package moonboy.kerbal.kerbaltransfercalc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	/* Keys for passing values between intents */
	// Orbit Info Screen Data Keys
	public final static String CUR_PLANET = "moonboy.kerbal.kerbaltransfercalc.CUR_PLANET";
	public final static String TAR_PLANET = "moonboy.kerbal.kerbaltransfercalc.TAR_PLANET";
	public final static String CUR_ORBIT = "moonboy.kerbal.kerbaltransfercalc.CUR_ORBIT";
	public final static String TAR_ORBIT = "moonboy.kerbal.kerbaltransfercalc.TAR_ORBIT";
	public final static String CUR_YEAR = "moonboy.kerbal.kerbaltransfercalc.CUR_YEAR";
	public final static String CUR_DOY = "moonboy.kerbal.kerbaltransfercalc.CUR_DOY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Goto the Kerbal Blog when the news button is pressed **/
	public void gotoBlog(View view){
		String url = "http://kerbaldevteam.tumblr.com/mobile";
		Uri uriUrl = Uri.parse(url);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);
	}

	/** Goto the Kerbal Wiki when the wiki button is pressed **/
	public void gotoWiki(View view){
		String url = "http://wiki.kerbalspaceprogram.com/wiki/Main_Page";
		Uri uriUrl = Uri.parse(url);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);
	}
	
	/** Open the Calculator input screen when start calculator is pressed **/
	public void startCalc(View view){
		Intent info_intent= new Intent(this,OrbitInfoScreen.class);
		startActivity(info_intent);
	}
}
