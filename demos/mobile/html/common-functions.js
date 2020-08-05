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

function runJsCode() {
    // Generate JavaScript code and run it.
    window.LoopTrap = 1000;
    Blockly.JavaScript.INFINITE_LOOP_TRAP =
        'if (--window.LoopTrap == 0) throw "Infinite loop.";\n';
    var code = Blockly.JavaScript.workspaceToCode(myWorkspace);
    //debugger
    Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
    
    try {
    eval(code);
    } catch (e) {
    alert(e);
    }
}

function callTts(text){
    Android.tts(text);
}

function callStt(){
    var string = "";
    try{
    string = Android.stt();
    }catch(e){
    alert(e);
    }
    return string;
}

function changeLanguage(workspaceLang){
    if(workspaceLang === 'en'){
        redirectToUri('webview-fa.html');
    }else if(workspaceLang === 'fa'){
        redirectToUri('webview.html');
    }
}

function redirectToUri(uri){
    if(checkOnAndroid()){
        Android.loadWebview(uri);
        // document.location=uri;
    }else
        window.location.replace(uri);
}

function checkOnAndroid(){
    return navigator.userAgent.match(/Android/i);
}

function callAndroidStt(btn) {
    string = "default string";
    string = Android.stt();
    alert(string);
}

function loadWorkspace(workspaceLang){
    // when language is switched between fa(persian) and en(english), another toolbox with different category names is loaded,
    // rtl is also turned on when language is fa and off when it is en, the rest is the same.
    var myWorkspace = Blockly.inject('blocklyDiv', {
        toolbox: BLOCKLY_TOOLBOX_XML['standard_' + workspaceLang],
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
        // theme: new Blockly.Theme('myTheme',{
        // 		'blockStyles': {
        // 			"list_blocks": {
        // 				"colourPrimary": "#4a148c",
        // 				"colourSecondary":"#AD7BE9",
        // 				"colourTertiary":"#CDB6E9"
        // 			},
        // 			"logic_blocks": {
        // 				"colourPrimary": "#01579b",
        // 				"colourSecondary":"#64C7FF",
        // 				"colourTertiary":"#C5EAFF"
        // 			}
        // 		},
        // 		'categoryStyles': {
        // 			"list_category": {
        // 				"colour": "#4a148c"
        // 			},
        // 			"logic_category": {
        // 				"colour": "#01579b",
        // 			}
        // 		},
        // 		'componentStyles': {
        // 			"workspaceBackgroundColour": "#1e1e1e",
        // 			"toolboxBackgroundColour": "#333"
        // 		}
        // 	}
        // ),
        renderer: 'zelos',
        media: 'media/',
        // media : 'https://blockly-demo.appspot.com/static/media/', 
        rtl: (workspaceLang === 'fa'),
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
	var el = document.querySelector('#blockly\\:1').parentNode;
	addClassToElement(el, 'ui vertical inverted labeled icon fluid menu');

	el = document.querySelector('.blocklyToolboxDiv');
	addClassToElement(el, 'ui middle aligned centered grid');

	// add class to the treeRoot to merge the outer tree and its elements
	el = document.querySelector('.blocklyTreeRoot');
    addClassToElement(el, 'ui inverted item menu');
    
    // logic is the first item in the menu
	convertCategoryToSemantic('#blockly\\:1', Blockly.Msg['LOGIC_CATEGORY'], 'random icon', borderStylePropertyName, observer);
	// loops is the second item in the menu
	convertCategoryToSemantic('#blockly\\:2', Blockly.Msg['LOOPS_CATEGORY'], 'redo alternate icon', borderStylePropertyName, observer);
	// math is the third item in the menu
	convertCategoryToSemantic('#blockly\\:3', Blockly.Msg['MATH_CATEGORY'], 'calculator icon', borderStylePropertyName, observer);
	// text is the forth item in the menu
	convertCategoryToSemantic('#blockly\\:4', Blockly.Msg['TEXT_CATEGORY'], 'text width icon', borderStylePropertyName, observer);
	// lists is the fifth item in the menu
	convertCategoryToSemantic('#blockly\\:5', Blockly.Msg['LISTS_CATEGORY'], 'list ol icon', borderStylePropertyName, observer);
	// colour is the sixth item in the menu
	convertCategoryToSemantic('#blockly\\:6', Blockly.Msg['COLOUR_CATEGORY'], 'palette icon', borderStylePropertyName, observer);
	// variables is the eighth item in the menu
	convertCategoryToSemantic('#blockly\\:8', Blockly.Msg['VARIABLES_CATEGORY'], 'buffer icon', borderStylePropertyName, observer);
	// functions is the ninth item in the menu
	convertCategoryToSemantic('#blockly\\:9', Blockly.Msg['FUNCTIONS_CATEGORY'], 'percentage icon', borderStylePropertyName, observer);
	// roobin is the tenth item in the menu
	convertCategoryToSemantic('#blockly\\:a', Blockly.Msg['ROOBIN_CATEGORY'], 'robot icon', borderStylePropertyName, observer);
}

function convertCategoryToSemantic(id, categoryName, iconName, borderStylePropertyName, observer){
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

function adjustFlyoutPostion(waitForDomLoad, workspaceLang){
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
    if(workspaceLang === 'fa'){
        blocklyFlyoutNewTranslateX = windowWidth - toolboxWidth - blocklyFlyoutWidth + 15;
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