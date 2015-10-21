package com.appspot.simple_ticker.hartenholmticker.presenters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appspot.simple_ticker.hartenholmticker.dataLoaders.SimplifiedPageLoader;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsFragment;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsPresenter extends RxPresenter<NewsFragment>
{
    private static final String BASE_URL = "http://www.tushartenholm-liga.de";
    private static final String NEWS_PAGE = "/aktuelles-1/";

    private String _cachedData = null;

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
    }

    @Override
    protected void onTakeView(NewsFragment view)
    {
        super.onTakeView(view);

        if (view != null)
        {
            WebView webView = view.getWebView();

            webView.setWebViewClient(new WebViewClient()
            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // ask user how to handle link
                    NewsFragment newsView = getView();
                    if (newsView != null)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        newsView.startActivity(intent);
                    }
                    return true;
                }

            });
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.getSettings().setJavaScriptEnabled(true);

            if (_cachedData != null)
            {
                setNewsPage(_cachedData);
            }
            else
            {
                updateData();
            }
        }
    }

    public void updateData()
    {
        NewsFragment view = getView();
        if (view != null)
        {
            view.setLoading(true);
        }

        SimplifiedPageLoader.loadreducedPage(BASE_URL + NEWS_PAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .compose(this.<String>deliverLatest())
                .subscribe(
                        data ->
                        {
                            _cachedData = data;
                            setNewsPage(data);
                        },
                        error ->
                        {
                            getView().showErrorMsg(error.getLocalizedMessage());
                            getView().setLoading(false);
                        }
                );
    }

    private void setNewsPage(String data)
    {
        NewsFragment view = getView();
        if (view != null)
        {
            view.getWebView().loadDataWithBaseURL(BASE_URL, data, "text/html", "utf-8", null);
            view.setLoading(false);
        }
    }
}
