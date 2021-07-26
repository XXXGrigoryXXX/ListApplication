package com.example.listapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {
    private final List<Person> rows;

    public static class ViewHolder {
        TextView tvDbId;
        TextView tvName;
        TextView tvGender;
        TextView tvDate;
    }

    public PersonAdapter(@NonNull Context context, @NonNull List<Person> list) {
        super(context, R.layout.row_layout, list);
        rows = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);

            holder = new ViewHolder();
            holder.tvDbId = convertView.findViewById(R.id.list_db_id);
            holder.tvName = convertView.findViewById(R.id.list_name);
            holder.tvGender = convertView.findViewById(R.id.list_gender);
            holder.tvDate = convertView.findViewById(R.id.list_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvDbId.setText(String.valueOf(rows.get(position).dbId));
        holder.tvName.setText(rows.get(position).name);
        holder.tvGender.setText(rows.get(position).gender);
        holder.tvDate.setText(rows.get(position).date);

        return convertView;
    }
}
