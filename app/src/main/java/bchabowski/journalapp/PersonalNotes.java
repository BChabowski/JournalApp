package bchabowski.journalapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class PersonalNotes {
    @PrimaryKey(autoGenerate = true)
    private long entryId;

    private Date timestamp;
    private String content;
    private int charCount;
    private int wordCount;

    public PersonalNotes(Date timestamp, String content, int charCount, int wordCount) {
        this.timestamp = timestamp;
        this.content = content;
        this.charCount = charCount;
        this.wordCount = wordCount;
    }


    //getters and setters
    public long getEntryId() {
        return entryId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCharCount() {
        return charCount;
    }

    public void setCharCount(int charChount) {
        this.charCount = charChount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
