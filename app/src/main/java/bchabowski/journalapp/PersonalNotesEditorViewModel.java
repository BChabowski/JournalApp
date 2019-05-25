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

    public boolean isNewFile(){
       return pn.getEntryId() == null;
    }

    public void setPersonalNote(long id){
        pn = readPersonalNote(id);
    }
    public void saveOrUpdatePersonalNote(String content){
        pn.setContent(content);
        pn.setCharCount(charCount(content));
        pn.setWordCount(wordCount(content));
        if (isNewFile()) {
            savePersonalNote(pn);
        } else {
            updatePersonalNote(pn);
        }
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
    public void savePersonalNote(PersonalNotes personalNote) {
        Long id = dbConnectorForPersonalNotes.savePersonalNote(personalNote);
        pn = readPersonalNote(id);
    }

    public void updatePersonalNote(PersonalNotes personalNote){
        dbConnectorForPersonalNotes.updatePersonalNote(personalNote);
    }
    private PersonalNotes readPersonalNote(final long entryId){
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

    public void addTags(final Context context, TextView tv){
    //////wywalić do tagsMessageBoxa kiedyś

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
                //set tags on screen
                ((PersonalNotesEditor)context).setTags();
                ((PersonalNotesEditor)context).saveFile();
            }
        });
        msgbox.setNeutralButton(R.string.show_all_tags, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tags = input.getText().toString();
                pn.setTags(tags);
                ((PersonalNotesEditor)context).saveFile();
                Intent i = new Intent(context,TagsList.class);
                i.putExtra("entryId",pn.getEntryId());
                i.putExtra("noteTags",tags);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

//        msgbox.setNegativeButton(R.string.cancel, null);
        msgbox.create();
        msgbox.show();


    }


    public void deleteNote(final Context context){
        AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
        msgbox.setTitle(R.string.delete_confirmation);
        msgbox.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePersonalNote(pn);
                ((PersonalNotesEditor)context).dontSave();
            }
        });
        msgbox.create();
        msgbox.show();

    }

    public void exportToTxt(Context context){
        ExportToTxt export = new ExportToTxt();
        export.exportToTxt(context,pn);
    }

    public PersonalNotes getPersonalNote(){
        return pn;
    }

}
