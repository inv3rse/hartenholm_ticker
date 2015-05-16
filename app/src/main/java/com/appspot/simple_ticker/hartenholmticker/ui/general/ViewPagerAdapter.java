package com.appspot.simple_ticker.hartenholmticker.ui.general;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsFragment;
import com.appspot.simple_ticker.hartenholmticker.ui.tables.TableFragment;
import com.appspot.simple_ticker.hartenholmticker.ui.ticker.TickerFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence _titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence titles[]) {
        super(fm);

        this._titles = titles;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            return new NewsFragment();
        }
        else if (position == 1)
        {
            return new TableFragment();
        }
        else
        {
            return new TickerFragment();
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