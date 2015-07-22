package com.appspot.simple_ticker.hartenholmticker.ui.news;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.NewsEntry;

import org.jsoup.nodes.Element;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsEntry>
{
    private Context _context;
    private List<NewsEntry> _elements;

    public NewsAdapter(Context context, List<NewsEntry> elements)
    {
        super(context, R.layout.news_card, elements);
        _elements = elements;
        _context = context;
    }

    @Override
    public int getCount()
    {
        return _elements.size();
    }

    @Override
    public void clear()
    {
        super.clear();
        _elements.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        NewsEntry newsEntry = _elements.get(position);

        View rowView = inflater.inflate(R.layout.news_card, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.news_card_title);
        TextView content = (TextView) rowView.findViewById(R.id.news_card_content);

        title.setText(newsEntry.getTitle());
        content.setText(Html.fromHtml(newsEntry.getContent()));

        return rowView;
    }
}
