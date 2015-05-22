package com.appspot.simple_ticker.hartenholmticker.ui.general;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.RestClient;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerApi;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsFragment;
import com.appspot.simple_ticker.hartenholmticker.ui.tables.TableFragment;
import com.appspot.simple_ticker.hartenholmticker.ui.ticker.TickerFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;


public class MainActivity extends AppCompatActivity
{
    private static final String NEWS_FRAGMENT_TAG = "NewsFragment";
    private static final String TICKER_FRAGMENT_TAG = "TickerFragment";
    private static final String TEAM1_FRAGMENT_TAG = "1. Herren";
    private static final String TEAM2_FRAGMENT_TAG = "2. Herren";
    private static final String TEAM3_FRAGMENT_TAG = "3. Herren";

    private Drawer.Result _drawer;
//    CharSequence _titles[] = {"News", "Tabelle", "Ticker"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        AccountHeader.Result header = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();

        _drawer = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_news),
                        new PrimaryDrawerItem().withName(R.string.drawer_ticker),
                        new SectionDrawerItem().withName(R.string.drawer_teams_section_name),
                        new SecondaryDrawerItem().withName(R.string.drawer_teams1),
                        new SecondaryDrawerItem().withName(R.string.drawer_teams2),
                        new SecondaryDrawerItem().withName(R.string.drawer_teams3),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_settings))
                .withOnDrawerItemClickListener((AdapterView<?> parent, View view, int position, long id, IDrawerItem item) -> {
                    if (item != null && item instanceof Nameable)
                    {
                        switch (((Nameable) item).getNameRes())
                        {
                            case R.string.drawer_news:
                                setFragment(NEWS_FRAGMENT_TAG);
                                break;
                            case R.string.drawer_ticker:
                                setFragment(TICKER_FRAGMENT_TAG);
                                break;
                            case R.string.drawer_teams1:
                                setFragment(TEAM1_FRAGMENT_TAG);
                                break;
                            case R.string.drawer_teams2:
                                setFragment(TEAM2_FRAGMENT_TAG);
                                break;
                            case R.string.drawer_teams3:
                                setFragment(TEAM3_FRAGMENT_TAG);
                                break;
                            case R.string.drawer_settings:
                                break;
                            default:
                                return;
                        }
                        getSupportActionBar().setTitle(((Nameable) item).getNameRes());
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withFireOnInitialOnClick(true)
                .build();

//        setFragment(_currentFragmentTag);

//        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), _titles);
//        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        viewPager.setAdapter(pagerAdapter);
//
//        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
//        tabLayout.setDistributeEvenly(true); // all have the same size
//
//        tabLayout.setCustomTabColorizer(
//                position -> getResources().getColor(R.color.tabsScrollColor)
//        );
//
//        tabLayout.setViewPager(viewPager);
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

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState = _drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void setFragment(String fragmentTag)
    {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(fragmentTag);

        if (fragment == null)
        {
            System.out.println("fragment new created");
            switch (fragmentTag)
            {
                case NEWS_FRAGMENT_TAG:
                    fragment = new NewsFragment();
                    break;
                case TICKER_FRAGMENT_TAG:
                    fragment = new TickerFragment();
                    break;
                case TEAM1_FRAGMENT_TAG:
                    fragment = new TableFragment();
                    break;
                default:
                    System.out.println("Fragment does not exist");
                    return;
            }
        }
        manager.beginTransaction().replace(R.id.container, fragment, fragmentTag).commit();
    }
}
