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
    public static final int UPDATE_CODE = 888;

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
            } else if (_currentGame != null)
            {
                setGame(_currentGame);
            } else
            {
                updateCurrentGame();
            }
        }
    }

    public void updateCurrentGame()
    {
        if (!_isLoading)
        {
            setLoading(true);
            if (_currentGame == null)
            {
                _client.loadGames(true)
                        .filter(games -> !games.isEmpty())
                        .flatMap(allGames -> _client.loadGame(allGames.get(0).getId(), true))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setGame, Throwable::printStackTrace);
            } else
            {
                _client.loadGame(_currentGame.getId(), true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setGame, Throwable::printStackTrace);
            }
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
        _client.deleteEntry(_currentGame, entryId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> updateCurrentGame(), Throwable::printStackTrace);
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
