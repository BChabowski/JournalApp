package bchabowski.journalapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class PersonalNotesEditor extends AppCompatActivity implements Colourable {
    private final long DEFAULT_VALUE = -1;
    private EditText textField;
    private Button hideTextButton;
    private TextView timeField;
    private PersonalNotesEditorViewModel model;
    private PersonalNotes personalNote = null;
    private Long entryId = null;
    private Date date;
    private String toSave;
    private Intent intent;
    private DateHelper dateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        model = new PersonalNotesEditorViewModel(getApplication());
        intent = getIntent();
        textField = findViewById(R.id.textSpace);
        hideTextButton = findViewById(R.id.hideTextButton);
        timeField = findViewById(R.id.timestampTextView);

        if (intent.hasExtra("entryId")) {
            //if it's edited note
            openFile();
        } else if (intent.hasExtra("date")) {
            //if it's note added from DayPage
            createNewFileWithDate();
        } //if it's note added from main activity
        else createNewFile();

        setDate();

        setColours();
    }

    private void createNewFile() {
        date = Calendar.getInstance().getTime();
    }

    private void createNewFileWithDate() {
        date = new Date(intent.getLongExtra("date", DEFAULT_VALUE));
    }

    private void openFile() {
        entryId = intent.getLongExtra("entryId", DEFAULT_VALUE);

        personalNote = model.readPersonalNote(entryId);
        if (personalNote != null) {
            textField.setText(personalNote.getContent());

            date = personalNote.getTimestamp();

        } else Toast.makeText(this, R.string.note_failed_to_open, Toast.LENGTH_LONG).show();
    }

    private void setDate() {
        timeField.setText(model.getEntryDateAsString(date));
    }

    @Override
    public void setColours() {
        int background = model.getBackgroundColour();
        getWindow().getDecorView().setBackgroundColor(background);
        int text = model.getTextColour();
        textField.setTextColor(text);
        timeField.setTextColor(text);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        prepareFile();
    }

    protected void prepareFile() {
        getText();
        if (!toSave.trim().equals("")) {
            saveFile();
        }
    }

    private void saveFile() {
        int chars = model.charCount(toSave);
        int words = model.wordCount(toSave);
        if (personalNote == null) {
            personalNote = new PersonalNotes(date, toSave, chars, words);
            model.savePersonalNote(personalNote);

        } else {
            personalNote.setContent(toSave);
            personalNote.setCharCount(chars);
            personalNote.setWordCount(words);
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
            case R.id.show_char_and_word_count:
                model.showCharsAndWords(getText());
                break;
            case R.id.change_background_colour:
                model.changeBackgroundColour();
                recreate();
                break;
            case R.id.set_daily_target:
                model.setCharTarget(this);
                break;
        }
        return true;
    }

    }



