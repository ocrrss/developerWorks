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
import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * Created by sperry on 3/1/18.
 */

public class ApplicationProperties extends Properties {
    // The first-class properties supported by this class
    public static final String MQTT_SERVER_HOST_NAME = "mqtt.server.host.name";
    public static final String MQTT_SERVER_PROTOCOL = "mqtt.server.protocol";
    public static final String MQTT_SERVER_PORT = "mqtt.server.port";
    public static final String ORG_ID = "org.id";
    public static final String API_KEY = "api.key";
    public static final String AUTH_TOKEN = "auth.token";
    public static final String CONTROLLER_DEVICE_TYPE = "controller.device.type";
    public static final String CONTROLLER_DEVICE_ID = "controller.device.id";
    public static final String CONTROLLER_ACTION = "controller.action";


    private Context mContext;

    public ApplicationProperties(Context context) {
        super();
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null!");
        }
        mContext = context;
    }

    public String getMqttServerHostName() {
        return fetchStringProperty(MQTT_SERVER_HOST_NAME);
    }

    public void setMqttServerHostName(String mqttServerHostName) {
        storeProperty(MQTT_SERVER_HOST_NAME, mqttServerHostName);
    }

    public String getMqttServerProtocol() {
        return fetchStringProperty(MQTT_SERVER_PROTOCOL);
    }

    public void setMqttServerProtocol(String mqttServerProtocol) {
        storeProperty(MQTT_SERVER_PROTOCOL, mqttServerProtocol);
    }

    public String getMqttServerPort() {
        return fetchStringProperty(MQTT_SERVER_PORT);
    }

    public void setMqttServerPort(String mqttServerPort) {
        storeProperty(MQTT_SERVER_PORT, mqttServerPort);
    }

    public String getOrgId() {
        return fetchStringProperty(ORG_ID);
    }

    public void setOrgId(String orgId) {
        storeProperty(ORG_ID, orgId);
    }

    public String getApiKey() {
        return fetchStringProperty(API_KEY);
    }

    public void setApiKey(String apiKey) {
        storeProperty(API_KEY, apiKey);
    }

    public String getAuthToken() {
        return fetchStringProperty(AUTH_TOKEN);
    }

    public void setAuthToken(String authToken) {
        storeProperty(AUTH_TOKEN, authToken);
    }

    public String getControllerDeviceType() {
        return fetchStringProperty(CONTROLLER_DEVICE_TYPE);
    }

    public void setControllerDeviceType(String controllerDeviceType) {
        storeProperty(CONTROLLER_DEVICE_TYPE, controllerDeviceType);
    }

    public String getControllerDeviceId() {
        return fetchStringProperty(CONTROLLER_DEVICE_ID);
    }

    public void setControllerDeviceId(String controllerDeviceId) {
        storeProperty(CONTROLLER_DEVICE_ID, controllerDeviceId);
    }

    public String getControllerAction() {
        return fetchStringProperty(CONTROLLER_ACTION);
    }

    public void setControllerAction(String controllerAction) {
        storeProperty(CONTROLLER_ACTION, controllerAction);
    }

    public void saveAll() {

    }

    /**
     * Helper method. Attempts to locate the specified property
     * in the "cache" (the super class' Properties store).
     * If that fails, the property will be loaded from SharedPreferences.
     *
     * @param propertyName
     * @return
     */
    private String fetchStringProperty(String propertyName) {
        String ret;
        //
        // First see if the property is in the "cache"
        ret = getProperty(propertyName);
        if (ret == null) {
            // Not there. Try SharedPreferences.
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(ApplicationProperties.class.getName(), Context.MODE_PRIVATE);
            ret = sharedPreferences.getString(propertyName, null);
            //
            // Property hit in SharedPreferences, store it in the cache
            if (ret != null) {
                setProperty(propertyName, ret);
            }
        }
        // Set the return value to Empty string (instead of null)
        if (ret == null) {
            ret = StringUtils.EMPTY;
        }
        return ret;
    }

    // TODO: Support other property types than String

    private void storeProperty(String propertyName, String propertyValue) {
        // Store the property in the cache
        setProperty(propertyName, propertyValue);
        // Now store it in SharedPreferences
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(ApplicationProperties.class.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(propertyName, propertyValue);
    }

    // TODO: Support other property types than String
}
