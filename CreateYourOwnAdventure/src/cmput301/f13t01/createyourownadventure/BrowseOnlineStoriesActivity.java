/*
 * CreateYourOwnAdventure
 * Gerald Manweiler
 * Copyright 2013 Gerald Manweiler Eddie Tai Jesse Chu Jesse Huard Reggie Miller
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package cmput301.f13t01.createyourownadventure;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cmput301.f13t01.elasticsearch.ESManager;
import cmput301.f13t01.elasticsearch.SearchManager;

/**
 * Sets up and handles browse online ui that allows user to search for and display
 * stories online, select an online story to read from beginning, select an online
 * story to save
 * 
 * @author Gerald Manweiler
 * @version 1.0 Nov 23 2013
 *
 */
public class BrowseOnlineStoriesActivity extends Activity{
	// local manager object to handle local story saving
	private LocalManager objLibrary;
	//story list view object
	private ListView lsvStories = null;
	//story info array list object
	//private ArrayList<StoryInfo> storyInfoList;
	// adapter for story info array list
	private StoryInfoListAdapter objStoryAdapter;
	
	//new objects for online browsing
	// ES Manager object to handle online story interactions
	private ESManager esLibrary;	
	private ArrayList<StoryInfo> results;

	/**
	 * Create Browse Online Story Screen
	 * 
	 * @param saveInstanceState The state of activity last kill
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//get last instance state and set view to main screen
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_online_activity);
		//set list view and register view for on long click contextual menu
		lsvStories = (ListView) findViewById(R.id.browse_online_activity_listview);
		registerForContextMenu(lsvStories);		
		
		//grab the local manager
		GlobalManager app = (GlobalManager) getApplication();
		objLibrary = app.getLocalManager();
		
		// Grab the ES Manager
		esLibrary = app.getESManager();
	}
	
	/**
	 * Resume activity updates the story list view
	 */
	protected void onResume() {
		super.onResume();
		//clear the search input boxes
		clearInputBoxes();	
		//get the story info list from  es manager for the story list adapter
		//results = esLibrary.searchOnlineStories("", "", "", 0);
		//initialize adapter and update the view
		//objStoryAdapter = new StoryInfoListAdapter(this, R.layout.story_info_list_item, results);
		//lsvStories.setAdapter(objStoryAdapter);
		//objStoryAdapter.notifyDataSetChanged();				
	}
	
	
	/**
	 * Places browse online and create new story icons in action bar
	 * 
	 * @param menu    The action bar menu xml resource to inflate into action bar
	 * @return boolean    True for display menu, false for no display
	 */	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    //inflate menu items specified in xml and return 
	    inflater.inflate(R.menu.browse_online_actionbar_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Deals with user action bar selection
	 * 
	 * @param item    The menu item resource selected by user
	 * @return boolean    Returns true for successful handling of menu item selection, false otherwise (from superclass)
	 */		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    
	    switch (item.getItemId()) {
	        //user wants to clear inputs for new search
	        case R.id.action_new_search:
	        	clearInputBoxes();
	        	return true;
	        //user wants to get online stories
	        case R.id.action_search_online_stories:
	            startSearchOnline();
	            return true;
	        //user wants next 20 stories
	        case R.id.action_get_next_20:
	            getNextTwenty();
	            return true;
	        //user wants to save  SELECTED STORY TO LOCAL DEVICE
	        case R.id.action_save_online_story:
	        	saveToLocal();
	        	return true;
	        //user wants a random story
	        case R.id.action_random_story:       	
	        	startRandomStory();
	        	return true;
	        //user wants the help menu
			case R.id.action_help:
				onSelectHelp();	
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * save online story to local device
	 */
	private void saveToLocal(){
		//feedback to user, echoing menu text
		Toast toast = Toast.makeText(getApplicationContext(), getResources()
				.getString(R.string.action_save_online_story), Toast.LENGTH_SHORT);
		toast.show();
		
		// Jesse's Additions past this point

		// This function needs to be called on a specific story...
		// It shouldn't be an action bar button
		// This call will save the story from the server locally
		// It returns the UUID of the story saved
		// localId = esLibrary.downloadStory(id)
	}
	/**
	 * clears input boxes for new search
	 */
	private void clearInputBoxes() {
		//feedback to user, echoing menu text
		Toast toast = Toast.makeText(getApplicationContext(), getResources()
				.getString(R.string.action_new_search), Toast.LENGTH_SHORT);
		toast.show();
		//implement edit box entry wipeout
		
		// Jesse's Additions past this point
		// Grabbing the EditText Views locally
		// Not sure if you want actual instance variables to refer to them, Gerald
		EditText searchTitle = (EditText) findViewById(R.id.search_title);
		EditText searchAuthor = (EditText) findViewById(R.id.search_author);
		EditText searchDesc = (EditText) findViewById(R.id.search_description);
		searchTitle.setText("");
		searchAuthor.setText("");
		searchDesc.setText("");
	}
	
	/**
	 * search for online stories based on search input parameters
	 */
	private void startSearchOnline(){
		//feedback to user, echoing menu text
		Toast toast = Toast.makeText(getApplicationContext(), getResources()
				.getString(R.string.action_search_online_stories), Toast.LENGTH_SHORT);
		toast.show();
		
		// Jesse's Additions past this point
		// Grabbing the EditText Views locally
		// Not sure if you want actual instance variables to refer to them, Gerald
		EditText searchTitle = (EditText) findViewById(R.id.search_title);
		EditText searchAuthor = (EditText) findViewById(R.id.search_author);
		EditText searchDesc = (EditText) findViewById(R.id.search_description);
		// Grab the search parameters
		String title = searchTitle.getText().toString();
		String author = searchAuthor.getText().toString();
		String desc = searchDesc.getText().toString();
		
		
		// !!! THIS DOES NOT WORK !!! not sure if it's access to inet problem or es problem
		// Search for stories, 0 is just a default we leave there, it's needed
		//results = esLibrary.searchOnlineStories(title, author, desc, 0);
		//THIS DOES WORK, but it's getting a local saved story
		results = objLibrary.getStoryInfoList();
		
		//initialize adapter and update the view
		objStoryAdapter = new StoryInfoListAdapter(this, R.layout.story_info_list_item, results);
		lsvStories.setAdapter(objStoryAdapter);
		objStoryAdapter.notifyDataSetChanged();
	}
	
	/**
	 * get next 20 stories
	 */
	private void getNextTwenty(){
		//feedback to user, echoing menu text
		Toast toast = Toast.makeText(getApplicationContext(), getResources()
				.getString(R.string.action_get_next_20), Toast.LENGTH_SHORT);
		toast.show();
		
		// Jesse/Reggie's Additions past this point
		// Index to start new query at (based on size of storyInfoList)
		Integer index = results.size();
		ArrayList<StoryInfo> next20 = esLibrary.getStoryInfoList(index);
		
		// You now have the next 20 StoryInfo objects in an ArrayList
		//add them to list and update data set
		results.addAll(next20);
		objStoryAdapter.notifyDataSetChanged();		
	}
	/**
	 * displays screen specific help
	 */
	private void onSelectHelp() {
		android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
		android.app.Fragment prev = getFragmentManager().findFragmentByTag("help_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		android.app.DialogFragment newFragment = (android.app.DialogFragment) HelpFragment.newInstance(HelpMessage.BROWSE_ONLINE);
		newFragment.show(ft, "help_dialog");
	}
	
	/**
	 * select random story and opens it for reading from beginning
	 */
	private void startRandomStory() {
		//feedback to user, echoing menu text
		Toast toast = Toast.makeText(getApplicationContext(), getResources()
				.getString(R.string.action_random_story), Toast.LENGTH_SHORT);
		toast.show();
		
		// Jesse's additions below
		// A random story object. We can play with the returns later if you want
		Story randomStory = esLibrary.getRandomOnlineStory();
		
		
		/*
		//get the story info list from manager and the size of list
		ArrayList<StoryInfo> randStorySource = objLibrary.getStoryInfoList();
		//if list size zero or less, no stories to select
		if (randStorySource.size() <= 0){
			return;
		}
		else
		{
			//new random object and story list size
		 	Random r = new Random();
		 	int listSize = randStorySource.size();
			//get next integer random number in range 0 to listSize - 1		 	
			int randStory = r.nextInt(listSize);
			//get the uuid of the randomly selected story
		    UUID randStoryId = randStorySource.get(randStory).getId();
			//create the intent and launch read story activity from beginning
		    Intent intent = new Intent(this, ReadFragmentActivity.class);
		    intent.putExtra(getResources().getString(R.string.story_continue), true);
			intent.putExtra(getResources().getString(R.string.story_id), randStoryId);
	        startActivity(intent);	
		}
		*/
	}	

}
