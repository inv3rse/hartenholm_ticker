package com.appspot.simple_ticker.hartenholmticker.ui.team;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.Player;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dev.dworks.libs.astickyheader.SimpleSectionedGridAdapter;
import dev.dworks.libs.astickyheader.SimpleSectionedGridAdapter.Section;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

@RequiresPresenter(LineUpPresenter.class)
public class LineUpFragment extends NucleusSupportFragment<LineUpPresenter>
{
    private static final String ARG_TEAM_ID = "TEAM_ID";

    static public LineUpFragment create(String websiteId)
    {
        LineUpFragment fragment = new LineUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_ID, websiteId);
        fragment.setArguments(args);
        return fragment;
    }

    private String _teamID;
    private GridView _gridView;

    @Override
    public PresenterFactory<LineUpPresenter> getPresenterFactory()
    {
        return () -> new LineUpPresenter(_teamID, "2014/15");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _teamID = getArguments().getString(ARG_TEAM_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_line_up, container, false);

        _gridView = (GridView) view.findViewById(R.id.grid);

        return view;
    }

    /**
     * Display players
     * @param players List of players sorted by position
     */
    public void onItemsNext(List<Player> players)
    {
        // get indexes for headers (positions)
        ArrayList<Section> sections = new ArrayList<>();

        String currPos = null;

        for (int i = 0; i < players.size(); i++)
        {
            if (currPos == null || !players.get(i).getPosition().equals(currPos))
            {
                sections.add(new Section(i, players.get(i).getPosition()));
                currPos = players.get(i).getPosition();
            }
        }

        // adapter needs an array
        Section[] sectionsArray = new Section[sections.size()];
        sections.toArray(sectionsArray);

        // put them into the grid
        SimpleSectionedGridAdapter gridAdapter = new SimpleSectionedGridAdapter(
                getActivity(),
                new PlayerAdapter(getActivity(), players),
                R.layout.position_header,
                R.id.position_header_layout,
                R.id.position_header);

        gridAdapter.setGridView(_gridView);
        gridAdapter.setSections(sectionsArray);

        _gridView.setAdapter(gridAdapter);
    }

    public void onItemsError(Throwable throwable)
    {
        Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private class PlayerAdapter extends BaseAdapter
    {
        private Context _context;
        private LayoutInflater _inflater;
        private List<Player> _players;

        public PlayerAdapter(Context context, List<Player> players)
        {
            _context = context;
            _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _players = players;
        }

        @Override
        public int getCount()
        {
            return _players.size();
        }

        @Override
        public Object getItem(int i)
        {
            return _players.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup group)
        {
            View player = _inflater.inflate(R.layout.player_view, group, false);

            ImageView image = (ImageView) player.findViewById(R.id.player_image);
            TextView name = (TextView) player.findViewById(R.id.player_name);

            name.setText(_players.get(i).getName());
            Picasso.with(_context).load(_players.get(i).getImageUrl()).into(image);

            return player;
        }
    }
}
