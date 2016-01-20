package com.appspot.simple_ticker.hartenholmticker.ui.ticker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.appspot.simple_ticker.hartenholmticker.R;

class ImageAdapter extends BaseAdapter
{
    private Context _context;
    private int _selected = -1;

    public ImageAdapter(Context context)
    {
        _context = context;
    }

    @Override
    public int getCount()
    {
        return images.length;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group)
    {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(_context);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(images[position]);

        if (position == _selected)
        {
            imageView.setBackgroundResource(R.color.highlight);
        }
        else
        {
            imageView.setBackgroundResource(android.R.color.transparent);
        }

        return imageView;
    }

    public void setSelection(int pos, boolean selected)
    {
        if (selected)
        {
            _selected = pos;
        }
        else
        {
            _selected = -1;
        }
        notifyDataSetChanged();
    }

    public static final Integer[] images =
            {
                    R.drawable.ic_ball,
                    R.drawable.ic_auswechslung,
                    R.drawable.ic_gelbe_karte,
                    R.drawable.ic_rote_karte,
                    R.drawable.ic_gelb_rote_karte,
                    R.drawable.ic_linienflagge,
                    R.drawable.ic_pfeiffe
            };
}