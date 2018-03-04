var client = require("ibmiotf");
var exec = require('child_process').exec;

var appAppConfig = {
	"org" : process.env.IOT_ORG,
	"id" : "homeautomation",
	"domain" : "internetofthings.ibmcloud.com",
	"auth-key" : process.env.IOT_API_KEY,
	"auth-token" : process.env.IOT_API_AUTH_TOKEN
};

console.log("Environment: org : " + process.env.IOT_ORG + " : device id : " + process.env.IOT_DEVICE_ID + " : domain : " + 
	appAppConfig.domain + " : device type : " + process.env.IOT_DEVICE_TYPE + " : auth-key : " + process.env.IOT_API_KEY + 
	" : auth-token : " + process.env.IOT_API_AUTH_TOKEN);

var appClient = new client.IotfApplication(appAppConfig);

console.log("Sending connect request to server");
appClient.connect();
console.log("Connect request sent to server");

appClient.on("connect", function() {
	console.log("The app client is now connected!!");
	appClient.subscribeToDeviceEvents();
	appClient.subscribeToDeviceCommands();
});

appClient.on("error", function(err) {
	console.log("Error: " + err);
});

appClient.on("deviceEvent", function(deviceType, deviceId, eventType, format, payload) {
	console.log("Device Event from :: " + deviceType + " : deviceId : " + deviceId + " : of event type : " + eventType + " : with payload : " + payload);
	appClient.publishDeviceCommand(deviceType, deviceId, eventType, format, payload);
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
			appClient.publishDeviceEvent(process.env.IOT_DEVICE_TYPE, process.env.IOT_DEVICE_ID, deviceEvent, "json", data);
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

appClient.on("command", function(commandName, format, payload, topic) {
	console.log("Device received command '" + commandName + "'");
	
	if (commandName === "doit") {
		console.log("doit! doit! doit!");
	}
});

var client = require("ibmiotf");
var exec = require('child_process').exec;

var deviceAppConfig = {
	"org" : process.env.IOT_ORG,
	"id" : process.env.IOT_DEVICE_ID,
	"domain" : "internetofthings.ibmcloud.com",
	"type" : process.env.IOT_DEVICE_TYPE,
	"auth-method" : "token",
	"auth-token" : process.env.IOT_DEVICE_AUTH_TOKEN
};

console.log("Environment: org : " + process.env.IOT_ORG + " : id : " + process.env.IOT_DEVICE_ID + " : domain : " + 
	deviceAppConfig.domain + " : type : " + deviceAppConfig.type + " : auth-method : " + "token" + 
	" : auth-token : " + process.env.IOT_DEVICE_AUTH_TOKEN);

// Create a new Device API client
var deviceClient = new client.IotfDevice(deviceAppConfig);

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

//deviceClient.on("deviceEvent", function(deviceType, deviceId, eventType, format, payload) {
	//console.log("Device Event from :: " + deviceType + " : deviceId : " + deviceId + " : of event type : " + eventType + " : with payload : " + payload);
	//var payloadJson = JSON.parse(payload);
	////if (payloadJson.d.text == "d") {
		//if ('d' in payloadJson && payloadJson.d.text == "light:on") {
			//var codesendCommand = "/var/www/rfoutlet/codesend " + process.env.RF_PLUG_ON_1 + " 0 " + process.env.RF_PLUG_PULSE_LENGTH;
			//console.log("Sending command " + codesendCommand);
			//exec(codesendCommand, function (error, stdout, stderr) {
				//console.log('stdout: ' + stdout);
				//console.log('stderr: ' + stderr);
			//});
			//var data = {
				//'eventType' : eventType,
				//'deviceType' : deviceType,
				//'deviceId' : deviceId,
				//'processedByApplication' : process.env.IOT_AUTH_API_KEY
			//};
			//var deviceEvent = "processed";
			
			//console.log("Connecting to device to publish device command...");
			//deviceClient.publishDeviceEvent(process.env.IOT_DEVICE_TYPE, process.env.IOT_DEVICE_ID, deviceEvent, "json", data);
		//} else if ('d' in payloadJson && payloadJson.d.text == "light:off") {
			//var codesendCommand = "/var/www/rfoutlet/codesend " + process.env.RF_PLUG_OFF_1 + " 0 " + process.env.RF_PLUG_PULSE_LENGTH;
			//console.log("Sending command " + codesendCommand);
			//exec(codesendCommand, function (error, stdout, stderr) {
				//console.log('stdout: ' + stdout);
				//console.log('stderr: ' + stderr);
			//});
		//}
	////}
//});
