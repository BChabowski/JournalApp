package bchabowski.journalapp;

import android.app.Application;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Date;

public class PersonalNotesEditorViewModel extends MainActivityModel {
    private int backgroundColour = Color.WHITE;
    private int textColour;
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


    //menu methods


    public void showCharsAndWords(String note){
        Toast.makeText(getApplication(),R.string.chars_count+charCount(note)+"\n"
                +R.string.words_count+wordCount(note),Toast.LENGTH_LONG).show();
    }


}
