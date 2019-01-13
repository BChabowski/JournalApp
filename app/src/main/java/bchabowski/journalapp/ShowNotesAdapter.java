package bchabowski.journalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class ShowNotesAdapter extends RecyclerView.Adapter<ShowNotesAdapter.MyViewHolder> {
//    public class ShowNotesAdapter extends JournalEntriesAdapter {
    private LayoutInflater inflater;
    private DateHelper helper;
    private JournalEntriesAdapter.ItemClickListener itemClickListener;
    private List<PersonalNotes> personalNotes;
    private AdaptersModel model;

    public ShowNotesAdapter(Context context, List<PersonalNotes> personalNotes) {
//        super(context,personalNotes);
        model = new AdaptersModel(context);
        helper = new DateHelper(new Date(),context.getResources());
        inflater = LayoutInflater.from(context);
        this.personalNotes = personalNotes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.single_entry_for_show_notes,viewGroup,false);
        return new ShowNotesAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ShowNotesAdapter.MyViewHolder myViewHolder, int i) {
        String content, hour, day, dayOfTheWeek, month;
        PersonalNotes pn = personalNotes.get(i);
        helper.setDate(pn.getTimestamp());
        hour = helper.getHour();
        day = helper.getDay();
        dayOfTheWeek = helper.getWeekday();
        month = helper.getMonthName();
        content = pn.getContent();
        myViewHolder.dayTV.setText(day);
        myViewHolder.monthTV.setText(month);
        myViewHolder.dayOfTheWeekTV.setText(dayOfTheWeek);
        myViewHolder.hourTV.setText(hour);
        myViewHolder.contentTV.setText(content);
        myViewHolder.dayTV.getRootView().setBackgroundColor(model.getBackgroundColour());
        int text = model.getTextColour();
        myViewHolder.dayTV.setTextColor(text);
        myViewHolder.monthTV.setTextColor(text);
        myViewHolder.dayOfTheWeekTV.setTextColor(text);
        myViewHolder.hourTV.setTextColor(text);
        myViewHolder.contentTV.setTextColor(text);
    }

    @Override
    public int getItemCount() {
        return personalNotes.size();
    }

    void setClickListener(JournalEntriesAdapter.ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    public void setColours() {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dayTV, monthTV, dayOfTheWeekTV, hourTV, contentTV;
        public MyViewHolder( View view){
            super(view);
            dayTV = view.findViewById(R.id.dayTV);
            monthTV = view.findViewById(R.id.monthTV);
            dayOfTheWeekTV = view.findViewById(R.id.dayOfTheWeekTV);
            hourTV = view.findViewById(R.id.hourTV);
            contentTV = view.findViewById(R.id.contentTV);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }


}
