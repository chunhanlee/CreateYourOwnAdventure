/*
StoryInfo Class for CreateYourOwnAdventure App.
Used to get basic information about a Story
    
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

/**
 * @author Jesse Chu <jhchu@ualberta.ca>
 *
 * StoryInfo class.
 * For use of views that list Story objects.
 */

public class StoryInfo {
	
	/* Instance Variables for StoryInfo */
	private Integer id;
	private String title;
	private String author;
	private String description;
	
	/**
	 * Constructor. Takes a Story and extracts its info.
	 * 
	 * @param story the Story to get the info of
	 */
	public StoryInfo(Integer id, Story story) {
		this.id = id;
		this.title = story.getTitle();
		this.author = story.getAuthor();
		this.description = story.getDescription();
	}
	
	/**
	 * Alternate Constructor.
	 * 
	 * Initializes a null instance of StoryInfo.
	 */
	public StoryInfo() {
		this.id = null;
		this.title = null;
		this.author = null;
		this.description = null;
	}

	/**
	 * Getter for the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Getter for the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Getter for the author.
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Getter for the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
}
