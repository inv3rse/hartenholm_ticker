package com.appspot.simple_ticker.hartenholmticker.ui.team;


import android.os.Bundle;

import com.appspot.simple_ticker.hartenholmticker.base.network.TeamLoader;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LineUpPresenter extends RxPresenter<LineUpFragment>
{
    private static final int REQUEST_LINEUP = 1;

    private String _team;
    private String _saison;

    public LineUpPresenter(String team, String saison)
    {
        _team = team;
        _saison = saison;
    }

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        restartableLatestCache(
                REQUEST_LINEUP,
                () -> TeamLoader.fetchLineUp(_team, _saison)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                LineUpFragment::onItemsNext,
                LineUpFragment::onItemsError
        );

        if (savedState == null) {
            fetchData();
        }
    }

    public void fetchData()
    {
        start(REQUEST_LINEUP);
    }

}
