package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import rx.Observable;

public class SimplifiedPageLoader
{
    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";

    /**
     * Reduces the Document to its content
     * @param document the document that will be simplified
     * @return the simplified document
     */
    public static Document simplifyDocument(Document document)
    {
        document.getElementsByTag("header").remove();
        document.getElementsByClass("menubtn").remove();
        document.getElementsByClass("tp-changeview").remove();
        document.getElementsByTag("footer").remove();

        Element element = document.getElementById("cc-website-title");
        if (element != null)
        {
            element.remove();
        }
        return document;
    }

    /**
     * Loads and reduces the page to its main content
     * @param pageUrl url to load
     * @return Html String
     */
    public static Observable<String> loadreducedPage(String pageUrl)
    {
        return Observable.create(
                observer ->
                {
                    try
                    {
                        if (!observer.isUnsubscribed())
                        {
                            Document document = Jsoup.connect(pageUrl).userAgent(USER_AGENT).get();
                            document = simplifyDocument(document);

                            observer.onNext(document.outerHtml());
                            observer.onCompleted();
                        }

                    } catch (IOException e)
                    {
                        observer.onError(e);
                    }
                }
        );
    }
}
