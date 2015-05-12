package com.appspot.simple_ticker.hartenholmticker.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TickerEntry implements Parcelable
{
    @SerializedName("id")
    private String _id;

    @SerializedName("minute")
    private int _minute;

    @SerializedName("content")
    private String _content;

    @SerializedName("postDate")
    private String _postDate;

    @SerializedName("iconNumber")
    private int _iconNumber;

    /**
     * Ticker entry for a game
     * @param minute minute something happend
     * @param content content of entry
     * @param iconNumber number of used icon (0 if there is no icon)
     */
    public TickerEntry(int minute, String content, int iconNumber)
    {
        _id = "-1";
        _minute = minute;
        _content = content;
        _iconNumber = iconNumber;
        _postDate = Game.DATE_FORMAT.format(new Date());
    }


    private TickerEntry(String id, int minute, String content, int iconNumber, String postDate)
    {
        _id = id;
        _minute = minute;
        _content = content;
        _iconNumber = iconNumber;
        _postDate = postDate;
    }

    public void setContent(String content)
    {
        _content = content;
    }

    public void setMinute(int minute)
    {
        _minute = minute;
    }

    public void setIconNumber(int iconNumber)
    {
        _iconNumber = iconNumber;
    }

    public void setPostDate(Date date)
    {
        _postDate = Game.DATE_FORMAT.format(date);
    }

    public String getId()
    {
        return _id;
    }

    public int getMinute()
    {
        return _minute;
    }

    public String getContent()
    {
        return _content;
    }

    public int getIconNumber()
    {
        return _iconNumber;
    }

    public boolean idIsValid()
    {
        return !_id.equals("-1");
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(_id);
        parcel.writeInt(_minute);
        parcel.writeString(_content);
        parcel.writeInt(_iconNumber);
        parcel.writeString(_postDate);
    }

    // for Parcelable interface
    public static final Creator<TickerEntry> CREATOR = new Creator<TickerEntry>()
    {
        @Override
        public TickerEntry createFromParcel(Parcel parcel)
        {
            String id = parcel.readString();
            int minute = parcel.readInt();
            String content = parcel.readString();
            int iconNumber = parcel.readInt();
            String postDate = parcel.readString();
            return new TickerEntry(id, minute, content, iconNumber, postDate);
        }

        @Override
        public TickerEntry[] newArray(int size)
        {
            return new TickerEntry[size];
        }
    };
}
