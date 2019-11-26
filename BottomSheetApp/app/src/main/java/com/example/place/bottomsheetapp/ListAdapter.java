package com.example.place.bottomsheetapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private List<ChatMessage> list;

    public void setList(List<ChatMessage> list) {
        this.list = list;
    }

    private String currentId;
    private int TYPE_CURRENT = 1;
    private int TYPE_OTHER = 2;

    public ListAdapter(List<ChatMessage> list, String currentId) {
        this.list = list;
        this.currentId = currentId;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        if (i == TYPE_CURRENT)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.personal_message_sender, null);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.personal_message_receiver, null);
        return new ListViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSenderId().equalsIgnoreCase(currentId))
            return TYPE_CURRENT;
        else
            return TYPE_OTHER;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder viewHolder, int position) {

        // Set their text
        viewHolder.messageText.setText(list.get(position).getMessageText());


        // Format the date before showing it
        viewHolder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                list.get(position).getMessageTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText, messageTime;
        public LinearLayout lnrMessage;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_text);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            lnrMessage = itemView.findViewById(R.id.lnrMessage);
        }
    }
}
