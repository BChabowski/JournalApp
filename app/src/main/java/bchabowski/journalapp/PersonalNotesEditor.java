package bchabowski.journalapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class PersonalNotesEditor extends AppCompatActivity {
    private final long DEFAULT_VALUE = -1;
    private EditText textField;
    private Button hideTextButton;
    private TextView timeField;
    private PersonalNotesEditorViewModel model;
    private PersonalNotes personalNote = null;
    private Long entryId = null;
    private Date date;
    private String toSave, entryDate;
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
        }
        else if (intent.hasExtra("date")){
            //if it's note added from DayPage
         createNewFileWithDate();
        }
        else createNewFile();
            //if it's note added from main activity
        setDate();

        setTextAndBackgroundColour();
    }

    private void createNewFile() {
        date = Calendar.getInstance().getTime();
    }

    private void createNewFileWithDate(){
        date = new Date(intent.getLongExtra("date", DEFAULT_VALUE));
    }

    private void openFile() {
        entryId = intent.getLongExtra("entryId", DEFAULT_VALUE);
        //don't know whether this if is necessary
        if (entryId > DEFAULT_VALUE) {
            personalNote = model.readPersonalNote(entryId);
            textField.setText(personalNote.getContent());

            date = personalNote.getTimestamp();

        }
        else Toast.makeText(this,R.string.note_failed_to_open,Toast.LENGTH_LONG).show();
    }

    private void setDate() {
        dateHelper = new DateHelper(date);
        entryDate = dateHelper.getDate();
        timeField.setText(entryDate);
    }


    private void setTextAndBackgroundColour(){
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
        Toast.makeText(getApplicationContext(), "znaków: "+charCount()+" słów:" +wordCount(),Toast.LENGTH_LONG).show();
    }

    protected void prepareFile() {
        getText();
        if (!toSave.trim().equals("")) {
            saveFile();
        }
    }

    private void saveFile() {
        int chars = charCount();
        int words = wordCount();
        if (personalNote == null)
        {
            personalNote = new PersonalNotes(date, toSave, chars, words);
            model.savePersonalNote(personalNote);

        } else {
            personalNote.setContent(toSave);
            model.updatePersonalNote(personalNote);
        }
    }

    private void getText(){
        toSave  = textField.getText().toString();
    }

    private int charCount(){
        getText();
        return model.charCount(toSave);
    }

    private int wordCount(){
        getText();
        return model.wordCount(toSave);
    }

}

