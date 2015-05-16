package com.appspot.simple_ticker.hartenholmticker.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import rx.Observable;

public class NewsFetcher
{
    private static String NEWS_URL = "http://www.tushartenholm-liga.de/aktuelles/";

    public static Observable<Elements> fetch()
    {
        return Observable.create(
                observer ->
                {
                    try
                    {
                        if (!observer.isUnsubscribed())
                        {
                            Document document = Jsoup.connect(NEWS_URL).get();
                            Elements elements = document.getElementById("content_area").getElementsByClass("j-text");

                            observer.onNext(elements);
                        }
                        observer.onCompleted();
                    } catch (IOException e)
                    {
                        observer.onError(e);
                    }
                }
        );
    }
}
