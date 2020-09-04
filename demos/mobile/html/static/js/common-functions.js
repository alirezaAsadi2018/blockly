var workspaceLang;
var borderStylePropertyName;
var codingLangSelected = 'js';
// Exit is used to signal the end of a script.
var myInterpreter = null;
var runner;
var code;
var runButton;

window.onload = function(){
    runButton = document.getElementById('runButton');
}

function setLanguageRelatedProps(lang){
    workspaceLang = lang;
    if(workspaceLang === 'fa' || workspaceLang === 'ar'){
		borderStylePropertyName = 'border-right-color';
	}else if(workspaceLang === 'en'){
		borderStylePropertyName = 'border-left-color';
	}
}

function changeWorkspaceLang(lang){
    if(workspaceLang === lang){
        return;
    }else{
        workspaceLang = lang;
        changeHtmlPage();
    }
}

function setCodingLang(lang){
    codingLangSelected = lang;
}

function showCode(){
    if(codingLangSelected === 'js'){
        showJs();
    }else if(codingLangSelected === 'py'){
        showPython();
    }
}

function showPython() {
    // Generate JavaScript code and display it.
    Blockly.Python.INFINITE_LOOP_TRAP = null;
    var code = Blockly.Python.workspaceToCode(myWorkspace);
    alert(code);
}

function showJs() {
    // Generate JavaScript code and display it.
    Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
    var code = Blockly.JavaScript.workspaceToCode(myWorkspace);
    alert(code);
}

function rotate(msg){
    mqttSend(msg);
}

function initApi(interpreter, globalObject) {
    // Add an API function for the alert() block.
    var wrapper = function(text) {
      return alert(arguments.length ? text : '');
    };
    interpreter.setProperty(globalObject, 'alert',
        interpreter.createNativeFunction(wrapper));
  
    // Add an API function for the prompt() block.
    wrapper = function(text) {
      return prompt(text);
    };
    interpreter.setProperty(globalObject, 'prompt',
        interpreter.createNativeFunction(wrapper));
}

function runCode() {
    Blockly.JavaScript.addReservedWords('exit');
    code = Blockly.JavaScript.workspaceToCode(myWorkspace);
    if(!code){
        resetInterpreter();
        return;
    }
    if(window.Worker === undefined){
         return;
    }
    runButton.disabled = 'disabled';
    var blob = new Blob(Array.prototype.map.call(document.querySelectorAll('script[type=\'text\/js-worker\']'), 
		function (oScript) { return oScript.textContent; }),{type: 'text/javascript'});
    document.worker = new Worker(window.URL.createObjectURL(blob));
    document.worker.onmessage = function(oEvent) {
		if(oEvent.data === 'fin'){
			resetInterpreter();
		}else if(oEvent.data.indexOf('alert') !== -1){
            eval(oEvent.data);
        }else if(oEvent.data.indexOf('prompt') !== -1){
            eval(oEvent.data);
        }else if(oEvent.data.indexOf('callStt') !== -1){
            eval(oEvent.data);
        }else if(oEvent.data.indexOf('callTts') !== -1){
            eval(oEvent.data);
        }else if(oEvent.data.indexOf('rotate') !== -1){
            eval(oEvent.data);
        }else if(oEvent.data.indexOf('requestServer') !== -1){
            eval(oEvent.data);
        }else if(oEvent.data.indexOf('error:') !== -1){
            alert(oEvent.data.substr(oEvent.data.indexOf('error:') + 'error:'.length));
            resetInterpreter();
        }
    };
    var url = document.location.href;
    var index = -1;
    if(workspaceLang === 'en'){
        index = url.indexOf('webview.html');
    }else if(workspaceLang === 'fa'){
        index = url.indexOf('webview-fa.html');
    }else if(workspaceLang === 'ar'){
        index = url.indexOf('webview-ar.html');
    }
    if (index != -1) {
        url = url.substring(0, index);
    }
    if(url[url.length - 1] === '/'){
        url = url.substring(0, url.length - 1);
    }
    // this is really odd but without this line english page url is not correct!!!
    if((workspaceLang === 'en' && !isAndroidUserAgent())){
        url = url + '/';
    }
    document.worker.postMessage({url:url});
    document.worker.postMessage({code:code});
}

function resetInterpreter(){
    if(runButton){
        runButton.disabled = '';
    }
    if(document.worker){
        document.worker.terminate();
    }
}

function stopCode(){
    resetInterpreter();
    if(isAndroidUserAgent()){
        Android.stopTts();
    }
}

