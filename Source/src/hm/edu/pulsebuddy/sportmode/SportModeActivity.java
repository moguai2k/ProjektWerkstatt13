package hm.edu.pulsebuddy.sportmode;

import hm.edu.pulsebuddy.R;
import hm.edu.pulsebuddy.misc.SportModeResultFragment;
import hm.edu.pulsebuddy.misc.SportModeStartFragment;
import hm.edu.pulsebuddy.misc.SportModeWorkoutPlanFragment;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 
 * @author Christoph Friegel
 * @author Tore Offermann
 * 
 */
public class SportModeActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SportModeStartFragment sportModeStartFragment = new SportModeStartFragment();

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport_test);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	// disable settings
	// @Override
	// public boolean onCreateOptionsMenu( Menu menu )
	// {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate( R.menu.sport_mode, menu );
	// return true;
	// }

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			// Fragment fragment = new SportTestNoteFragment();
			// Bundle args = new Bundle();
			// args.putInt( SportTestNoteFragment.ARG_SECTION_NUMBER, position +
			// 1 );
			// fragment.setArguments( args );
			// return fragment;

			Fragment fragment;
			switch (position) {
			case 0:
				fragment = new SportModeStartFragment();
				break;
			case 1:
				fragment = new SportModeWorkoutPlanFragment();
				break;
			case 2:
				fragment = new SportModeResultFragment();
				break;
			default:
				throw new IllegalArgumentException("Invalid section number");
			}

			// // set args if necessary
			// Bundle args = new Bundle();
			// args.putInt( DummySectionFragment.ARG_SECTION_NUMBER, position +
			// 1 );
			// fragment.setArguments( args );

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.sport_mode_title_sport_test)
						.toUpperCase(l);
			case 1:
				return getString(R.string.sport_mode_title_workout_plan)
						.toUpperCase(l);
			case 2:
				return getString(R.string.sport_test_title_result).toUpperCase(
						l);
			}
			return null;
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}

}
