package pf.acg.ror;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

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
	private static final String DIALOG_ROLE = "role";
	private static final int REQUEST_ROLE = 0;
	
	private PC mCharacter;
	private EditText mNameField;
	private Callbacks mCallbacks;
	private ImageButton mRoleButton;
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
	private String[] mRoles;
	private TextView mRoleField;
	private View mV;
	
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
		
		if(requestCode == REQUEST_ROLE){
			Integer roleBonus = (Integer)data.getSerializableExtra(RolePickerFragment.EXTRA_ROLE_BONUS);
			mCharacter.setRoleBonus(roleBonus);
			
			LinearLayout powers = (LinearLayout)mV.findViewById(R.id.role_powers_layout);
			powers.removeAllViews();
			setCharacterResourceArrays();
			updateRoleUI();
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
		mV = inflater.inflate(R.layout.character_fragment, parent, false);
		setCharacterResourceArrays();
		
		mNameField = (EditText)mV.findViewById(R.id.pc_name);
		mNameField.setText(mCharacter.getName());
		
		mRoleField = (TextView)mV.findViewById(R.id.pc_role);
		updateRoleUI();
		
		//Strength
		TextView mStrField = (TextView)mV.findViewById(R.id.str_base);
		mStrField.setText(mStrBase);
		createBonusSpinner(R.id.str_bonus, mStrBonus, mCharacter.getStrBonus(), attr.Str);
				
		//Dexterity
		TextView mDexField = (TextView)mV.findViewById(R.id.dex_base);
		mDexField.setText(mDexBase);
		createBonusSpinner(R.id.dex_bonus, mDexBonus, mCharacter.getDexBonus(), attr.Dex);
		
		//Constitution
		TextView mConField = (TextView)mV.findViewById(R.id.con_base);
		mConField.setText(mConBase);
		createBonusSpinner(R.id.con_bonus, mConBonus, mCharacter.getConBonus(), attr.Con);

		//Intelligence
		TextView mIntField = (TextView)mV.findViewById(R.id.int_base);
		mIntField.setText(mIntBase);
		createBonusSpinner(R.id.int_bonus, mIntBonus, mCharacter.getIntBonus(), attr.Int);
		
		//Wisdom
		TextView mWisField = (TextView)mV.findViewById(R.id.wis_base);
		mWisField.setText(mWisBase);
		createBonusSpinner(R.id.wis_bonus, mWisBonus, mCharacter.getWisBonus(), attr.Wis);
		
		//Charisma
		TextView mChaField = (TextView)mV.findViewById(R.id.cha_base);
		mChaField.setText(mChaBase);
		createBonusSpinner(R.id.cha_bonus, mChaBonus, mCharacter.getWisBonus(), attr.Cha);
		
		//Skills
		StringBuilder builder = new StringBuilder();
		for(String s : mSkills) {
		    builder.append(s + "\n");
		}
		
		LinearLayout skills = (LinearLayout)mV.findViewById(R.id.skills_layout);
		TextView tv = new TextView(this.getActivity());
		tv.setText(builder.toString());
		skills.addView(tv);
		
		//Favored Card
		TextView favCard = (TextView)mV.findViewById(R.id.favored_card);
		favCard.setText(mFavCard);
		
		//WeaponLimit
		createBonusSpinner(R.id.weapons, mWeaponsLimit, mCharacter.getWeapons(), attr.WeaponLimit);

		//ArmorLimit
		createBonusSpinner(R.id.armor, mArmorsLimit, mCharacter.getArmors(), attr.ArmorLimit);

		//SpellLimit
		createBonusSpinner(R.id.spells, mSpellsLimit, mCharacter.getSpells(), attr.SpellLimit);
		
		//ItemLimit
		createBonusSpinner(R.id.items, mItemsLimit, mCharacter.getItems(), attr.ItemLimit);
		
		//AllyLimit
		createBonusSpinner(R.id.allies, mAlliesLimit, mCharacter.getAllies(), attr.AllyLimit);
		
		//BlessingLimit
		createBonusSpinner(R.id.blessings, mBlessingsLimit, mCharacter.getBlessings(), attr.BlessingLimit);
		
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
		
		mRoleButton = (ImageButton)mV.findViewById(R.id.character_roleButton);
		mRoleButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				RolePickerFragment dialog = RolePickerFragment.newInstance(mCharacter.getRoleBonus(),
						mRoles);
				dialog.setTargetFragment(CharacterFragment.this, REQUEST_ROLE);
				dialog.show(fm, DIALOG_ROLE);
			}
		});
		
		mPhotoButton = (ImageButton)mV.findViewById(R.id.character_imageButton);
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
		
		mPhotoView = (ImageView)mV.findViewById(R.id.character_imageView);
		
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
		
		return mV;
	}

	private void setCharacterResourceArrays(){
		Log.d(TAG, "Character role found " + mCharacter.getRole());
		switch(mCharacter.role_to_enum()){
		case monk:
			setMonkValues();return;
		case paladin:
			setPaladinValues();return;
		case barbarian:
			setBarbarianValues();return;
		case fighter:
			setFighterValues();return;
		case sorceress:
			setSorceressValues();return;
		case wizard:
			setWizardValues(); return;
		case rogue:
			setRogueValues();return;
		case cleric:
			setClericValues();return;
		case bard:
			setBardValues();return;
		case ranger:
			setRangerValues();return;
		case druid:
			setDruidValues();return;
		case none:
			Log.d(TAG, "none role found");
			return;
		default:
			Log.d(TAG, "no role found in switch");
			return;
		}		
	}

	private void setValuesFromJSON(InputStream role_res_io, String powers_key) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int ctr;
		try {
		    ctr = role_res_io.read();
		    while (ctr != -1) {
		        byteArrayOutputStream.write(ctr);
		        ctr = role_res_io.read();
		    }
		    role_res_io.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Log.v("JSON Data", byteArrayOutputStream.toString());
		try {
		    // Parse the data into jsonobject to get original data in form of json.
		    JSONObject json = new JSONObject(
		            byteArrayOutputStream.toString());
		    mStrBase = json.getString("str");
		    mDexBase = json.getString("dex");
		    mConBase = json.getString("con");
		    mIntBase = json.getString("int");
		    mWisBase = json.getString("wis");
		    mChaBase = json.getString("cha");
			mFavCard = "Card Feats:         Favored Card: " + json.getString("fav_card");

		    mRoles = JSONArray2Array((JSONArray) json.get("role_bonus"));
		    mSkills = JSONArray2Array((JSONArray) json.get("skills"));

		    
		    mStrBonus = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("str_bonus")));
		    
		    mDexBonus = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("dex_bonus")));
		    
		    mConBonus = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner,
		    		JSONArray2List((JSONArray)json.get("con_bonus")));
		    
		    mIntBonus = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner,
		    		JSONArray2List((JSONArray)json.get("int_bonus")));
		    
		    mWisBonus = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("wis_bonus")));
		    
		    mChaBonus = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("cha_bonus")));
		    
		    mWeaponsLimit = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("weapons")));
		    
		    mArmorsLimit = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("armors")));
		    
		    mSpellsLimit = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("spells")));
		    
		    mItemsLimit = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("items")));
		    
		    mAlliesLimit = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("allies")));
		    
		    mBlessingsLimit = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("blessings")));
		    		    
			mHandLimit = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner,
					JSONArray2List((json.getJSONArray("hand_limit").
							getJSONArray(mCharacter.getRoleBonus()))));
			
			mProficiency = new ArrayAdapter<CharSequence>(this.getActivity(), R.layout.skills_spinner,
					JSONArray2List(json.getJSONArray("proficiencies").
							getJSONArray(mCharacter.getRoleBonus())));

			JSONArray json_powers = json.getJSONObject("powers").getJSONArray(powers_key);
			for(int i = 0; i < json_powers.length(); i++){
				ArrayAdapter<CharSequence> power_adapter = new ArrayAdapter<CharSequence>(this.getActivity(),
						R.layout.skills_spinner,
						JSONArray2List(json_powers.getJSONArray(i)));
				createPowerSpinner(mV,i,power_adapter);
			}

		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	private void setBarbarianValues(){
		Log.d(TAG, "grab values from resource barbarian_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.barbarian_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "barbarian");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "berserker");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "juggernaught");
			return;
		}	
	}
	
	private void setBardValues(){
		Log.d(TAG, "grab values from resource bard_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.bard_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "bard");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "virtuoso");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "charlatan");
			return;
		}
	}

	private void setClericValues(){
		Log.d(TAG, "grab values from resource cleric_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.cleric_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "cleric");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "healer");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "exorcist");
			return;
		}
	}

	private void setDruidValues(){
		Log.d(TAG, "grab values from resource druid_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.druid_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "druid");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "shapeshifter");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "wild_warden");
			return;
		}
	}

	private void setFighterValues(){
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
		
		createPowerSpinner(mV, 0, R.array.fighter_power1);
		createPowerSpinner(mV, 1, R.array.fighter_power2);
	}
	
	@SuppressLint("InlinedApi")
	private void setMonkValues(){
		Log.d(TAG, "grab values from resource monk_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.monk_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "monk");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "zen_archer");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "drunken_master");
			return;
		}
	}

	private void setPaladinValues(){
		Log.d(TAG, "grab values from resource paladin_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.palladin_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "palladin");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "crusaider");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "hopitaler");
			return;
		}	
	}

	private void setRangerValues(){
		Log.d(TAG, "grab values from resource ranger_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.ranger_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "ranger");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "sniper");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "tracker");
			return;
		}
	}

	private void setRogueValues(){
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
		
		createPowerSpinner(mV, 0, R.array.rogue_power1);
		createPowerSpinner(mV, 1, R.array.rogue_power2);
	}
	
	private void setSorceressValues(){
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
		
		createPowerSpinner(mV, 0, R.array.sorceress_power1);
		createPowerSpinner(mV, 1, R.array.sorceress_power2);
	}

	private void setWizardValues(){
		Log.d(TAG, "grab values from resource wizard_ror.json");
		InputStream role_res_io = this.getActivity().getResources().openRawResource(R.raw.wizard_ror);
		switch(mCharacter.getRoleBonus()){
		case 0:
			setValuesFromJSON(role_res_io, "wizard");
			return;
		case 1:
			setValuesFromJSON(role_res_io, "evoker");
			return;
		case 2:
			setValuesFromJSON(role_res_io, "illusionist");
			return;
		}
	}

	private void updateRoleUI(){
		mRoleField.setText(mRoles[mCharacter.getRoleBonus()]);
		
		//HandLimit
		createBonusSpinner(R.id.hand_limit, mHandLimit, mCharacter.getHandLimit(), attr.HandLimit);
		
		//Proficiency
		createBonusSpinner(R.id.proficieny, mProficiency, mCharacter.getProficieny(), attr.Proficiency);

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

	private String[] JSONArray2Array(JSONArray json) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < json.length(); i++) {
	        list.add((String) json.opt(i));
	    }
		String[] a = new String[list.size()];
		return list.toArray(a);
	}

	private ArrayList<CharSequence> JSONArray2List(JSONArray json_array) {
	    ArrayList<CharSequence> list = new ArrayList<CharSequence>();
		for (int i = 0; i < json_array.length(); i++) {
			list.add( (String) json_array.opt(i) );
	    }
		return list;
	}

	private void createBonusSpinner(
									int layoutSpinnerId, ArrayAdapter<CharSequence> bonusAdapter,
									Integer bonusSelection, final attr attribute){
		
		Spinner mStrSpinner = (Spinner)mV.findViewById(layoutSpinnerId);
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
	
	private void createPowerSpinner(View v, final int powerIndex, ArrayAdapter<CharSequence> powerAdapter){
		LinearLayout powers = (LinearLayout)v.findViewById(R.id.role_powers_layout);
		
		Spinner powersSpinner1 = new Spinner(v.getContext(),Spinner.MODE_DIALOG);
	
		powersSpinner1.setAdapter(powerAdapter);
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
