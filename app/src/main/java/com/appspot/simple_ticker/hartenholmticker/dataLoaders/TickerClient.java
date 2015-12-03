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
        if (!sync && _game != null)
        {
            return Observable.defer(() -> Observable.just(_games));
        } else
        {
            return _tickerApi.listGames(maxNum).doOnNext(games -> _games = games);
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

    public Observable<Game> createGame(Game game)
    {
        return _tickerApi.createGame(game).doOnNext(newGame -> {
            _game = newGame;
            _games.add(0, newGame);
        });
    }

    public Observable<Game> createEntry(Game game, TickerEntry entry)
    {
        return _tickerApi.createEntry(game.getId(), entry)
                .flatMap(newEntry -> _tickerApi.getGame(game.getId()))
                .doOnNext(resultGame -> _game = resultGame);
    }

    public Observable<Game> deleteEntry(Game game, String entryID)
    {
        return _tickerApi.deleteEntry(game.getId(), entryID)
                .flatMap(message -> _tickerApi.getGame(game.getId()))
                .doOnNext(resultGame -> _game = resultGame);
    }
}
