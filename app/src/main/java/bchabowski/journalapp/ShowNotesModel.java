package bchabowski.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class ShowNotesModel extends MainActivityModel {
    private DbConnectorForPersonalNotes db;
    private List<PersonalNotes> personalNotes;


    public ShowNotesModel(@NonNull Application application) {
        super(application);
        db = DbConnectorForPersonalNotes.getDbConnector(application);
    }

    public List<PersonalNotes> showAllNotesFromDb(){
        personalNotes =  db.showAll();
        return personalNotes;
    }

    public List<PersonalNotes> showAllNotesFromCurrentMonth(Date date){
        personalNotes = db.showNotesFromMonth(date);
        return personalNotes;
    }

    public List<PersonalNotes> getPersonalNotes(){
        return personalNotes;
    }
}
