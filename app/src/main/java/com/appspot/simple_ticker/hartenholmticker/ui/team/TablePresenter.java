package com.appspot.simple_ticker.hartenholmticker.ui.team;

import android.os.Bundle;

import com.appspot.simple_ticker.hartenholmticker.base.network.TableLoader;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TablePresenter extends RxPresenter<TableFragment>
{
    private static final int REQUEST_TABLE = 1;
    private String _teamId;

    public TablePresenter(String teamId)
    {
        _teamId = teamId;
    }

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        restartableLatestCache(
                REQUEST_TABLE,
                () -> TableLoader.fetchTable(_teamId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                TableFragment::onItemsNext,
                TableFragment::onItemsError
        );

        if (savedState == null) {
            fetchData();
        }
    }

    public void fetchData()
    {
        start(REQUEST_TABLE);
    }
}
