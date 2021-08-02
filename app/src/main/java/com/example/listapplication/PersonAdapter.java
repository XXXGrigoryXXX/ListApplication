package com.example.listapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    interface setOnClickListener{
        void onClick(Person person, int position);
    }

    private final setOnClickListener onClickListener;

    private final LayoutInflater inflater;
    ArrayList<Person> arrayList = new ArrayList<>();

    PersonAdapter(Context context, ArrayList<Person> arrayList, setOnClickListener onClickListener) {

        this.onClickListener = onClickListener;
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        PersonViewHolder personViewHolder = new PersonViewHolder(view);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {

        Person person = arrayList.get(position);

        holder.dbId.setText(Integer.toString(person.getDbId()));
        holder.name.setText(String.valueOf(person.getName()));
        holder.gender.setText(String.valueOf(person.getGender()));
        holder.date.setText(String.valueOf(person.getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onClick(person, position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView dbId, name, gender, date;

        PersonViewHolder (View view) {

            super(view);

            dbId = (TextView) view.findViewById(R.id.textViewMemberId);
            name = (TextView) view.findViewById(R.id.textViewName);
            gender = (TextView) view.findViewById(R.id.textViewGender);
            date = (TextView) view.findViewById(R.id.textViewDateOfBirth);
        }

    }

    // ArrayList<Person> arrayList = new ArrayList<>();

    /* PersonAdapter(ArrayList<Person> arrayList) {

        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Fruit fruit = arrayList.get(position);

        holder.name.setText(fruit.getName());
        holder.calories.setText(Integer.toString(fruit.getCalories()));
        holder.fat.setText(String.valueOf(fruit.getFat()));
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMemberId, textViewName, textViewGender, textViewDateOfBirth;

        RecyclerViewHolder (View view) {

            super(view);

            textViewMemberId = (TextView) view.findViewById(R.id.textViewMemberId);
            textViewName = (TextView) view.findViewById(R.id.textViewName);
            textViewGender = (TextView) view.findViewById(R.id.textViewGender);
            textViewDateOfBirth = (TextView) view.findViewById(R.id.textViewDateOfBirth);
        }
    } */

}

/* import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {
    private final ArrayList<Person> rows;

    public static class ViewHolder {
        TextView tvDbId;
        TextView tvName;
        TextView tvGender;
        TextView tvDate;
    }

    public PersonAdapter(@NonNull Context context, @NonNull ArrayList<Person> list) {
        super(context, R.layout.list_item, list);
        rows = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);

            holder = new ViewHolder();
            holder.tvDbId = convertView.findViewById(R.id.textViewMemberId);
            holder.tvName = convertView.findViewById(R.id.textViewName);
            holder.tvGender = convertView.findViewById(R.id.textViewGender);
            holder.tvDate = convertView.findViewById(R.id.textViewDateOfBirth);
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
} */

