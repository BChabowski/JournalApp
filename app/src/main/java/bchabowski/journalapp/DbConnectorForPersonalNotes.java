package bchabowski.journalapp;

import android.content.Context;

public class DbConnectorForPersonalNotes {
    private UserDatabase db;
    private static DbConnectorForPersonalNotes connInstance;

    private DbConnectorForPersonalNotes(Context context){
        db = UserDatabase.getDb(context);
    }

    public static DbConnectorForPersonalNotes getDbConnector(Context context){
        if(connInstance==null){
            connInstance = new DbConnectorForPersonalNotes(context);
        }
        return connInstance;
    }

    //db connections will go here
}
