package com.appspot.simple_ticker.hartenholmticker.ui.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.NewsEntry;
import com.appspot.simple_ticker.hartenholmticker.presenters.NewsPresenter;

import org.jsoup.select.Elements;

import java.util.List;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

@RequiresPresenter(NewsPresenter.class)
public class NewsFragment extends NucleusSupportFragment<NewsPresenter>
{
    private ListView _listView = null;
    private SwipeRefreshLayout _refreshLayout = null;
    private boolean _isLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        _listView = (ListView) view.findViewById(R.id.news_list);
        _refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_news);

        _refreshLayout.setOnRefreshListener(() ->
        {
            _refreshLayout.setRefreshing(true);
            getPresenter().fetchData();
        });

        // dirty fix as initial setRefreshing is bugged
        _refreshLayout.post(() ->
        {
            if (!_isLoaded)
            {
                _refreshLayout.setRefreshing(true);
            }
        });

        return view;
    }

    public void onItemsNext(List<NewsEntry> newsEntries)
    {
        _listView.setAdapter(new NewsAdapter(getActivity(), newsEntries));
        _isLoaded = true;
        _refreshLayout.setRefreshing(false);
    }

    public void onItemsError(Throwable throwable)
    {
        _isLoaded = true;
        _refreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
