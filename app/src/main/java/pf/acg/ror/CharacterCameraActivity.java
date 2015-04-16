package pf.acg.ror;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CharacterCameraActivity extends SingleFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		//Hide the window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Hide the status bar
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected Fragment createFragment(){
		return new CharacterCameraFragment();
	}

}
