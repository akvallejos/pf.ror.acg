package pf.acg.ror;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pf.acg.ror.PC.valid_roles;

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

    private ArrayAdapter<CharSequence> mRorAdventures;
    private ArrayAdapter<CharSequence> mSosAdventures;
	private ArrayAdapter<CharSequence> mWorAdventures;

	private JSONArray mPowers;

	private String[] mSkills;
	private String[] mRoles;
	private String[] mStrSkills;
	private String[] mDexSkills;
	private String[] mConSkills;
	private String[] mIntSkills;
	private String[] mWisSkills;
	private String[] mChaSkills;


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

    public ArrayAdapter<CharSequence> getRorAdventures(){ return mRorAdventures; }

    public ArrayAdapter<CharSequence> getSosAdventures(){ return mSosAdventures; }

	public ArrayAdapter<CharSequence> getWorAdventures(){ return mWorAdventures; }

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

	public String[] getStrSkills() {return mStrSkills; }

	public String[] getDexSkills() {return mDexSkills; }

	public String[] getConSkills() {return mConSkills; }

	public String[] getIntSkills() {return mIntSkills; }

	public String[] getWisSkills() {return mWisSkills; }

	public String[] getChaSkills() {return mChaSkills; }

	public ArrayList<CharSequence> getPowersList(int i) throws JSONException{
		return JSONArray2List(getPowers().getJSONArray(i));
	}


	public String[] getSkills() {
		return mSkills;
	}


	public String[] getRoles() {
		return mRoles;
	}

    public void setCharacterResourceArrays(Activity activity, PC character){
        //Log.d(TAG, "Character role found " + mCharacter.getRole());

        mInputStream = activity.getResources().openRawResource(
                activity.getResources().getIdentifier(character.getRole(), "raw", activity.getPackageName()));

        setValuesFromJSON(activity,mInputStream, character.getRoleBonus());
        InputStream rorProgressIO = activity.getResources().openRawResource(R.raw.adventure_ror);
        mRorAdventures = setProgressList(activity, rorProgressIO);

        InputStream sosProgressIO = activity.getResources().openRawResource(R.raw.adventure_ss);
        mSosAdventures = setProgressList(activity, sosProgressIO);

		InputStream worProgressIO = activity.getResources().openRawResource(R.raw.adventure_wr);
		mWorAdventures = setProgressList(activity, worProgressIO);

        try {
            mInputStream.close();
            rorProgressIO.close();
            sosProgressIO.close();
        } catch (IOException e) {
            //Log.d(TAG, e.printStackTrace());
        }
    }


    private ArrayAdapter<CharSequence> setProgressList(Activity activity, InputStream progress_io){
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
            ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.skills_spinner,
                    JSONArray2List((JSONArray)json.get("adventures")));
            return arrayAdapter;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            //mSkills = JSONArray2Array((JSONArray) json.get("skills"));

			mStrSkills = JSONArray2Array((JSONArray) ((JSONObject) json.get("skills")).get("str"));
			mDexSkills = JSONArray2Array((JSONArray) ((JSONObject) json.get("skills")).get("dex"));
			mConSkills = JSONArray2Array((JSONArray) ((JSONObject) json.get("skills")).get("con"));
			mIntSkills = JSONArray2Array((JSONArray) ((JSONObject) json.get("skills")).get("int"));
			mWisSkills = JSONArray2Array((JSONArray) ((JSONObject) json.get("skills")).get("wis"));
			mChaSkills = JSONArray2Array((JSONArray) ((JSONObject) json.get("skills")).get("cha"));



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
