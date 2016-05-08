package com.appspot.simple_ticker.hartenholmticker.ui.news;

import android.os.Bundle;

import com.appspot.simple_ticker.hartenholmticker.base.network.SimplifiedPageLoader;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsPresenter extends RxPresenter<NewsFragment>
{
    private static final String BASE_URL = "http://www.tushartenholm-liga.de";
    private static final String NEWS_PAGE = "/aktuelles-1/";

    private static final int REQUEST_NEWS = 1;

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        restartableLatestCache(
                REQUEST_NEWS,
                () -> SimplifiedPageLoader.loadreducedPage(BASE_URL + NEWS_PAGE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                (view, data) -> view.showWebView(BASE_URL, data),
                (view, error) -> view.showErrorMsg(error.getLocalizedMessage())
        );


        if (savedState == null) {
            loadNews();
        }
    }

    public void loadNews() {
        start(REQUEST_NEWS);
    }

}
