package bchabowski.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TagsList extends AppCompatActivity {

    private RecyclerView tagsRV;
    private Intent intent;
    private TagsListAdapter adapter;
    private TagsListModel model;
    private String tags;
    private View layout;

    //przyjmuje stringa do wyszukanie w intencie, przekazuje do modelu
    //wyniki wrzuca w adapter
    //jeżeli jest wywołane z edytora powinno dodawać tagi do notatki
    //może tagi obecne w notatce nie powinny się pojawaić??

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new TagsListModel(getApplication());
        super.setTheme(model.getTheme());
        setContentView(R.layout.activity_tags_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagsRV = findViewById(R.id.tagsRV);

        intent = getIntent();
        tags = intent.getStringExtra("tagsString");

        if (tags != null) {
            if(tags.equals("")){
                adapter = new TagsListAdapter(this,model.getPersonalNotesWithoutTags());
            }
            else{
            adapter = new TagsListAdapter(this, model.getPersonalNotesByTag(tags));
            }
        } else {
            adapter = new TagsListAdapter(this, model.getTags());
        }
        tagsRV.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(tagsRV.getContext());
        tagsRV.setLayoutManager(mLayoutManager);
        tagsRV.setItemAnimator(new DefaultItemAnimator());
        tagsRV.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        ////jeżeli jest wywołane z edytora, onClick powinien dodawać tagi do notatki!!!!
        if(!intent.hasExtra("isCalledFromEditor"))
        setAdapterListener();

        //do metody?
        layout = findViewById(R.id.tagsListLayout);
        model.setSwipeRightToBack(this,layout,tagsRV);
    }

    private void setAdapterListener() {
        JournalEntriesAdapter.ItemClickListener listener;
        if (tags != null) {
            listener = new JournalEntriesAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    long noteId = model.getPersonalNotes().get(position).getEntryId();
                    Intent i = new Intent(getApplicationContext(), PersonalNotesEditor.class);
                    i.putExtra("entryId", noteId);
                    startActivity(i);
                }
            };
        } else {
            listener = new JournalEntriesAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent i = new Intent(getApplicationContext(), TagsList.class);
                    i.putExtra("tagsString", model.getPersonalNotes().get(position).getTags());
                    startActivity(i);
                }
            };
        }
        adapter.setClickListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tags_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent i = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.search_by_tags:
                model.showTagsMessagebox(this);
                break;
        }
        return true;
    }
}
