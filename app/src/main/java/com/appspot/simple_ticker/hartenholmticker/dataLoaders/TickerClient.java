package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.appspot.simple_ticker.hartenholmticker.data.GameListPair;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;

import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class TickerClient {
    private static final String API_BASE_URL = "https://simple-ticker.appspot.com/";

    private TickerApi _tickerApi;

    private List<Game> _games;  // local copy of server games (without entries)
    private Game _game;  // last loaded game (with entries)

    public TickerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        _tickerApi = retrofit.create(TickerApi.class);

        _games = null;
        _game = null;
    }

    public Observable<List<Game>> loadGames(boolean sync) {
        return loadGames(10, sync);
    }

    public Observable<List<Game>> loadGames(int maxNum, boolean sync) {
        if (!sync && _games != null) {
            return Observable.defer(() -> Observable.just(_games));
        } else {
            return _tickerApi.listGames(maxNum)
                    .subscribeOn(Schedulers.io())
                    .doOnNext(games -> _games = games);
        }
    }

    public Observable<Game> loadGame(String id, boolean sync) {
        if (!sync && _game != null && id.equals(_game.getId())) {
            return Observable.defer(() -> Observable.just(_game));
        } else {
            return _tickerApi.getGame(id)
                    .subscribeOn(Schedulers.io())
                    .doOnNext(game -> _game = game);
        }
    }

    public Observable<GameListPair> getLatest(boolean sync) {
        if (!sync && _game != null && _games != null) {
            return Observable.defer(() -> Observable.just(new GameListPair(_games, _game)));
        } else {
            return loadGames(true)
                    .filter(games -> !games.isEmpty())
                    .flatMap(games1 -> loadGame(_game != null? _game.getId() : games1.get(0).getId(), true)
                            .map(actualGame -> new GameListPair(games1, actualGame)));
        }
    }

    public Observable<Game> createGame(Game game) {
        return _tickerApi.createGame(game)
                .subscribeOn(Schedulers.io())
                .doOnNext(newGame -> {
                    _game = newGame;
                    if (_games != null) {
                        _games.add(0, newGame);
                    }
                });
    }

    public Observable<GameListPair> deleteGame(Game game) {
        return _tickerApi.deleteGame(game.getId())
                .subscribeOn(Schedulers.io())
                .doOnNext(message0 -> {
                    _game = null;
                    _games = null;
                })
                .flatMap(message -> getLatest(true));
    }

    public Observable<Game> createEntry(String gameId, TickerEntry entry) {
        return _tickerApi.createEntry(gameId, entry)
                .subscribeOn(Schedulers.io())
                .flatMap(newEntry -> loadGame(gameId, true));
    }

    public Observable<Game> editEntry(String gameId, TickerEntry entry) {
        return _tickerApi.editEntry(gameId, entry.getId(), entry)
                .subscribeOn(Schedulers.io())
                .flatMap(message -> loadGame(gameId, true));
    }

    public Observable<Game> deleteEntry(String gameId, String entryID) {
        return _tickerApi.deleteEntry(gameId, entryID)
                .subscribeOn(Schedulers.io())
                .flatMap(message -> loadGame(gameId, true));
    }
}
