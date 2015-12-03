package com.appspot.simple_ticker.hartenholmticker.data;

import com.google.gson.annotations.SerializedName;

public class Message
{
    @SerializedName("Statuscode")
    private int _statusCode;

    @SerializedName("msg")
    private String _message;

    public Message()
    {}

    public int getStatusCode()
    {
        return _statusCode;
    }
}
