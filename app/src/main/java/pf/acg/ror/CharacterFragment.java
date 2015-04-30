package pf.acg.ror;

import java.util.UUID;

import org.json.JSONException;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class CharacterFragment extends Fragment {

	public static final String EXTRA_CHARACTER_ID =  "pf.acg.ror";
	private static final String TAG = "CharacterFragment";
	private static final int REQUEST_PHOTO = 1;
	private static final String DIALOG_IMAGE = "image";
	private static final String DIALOG_ROLE = "role";
	private static final int REQUEST_ROLE = 0;
	
	private PC mCharacter;
	private CharacterHelper mCH;
	private EditText mNameField;
	private Callbacks mCallbacks;
	private ImageButton mRoleButton;
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	
	private TextView mRoleField;
	private View mV;
	
	private enum attr{Str, Dex, Con, Int, Wis, Cha, 
		HandLimit, WeaponLimit, ArmorLimit, SpellLimit, ItemLimit, AllyLimit, BlessingLimit, Proficiency; }
    private enum adv{ror, sos;}

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
		//Log.d("date_select", String.valueOf(Activity.RESULT_OK));
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
				//Log.i(TAG, "Crime: " + mCharacter.getName() +"  has photo: " + filename);
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
		mStrField.setText(mCH.getStrBase());
		createBonusSpinner(R.id.str_bonus, mCH.getStrBonus(), mCharacter.getStrBonus(), attr.Str);
				
		//Dexterity
		TextView mDexField = (TextView)mV.findViewById(R.id.dex_base);
		mDexField.setText(mCH.getDexBase());
		createBonusSpinner(R.id.dex_bonus, mCH.getDexBonus(), mCharacter.getDexBonus(), attr.Dex);
		
		//Constitution
		TextView mConField = (TextView)mV.findViewById(R.id.con_base);
		mConField.setText(mCH.getConBase());
		createBonusSpinner(R.id.con_bonus, mCH.getConBonus(), mCharacter.getConBonus(), attr.Con);

		//Intelligence
		TextView mIntField = (TextView)mV.findViewById(R.id.int_base);
		mIntField.setText(mCH.getIntBase());
		createBonusSpinner(R.id.int_bonus, mCH.getIntBonus(), mCharacter.getIntBonus(), attr.Int);
		
		//Wisdom
		TextView mWisField = (TextView)mV.findViewById(R.id.wis_base);
		mWisField.setText(mCH.getWisBase());
		createBonusSpinner(R.id.wis_bonus, mCH.getWisBonus(), mCharacter.getWisBonus(), attr.Wis);
		
		//Charisma
		TextView mChaField = (TextView)mV.findViewById(R.id.cha_base);
		mChaField.setText(mCH.getChaBase());
		createBonusSpinner(R.id.cha_bonus, mCH.getChaBonus(), mCharacter.getWisBonus(), attr.Cha);
		
		//Skills
		StringBuilder builder = new StringBuilder();
		for(String s : mCH.getSkills()) {
		    builder.append(s + "\n");
		}
		
		RelativeLayout skills = (RelativeLayout)mV.findViewById(R.id.skills_layout);
		TextView tv = new TextView(this.getActivity());
		tv.setText(builder.toString());
		tv.setTextSize(18);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, R.id.skills_table);
		skills.addView(tv,lp);
		
		//Favored Card
		TextView favCard = (TextView)mV.findViewById(R.id.favored_card);
		favCard.setText(mCH.getFavCard());
		
		//WeaponLimit
		createBonusSpinner(R.id.weapons, mCH.getWeaponsLimit(), mCharacter.getWeapons(), attr.WeaponLimit);

		//ArmorLimit
		createBonusSpinner(R.id.armor, mCH.getArmorsLimit(), mCharacter.getArmors(), attr.ArmorLimit);

		//SpellLimit
		createBonusSpinner(R.id.spells, mCH.getSpellsLimit(), mCharacter.getSpells(), attr.SpellLimit);
		
		//ItemLimit
		createBonusSpinner(R.id.items, mCH.getItemsLimit(), mCharacter.getItems(), attr.ItemLimit);
		
		//AllyLimit
		createBonusSpinner(R.id.allies, mCH.getAlliesLimit(), mCharacter.getAllies(), attr.AllyLimit);
		
		//BlessingLimit
		createBonusSpinner(R.id.blessings, mCH.getBlessingsLimit(), mCharacter.getBlessings(), attr.BlessingLimit);

        //Progress Update

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
						mCH.getRoles());
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
		//Log.d(TAG, "Character role found " + mCharacter.getRole());
		mCH = new CharacterHelper();
		mCH.setCharacterResourceArrays(getActivity(), mCharacter.role_to_enum(), mCharacter.getRoleBonus());		
	}

	

	private void updateRoleUI(){
		mRoleField.setText(mCH.getRoles()[mCharacter.getRoleBonus()]);
		mCallbacks.onCharacterUpdated(mCharacter);

		
		//HandLimit
		createBonusSpinner(R.id.hand_limit, mCH.getHandLimit(), mCharacter.getHandLimit(), attr.HandLimit);
		
		//Proficiency
		createBonusSpinner(R.id.proficieny, mCH.getProficiency(), mCharacter.getProficieny(), attr.Proficiency);

		addPowerSpinners();

        createProgressSpinner(mCH.getRorAdventures(),R.id.ror_progress_spinner, mCharacter.getRoRProgress(), adv.ror);
        createProgressSpinner(mCH.getSosAdventures(),R.id.sos_progress_spinner, mCharacter.getSoSProgress(), adv.sos);
		
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

    private void createProgressSpinner(ArrayAdapter<CharSequence> progressAdapter, int layoutSpinnerId,
                                       Integer progessSelection, final adv adventure) {
        Spinner spinner = (Spinner)mV.findViewById(layoutSpinnerId);
        spinner.setAdapter(progressAdapter);
        spinner.setSelection(progessSelection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch(adventure){
                    case ror:
                        mCharacter.setRoRProgress(pos);
                        return;
                    case sos:
                        mCharacter.setSoSProgress(pos);
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
	private void createBonusSpinner(
									int layoutSpinnerId, ArrayAdapter<CharSequence> bonusAdapter,
									Integer bonusSelection, final attr attribute){
		
		Spinner spinner = (Spinner)mV.findViewById(layoutSpinnerId);
		spinner.setAdapter(bonusAdapter);
        spinner.setSelection(bonusSelection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
		LinearLayout powers = (LinearLayout)v.findViewById(R.id.role_powers_layout);
		
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
	
	private void addPowerSpinners(){
		//Powers
		for(int i = 0; i < mCH.getPowers().length(); i++){
			ArrayAdapter<CharSequence> power_adapter;
			try {
				power_adapter = new ArrayAdapter<CharSequence>(getActivity(),
						R.layout.skills_spinner,
						mCH.getPowersList(i));
				createPowerSpinner(mV,i,power_adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
