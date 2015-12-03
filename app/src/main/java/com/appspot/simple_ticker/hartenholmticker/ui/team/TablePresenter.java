package com.appspot.simple_ticker.hartenholmticker.ui.team;

import android.os.Bundle;

import com.appspot.simple_ticker.hartenholmticker.data.Table;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TableLoader;
import com.appspot.simple_ticker.hartenholmticker.ui.team.TableFragment;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TablePresenter extends RxPresenter<TableFragment>
{
    private Table _cachedData = null;
    private String _teamId;

    public TablePresenter(String teamId)
    {
        _teamId = teamId;
    }

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
    }

    @Override
    protected void onTakeView(TableFragment view)
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
        TableLoader.fetchTable(_teamId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .compose(this.<Table>deliverLatest())
                .subscribe(
                        table ->
                        {
                            _cachedData = table;
                            getView().onItemsNext(table);
                        },
                        error ->
                        {
                            getView().onItemsError(error);
                        }
                );
    }
}
