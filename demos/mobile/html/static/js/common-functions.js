var LANGUAGE_NAME = {
    'ar': 'عربی',
    'en': 'English',
    'fa': 'فارسی'
};
var LANGUAGE_RTL = ['ar', 'fa'];
var workspaceLang = getLang();
var myWorkspace;
var borderStylePropertyName;
var codingLangSelected = 'js';
var myInterpreter = null;
var runner;
var code;
var runButtons;
var serverIndicatorIcons;
var serverIndicatorInterval;
var serverOnIndicatorColor = 'green';
var serverOffIndicatorColor = 'yellow';
var keyEventBlocksOnWorkspace = {};
var deviceArray;


function download(filename, text) {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    element.setAttribute('download', filename);
  
    element.style.display = 'none';
    document.body.appendChild(element);
  
    element.click();
  
    document.body.removeChild(element);
}

function downloadCode(){
    var text = worspaceToBlockText();
    download("blocks.txt", text);
}

window.onload = function(){
    init();
    loadLastWorkspaceBlocks();
    runButtons = document.querySelectorAll('#runBtn');
    serverIndicatorIcons = document.querySelectorAll('#serverIndicator');
    // serverIndicatorInterval = setInterval(checkServerConnected, 5000);
}
window.addEventListener('resize', onresizeFunc, false);

if(workspaceLang === 'ar'){
    document.write('<style type="text/css">\n.ui {\nfont-family: "Helvetica Neue", "Segoe UI", Helvetica, sans-serif !important;\n}\n</style>');
}
document.write('<script src="static/js/jquery.min.js" type="text/javascript"></script>\n');
document.write('<link rel="stylesheet" type="text/css" href="static/css/semantic' + (isRtl()?'.rtl':'') + '.min.css">\n');
document.write('<script src="static/js/semantic.min.js" type="text/javascript"></script>\n');
document.write('<script src="static/js/blockly_compressed.js"></script>\n');
document.write('<script src="static/js/blocks_compressed.js"></script>\n');
document.write('<script src="static/js/javascript_compressed.js"></script>\n');
document.write('<script src="static/js/python_compressed.js"></script>\n');
document.write('<script src="static/js/toolbox_standard.js"></script>\n');
document.write('<script src="static/msg/js/' + workspaceLang + '.js"></script>\n');
document.write('<script src="static/js/acorn_interpreter.js"></script>\n');
document.write('<script src="static/js/block_defs.js"></script>\n');

function onresizeFunc(){
    Blockly.svgResize(myWorkspace);
}

function getStringParamFromUrl(name, defaultValue) {
    var val = location.search.match(new RegExp('[?&]' + name + '=([^&]+)'));
    return val ? decodeURIComponent(val[1].replace(/\+/g, '%20')) : defaultValue;
};

function getLang(){
    var lang = getStringParamFromUrl('lang', '');
    if (LANGUAGE_NAME[lang] === undefined) {
        // Default to English.
        lang = 'fa';
    }
    return lang;
}

function changeLanguage(newLang) {
    var search = window.location.search;
    if (search.length <= 1) {
        search = '?lang=' + newLang;
    } else if (search.match(/[?&]lang=[^&]*/)) {
        search = search.replace(/([?&]lang=)[^&]*/, '$1' + newLang);
    } else {
        search = search.replace(/\?/, '?lang=' + newLang + '&');
    }

    window.location = window.location.protocol + '//' +
        window.location.host + window.location.pathname + search;
};


