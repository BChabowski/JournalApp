package bchabowski.journalapp;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class PersonalNotesEditor extends AppCompatActivity {
    private final long DEFAULT_VALUE = -1;
    private EditText textField;
    private TextView timeField, tagsField;
    private FloatingActionButton done;
    private PersonalNotesEditorViewModel model;
    //private PersonalNotes personalNote = PersonalNotes.getEmptyPersonalNote();

    private Date date;
    private String toSave;
    private Intent intent;
    private boolean isNewFile = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        model = new PersonalNotesEditorViewModel(getApplication());
        super.setTheme(model.getTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        intent = getIntent();
        textField = findViewById(R.id.textSpace);
        textField.setSingleLine(false);
        timeField = findViewById(R.id.timestampTextView);
        tagsField = findViewById(R.id.textEditorTags);
        done = findViewById(R.id.doneFab);
        setDoneListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (intent.hasExtra("entryId")) {
            //if it's edited note
            openFile();
        } else if (intent.hasExtra("date")) {
            //if it's note added from DayPage
            createNewFileWithDate();
        } //if it's note added from main activity
        else createNewFile();


        //so keyboard won't cover edittext
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setDate();
        setTags();
        setTagsClickListener();

        //dunno how to fix it, and I'm too tired to search

//        OnSwipeTouchListener onTouchListener = new OnSwipeTouchListener(this) {
//            public void onSwipeRight() {
//                onBackPressed();
//            }
//            public void onSwipeTop() {super.onSwipeTop();}
//            public void onSwipeBottom() {super.onSwipeBottom();}
//        };
//        textField.setOnTouchListener(onTouchListener);
    }

    private void setTagsClickListener(){
        tagsField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveFile();
                addTagsViaModel();
            }
        });
    }


    public void setTags(){
        String tags = model.getPersonalNote().getTags().trim();

        //to prevent adding last tag over and over again after clicking OK in add tags dialog without changing anything
        String[] noteTags = tags.split(",");


        String tagsUpdated = new String();
        boolean hasNewTag = intent.hasExtra("newTag");
        if(hasNewTag){
            String newTag = intent.getStringExtra("newTag");
//            for(String s:noteTags){
//                String trimmed = s.trim();
//                if(trimmed.equals(newTag)){
//                    return;
//                }
//            }
            if(tags.equals("")){
                model.getPersonalNote().setTags(newTag);
            }
            else {
                boolean isLastCharComma = tags.charAt(tags.length()-1)==',';
                if(isLastCharComma){
                    tagsUpdated = tags + " "+newTag;
                }
                else{
                    tagsUpdated = tags + ", "+newTag;
                }
                model.getPersonalNote().setTags(tagsUpdated);
            }
            intent.removeExtra("newTag");
        }

//        if(tags.equals("")) tagsField.setVisibility(View.GONE);
//        else
            tagsField.setVisibility(View.VISIBLE);

        tagsField.setText(model.getPersonalNote().getTags());


//        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.textEditorLayout);
//        ConstraintSet set = new ConstraintSet();
//        String[] tags = personalNote.getTags().split(",");
//
//        for(int i = 0;i<tags.length;i++) {
//            TextView layout = new TextView(this);
//            layout.setId(View.generateViewId());
//            layout.setText(tags[i]);
//            layout.addView(layout, i);
//            set.clone(layout);
//            set.connect(layout.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 60);
//            set.applyTo(layout);
//        }
    }

    private void createNewFile() {
        date = Calendar.getInstance().getTime();
        model.getPersonalNote().setTimestamp(date);
    }

    private void createNewFileWithDate() {
        date = new Date(intent.getLongExtra("date", DEFAULT_VALUE));
        model.getPersonalNote().setTimestamp(date);
    }

    private void openFile() {
        Long entryId = intent.getLongExtra("entryId", DEFAULT_VALUE);
        isNewFile = false;
        model.setPersonalNote(entryId);

            textField.setText(model.getPersonalNote().getContent());

            date = model.getPersonalNote().getTimestamp();

    }

    private void setDate() {
        timeField.setText(model.getEntryDateAsString(date));
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveFile();
    }

    private void setDoneListener(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void dontSave(){
        super.onBackPressed();
    }

    protected void saveFile() {
        toSave = textField.getText().toString();
        if (!toSave.trim().equals("")) {
            saveOrUpdateFile();
        }
    }

    //chyba działa
    private void saveOrUpdateFile() {
        model.saveOrUpdatePersonalNote(toSave);

    }



//    private String getText() {
//        toSave = textField.getText().toString();
//        return toSave;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_notes_editor_menu, menu);
        return true;
    }

    private void addTagsViaModel(){

        model.addTags(this, tagsField);
    }
    ////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                saveFile();
                Intent i = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.delete_note:
                if(model.isNewFile()) dontSave();
                else model.deleteNote(this);
                break;
            case R.id.show_char_and_word_count:
                model.showCharsAndWords(textField.getText().toString(), this);
                break;
            case R.id.change_background_colour:
                model.changeBackgroundColour();
                recreate();
                break;
            case R.id.set_daily_target:
                model.setCharTarget(this);
                break;
            case R.id.add_tags:
                addTagsViaModel();
                //setTags();
                break;
            case R.id.export_to_txt:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                saveFile();
                model.exportToTxt(this);
                break;
//            case R.id.show_all_tags:
//                saveFile();
//                model.showAllTags(this);
//                break;
        }
        return true;
    }

    }



