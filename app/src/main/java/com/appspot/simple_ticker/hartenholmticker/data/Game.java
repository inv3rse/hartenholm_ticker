package com.appspot.simple_ticker.hartenholmticker.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Game implements Parcelable {
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMAN);

    @SerializedName("id")
    private String _id;

    @SerializedName("team1")
    private String _team1;

    @SerializedName("team2")
    private String _team2;

    @SerializedName("date")
    private String _date;

    @SerializedName("entries")
    private List<TickerEntry> _entries;

    public Game(String team1, String team2, Date date) {
        _id = "currentGame";
        _team1 = team1;
        _team2 = team2;
        _date = DATE_FORMAT.format(date);
        _entries = new ArrayList<>();
    }

    private Game(String id, String date, String team1, String team2, List<TickerEntry> entries) {
        _id = id;
        _date = date;
        _team1 = team1;
        _team2 = team2;
        _entries = entries;
    }

    public String getId() {
        return _id;
    }

    void setId(String id) {
        _id = id;
    }

    public String getTeam1() {
        return _team1;
    }

    public String getTeam2() {
        return _team2;
    }

    void setTeam1(String team) {
        _team1 = team;
    }

    void setTeam2(String team) {
        _team2 = team;
    }

    public String getDateString() {
        return _date;
    }

    public Date getDate() {
        try {
            return DATE_FORMAT.parse(_date);
        } catch (ParseException e) {
            return null;
        }
    }

    public List<TickerEntry> getEntries() {
        return _entries;
    }

    void setDate(Date date) {
        _date = DATE_FORMAT.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        TickerEntry entryArray[] = new TickerEntry[_entries.size()];
        _entries.toArray(entryArray);

        parcel.writeString(_id);
        parcel.writeString(_date);
        parcel.writeString(_team1);
        parcel.writeString(_team2);
        parcel.writeParcelableArray(entryArray, PARCELABLE_WRITE_RETURN_VALUE);
    }

    // for Parcelable interface
    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel parcel) {
            String id = parcel.readString();
            String date = parcel.readString();
            String team1 = parcel.readString();
            String team2 = parcel.readString();

            TickerEntry[] entries = (TickerEntry[]) parcel.readParcelableArray(TickerEntry.class.getClassLoader());
            return new Game(id, date, team1, team2, Arrays.asList(entries));
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return !(_id != null ? !_id.equals(game._id) : game._id != null);

    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }
}
