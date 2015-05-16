package com.appspot.simple_ticker.hartenholmticker.ui.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.presenters.NewsPresenter;

import org.jsoup.select.Elements;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

@RequiresPresenter(NewsPresenter.class)
public class NewsFragment extends NucleusSupportFragment<NewsPresenter>
{
    private ListView _listView = null;
    private SwipeRefreshLayout _refreshLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        _listView = (ListView) view.findViewById(R.id.news_list);
        _refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_news);

        _refreshLayout.setOnRefreshListener(
                () ->
                {
                    getPresenter().fetchData();
                    _refreshLayout.setRefreshing(true);
                }
        );

        return view;
    }

    public void setLoading(boolean loading)
    {
        if (_refreshLayout != null)
        {
            _refreshLayout.setRefreshing(loading);
        }
    }

    public void onItemsNext(Elements newsEntries)
    {
        System.out.println("itemsnext called");
        _listView.setAdapter(new NewsAdapter(getActivity(), newsEntries));
    }

    public void onItemsError(Throwable throwable)
    {
        Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
