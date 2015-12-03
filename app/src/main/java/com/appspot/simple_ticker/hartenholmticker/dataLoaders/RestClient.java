package com.appspot.simple_ticker.hartenholmticker.dataLoaders;


import retrofit.RestAdapter;

public class RestClient
{
    private static final String baseUrl = "https://simple-ticker.appspot.com";
    private static TickerApi api = null;

    public static TickerApi getApi()
    {
        if (api == null)
        {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(baseUrl)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            api = restAdapter.create(TickerApi.class);
        }

        return api;
    }
}
