package pf.acg.ror;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;



public class CharacterBinder {
	private static final String TAG = "CharacterBinder";
	private static final String FILENAME = "characters.json";
	
	private static CharacterBinder sCharacterBinder;
	private Context mAppContext;
	private ArrayList<PC> mCharacters;
	private CharacterIntentJSONSerializer mSerializer;
	
	private CharacterBinder(Context appContext){
		mAppContext = appContext;
		mSerializer = new CharacterIntentJSONSerializer(mAppContext, FILENAME);
		
		try{
			mCharacters = mSerializer.loadCharacters();
		} catch(Exception e) {
			mCharacters = new ArrayList<PC>();
			//Log.e(TAG, "Error loading characters");
		}
	}
	
	public static CharacterBinder get(Context c){
		if(sCharacterBinder == null){
			sCharacterBinder = new CharacterBinder(c.getApplicationContext());
		}
		return sCharacterBinder;
	}
	
	public void addCharacter(PC c){
		mCharacters.add(c);
	}
	
	public ArrayList<PC> getCharacters(){
		return mCharacters;
	}
	
	public PC getPC(UUID id){
		for(PC c:mCharacters){
			if(c.getId().equals(id)){
				return c;
			}
		}
		return null;
	}//end getCrime
	
	public boolean saveCharacters(){
		try{
			mSerializer.saveCharacters(mCharacters);
			//Log.d(TAG, "characters saved");
			return true;
		} catch (Exception e) {
			//Log.e(TAG, "Error saving data: ", e);
			return false;
		}
	}
	
	public void deleteCharacter(PC c){
		mCharacters.remove(c);
	}
}
