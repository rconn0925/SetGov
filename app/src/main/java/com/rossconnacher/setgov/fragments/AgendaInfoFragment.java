package com.rossconnacher.setgov.fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.models.Agenda;
import com.rossconnacher.setgov.models.Event;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AgendaInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AgendaInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaInfoFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "agenda";



    @InjectView(R.id.agendaEventImage)
    public ImageView agendaEventImage;
    @InjectView(R.id.agendaDiscussionTopic)
    public TextView agendaDiscussionTopic;
    @InjectView(R.id.agendaUpdate)
    public TextView agendaUpdate;
    @InjectView(R.id.agendaComments)
    public TextView agendaComments;

    private Agenda mAgenda;
    private Event mEvent;

    private OnFragmentInteractionListener mListener;
    private ImageView backButton;

    public AgendaInfoFragment() {
        // Required empty public constructor
    }


    public static AgendaInfoFragment newInstance(Agenda agenda) {
        AgendaInfoFragment fragment = new AgendaInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, agenda);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAgenda = (Agenda)getArguments().getSerializable(ARG_PARAM1);
            mEvent  = mAgenda.getEvent();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_agenda_info, container, false);
        ButterKnife.inject(this,view);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(mEvent.getName());

        agendaComments.setText(mAgenda.getComments());
        agendaEventImage.setImageResource(mEvent.getImageResID());
        agendaDiscussionTopic.setText("Discussion Topic: "+mAgenda.getTitle());
        agendaUpdate.setText(mAgenda.getCategory());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
