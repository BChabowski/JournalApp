package bchabowski.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagsListModel extends MainActivityModel {
    //przytjmuje String tagów, wywołuje wyszukiwanie w bazie, w przypadku niepowodzenia wpisuje w
    //pusty PersonalNote "Nie znaleziono
    //
    private DbConnectorForPersonalNotes db;
    private List<PersonalNotes> personalNotes;

    public TagsListModel(@NonNull Application application) {
        super(application);
        db = DbConnectorForPersonalNotes.getDbConnector(application);
    }

    public List<PersonalNotes> getPersonalNotesByTag(String tags){
        personalNotes = db.showNotesByTags(tags);
        return personalNotes;
    }

    public List<PersonalNotes> getPersonalNotesWithoutTags(){
        personalNotes = db.showNotesWithoutTags();
        return personalNotes;
    }

    public List<PersonalNotes> getPersonalNotes(){
        return personalNotes;
    }

    public List<PersonalNotes> getTags(){
        List<String> tags = db.showAllTags();
        Collections.sort(tags, Collator.getInstance());
        String trimmed;
        //List<PersonalNotes> pn = Collections.emptyList();
        ArrayList<PersonalNotes> pn = new ArrayList<>();
        for(String tag : tags){
            PersonalNotes p = PersonalNotes.getEmptyPersonalNote();
            if(!tag.equals("")){
            p.setTags(tag);
            pn.add(p);
            }
        }
        personalNotes = pn;
        return personalNotes;
    }
}
