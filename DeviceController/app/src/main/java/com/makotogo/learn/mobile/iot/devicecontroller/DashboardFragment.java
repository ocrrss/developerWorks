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
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnDashboardFragmentInteractionListener}
 * interface.
 */
public class DashboardFragment extends Fragment {

    private static final String TAG = DashboardFragment.class.getSimpleName();

    // The Fragment Tag.
    public static final String FRAGMENT_TAG = DashboardFragment.class.getName();

    // Parameter argument names (for access within the Bundle)
    private static final String ARG_COLUMN_COUNT = "arg-column-count";
    private static final String ARG_PROPERTIES = "arg-properties";

    // Parameter variables
    private int mColumnCount;
    private OnDashboardFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashboardFragment() {
    }


    public static DashboardFragment newInstance(ApplicationProperties applicationProperties, int columnCount) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable(ARG_PROPERTIES, applicationProperties);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dashboard_list, container, false);

        // Subscribe to discovery response messages
        mListener.subscribeToDeviceResponse(JSONDiscoveryDeviceResponse.DISCOVERY.toString(), new DeviceResponseCallback() {
            @Override
            public void onDeviceResponse(MqttMessage deviceResponseMessage) {
                final String METHOD = "onDeviceResponse(): ";
                // Get the response
                String messageAsString = new String(deviceResponseMessage.getPayload());
                // Create a JSON object
                try {
                    JSONObject jsonObject = new JSONObject(messageAsString);
                    // TODO: Clean up these string literals. They offend me.
                    jsonObject = jsonObject.getJSONObject(JSONDiscoveryDeviceResponse.D.toString());
                    String deviceResponse = jsonObject.getString(JSONDiscoveryDeviceResponse.DEVICE_RESPONSE.toString());
                    JSONArray devices = jsonObject.getJSONArray(JSONDiscoveryDeviceResponse.DEVICES.toString());
                    // Clear out the current collection of IotDeviceItems
                    IotDeviceContent.clearAll();
                    // Create an IotDeviceItem for each device in the list
                    for (int aa = 0; aa < devices.length(); aa++) {
                        JSONObject device = devices.getJSONObject(aa);
                        // Create the IotDeviceItem
                        String deviceId = device.getString(JSONDiscoveryDeviceResponse.DEVICE_ID.toString());
                        String deviceDescription = device.getString(JSONDiscoveryDeviceResponse.DESCRIPTION.toString());
                        JSONArray actionsJsonArray = device.getJSONArray(JSONDiscoveryDeviceResponse.ACTIONS.toString());
                        //
                        // Loop through the actions and set them on the IotDeviceContent object
                        List<Pair<String, String>> actions = new ArrayList<>();
                        for (int bb = 0; bb < actionsJsonArray.length(); bb++) {
                            JSONObject action = (JSONObject)actionsJsonArray.get(bb);
                            String actionName = action.getString(JSONDiscoveryDeviceResponse.ACTION.toString());
                            String actionDescription = action.getString(JSONDiscoveryDeviceResponse.DESCRIPTION.toString());
                            actions.add(Pair.of(actionName, actionDescription));
                        }
                        IotDeviceContent.IotDeviceItem iotDeviceItem =
                                new IotDeviceContent.IotDeviceItem(Integer.valueOf(aa).toString(), deviceId, deviceDescription, actions);
                        IotDeviceContent.addItem(iotDeviceItem);
                    }
                    // Set the adapter
                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view;
                        if (mColumnCount <= 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                        }
                        recyclerView.setAdapter(new DashboardRecyclerViewAdapter(IotDeviceContent.ITEMS, mListener));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, METHOD+"JSONException occurred while creating JSONObject from String: '" + messageAsString + "'", e);
                }
            }
        });

        // Get the ApplicationClient and broadcast a "discovery" request to
        /// see all of the devices that the Controller device is managing.
        mListener.publishDiscoveryEvent();
        return view;
    }

    /**
     * Callback to hand items to this class for display
     * @param items
     */
    public void onItemsDeliveredCallback(List<IotDeviceContent.IotDeviceItem> items) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardFragmentInteractionListener) {
            mListener = (OnDashboardFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDashboardFragmentInteractionListener");
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
    public interface OnDashboardFragmentInteractionListener {

        /**
         * Tell the owning Activity that the user has pressed an item
         * in the RecyclerView.
         *
         * @param item The item that was pressed
         */
        void onDashboardFragmentInteraction(IotDeviceContent.IotDeviceItem item);

        void publishDiscoveryEvent();

        void subscribeToDeviceResponse(String deviceResponse, DeviceResponseCallback deviceResponseCallback);

    }
}
