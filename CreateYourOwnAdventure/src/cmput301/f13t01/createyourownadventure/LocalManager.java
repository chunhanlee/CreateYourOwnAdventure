/*
LocalManager class for CreateYourOwnAdventure.
This deals with the management of stories on disk.
All saving & loading is handled here, along with deletion.

     Copyright  �2013 Reginald Miller, Jesse Chu
    <Contact: rmiller3@ualberta.ca, jhchu@ualberta.ca>
    
    License GPLv3: GNU GPL Version 3
    <http://gnu.org/licenses/gpl.html>.
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package cmput301.f13t01.createyourownadventure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.net.Uri;

/**
 * This class is designed to maintain all of the
 * stories that are stored locally on the user's
 * device. Saving, Loading, and Deleting are
 * handled.
 * 
 * @author Reginald Miller, Jesse Chu
 *
 */

public class LocalManager implements Serializable, LibraryManager {
	
	/* Instance Variables for Library */
	private Context context;
	private HashMap<UUID, StoryInfo> storyInfoList;
	
	/**
	 * Constructor. Takes a context, loads all StoryInfo objects
	 * 
	 * @param context Context to allow for saving/loading
	 */
	public LocalManager(Context context) {
		this.context = context;
		this.loadStoryInfoList();
	}
	