function initLanguage() {
    // Set the HTML's language and direction.
    var rtl = isRtl();
    document.dir = rtl ? 'rtl' : 'ltr';
    document.head.parentElement.setAttribute('lang', workspaceLang);

    // Inject language strings.
    var els = document.querySelectorAll('.show-code-translate');
    for(var i = 0; i < els.length; ++i){
        els[i].innerHTML = '<i class="code icon"></i>' + Blockly.Msg['SHOW_CODE'];
    }
    els = document.querySelectorAll('.run-button-translate');
    for(var i = 0; i < els.length; ++i){
        els[i].innerHTML = '<i class="green play icon"></i>' + Blockly.Msg['RUN'];
    }
    els = document.querySelectorAll('.stop-button-translate');
   for(var i = 0; i < els.length; ++i){
        els[i].innerHTML = '<i class="red stop icon"></i>' + Blockly.Msg['STOP'];
    }
    els = document.querySelectorAll('.bluetooth-button-translate');
    for(var i = 0; i < els.length; ++i){
        els[i].innerHTML = '<i class="blue bluetooth icon"></i>' + Blockly.Msg['BLUETOOTH_CONNECTION'];
    }
    els = document.querySelectorAll('.bluetooth-select-translate');
    for(var i = 0; i < els.length; ++i){
        els[i].textContent = Blockly.Msg['BLUETOOTH_SELECT_DEVICE'];
    }
    els = document.querySelectorAll('.js-item-translate');
    for(var i = 0; i < els.length; ++i){
        els[i].innerHTML = '<i class="js icon"></i>' + Blockly.Msg['JAVASCRIPT'];
    }
    els = document.querySelectorAll('.python-item-translate');
    for(var i = 0; i < els.length; ++i){
        els[i].innerHTML = '<i class="python icon"></i>' + Blockly.Msg['PYTHON'];
    }
    els = document.querySelectorAll('.lang-button-translate');
    for(var i = 0; i < els.length; ++i){
        els[i].textContent = LANGUAGE_NAME[getLang()];
    }
};

function isRtl() {
    return LANGUAGE_RTL.indexOf(workspaceLang) != -1;
};

function addPopupToDisabledBlocks(){
    $('g .blocklyDisabled.blocklyDraggable').each(function(index) {
        $(this).popup({
          content  : Blockly.Msg['ROOBIN_YOU_SHOULD_BUY_THESE_BLOCKS']
        });
    });
}

function init() {
    document.title = Blockly.Msg['ROOBIN_CATEGORY'];
    
	$('.ui.dropdown').dropdown();

	$('.combo.dropdown').dropdown({
		action: 'combo'
    });

    // enable all popups for mobile div top menu
    $('.show-code-popup').popup({
        position   : 'bottom center',
        content : Blockly.Msg['SHOW_CODE']
    });
    $('.run-code-popup').popup({
        position   : 'bottom center',
        content : Blockly.Msg['RUN']
    });
    $('.stop-code-popup').popup({
        position   : 'bottom center',
        content : Blockly.Msg['STOP']
    });
    $('.bluetooth-popup').popup({
        position   : 'bottom center',
        content : Blockly.Msg['BLUETOOTH_CONNECTION']
    });
    $('.code-lang-popup').popup({
        position   : 'bottom center',
        content : Blockly.Msg['SELECT_LANGUAGE']
    });
    $('.download-popup').popup({
        position   : 'bottom right',
        content : Blockly.Msg['ROOBIN_DOWNLOAD']
    });
    $('.upload-popup').popup({
        position   : 'bottom right',
        content : Blockly.Msg['ROOBIN_UPLOAD']
    });
    

    // popup for download and upload btns
    $('#downloadBtn').popup({
        position   : 'bottom right',
        content : Blockly.Msg['ROOBIN_DOWNLOAD']
    });

    $('#uploadBtn').popup({
        position   : 'bottom right',
        content : Blockly.Msg['ROOBIN_UPLOAD']
    });

    addPopupToDisabledBlocks();

    // upload button click handler
    $('#uploadBtn').click(function() {
        $(this).parent().find("input:file").click();
    });
      
    // upload button action handler
    $('input:file', '.ui.action.input').on('change', function(e) {
        var file = e.target.files[0];
        if(file && file.type.match('text')){
            var reader = new FileReader();
            reader.onload = (function(theFile) {
                return function(e) {
                    var text = e.target.result;
                    blockTextToWorkspace(text);
                };
            })(file);
            reader.readAsText(file);
        }
    });

    

    initLanguage();

	setLanguageRelatedProps(workspaceLang);
    myWorkspace = loadWorkspace(workspaceLang);
    
    // UI part
	convertCategoriesTosemantic();

   // addEventsToBluetoothButton();

    // Listen to events on primary workspace.
	myWorkspace.addChangeListener(blocksEventListener);
	document.addEventListener('keydown', keyPressedBlocksEventListener);
    
    Blockly.svgResize(myWorkspace);
    
};

