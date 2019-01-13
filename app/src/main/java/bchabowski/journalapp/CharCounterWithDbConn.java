package bchabowski.journalapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CharCounterWithDbConn extends CharCounter {
    private DbConnectorForPersonalNotes db;
    Calendar calendar = Calendar.getInstance();

    public CharCounterWithDbConn(DbConnectorForPersonalNotes conn){
        db = conn;
    }

    public double avgCharsBetween(Date from, Date to){
        ArrayList<Integer> counts = getAllCharCountsBetween(from, to);
        return calculateAvg(counts);
    }

    public double avgCharsBetweenWithoutZeroes(Date from, Date to){
        ArrayList<Integer> counts = getAllCharCountsBetween(from, to);
        return calculateAvgWithoutZeroes(counts);
    }

    public double avgWordsBetween(Date from, Date to){
        ArrayList<Integer> counts = getAllWordCountsBetween(from, to);
        return calculateAvg(counts);
    }
    public double avgWordsBetweenWithoutZeroes(Date from, Date to){
        ArrayList<Integer> counts = getAllWordCountsBetween(from, to);
        return calculateAvgWithoutZeroes(counts);
    }

    //test with incrementDate method////////////////////////////////////////////////////////////////
    public ArrayList<Integer> getAllCharCountsBetween(Date from, Date to){
        Date curr = from;
        ArrayList<Integer> charCounts = new ArrayList<>();

        do{
            int c = db.readCharCountFromDay(curr);
            charCounts.add(c);
            curr = incrementDate(curr);
        }
        while(curr.getTime()<=to.getTime());
        return charCounts;
    }

    public ArrayList<Integer> getAllWordCountsBetween(Date from, Date to){
        Date curr = from;
        ArrayList<Integer> charCounts = new ArrayList<>();
        do{
            int c = db.readWordCountFromDay(curr);
            charCounts.add(c);
            curr = incrementDate(curr);
        }
        while(curr.getTime()<=to.getTime());
        return charCounts;
    }

    private Date incrementDate(Date date){
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        return calendar.getTime();
    }

    private double calculateAvg(ArrayList<Integer> counts){
        int sum = 0;
        for (int i : counts) {
            sum+=i;
        }
        double result = (double)sum/(double)counts.size();
        return result;
    }

    private double calculateAvgWithoutZeroes(ArrayList<Integer> counts){
        int sum = 0;
        int size = 0;
        for (int i : counts) {
            if(i>0){
                sum+=i;
                size++;
            }
        }
        if(size==0) return 0;
        double result = (double)sum/(double)size;
        return result;
    }


}
