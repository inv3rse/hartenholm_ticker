package com.appspot.simple_ticker.hartenholmticker.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent
{
    Application getApplication();
}
