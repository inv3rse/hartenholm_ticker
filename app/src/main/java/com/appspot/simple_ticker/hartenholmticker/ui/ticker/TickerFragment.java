package com.appspot.simple_ticker.hartenholmticker.ui.ticker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.MyApp;
import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;
import com.appspot.simple_ticker.hartenholmticker.ui.general.MainActivity;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;


@RequiresPresenter(TickerPresenter.class)
public class TickerFragment extends NucleusSupportFragment<TickerPresenter> {
    private ListView _listView;
    private SwipeRefreshLayout _refreshLayout;
    private boolean _ignoreAdapterEvent = false;

    public TickerFragment() {
        setHasOptionsMenu(true);
    }

    public PresenterFactory<TickerPresenter> getPresenterFactory() {
        return () -> ((MyApp) getActivity().getApplication()).getAppComponent().getTickerPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticker, container, false);

        _listView = (ListView) view.findViewById(R.id.entry_list);
        _refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ticker_refreshLayout);

        _refreshLayout.setOnRefreshListener(() -> getPresenter().updateCurrentGame());

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.ticker_fab);
        floatingActionButton.attachToListView(_listView);

        floatingActionButton.setOnClickListener(
                contextView -> getPresenter().createEntry()
        );

        registerForContextMenu(_listView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.getToolbar().setDisplayShowTitleEnabled(false);
            activity.getSpinner().setVisibility(View.VISIBLE);
            activity.getSpinner().setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> view, View view1, int i, long l) {
                            if (_ignoreAdapterEvent) {
                                _ignoreAdapterEvent = false;
                            } else {
                                getPresenter().selectGame(i);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> view) {

                        }
                    }
            );
        }
    }

    @Override
    public void onDetach() {
        MainActivity activity = (MainActivity) getActivity();
        activity.getSpinner().setVisibility(View.GONE);
        activity.getSpinner().setOnItemSelectedListener(null);
        activity.getToolbar().setDisplayShowTitleEnabled(true);
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ticker, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_popup_ticker_entry, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_entry) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            TickerEntry entry = (TickerEntry) _listView.getAdapter().getItem(info.position);
            getPresenter().removeEntry(entry.getId());
        } else if (id == R.id.action_edit_entry) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            TickerEntry entry = (TickerEntry) _listView.getAdapter().getItem(info.position);
            getPresenter().editEntry(entry);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_game) {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            getActivity().startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setLoading(boolean loading) {
        _refreshLayout.post(() -> _refreshLayout.setRefreshing(loading));
    }

    public void setGameList(List<Game> games, int selected) {
        MainActivity activity = (MainActivity) getActivity();
        _ignoreAdapterEvent = true;
        activity.getSpinner().setAdapter(new GameListAdapter(getContext(), games));
        activity.getSpinner().setSelection(selected);
    }

    public void setGame(Game game) {
        _listView.setAdapter(new TickerEntryAdapter(getActivity(), game.getEntries()));
    }

    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
