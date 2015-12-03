package com.appspot.simple_ticker.hartenholmticker.dagger;

import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerClient;
import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsPresenter;
import com.appspot.simple_ticker.hartenholmticker.ui.ticker.TickerPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule
{
    @Provides
    @Singleton
    public TickerPresenter provideTickerPresenter(TickerClient client)
    {
        return new TickerPresenter(client);
    }

    @Provides
    @Singleton
    public NewsPresenter provideNewsPresenter()
    {
        return new NewsPresenter();
    }
}
