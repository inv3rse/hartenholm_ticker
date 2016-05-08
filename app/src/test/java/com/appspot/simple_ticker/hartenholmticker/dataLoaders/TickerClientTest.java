package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import com.appspot.simple_ticker.hartenholmticker.base.network.TickerApiBuilder;
import com.appspot.simple_ticker.hartenholmticker.base.network.TickerClient;
import com.appspot.simple_ticker.hartenholmticker.data.Game;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.List;

import rx.observers.TestSubscriber;

public class TickerClientTest extends TestCase
{
    private TickerClient _client;

    @Before
    public void setUp()
    {
        _client = new TickerClient(TickerApiBuilder.buildTickerApi());
    }

    public void testLoadGames() throws Exception
    {
        TestSubscriber<List<Game>> subscriber = new TestSubscriber<>();
        _client.loadGames(true).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        subscriber.assertNoErrors();
        assertFalse(subscriber.getOnNextEvents().isEmpty());
    }

    public void testLoadGame() throws Exception
    {
        assertEquals(1, 1);
    }
}