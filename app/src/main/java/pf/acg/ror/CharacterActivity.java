package pf.acg.ror;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class CharacterActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		UUID pcId = (UUID)getIntent().getSerializableExtra(CharacterFragment.EXTRA_CHARACTER_ID);
		
		return CharacterFragment.newInstance(pcId);
	}

}
