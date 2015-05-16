package com.appspot.simple_ticker.hartenholmticker.ui.general;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.RestClient;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerApi;


public class MainActivity extends AppCompatActivity
{
    Toolbar _toolbar;
    CharSequence _titles[] = {"News", "Tabelle", "Ticker"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(_toolbar);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), _titles);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        tabLayout.setDistributeEvenly(true); // all have the same size

        tabLayout.setCustomTabColorizer(
                position -> getResources().getColor(R.color.tabsScrollColor)
        );

        tabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            TickerApi api = RestClient.getApi();
            api.listGames();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
