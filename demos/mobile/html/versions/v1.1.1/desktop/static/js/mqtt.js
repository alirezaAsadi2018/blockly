var client = undefined;
var isConnected = false;
mqttConnect(undefined);

// this function is called from outside -> responsible for sending msg's and checking if the client is connected  
function mqttSend(msg){
	if(isConnected){
		sendMessage(msg);
	}else{
		mqttConnect(msg);
	}
}
function mqttConnect(msg){
	const brokerAddress = 'broker.mqtt-dashboard.com';
	const brokerPort = 8000;
	const clientId = "js-loopback";
	client = new Paho.MQTT.Client(brokerAddress, Number(brokerPort), clientId);
	// set callback handlers
	client.onConnectionLost = onMqttConnectionLost;
	client.onMessageArrived = onMqttMessageArrived;
	// connect the client
	const callback = function(){
		onMqttConnect(client);
		sendMessage(msg);
	};
	client.connect({
		cleanSession: true,
		useSSL: false,
		timeout: 3,
		onSuccess: callback,
		onFailure: onMqttFailure
	});
}

function sendMessage(msg){
	// TODO:
	// maybe later initialize msg with a starting string for the server to know that messaging has started
	// but for now it is inited with "undefined" the first time the client is created at program startup
	// and thus no message will be sent!!
	if(msg){
		const messageObj = new Paho.MQTT.Message(msg);
		const topicPath = "EmbeddedProject/OpenDoor";
		messageObj.destinationName = topicPath;
		client.send(messageObj);
	}
}

// called when the client connects
function onMqttConnect(client) {
  // Once a connection has been made, make a subscription.
  isConnected = true;
  console.log("onConnect");
  client.subscribe("loopback/hello");
}

// called when the client loses its connection
function onMqttConnectionLost(responseObject) {
  client = undefined;
  isConnected = false;
  if (responseObject.errorCode !== 0) {
    console.log("onConnectionLost:"+responseObject.errorMessage);
  }
}

function onMqttFailure(e){
	client = undefined;
	isConnected = false;
	console.log("mqtt connection failed with this error: ", JSON.stringify(e));
	if(e.errorCode == 7){
		alert('check your internet connection or disconnect your proxy/vpn');
	}
}

// called when a message arrives
function onMqttMessageArrived(message) {
  console.log("onMessageArrived:"+message.payloadString);
}