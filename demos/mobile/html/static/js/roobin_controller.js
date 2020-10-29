// define constants for motors
HEADNOD = 6
HEADTURN = 1
EYETURN = 2
LIDBLINK = 3
TOPLIP = 4
BOTTOMLIP = 5
EYETILT = 0

// array to hold 
sensors = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]

// define a module level variable for the serial port
port=""
// define library version
version ="2.85"
// flag to stop writing when writing for threading
writing = false
// global to set the params to speech synthesizer which control the voice
voice = ""
// Global flag to set synthesizer, default is festival, espeak also supported.
// If it's not festival then it needs to support -w parameter to write to file e.g. espeak or espeak-NG
synthesizer = "festival"

ser = undefined

// Function to check if a number is a digit including negative numbers
function is_digit(n){
    return Boolean(+n);
}

root = [{"Name":"HeadTurn", "Min":"0", "Max":"1000", "Motor":"1", "Speed":"40", "Reverse":"false", "Acceleration":"60", "RestPosition":"5", "Avoid":""},
        {"Name":"HeadNod", "Min":"140", "Max":"700", "Motor":"0", "Speed":"0", "Reverse":"true", "Acceleration":"60", "RestPosition":"5", "Avoid":""},
        {"Name":"EyeTurn", "Min":"380", "Max":"780", "Motor":"2", "Speed":"0", "Reverse":"false", "Acceleration":"0", "RestPosition":"5", "Avoid":""},
        {"Name":"EyeTilt", "Min":"520", "Max":"920", "Motor":"6", "Speed":"0", "Reverse":"false", "Acceleration":"30", "RestPosition":"5", "Avoid":""},
        {"Name":"TopLip", "Min":"0", "Max":"550", "Motor":"4", "Speed":"0", "Reverse":"true", "Acceleration":"0", "RestPosition":"5", "Avoid":"BottomLip"},
        {"Name":"BottomLip", "Min":"0", "Max":"550", "Motor":"5", "Speed":"0", "Reverse":"true", "Acceleration":"0", "RestPosition":"5", "Avoid":"TopLip"},
        {"Name":"LidBlink", "Min":"35", "Max":"305", "Motor":"3", "Speed":"0", "Reverse":"false", "Acceleration":"0", "RestPosition":"10", "Avoid":""},
        {"Name":"MouthOpen", "Min":"80", "Max":"460", "Motor":"7", "Speed":"0", "Reverse":"false", "Acceleration":"0", "RestPosition":"10", "Avoid":""}]

// Put motor ranges into lists
motorPos = [11,11,11,11,11,11,11,11]
motorMins = [0,0,0,0,0,0,0,0]
motorMaxs = [0,0,0,0,0,0,0,0]
motorRev = [false,false,false,false,false,false,false,false]
restPos = [0,0,0,0,0,0,0,0]
isAttached = [false,false,false,false,false,false,false,false]

function moveSpeechMouth(text){
    for(var i = 0; i < 10; ++i){
        mouthing(1);
        mouthing(1);
        mouthing(1);
    }
    setTimeout(function(){
        mouthing(1);
        mouthing(1);
    }, 500);
}    

function mouthing(ph){
    var msg = "p0"+String(ph)+"\n";
    serwrite(msg);
}

function change_eye(eyes_side_list, eyes_list){
    var eye_state = {
        'اصل':5,
        'دایره ای':4,
        'لوزی':3,
        'مربعی':2,
        'مثلثی': 1,
        'بتمن':'b',
        'کیتی':'k',
        'باب اسفنج':'y',
        'پرنده عصبانی':'a',
        'سالیوان': 's',
        'مینیون': 'm',
    }[eyes_list];

    var eye_side = {
        'راست':2,
        'چپ': 1,
    }[eyes_side_list];

    try{
        change_eye_command(eye_state, eye_side);
    }catch(e){
        //TODO
    }
}

function change_mouth(mouth_list){
    // Changes mouth form
    var mouth_state = {
        'روبین':1,
        'غنچه':2,
    }[mouth_list];

    try{
        change_mouth_command(mouth_state);
    }catch(e){
        //TODO
    }
}

// Changes mouth form
function change_mouth_command(mouth_state){
    msg = "f" + mouth_state.toString() + "," + "\n";
    serwrite(msg);
}



function recovery(){
    try{
        Android.restartBluetooth();
    }catch(e){
        //TODO
    }
}

function introduce(){
    try{
        if(roobinLang === 'en'){
            var text = 'Hello . I am roobin. Your good friend !';
        }else if(roobinLang === 'fa'){
            var text = 'سلام ، من روبین هستم ، دوسته خوبه شما';
        }
        Android.tts(text, roobinLang);
    }catch(e){
        //TODO
    }
}

function say_hello(){
    try{
        if(roobinLang === 'en'){
            var text = 'Hello. I am Roobin. Nice to meet you !';
        }else if(roobinLang === 'fa'){
            var text = 'سلام.من روبین هستم.از آشنایی با شما خوشحالم';
        }
        Android.tts(text);
    }catch(e){
        //TODO
    }
}

function setSpeakingSpeed(speed){
    Android.setTtsSpeakingSpeed(+speed);
}

function setSpeakingPitch(pitch){
    Android.setTtsSpeakingPitch(+pitch);
}

function changeSpeakingPitch(pitchChange){
    Android.changeTtsSpeakingPitch(+pitchChange);
}

// Changes eyes form
function change_eye_command(eye_state, eye_side){
    msg = "z" + eye_state.toString() + eye_side.toString() + "," + "\n";
    serwrite(msg);
}

function move_motor(motor, angle){
    motor = {
        "گردن":1,
        "سر":0
    }[motor];
    try{
        move(+motor, +angle, 10);
    }catch(e){
        //TODO
        //add a nice message 
    }
}

function move(m, pos, spd){
    //Limit values to keep then within range
    var pos = limit(pos);
    var spd = limit(spd);
    var absPos = pos;

    //Reverse the motor if necessary   
    if(motorRev[m])
        pos = 10 - pos;

    // Attach motor       
    attach(m);
    
    // Scale range of speed
    spd = (250/10)*spd;

    // Construct message from values
    msg = "m0" + m.toString() + "," + absPos.toString() + "," + spd.toString();

    // Write message to serial port
    serwrite(msg);

    // Update motor positions list
    motorPos[m] = pos;  
}

//Function to limit values so they are between 0 - 10
function limit(val){
     if(+val > 80)
       return 80;
     else if(+val < 0) 
        return 0;
     else
        return val;
}
 
// Function to attach Roobin's motors. Argument | m (motor) int (0-6)
function attach(m){
    if(!isAttached[m]){
        // Construct message
        msg = "a0" + m.toString();

        // Write message to serial port
        serwrite(msg);

        // Update flag
        isAttached[m] = true;
    }
}

function serwrite(code){
    //wait until previous write is finished
    //waitTillWritingFinished();
    writing = true;
    Android.send(code);
    writing = false;
}