package com.setgov.android.adapters;

/**
 * Created by Ross on 9/16/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.setgov.android.R;
import com.setgov.android.models.AgendaPDF;
import com.setgov.android.viewholders.AgendaPDFViewHolder;
import com.setgov.android.viewholders.AgendaViewHolder;

import java.util.ArrayList;
import java.util.List;



public class AgendaPDFAdapter extends RecyclerView.Adapter<AgendaPDFViewHolder> implements View.OnClickListener {

    private static final String TAG = "AgendaPDFAdapter";
    private Context mContext;
    private List<AgendaPDF> mAgendas;
    private RecyclerView mRecyclerView;

    public AgendaPDFAdapter(RecyclerView recyclerView, Context context, ArrayList<AgendaPDF> agendas){
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        this.mAgendas = agendas;
    }

    @Override
    public AgendaPDFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.agenda_pdf_item, parent, false);
        view.setOnClickListener(this);
        return new AgendaPDFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgendaPDFViewHolder holder, int position) {
        final AgendaPDF agenda = mAgendas.get(position);

        holder.agendaCategory.setText(agenda.getEvent().getDescription());

    }

    @Override
    public int getItemCount() {
        return mAgendas.size();
    }

    @Override
    public void onClick(View v) {



        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        AgendaPDF agenda = mAgendas.get(itemPosition);
        Log.d(TAG," agendaEventID: "+ agenda.getEvent().getId());

        if(agenda.getEvent().getId() == 28) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(agenda.getPdfURL())));
        }

        /*
        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        Fragment currentFragment = AgendaInfoFragment.newInstance(agenda);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(TAG).replace(R.id.contentFrame, currentFragment).commit();
        */
    }
}
