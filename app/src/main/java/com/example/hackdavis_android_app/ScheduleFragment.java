package com.example.hackdavis_android_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import androidx.fragment.app.Fragment;


public class ScheduleFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private View rootView;
    private LayoutInflater layoutInflater;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    // Got some data from the static json
    public void addScheduleData(JSONObject scheduleData) {
        System.out.println("got here");
        try {
            Object value = scheduleData.getString("data");
            JSONArray jarray = new JSONArray(value.toString());

            for (int i = 0; i < jarray.length(); i++)
            {
                Object eventObject = jarray.get(i);

                if (eventObject instanceof JSONObject)
                {
                    addScheduleCard(new JSONObject((eventObject.toString())));
                }
            }

            // Remove progressbar
            View progressBar = rootView.findViewById(R.id.progressBar);
            ((ViewGroup) progressBar.getParent()).removeView(progressBar);
        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private void addScheduleCard(JSONObject eventData)
    {
        LinearLayout linearLayoutContainer = rootView.findViewById(R.id.event_container);

        View v = layoutInflater.inflate(R.layout.schedule_card, linearLayoutContainer, false);

        // fill in any details dynamically here
        TextView titleView = (TextView) v.findViewById(R.id.card_title);
        TextView timeView = (TextView) v.findViewById(R.id.card_time);
        TextView descView = (TextView) v.findViewById(R.id.card_description);
        try {
            titleView.setText(eventData.getString("name"));
            descView.setText(eventData.getString("description"));
            timeView.setText(eventData.getString("time"));
        } catch (JSONException e)
        {
            System.out.println("Error: " + e.toString());
        }

        linearLayoutContainer.addView(v);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

// Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


        return rootView;
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
    }
}
