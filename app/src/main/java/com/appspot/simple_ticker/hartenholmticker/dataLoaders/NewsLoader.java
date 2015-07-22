package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import com.appspot.simple_ticker.hartenholmticker.data.NewsEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class NewsLoader
{
    private static final String NEWS_URL = "http://www.tushartenholm-liga.de/aktuelles-1/";

    public static Observable<List<NewsEntry>> fetch()
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
                            Elements elements = document.getElementById("cc-matrix-2649558093").getElementsByClass("j-textWithImage");
                            ArrayList<NewsEntry> news = new ArrayList<>();

                            for (Element element : elements)
                            {
                                String imageUrl = null;
                                String title = null;
                                String content = null;

                                Elements images =element.getElementsByTag("img");
                                if (!images.isEmpty())
                                {
                                    imageUrl = images.first().absUrl("src");
                                }

                                Elements text = element.getElementsByAttributeValue("data-name", "text");
                                if (!text.isEmpty())
                                {
                                    Elements paragraphs = text.first().getElementsByTag("p");
                                    if (paragraphs.size() == 1)
                                    {
                                        List<Node> nodes = paragraphs.first().childNodes();
                                        if (nodes.size() > 2)
                                        {
                                            title = nodes.get(0).outerHtml().trim();
                                            nodes.get(0).remove();
                                            nodes.get(0).remove();
                                        }
                                    }
                                    else if (paragraphs.size() > 1)
                                    {
                                        title = paragraphs.first().text();
                                        paragraphs.first().remove();
                                    }

                                    content = text.first().html();
                                }

                                if (title != null && content != null)
                                {
                                    news.add(new NewsEntry(title, content, imageUrl));
                                }
                            }

                            observer.onNext(news);
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
