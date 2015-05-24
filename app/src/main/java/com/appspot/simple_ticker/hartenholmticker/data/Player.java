package com.appspot.simple_ticker.hartenholmticker.data;

public class Player
{
    private String _name;
    private String _position;
    private String _imageUrl;

    public Player(String name, String position, String imageUrl)
    {
        _name = name;
        _position = position;
        _imageUrl = imageUrl.replace(" ", "%20");
    }

    public String getName()
    {
        return _name;
    }

    public String getPosition()
    {
        return _position;
    }

    public String getImageUrl()
    {
        return _imageUrl;
    }
}
