package net.kielian.www.tapforbpm;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BPMTapper.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BPMTapper#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BPMTapper extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button resetButton;
    private TextView BPMText;
    private Switch averageSwitch;
    private boolean averaging_on=false;
    private FrameLayout rootLayout;
    private double bpm=0;
    private double bpm_tally=0;
    private long currentMillis=-1;
    private long prevMillis =-1;
    private List<Long> longDeque;
    private long[] currentMillisArr;
    private int curIndex=0;
    private final int ARRAY_LENGTH=5;
    private final double CONSTANT=12000.0; //60000.0/ARRAY_LENGTH:


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BPMTapper.
     */
    // TODO: Rename and change types and number of parameters
    public static BPMTapper newInstance(String param1, String param2) {
        BPMTapper fragment = new BPMTapper();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BPMTapper() {
        // Required empty public constructor
    }

    private void getCurrentMillis() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate(R.layout.fragment_bpmtapper, container, false);
        resetButton = (Button) rootView.findViewById(R.id.button);
        BPMText = (TextView) rootView.findViewById(R.id.tap_here);
        rootLayout = (FrameLayout) rootView.findViewById(R.id.root_layout);
        averageSwitch = (Switch) rootView.findViewById(R.id.switch1);
        longDeque = new ArrayList<Long>();

        averageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                averaging_on = isChecked;

                //reset values each time
                //TODO Helpe function
                bpm = 0;
                prevMillis = -1;
                currentMillis = -1;
                BPMText.setText("> Tap Here <");
                curIndex=0;
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //TODO Helper Function
                bpm = 0;
                prevMillis = -1;
                currentMillis = -1;
                BPMText.setText("> Tap Here <");

                curIndex=0;
            }
        });
        rootLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!averaging_on) {
                    if (currentMillis == -1) {
                        currentMillis = System.currentTimeMillis(); // will get you current time in milli
                        BPMText.setText(">Once More<");
                    } else {
                        prevMillis = currentMillis;
                        currentMillis = System.currentTimeMillis();
                        bpm = (60000.0 / (currentMillis - prevMillis)); // will get you current time in milli
                        BPMText.setText(" " + Math.round(bpm));
                    }
                } else {
                    longDeque.add(System.currentTimeMillis());
                    bpm_tally = 0;
                    int size = longDeque.size();
                    double constant = 60000.0 / size;
                    for (int i = 1; i < size; i++) {
                        bpm_tally += constant / (longDeque.get(i) - longDeque.get(i - 1));
                    }
                    if (size > ARRAY_LENGTH) {
                        longDeque.remove(0);
                    }
                    BPMText.setText(" " + Math.round(bpm_tally));
                }
            }
        });

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
