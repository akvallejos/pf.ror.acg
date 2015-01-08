package pf.acg.ror;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class CharacterPagerActivity extends FragmentActivity
	implements CharacterFragment.Callbacks{
	private static String PAGER_TAG = "CrimePagerActivity";
	private ViewPager mViewPager;
	private ArrayList<PC> mCharacters;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mCharacters = CharacterBinder.get(this).getCharacters();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){
			@Override
			public int getCount(){
				return mCharacters.size();
			}
			
			@Override
			public Fragment getItem(int pos){
				PC character = mCharacters.get(pos);
				Log.d(PAGER_TAG, "Character id:" + character.getId());
				return CharacterFragment.newInstance(character.getId());
			}
			
		});//end setAdapter
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
			public void onPageScrollStateChanged(int state){
				// TODO Auto-generated method stub
			}
			
			public void onPageScrolled(int pos, float posOffset, int posOffsetPizels){
				// TODO Auto-generated method stub
			}
			
			public void onPageSelected(int pos){
				PC character = mCharacters.get(pos);
				if(character.getName()!=null){
					setTitle(character.getName());
				}
			}
		});
		
		UUID crimeId = (UUID)getIntent().getSerializableExtra(CharacterFragment.EXTRA_CHARACTER_ID);
		for(int i = 0; i < mCharacters.size(); i++){
			if(mCharacters.get(i).getId().equals(crimeId)){
				mViewPager.setCurrentItem(i);
				break;
			}//end if
		}//end for
		
	} //onCreate

	public void onCharacterUpdated(PC character){
		//left empty to provide phone compatability
	}
}
