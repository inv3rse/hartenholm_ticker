package com.appspot.simple_ticker.hartenholmticker.presenters;

import android.os.Bundle;

import com.appspot.simple_ticker.hartenholmticker.dataLoaders.NewsLoader;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsFragment;

import org.jsoup.select.Elements;

import java.util.concurrent.TimeUnit;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsPresenter extends RxPresenter<NewsFragment>
{
    private Elements _cachedData = null;

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
            if (_cachedData != null)
            {
                getView().onItemsNext(_cachedData);
            }
            else
            {
                fetchData();
            }
        }
    }

    public void fetchData()
    {
        NewsLoader.fetch()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .compose(this.<Elements>deliverLatest())
                .subscribe(
                        elements ->
                        {
                            _cachedData = elements;
                            getView().onItemsNext(elements);
                        },
                        error ->
                        {
                            getView().onItemsError(error);
                        }
                );
    }
}