function requestServer(query){
    var serverUrl = 'http://localhost:1234';
    // server is only accessible from browser for now!!
    if(!navigator.userAgent.match(/Android/i)){
        // sendIfServerConnected(serverUrl, query);
        fetch('http://localhost:1234')
        .then(function(data){if(data.status === 200) serverFunc();})
        .catch(function(error){alert('server not connected!')});
        var serverFunc = function(){
            var url = 'http://localhost:1234/' + query;
            fetch(url)
            .then(function(data){})
            .catch(function(error){});
        }
    }
}

function callTts(text){
    var busy = false;
    if(!isAndroidUserAgent()){
        alert('not supported!');
        return;
    }
    Android.tts(text, workspaceLang);
}

function callStt(){
    if(!isAndroidUserAgent()){
        alert("Sorry!! Stt is available only on Android!!");
        return;
    }
    var string = "";
    try{
        string = Android.stt(workspaceLang);
    }catch(e){
        alert(e);
    }
    return string;
}

function changeHtmlPage(){
    if(workspaceLang === 'en'){
        redirectToUri('webview.html');
    }else if(workspaceLang === 'fa'){
        redirectToUri('webview-fa.html');
    }else if(workspaceLang === 'ar'){
        redirectToUri('webview-ar.html');
    }
}

function redirectToUri(uri){
    if(isAndroidUserAgent()){
        Android.loadWebview(uri);
        // document.location=uri;
    }else
        window.location.replace(uri);
}

function isAndroidUserAgent(){
    return navigator.userAgent.match(/Android/i);
}

function callAndroidStt(btn) {
    string = "default string";
    string = Android.stt();
    alert(string);
}

function loadWorkspace(){
    defineRoobinTheme();
    // when language is switched between fa(persian) and en(english), another toolbox with different category names is loaded,
    // rtl is also turned on when language is fa and off when it is en, the rest is the same.
    var myWorkspace = Blockly.inject('blocklyDiv', {
        toolbox: BLOCKLY_TOOLBOX_XML['standard'],
        collapse : true, 
        comments : true, 
        disable : true, 
        maxBlocks : Infinity, 
        trashcan : true,
        horizontalLayout : false, 
        toolboxPosition : 'start', 
        css : true, 
        rtl : true, 
        scrollbars : true, 
        sounds : true, 
        oneBasedIndex : true, 
        grid : {
            spacing : 20, 
            length : 1, 
            colour : '#888', 
            snap : false
        },
        theme: Blockly.Themes.Roobin_Theme,
        renderer: 'zelos',
        media: 'media/',
        // media : 'https://blockly-demo.appspot.com/static/media/', 
        rtl: (workspaceLang === 'fa' || workspaceLang === 'ar'),
        zoom : {
            controls : true, 
            wheel : true, 
            startScale : 1, 
            maxScale : 3, 
            minScale : 0.3, 
            scaleSpeed : 1.2
        }
    });
    return myWorkspace;
}

function addEventsToBluetoothButton(){
    document.querySelector('.connect-bluetooth-device').addEventListener('click', function(evnet){
		if(!deviceArray || deviceArray.length === 0){
			populateBluetoothDevicesForm();
		}else{
			bluetoothDeviceSelected = $(".select-bluetooth-device").dropdown('get value');
            if(!bluetoothDeviceSelected || bluetoothDeviceSelected === 'select a device to connect'
                || bluetoothDeviceSelected === 'یک دستگاه را برای اتصال انتخاب کنید'){
				return;
			}
			Android.connectBluetooth(bluetoothDeviceSelected);
		}
	});

	document.querySelector('.select-bluetooth-device').addEventListener('click', populateBluetoothDevicesForm);
}

function populateBluetoothDevicesForm(){
    deviceArray = Android.list().split(',');
    $.each(deviceArray, function (i, item) {
        item = item.trim();
        item = item.split(" ").join("-").split('_').join("-");
        if (!$('.bluetooth-drop .' + item).length) {
            $('.bluetooth-drop').append($('<div>', {
                value: i.toString(),
                text: item.toString(),
                class: 'item ' + item
            }));
        }
    });
}

