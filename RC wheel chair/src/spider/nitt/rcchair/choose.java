package spider.nitt.rcchair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class choose extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose);
	}

	public void choose(View v){
		switch(v.getId()){
		case R.id.speak:
			Intent OpenAct1 = new Intent("spider.nitt.rcchair.VOICETOTEXTACTIVITY"); 
			startActivity(OpenAct1);
			finish();
			break;
		case R.id.remote:
			Intent OpenAct2 = new Intent("spider.nitt.rcchair.RCWHEELCHAIRACTIVITY"); 
			startActivity(OpenAct2);
			finish();
			break;
		}
	}
}
