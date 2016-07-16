package com.example.metrorecord;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	
	static public Map<Integer,SpeedRecord> map = new HashMap<Integer,SpeedRecord>();
	static public int cur_sr;
	public Map<Integer,Integer> map_2 = new HashMap<Integer,Integer>();
	
	private CharSequence mTitle;
	public SpeedRecord mspeedrecord = null;
	public Fragment mcontent = null;
	public PlaceholderFragment mplaceholderfragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		init_map();
	}
	
	private void init_map(){
		cur_sr = R.id.metro1;
		map_2.put(R.id.metro1, 5);
		map_2.put(R.id.metro2, 1);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		// these complicated if statements is to restore the current fragment in order not to destroy it when we switch to another fragment
		if (position == 1){
			if ((mspeedrecord=map.get(cur_sr)) == null){
				mspeedrecord = new SpeedRecord();
				mspeedrecord.current_line = map_2.get(cur_sr);
				map.put(cur_sr, mspeedrecord);
				fragmentManager.beginTransaction().add(R.id.container,mspeedrecord).commit();
				fragmentManager.beginTransaction().hide(mcontent).show(mspeedrecord).commit();
				mcontent = mspeedrecord;
			}else{
				fragmentManager.beginTransaction().hide(mcontent).show(mspeedrecord).commit();
				mcontent = mspeedrecord;
			}
			onSectionAttached(2);
		}else{
			if (mcontent == null){
				mplaceholderfragment = PlaceholderFragment.newInstance(position + 1);
				mcontent = mplaceholderfragment;
				fragmentManager.beginTransaction().add(R.id.container, mplaceholderfragment).commit();
			}else{
				fragmentManager.beginTransaction().hide(mcontent).show(mplaceholderfragment).commit();
				mcontent = mplaceholderfragment;
			}
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.metro1 && mTitle.equals(getString(R.string.title_section2))) {
			cur_sr = id;
			onNavigationDrawerItemSelected(1);
			return true;
		}else if(id == R.id.metro2 && mTitle.equals(getString(R.string.title_section2))){
			cur_sr = id;
			onNavigationDrawerItemSelected(1);
			return true;
		}else if(!mTitle.equals(getString(R.string.title_section2))){
			Log.d("haha", "please select line in speed part");
			return super.onOptionsItemSelected(item);
			//return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private Activity mactivity;
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.trrrr, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
			mactivity = activity;
		}
		
		@Override
		public void onHiddenChanged(boolean hidden) {
			super.onHiddenChanged(hidden);
			if (hidden) {// 不在最前端界面显示
				
			} else {// 重新显示到最前端中
				((MainActivity) mactivity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
			}
		}
		
		@Override
		public void onDestroy(){
			SqlDatabase dbEntry = new SqlDatabase(getActivity());
	        dbEntry.open();
	        Log.d("database", "Database opened.");
	        SpeedRecord tmp = MainActivity.map.get(MainActivity.cur_sr);
	        dbEntry.createEntry( tmp.station_info,tmp.halt_or_not,tmp.current_line);
	        dbEntry.close();
	        Log.d("save when stop", "save complete");
	        new Logger(tmp.station_info,tmp.halt_or_not,"/sdcard/speed/");
	        map = new HashMap<Integer,SpeedRecord>();
			super.onStop();
		}
	}

}
