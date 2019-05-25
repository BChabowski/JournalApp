package bchabowski.journalapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class PersonalNotes {
    @PrimaryKey(autoGenerate = true)
    private Long entryId;

    private Date timestamp;
    private String content;
    private int charCount;
    private int wordCount;
    private String tags;

    public PersonalNotes(Date timestamp, String content, int charCount, int wordCount, String tags) {
        this.timestamp = timestamp;
        this.content = content;
        this.charCount = charCount;
        this.wordCount = wordCount;
        this.tags = tags;
    }


    public static PersonalNotes getEmptyPersonalNote(){
        return new PersonalNotes(new Date(0L),"",0,0,"");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if(this.getEntryId()==((PersonalNotes)obj).getEntryId())
            return true;
        return false;
    }

    //getters and setters


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getEntryId() {
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
