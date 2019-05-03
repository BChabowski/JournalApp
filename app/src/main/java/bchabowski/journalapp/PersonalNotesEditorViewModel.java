package bchabowski.journalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class PersonalNotesEditorViewModel extends MainActivityModel {
    private int backgroundColour = Color.WHITE;
    private int textColour;
    private TextView dialogTitle;

    PersonalNotes pn = PersonalNotes.getEmptyPersonalNote();
    String tags;
    EditText input;
    TextView txtv;

//    private String tags;
    private CharCounterWithDbConn counter;
    private DbConnectorForPersonalNotes dbConnectorForPersonalNotes;
    private MenusHelper menusHelper;

    public PersonalNotesEditorViewModel(@NonNull Application application) {
        super(application);
        dbConnectorForPersonalNotes = DbConnectorForPersonalNotes.getDbConnector(getApplication());
        counter = new CharCounterWithDbConn(dbConnectorForPersonalNotes);
        menusHelper = new MenusHelper(getApplication());
    }

    public String getEntryDateAsString(Date date){
        DateHelper dateHelper = new DateHelper(date);
        return dateHelper.getDate();
    }
    public int charCount(String toCount){
        return counter.countChars(toCount);
    }

    public int wordCount(String toCount){
        return counter.countWords(toCount);
    }


    //db connection methods
    public void savePersonalNote(final PersonalNotes personalNote) {
        dbConnectorForPersonalNotes.savePersonalNote(personalNote);
    }

    public void updatePersonalNote(PersonalNotes personalNote){
        dbConnectorForPersonalNotes.updatePersonalNote(personalNote);
    }
    public PersonalNotes readPersonalNote(final long entryId){
        return dbConnectorForPersonalNotes.readPersonalNote(entryId);
    }

    private void deletePersonalNote(PersonalNotes personalNote){
        dbConnectorForPersonalNotes.deletePersonalNote(personalNote);
    }



    //menu methods


    public void showCharsAndWords(String note, Context context){
        String charsCount = context.getString(R.string.chars_count);
        String wordsCount = context.getString(R.string.words_count);
        Toast.makeText(getApplication(),charsCount+charCount(note)+"\n"
                +wordsCount+wordCount(note),Toast.LENGTH_LONG).show();
    }

    public void addTags(final Context context, PersonalNotes note, TextView tv){
    //////wywalić do tagsMessageBoxa kiedyś
        pn = note;
        txtv = tv;
        tags = pn.getTags();
        input = new EditText(context);
        input.setText(tags);
        final AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
        msgbox.setCustomTitle(dialogTitle);
        msgbox.setTitle(R.string.add_tags_messagebox);
        msgbox.setView(input);
        msgbox.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tags = input.getText().toString();
                pn.setTags(tags);
                ((PersonalNotesEditor)context).setTags();
            }
        });
        msgbox.setNeutralButton(R.string.show_all_tags, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context,TagsList.class);
                i.putExtra("isCalledFromEditor",true);
                context.startActivity(i);
            }
        });

//        msgbox.setNegativeButton(R.string.cancel, null);
        msgbox.create();
        msgbox.show();


    }


    public void deleteNote(final Context context, final PersonalNotes pn){
        AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
        msgbox.setTitle(R.string.delete_confirmation);
        msgbox.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePersonalNote(pn);
                ((PersonalNotesEditor)context).dontSave();
            }
        });
//        msgbox.setNegativeButton(R.string.cancel, null);
        msgbox.create();
        msgbox.show();

    }

    public void exportToTxt(Context context,PersonalNotes note){
        ExportToTxt export = new ExportToTxt();
        export.exportToTxt(context,note);
    }


}
