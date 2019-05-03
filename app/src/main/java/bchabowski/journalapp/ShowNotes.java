package bchabowski.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;

public class ShowNotes extends AppCompatActivity {
    private RecyclerView notes;
    private ShowNotesAdapter adapter;
    private Intent intent;
    private ShowNotesModel model;
    private JournalEntriesAdapter.ItemClickListener adapterListener;
    private Date date;
    private View layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ShowNotesModel(getApplication());
        super.setTheme(model.getTheme());

        setContentView(R.layout.activity_show_notes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        if(intent.hasExtra("epoch")){
            date = new Date(intent.getLongExtra("epoch",0));
        }

        notes = findViewById(R.id.showNotesRV);
        setAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(notes.getContext());
        notes.setLayoutManager(mLayoutManager);
        notes.setItemAnimator(new DefaultItemAnimator());
        notes.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        layout = findViewById(R.id.showNotesLayout);
        model.setSwipeRightToBack(this,layout,notes);
    }

    private void setAdapter(){
        if(date!=null){
            adapter = new ShowNotesAdapter(this,model.showAllNotesFromCurrentMonth(date));
            setAdapterListener();
            notes.setAdapter(adapter);
        }
        else {
            adapter = new ShowNotesAdapter(this,model.showAllNotesFromDb());
            setAdapterListener();
            notes.setAdapter(adapter);
        }
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

    @Override
    public void onResume(){
        super.onResume();
        setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:

                Intent i = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
        return true;
    }

}
