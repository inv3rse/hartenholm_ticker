package com.appspot.simple_ticker.hartenholmticker.dagger;

import android.app.Application;

import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule
{
    Application _application;

    public AppModule(Application application)
    {
        _application = application;
    }

    @Provides
    @Singleton
    public Application providesApplication()
    {
        return _application;
    }

    @Provides
    @Singleton
    public TickerClient provideTickerClient()
    {
        return new TickerClient();
    }
}
