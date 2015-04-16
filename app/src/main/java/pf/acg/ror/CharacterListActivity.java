package pf.acg.ror;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class CharacterListActivity extends SingleFragmentActivity 
	implements CharacterListFragment.Callbacks, CharacterFragment.Callbacks{

	@Override
	protected Fragment createFragment() {
		return new CharacterListFragment();
	}

	@Override
	protected int getLayoutResId(){
		//return R.layout.activity_twopane;
		return R.layout.activity_masterdetail;
	}

	@Override
	public void onCharacterSelected(PC character) {
		if(findViewById(R.id.detailedFragmentContainer) == null){
			//start instance of Crime Pager Activity
			Intent i = new Intent(this, CharacterPagerActivity.class);
			i.putExtra(CharacterFragment.EXTRA_CHARACTER_ID, character.getId());
			startActivity(i);
		}else{
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment oldDetail = fm.findFragmentById(R.id.detailedFragmentContainer);
			Fragment newDetail = CharacterFragment.newInstance(character.getId());
			
			if(oldDetail != null){
				ft.remove(oldDetail);
			}
			
			ft.add(R.id.detailedFragmentContainer, newDetail);
			ft.commit();
			//CharacterListFragment listFragment = (CharacterListFragment)fm.findFragmentById(R.id.fragmentContainer);
			//listFragment.updateUI();
		}

	}

	@Override
	public void onCharacterUpdated(PC character) {
		FragmentManager fm = getSupportFragmentManager();
		CharacterListFragment listFragment = (CharacterListFragment)fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}

}
