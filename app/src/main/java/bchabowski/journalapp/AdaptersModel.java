package bchabowski.journalapp;

import android.content.Context;
import android.graphics.Color;

public class AdaptersModel  {
    private DbConnectorForUser db;
    public AdaptersModel(Context context){
        db = DbConnectorForUser.getDbConnector(context);
    }

    public int getTextColour(){
        int backgroundColour = db.readUser().getBackgroundColour();
        if(backgroundColour==Color.WHITE)
            return Color.BLACK;
        return Color.WHITE;
    }

    public int getBackgroundColour(){
        return db.readUser().getBackgroundColour();
    }
}
