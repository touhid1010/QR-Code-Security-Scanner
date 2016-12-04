package com.netizenbd.netichecker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.netizenbd.netichecker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SMS.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SMS#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SMS extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    EditText editText_SMSText;
    CheckBox checkbox_sendSMS;
    Button button_smsSave;
    public static final String PREFERENCE_NAME_SETTINGS = "settingsPref";
    public static final String PREFERENCE_SMS_KEY_SMS_TEXT = "smstext";
    public static final String PREFERENCE_SMS_KEY_CHECKBOX = "check";
    boolean b;
    SharedPreferences preferences;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SMS() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SMS.
     */
    // TODO: Rename and change types and number of parameters
    public static SMS newInstance(String param1, String param2) {
        SMS fragment = new SMS();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View view = inflater.inflate(R.layout.fragment_sms, container, false);

        editText_SMSText = (EditText) view.findViewById(R.id.editText_SMSText);
        checkbox_sendSMS = (CheckBox) view.findViewById(R.id.checkbox_sendSMS);
        button_smsSave = (Button) view.findViewById(R.id.button_smsSave);
        button_smsSave.setOnClickListener(this);

        // Shared pref
        preferences = this.getActivity().getSharedPreferences(PREFERENCE_NAME_SETTINGS, Context.MODE_PRIVATE);
        if (preferences != null) {
            editText_SMSText.setText(preferences.getString(PREFERENCE_SMS_KEY_SMS_TEXT, ""));
            checkbox_sendSMS.setChecked(preferences.getBoolean(PREFERENCE_SMS_KEY_CHECKBOX, false));
        }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_smsSave:


                if (checkbox_sendSMS.isChecked()) {
                    b = true;
                } else {
                    b = false;
                }

                // Shared Pref
                Editor editor = preferences.edit();
                editor.putString(PREFERENCE_SMS_KEY_SMS_TEXT, editText_SMSText.getText().toString());
                editor.putBoolean(PREFERENCE_SMS_KEY_CHECKBOX, b);
                editor.apply();
                editor.commit();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                break;
        }
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