function blocksEventListener(event) {
    saveLastWorkspaceBlocks();
    if(event instanceof Blockly.Events.Ui && event.element === 'category' && event.newValue === 'Roobin'){
        addPopupToDisabledBlocks();
    }
    if (event instanceof Blockly.Events.Ui &&  event.element === 'click') {
        var block_clicked = myWorkspace.getBlockById(event.blockId);
        // This line initializes variable_db_  
        Blockly.JavaScript.workspaceToCode(Blockly.getMainWorkspace());
        try{
            var code_to_run = Blockly.JavaScript.blockToCode(block_clicked);
            if(typeof code_to_run !== 'string'){
                code_to_run = code_to_run[0];
            }
            runCode(code_to_run);
        }catch(e){
            //Do sth
        }
    }
    else if(event instanceof Blockly.Events.BlockCreate){
        var block_created_id = event.blockId;
        var block_created = myWorkspace.getBlockById(block_created_id);
        if(block_created && (block_created.type === 'roobin_keyBoard_event')){
            keyEventBlocksOnWorkspace[block_created_id] = block_created;
        }
        // new blocks added to workspace
    }
    else if(event instanceof Blockly.Events.BlockDelete){
        var block_deleted_id = event.blockId;
        // some blocks were deleted from workspace
    }
}

function worspaceToBlockText(){
    var xml = Blockly.Xml.workspaceToDom(myWorkspace);
    var text = Blockly.Xml.domToText(xml);
    return text;
}

function saveLastWorkspaceBlocks(text){
    var text = worspaceToBlockText();
    if (window.sessionStorage) {
        window.localStorage.lastWorkspaceBlocks = text;
    }
}

function loadLastWorkspaceBlocks(){
    try {
        var lastWorkspaceBlocks = window.localStorage.lastWorkspaceBlocks;
    } catch(e) {
        var lastWorkspaceBlocks = null;
    }
    if (lastWorkspaceBlocks) {
        // Language switching stores the blocks during the reload.
        delete window.localStorage.lastWorkspaceBlocks;
        blockTextToWorkspace(lastWorkspaceBlocks);
    }
}

function blockTextToWorkspace(text){
    if(arguments.length < 1){
        return;
    }
    var xml = Blockly.Xml.textToDom(text);
    Blockly.Xml.domToWorkspace(xml, myWorkspace);  
}

function keyPressedBlocksEventListener(e){
    var keyCodeRecieved = e.code;
    for(blockId in keyEventBlocksOnWorkspace){
        var keyEventBlock = keyEventBlocksOnWorkspace[blockId];
        var keyCodeSelected = keyEventBlock.getFieldValue('SEL_ROOBIN_KEY_PRESSED');
        if(keyCodeRecieved === keyCodeSelected){
            var code_to_run = Blockly.JavaScript.blockToCode(keyEventBlock);
            code_to_run = 'var keyPressed = \'' + e.code + '\';\n' + code_to_run;
            runCode(code_to_run);
        }
    }
}

