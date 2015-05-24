package com.appspot.simple_ticker.hartenholmticker.ui.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.Table;


public class TableAdapter extends ArrayAdapter<Table.TableEntry>
{
    private Context _context;
    private Table _table;

    public TableAdapter(Context context, Table table)
    {
        super(context, R.layout.table_card, table.getEntries());
        _table = table;
        _context = context;
    }

    @Override
    public int getCount()
    {
        return _table.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Table.TableEntry entry = _table.get(position);

        View rowView = inflater.inflate(R.layout.table_card, parent, false);
        TextView rank = (TextView) rowView.findViewById(R.id.table_rank);
        TextView clubName = (TextView) rowView.findViewById(R.id.table_club_name);
        TextView points = (TextView) rowView.findViewById(R.id.table_points);

        rank.setText(String.valueOf(entry.getRank()));
        clubName.setText(entry.getName());
        points.setText(String.valueOf(entry.getPoints()));

        return rowView;
    }
}