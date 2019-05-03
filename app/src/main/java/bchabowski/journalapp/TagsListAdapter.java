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

public class TagsListAdapter extends RecyclerView.Adapter<TagsListAdapter.TagsViewHolder> {
    private LayoutInflater inflater;
    private DateHelper helper;
    private JournalEntriesAdapter.ItemClickListener itemClickListener;
    private List<PersonalNotes> personalNotes;

    public TagsListAdapter(Context context, List<PersonalNotes> personalNotes) {
        helper = new DateHelper(new Date(), context.getResources());
        inflater = LayoutInflater.from(context);
        this.personalNotes = personalNotes;
    }

    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.single_tag, viewGroup, false);
        return new TagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder tagsViewHolder, int i) {
        String tag, date, content;
        String[] words;
        PersonalNotes pn = personalNotes.get(i);
        helper.setDate(pn.getTimestamp());
        //set content to be first 10 words
        //wyrzucić w inne miejsce??????
        int wordsToShow = 50;
        content = pn.getContent().trim();
        if (!content.equals("")) {
            words = content.split("\\s+");
            if (words.length > wordsToShow) {
                String toTrim = new String();
                for (int j = 0; j < wordsToShow; j++) {
                    toTrim += words[j] + " ";
                }
                content = toTrim.trim();
                content += "...";
            }
        }
        //do the rest
        date = helper.getDate();
        if(pn.getTimestamp().getTime()==0L){
            date = "";
        }
        tag = pn.getTags();
        if(tag.equals("")) tagsViewHolder.tagTV.setVisibility(View.GONE);

        if(date.equals("")) tagsViewHolder.tagDateTV.setVisibility(View.INVISIBLE);
        if(content.equals("")) {
            tagsViewHolder.tagContentTV.setVisibility(View.GONE);}
        else {
            tag = new String("#: "+tag);
        }
        tagsViewHolder.tagTV.setText(tag);
        tagsViewHolder.tagDateTV.setText(date);
        tagsViewHolder.tagContentTV.setText(content);
    }

    @Override
    public int getItemCount() {
        return personalNotes.size();
    }

    void setClickListener(JournalEntriesAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class TagsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tagTV, tagDateTV, tagContentTV;

        public TagsViewHolder(View view) {
            super(view);
            tagTV = view.findViewById(R.id.tagTV);
            tagDateTV = view.findViewById(R.id.tagDateTV);
            tagContentTV = view.findViewById(R.id.tagContentTV);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}