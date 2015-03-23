package pf.acg.ror;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CharacterListFragment extends ListFragment 
{
	private static final String TAG = "characterlistfragment";
	private ArrayList<PC> mCharacters;
	private Callbacks mCallbacks;

	//start interface
	public interface Callbacks {
		void onCharacterSelected(PC character);
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
	//end interface
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		
		mCharacters = CharacterBinder.get(getActivity()).getCharacters();
		
		CharacterAdapter adapter = new CharacterAdapter(mCharacters);
		setListAdapter(adapter);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		CharacterBinder.get(getActivity()).saveCharacters();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		((CharacterAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		registerForContextMenu(listView);
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_character_list, menu);
	}

	public void updateUI() {
		((CharacterAdapter)getListAdapter()).notifyDataSetChanged();
	}

	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.menu_item_new_monk:
			addCharacter(new PC("monk"));
			return true;
		case R.id.menu_item_new_paladin:
			addCharacter(new PC("paladin"));
			return true;	
		case R.id.menu_item_new_barbarian:
			addCharacter(new PC("barbarian"));
			return true;
		case R.id.menu_item_new_bard:
			addCharacter(new PC("bard"));
			return true;
		case R.id.menu_item_new_cleric:
			addCharacter(new PC("cleric"));
			return true;
		case R.id.menu_item_new_druid:
			addCharacter(new PC("druid"));
			return true;
		case R.id.menu_item_new_fighter:
			addCharacter(new PC("fighter"));
			return true;
		case R.id.menu_item_new_ranger:
			addCharacter(new PC("ranger"));
			return true;
		case R.id.menu_item_new_rogue:
			addCharacter(new PC("rogue"));
			return true;
		case R.id.menu_item_new_sorceress:
			addCharacter(new PC("sorceress"));
			return true;
		case R.id.menu_item_new_wizard:
			addCharacter(new PC("wizard"));
			return true;
		case R.id.menu_item_raider_ss:
			addCharacter(new PC("raider_ss"));
			return true;
		case R.id.menu_item_new_oracle_ss:
			addCharacter(new PC("oracle_ss"));
			return true;
		case R.id.menu_item_new_swashbucker_ss:
			addCharacter(new PC("swashbuckler_ss"));
			return true;
		case R.id.menu_item_new_bard_ss:
			addCharacter(new PC("bard_ss"));
			return true;
		case R.id.menu_item_new_gunslinger_ss:
			addCharacter(new PC("gunslinger_ss"));
			return true;
		case R.id.menu_item_new_rogue_ss:
			addCharacter(new PC("rogue_ss"));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		getActivity().getMenuInflater().inflate(R.menu.character_list_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		CharacterAdapter adapter = (CharacterAdapter)getListAdapter();
		PC character = adapter.getItem(position);
		
		switch(item.getItemId()){
		case R.id.menu_item_delete:
			CharacterBinder.get(getActivity()).deleteCharacter(character);
			adapter.notifyDataSetChanged();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		PC c = ((CharacterAdapter)getListAdapter()).getItem(position);
		//Log.d(TAG, c.getRole() + " was clicked");
		
		//Start a Character Pager Activity
		//Intent i = new Intent(getActivity(), CharacterPagerActivity.class);
		//i.putExtra(CharacterFragment.EXTRA_CHARACTER_ID, c.getId());
		//startActivity(i);
		mCallbacks.onCharacterSelected(c);
	}
	
	private void addCharacter(PC character){
		//Log.d(TAG, character.getRole());
		CharacterBinder.get(getActivity()).addCharacter(character);
		((CharacterAdapter)getListAdapter()).notifyDataSetChanged();
		mCallbacks.onCharacterSelected(character);
	}
	
	private class CharacterAdapter extends ArrayAdapter<PC> {
		
		public CharacterAdapter(ArrayList<PC> characters){
			super(getActivity(),0,characters);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			//If we wern't given a view, inflate one
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_character,null);
			}
			
			//Configure the view for this crime
			PC c = getItem(position);
			CharacterHelper ch = new CharacterHelper();
			ch.setCharacterResourceArrays(getActivity(), c.role_to_enum(), c.getRoleBonus());
			
			//Set the Title
			TextView nameTextView = (TextView)convertView.findViewById(R.id.character_list_item_nameTextView);
			nameTextView.setText(c.getName());
			
			TextView roleTextView = (TextView)convertView.findViewById(R.id.character_list_item_roleTextView);
			roleTextView.setText(ch.getRoles()[c.getRoleBonus()]);
			
			return convertView;
		}
	}
}
