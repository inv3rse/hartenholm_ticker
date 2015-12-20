package com.appspot.simple_ticker.hartenholmticker.ui.ticker;

import android.content.Intent;
import android.util.Log;

import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerClient;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;

public class TickerPresenter extends RxPresenter<TickerFragment>
{
    private static final int UPDATE_CODE = 888;

    private Game _currentGame;
    private TickerClient _client;

    private boolean _isLoading;

    public TickerPresenter(TickerClient tickerClient)
    {
        _client = tickerClient;
        _currentGame = null;
        _isLoading = false;

        Log.d("TickerPresenter", "new created");
    }

    @Override
    protected void onTakeView(TickerFragment view)
    {
        super.onTakeView(view);
        if (view != null)
        {
            if (_isLoading)
            {
                view.setLoading(true);
            } else
            {
                setLoading(true);
                _client.getLatestGame(false)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setGame, throwable -> {
                                    throwable.printStackTrace();
                                    setLoading(false);
                                }
                        );
            }
        }
    }

    public void updateCurrentGame()
    {
        if (!_isLoading)
        {
            setLoading(true);
            _client.getLatestGame(true)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setGame, throwable -> {
                                throwable.printStackTrace();
                                setLoading(false);
                            }
                    );
        }
    }

    public void createEntry()
    {
        if (_currentGame != null)
        {
            Intent intent = new Intent(getView().getContext(), TickerEntryActivity.class);
            intent.putExtra(TickerEntryActivity.EXTRA_GAME_ID, _currentGame.getId());
            getView().startActivityForResult(intent, UPDATE_CODE);
        }
    }

    public void editEntry(TickerEntry entry)
    {
        Intent intent = new Intent(getView().getContext(), TickerEntryActivity.class);
        intent.putExtra(TickerEntryActivity.EXTRA_GAME_ID, _currentGame.getId());
        intent.putExtra(TickerEntryActivity.EXTRA_CHANGE_ENTRY, entry);
        getView().startActivityForResult(intent, UPDATE_CODE);
    }

    public void removeEntry(String entryId)
    {
        _client.deleteEntry(_currentGame.getId(), entryId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setGame, Throwable::printStackTrace);
    }

    private void setGame(Game game)
    {
        _currentGame = game;

        TickerFragment view = getView();
        if (view != null)
        {
            view.setGame(game);
            _isLoading = false;
            view.setLoading(false);
        }
    }

    private void setLoading(boolean loading)
    {
        _isLoading = loading;
        TickerFragment view = getView();
        if (view != null)
        {
            view.setLoading(loading);
        }
    }
}
