package com.appspot.simple_ticker.hartenholmticker.ui.ticker;

import android.content.Intent;
import android.util.Log;

import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.appspot.simple_ticker.hartenholmticker.data.GameListPair;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerClient;

import java.util.List;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;

public class TickerPresenter extends RxPresenter<TickerFragment>
{
    private static final int UPDATE_CODE = 888;

    private Game _currentGame;
    private List<Game> _games;
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

                _client.getLatest(false)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setGameAndList, this::loadError, () -> setLoading(false));
            }
        }
    }

    protected void onDropView()
    {
        _games = null;
        _currentGame = null;
    }

    public void selectGame(int index)
    {
        if (!_isLoading)
        {
            Game selected = _games.get(index);
            if (_currentGame == null || !selected.getId().equals(_currentGame.getId()))
            {
                setLoading(true);
                _client.loadGame(_games.get(index).getId(), true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setGame, this::loadError, () -> setLoading(false));
            }
        }
    }

    public void updateCurrentGame()
    {
        if (!_isLoading)
        {
            setLoading(true);

            _client.getLatest(true)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setGameAndList, this::loadError, () -> setLoading(false));
        }
    }

    public void deleteGame()
    {
        if (!_isLoading)
        {
            setLoading(true);

            _client.deleteGame(_currentGame)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setGameAndList, this::loadError, () -> setLoading(false));
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
        if (!_isLoading)
        {
            setLoading(true);
            _client.deleteEntry(_currentGame.getId(), entryId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setGame, this::loadError, ()->setLoading(false));
        }
    }

    private void setGame(Game game)
    {
        TickerFragment view = getView();
        if (view != null)
        {
            view.setGame(game);
        }

        _currentGame = game;
    }

    private void setGameAndList(GameListPair game)
    {
        TickerFragment view = getView();
        if (view != null)
        {
            if (!game.allGames.equals(_games))
            {
                view.setGameList(game.allGames, game.getIndex());
            }
            view.setGame(game.actualGame);
        }

        _games = game.allGames;
        _currentGame = game.actualGame;
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

    private void loadError(Throwable throwable)
    {
        throwable.printStackTrace();
        setLoading(false);
    }
}
