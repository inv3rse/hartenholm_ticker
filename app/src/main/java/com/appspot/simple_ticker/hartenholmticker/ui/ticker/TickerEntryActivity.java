package com.appspot.simple_ticker.hartenholmticker.ui.ticker;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appspot.simple_ticker.hartenholmticker.MyApp;
import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.TickerEntry;
import com.appspot.simple_ticker.hartenholmticker.dataLoaders.TickerClient;

import java.util.Date;

public class TickerEntryActivity extends AppCompatActivity
{
    public static final String EXTRA_CHANGE_ENTRY = "extra_change_entry";
    public static final String EXTRA_GAME_ID = "extra_game_id";

    private static final String KEY_ICON_NR = "key_selected_icon";

    private EditText _minuteEdit;
    private EditText _contentEdit;
    private ImageView _iconView;

    private String _gameId;
    private TickerEntry _tickerEntry;
    private TickerClient _client;

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
        _iconView = (ImageView) findViewById(R.id.selectedIcon);
        _tickerEntry = null;

        Bundle arguments = getIntent().getExtras();
        if (arguments == null || (_gameId = arguments.getString(EXTRA_GAME_ID)) == null)
        {
            // wee need the game id or we can't commit any changes
            Toast.makeText(this, "MISSING GAME ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        _tickerEntry = arguments.getParcelable(EXTRA_CHANGE_ENTRY);

        if (_tickerEntry == null)
        {
            _tickerEntry = new TickerEntry(-1, "", -1);
        }

        if (savedInstanceState != null)
        {
            _tickerEntry.setIconNumber(savedInstanceState.getInt(KEY_ICON_NR));
        }

        // init ui fields
        setIcon(_tickerEntry.getIconNumber());
        if (_tickerEntry.getMinute() != -1)
        {
            _minuteEdit.setText(String.valueOf(_tickerEntry.getMinute()));
        }
        _contentEdit.setText(_tickerEntry.getContent());

        _client = ((MyApp) getApplication()).getAppComponent().getTickerClient();

        Button setIconBtn = (Button) findViewById(R.id.selectIconButton);
        setIconBtn.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            IconChooserDialog chooserDialog = new IconChooserDialog();
            chooserDialog.show(fragmentManager, "selectIcon");
        });

        Button commit = (Button) findViewById(R.id.entry_commit);
        commit.setOnClickListener(
                view ->
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
                            _client.editEntry(_gameId, _tickerEntry)
                                    .subscribe(
                                            result ->
                                            {
                                                System.out.println("successful");
                                                finish();
                                            },
                                            Throwable::printStackTrace
                                    );
                        } else
                        {
                            // create new entry
                            _client.createEntry(_gameId, _tickerEntry)
                                    .subscribe(
                                            result ->
                                            {
                                                System.out.println("successful");
                                                finish();
                                            },
                                            Throwable::printStackTrace
                                    );
                        }
                    }
                });

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ICON_NR, _tickerEntry.getIconNumber());
    }

    public void setIcon(int index)
    {
        _tickerEntry.setIconNumber(index);
        if (index >= 0 && index < ImageAdapter.images.length)
        {
            _iconView.setImageResource(ImageAdapter.images[index]);
        }
        else
        {
            _iconView.setImageResource(android.R.color.transparent);
        }
    }
 }
