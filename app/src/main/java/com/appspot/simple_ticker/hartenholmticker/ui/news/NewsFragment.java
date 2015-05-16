package com.appspot.simple_ticker.hartenholmticker.ui.news;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.news.NewsFetcher;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment
{


    public NewsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        ListView news = (ListView) view.findViewById(R.id.news_list);

        // load news and populate view
        NewsFetcher.fetch()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> news.setAdapter(new NewsAdapter(news.getContext(), result)),
                        Throwable::printStackTrace
                );

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//    {
//        inflater.inflate(R.menu.appointments, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        int id = item.getItemId();
//        return super.onOptionsItemSelected(item);
//    }

}
