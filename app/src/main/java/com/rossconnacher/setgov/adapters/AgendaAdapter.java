package com.rossconnacher.setgov.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.activities.AgendaInfoActivity;
import com.rossconnacher.setgov.models.Agenda;
import com.rossconnacher.setgov.models.Event;
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
    private Event mEvent;

    public AgendaAdapter(Event event, RecyclerView recyclerView, Context context, ArrayList<Agenda> agendas){
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        this.mAgendas = agendas;
        this.mEvent = event;
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
        Intent intent = new Intent(mContext, AgendaInfoActivity.class);
        intent.putExtra("Agenda",agenda);
        intent.putExtra("Event", mEvent);
        mContext.startActivity(intent);
    }
}
