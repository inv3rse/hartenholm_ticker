package com.appspot.simple_ticker.hartenholmticker.ui.general;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.base.network.TableLoader;
import com.appspot.simple_ticker.hartenholmticker.base.network.TeamLoader;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsFragment;
import com.appspot.simple_ticker.hartenholmticker.ui.team.TeamFragment;
import com.appspot.simple_ticker.hartenholmticker.ui.ticker.TickerFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import nucleus.presenter.Presenter;
import nucleus.view.ViewWithPresenter;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener
{
    private static final String TAG_NEWS_FRAGMENT = "NewsFragment";
    private static final String TAG_TICKER_FRAGMENT = "TickerFragment";
    private static final String TAG_TEAM1_FRAGMENT = "1. Herren";
    private static final String TAG_TEAM2_FRAGMENT = "2. Herren";
    private static final String TAG_TEAM3_FRAGMENT = "3. Herren";

    private Drawer _drawer;
    private Spinner _spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;

        _spinner = (Spinner) findViewById(R.id.spinner_nav);

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();

        _drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_news).withTag(TAG_NEWS_FRAGMENT),
                        new PrimaryDrawerItem().withName(R.string.drawer_ticker).withTag(TAG_TICKER_FRAGMENT),
                        new SectionDrawerItem().withName(R.string.drawer_teams_section_name),
                        new SecondaryDrawerItem().withName(R.string.drawer_teams1).withTag(TAG_TEAM1_FRAGMENT),
                        new SecondaryDrawerItem().withName(R.string.drawer_teams2).withTag(TAG_TEAM2_FRAGMENT),
                        new SecondaryDrawerItem().withName(R.string.drawer_teams3).withTag(TAG_TEAM3_FRAGMENT),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_settings))
                .withOnDrawerItemClickListener(this)
                .withSavedInstance(savedInstanceState)
                .withFireOnInitialOnClick(true)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        _drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem item)
    {
        Object tag;
        if (item != null && (tag = item.getTag()) != null && tag instanceof String)
        {
            setFragment((String) tag);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null && item instanceof Nameable) {
                actionBar.setTitle(((Nameable) item).getName().getTextRes());
            }
        }
        return false;
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
                case TAG_NEWS_FRAGMENT:
                    fragment = new NewsFragment();
                    break;
                case TAG_TICKER_FRAGMENT:
                    fragment = new TickerFragment();
                    break;
                case TAG_TEAM1_FRAGMENT:
                    fragment = TeamFragment.create(TableLoader.ID_HARTENHOLM_1, TeamLoader.TEAM1_ID);
                    break;
                case TAG_TEAM2_FRAGMENT:
                    fragment = TeamFragment.create(TableLoader.ID_HARTENHOLM_2, TeamLoader.TEAM2_ID);
                    break;
                case TAG_TEAM3_FRAGMENT:
                    fragment = TeamFragment.create(TableLoader.ID_HARTENHOLM_3, TeamLoader.TEAM3_ID);
                    break;
                default:
                    System.out.println("Fragment does not exist");
                    return;
            }
        }
        Fragment oldFragment = manager.findFragmentById(R.id.container);
        manager.beginTransaction().replace(R.id.container, fragment, fragmentTag).commit();

        // destroy presenter
        if (oldFragment != null && oldFragment != fragment && oldFragment instanceof ViewWithPresenter){
            Presenter presenter = ((ViewWithPresenter)oldFragment).getPresenter();
            if (presenter != null) {
                presenter.destroy();
            }
        }
    }

    public android.support.v7.app.ActionBar getToolbar()
    {
        return getSupportActionBar();
    }

    public Spinner getSpinner()
    {
        return _spinner;
    }
}
