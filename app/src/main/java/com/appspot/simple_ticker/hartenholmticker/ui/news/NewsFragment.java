package com.appspot.simple_ticker.hartenholmticker.ui.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.MyApp;
import com.appspot.simple_ticker.hartenholmticker.R;

import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

@RequiresPresenter(NewsPresenter.class)
public class NewsFragment extends NucleusSupportFragment<NewsPresenter>
{
    private WebView _webView = null;
    private SwipeRefreshLayout _refreshLayout = null;

    @Override
    public PresenterFactory<NewsPresenter> getPresenterFactory()
    {
        return () -> ((MyApp) getActivity().getApplication()).getPresenterComponent().getNewsPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        _webView = (WebView) view.findViewById(R.id.news_webView);
        _refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_news);

        _refreshLayout.setOnRefreshListener(() -> getPresenter().updateData());

        return view;
    }

    public WebView getWebView()
    {
        return _webView;
    }

    public void setLoading(boolean loading)
    {
        _refreshLayout.post(() -> _refreshLayout.setRefreshing(loading));
    }

    public void showErrorMsg(String msg)
    {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
