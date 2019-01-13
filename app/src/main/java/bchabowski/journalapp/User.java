package bchabowski.journalapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    private int id = 1;
    @ColumnInfo(name = "pinCode")
    private String pinCode;
    @ColumnInfo(name = "pinQuestion")
    private String pinQuestion;
    @ColumnInfo(name = "pinAnswer")
    private String pinAnswer;
    @ColumnInfo(name = "alwaysLoggedIn")
    private boolean alwaysLoggedIn;
    @ColumnInfo(name = "charThreshold")
    private int charThreshold;
    @ColumnInfo(name = "backgroundColour")
    private int backgroundColour;

    public User(String pinCode, String pinQuestion, String pinAnswer, boolean alwaysLoggedIn, int charThreshold, int backgroundColour) {
        this.pinCode = pinCode;
        this.pinQuestion = pinQuestion;
        this.pinAnswer = pinAnswer;
        this.alwaysLoggedIn = alwaysLoggedIn;
        this.charThreshold = charThreshold;
        this.backgroundColour = backgroundColour;
    }

    //getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }


    public boolean isAlwaysLoggedIn() {
        return alwaysLoggedIn;
    }

    public void setAlwaysLoggedIn(boolean alwaysLoggedIn) {
        this.alwaysLoggedIn = alwaysLoggedIn;
    }

    public int getCharThreshold() {
        return charThreshold;
    }

    public void setCharTarget(int charThreshold) {
        this.charThreshold = charThreshold;
    }

    public int getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(int backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public String getPinQuestion() {
        return pinQuestion;
    }

    public void setPinQuestion(String pinQuestion) {
        this.pinQuestion = pinQuestion;
    }

    public String getPinAnswer() {
        return pinAnswer;
    }

    public void setPinAnswer(String pinAnswer) {
        this.pinAnswer = pinAnswer;
    }
}
