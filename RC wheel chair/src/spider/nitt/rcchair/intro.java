package spider.nitt.rcchair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class intro extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.intro);
	
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(3000);
				} catch(InterruptedException e){
					e.printStackTrace();
			} finally{
				Intent OpenApp = new Intent("spider.nitt.rcchair.CHOOSE"); 
				startActivity(OpenApp);
				finish();
			}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	

}
