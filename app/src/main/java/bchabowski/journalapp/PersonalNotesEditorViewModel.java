package bchabowski.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PersonalNotesEditorViewModel extends AndroidViewModel {
    private UserDatabase db = UserDatabase.getDb(getApplication());
    private int backgroundColour = Color.WHITE;
    private int textColour;

    public PersonalNotesEditorViewModel(@NonNull Application application) {
        super(application);
    }

    public void savePersonalNote(final PersonalNotes personalNote) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.notesAndUserDAO().insertNote(personalNote);
               return null;
            }
        }.execute();
    }

    public void updatePersonalNote(final PersonalNotes personalNote){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.notesAndUserDAO().updateNote(personalNote);
                return null;
            }
        }.execute();
    }
    public PersonalNotes readPersonalNote(final long entryId){
        PersonalNotes note = null;
        try {
            note = new AsyncTask<Void, Void, PersonalNotes>() {

                @Override
                protected PersonalNotes doInBackground(Void... voids) {
                    return db.notesAndUserDAO().readNoteById(entryId);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(note==null)
            return new PersonalNotes(new Date(0L),"",0,0);
        else return note;
    }

    public int getBackgroundColour() {
        User user = readUser();
        if(user!=null){
            backgroundColour = user.getBackgroundColour();
        }
        return backgroundColour;
    }

    public int getTextColour(){
        if(backgroundColour==Color.WHITE){
            textColour = Color.BLACK;
        }
        else textColour = Color.WHITE;
        return textColour;
    }

    private User readUser(){
        User user = null;
        try {
            user = new AsyncTask<Void, Void, User>() {
                @Override
                protected User doInBackground(Void... voids) {
                    return db.notesAndUserDAO().readUser();
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int charCount(String toCount){
        int chars = toCount.length();
        return chars;
    }

    public int wordCount(String toCount){
        String trimmed = toCount.trim();

        if(trimmed.length()==0) return 0;

        String[] words = trimmed.split("\\s+");

        return words.length;
    }

}
