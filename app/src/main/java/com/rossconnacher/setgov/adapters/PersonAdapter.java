package com.rossconnacher.setgov.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.models.Person;
import com.rossconnacher.setgov.viewholders.PersonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 6/18/2017.
 */

public class PersonAdapter  extends RecyclerView.Adapter<PersonViewHolder>{

    private static final String TAG = "PersonAdapter";
    private Context mContext;
    private List<Person> mPersons;
   // private RecyclerView mRecyclerView;

    public PersonAdapter(Context context, ArrayList<Person> persons){
        this.mContext = context;
        this.mPersons = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        final Person person = mPersons.get(position);
        holder.personImageView.setImageResource(person.getImageResID());
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }
}
