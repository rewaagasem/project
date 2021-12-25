package com.rewaagasem.university_project_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rewaagasem.university_project_android.R;
import com.rewaagasem.university_project_android.model.BMIRecord;

import java.util.List;

public class BMIRecordsAdapter extends RecyclerView.Adapter<BMIRecordsAdapter.RecordVH> {


    List<BMIRecord> recordList;


    public BMIRecordsAdapter(List<BMIRecord> recordList) {
        this.recordList =recordList;

    }

    @NonNull
    @Override
    public RecordVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_row, parent, false);
        return new RecordVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordVH holder, int position) {
        holder.onBind(recordList.get(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }


    class RecordVH extends RecyclerView.ViewHolder {

        TextView date,weight,length,status;

        public RecordVH(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.record_date);
            weight = itemView.findViewById(R.id.record_weight);
            length = itemView.findViewById(R.id.record_length);
            status = itemView.findViewById(R.id.record_status);


        }

        public void onBind(BMIRecord record) {

            date.setText(record.getDate());
            weight.setText(record.getWeight());
            length.setText(record.getLength());
            status.setText(record.getStatus());

        }
    }
}