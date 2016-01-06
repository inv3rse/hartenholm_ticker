package com.appspot.simple_ticker.hartenholmticker.data;

import java.util.List;

public class GameListPair {
    public List<Game> allGames;
    public Game actualGame;

    private int _gameIndex;

    public GameListPair(List<Game> games, Game game) {
        allGames = games;
        actualGame = game;
        _gameIndex = games.indexOf(game);
        if (_gameIndex < 0) {
            _gameIndex = 0;
        }
    }

    public int getIndex() {
        return _gameIndex;
    }
}
