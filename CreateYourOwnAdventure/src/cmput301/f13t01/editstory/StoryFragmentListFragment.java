/*
StoryFragmentListFragment class for CreateYourOwnAdventure.
This class is used to create a dialog to show all available fragments for a
story, and handle user selection of fragments. This dialog can be statically
instantiated.

     Copyright  �2013 Jesse Huard
    <Contact: jhuard@ualberta.ca>
    
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

package cmput301.f13t01.editstory;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import cmput301.f13t01.R;
import cmput301.f13t01.model.StoryFragmentInfo;
import cmput301.f13t01.storylibrary.GlobalManager;

/**
 * This class is used to create a dialog to show all available fragments for a
 * story, and handle user selection of fragments. This dialog can be statically
 * instantiated.
 * 
 * @author Jesse Huard
 * 
 */

public class StoryFragmentListFragment extends DialogFragment {

	private StoryFragmentListListener listener;
	private FragmentListAdapter adapter;

	/**
	 * Constructor
	 * 
	 * @return an instance of StoryFragmentListFragment
	 */
	static StoryFragmentListFragment newInstance() {
		StoryFragmentListFragment f = new StoryFragmentListFragment();

		Bundle args = new Bundle();
		f.setArguments(args);

		return f;
	}

	@Override
	/**
	 * Override the onAttach function
	 */
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.listener = (StoryFragmentListListener) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement StoryFragmentListListener");
		}
	}

	@Override
	/**
	 * Override the onCreateDialog function
	 */
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GlobalManager app = (GlobalManager) getActivity().getApplication();
		ArrayList<StoryFragmentInfo> info = app.getStoryManager()
				.getFragmentInfoList();

		FragmentListAdapter adapt = new FragmentListAdapter(getActivity(), info);
		this.adapter = adapt;

		// Add the onclick listener
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.story_fragments);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				int fragmentId = adapter.getIdAtPosition(item);
				listener.onStoryFragmentSelected(fragmentId);
			}
		});
		return builder.create();
	}

	/*
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { Dialog dialog = getDialog();
	 * Window window = dialog.getWindow(); window.setGravity(Gravity.TOP);
	 * 
	 * return super.onCreateView(inflater, container, savedInstanceState); }
	 */

	@Override
	/**
	 * Override the onCreate function
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GlobalManager app = (GlobalManager) getActivity().getApplication();
		ArrayList<StoryFragmentInfo> info = app.getStoryManager()
				.getFragmentInfoList();

		this.adapter = new FragmentListAdapter(getActivity(), info);

	}
}