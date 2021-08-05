package com.example.listapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private final ArrayList<Person> arrayList;
    private final OnPersonItemClick listener;

    PersonAdapter(ArrayList<Person> arrayList, OnPersonItemClick listener) {
        this.listener = listener;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PersonViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = arrayList.get(position);
        holder.bind(person);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView name, gender, date;
        private final TextView dbId;
        private Person person;

        PersonViewHolder(View view, OnPersonItemClick listener) {
            super(view);
            dbId = view.findViewById(R.id.textViewMemberId);
            name = (TextView) view.findViewById(R.id.textViewName);
            gender = (TextView) view.findViewById(R.id.textViewGender);
            date = (TextView) view.findViewById(R.id.textViewDateOfBirth);
            view.setOnClickListener(v -> {
                if (listener == null || person == null) {
                    return;
                }
                listener.onPersonClicked(person.getDbId(), getAdapterPosition());
            });
        }

        public void bind(Person person) {
            this.person = person;
            dbId.setText(Integer.toString(person.getDbId()));
            name.setText(String.valueOf(person.getName()));
            gender.setText(String.valueOf(person.getGender()));
            date.setText(String.valueOf(person.getDate()));
        }
    }

    public interface OnPersonItemClick {
        void onPersonClicked(int dbId, int position);
    }
}

