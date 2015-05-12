package com.appspot.simple_ticker.hartenholmticker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;
import com.appspot.simple_ticker.hartenholmticker.rest.RestClient;
import com.appspot.simple_ticker.hartenholmticker.rest.TickerApi;
import com.melnykov.fab.FloatingActionButton;

import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class TickerFragment extends Fragment
{
    private static final String KEY_CURRENT_GAME = "key_current_game";

    private TickerApi _api;
    private ListView _listView;
    private Game _currentGame;

    public TickerFragment()
    {
        _currentGame = null;
        _api = RestClient.getApi();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            _currentGame = savedInstanceState.getParcelable(KEY_CURRENT_GAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticker, container, false);
        _listView = (ListView) view.findViewById(R.id.entry_list);

        if (_currentGame == null)
        {
            _currentGame = new Game("unknown", new Date());
            loadCurrentGame();
        }
        else
        {
            _listView.setAdapter(new TickerEntryAdapter(getActivity(), _currentGame.getEntries()));
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.ticker_fab);
        floatingActionButton.attachToListView(_listView);

        floatingActionButton.setOnClickListener(
                contextView ->
                {
                    Intent intent = new Intent(getActivity(), TickerEntryActivity.class);
                    intent.putExtra(TickerEntryActivity.EXTRA_GAME_ID, _currentGame.getId());
                    getActivity().startActivity(intent);
                }
        );

        registerForContextMenu(_listView);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_ticker, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info)
    {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_popup_ticker_entry, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_delete_entry)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            TickerEntry entry = (TickerEntry)_listView.getAdapter().getItem(info.position);
            _api.deleteEntry(_currentGame.getId(), entry.getId()).subscribe(
                    result -> System.out.println("Entry deleted"),
                    error -> error.printStackTrace()
            );
        }
        else if (id == R.id.action_edit_entry)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            TickerEntry entry = (TickerEntry)_listView.getAdapter().getItem(info.position);
            Intent intent = new Intent(getActivity(), TickerEntryActivity.class);
            intent.putExtra(TickerEntryActivity.EXTRA_GAME_ID, _currentGame.getId());
            intent.putExtra(TickerEntryActivity.EXTRA_CHANGE_ENTRY, entry);
            getActivity().startActivity(intent);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_sync)
        {
            loadCurrentGame();
            return true;
        }
        else if (id == R.id.action_new_game)
        {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            getActivity().startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_CURRENT_GAME, _currentGame);
    }

    private void loadCurrentGame()
    {
        _api.getCurrentGame().observeOn(AndroidSchedulers.mainThread()).subscribe(
                result ->
                {
                    _listView.setAdapter(new TickerEntryAdapter(getActivity(), result.getEntries()));
                    _currentGame = result;
                },
                error -> error.printStackTrace());
    }

}
