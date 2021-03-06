package pf.acg.ror;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.FrameLayout;
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
		case R.id.rate_my_app:
			launchPlayStore();
			return true;
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
		case R.id.menu_item_new_raider_ss:
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
		case R.id.menu_item_new_fighter_ss:
			addCharacter(new PC("fighter_ss"));
			return true;
		case R.id.menu_item_new_gunslinger_ss:
			addCharacter(new PC("gunslinger_ss"));
			return true;
		case R.id.menu_item_new_rogue_ss:
			addCharacter(new PC("rogue_ss"));
			return true;
		case R.id.menu_item_new_magus_ss:
			addCharacter(new PC("magus_ss"));
			return true;
		case R.id.menu_item_new_alchemist_ss:
			addCharacter(new PC("alchemist_ss"));
			return true;
		case R.id.menu_item_new_witch_ss:
			addCharacter(new PC("witch_ss"));
			return true;
		case R.id.menu_item_new_druid_ss:
			addCharacter(new PC("druid_ss"));
			return true;
		case R.id.menu_item_new_warpriest_ss:
			addCharacter(new PC("warpriest_ss"));
			return true;
		case R.id.menu_item_new_cavalier_wr:
			addCharacter(new PC("cavalier_wr"));
			return true;
		case R.id.menu_item_new_summoner_wr:
			addCharacter(new PC("summoner_wr"));
			return true;
		case R.id.menu_item_new_arcanist_wr:
			addCharacter(new PC("arcanist_wr"));
			return true;
		case R.id.menu_item_new_ranger_wr:
			addCharacter(new PC("ranger_wr"));
			return true;
		case R.id.menu_item_new_inquisitor_wr:
			addCharacter(new PC("inquisitor_wr"));
			return true;
		case R.id.menu_item_new_cleric_wr:
			addCharacter(new PC("cleric_wr"));
			return true;
		case R.id.menu_item_new_paladin_wr:
			addCharacter(new PC("paladin_wr"));
			return true;
		case R.id.menu_item_new_hunter_wr:
			addCharacter(new PC("hunter_wr"));
			return true;
		case R.id.menu_item_new_bloodrager_wr:
			addCharacter(new PC("bloodrager_wr"));
			return true;
		case R.id.menu_item_new_sorcerer_wr:
			addCharacter(new PC("sorceress_wr"));
			return true;
		case R.id.menu_item_new_shaman_wr:
			addCharacter(new PC("shaman_wr"));
			return true;
		case R.id.menu_item_new_bekah_cd:
			addCharacter(new PC("bekah_cd"));
			return true;
		case R.id.menu_item_new_lem_cd:
			addCharacter(new PC("lem_cd"));
			return true;
		case R.id.menu_item_new_meliski_cd:
			addCharacter(new PC("meliski_cd"));
			return true;
		case R.id.menu_item_new_siwar_cd:
			addCharacter(new PC("sinwar_cd"));
			return true;
		case R.id.menu_item_new_flenta_cd:
			addCharacter(new PC("flenta_cd"));
			return true;
		case R.id.menu_item_new_tontelizi_cd:
			addCharacter(new PC("tontelizi_cd"));
			return true;
		case R.id.menu_item_new_valeros_cd:
			addCharacter(new PC("valeros_cd"));
			return true;
		case R.id.menu_item_new_vika_cd:
			addCharacter(new PC("vika_cd"));
			return true;
		case R.id.menu_item_new_koren_cd:
			addCharacter(new PC("koren_cd"));
			return true;
		case R.id.menu_item_new_raz_cd:
			addCharacter(new PC("raz_cd"));
			return true;
		case R.id.menu_item_new_seelah_cd:
			addCharacter(new PC("seelah_cd"));
			return true;
		case R.id.menu_item_new_agna_cd:
			addCharacter(new PC("agna_cd"));
			return true;
		case R.id.menu_item_new_arabundi_cd:
			addCharacter(new PC("arabundi_cd"));
			return true;
		case R.id.menu_item_new_harsk_cd:
			addCharacter(new PC("harsk_cd"));
			return true;
		case R.id.menu_item_new_wrathack_cd:
			addCharacter(new PC("wrathack_cd"));
			return true;
		case R.id.menu_item_new_lesath_cd:
			addCharacter(new PC("lesath_cd"));
			return true;
		case R.id.menu_item_new_merisiel_cd:
			addCharacter(new PC("merisiel_cd"));
			return true;
		case R.id.menu_item_new_olenjack_cd:
			addCharacter(new PC("olenjack_cd"));
			return true;
		case R.id.menu_item_new_wu_shen_cd:
			addCharacter(new PC("wu_shen_cd"));
			return true;
		case R.id.menu_item_new_oracle_mm:
			addCharacter(new PC("oracle_mm"));
			return true;
		case R.id.menu_item_new_alchemist_mm:
			addCharacter(new PC("alchemist_mm"));
			return true;
		case R.id.menu_item_new_spiritualist_mm:
			addCharacter(new PC("spiritualist_mm"));
			return true;
		case R.id.menu_item_new_wizard_mm:
			addCharacter(new PC("wizard_mm"));
			return true;
		case R.id.menu_item_new_rogue_mm:
			addCharacter(new PC("rogue_mm"));
			return true;
		case R.id.menu_item_new_kineticist_mm:
			addCharacter(new PC("kineticist_mm"));
			return true;
		case R.id.menu_item_new_slayer_mm:
			addCharacter(new PC("slayer_mm"));
			return true;
		case R.id.menu_item_new_magus_mm:
			addCharacter(new PC("magus_mm"));
			return true;
		case R.id.menu_item_new_druid_mm:
			addCharacter(new PC("druid_mm"));
			return true;
		case R.id.menu_item_new_cleric_mm:
			addCharacter(new PC("cleric_mm"));
			return true;
		case R.id.menu_item_new_occultist_mm:
			addCharacter(new PC("occultist_mm"));
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
			cleanDetailView();
			adapter.notifyDataSetChanged();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void cleanDetailView() {
		FrameLayout detailFragment = (FrameLayout)getActivity().findViewById(R.id.detailedFragmentContainer);
		if(detailFragment != null){
			detailFragment.removeAllViewsInLayout();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		PC c = ((CharacterAdapter)getListAdapter()).getItem(position);
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
			
			//Configure the view for this character
			PC c = getItem(position);
			CharacterHelper ch = new CharacterHelper();
			ch.setCharacterResourceArrays(getActivity(), c);
			
			//Set the Title
			TextView nameTextView = (TextView)convertView.findViewById(R.id.character_list_item_nameTextView);
			nameTextView.setText(c.getName());
			
			TextView roleTextView = (TextView)convertView.findViewById(R.id.character_list_item_roleTextView);
			roleTextView.setText(ch.getRoles()[c.getRoleBonus()]);
			
			return convertView;
		}
	}

	private void launchPlayStore()
	{
		Context context = getActivity().getApplicationContext();
		Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
		}
	}
}