	/**
	 * Gets the story with a given ID.
	 * 
	 * @param storyId   ID of the story
	 * @return   Returns the story with that ID
	 */
	public Story getStory(UUID storyId) {
		if (this.storyInfoList.keySet().contains(storyId)) {
			return loadStory(storyId);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns a StoryInfo object for the given ID.
	 * Used to display StoryInfo in lists.
	 * 
	 * @param id the ID of the Story to get the info of
	 * @return a StoryInfo object for that ID
	 */
	public StoryInfo getStoryInfo(UUID storyId) {
		if (this.storyInfoList.keySet().contains(storyId)) {
			return this.storyInfoList.get(storyId);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns an ArrayList of StoryInfo objects for
	 * all Story objects.
	 * 
	 * @return an ArrayList of all StoryInfo
	 */
	public ArrayList<StoryInfo> getStoryInfoList() {
		ArrayList<StoryInfo> fetchList = new ArrayList<StoryInfo>(this.storyInfoList.values());
		// TODO: Sort the list consistently before return
		Collections.sort(fetchList, new Comparator<StoryInfo>() {
			@Override 
			public int compare(final StoryInfo s1, final StoryInfo s2) {
				return s1.getTitle().compareTo(s2.getTitle());
			}
		});
		return fetchList;
	}

	/**
	 * Takes a story and adds it to the list
	 * 
	 * @param story The Story object to add to the Library
	 */
	public UUID addStory(Story story) {
		// ID assigned is a randomUUID... Shouldn't collide
		UUID id = UUID.randomUUID();
		// Places new StoryInfo into StoryInfoList
		StoryInfo newStoryInfo = new StoryInfo(id, story); 
		this.storyInfoList.put(id, newStoryInfo);
		this.saveStoryInfoList();
		// Save newly added Story
		this.saveStory(id, story);
		return id;
	}
	
	/**
	 * Deletes the story according to its ID.
	 * 
	 * @param storyId   ID of story to remove
	 */
	public boolean removeStory(UUID storyId) {
		// Story exists, removed
		if (this.storyInfoList.containsKey(storyId)) {
			// Remove from storyInfoList
			this.storyInfoList.remove(storyId);
			this.saveStoryInfoList();
			// Delete file for Story
			File storyFile = new File(storyId.toString()+".story");
			storyFile.delete();
			return true;
		} else {
			// Story does not exist, failure
			return false;
		}
	}
	
	/**
	 * Removes multiple stories at once from the storyList.
	 * 
	 * @param ArrayList of story IDs to be removed
	 */
	public void removeMultipleStories(ArrayList<UUID> stories) {
		for (UUID storyId : stories) {
			this.removeStory(storyId);
		}
	}
	
	@Deprecated
	/**
	 * Puts a new or updated story into the list of stories
	 * with the ID
	 * 
	 * @param storyId   ID of story to update
	 * @param story   Story that has been updated
	 */
	public void updateStory(UUID id, Story story) {
		// Save given story
		this.saveStory(id, story);
	}
	
	/**
	 * Save a Story into a file.
	 * 
	 * @param id The ID of the Story to save
	 * @param story The story to save to file
	 * 
	 * @return true if save is successful, false otherwise
	 */
	public boolean saveStory(UUID id, Story story) {
		// Update storyInfoList
		StoryInfo newStoryInfo = new StoryInfo(id, story); 
		this.storyInfoList.put(id, newStoryInfo);
		this.saveStoryInfoList();
		// Attempts to save a Story to a file
		try{
		    // Generate the save file name
		    String saveFile = id.toString()+".story";
			// Output streams to save Story object
			FileOutputStream fos = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// Save the Story
            oos.writeObject(story);
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			// Write access error
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// Something went wrong
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Load a Story from a file.
	 * 
 	 * @param id The ID of the Story to save
	 */
	public Story loadStory(UUID id) {
	    // Generate the save file name
	    String saveFile = id.toString()+".story";
		// Attempts to Story from file
		try {
			// Input streams to load Story object
			FileInputStream fis = context.openFileInput(saveFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			// Loads the Story from file
			Story loadedStory = (Story) ois.readObject();
			fis.close();
			return loadedStory;
		} catch (FileNotFoundException e) {
			// ID doesn't exist
			return null;
		} catch (IOException e) {
			// Something messed up
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Saves a given image by URI and returns the URI assigned.
	 * 
	 * @param image the image to save's URI
	 * @return the URI that the image was saved with
	 */
	public String saveImage(Uri mediaUri, MediaType mediaType) {
		// Determines path to save images
		String folderPath = context.getFilesDir().getAbsolutePath()+"/"+mediaType.toString();
		File folder = new File(folderPath);
		// Ensures folder exists
		if (!folder.exists()) {
			folder.mkdir();
		}
		// Generates an ID for the image
		UUID id = UUID.randomUUID();
		String saveFilePath = folder+"/"+id.toString();
		File mediaFile = new File(saveFilePath);
		// Copy the file over
		try {
			InputStream is = new FileInputStream(new File(mediaUri.getPath()));
			OutputStream os = new FileOutputStream(mediaFile);
			byte[] buf = new byte[1024];
			int len;
			while((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
			is.close();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Return the assigned file name
		return mediaFile.getName();
	}
	
	/**
	 * Saves the storyInfoList of the Library to file.
	 * 
	 * Filename is hard coded.
	 */
	public void saveStoryInfoList() {
		// Attempts to save StoryInfoList to a file
		try{
		    // Generate the save file name
		    String saveFile = "StoryInfoList.sav";
			// Output streams to save storyInfoList
			FileOutputStream fos = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// Save the storyInfoList
            oos.writeObject(this.storyInfoList);
			fos.close();
		} catch (FileNotFoundException e) {
			// Write access error
			e.printStackTrace();
		} catch (IOException e) {
			// Something went wrong
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the storyInfoList of the Library from file.
	 * 
	 * Filename is hard coded.
	 */
	public void loadStoryInfoList() {
	    // Generate the save file name
	    String saveFile = "StoryInfoList.sav";
		// Attempts to Story from file
		try {
			// Input streams to load storyInfoList
			FileInputStream fis = context.openFileInput(saveFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			// Loads the storyInfoList
			this.storyInfoList = (HashMap<UUID, StoryInfo>) ois.readObject();
			fis.close();
		} catch (FileNotFoundException e) {
			// No existing file
			this.storyInfoList = new HashMap<UUID, StoryInfo>();
			this.saveStoryInfoList();
		} catch (IOException e) {
			// Something messed up
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the Serializable StoryList object to a file.
	 * 
	 * @param out   ObjectOutputStream to write with
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		out.writeObject(this.storyInfoList);
		return;
	}
	
	/**
	 * Reads the Serializable StoryList object from a file.
	 * 
	 * @param in   ObjectInputStream to read with
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		this.storyInfoList = (HashMap<UUID, StoryInfo>) in.readObject();
		return;
	}
		 
	private void readObjectNoData() 
		     throws ObjectStreamException {
		return;
	}

}
