package com.appspot.simple_ticker.hartenholmticker.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;
import com.appspot.simple_ticker.hartenholmticker.rest.RestClient;
import com.appspot.simple_ticker.hartenholmticker.rest.TickerApi;

import java.util.Date;

public class TickerEntryActivity extends AppCompatActivity
{
    public static final String EXTRA_CHANGE_ENTRY = "extra_change_entry";
    public static final String EXTRA_GAME_ID = "extra_game_id";

    private EditText _minuteEdit;
    private EditText _contentEdit;

    private String _gameId;
    private TickerEntry _tickerEntry;
    private TickerApi _api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.title_write_tickerentry);

        _minuteEdit = (EditText) findViewById(R.id.entry_minute);
        _contentEdit = (EditText) findViewById(R.id.entry_content);
        _tickerEntry = null;

        Bundle arguments = getIntent().getExtras();
        if (arguments == null || (_gameId = arguments.getString(EXTRA_GAME_ID)) == null)
        {
            // wee need the game id or we can't commit any changes
            Toast.makeText(this, "MISSING GAME ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        _tickerEntry =arguments.getParcelable(EXTRA_CHANGE_ENTRY);

        // init ui fields
        if (_tickerEntry == null)
        {
            _tickerEntry = new TickerEntry(-1, "", 0);
        }
        if (_tickerEntry.getMinute() != -1)
        {
            _minuteEdit.setText(String.valueOf(_tickerEntry.getMinute()));
        }
        _contentEdit.setText(_tickerEntry.getContent());

        _api = RestClient.getApi();

        Button commit = (Button) findViewById(R.id.entry_commit);
        commit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String minute = _minuteEdit.getText().toString();
                String content = _contentEdit.getText().toString();

                if (!minute.isEmpty() && !content.isEmpty())
                {
                    _tickerEntry.setContent(content);
                    _tickerEntry.setMinute(Integer.parseInt(minute));
                    _tickerEntry.setPostDate(new Date());

                    if (_tickerEntry.idIsValid())
                    {
                        // change existing entry
                        _api.editEntry(_gameId, _tickerEntry.getId(), _tickerEntry).subscribe(
                                result ->
                                {
                                    System.out.println("successfull");
                                    finish();
                                },
                                error -> error.printStackTrace()
                        );
                    }
                    else
                    {
                        // create new entry
                        _api.createEntry(_gameId, _tickerEntry).subscribe(
                                result ->
                                {
                                    System.out.println("successfull");
                                    finish();
                                },
                                error -> error.printStackTrace()
                        );
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ticker_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
