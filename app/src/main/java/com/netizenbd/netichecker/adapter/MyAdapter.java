package com.netizenbd.netichecker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.netizenbd.netichecker.CommonNames;
import com.netizenbd.netichecker.ParticipantList;
import com.netizenbd.netichecker.R;
import com.netizenbd.netichecker.sqlitedatabase.DataEntity;
import com.netizenbd.netichecker.sqlitedatabase.DataService;

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
    public void onBindViewHolder(final ParticipantViewHolder holder, int position) {
        DataEntity dataEntity = dataEntityList.get(position);
        holder.textView_name.setText(dataEntity.getName());
        holder.textView_participantID.setText(dataEntity.getParticipateID());
        holder.textView_participantType.setText(dataEntity.getParticipateType());
        holder.textView_phone.setText(dataEntity.getPhone());
        holder.textView_area.setText(dataEntity.getArea());
        holder.textView_checkingTime.setText("" + dataEntity.getDateTime());

        // Set tag for delete action
        holder.textView_name.setTag(dataEntity.getTableColumnId()); // To delete by column id


        // on Click listener
        holder.imageButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Delete item from db
                 */
                DataService dataService = new DataService(view.getContext());
                long deleteStatus = dataService.deleteParticipantData(holder.textView_name.getTag().toString());
                if (deleteStatus > 0) {
                    Toast.makeText(view.getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
                }

                /**
                 * Remove item from list after delete from db
                 */
                int newPosition = holder.getAdapterPosition();
                Log.d("touhid","on Click onBindViewHolder");
                dataEntityList.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, dataEntityList.size());

                /**
                 * Change Total participant after delete item
                 */
                ParticipantList participantList = new ParticipantList();
                participantList.getAndShowParticipantAmount((Activity) context);


            }
        });


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

        ImageButton imageButton_delete;

        ParticipantViewHolder(final View itemView) {
            super(itemView);
            cardView_list = (CardView) itemView.findViewById(R.id.cardView_list);

            textView_name = (TextView) itemView.findViewById(R.id.textView_name);
            textView_participantID = (TextView) itemView.findViewById(R.id.textView_participantID);
            textView_participantType = (TextView) itemView.findViewById(R.id.textView_participantType);
            textView_phone = (TextView) itemView.findViewById(R.id.textView_phone);
            textView_area = (TextView) itemView.findViewById(R.id.textView_area);
            textView_checkingTime = (TextView) itemView.findViewById(R.id.textView_checkingTime);

            imageButton_delete = (ImageButton) itemView.findViewById(R.id.imageButton_delete);

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
