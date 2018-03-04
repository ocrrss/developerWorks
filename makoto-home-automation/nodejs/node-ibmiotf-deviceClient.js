var client = require("ibmiotf");
var exec = require('child_process').exec;

var appConfig = {
	"org" : process.env.IOT_ORG,
	"id" : process.env.IOT_DEVICE_ID,
	"domain" : "internetofthings.ibmcloud.com",
	"type" : process.env.IOT_DEVICE_TYPE,
	"auth-method" : "token",
	"auth-token" : process.env.IOT_DEVICE_AUTH_TOKEN
};

console.log("Environment: org : " + process.env.IOT_ORG + " : id : " + process.env.IOT_DEVICE_ID + " : domain : " + 
	appConfig.domain + " : type : " + appConfig.type + " : auth-method : " + "token" + 
	" : auth-token : " + process.env.IOT_DEVICE_AUTH_TOKEN);

// Create a new Device API client
var deviceClient = new client.IotfDevice(appConfig);

// Good idea in development. Turn off for prod.
deviceClient.log.setLevel('debug');

console.log("Connecting device to WatsonIoT platform...");
deviceClient.connect();
console.log("(Connect request sent)");

deviceClient.on("connect", function() {
	console.log("This device is now connected.");
	// TODO: Publish Connected event to anyone who is interested
	deviceClient.publish("connected", "json", '{"d" : { "connected" : true } } ' );
});

deviceClient.on("error", function(err) {
	console.log("Error: " + err);
});

deviceClient.on("command", function(commandName, format, payload, topic) {
	console.log("Device received command '" + commandName + "'");
	
	if (commandName === "doit") {
		console.log("doit! doit! doit!");
	}
});

deviceClient.on("deviceEvent", function(deviceType, deviceId, eventType, format, payload) {
	console.log("Device Event from :: " + deviceType + " : deviceId : " + deviceId + " : of event type : " + eventType + " : with payload : " + payload);
	var payloadJson = JSON.parse(payload);
	//if (payloadJson.d.text == "d") {
		if ('d' in payloadJson && payloadJson.d.text == "light:on") {
			var codesendCommand = "/var/www/rfoutlet/codesend " + process.env.RF_PLUG_ON_1 + " 0 " + process.env.RF_PLUG_PULSE_LENGTH;
			console.log("Sending command " + codesendCommand);
			exec(codesendCommand, function (error, stdout, stderr) {
				console.log('stdout: ' + stdout);
				console.log('stderr: ' + stderr);
			});
			var data = {
				'eventType' : eventType,
				'deviceType' : deviceType,
				'deviceId' : deviceId,
				'processedByApplication' : process.env.IOT_AUTH_API_KEY
			};
			var deviceEvent = "processed";
			
			console.log("Connecting to device to publish device command...");
			deviceClient.publishDeviceEvent(process.env.IOT_DEVICE_TYPE, process.env.IOT_DEVICE_ID, deviceEvent, "json", data);
		} else if ('d' in payloadJson && payloadJson.d.text == "light:off") {
			var codesendCommand = "/var/www/rfoutlet/codesend " + process.env.RF_PLUG_OFF_1 + " 0 " + process.env.RF_PLUG_PULSE_LENGTH;
			console.log("Sending command " + codesendCommand);
			exec(codesendCommand, function (error, stdout, stderr) {
				console.log('stdout: ' + stdout);
				console.log('stderr: ' + stderr);
			});
		}
	//}
});
