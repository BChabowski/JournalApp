package bchabowski.journalapp;

public class CharCounter {

    public CharCounter(){}

    public int countChars(String text){
        return text.length();
    }

    public int countWords(String text){
        String trimmed = text.trim();

        //empty string after splitting will return length==1. Line below is simple workaround
        if(trimmed.length()==0) return 0;

        String[] words = trimmed.split("\\s+");

        return words.length;
    }

}
