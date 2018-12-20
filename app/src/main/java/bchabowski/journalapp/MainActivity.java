package bchabowski.journalapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    protected CompactCalendarView calendarView;
    protected TextView month;
    protected String[] months, days;
    protected Resources resources;
    protected FloatingActionButton addNote;
    private DateHelper helper;
    private MainActivityModel model;

    private boolean wasBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = new MainActivityModel(getApplication());
        checkPassword();

        resources = getResources();
        calendarView = findViewById(R.id.calendarView);
        helper = new DateHelper(calendarView.getFirstDayOfCurrentMonth(),resources);

        days = resources.getStringArray(R.array.weekdays_abbreviations);
        calendarView.setDayColumnNames(days);
        setCalendarViewListener();
        setEvents();

        month = findViewById(R.id.showMonth);
        months = resources.getStringArray(R.array.months);
        setMonthName();

        addNote = findViewById(R.id.addNoteMainActivityFAB);
        setAddNoteListener();

    }

    private void checkPassword() {
        if (model.isProtected()) {
            Intent i = getIntent();
            if (!i.hasExtra("pass_ok")) {
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }
        }
    }


    private void setMonthName(){
        //do modelu?

        helper.setDate(calendarView.getFirstDayOfCurrentMonth());
        month.setText(helper.getMonth()+"  "+helper.getYear());
    }

    private void setAddNoteListener(){
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PersonalNotesEditor.class);
                startActivity(i);
            }
        });
    }

    private void setCalendarViewListener(){
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //do przetestowania!!!!!
                //i do wrzucenia w model

                helper.setDate(dateClicked);
                String dateString = helper.getDateWithoutHours();
                Date currDate = new Date();
                helper.setDate(currDate);
                dateString += " " + helper.getHour();
                Date finalDate = helper.parseStringToDate(dateString);
                helper.setDate(finalDate);

                Long epoch = finalDate.getTime();
                Intent intent = new Intent(getApplicationContext(), DayPage.class);
                intent.putExtra("epoch",epoch);

                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setMonthName();
            }
        });
    }

    private void setEvents() {
        //prototype functionality
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = format.parse("2018/11/22");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Event event = new Event(Color.GREEN,date.getTime());
        calendarView.addEvent(event);
        //rysuje kropkę pod zaznaczonym dniem
        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);
    }

    public void goToLoginScreen(View v){
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

    public void goToCreateAcc(View v){
        Intent i = new Intent(getApplicationContext(),AddPin.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem usePassword = menu.findItem(R.id.use_pass);
        if(model.isProtected()) usePassword.setTitle(R.string.stop_using_pin);

        else
            usePassword.setTitle(R.string.set_pin);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        model.chooseOnClickListener(id, this);
        return true;
    }

    //do przetestowania///////////////
    @Override
    public void onBackPressed() {
        if(wasBackPressed)
        super.onBackPressed();
        else{
            wasBackPressed = true;
            Toast.makeText(this,R.string.press_back_again_to_quit,Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    wasBackPressed = false;
                }
            },2000);
        }
    }
}
