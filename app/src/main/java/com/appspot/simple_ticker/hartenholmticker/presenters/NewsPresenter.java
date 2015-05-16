package com.appspot.simple_ticker.hartenholmticker.presenters;

import android.os.Bundle;

import com.appspot.simple_ticker.hartenholmticker.dataLoaders.NewsLoader;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsFragment;

import org.jsoup.select.Elements;

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
                setLoading(false);
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
        setLoading(true);
        NewsLoader.fetch()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        elements ->
                        {
                            _cachedData = elements;
                            setLoading(false);
                            getView().onItemsNext(elements);
                        },
                        error ->
                        {
                            setLoading(false);
                            getView().onItemsError(error);
                        }
                );
    }


    private void setLoading(boolean loading)
    {
        if (getView() != null)
        {
            getView().setLoading(loading);
        }
    }
}
