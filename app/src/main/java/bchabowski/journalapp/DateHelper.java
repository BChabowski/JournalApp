package bchabowski.journalapp;

import android.content.res.Resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    private Date date;
    private String[] months, weekdays;
    private Resources resources;
    private DateFormat wholeDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private DateFormat wholeDateWithoutHoursFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DateFormat dayFormat = new SimpleDateFormat("dd");
    private DateFormat weekdayFormat = new SimpleDateFormat("u");
    private DateFormat monthFormat = new SimpleDateFormat("MM");
    private DateFormat yearFormat = new SimpleDateFormat("yyyy");
    private DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

    public DateHelper(Date date){
        this.date = date;
    }

    public DateHelper(Date date, Resources resources){
        this.date = date;
        this.resources = resources;
    }

    public String getDate(){
        return wholeDateFormat.format(date);
    }


    public String getDay() {
        int d = Integer.valueOf(dayFormat.format(date));
        return String.valueOf(d);
    }

    public String getMonth(){
        return monthFormat.format(date);
    }

    public Date getFirstDayOfMonth(){
        return parseStringToDate("01/"+getMonth()+"/"+getYear()+" 00:00:00");
    }

    public Date getLastDayOfMonth(){
        String nextMonth,year;
        int tmp;
        if(getMonth().equals("12")){
            nextMonth = "01";
            tmp = Integer.parseInt(getYear())+1;
            year = String.valueOf(tmp);
        }
        else{
            year = getYear();
            tmp = Integer.parseInt(getMonth())+1;
            nextMonth = String.valueOf(tmp);
            if(nextMonth.length()==1)
                nextMonth = "0"+nextMonth;
        }
        Date nextMonthBegg = parseStringToDate("01/"+nextMonth+"/"+year+" 00:00:00");
        return new Date(nextMonthBegg.getTime()-1L);
    }


    public String getMonthName(){
        if(resources!=null){
        months = resources.getStringArray(R.array.months);
        return getMonthNameFromResources();}

        return resources.getString(R.string.error);
    }



    public String getWeekday() {
        if(resources!=null){
        weekdays = resources.getStringArray(R.array.weekdays);
        return getWeekdayName();}

        return resources.getString(R.string.error);
    }


    public String getDateWithoutHours(){
        return wholeDateWithoutHoursFormat.format(date);
    }

    public String getYear() {
        return yearFormat.format(date);
    }

    public String getHour() {
        return hourFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date parseStringToDate(String toConvert){
        Date converted = null;
        try {
            converted = wholeDateFormat.parse(toConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converted;
    }

    public Date parseStringWithoutHoursToDate(String toConvert){
        Date converted = null;
        try {
            converted = wholeDateWithoutHoursFormat.parse(toConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converted;
    }


    private String getMonthNameFromResources(){

        int month = Integer.parseInt(monthFormat.format(date));

        return months[month-1];
    }

    private String getWeekdayName(){
        int weekday = Integer.parseInt(weekdayFormat.format(date));
        return weekdays[weekday-1];
    }
}
