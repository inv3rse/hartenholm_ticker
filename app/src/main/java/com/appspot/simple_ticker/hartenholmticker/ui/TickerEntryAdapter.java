package com.appspot.simple_ticker.hartenholmticker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;

import java.util.List;


public class TickerEntryAdapter extends ArrayAdapter<TickerEntry>
{
    private final Context _context;
    private final List<TickerEntry> _entries;

    public TickerEntryAdapter(Context context, List<TickerEntry> entries)
    {
        super(context, R.layout.ticker_card, entries);
        _context = context;
        _entries = entries;
    }

    @Override
    public int getCount()
    {
        return _entries.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TickerEntry entry = _entries.get(position);

        View rowView = inflater.inflate(R.layout.ticker_card, parent, false);
        TextView minute = (TextView) rowView.findViewById(R.id.ticker_card_minute);
        TextView content = (TextView) rowView.findViewById(R.id.ticker_card_content);

        minute.setText(String.valueOf(entry.getMinute()));
        content.setText(entry.getContent());

        return rowView;
    }
}
