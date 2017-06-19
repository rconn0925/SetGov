package com.rossconnacher.setgov.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.models.Agenda;
import com.rossconnacher.setgov.models.Event;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AgendaInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.backButton)
    public ImageView backButton;
    @InjectView(R.id.toolbarTitle)
    public TextView toolbarTitle;

    private Agenda mAgenda;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_info);
        ButterKnife.inject(this);

        mAgenda  = (Agenda) getIntent().getSerializableExtra("Agenda");
        mEvent  = (Event) getIntent().getSerializableExtra("Event");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==backButton.getId()){
            Intent i = new Intent(this, EventInfoActivity.class);
            i.putExtra("Event",mEvent);
            startActivity(i);
        }
    }
}
