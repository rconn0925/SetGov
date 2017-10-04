package com.setgov.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.setgov.android.R;
import com.setgov.android.fragments.AgendaInfoFragment;
import com.setgov.android.models.Agenda;
import com.setgov.android.viewholders.AgendaViewHolder;

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
        Log.d(TAG," agendaEventID: "+ agenda.getEvent().getId());

        if(agenda.getEvent().getId() == 28) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://fortlauderdale.legistar.com/View.ashx?M=A&ID=542490&GUID=2D769EE5-7983-4869-A3F4-E5D31967452E")));
        } else {
            Toast.makeText(mContext.getApplicationContext(), "Agenda not yet available", Toast.LENGTH_SHORT).show();
        }

        /*
        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        Fragment currentFragment = AgendaInfoFragment.newInstance(agenda);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(TAG).replace(R.id.contentFrame, currentFragment).commit();
        */
    }
}