function convertCategoriesTosemantic(){
    var observer = new MutationObserver(function(mutations) {
		mutations.forEach(function(mutation) {
			adjustFlyoutPostion(false, workspaceLang);
			if (mutation.type == "attributes") {
				var target = mutation.target;
				var el = target.querySelector('.blocklyTreeRow');
				var borderColor = window.getComputedStyle(el).getPropertyValue(borderStylePropertyName);
				addClassToElement(el, 'item');
				if(target.attributes["aria-selected"].value === "false"){ // if it is white
					el.style.color = borderColor;
				}else{
					el.style.color = 'white';
				}
			}
		});
	});

    // add class to the outer div to be the labeled menu
	// var el = document.querySelector('#blockly-1').parentNode;
    // addClassToElement(el, 'ui vertical inverted labeled icon fluid menu');
    var el = document.querySelector('#blockly-0');
    addClassToElement(el, 'ui vertical inverted labeled icon fluid menu');

	el = document.querySelector('.blocklyToolboxDiv');
	addClassToElement(el, 'ui inverted menu');

	// add class to the treeRoot to merge the outer tree and its elements
	el = document.querySelector('.blocklyTreeRoot');
    addClassToElement(el, 'ui inverted item menu');
    
    // logic is the first item in the menu
	convertCategoryToSemantic('#blockly-1', Blockly.Msg['LOGIC_CATEGORY'], 'random icon', observer);
	// loops is the second item in the menu
	convertCategoryToSemantic('#blockly-2', Blockly.Msg['LOOPS_CATEGORY'], 'redo alternate icon', observer);
	// math is the third item in the menu
	convertCategoryToSemantic('#blockly-3', Blockly.Msg['MATH_CATEGORY'], 'calculator icon', observer);
	// text is the forth item in the menu
	convertCategoryToSemantic('#blockly-4', Blockly.Msg['TEXT_CATEGORY'], 'text width icon', observer);
	// lists is the fifth item in the menu
	convertCategoryToSemantic('#blockly-5', Blockly.Msg['LISTS_CATEGORY'], 'list ol icon', observer);
	// colour is the sixth item in the menu
	convertCategoryToSemantic('#blockly-6', Blockly.Msg['COLOUR_CATEGORY'], 'palette icon', observer);
	// variables is the eighth item in the menu
	convertCategoryToSemantic('#blockly-8', Blockly.Msg['VARIABLES_CATEGORY'], 'buffer icon', observer);
	// functions is the ninth item in the menu
	convertCategoryToSemantic('#blockly-9', Blockly.Msg['FUNCTIONS_CATEGORY'], 'percentage icon', observer);
	// roobin is the tenth item in the menu
	convertCategoryToSemantic('#blockly-a', Blockly.Msg['ROOBIN_CATEGORY'], 'robot icon', observer);
}

function convertCategoryToSemantic(id, categoryName, iconName, observer){
    var attrs = {};
    $.each($(id).find('.blocklyTreeRow')[0].attributes, function(idx, attr) {
        attrs[attr.nodeName] = attr.value;
    });
    $(id).find('.blocklyTreeRow').replaceWith(function () {
        return $("<a />", attrs).append($(this).contents());
    });

    var el = document.querySelector(id).querySelector('.blocklyTreeRow');
    var borderColor = window.getComputedStyle(el).getPropertyValue(borderStylePropertyName);

    $(id).find('.blocklyTreeRow').addClass('item').append(categoryName).css('padding', '0px').css('color', borderColor)
    .find('.blocklyTreeLabel').replaceWith('<i class="blocklyTreeLabel ' + iconName + '"></i>');
    
    observer.observe(document.querySelector(id), {
        attributes: true //configure it to listen to attribute changes
    });
}

function addClassToElement(el, classString){
    var arr = el.className.split(" ");
    if (arr.indexOf(classString) == -1) {
        el.className += " " + classString;
    }
}

function adjustFlyoutPostion(waitForDomLoad){
    var windowWidth = $(window).width();
    var flyoutEl = document.querySelectorAll('.blocklyFlyout')[1];
    var toolboxEl = document.querySelector('.blocklyToolboxDiv.blocklyNonSelectable');
    var toolboxWidth = +window.getComputedStyle(toolboxEl).width.slice(0,-2);
    var blocklyFlyoutWidth = +window.getComputedStyle(flyoutEl).width.slice(0,-2);
    var blocklyFlyoutTransformMatrix = window.getComputedStyle(flyoutEl).transform;
    if(blocklyFlyoutTransformMatrix === 'none')
        return;
    var blocklyFlyoutTransformArray = blocklyFlyoutTransformMatrix.split(',');
    var blocklyFlyoutTranslateX = +blocklyFlyoutTransformArray[4];
    var blocklyFlyoutTranslateY = +blocklyFlyoutTransformArray[5].slice(0,-1);
    var blocklyFlyoutNewTranslateX;
    var blocklyFlyoutScrollbarOffset;
    var blocklyFlyoutScrollbarTranslateX;
    if(workspaceLang === 'fa' || workspaceLang === 'ar'){
        blocklyFlyoutNewTranslateX = windowWidth - toolboxWidth - blocklyFlyoutWidth;
        blocklyFlyoutScrollbarTranslateX = blocklyFlyoutNewTranslateX;
    }else if(workspaceLang === 'en'){
        blocklyFlyoutNewTranslateX = blocklyFlyoutTranslateX;
        blocklyFlyoutScrollbarOffset = -15;
        blocklyFlyoutScrollbarTranslateX = blocklyFlyoutTranslateX + blocklyFlyoutWidth + blocklyFlyoutScrollbarOffset;
    }
    var blocklyFlyoutScrollbarTranslateY = 2.5;
    var scrollbarEl = document.querySelectorAll('.blocklyScrollbarVertical.blocklyFlyoutScrollbar')[1];

    if(waitForDomLoad){
        $(document).ready(function() {
            flyoutEl.style.transform = 'translate(' + blocklyFlyoutNewTranslateX + 'px, ' + blocklyFlyoutTranslateY + 'px)';
            scrollbarEl.style.transform = 'translate(' + (blocklyFlyoutScrollbarTranslateX) + 'px, '
                + blocklyFlyoutScrollbarTranslateY + 'px)';
        });
    }else{
        flyoutEl.style.transform = 'translate(' + blocklyFlyoutNewTranslateX + 'px, ' + blocklyFlyoutTranslateY + 'px)';
        scrollbarEl.style.transform = 'translate(' + (blocklyFlyoutScrollbarTranslateX) + 'px, '
            + blocklyFlyoutScrollbarTranslateY + 'px)';
    }
}