function setLanguageRelatedProps(){
    if(workspaceLang === 'fa' || workspaceLang === 'ar'){
		borderStylePropertyName = 'border-right-color';
	}else if(workspaceLang === 'en'){
		borderStylePropertyName = 'border-left-color';
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

function createWorker(){
    try{
        var blob;
        var blobCode = Array.prototype.map.call(document.querySelectorAll('script[type=\'text\/js-worker\']'), 
            function (oScript) { return oScript.textContent; });
        try {
            blob = new Blob(blobCode, {type: 'text/javascript'});
        } catch (e) {
            var blobBuilder = new (window.BlobBuilder || window.WebKitBlobBuilder || window.MozBlobBuilder)();
            for(var i = 0; i < blobCode.length; ++i){
                blobBuilder.append(blobCode[i])
            }
            blob = blobBuilder.getBlob('text/javascript');
        }
        var windowUrl = window.URL || window.webkitURL;
        var blobUrl = windowUrl.createObjectURL(blob);
        document.worker = new Worker(blobUrl);
    }catch (e2) {
        // can do nothing more
    }
}

function runCode(code){
    if(arguments.length < 1){
        code = Blockly.JavaScript.workspaceToCode(myWorkspace);
    }
    if(!code){
        resetInterpreter();
        return;
    }
    if(window.Worker === undefined){
        return;
    }
    for(var i = 0; i < runButtons.length; ++i){
        if(runButtons[i].classList.contains('disabled'))
            return;
    }
    resetInterpreter();
    for(var i = 0; i < runButtons.length; ++i){
        runButtons[i].classList.add('disabled');
    }
    Blockly.JavaScript.addReservedWords('code,timeouts,checkTimeout,exit');

    createWorker();

    if(!document.worker){
        // worker could not be initialized
        Blockly.JavaScript.INFINITE_LOOP_TRAP = 'checkTimeout();\n';
        var timeouts = 0;
        var checkTimeout = function() {
            if (timeouts++ > 1000000) {
                throw Blockly.Msg['TIMEOUT'];
            }
        };
        Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
        try {
            eval(code);
        } catch (e) {
            alert(Blockly.Msg['BAD_CODE'].replace('%1', e));
        }
        resetInterpreter();
        return;
    }

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
    var url =  window.location.protocol + '//' +
        window.location.host + window.location.pathname;
    var index = url.indexOf('index.html');;
    if (index != -1) {
        url = url.substring(0, index - 1);
    }
    // this is really odd but without this line english page url is not correct!!!
    if(!isAndroidUserAgent()){
        url = url + '/';
    }
    document.worker.postMessage({url:url});
    document.worker.postMessage({code:code});
}

function resetInterpreter(){
    
    if(runButtons){
        for(var i = 0; i < runButtons.length; ++i){
            runButtons[i].classList.remove('disabled');
        }
    }
    if(document.worker){
        document.worker.terminate();
        document.worker = undefined;
    }
}

function stopCode(){
    resetInterpreter();
    if(isAndroidUserAgent()){
        Android.stopTts();
    }
}

function changeServerIndicatorColor(){
    for(var i = 0; i < serverIndicatorIcons.length; ++i){
        if(serverIndicatorIcons[i].classList.contains(serverOnIndicatorColor)){
            serverIndicatorIcons[i].classList.remove(serverOnIndicatorColor);
            serverIndicatorIcons[i].classList.add(serverOffIndicatorColor);
        }else if(serverIndicatorIcons[i].classList.contains(serverOffIndicatorColor)){
            serverIndicatorIcons[i].classList.remove(serverOffIndicatorColor);
            serverIndicatorIcons[i].classList.add(serverOnIndicatorColor);
        }
    }   
}

function checkServerConnected(){
    href = 'http://localhost:1234';
    req = new XMLHttpRequest();
    req.open('GET', href, true);
    
    req.onreadystatechange = function() {
        if(req.readyState == 4 && req.status == 200){
            clearInterval(serverIndicatorInterval);
            changeServerIndicatorColor();
        }
    };
    req.send(null);
}

function requestServer(query){
    var href = 'http://localhost:1234';
    if(!navigator.userAgent.match(/Android/i)){
        var url = href + '/' + query;
        console.log(url);
        req = new XMLHttpRequest();
        req.open('GET', url, true);
        req.send(null);
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
        media: 'static/media/',
        // media : 'https://blockly-demo.appspot.com/static/media/', 
        rtl: isRtl(),
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
            if(!bluetoothDeviceSelected || bluetoothDeviceSelected === Blockly.Msg['BLUETOOTH_SELECT_DEVICE']){
				return;
            }
            if(isAndroidUserAgent()){
                Android.connectBluetooth(bluetoothDeviceSelected);
            }
		}
	});

	document.querySelector('.select-bluetooth-device').addEventListener('click', populateBluetoothDevicesForm);
}

function populateBluetoothDevicesForm(){
    if(!isAndroidUserAgent())
        return;
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
                onresizeFunc();
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
            colourTertiary: "#BB6E00"
        },
        roobin_motor_blocks: {
            colourPrimary: "#00c9b0",
            colourSecondary: "#FF8000",
            colourTertiary: "#007d55"
        },
        roobin_setup_blocks: {
            colourPrimary: "#93195b",
            colourSecondary: "#FF8000",
            colourTertiary: "#BB6E00"
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