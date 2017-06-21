package com.rossconnacher.setgov.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.fragments.AgendaInfoFragment;
import com.rossconnacher.setgov.models.Agenda;
import com.rossconnacher.setgov.viewholders.AgendaViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 6/16/2017.
 */

public class AgendaAdapter extends RecyclerView.Adapter<AgendaViewHolder> implements View.OnClickListener {

    private static final String TAG = "AgendaAdapter";
    private Context mContext;
    private List<Agenda> mAgendas;
    private RecyclerView mRecyclerView;

    public AgendaAdapter(RecyclerView recyclerView, Context context, ArrayList<Agenda> agendas){
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        this.mAgendas = agendas;
    }

    @Override
    public AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.agenda_item, parent, false);
        view.setOnClickListener(this);
        return new AgendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, int position) {
        final Agenda agenda = mAgendas.get(position);

        holder.agendaTitle.setText(agenda.getTitle());
        holder.agendaCategory.setText(agenda.getCategory());
      //  holder.agendaimage.setImageDrawable();
    }

    @Override
    public int getItemCount() {
        return mAgendas.size();
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        Agenda agenda = mAgendas.get(itemPosition);
        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        Fragment currentFragment = AgendaInfoFragment.newInstance(agenda);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.exit_to_right,R.anim.enter_from_left).addToBackStack(TAG).replace(R.id.contentFrame, currentFragment).commit();
    }
}
