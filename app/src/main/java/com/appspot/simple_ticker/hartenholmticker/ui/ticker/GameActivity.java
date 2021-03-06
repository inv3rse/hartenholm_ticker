package com.appspot.simple_ticker.hartenholmticker.ui.ticker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.appspot.simple_ticker.hartenholmticker.base.App;
import com.appspot.simple_ticker.hartenholmticker.R;
import com.appspot.simple_ticker.hartenholmticker.data.Game;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.GERMAN);
    private static final SimpleDateFormat SAVE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN);

    private static final String KEY_SELECTED_DATE = "KEY_SELECTED_DATE";

    private EditText _team1Edit;
    private EditText _team2Edit;
    private EditText _dateEdit;
    private EditText _timeEdit;
    private Calendar _selectedDate;

    public GameActivity()
    {
        _selectedDate = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null)
        {
            try
            {
                Date date = SAVE_FORMAT.parse(savedInstanceState.getString(KEY_SELECTED_DATE));
                _selectedDate.setTime(date);
            } catch (ParseException ignored)
            {
            }
        }

        _team1Edit = (EditText) findViewById(R.id.game_team1);
        _team2Edit = (EditText) findViewById(R.id.game_team2);
        _dateEdit = (EditText) findViewById(R.id.game_date);
        _timeEdit = (EditText) findViewById(R.id.game_time);

        Toolbar toolbar = (Toolbar) findViewById(R.id.game_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.game_edit_title);

        _dateEdit.setText(DATE_FORMAT.format(_selectedDate.getTime()));
        _dateEdit.setOnClickListener(
                view ->
                {
                    DatePickerDialog datepickerDialog = DatePickerDialog.newInstance(
                            GameActivity.this,
                            _selectedDate.get(Calendar.YEAR),
                            _selectedDate.get(Calendar.MONTH),
                            _selectedDate.get(Calendar.DAY_OF_MONTH)
                    );
                    datepickerDialog.show(getFragmentManager(), "DatePickerDialog");
                }
        );

        _timeEdit.setText(TIME_FORMAT.format(_selectedDate.getTime()));
        _timeEdit.setOnClickListener(
                view1 ->
                {
                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                            GameActivity.this,
                            _selectedDate.get(Calendar.HOUR_OF_DAY),
                            _selectedDate.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
                }
        );

        Button commit = (Button) findViewById(R.id.game_commit);
        commit.setOnClickListener(
                view ->
                {
                    String team1 = _team1Edit.getText().toString();
                    String team2 = _team2Edit.getText().toString();
                    Date date = _selectedDate.getTime();
                    if (!team1.isEmpty() && !team2.isEmpty())
                    {
                        App.component().getTickerClient()
                                .createGame(new Game(team1, team2, date))
                                .subscribe(
                                        result -> finish(),
                                        Throwable::printStackTrace
                                );
                    }

                }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SELECTED_DATE, SAVE_FORMAT.format(_selectedDate.getTime()));
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int month, int day)
    {
        _selectedDate.set(Calendar.YEAR, year);
        _selectedDate.set(Calendar.MONTH, month);
        _selectedDate.set(Calendar.DAY_OF_MONTH, day);

        _dateEdit.setText(DATE_FORMAT.format(_selectedDate.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout layout, int hourOfDay, int minute)
    {
        _selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        _selectedDate.set(Calendar.MINUTE, minute);

        _timeEdit.setText(TIME_FORMAT.format(_selectedDate.getTime()));
    }
}
