/*
 *    Copyright 2018 Makoto Consulting Group, Inc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.makotogo.learn.mobile.iot.devicecontroller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;

import java.util.Properties;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHomeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROPERTIES = "arg-properties";
    public static final String FRAGMENT_TAG = HomeFragment.class.getName();

    private ApplicationProperties mProperties;

    private OnHomeFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param applicationProperties
     * @return
     */
    public static HomeFragment newInstance(ApplicationProperties applicationProperties) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROPERTIES, applicationProperties);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProperties = (ApplicationProperties)getArguments().getSerializable(ARG_PROPERTIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_home, container, false);
        //
        // Setup the view
        // Org ID
        EditText orgId = view.findViewById(R.id.orgIdTextField);
        orgId.setText(mProperties.getOrgId());
        // Controller Device Type
        EditText deviceType = view.findViewById(R.id.deviceTypeTextField);
        deviceType.setText(mProperties.getControllerDeviceType());
        // Controller Device ID
        EditText deviceId = view.findViewById(R.id.deviceIdTextField);
        deviceId.setText(mProperties.getControllerDeviceId());
        // API Key
        EditText apiKey = view.findViewById(R.id.apiKeyTextField);
        apiKey.setText(mProperties.getApiKey());
        // Auth Token
        EditText authToken = view.findViewById(R.id.authTokenTextField);
        authToken.setText(mProperties.getAuthToken());
        // Connect button
        Button connect = view.findViewById(R.id.connectButton);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reach out to the Activity and get the ApplicationClient reference.
                // Drop the ApplicationClient reference on the floor, we don't care
                // about it at the moment.
                mListener.getApplicationClientAndConnectIfNecessary();
                // Save the current values to SharedPreferences so the user only
                // has to enter them once.
                saveCurrentValues(view);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            mListener = (OnHomeFragmentInteractionListener) context;
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

    //****************************************************************************
    //

    /**
     * Save the current field values in the ApplicationProperties object
     */
    private void saveCurrentValues(View view) {
        // OrgId
        EditText textField = view.findViewById(R.id.orgIdTextField);
        mProperties.setProperty(ApplicationProperties.ORG_ID, textField.getText().toString());
        // DeviceType
        textField = view.findViewById(R.id.deviceTypeTextField);
        mProperties.setProperty(ApplicationProperties.CONTROLLER_DEVICE_TYPE, textField.getText().toString());
        // DeviceId
        textField = view.findViewById(R.id.deviceIdTextField);
        mProperties.setProperty(ApplicationProperties.CONTROLLER_DEVICE_ID, textField.getText().toString());
        // API Key
        textField = view.findViewById(R.id.apiKeyTextField);
        mProperties.setProperty(ApplicationProperties.API_KEY, textField.getText().toString());
        // AuthToken
        textField = view.findViewById(R.id.authTokenTextField);
        mProperties.setProperty(ApplicationProperties.AUTH_TOKEN, textField.getText().toString());
    }

    public interface OnHomeFragmentInteractionListener {

        /**
         * Return the ApplicationClient managed by MainActivity.
         * If ApplicationClient does not exist, it will be created.
         * Guaranteed to be connected upon successful return.
         *
         * @return ApplicationClient - a connected ApplicationClient that
         * can be used to communicate with the MQTT server.
         */
        ApplicationClient getApplicationClientAndConnectIfNecessary();

    }

}
