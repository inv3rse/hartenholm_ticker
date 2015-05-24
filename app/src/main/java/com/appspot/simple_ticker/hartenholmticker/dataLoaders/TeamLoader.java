package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import com.appspot.simple_ticker.hartenholmticker.data.Player;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class TeamLoader
{
    public static final String TEAM1_ID = "1. Herren";
    public static final String TEAM2_ID = "2. Herren";
    public static final String TEAM3_ID = "3. Herren";

    private static final String LINE_UP_URL = "http://www.tushartenholm.de/home.php?content=04&saison={saison}&team={team}";

    public static Observable<List<Player>> fetchLineUp(String team, String saison)
    {
        return Observable.create(
                observer ->
                {
                    try
                    {
                        if (!observer.isUnsubscribed())
                        {
                            ArrayList<Player> players = new ArrayList<>();
                            String currentPos = null;

                            Document document = Jsoup.connect(LINE_UP_URL.replace("{team}", team).replace("{saison}", saison)).get();
                            Elements content = document.select("#content > div[style] > div > div");
                            for (Element element : content)
                            {
                                Elements position = element.select("div > div > font");

                                if (!position.isEmpty())
                                {
                                    currentPos = position.first().text();
                                    continue;
                                }

                                Elements player = element.select("div > center");
                                if (!player.isEmpty() && currentPos != null)
                                {
                                    Elements name = player.first().select("p > font");
                                    Elements img = player.first().select("div > img");

                                    if (!name.isEmpty() && ! img.isEmpty())
                                    {
                                        players.add(new Player(name.first().text(), currentPos, img.first().absUrl("src")));
                                    }
                                }
                            }

                            observer.onNext(players);
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
