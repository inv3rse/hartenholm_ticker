package com.appspot.simple_ticker.hartenholmticker.ui.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.base.dagger.news.DaggerNewsComponent;

import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

public class NewsFragment extends NucleusSupportFragment<NewsPresenter>
{
    private WebView _webView = null;
    private SwipeRefreshLayout _refreshLayout = null;
    private boolean loading = true;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setPresenterFactory(() -> DaggerNewsComponent.create().getNewsPresenter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        _webView = (WebView) view.findViewById(R.id.news_webView);
        setupWebView(_webView);

        _refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_news);
        _refreshLayout.setOnRefreshListener(() -> getPresenter().loadNews());

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (loading)
        {
            setLoading(true);
        }
    }

    public void showWebView(String baseUrl, String data)
    {
        setLoading(false);
        _webView.loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", null);
    }

    public void showErrorMsg(String msg)
    {
        setLoading(false);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setLoading(boolean loading)
    {
        this.loading = loading;
        _refreshLayout.post(() -> _refreshLayout.setRefreshing(loading));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(WebView webView)
    {
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // ask user how to handle link
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

        });
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
    }
}