function defineRoobinTheme(){
    Blockly.Themes.Roobin_Theme={};
    Blockly.Themes.Roobin_Theme.categoryStyles = {              
        colour_category: {
            colour: "#CF63CF"
        },
        list_category: {
            colour: "#9966FF"
        },
        logic_category: {
            colour: "#4C97FF"
        },
        loop_category: {
            colour: "#0fBD8C"
        },
        math_category: {
            colour: "#59C059"
        },
        procedure_category: {
            colour: "#FF6680"
        },
        text_category: {
            colour: "#FFBF00"
        },
        variable_category: {
            colour: "#DC143C"
        },
        function_category: {
            colour: "FF6680"
        },
        roobin_category: {
            colour: "#F2711C"
        },
    };
    Blockly.Themes.Roobin_Theme.defaultBlockStyles = {
        colour_blocks: {
            colourPrimary: "#CF63CF",
            colourSecondary: "#C94FC9",
            colourTertiary: "#BD42BD"
        },
        list_blocks: {
        colourPrimary: "#9966FF",
        colourSecondary: "#855CD6",
        colourTertiary: "#774DCB"
        },
        logic_blocks: {
            colourPrimary: "#4C97FF",
            colourSecondary: "#4280D7",
            colourTertiary: "#3373CC"
        },
        loop_blocks: {
            colourPrimary: "#0fBD8C",
            colourSecondary: "#0DA57A",
            colourTertiary: "#0B8E69"
        },
        math_blocks: {
            colourPrimary: "#59C059",
            colourSecondary: "#46B946",
            colourTertiary: "#389438"
        },
        procedure_blocks: {
            colourPrimary: "#FF6680",
            colourSecondary: "#FF4D6A",
            colourTertiary: "#FF3355"
        },
        text_blocks: {
            colourPrimary: "#FFBF00",
            colourSecondary: "#E6AC00",
            colourTertiary: "#CC9900"
        },
        variable_blocks: {
            colourPrimary: "#DC143C",
            colourSecondary: "#FF8000",
            colourTertiary: "#DB6E00"
        },
        roobin_blocks: {
            colourPrimary: "#F2711C",
            colourSecondary: "#FF8000",
            colourTertiary: "#DB6E00"
        },
        roobin_motor_blocks: {
            colourPrimary: "#008080",
            colourSecondary: "#FF8000",
            colourTertiary: "#008080"
        },
        roobin_setup_blocks: {
            colourPrimary: "#808080",
            colourSecondary: "#FF8000",
            colourTertiary: "#808080"
        },
        function_blocks: {
            colourPrimary: "#FF6680",
            colourSecondary: "#FF8000",
            colourTertiary: "#DB6E00"
        },
        hat_blocks: {
            colourPrimary: "#4C97FF",
            colourSecondary: "#4280D7",
            colourTertiary: "#3373CC",
            hat: "cap"
        }
    };
    Blockly.Themes.Roobin_Theme = new Blockly.Theme("Roobin_Theme",  
        Blockly.Themes.Roobin_Theme.defaultBlockStyles,
        Blockly.Themes.Roobin_Theme.categoryStyles);

    if(workspaceLang === 'ar'){
        Blockly.Themes.Roobin_Theme.setFontStyle ({
            'family': "Helvetica Neue, Segoe UI, Helvetica, sans-serif",
            'weight': 'normal', // Use default font-weight
            'size': 12
        });
    }else if(workspaceLang === 'fa' || workspaceLang === 'en'){
        Blockly.Themes.Roobin_Theme.setFontStyle ({
            'family': "Yekan, Helvetica Neue, Segoe UI, Helvetica, sans-serif",
            'weight': 'normal', // Use default font-weight
            'size': 12
        });
    }
}