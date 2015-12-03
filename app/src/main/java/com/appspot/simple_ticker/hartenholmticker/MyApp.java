package com.appspot.simple_ticker.hartenholmticker;

import android.app.Application;

import com.appspot.simple_ticker.hartenholmticker.dagger.AppComponent;
import com.appspot.simple_ticker.hartenholmticker.dagger.AppModule;
import com.appspot.simple_ticker.hartenholmticker.dagger.DaggerAppComponent;
import com.appspot.simple_ticker.hartenholmticker.dagger.DaggerPresenterComponent;
import com.appspot.simple_ticker.hartenholmticker.dagger.PresenterComponent;

public class MyApp extends Application
{
    private AppComponent _appComponent;
    private PresenterComponent _presenterComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();

        AppModule appModule = new AppModule(this);

        _appComponent = DaggerAppComponent.builder().appModule(appModule).build();
        _presenterComponent = DaggerPresenterComponent.builder().appModule(appModule).build();
    }

    public AppComponent getAppComponent()
    {
        return _appComponent;
    }

    public PresenterComponent getPresenterComponent()
    {
        return _presenterComponent;
    }
}
