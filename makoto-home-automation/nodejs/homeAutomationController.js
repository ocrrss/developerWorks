//
// The Home Automation Controller
//
var client = require("ibmiotf")
var exec = require('child_process').exec

// The configuration
var appConfig = require("./config/myAppConfig.json")
var deviceConfig = require("./config/myDeviceConfig.json")

// Create the Application Client
var applicationClient = new client.IotfApplication(appConfig);
console.log("Connecting application client...");
applicationClient.connect();

/**
 * When a connect message is received, we're good to go
 */
applicationClient.on("connect", function() {
    console.log("Application client, connected.");
    applicationClient.subscribeToDeviceEvents(appConfig.deviceType);
});

/**
 * When an error is received, process it
 */
applicationClient.on("error", function(err) {
    console.log("Error: " + err);
});

/**
 * When a device event is received, process it
 */
applicationClient.on("deviceEvent", function (deviceType, deviceId, eventType, format, payload) {

    console.log("Device Event from :: " + deviceType + " : " + deviceId + " of event " + eventType + " with payload : " + payload);
    // Is the event a discovery request?
    if (isDiscoveryRequest(deviceType, deviceId, payload)) {
        // Send a discovery message in response to the request for one
        sendDiscoveryMessage(deviceType, deviceId)
    }
    // Is the event
});

// Create the Device Client
var deviceClient = new client.IotfDevice(deviceConfig);
console.log("Connecting device client...");
deviceClient.connect();

/**
 * When a connect message is received, we're good to go
 */
deviceClient.on("connect", function() {
    console.log("Device client, connected.");
});

/**
 * When an error is received, process it
 */
deviceClient.on("error", function(err) {
    console.log("Error: " + err);
});

/**
 * Investigates the specified deviceType, deviceId, and payload
 * to determine whether or not the payload contains a discovery
 * message.
 * 
 * Return true if so, false otherwise.
 */
function isDiscoveryRequest(deviceType, deviceId, payload) {
    // Sanity check
    if (deviceType == appConfig.deviceType) {
        // Sanity check (yes, another one)
        if (deviceId == appConfig.deviceId) {
            var payloadJson = JSON.parse(payload);
            // Make sure the deviceRequest.deviceId is correct
            if ('d' in payloadJson && 'deviceRequest' in payloadJson.d && payloadJson.d.deviceRequest.deviceId == appConfig.deviceId) {
                // Make sure the action is correct
                if (payloadJson.d.deviceRequest.action == "discovery") {
                    return true;
                }
            }
        }
    }
    return false;
}

/**
 * Investiates the specified deviceType, deviceId, and payload
 * to determine whether or not the payload contains a device event
 * that is understood by the home automation controller
 */
function isDeviceRequest(deviceType, deviceId, payload) {
    // Sanity check
    if (deviceType == appConfig.deviceType) {
        // Sanity check (yes, another one)
        if (deviceId == appConfig.deviceId) {
            // Process the various requests the home automation controller understands
            
        }
    }
    return false;
}

/**
 * Send a discovery message
 */
function sendDiscoveryMessage(deviceType, deviceId) {
    console.log("Sending discovery message...");
    // Publish the response event with the discoveryJson message
    var discoveryJson = require("./config/discoveryMessage.json");
    publishEvent("response", discoveryJson);
}

/**
 * Publish the specified JSON message under the specified eventName
 */
function publishEvent(eventName, jsonMessageToPublish) {
    // Use the Device Client to publish the JSON event
    deviceClient.publish(eventName, "json", jsonMessageToPublish);
}