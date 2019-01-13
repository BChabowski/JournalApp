package bchabowski.journalapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class DayPage extends AppCompatActivity implements Colourable {
    private final Long EPOCH_BEGINING = 0L;
    Date date;
    Intent intent;
    TextView day, dayOfTheWeek, monthAndYear;
    RecyclerView entriesList;
    FloatingActionButton addNote;
    Resources resources;
    DayPageModel model;
    JournalEntriesAdapter adapter;
    JournalEntriesAdapter.ItemClickListener adapterListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.DarkStyle);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_page);
        intent = getIntent();
        resources = getResources();

        day = findViewById(R.id.dayTV);
        dayOfTheWeek = findViewById(R.id.dayOfTheWeek);
        monthAndYear = findViewById(R.id.monthAndYear);

        model = new DayPageModel(getApplication(),intent.getLongExtra("epoch",EPOCH_BEGINING));
        setDate();

        entriesList = findViewById(R.id.entriesRecyclerView);

        setAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(entriesList.getContext());
        entriesList.setLayoutManager(mLayoutManager);
        entriesList.setItemAnimator(new DefaultItemAnimator());
        entriesList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        addNote = findViewById(R.id.addNoteDayPageFAB);
        setAddNoteListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.day_page_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
        switch(id) {
            case R.id.change_colour:
                model.changeBackgroundColour();
                recreate();
                break;
            case R.id.set_daily_char_target:
                model.setCharTarget(this);
                break;
        }       return true;
    }

    private void setDate(){
        day.setText(model.getDay());
        monthAndYear.setText(model.getMonthAndYear());
        dayOfTheWeek.setText(model.getWeekday());
    }

    private void setAdapter(){
        adapter = new JournalEntriesAdapter(this,model.getPersonalNotesLinkedToTheDayFromDb());
        setAdapterListener();
        entriesList.setAdapter(adapter);
    }

    private void setAdapterListener(){

         adapterListener = new JournalEntriesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long noteId = model.getPersonalNotes().get(position).getEntryId();
                Intent i = new Intent(getApplicationContext(),PersonalNotesEditor.class);
                i.putExtra("entryId",noteId);
                startActivity(i);
            }
        };

        adapter.setClickListener(adapterListener);
    }

    private void setAddNoteListener(){
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PersonalNotesEditor.class);
                i.putExtra("date",model.getTime());
                startActivity(i);
            }
        });
    }

    @Override
    public void setColours() {
        int text = model.getTextColour();
        int background = model.getBackgroundColour();
        day.setTextColor(text);
        monthAndYear.setTextColor(text);
        dayOfTheWeek.setTextColor(text);
        getWindow().getDecorView().setBackgroundColor(background);
        addNote.setColorFilter(Color.GRAY);
    }
}
