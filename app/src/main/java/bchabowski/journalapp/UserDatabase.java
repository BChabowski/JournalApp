package bchabowski.journalapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {User.class, PersonalNotes.class}, version = 4)
@TypeConverters({DateConverter.class})
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase dbInstance;
    private static final String DBNAME = "dlcDB";

    public abstract NotesAndUserDAO notesAndUserDAO();

    // migration blueprint for future use
//    static final Migration MIGRATION_1_2 = new Migration(1,2){
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            //for copying purposes
//            database.execSQL("Create table PN (entryId INTEGER PRIMARY KEY, timestamp TEXT, content TEXT)");
//            database.execSQL("Insert into PN select * from PersonalNotes");
//            database.execSQL("Drop table PersonalNotes");
//            database.execSQL("Alter table PN rename to PersonalNotes");
//            database.execSQL("Drop Table User");
//        }
//    };

    public static UserDatabase getDb(Context context){
        if(dbInstance==null){
            dbInstance = Room.databaseBuilder(context,UserDatabase.class,DBNAME).fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }
}
