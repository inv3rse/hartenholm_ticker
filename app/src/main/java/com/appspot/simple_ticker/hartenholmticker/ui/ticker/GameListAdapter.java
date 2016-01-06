package com.appspot.simple_ticker.hartenholmticker.ui.ticker;

import android.content.Context;
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
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Game game = _games.get(position);

        View rowView = inflater.inflate(R.layout.game_entry, parent, false);
        TextView team1 = (TextView) rowView.findViewById(R.id.team1);
        TextView team1_score = (TextView) rowView.findViewById(R.id.team1_score);
        TextView team2 = (TextView) rowView.findViewById(R.id.team2);
        TextView team2_score = (TextView) rowView.findViewById(R.id.team2_score);

        team1.setText("Hartenholm");
        team1_score.setText("");

        team2.setText(game.getEnemy());
        team2_score.setText("");

        return rowView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(position, convertView, parent);
    }
}
