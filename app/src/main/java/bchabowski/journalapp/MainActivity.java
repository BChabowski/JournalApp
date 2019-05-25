package bchabowski.journalapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements Colourable {
    protected CompactCalendarView calendar;
    protected TextView month;
    protected String[] months, days;
    protected Resources resources;
    protected FloatingActionButton addNote;

    private Button button;

    private MainActivityModel model;
    private ArrayList<Integer> charCounts = new ArrayList<>();

    private boolean wasBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new MainActivityModel(getApplication());
        super.setTheme(model.getTheme());
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        checkPassword();

        resources = getResources();
        //to change calendarview easily, so it matches theme:
        CompactCalendarView hidden;
        if(model.getTextColour()==Color.WHITE){
            calendar = findViewById(R.id.calendarViewWhite);
            hidden = findViewById(R.id.calendarView);
        }
        else{
            calendar = findViewById(R.id.calendarView);
            hidden = findViewById(R.id.calendarViewWhite);
        }
        hidden.setVisibility(View.INVISIBLE);

        calendar.setCalendarBackgroundColor(Color.TRANSPARENT);
        calendar.shouldDrawIndicatorsBelowSelectedDays(true);

        model.setDate(calendar.getFirstDayOfCurrentMonth());


        days = resources.getStringArray(R.array.weekdays_abbreviations);
        calendar.setDayColumnNames(days);
        setCalendarViewListener();
        setEvents();

        month = findViewById(R.id.showMonth);
        months = resources.getStringArray(R.array.months);
        setMonthName();

        addNote = findViewById(R.id.addNoteMainActivityFAB);
        setAddNoteListener();



        //setColours();
    }

    private void checkPassword() {
        if (model.isProtected()) {
            Intent i = getIntent();
            if (!i.hasExtra("pass_ok")) {
                i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
    }

    //after updating date in model it sets month name
    private void setMonthName(){
        model.setDate(calendar.getFirstDayOfCurrentMonth());
        month.setText(model.getMonthNameAndYear());
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
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Date customDate = model.createDateWithCurrentHour(dateClicked);

                Long epoch = customDate.getTime();
                Intent intent = new Intent(getApplicationContext(), DayPage.class);
                intent.putExtra("epoch",epoch);

                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setMonthName();
                setEvents();
            }
        });
    }

    private void setEvents() {
        int chars;
        boolean isCharTargetSet = model.isCharTargetSet();
        Date firstDayOfMonth = calendar.getFirstDayOfCurrentMonth();
        model.setDate(firstDayOfMonth);
        calendar.removeAllEvents();
        charCounts =  model.getCounts(firstDayOfMonth);
        for(int i =1;i<=charCounts.size();i++){
            chars = charCounts.get(i-1);
            if(chars<1) continue;
            if(isCharTargetSet) {
                if(chars < model.getCharTarget()-chars*0.15){
                    calendar.addEvent(model.redEvent(i));
                }
                else if (chars > 0 && chars > model.getCharTarget()+ chars*0.15){
                    calendar.addEvent(model.blueEvent(i));
                }
                else{
                    calendar.addEvent(model.greenEvent(i));
                }
            }
            else{
                    calendar.addEvent(model.greenEvent(i));
            }
        }
    }

    ///test it out
    @Override
     public void setColours(){
        int background = model.getBackgroundColour();
        int text = model.getTextColour();
        getWindow().getDecorView().setBackgroundColor(background);
        calendar.setCalendarBackgroundColor(background);
        addNote.setColorFilter(Color.GRAY);
        month.setTextColor(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEvents();
        calendar.invalidate();
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

        else usePassword.setTitle(R.string.set_pin);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch(id){
            case R.id.use_pass:
               model.usePass(this);
                break;
            case R.id.change_background_colour:
                model.changeBackgroundColour();
                //to recreate with new colours?
                recreate();
//                setColours();
                break;
            case R.id.show_all_notes:
                intent = new Intent(getApplicationContext(),ShowNotes.class);
                startActivity(intent);
                break;
            case R.id.show_current_month_notes:
                intent = new Intent(getApplicationContext(),ShowNotes.class);
                Date currDate = new Date();
                intent.putExtra("epoch",currDate.getTime());
                startActivity(intent);
                break;
            case R.id.set_daily_target:
                model.setCharTarget(this);
                break;
            case R.id.search_by_tags:
                model.showTagsMessagebox(this);
                break;
            case R.id.show_all_tags:
                model.showAllTags(this);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(wasBackPressed)
//        super.onBackPressed();
            finishAffinity();
        else{
            wasBackPressed = true;
            Toast.makeText(getApplicationContext(),R.string.press_back_again_to_quit,Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    wasBackPressed = false;
                }
            },2000);
        }
    }
}
