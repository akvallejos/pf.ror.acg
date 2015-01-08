package pf.acg.ror;

import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class CharacterFragment extends Fragment {

	public static final String EXTRA_CHARACTER_ID =  "pf.acg.ror";
	private static final String TAG = "CharacterFragment";
	private static final int REQUEST_PHOTO = 1;
	private static final String DIALOG_IMAGE = "image";
	
	private PC mCharacter;
	private EditText mNameField;
	private Callbacks mCallbacks;
	
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	
	private String mStrBase;
	private String mDexBase;
	private String mConBase;
	private String mIntBase;
	private String mWisBase;
	private String mChaBase;
	private String mFavCard;
	
	private ArrayAdapter<CharSequence> mStrBonus;
	private ArrayAdapter<CharSequence> mDexBonus;
	private ArrayAdapter<CharSequence> mConBonus;
	private ArrayAdapter<CharSequence> mIntBonus;
	private ArrayAdapter<CharSequence> mWisBonus;
	private ArrayAdapter<CharSequence> mChaBonus;
	private ArrayAdapter<CharSequence> mHandLimit;
	private ArrayAdapter<CharSequence> mProficiency;
	
	private ArrayAdapter<CharSequence> mWeaponsLimit;
	private ArrayAdapter<CharSequence> mArmorsLimit;
	private ArrayAdapter<CharSequence> mSpellsLimit;
	private ArrayAdapter<CharSequence> mItemsLimit;
	private ArrayAdapter<CharSequence> mAlliesLimit;
	private ArrayAdapter<CharSequence> mBlessingsLimit;
	
	private String[] mSkills;
	
	private enum attr{Str, Dex, Con, Int, Wis, Cha, 
		HandLimit, WeaponLimit, ArmorLimit, SpellLimit, ItemLimit, AllyLimit, BlessingLimit, Proficiency; }

	public static Fragment newInstance(UUID pcId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CHARACTER_ID, pcId);
		
		CharacterFragment fragment = new CharacterFragment();
		fragment.setArguments(args);
		
		return fragment;
	}

	public interface Callbacks {
		void onCharacterUpdated(PC character);

	}
	
	@Override
	public void onStart(){
		super.onStart();
		showPhoto();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onPause(){
		super.onPause();
		CharacterBinder.get(getActivity()).saveCharacters();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		Log.d("date_select", String.valueOf(Activity.RESULT_OK));
		if(resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_PHOTO) {
			String filename = data.getStringExtra(CharacterCameraFragment.EXTRA_PHOTO_FILENAME);
			if(filename != null){
				if(mCharacter.getPhoto() != null){
					getActivity().deleteFile(mCharacter.getPhoto().getFilename());
					PictureUtils.cleanImageView(mPhotoView);
				}
				Photo p = new Photo(filename);
				mCharacter.setPhoto(p);
				mCallbacks.onCharacterUpdated(mCharacter);
				showPhoto();
				Log.i(TAG, "Crime: " + mCharacter.getName() +"  has photo: " + filename);
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//mCrime = new Crime();
		UUID characterId = (UUID)getArguments().getSerializable(EXTRA_CHARACTER_ID);
		
		mCharacter = CharacterBinder.get(getActivity()).getPC(characterId);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.character_fragment, parent, false);
		setCharacterResourceArrays(v);

		TextView mRoleField = (TextView)v.findViewById(R.id.pc_role);
		mRoleField.setText(mCharacter.getRole());
		
		
		mNameField = (EditText)v.findViewById(R.id.pc_name);
		mNameField.setText(mCharacter.getName());
		
		//Strength
		TextView mStrField = (TextView)v.findViewById(R.id.str_base);
		mStrField.setText(mStrBase);
		createBonusSpinner(v, R.id.str_bonus, mStrBonus, mCharacter.getStrBonus(), attr.Str);
				
		//Dexterity
		TextView mDexField = (TextView)v.findViewById(R.id.dex_base);
		mDexField.setText(mDexBase);
		createBonusSpinner(v, R.id.dex_bonus, mDexBonus, mCharacter.getDexBonus(), attr.Dex);
		
		//Constitution
		TextView mConField = (TextView)v.findViewById(R.id.con_base);
		mConField.setText(mConBase);
		createBonusSpinner(v, R.id.con_bonus, mConBonus, mCharacter.getConBonus(), attr.Con);

		//Intelligence
		TextView mIntField = (TextView)v.findViewById(R.id.int_base);
		mIntField.setText(mIntBase);
		createBonusSpinner(v, R.id.int_bonus, mIntBonus, mCharacter.getIntBonus(), attr.Int);
		
		//Wisdom
		TextView mWisField = (TextView)v.findViewById(R.id.wis_base);
		mWisField.setText(mWisBase);
		createBonusSpinner(v, R.id.wis_bonus, mWisBonus, mCharacter.getWisBonus(), attr.Wis);
		
		//Charisma
		TextView mChaField = (TextView)v.findViewById(R.id.cha_base);
		mChaField.setText(mChaBase);
		createBonusSpinner(v, R.id.cha_bonus, mChaBonus, mCharacter.getWisBonus(), attr.Cha);
		
		//Skills
		StringBuilder builder = new StringBuilder();
		for(String s : mSkills) {
		    builder.append(s + "\n");
		}
		
		LinearLayout skills = (LinearLayout)v.findViewById(R.id.skills_layout);
		TextView tv = new TextView(this.getActivity());
		tv.setText(builder.toString());
		skills.addView(tv);
		
		//Favored Card
		TextView favCard = (TextView)v.findViewById(R.id.favored_card);
		favCard.setText(mFavCard);
		
		//HandLimit
		createBonusSpinner(v, R.id.hand_limit, mHandLimit, mCharacter.getHandLimit(), attr.HandLimit);

		//WeaponLimit
		createBonusSpinner(v, R.id.weapons, mWeaponsLimit, mCharacter.getWeapons(), attr.WeaponLimit);

		//ArmorLimit
		createBonusSpinner(v, R.id.armor, mArmorsLimit, mCharacter.getArmors(), attr.ArmorLimit);

		//SpellLimit
		createBonusSpinner(v, R.id.spells, mSpellsLimit, mCharacter.getSpells(), attr.SpellLimit);
		
		//ItemLimit
		createBonusSpinner(v, R.id.items, mItemsLimit, mCharacter.getItems(), attr.ItemLimit);
		
		//AllyLimit
		createBonusSpinner(v, R.id.allies, mAlliesLimit, mCharacter.getAllies(), attr.AllyLimit);
		
		//BlessingLimit
		createBonusSpinner(v, R.id.blessings, mBlessingsLimit, mCharacter.getBlessings(), attr.BlessingLimit);
		
		//Proficiency
		createBonusSpinner(v, R.id.proficieny, mProficiency, mCharacter.getProficieny(), attr.Proficiency);
		
		mNameField.addTextChangedListener(new TextWatcher(){
			
			@Override
			public void onTextChanged(CharSequence c, int start, int before, int count){
				mCharacter.setName(c.toString());
				mCallbacks.onCharacterUpdated(mCharacter);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mPhotoButton = (ImageButton)v.findViewById(R.id.character_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent i = new Intent(getActivity(), CharacterCameraActivity.class);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		}); //mPhotoButton.onClickListener()
		
		//disable button if no camera present
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
				(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && 
						Camera.getNumberOfCameras() > 0);
		
		if(!hasACamera){
			mPhotoButton.setEnabled(false);
		}
		
		mPhotoView = (ImageView)v.findViewById(R.id.character_imageView);
		
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Photo p = mCharacter.getPhoto();
				if(p == null)
					return;
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				
				String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
				ImageFragment.newInstace(path).show(fm, DIALOG_IMAGE);
			}
		});
		
		return v;
	}

	private void showPhoto(){
		//(re)set the image button's image based on our photo
		Photo p = mCharacter.getPhoto();
		BitmapDrawable b = null;
		if(p != null){
			String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	} //showPhoto()
	
	private void setCharacterResourceArrays(View v){
		Log.d(TAG, "Character role found " + mCharacter.getRole());
		switch(mCharacter.role_to_enum()){
		case monk:
			setMonkValues(v);return;
		case paladin:
			setPaladinValues(v);return;
		case barbarian:
			setBarbarianValues(v);return;
		case fighter:
			setFighterValues(v);return;
		case sorceress:
			setSorceressValues(v);return;
		case wizard:
			setWizardValues(v); return;
		case rogue:
			setRogueValues(v);return;
		case cleric:
			setClericValues(v);return;
		case bard:
			setBardValues(v);return;
		case ranger:
			setRangerValues(v);return;
		case druid:
			setDruidValues(v);return;
		case none:
			Log.d(TAG, "none role found");
			return;
		default:
			Log.d(TAG, "no role found in switch");
			return;
		}		
	}
	
	
	@SuppressLint("InlinedApi")
	private void setMonkValues(View v){
		Log.d(TAG, "grab values from resource monk.xml");

		mStrBase = getString(R.string.monk_str);
		mDexBase = getString(R.string.monk_dex);
		mConBase = getString(R.string.monk_con);
		mIntBase = getString(R.string.monk_int);
		mWisBase = getString(R.string.monk_wis);
		mChaBase = getString(R.string.monk_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.monk_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.monk_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.monk_skills);
		
		createPowerSpinner(v, 0, R.array.monk_power1);
		createPowerSpinner(v, 1, R.array.monk_power2);		
	}

	private void setPaladinValues(View v){
		Log.d(TAG, "grab values from resource paladin.xml");
		
		mStrBase = getString(R.string.paladin_str);
		mDexBase = getString(R.string.paladin_dex);
		mConBase = getString(R.string.paladin_con);
		mIntBase = getString(R.string.paladin_int);
		mWisBase = getString(R.string.paladin_wis);
		mChaBase = getString(R.string.paladin_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.paladin_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.paladin_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.paladin_skills);
		
		createPowerSpinner(v, 0, R.array.paladin_power1);
		createPowerSpinner(v, 1, R.array.paladin_power2);	
	}
	
	private void setBarbarianValues(View v){
		Log.d(TAG, "grab values from resource barbarian.xml");
		
		mStrBase = getString(R.string.barbarian_str);
		mDexBase = getString(R.string.barbarian_dex);
		mConBase = getString(R.string.barbarian_con);
		mIntBase = getString(R.string.barbarian_int);
		mWisBase = getString(R.string.barbarian_wis);
		mChaBase = getString(R.string.barbarian_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.barbarian_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.barbarian_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.barbarian_skills);
		
		createPowerSpinner(v, 0, R.array.barbarian_power1);
		createPowerSpinner(v, 1, R.array.barbarian_power2);	
	}
	
	private void setWizardValues(View v){
		Log.d(TAG, "grab values from resource wizard.xml");
		
		mStrBase = getString(R.string.wizard_str);
		mDexBase = getString(R.string.wizard_dex);
		mConBase = getString(R.string.wizard_con);
		mIntBase = getString(R.string.wizard_int);
		mWisBase = getString(R.string.wizard_wis);
		mChaBase = getString(R.string.wizard_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.wizard_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.wizard_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.wizard_skills);
		
		createPowerSpinner(v, 0, R.array.wizard_power1);
		createPowerSpinner(v, 1, R.array.wizard_power2);
		createPowerSpinner(v, 2, R.array.wizard_power3);
	}
	
	private void setFighterValues(View v){
		Log.d(TAG, "grab values from resource fighter.xml");
		
		mStrBase = getString(R.string.fighter_str);
		mDexBase = getString(R.string.fighter_dex);
		mConBase = getString(R.string.fighter_con);
		mIntBase = getString(R.string.fighter_int);
		mWisBase = getString(R.string.fighter_wis);
		mChaBase = getString(R.string.fighter_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.fighter_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.fighter_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.fighter_skills);
		
		createPowerSpinner(v, 0, R.array.fighter_power1);
		createPowerSpinner(v, 1, R.array.fighter_power2);
	}
	
	private void setSorceressValues(View v){
		Log.d(TAG, "grab values from resource sorceress.xml");
		
		mStrBase = getString(R.string.sorceress_str);
		mDexBase = getString(R.string.sorceress_dex);
		mConBase = getString(R.string.sorceress_con);
		mIntBase = getString(R.string.sorceress_int);
		mWisBase = getString(R.string.sorceress_wis);
		mChaBase = getString(R.string.sorceress_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.sorceress_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.sorceress_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.sorceress_skills);
		
		createPowerSpinner(v, 0, R.array.sorceress_power1);
		createPowerSpinner(v, 1, R.array.sorceress_power2);
	}
	
	private void setRogueValues(View v){
		Log.d(TAG, "grab values from resource rogue.xml");
		
		mStrBase = getString(R.string.rogue_str);
		mDexBase = getString(R.string.rogue_dex);
		mConBase = getString(R.string.rogue_con);
		mIntBase = getString(R.string.rogue_int);
		mWisBase = getString(R.string.rogue_wis);
		mChaBase = getString(R.string.rogue_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.rogue_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.rogue_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.rogue_skills);
		
		createPowerSpinner(v, 0, R.array.rogue_power1);
		createPowerSpinner(v, 1, R.array.rogue_power2);
	}
	
	private void setClericValues(View v){
		Log.d(TAG, "grab values from resource cleric.xml");
		
		mStrBase = getString(R.string.cleric_str);
		mDexBase = getString(R.string.cleric_dex);
		mConBase = getString(R.string.cleric_con);
		mIntBase = getString(R.string.cleric_int);
		mWisBase = getString(R.string.cleric_wis);
		mChaBase = getString(R.string.cleric_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.cleric_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.cleric_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.cleric_skills);
		
		createPowerSpinner(v, 0, R.array.cleric_power1);
		createPowerSpinner(v, 1, R.array.cleric_power2);
	}
	
	private void setBardValues(View v){
		Log.d(TAG, "grab values from resource bard.xml");
		
		mStrBase = getString(R.string.bard_str);
		mDexBase = getString(R.string.bard_dex);
		mConBase = getString(R.string.bard_con);
		mIntBase = getString(R.string.bard_int);
		mWisBase = getString(R.string.bard_wis);
		mChaBase = getString(R.string.bard_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.bard_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.bard_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.bard_skills);
		
		createPowerSpinner(v, 0, R.array.bard_power1);
		createPowerSpinner(v, 1, R.array.bard_power2);
	}
	
	private void setRangerValues(View v){
		Log.d(TAG, "grab values from resource ranger.xml");
		
		mStrBase = getString(R.string.ranger_str);
		mDexBase = getString(R.string.ranger_dex);
		mConBase = getString(R.string.ranger_con);
		mIntBase = getString(R.string.ranger_int);
		mWisBase = getString(R.string.ranger_wis);
		mChaBase = getString(R.string.ranger_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.ranger_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.ranger_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.ranger_skills);
		
		createPowerSpinner(v, 0, R.array.ranger_power1);
		createPowerSpinner(v, 1, R.array.ranger_power2);
	}
	
	private void setDruidValues(View v){
		Log.d(TAG, "grab values from resource druid.xml");
		
		mStrBase = getString(R.string.druid_str);
		mDexBase = getString(R.string.druid_dex);
		mConBase = getString(R.string.druid_con);
		mIntBase = getString(R.string.druid_int);
		mWisBase = getString(R.string.druid_wis);
		mChaBase = getString(R.string.druid_cha);
		mFavCard = "Card Feats:         Favored Card: " + getString(R.string.druid_fav_card);

		mStrBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_str_bonus, R.layout.skills_spinner);
		mDexBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_dex_bonus, R.layout.skills_spinner);
		mConBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_con_bonus, R.layout.skills_spinner);
		mIntBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_int_bonus, R.layout.skills_spinner);
		mWisBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_wis_bonus, R.layout.skills_spinner);
		mChaBonus = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_cha_bonus, R.layout.skills_spinner);
		mHandLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_hand_limit, R.layout.skills_spinner);

		mWeaponsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_weapons, R.layout.skills_spinner);
		mArmorsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_armors, R.layout.skills_spinner);
		mSpellsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_spells, R.layout.skills_spinner);
		mItemsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_items, R.layout.skills_spinner);
		mAlliesLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_allies, R.layout.skills_spinner);
		mBlessingsLimit = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_blessings, R.layout.skills_spinner);
		mProficiency = ArrayAdapter.createFromResource(this.getActivity(), R.array.druid_proficiencies, R.layout.skills_spinner);

		mSkills = this.getActivity().getResources().getStringArray(R.array.druid_skills);
		
		createPowerSpinner(v, 0, R.array.druid_power1);
		createPowerSpinner(v, 1, R.array.druid_power2);
	}
	
	private void createBonusSpinner(View v, 
									int layoutSpinnerId, ArrayAdapter<CharSequence> bonusAdapter,
									Integer bonusSelection, final attr attribute){
		
		Spinner mStrSpinner = (Spinner)v.findViewById(layoutSpinnerId);
		mStrSpinner.setAdapter(bonusAdapter);
		mStrSpinner.setSelection(bonusSelection);
		mStrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				switch(attribute){
				case Str:
					mCharacter.setStrBonus(pos);
					return;
				case Dex:
					mCharacter.setDexBonus(pos);
					return;
				case Con:
					mCharacter.setConBonus(pos);
					return;
				case Int:
					mCharacter.setIntBonus(pos);
					return;
				case Wis:
					mCharacter.setWisBonus(pos);
					return;
				case Cha:
					mCharacter.setChaBonus(pos);
					return;
				case HandLimit:
					mCharacter.setHandLimit(pos);
					return;
				case Proficiency:
					mCharacter.setProficieny(pos);
					return;
				default:
					return;
				}					
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void createPowerSpinner(View v, final int powerIndex, int res){
		LinearLayout powers = (LinearLayout)v.findViewById(R.id.powers_layout);
		
		Spinner powersSpinner1 = new Spinner(v.getContext(),Spinner.MODE_DIALOG);
		ArrayAdapter<CharSequence> power1 = ArrayAdapter.createFromResource(this.getActivity(), 
																			res, 
																			R.layout.skills_spinner);
		power1.setDropDownViewResource(R.layout.skills_spinner);
		powersSpinner1.setAdapter(power1);
		powersSpinner1.setSelection(mCharacter.getPowersAt(powerIndex));
		powersSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				mCharacter.setPowersAt(powerIndex, pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		powers.addView(powersSpinner1);
	}
	
}
