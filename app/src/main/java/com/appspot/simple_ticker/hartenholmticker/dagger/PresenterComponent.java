package com.appspot.simple_ticker.hartenholmticker.dagger;

import com.appspot.simple_ticker.hartenholmticker.ui.news.NewsPresenter;
import com.appspot.simple_ticker.hartenholmticker.ui.ticker.TickerPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface PresenterComponent
{
    TickerPresenter getTickerPresenter();
    NewsPresenter getNewsPresenter();
}
