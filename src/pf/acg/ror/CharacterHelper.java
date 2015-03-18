package pf.acg.ror;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pf.acg.ror.PC.valid_roles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.ArrayAdapter;

public class CharacterHelper {

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
	
	private JSONArray mPowers;
	
	private String[] mSkills;
	private String[] mRoles;
	
	//private PC mCharacter;
	
	public CharacterHelper(){}
	

	public String getStrBase() {
		return mStrBase;
	}


	public String getDexBase() {
		return mDexBase;
	}


	public String getConBase() {
		return mConBase;
	}


	public String getIntBase() {
		return mIntBase;
	}


	public String getWisBase() {
		return mWisBase;
	}


	public String getChaBase() {
		return mChaBase;
	}


	public String getFavCard() {
		return mFavCard;
	}


	public ArrayAdapter<CharSequence> getStrBonus() {
		return mStrBonus;
	}


	public ArrayAdapter<CharSequence> getDexBonus() {
		return mDexBonus;
	}


	public ArrayAdapter<CharSequence> getConBonus() {
		return mConBonus;
	}


	public ArrayAdapter<CharSequence> getIntBonus() {
		return mIntBonus;
	}


	public ArrayAdapter<CharSequence> getWisBonus() {
		return mWisBonus;
	}


	public ArrayAdapter<CharSequence> getChaBonus() {
		return mChaBonus;
	}


	public ArrayAdapter<CharSequence> getHandLimit() {
		return mHandLimit;
	}


	public ArrayAdapter<CharSequence> getProficiency() {
		return mProficiency;
	}


	public ArrayAdapter<CharSequence> getWeaponsLimit() {
		return mWeaponsLimit;
	}


	public ArrayAdapter<CharSequence> getArmorsLimit() {
		return mArmorsLimit;
	}


	public ArrayAdapter<CharSequence> getSpellsLimit() {
		return mSpellsLimit;
	}


	public ArrayAdapter<CharSequence> getItemsLimit() {
		return mItemsLimit;
	}


	public ArrayAdapter<CharSequence> getAlliesLimit() {
		return mAlliesLimit;
	}


	public ArrayAdapter<CharSequence> getBlessingsLimit() {
		return mBlessingsLimit;
	}


	public JSONArray getPowers() {
		return mPowers;
	}
	
	public ArrayList<CharSequence> getPowersList(int i) throws JSONException{
		return JSONArray2List(getPowers().getJSONArray(i));
	}


	public String[] getSkills() {
		return mSkills;
	}


	public String[] getRoles() {
		return mRoles;
	}



	public void setCharacterResourceArrays(Activity activity, valid_roles role, int roleBonus){
		//Log.d(TAG, "Character role found " + mCharacter.getRole());
		switch(role){
		case monk:
			setMonkValues(activity, roleBonus);return;
		case paladin:
			setPaladinValues(activity, roleBonus);return;
		case barbarian:
			setBarbarianValues(activity, roleBonus);return;
		case fighter:
			setFighterValues(activity, roleBonus);return;
		case sorceress:
			setSorceressValues(activity, roleBonus);return;
		case wizard:
			setWizardValues(activity, roleBonus); return;
		case rogue:
			setRogueValues(activity, roleBonus);return;
		case cleric:
			setClericValues(activity, roleBonus);return;
		case bard:
			setBardValues(activity, roleBonus);return;
		case ranger:
			setRangerValues(activity, roleBonus);return;
		case druid:
			setDruidValues(activity, roleBonus);return;
		case raider_ss:
			setRaiderValues(activity, roleBonus);return;
		case oracle_ss:
			setOracleValues(activity, roleBonus);return;
		case none:
			//Log.d(TAG, "none role found");
			return;
		default:
			//Log.d(TAG, "no role found in switch");
			return;
		}		
	}

	private void setValuesFromJSON(Activity activity, InputStream role_res_io, String powers_key, int role_bonus) {
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
		//Log.v("JSON Data", byteArrayOutputStream.toString());
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

		    
		    mStrBonus = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("str_bonus")));
		    
		    mDexBonus = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("dex_bonus")));
		    
		    mConBonus = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner,
		    		JSONArray2List((JSONArray)json.get("con_bonus")));
		    
		    mIntBonus = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner,
		    		JSONArray2List((JSONArray)json.get("int_bonus")));
		    
		    mWisBonus = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("wis_bonus")));
		    
		    mChaBonus = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("cha_bonus")));
		    
		    mWeaponsLimit = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("weapons")));
		    
		    mArmorsLimit = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("armors")));
		    
		    mSpellsLimit = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("spells")));
		    
		    mItemsLimit = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("items")));
		    
		    mAlliesLimit = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("allies")));
		    
		    mBlessingsLimit = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner, 
		    		JSONArray2List((JSONArray)json.get("blessings")));
		    		    
			mHandLimit = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner,
					JSONArray2List((json.getJSONArray("hand_limit").
							getJSONArray(role_bonus))));
			
			mProficiency = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner,
					JSONArray2List(json.getJSONArray("proficiencies").
							getJSONArray(role_bonus)));

			mPowers = json.getJSONObject("powers").getJSONArray(powers_key);

		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

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

	private void setBarbarianValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource barbarian_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.barbarian_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "barbarian", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "berserker", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "juggernaught", role_bonus);
			return;
		}	
	}
	
	private void setBardValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource bard_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.bard_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "bard", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "virtuoso", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "charlatan", role_bonus);
			return;
		}
	}

	private void setClericValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource cleric_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.cleric_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "cleric", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "healer", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "exorcist", role_bonus);
			return;
		}
	}

	private void setDruidValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource druid_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.druid_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "druid", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "shapeshifter", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "wild_warden", role_bonus);
			return;
		}
	}

	private void setFighterValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource fighter_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.fighter_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "fighter", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "guardian", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "weapon_master", role_bonus);
			return;
		}
	}
	
	@SuppressLint("InlinedApi")
	private void setMonkValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource monk_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.monk_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "monk", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "zen_archer", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "drunken_master", role_bonus);
			return;
		}
	}

	private void setPaladinValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource paladin_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.palladin_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "palladin", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "crusaider", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "hopitaler", role_bonus);
			return;
		}	
	}

	private void setRangerValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource ranger_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.ranger_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "ranger", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "sniper", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "tracker", role_bonus);
			return;
		}
	}

	private void setRogueValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource rogue_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.rogue_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "rogue", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "acrobat", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "thief", role_bonus);
			return;
		}
	}
	
	private void setSorceressValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource sorceress_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.sorceress_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "sorceress", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "abyssal_sorcerer", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "celestial_sorcerer", role_bonus);
			return;
		}
	}

	private void setWizardValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource wizard_ror.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.wizard_ror);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "wizard", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "evoker", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "illusionist", role_bonus);
			return;
		}
	}
	
	private void setRaiderValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource raider_ss.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.raider_ss);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "raider", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "kleptomaniac", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "wrecker", role_bonus);
			return;
		}
	}
	
	private void setOracleValues(Activity activity, int role_bonus){
		//Log.d(TAG, "grab values from resource raider_ss.json");
		InputStream role_res_io = activity.getResources().openRawResource(R.raw.oracle_ss);
		switch(role_bonus){
		case 0:
			setValuesFromJSON(activity, role_res_io, "oracle", role_bonus);
			return;
		case 1:
			setValuesFromJSON(activity, role_res_io, "stargazer", role_bonus);
			return;
		case 2:
			setValuesFromJSON(activity, role_res_io, "tempest", role_bonus);
			return;
		}
	}
}
