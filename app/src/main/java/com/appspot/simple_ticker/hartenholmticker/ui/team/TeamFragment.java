package com.appspot.simple_ticker.hartenholmticker.ui.team;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsFragment;
import com.appspot.simple_ticker.hartenholmticker.ui.ticker.TickerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment
{
    private static final String ARG_FUSSBALL_TEAM_ID = "ARG_FUSSBALL_TEAM_ID";
    private static final String ARG_WEB_TEAM_ID = "ARG_TEAM_ID";
    private static final String[] TITLES = {"Tabelle", "Aufstellung"};

    /**
     * Creates a Fragment showing info for the team
     * @param fussballDeTeamId team id on fußball.de
     * @param websiteId team id on tushartenholm.de
     * @return the fragment
     */
    public static TeamFragment create(String fussballDeTeamId, String websiteId)
    {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();

        args.putString(ARG_FUSSBALL_TEAM_ID, fussballDeTeamId);
        args.putString(ARG_WEB_TEAM_ID, websiteId);

        fragment.setArguments(args);
        return fragment;
    }

    private String _fussballDeId;
    private String _websiteId;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        _fussballDeId = args.getString(ARG_FUSSBALL_TEAM_ID);
        _websiteId = args.getString(ARG_WEB_TEAM_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), TITLES);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        SlidingTabLayout tabLayout = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabLayout.setDistributeEvenly(true); // all have the same size

        tabLayout.setCustomTabColorizer(
                position -> getResources().getColor(R.color.tabsScrollColor)
        );

        tabLayout.setViewPager(viewPager);

        return view;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter
    {

        CharSequence _titles[];

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm,CharSequence titles[])
        {
            super(fm);
            _titles = titles;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            if(position == 0) // if the position is 0 we are returning the First tab
            {
                return TableFragment.create(_fussballDeId);
            }
            else
            {
                return LineUpFragment.create(_websiteId);
            }
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return _titles[position];
        }

        // This method return the Number of tabs for the tabs Strip
        @Override
        public int getCount() {
            return _titles.length;
        }
    }
}
