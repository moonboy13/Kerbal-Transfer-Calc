package conlon.kerbal.kerbaltransfercalc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

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
		String url = "http://kerbaldevteam.tumblr.com/";
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
