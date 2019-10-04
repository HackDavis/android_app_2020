package com.example.hackdavis_android_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class ScheduleFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        for (int i = 0; i < 20; i++) {
            initScheduleView(rootView, inflater);
        }


        return rootView;
    }

    public void initScheduleView(View rootView, LayoutInflater inflater)
    {
        LinearLayout linearLayoutContainer = rootView.findViewById(R.id.event_container);

        View v = inflater.inflate(R.layout.schedule_card, linearLayoutContainer, false);

        // fill in any details dynamically here
        TextView textView = (TextView) v.findViewById(R.id.card_title);
        textView.setText("Test title");

        linearLayoutContainer.addView(v);
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
