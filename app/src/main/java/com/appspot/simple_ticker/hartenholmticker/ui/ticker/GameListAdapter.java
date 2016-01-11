package com.appspot.simple_ticker.hartenholmticker.ui.ticker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.Game;

import java.util.List;

public class GameListAdapter extends ArrayAdapter<Game>
{
    private final Context _context;
    private final List<Game> _games;

    public GameListAdapter(Context context, List<Game> games)
    {
        super(context, R.layout.game_entry, games);
        setDropDownViewResource(R.layout.game_entry_dropdown);

        _context = context;
        _games = games;
    }

    @Override
    public int getCount()
    {
        return _games.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createView(position, parent, R.layout.game_entry);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return createView(position, parent, R.layout.game_entry_dropdown);
    }

    private View createView(int position, ViewGroup parent, @LayoutRes int layoutRes)
    {
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Game game = _games.get(position);

        View rowView = inflater.inflate(layoutRes, parent, false);
        TextView text = (TextView) rowView.findViewById(R.id.textView);

        text.setText("Hartenholm - " + game.getEnemy());
        return rowView;
    }
}
