package bchabowski.journalapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface NotesAndUserDAO {

    //Personal Notes Queries
    @Insert
    void insertNote(PersonalNotes personalNotes);

    @Update
    void updateNote(PersonalNotes personalNotes);

    @Delete
    void deleteNote(PersonalNotes personalNotes);

    @Query("Select * from PersonalNotes where entryId = :id")
    PersonalNotes readNoteById(long id);

    @Query("Select charCount from PersonalNotes where timestamp between :from and :to")
    List<Integer> readCharCountsByDate(Date from, Date to);

    @Query("Select wordCount from PersonalNotes where timestamp between :from and :to")
    List<Integer> readWordCountsByDate(Date from, Date to);


    @Query("Select * from PersonalNotes where timestamp between :from and :to order by timestamp")
    List<PersonalNotes> readAllNotesBetween(Date from, Date to);

    @Query("Select * from PersonalNotes order by timestamp")
    List<PersonalNotes> readAllNotes();
    //User queries

    @Query("Select count(*) from User")
    int isUserCreated();

    @Query("Select * from user")
    User readUser();

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("Delete from user")
    void deleteUser();


}
