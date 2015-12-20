package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;

import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;

public class TickerClient
{
    private static final String API_BASE_URL = "http://simple-ticker.appspot.com";

    private TickerApi _tickerApi;

    private List<Game> _games;  // local copy of server games (without entries)
    private Game _game;  // last loaded game (with entries)

    public TickerClient()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        _tickerApi = restAdapter.create(TickerApi.class);

        _games = null;
        _game = null;
    }

    public Observable<List<Game>> loadGames(boolean sync)
    {
        return loadGames(10, sync);
    }

    public Observable<List<Game>> loadGames(int maxNum, boolean sync)
    {
        if (!sync && _games != null)
        {
            return Observable.defer(() -> Observable.just(_games));
        } else
        {
            return _tickerApi.listGames(maxNum)
                    .doOnNext(games -> _games = games);
        }
    }

    public Observable<Game> loadGame(String id, boolean sync)
    {
        if (!sync && _game != null && id.equals(_game.getId()))
        {
            return Observable.defer(() -> Observable.just(_game));
        } else
        {
            return _tickerApi.getGame(id).doOnNext(game -> _game = game);
        }
    }

    public Observable<Game> getLatestGame(boolean sync)
    {
        if (!sync && _game != null)
        {
            return Observable.defer(() -> Observable.just(_game));
        } else
        {
            return loadGames(true)
                    .filter(games -> !games.isEmpty())
                    .flatMap(gamesF -> loadGame(gamesF.get(0).getId(), true));
        }
    }

    public Observable<Game> createGame(Game game)
    {
        return _tickerApi.createGame(game).doOnNext(newGame -> {
            _game = newGame;
            if (_games != null)
            {
                _games.add(0, newGame);
            }
        });
    }

    public Observable<Game> createEntry(String gameId, TickerEntry entry)
    {
        return _tickerApi.createEntry(gameId, entry)
                .flatMap(newEntry -> _tickerApi.getGame(gameId))
                .doOnNext(resultGame -> {
                    this._game = resultGame;
                    System.out.println("created entry");
                });
    }

    public Observable<Game> editEntry(String gameId, TickerEntry entry)
    {
        return _tickerApi.editEntry(gameId, entry.getId(), entry)
                .flatMap(message -> _tickerApi.getGame(gameId))
                .doOnNext(resultGame -> {
                    _game = resultGame;
                });
    }

    public Observable<Game> deleteEntry(String gameId, String entryID)
    {
        return _tickerApi.deleteEntry(gameId, entryID)
                .flatMap(message -> _tickerApi.getGame(gameId))
                .doOnNext(resultGame -> _game = resultGame);
    }
}
