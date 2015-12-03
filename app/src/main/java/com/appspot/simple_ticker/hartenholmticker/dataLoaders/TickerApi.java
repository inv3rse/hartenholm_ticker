package com.appspot.simple_ticker.hartenholmticker.dataLoaders;


import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.appspot.simple_ticker.hartenholmticker.data.Message;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface TickerApi
{
    @GET("/games")
    Observable<List<Game>> listGames(@Query("num") int num);

    @POST("/games")
    Observable<Game> createGame(@Body Game game);

    @GET("/game/{id}")
    Observable<Game> getGame(@Path("id") String id);

    @GET("/currentGame")
    Observable<Game> getCurrentGame();

    @POST("/game/{game_id}")
    Observable<TickerEntry> createEntry(@Path("game_id") String gameId, @Body TickerEntry entry);

    @PUT("/game/{game_id}/{entry_id}")
    Observable<TickerEntry> editEntry(@Path("game_id") String gameId, @Path("entry_id") String entryId, @Body TickerEntry entry);

    @DELETE("/game/{game_id}/{entry_id}")
    Observable<Message> deleteEntry(@Path("game_id") String game_id, @Path("entry_id") String entryId);

    @DELETE("/game/{id}")
    void deleteGame(@Path("id") String id);
}
