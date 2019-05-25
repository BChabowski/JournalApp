package bchabowski.journalapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {User.class, PersonalNotes.class}, version = 6)
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
//            database.execSQL("Create table PN (entryId LONG PRIMARY KEY, timestamp TEXT, content TEXT, )");
//            database.execSQL("Insert into PN select * from PersonalNotes");
//            database.execSQL("Drop table PersonalNotes");
//            database.execSQL("Alter table PN rename to PersonalNotes");
//            database.execSQL("Drop Table User");
//        }
//    };
    static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("Create table PN (entryId INTEGER PRIMARY KEY, timestamp INTEGER, content TEXT, " +
                    "charCount INTEGER NOT NULL, wordCount INTEGER NOT NULL, tags TEXT)");
            database.execSQL("Insert into PN select * from PersonalNotes");
            database.execSQL("Drop table PersonalNotes");
            database.execSQL("Alter table PN rename to PersonalNotes");
        }
    };

    public static UserDatabase getDb(Context context){
        if(dbInstance==null){
            dbInstance = Room.databaseBuilder(context,UserDatabase.class,DBNAME).addMigrations(MIGRATION_5_6)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }
}
