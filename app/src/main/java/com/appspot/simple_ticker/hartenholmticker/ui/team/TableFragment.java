package com.appspot.simple_ticker.hartenholmticker.ui.team;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.Table;
import com.appspot.simple_ticker.hartenholmticker.presenters.TablePresenter;

import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

@RequiresPresenter(TablePresenter.class)
public class TableFragment extends NucleusSupportFragment<TablePresenter>
{
    private static final String ARG_TEAM_ID = "ARG_TEAM_ID";

    public static TableFragment create(String teamId)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEAM_ID, teamId);

        TableFragment fragment = new TableFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private ListView _listView;
    private SwipeRefreshLayout _refreshLayout;
    private String _teamId;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _teamId = getArguments().getString(ARG_TEAM_ID);
        super.onCreate(savedInstanceState);
    }

    /**
     * Create a Presenter with a specified teamID
     * @return Factory to create a presenter for a specified team (data source)
     */
    @Override
    public PresenterFactory<TablePresenter> getPresenterFactory()
    {
        return () -> new TablePresenter(_teamId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        _listView = (ListView) view.findViewById(R.id.table_rows);
        _refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_table);
        _refreshLayout.setRefreshing(true);

        _refreshLayout.setOnRefreshListener(
                () -> getPresenter().fetchData()
        );

        return view;
    }

    public void onItemsNext(Table table)
    {
        _listView.setAdapter(new TableAdapter(getActivity(), table));
    }

    public void onItemsError(Throwable throwable)
    {
        Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void setLoading(boolean loading)
    {
        if (_refreshLayout != null)
        {
            _refreshLayout.setRefreshing(loading);
        }
    }
}