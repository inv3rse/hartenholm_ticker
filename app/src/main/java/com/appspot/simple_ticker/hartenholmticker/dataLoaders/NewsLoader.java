package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import rx.Observable;

public class NewsLoader
{
    private static String NEWS_URL = "http://www.tushartenholm-liga.de/aktuelles/";

    public static Observable<Elements> fetch()
    {
        System.out.println("fetching news");
        return Observable.create(
                observer ->
                {
                    try
                    {
                        if (!observer.isUnsubscribed())
                        {
                            Document document = Jsoup.connect(NEWS_URL).get();
                            Elements elements = document.getElementById("content_area").getElementsByClass("j-text");

                            // remove the last element which is a repetition of the first or the second
                            // if it has a date
                            for (Element element : elements)
                            {
                                Elements children = element.children();
                                if (children.size() >=  3 && (
                                        children.first().equals(children.last())) ||
                                        children.get(1).equals(children.last()))
                                {
                                    children.last().remove();
                                }
                            }

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
