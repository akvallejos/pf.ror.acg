package pf.acg.ror;

import java.util.Arrays;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PC {
	private static final String JSON_ID = "id";
	private static final String JSON_NAME = "name";
	private static final String JSON_ROLE = "role";
	private static final String JSON_ROLE_BONUS = "role_bonus";
	private static final String JSON_STR_BONUS = "str";
	private static final String JSON_DEX_BONUS = "dex";
	private static final String JSON_CON_BONUS = "con";
	private static final String JSON_INT_BONUS = "int";
	private static final String JSON_WIS_BONUS = "wis";
	private static final String JSON_CHA_BONUS = "cha";
	private static final String JSON_HAND_LIMIT = "hand_limit";
	private static final String JSON_ARMORS = "armors";
	private static final String JSON_WEAPONS = "weapons";
	private static final String JSON_SPELLS = "spells";
	private static final String JSON_ITEMS = "items";
	private static final String JSON_ALLIES = "allies";
	private static final String JSON_BLESSINGS = "blessings";
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_POWERS_ARRAY = "powers";
	private static final String JSON_PROFICIENCY = "proficiency";
	private static final String JSON_ROR_PROGRESS = "ror_progress";
	private	static final String JSON_SOS_PROGRESS = "sos_progress";
	private static final String JSON_WOR_PROGRESS = "wor_progress";
	private static final int POWERS_ARRAY_SIZE = 16;

	private static final String TAG = "PC.class";

	
	private UUID mId;
	private String mName;
	private String mRole;
	private Photo mPhoto;
	
	private Integer mStrBonus = 0;
	private Integer mDexBonus = 0;
	private Integer mConBonus = 0;
	private Integer mIntBonus = 0;
	private Integer mWisBonus = 0;
	private Integer mChaBonus = 0;
	
	private Integer mRoleBonus = 0;
	
	private Integer mHandLimit = 0;
	
	private Integer mArmors = 0;
	private Integer mWeapons = 0;
	private Integer mSpells = 0;
	private Integer mItems = 0;
	private Integer mAllies = 0;
	private Integer mBlessings = 0;
	
	private Integer mProficieny = 0;

	private Integer[] mPowersArray = new Integer[POWERS_ARRAY_SIZE];

    private Integer mRoRProgress = 0;
    private Integer mSoSProgress = 0;
	private Integer mWoRProgress = 0;
	
	// Used to convert String role value into enum for use in switch statements
	public enum valid_roles{
		cavalier_wr, summoner_wr, arcanist_wr, ranger_wr, inquisitioner_wr, cleric_wr,
		paladin_wr, hunter_wr, bloodrager_wr, sorceress_wr, shaman_wr,
		raider_ss, oracle_ss, swashbuckler_ss, bard_ss, gunslinger_ss, rogue_ss, magus_ss, fighter_ss,
		alchemist_ss, witch_ss, druid_ss, warpriest_ss,
		bekah_cd, lem_cd, meliski_cd, siwar_cd,
		flenta_cd, tontelizi_cd, valeros_cd, vika_cd,
		koren_cd, raz_cd, seelah_cd,
		agna_cd, arabundi_cd, harsk_cd, wrathack_cd,
		lesath_cd, merisiel_cd, olenjack_cd, wu_shen_cd,
		oracle_mm,
		monk, paladin, fighter, sorceress, wizard, rogue, cleric, bard, ranger, druid, barbarian, none;
	
		public static valid_roles role_to_enum(String lRole) {
		    for (valid_roles r : valid_roles.values()) {
				//Log.d(TAG, r.name());
		        if (r.name().equals(lRole)) {
		            return r;
		        }
		    }
			return none;
		}
	};
	
	public PC(String role){
		//Generate a unique identifier
		mId = UUID.randomUUID();
		mRole = role;
		mName = "Character";
	}
	
	public valid_roles role_to_enum(){
		return valid_roles.role_to_enum(mRole);
	}
	
	public PC(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if(json.has(JSON_NAME)){
			mName = json.getString(JSON_NAME);
		}
		
		mRole = json.getString(JSON_ROLE);
		mRoleBonus = json.getInt(JSON_ROLE_BONUS);
		
		mStrBonus = json.getInt(JSON_STR_BONUS);
		mDexBonus = json.getInt(JSON_DEX_BONUS);
		mConBonus = json.getInt(JSON_CON_BONUS);
		mIntBonus = json.getInt(JSON_INT_BONUS);
		mWisBonus = json.getInt(JSON_WIS_BONUS);
		mChaBonus = json.getInt(JSON_CHA_BONUS);

		mHandLimit = json.getInt(JSON_HAND_LIMIT);

		mArmors = json.getInt(JSON_ARMORS);
		mWeapons = json.getInt(JSON_WEAPONS);
		mSpells = json.getInt(JSON_SPELLS);
		mItems = json.getInt(JSON_ITEMS);
		mAllies = json.getInt(JSON_ALLIES);
		mBlessings = json.getInt(JSON_BLESSINGS);

		mProficieny = json.getInt(JSON_PROFICIENCY);

		mRoRProgress = json.getInt(JSON_ROR_PROGRESS);
		mWoRProgress = json.getInt(JSON_WOR_PROGRESS);
		mSoSProgress = json.getInt(JSON_SOS_PROGRESS);
		
		JSONArray powers = (JSONArray) json.get(JSON_POWERS_ARRAY);
		for(int i = 0; i < powers.length(); i++){
			mPowersArray[i] = powers.optInt(i, 0);
		}

		if(json.has(JSON_PHOTO))
			mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId);
		json.put(JSON_NAME, mName);
		json.put(JSON_ROLE, mRole);
		json.put(JSON_ROLE_BONUS, mRoleBonus);
		
		json.put(JSON_STR_BONUS, mStrBonus);
		json.put(JSON_DEX_BONUS, mDexBonus);
		json.put(JSON_CON_BONUS, mConBonus);
		json.put(JSON_INT_BONUS, mIntBonus);
		json.put(JSON_WIS_BONUS, mWisBonus);
		json.put(JSON_CHA_BONUS, mChaBonus);
		
		json.put(JSON_HAND_LIMIT, mHandLimit);

		json.put(JSON_ARMORS, mArmors);
		json.put(JSON_WEAPONS, mWeapons);
		json.put(JSON_SPELLS, mSpells);
		json.put(JSON_ITEMS, mItems);
		json.put(JSON_ALLIES, mAllies);
		json.put(JSON_BLESSINGS, mBlessings);

		json.put(JSON_PROFICIENCY, mProficieny);

		json.put(JSON_ROR_PROGRESS, mRoRProgress);
		json.put(JSON_SOS_PROGRESS, mSoSProgress);
		json.put(JSON_WOR_PROGRESS, mWoRProgress);
		
		json.put(JSON_POWERS_ARRAY, new JSONArray(Arrays.asList(mPowersArray)));

		if(mPhoto != null)
			json.put(JSON_PHOTO, mPhoto.toJSON());
		
		return json;
	}
	
	public UUID getId() {
		return mId;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getRole() {
		return mRole;
	}
	
	public Integer getRoleBonus() {
		return mRoleBonus;
	}
	
	public void setRoleBonus(Integer roleBonus) {
		mRoleBonus = roleBonus;
	}
	
	public Integer getStrBonus() {
		return mStrBonus;
	}
	
	public void setStrBonus(Integer strBonus) {
		mStrBonus = strBonus;
	}
	
	public Integer getDexBonus() {
		return mDexBonus;
	}
	
	public void setDexBonus(Integer dexBonus) {
		mDexBonus = dexBonus;
	}
	
	public Integer getConBonus() {
		return mConBonus;
	}
	
	public void setConBonus(Integer conBonus) {
		mConBonus = conBonus;
	}
	
	public Integer getIntBonus() {
		return mIntBonus;
	}
	
	public void setIntBonus(Integer intBonus) {
		mIntBonus = intBonus;
	}
	
	public Integer getWisBonus() {
		return mWisBonus;
	}
	
	public void setWisBonus(Integer wisBonus) {
		mWisBonus = wisBonus;
	}
	
	public Integer getChaBonus() {
		return mChaBonus;
	}
	
	public void setChaBonus(Integer chaBonus) {
		mChaBonus = chaBonus;
	}
	
	public Integer getHandLimit() {
		return mHandLimit;
	}
	
	public void setHandLimit(Integer handLimit) {
		mHandLimit = handLimit;
	}
	
	public Integer getArmors() {
		return mArmors;
	}
	
	public void setArmors(Integer armors) {
		mArmors = armors;
	}
	
	public Integer getWeapons() {
		return mWeapons;
	}
	
	public void setWeapons(Integer weapons) {
		mWeapons = weapons;
	}
	
	public Integer getSpells() {
		return mSpells;
	}
	
	public void setSpells(Integer spells) {
		mSpells = spells;
	}
	
	public Integer getItems() {
		return mItems;
	}
	
	public void setItems(Integer items) {
		mItems = items;
	}
	
	public Integer getAllies() {
		return mAllies;
	}
	
	public void setAllies(Integer allies) {
		mAllies = allies;
	}
	
	public Integer getBlessings() {
		return mBlessings;
	}
	
	public void setBlessings(Integer blessings) {
		mBlessings = blessings;
	}

	public int getPowersAt(int i) {
		if(mPowersArray == null || mPowersArray[i] == null)
			return 0;
		return mPowersArray[i];
	}

	public void setPowersAt(int index, int value) {
		mPowersArray[index] = value;
	}

	public Integer getProficieny() {
		return mProficieny;
	}

	public void setProficieny(Integer proficieny) {
		mProficieny = proficieny;
	}

	public Photo getPhoto(){
		return mPhoto;
	}
	
	public void setPhoto(Photo p){
		mPhoto = p;
	}

    public Integer getRoRProgress() {
        return mRoRProgress;
    }

    public void setRoRProgress(Integer RoRProgress) {
        mRoRProgress = RoRProgress;
    }

    public Integer getSoSProgress() {
        return mSoSProgress;
    }

    public void setSoSProgress(Integer SoSProgress) {
        mSoSProgress = SoSProgress;
    }

	public Integer getWoRProgress() {
		return mWoRProgress;
	}

	public void setWoRProgress(Integer WoRProgress) { mWoRProgress = WoRProgress; }
}
