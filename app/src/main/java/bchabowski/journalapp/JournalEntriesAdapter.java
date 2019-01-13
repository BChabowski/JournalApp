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

public class JournalEntriesAdapter extends RecyclerView.Adapter<JournalEntriesAdapter.MyViewHolder> {
    protected LayoutInflater inflater;
    protected DateHelper helper = new DateHelper(new Date());
    protected ItemClickListener itemClickListener;
    protected List<PersonalNotes> personalNotes;
    private AdaptersModel model;

    public  JournalEntriesAdapter(Context context, List<PersonalNotes> personalNotes){
        inflater = LayoutInflater.from(context);
        this.personalNotes = personalNotes;
        model = new AdaptersModel(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.activity_single_entry,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {
        String content, hour;
        PersonalNotes pn = personalNotes.get(i);
        helper.setDate(pn.getTimestamp());
        hour = helper.getHour();
        content = pn.getContent();
        myViewHolder.entryHour.setText(hour);
        myViewHolder.entryContent.setText(content);
        myViewHolder.entryHour.getRootView().setBackgroundColor(model.getBackgroundColour());
        myViewHolder.entryHour.setTextColor(model.getTextColour());
        myViewHolder.entryContent.setTextColor(model.getTextColour());
    }

    @Override
    public int getItemCount() {
        return personalNotes.size();
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView entryHour, entryContent;
        public MyViewHolder( View view){
            super(view);
            entryHour = view.findViewById(R.id.entryHourInSingleEntry);
            entryContent = view.findViewById(R.id.entryContentInSingleEntry);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }


    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
