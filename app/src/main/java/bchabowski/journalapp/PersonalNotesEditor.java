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
    private PersonalNotes personalNote = PersonalNotes.getEmptyPersonalNote();
    private Long entryId = null;
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

        OnSwipeTouchListener onTouchListener = new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                onBackPressed();
            }
        };
        textField.setOnTouchListener(onTouchListener);
    }

    private void setTagsClickListener(){
        tagsField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
                Intent i = new Intent(getApplicationContext(), TagsList.class);
                i.putExtra("tagsString",tagsField.getText().toString());
                startActivity(i);
            }
        });
    }

    public void setTags(){
        if(personalNote.getTags().equals("")) tagsField.setVisibility(View.GONE);
        else tagsField.setVisibility(View.VISIBLE);

        tagsField.setText(personalNote.getTags());
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
        personalNote.setTimestamp(date);
    }

    private void createNewFileWithDate() {
        date = new Date(intent.getLongExtra("date", DEFAULT_VALUE));
        personalNote.setTimestamp(date);
    }

    private void openFile() {
        entryId = intent.getLongExtra("entryId", DEFAULT_VALUE);
        isNewFile = false;
        personalNote = model.readPersonalNote(entryId);
        if (personalNote != null) {
            textField.setText(personalNote.getContent());

            date = personalNote.getTimestamp();

        } else Toast.makeText(getApplicationContext(), R.string.note_failed_to_open, Toast.LENGTH_LONG).show();
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
        getText();
        if (!toSave.trim().equals("")) {
            saveOrUpdateFile();
        }
    }



    //chyba działa
    private void saveOrUpdateFile() {
        int chars = model.charCount(toSave);
        int words = model.wordCount(toSave);
        personalNote.setContent(toSave);
        personalNote.setCharCount(chars);
        personalNote.setWordCount(words);
        if (entryId == null) {
            model.savePersonalNote(personalNote);
        } else {
            model.updatePersonalNote(personalNote);
        }
    }



    private String getText() {
        toSave = textField.getText().toString();
        return toSave;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_notes_editor_menu, menu);
        return true;
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
                if(isNewFile) dontSave();
                else model.deleteNote(this,personalNote);
                break;
            case R.id.show_char_and_word_count:
                model.showCharsAndWords(getText(), this);
                break;
            case R.id.change_background_colour:
                model.changeBackgroundColour();
                recreate();
                break;
            case R.id.set_daily_target:
                model.setCharTarget(this);
                break;
            case R.id.add_tags:
                model.addTags(this, personalNote, tagsField);
                //setTags();
                break;
            case R.id.export_to_txt:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                saveFile();
                model.exportToTxt(this,personalNote);
                break;
            case R.id.show_all_tags:
                saveFile();
                model.showAllTags(this);
                break;
        }
        return true;
    }

    }



