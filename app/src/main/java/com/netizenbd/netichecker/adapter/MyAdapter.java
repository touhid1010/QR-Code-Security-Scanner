package com.netizenbd.netichecker.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netizenbd.netichecker.R;
import com.netizenbd.netichecker.sqlitedatabase.DataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md. Touhidul Islam on 11/22/2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ParticipantViewHolder> {

    // Getting list of participant
    List<DataEntity> dataEntityList;
    Context context;
    LayoutInflater inflater;

    public MyAdapter(Context context, List<DataEntity> dataEntityList) {
        this.context = context;
        this.dataEntityList = dataEntityList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ParticipantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cardview_participant_list, parent, false);
        ParticipantViewHolder viewHolder1 = new ParticipantViewHolder(v);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ParticipantViewHolder holder, int position) {
        DataEntity dataEntity = dataEntityList.get(position);
        holder.textView_name.setText(dataEntity.getName());
        holder.textView_participantID.setText(dataEntity.getParticipateID());
        holder.textView_participantType.setText(dataEntity.getParticipateType());
        holder.textView_phone.setText(dataEntity.getPhone());
        holder.textView_area.setText(dataEntity.getArea());
        holder.textView_checkingTime.setText("" + dataEntity.getDateTime());


        // on Click listener
//        holder.cardView_list
    }

    @Override
    public int getItemCount() {
        return dataEntityList.size();
    }

    /**
     * Apply ViewHolder pattern
     */
    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {

        CardView cardView_list;

        TextView textView_name,
                textView_participantID,
                textView_participantType,
                textView_phone,
                textView_area,
                textView_checkingTime;

        ImageView personPhoto;

        ParticipantViewHolder(View itemView) {
            super(itemView);
            cardView_list = (CardView) itemView.findViewById(R.id.cardView_list);

            textView_name = (TextView) itemView.findViewById(R.id.textView_name);
            textView_participantID = (TextView) itemView.findViewById(R.id.textView_participantID);
            textView_participantType = (TextView) itemView.findViewById(R.id.textView_participantType);
            textView_phone = (TextView) itemView.findViewById(R.id.textView_phone);
            textView_area = (TextView) itemView.findViewById(R.id.textView_area);
            textView_checkingTime = (TextView) itemView.findViewById(R.id.textView_checkingTime);

//            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);

        }
    }


    /**
     * Apply filter for search
     */
    public void setFilter(List<DataEntity> dataEntities) {
        dataEntityList = new ArrayList<>();
        dataEntityList.addAll(dataEntities);
        Log.d("ttt", "setFilter: " + dataEntities.size());
        notifyDataSetChanged();
    }
}
