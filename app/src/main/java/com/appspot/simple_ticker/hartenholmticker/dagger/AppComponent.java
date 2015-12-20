package com.appspot.simple_ticker.hartenholmticker.dagger;

import android.app.Application;

import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerClient;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsPresenter;
import com.appspot.simple_ticker.hartenholmticker.ui.ticker.TickerPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent
{
    Application getApplication();
    TickerClient getTickerClient();

    TickerPresenter getTickerPresenter();
    NewsPresenter getNewsPresenter();
}
