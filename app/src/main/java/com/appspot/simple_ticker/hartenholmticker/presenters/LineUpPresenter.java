package com.appspot.simple_ticker.hartenholmticker.presenters;


import android.os.Bundle;

import com.appspot.simple_ticker.hartenholmticker.data.Player;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TeamLoader;
import com.appspot.simple_ticker.hartenholmticker.ui.team.LineUpFragment;

import java.util.List;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LineUpPresenter extends RxPresenter<LineUpFragment>
{
    private List<Player> _cachedData = null;
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
    }

    @Override
    protected void onTakeView(LineUpFragment view)
    {
        super.onTakeView(view);
        if (view != null)
        {
            if (_cachedData != null)
            {
                getView().onItemsNext(_cachedData);
            } else
            {
                fetchData();
            }
        }
    }

    public void fetchData()
    {
        TeamLoader.fetchLineUp(_team, _saison)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        players ->
                        {
                            _cachedData = players;
                            getView().onItemsNext(players);
                        },
                        error ->
                        {
                            getView().onItemsError(error);
                        }
                );
    }

}
