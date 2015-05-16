package com.appspot.simple_ticker.hartenholmticker.data;


import java.util.List;

public class Table
{
    private List<TableEntry> _entries;
    private String _leagueName;

    public Table(List<TableEntry> entries, String name)
    {
        _entries = entries;
        _leagueName = name;
    }

    public TableEntry get(int index)
    {
//        assert index < size() && index >= 0;
        return _entries.get(index);
    }

    public int size()
    {
        return _entries.size();
    }

    public List<TableEntry> getEntries()
    {
        return _entries;
    }

    public void setEntries(List<TableEntry> entries)
    {
        _entries = entries;
    }

    public String getLeagueName()
    {
        return _leagueName;
    }

    public void setLeagueName(String leagueName)
    {
        _leagueName = leagueName;
    }

    public static class TableEntry
    {
        private boolean _isOwn;
        private int _rank;
        private int _numGames;
        private int _points;
        private int _wins;
        private int _losses;
        private int _draws;
        private int _goalDiff;
        private String _name;

        public TableEntry(boolean own, int rank, int numGames, int points, int wins, int losses, int draws, int goalDiff, String name)
        {
            _isOwn = own;
            _rank = rank;
            _numGames = numGames;
            _points = points;
            _wins = wins;
            _losses = losses;
            _draws = draws;
            _goalDiff = goalDiff;
            _name = name;
        }

        public boolean isOwn()
        {
            return _isOwn;
        }

        public int getRank()
        {
            return _rank;
        }

        public int getNumGames()
        {
            return _numGames;
        }

        public int getPoints()
        {
            return _points;
        }

        public int getWins()
        {
            return _wins;
        }

        public int getLosses()
        {
            return _losses;
        }

        public int getDraws()
        {
            return _draws;
        }

        public int getGoalDiff()
        {
            return _goalDiff;
        }

        public String getName()
        {
            return _name;
        }
    }
}
