package com.setgov.android.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.setgov.android.R;
import com.setgov.android.SimpleDividerItemDecoration;
import com.setgov.android.adapters.RepresentativeAdapter;
import com.setgov.android.models.City;
import com.setgov.android.models.Office;
import com.setgov.android.models.Representative;
import com.setgov.android.networking.GoogleCivicApiEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CityInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CityInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityInfoFragment extends Fragment {
    private static final String TAG = "CityInfoFragment";
    private static final String ARG_PARAM1 = "city";
    @InjectView(R.id.cityInfoCity)
    public TextView cityName;
    @InjectView(R.id.cityInfoState)
    public TextView stateName;
    @InjectView(R.id.cityInfoImage)
    public ImageView cityInfoImage;
    @InjectView(R.id.cityInfoGovList)
    public RecyclerView cityInfoGovList;
    private OnFragmentInteractionListener mListener;
    private City mCity;
    private GoogleCivicApiEngine mEngine;
    private ArrayList<Representative> mReps;
    private RepresentativeAdapter mAdapter;

    public CityInfoFragment() {
        // Required empty public constructor
    }

    public static CityInfoFragment newInstance(City city) {
        CityInfoFragment fragment = new CityInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (City)getArguments().getSerializable(ARG_PARAM1);
        }
         mEngine = new GoogleCivicApiEngine();

    }

    public void getReps(){
        mReps.clear();
        if(mCity.getCityName().equals("Boston")){
            Office citycouncilOffice = new Office("City Councilor");
            Office mayorOffice = new Office("Mayor");
            Representative mayor = new Representative(mayorOffice,"Martin Walsh","Democratic","","");
            Representative citycouncil1 = new Representative(citycouncilOffice,"Michelle Wu","Democratic","","");
            Representative citycouncil2 = new Representative(citycouncilOffice,"Michael Flaherty","Democratic","","");
            Representative citycouncil3 = new Representative(citycouncilOffice,"Annissa Essaibi George","Democratic","","");
            Representative citycouncil4 = new Representative(citycouncilOffice,"Ayanna Pressley","Democratic","","");
            Representative citycouncil5 = new Representative(citycouncilOffice,"Salvatore Lamattina","Democratic","","");
            Representative citycouncil6 = new Representative(citycouncilOffice,"Bill Linehan","Democratic","","");
            Representative citycouncil7 = new Representative(citycouncilOffice,"Frank Baker","Democratic","","");
            Representative citycouncil8 = new Representative(citycouncilOffice,"Andrea Campbell","Democratic","","");
            Representative citycouncil9 = new Representative(citycouncilOffice,"Timothy McCarthy","Democratic","","");
            Representative citycouncil10 = new Representative(citycouncilOffice,"Matt O'Malley","Democratic","","");
            Representative citycouncil11 = new Representative(citycouncilOffice,"Tito Jackson","Democratic","","");
            Representative citycouncil12 = new Representative(citycouncilOffice,"Josh Zakim","Democratic","","");
            Representative citycouncil13 = new Representative(citycouncilOffice,"Mark Ciommo","Democratic","","");
            mReps.add(mayor);
            mReps.add(citycouncil1);
            mReps.add(citycouncil2);
            mReps.add(citycouncil3);
            mReps.add(citycouncil4);
            mReps.add(citycouncil5);
            mReps.add(citycouncil6);
            mReps.add(citycouncil7);
            mReps.add(citycouncil8);
            mReps.add(citycouncil9);
            mReps.add(citycouncil10);
            mReps.add(citycouncil11);
            mReps.add(citycouncil12);
            mReps.add(citycouncil13);

        } else if (mCity.getCityName().equals("Fort Lauderdale")){
            Office citycommissionerOffice = new Office("City Commissioner");
            Office mayorOffice = new Office("Mayor");
            Office viceMayorOffice = new Office("Vice Mayor");
            Office cityManagerOffice = new Office("City Manager");
            Office cityClerkOffice = new Office("City Clerk");

            Representative mayor = new Representative(mayorOffice,"Jack Seiler","Democratic","","");
            Representative viceMayor = new Representative(viceMayorOffice,"Bruce G. Roberts","Republican","","");
            Representative citycommissioner1 = new Representative(citycommissionerOffice,"Dean Trantalis","Democratic","","");
            Representative citycommissioner2 = new Representative(citycommissionerOffice,"Robert L. McKinzie","Democratic","","");
            Representative citycommissioner3 = new Representative(citycommissionerOffice,"Romney Rogers","Democratic","","");
            Representative citymanager = new Representative(cityManagerOffice,"Lee R. Feldman","Democratic","","");
            Representative cityclerk = new Representative(cityClerkOffice,"Arleen Gross","Democratic","","");
            mReps.add(mayor);
            mReps.add(viceMayor);
            mReps.add(citycommissioner1);
            mReps.add(citycommissioner2);
            mReps.add(citycommissioner3);
            mReps.add(citymanager);
            mReps.add(cityclerk);
        }
        initRepresentativesView();
    }


    public void getRepresentatives(){
        Call<String> elections = mEngine.getRepresentatives(mCity.getCityName()+" "+mCity.getState());
        elections.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG,"Get elections succes: " + response.body());

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());

                    JSONObject divisions = jsonObj.getJSONObject("divisions");

                    JSONObject ocddivisionsUS = divisions.getJSONObject("ocd-division/country:us");
                    JSONObject ocddivisionsState = divisions.getJSONObject("ocd-division/country:us/state:"+mCity.getStateShort().toLowerCase());
                    JSONObject ocddivisionsCounty = divisions.getJSONObject("ocd-division/country:us/state:"+mCity.getStateShort().toLowerCase()+"/county:"+mCity.getCounty().toLowerCase());
                    JSONObject ocddivisionsPlace  = divisions.getJSONObject("ocd-division/country:us/state:"+mCity.getStateShort().toLowerCase()+"/place:"+mCity.getCityName().replace(" ","_").toLowerCase());
                    ArrayList<Office> officesList = new ArrayList<Office>();
                    ArrayList<Office> newOfficesList = new ArrayList<Office>();
                    ArrayList<Representative> representativeList = new ArrayList<Representative>();
                    ArrayList<Representative> newReps = new ArrayList<Representative>();
                    ArrayList<Representative> newNewReps = new ArrayList<Representative>();
                    JSONArray offices = jsonObj.getJSONArray("offices");
                    for(int i = 0; i< offices.length();i++){
                        Office office = new Office(offices.getJSONObject(i));
                        officesList.add(office);

                    }

                    JSONArray officials = jsonObj.getJSONArray("officials");
                    for(int i = 0; i< officials.length();i++){
                        String officialName = officials.getJSONObject(i).getString("name");
                        String officialParty = "";
                        String officialPhotoUrl = "";
                        String officialUrl = "";
                        if(officials.getJSONObject(i).has("party")){
                            officialParty = officials.getJSONObject(i).getString("party");
                        }
                        if(officials.getJSONObject(i).has("photoUrl")){
                            officialPhotoUrl = officials.getJSONObject(i).getString("photoUrl");
                        }
                        if(officials.getJSONObject(i).has("url")){
                            officialUrl = officials.getJSONObject(i).getString("Url");
                        }
                        Representative rep = new Representative(officialName,officialParty,officialPhotoUrl,officialUrl);
                        representativeList.add(rep);
                    }
                    for(Office office:officesList){
                        for(int i = 0; i<office.getOfficialIndicies().size();i++){
                            Representative r = representativeList.get(office.getOfficialIndicies().get(i));
                            r.setOffice(office);
                            newReps.add(r);
                        }
                    }
                    Log.d(TAG,"divisions: " + ocddivisionsState.toString());
                    Log.d(TAG,"divisions: " + ocddivisionsCounty.toString());
                    Log.d(TAG,"divisions: " + ocddivisionsPlace.toString());
                    ArrayList<Integer> selectedOfficesIndices = new ArrayList<Integer>();
                    ArrayList<Integer> selectedOfficialsIndices = new ArrayList<Integer>();
                    JSONArray selectedOfficesCountyJSON = ocddivisionsCounty.getJSONArray("officeIndices");

                    Log.d(TAG,"selectedOfficesCountyJSON"+selectedOfficesCountyJSON.toString());
                    for(int i = 0 ;i <selectedOfficesCountyJSON.length();i++){
                        selectedOfficesIndices.add(selectedOfficesCountyJSON.getInt(i));
                    }
                    JSONArray selectedOfficesCityJSON = ocddivisionsPlace.getJSONArray("officeIndices");
                    Log.d(TAG, selectedOfficesCityJSON.toString());
                    for(int i = 0 ;i <selectedOfficesCityJSON.length();i++){
                        selectedOfficesIndices.add(selectedOfficesCityJSON.getInt(i));
                    }
                    for(int i = 0; i <selectedOfficesIndices.size();i++){
                        Office of = officesList.get(selectedOfficesIndices.get(i));
                        newOfficesList.add(of);
                    }
                    for(Office off:newOfficesList){
                        ArrayList<Integer> finalOfficalIndicesList = new ArrayList<Integer>();
                        finalOfficalIndicesList.addAll(off.getOfficialIndicies());
                        for(int i = 0; i< finalOfficalIndicesList.size();i++){
                            Representative r = representativeList.get(finalOfficalIndicesList.get(i));
                            newNewReps.add(r);
                        }
                    }
                    for(Representative rep: newNewReps){
                        Log.d(TAG,"repOffice: " + rep.getOffice().getName());
                        Log.d(TAG,"repName: " + rep.getName());
                    }
                    mReps = newNewReps;
                    initRepresentativesView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_info, container, false);
        ButterKnife.inject(this,view);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.my_city);
        cityName.setText(mCity.getCityName());
        stateName.setText(mCity.getState());
        Resources res = getActivity().getResources();
        String mDrawableName = mCity.getCityName().toLowerCase().replace(" ","");
        int resID = res.getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());
        cityInfoImage.setImageResource(resID);
       // getRepresentatives();
        mReps = new ArrayList<>();
        getReps();
        return view;
    }

    public void initRepresentativesView(){
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 1);
        cityInfoGovList.setLayoutManager(manager);
        cityInfoGovList.addItemDecoration(new SimpleDividerItemDecoration(getActivity(),false));
        mAdapter = new RepresentativeAdapter(cityInfoGovList,getActivity(), mReps);
        cityInfoGovList.setAdapter(mAdapter);
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
