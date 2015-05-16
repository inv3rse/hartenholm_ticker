package com.appspot.simple_ticker.hartenholmticker.dataLoaders;

import com.appspot.simple_ticker.hartenholmticker.data.Table;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import rx.Observable;

public class TableLoader
{
    public static String ID_HARTENHOLM_1 = "011MIAN7RO000000VTVG0001VTR8C1K7";
    public static String ID_HARTENHOLM_2 = "011MIC26JK000000VTVG0001VTR8C1K7";
    public static String ID_HARTENHOLM_3 = "016G1UU77K000000VV0AG80NVV638PD6";

    // der name der Mannschaft ist in der URL anscheinend unwichtig
    // wird eine ungültige Saison übergeben, wird die Aktuelle angezeigt
    // nur die id muss in der URL angepasst werden
    private static String URL = "http://www.fussball.de/mannschaft/xxx/-/saison/xxx/team-id/{id}";

    public static Observable<Table> fetchTable(String id)
    {
        return Observable.create(
                observer ->
                {
                    try
                    {
                        if (!observer.isUnsubscribed())
                        {
                            String leagueStr;
                            ArrayList<Table.TableEntry> entries = new ArrayList<>();

                            Document document = Jsoup.connect(URL.replace("{id}", id)).get();
                            Elements league = document.select("div.inner > a.profile-value");

                            if (league.isEmpty())
                            {
                                observer.onError(new IOException("can not load table data"));
                                return;
                            }

                            leagueStr = league.first().text();

                            // fetch all table entries
                            Elements rows = document.select("#team-fixture-league-tables > table.table > tbody > tr");
                            for (Element element : rows)
                            {
                                Elements children = element.children();
                                if (children.size() == 10)
                                {
                                    Elements clubElement = children.get(2).select(".club-name");
                                    if (!clubElement.isEmpty())
                                    {
                                        String rankStr = children.get(1).text();
                                        String clubName = clubElement.text();
                                        boolean isOwn = element.classNames().contains("own");

                                        try
                                        {
                                            int rank = Integer.parseInt(rankStr.substring(0, rankStr.length() - 1));
                                            int numGames = Integer.parseInt(children.get(3).text());
                                            int numWins = Integer.parseInt(children.get(4).text());
                                            int numDraws = Integer.parseInt(children.get(5).text());
                                            int numLosses = Integer.parseInt(children.get(6).text());
                                            int goalDiff = Integer.parseInt(children.get(8).text());
                                            int points = Integer.parseInt(children.get(9).text());

                                            entries.add(new Table.TableEntry(isOwn, rank, numGames, points, numWins, numLosses, numDraws, goalDiff, clubName));
                                        }
                                        catch (NumberFormatException ignore)
                                        {
                                        }
                                    }
                                }
                            }
                            observer.onNext(new Table(entries, leagueStr));
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
