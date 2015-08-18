package pf.acg.ror;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class CharacterIntentJSONSerializer {
	private Context mContext;
	private String mFilename;
	
	public CharacterIntentJSONSerializer(Context c, String f){
		mContext = c;
		mFilename = f;
	}
	
	public void saveCharacters(ArrayList<PC> characters)
		throws JSONException, IOException{
			//Build an array in JSON
			JSONArray array = new JSONArray();
			for(PC c: characters)
				array.put(c.toJSON());
			
			//Write to Disk
			Writer writer = null;
			try {
				OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
				writer = new OutputStreamWriter(out);
				writer.write(array.toString());
			} finally {
				if(writer != null)
					writer.close();
			}
	}
	
	public ArrayList<PC> loadCharacters() throws JSONException, IOException {
		ArrayList<PC> characters = new ArrayList<PC>();
		BufferedReader reader = null;
		try {
			//open and read file
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				//Line breaks are ommitted
				jsonString.append(line);
			}
			//Parse JSON
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			//Build the array of crimes
			for(int i = 0; i < array.length(); i++){
				characters.add(new PC(array.getJSONObject(i)));
			}
		} catch(FileNotFoundException e) {
			//ignore; it happens when starting new crime list
		} finally {
			if(reader != null)
				reader.close();
		}
		return characters;
	} //end loadCharacters()

}
