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
import android.util.Log;
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

    private ArrayAdapter<CharSequence> mRorAdventures;
	
	private JSONArray mPowers;
	
	private String[] mSkills;
	private String[] mRoles;

    private InputStream mInputStream;
	
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

    public ArrayAdapter<CharSequence> getRorAdventures(){return mRorAdventures;}

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
            mInputStream = activity.getResources().openRawResource(R.raw.monk_ror);
            break;
		case paladin:
            mInputStream = activity.getResources().openRawResource(R.raw.palladin_ror);
            break;
		case barbarian:
            mInputStream = activity.getResources().openRawResource(R.raw.barbarian_ror);
            break;
        case fighter:
            mInputStream = activity.getResources().openRawResource(R.raw.fighter_ror);
            break;
		case sorceress:
            mInputStream = activity.getResources().openRawResource(R.raw.sorceress_ror);
            break;
		case wizard:
            mInputStream = activity.getResources().openRawResource(R.raw.wizard_ror);
            break;
		case rogue:
            mInputStream = activity.getResources().openRawResource(R.raw.rogue_ror);
            break;
		case cleric:
            mInputStream = activity.getResources().openRawResource(R.raw.cleric_ror);
            break;
		case bard:
            mInputStream = activity.getResources().openRawResource(R.raw.bard_ror);
            break;
		case ranger:
            mInputStream = activity.getResources().openRawResource(R.raw.ranger_ror);
            break;
		case druid:
            mInputStream = activity.getResources().openRawResource(R.raw.druid_ror);
            break;
		case raider_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.raider_sas);
            break;
		case oracle_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.oracle_sas);
            break;
		case swashbuckler_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.swashbucker_sas);
            break;
		case bard_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.bard_sas);
            break;
		case gunslinger_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.gunslinger_sas);
            break;
		case rogue_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.rogue_sas);
            break;
		case magus_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.magus_sas);
            break;
		case fighter_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.fighter_sas);
            break;
		case alchemist_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.alchemist_sas);
            break;
		case druid_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.druid_sas);
            break;
		case warpriest_ss:
            mInputStream = activity.getResources().openRawResource(R.raw.warpriest_sas);
            break;
		case none:
			//Log.d(TAG, "none role found");
			return;
		default:
			//Log.d(TAG, "no role found in switch");
			return;
		}
        setValuesFromJSON(activity,mInputStream, roleBonus);
        InputStream rorProgressIO = activity.getResources().openRawResource(R.raw.adventure_ror);
        setProgressList(activity, rorProgressIO);

        try {
            mInputStream.close();
            rorProgressIO.close();
        } catch (IOException e) {
            //Log.d(TAG, e.printStackTrace());
        }
    }

    private void setProgressList(Activity activity, InputStream progress_io){
        // Parse the data into jsonobject to get original data in form of json.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = progress_io.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = progress_io.read();
            }
            progress_io.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(byteArrayOutputStream.toString());
            mRorAdventures = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner,
                    JSONArray2List((JSONArray)json.get("adventures")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setValuesFromJSON(Activity activity, InputStream role_res_io, int role_bonus) {
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
            String[] roleBonusKeys = JSONArray2Array((JSONArray) json.get("role_bonus_keys"));
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

            mPowers = json.getJSONObject("powers").getJSONArray(roleBonusKeys[role_bonus]);

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
}
