var LANGUAGE_NAME={ar:"عربی",en:"English",fa:"فارسی"},LANGUAGE_RTL=["ar","fa"],workspaceLang=getLang(),myWorkspace,borderStylePropertyName,codingLangSelected="js",myInterpreter=null,runner,code,runButtons,serverIndicatorIcons,serverIndicatorInterval,serverOnIndicatorColor="green",serverOffIndicatorColor="yellow",keyEventBlocksOnWorkspace={},deviceArray;function getWorkspace(){return myWorkspace=myWorkspace||Blockly.getMainWorkspace()}function download(e,o){var t=document.createElement("a");t.setAttribute("href","data:text/plain;charset=utf-8,"+encodeURIComponent(o)),t.setAttribute("download",e),t.style.display="none",document.body.appendChild(t),t.click(),document.body.removeChild(t)}function downloadCode(){download("RoobinBlocks.txt",worspaceToBlockText())}function onresizeFunc(){Blockly.svgResize(getWorkspace())}function getStringParamFromUrl(e,o){var t=location.search.match(new RegExp("[?&]"+e+"=([^&]+)"));return t?decodeURIComponent(t[1].replace(/\+/g,"%20")):o}function getLang(){var e=getStringParamFromUrl("lang","");return void 0===LANGUAGE_NAME[e]&&(e="fa"),e}function changeLanguage(e){var o=(o=window.location.search).length<=1?"?lang="+e:o.match(/[?&]lang=[^&]*/)?o.replace(/([?&]lang=)[^&]*/,"$1"+e):o.replace(/\?/,"?lang="+e+"&");window.location=window.location.protocol+"//"+window.location.host+window.location.pathname+o}function initLanguage(){var e=isRtl();document.dir=e?"rtl":"ltr",document.head.parentElement.setAttribute("lang",workspaceLang);for(var o=document.querySelectorAll(".show-code-translate"),t=0;t<o.length;++t)o[t].innerHTML='<i class="code icon"></i>'+Blockly.Msg.SHOW_CODE;o=document.querySelectorAll(".run-button-translate");for(t=0;t<o.length;++t)o[t].innerHTML='<i class="green play icon"></i>'+Blockly.Msg.RUN;o=document.querySelectorAll(".stop-button-translate");for(t=0;t<o.length;++t)o[t].innerHTML='<i class="red stop icon"></i>'+Blockly.Msg.STOP;o=document.querySelectorAll(".bluetooth-button-translate");for(t=0;t<o.length;++t)o[t].innerHTML='<i class="blue bluetooth icon"></i>'+Blockly.Msg.BLUETOOTH_CONNECTION;o=document.querySelectorAll(".bluetooth-select-translate");for(t=0;t<o.length;++t)o[t].textContent=Blockly.Msg.BLUETOOTH_SELECT_DEVICE;o=document.querySelectorAll(".js-item-translate");for(t=0;t<o.length;++t)o[t].innerHTML='<i class="js icon"></i>'+Blockly.Msg.JAVASCRIPT;o=document.querySelectorAll(".python-item-translate");for(t=0;t<o.length;++t)o[t].innerHTML='<i class="python icon"></i>'+Blockly.Msg.PYTHON;o=document.querySelectorAll(".lang-button-translate");for(t=0;t<o.length;++t)o[t].textContent=LANGUAGE_NAME[getLang()]}function isRtl(){return-1!=LANGUAGE_RTL.indexOf(workspaceLang)}function addPopupToDisabledBlocks(){var o=getWorkspace().getTopBlocks().map(function(e){return e.svgGroup_});$("g .blocklyDisabled.blocklyDraggable").each(function(e){o.includes($(this)[0])||($(this).popup({content:Blockly.Msg.ROOBIN_YOU_SHOULD_BUY_THESE_BLOCKS}),$(this).click(function(){openUrl("https://www.roobin.co/product/roobin",!0)}))})}function init(){document.title=Blockly.Msg.ROOBIN_CATEGORY,$(".ui.sidebar").sidebar({context:$(".ui.pushable.segment"),transition:"overlay"}).sidebar("attach events","#mobile_item"),$(".ui.dropdown").dropdown(),$(".combo.dropdown").dropdown({action:"combo"}),$(".server-indicator-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.SERVER_INDICATOR_PENDING})}),$(".show-code-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.SHOW_CODE})}),$(".run-code-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.RUN})}),$(".stop-code-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.STOP})}),$(".bluetooth-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.BLUETOOTH_CONNECTION})}),$(".code-lang-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.SELECT_LANGUAGE})}),$(".download-popup").each(function(e){$(this).popup({position:"bottom right",content:Blockly.Msg.ROOBIN_DOWNLOAD})}),$(".upload-popup").each(function(e){$(this).popup({position:"bottom right",content:Blockly.Msg.ROOBIN_UPLOAD})}),$(".upload-action").each(function(e){$(this).click(function(){$(this).parent().find("input:file").click()})}),$("input:file",".ui.action.input").each(function(e){$(this).on("change",function(e){var o,t=e.target.files[0];t&&t.type.match("text")&&((o=new FileReader).onload=function(e){blockTextToWorkspace(e.target.result)},o.readAsText(t))})}),initLanguage(),setLanguageRelatedProps(workspaceLang),myWorkspace=loadWorkspace(workspaceLang),convertCategoriesTosemantic(),myWorkspace.addChangeListener(blocksEventListener),document.addEventListener("keydown",keyPressedBlocksEventListener),Blockly.svgResize(myWorkspace)}function blockIdToCode(e){try{getWorkspace(),Blockly.JavaScript.init(myWorkspace);var o=myWorkspace.getBlockById(e),t=Blockly.JavaScript.blockToCode(o);Array.isArray(t)&&(t=t[0]),t&&o.outputConnection&&(t=Blockly.JavaScript.scrubNakedValue(t),Blockly.JavaScript.STATEMENT_PREFIX&&!o.suppressPrefixSuffix&&(t=Blockly.JavaScript.injectId(Blockly.JavaScript.STATEMENT_PREFIX,o)+t),Blockly.JavaScript.STATEMENT_SUFFIX&&!o.suppressPrefixSuffix&&(t+=Blockly.JavaScript.injectId(Blockly.JavaScript.STATEMENT_SUFFIX,o)));var r=Blockly.JavaScript.finish(t);return r=(r=(r=r.replace(/^\s+\n/,"")).replace(/\n\s+$/,"\n")).replace(/[ \t]+\n/g,"\n")}catch(e){}}function blocksEventListener(e){var o,t;saveLastWorkspaceBlocks(),e instanceof Blockly.Events.Ui&&"category"===e.element&&"Roobin"===e.newValue&&addPopupToDisabledBlocks(),e instanceof Blockly.Events.Ui&&"click"===e.element?runCode(blockIdToCode(e.blockId)):e instanceof Blockly.Events.BlockCreate?(o=e.blockId,(t=getWorkspace().getBlockById(o))&&"roobin_keyBoard_event"===t.type&&(keyEventBlocksOnWorkspace[o]=t)):e instanceof Blockly.Events.BlockDelete&&e.blockId}function worspaceToBlockText(){var e=Blockly.Xml.workspaceToDom(getWorkspace());return Blockly.Xml.domToText(e)}function saveLastWorkspaceBlocks(e){e=worspaceToBlockText();window.sessionStorage&&(window.localStorage.lastWorkspaceBlocks=e)}function loadLastWorkspaceBlocks(){try{var o=window.localStorage.lastWorkspaceBlocks}catch(e){o=null}o&&(delete window.localStorage.lastWorkspaceBlocks,blockTextToWorkspace(o))}function blockTextToWorkspace(e){var o;getWorkspace().clear(),arguments.length<1||(o=Blockly.Xml.textToDom(e),Blockly.Xml.domToWorkspace(o,getWorkspace()))}function keyPressedBlocksEventListener(e){var o,t=e.code;for(blockId in keyEventBlocksOnWorkspace){t===keyEventBlocksOnWorkspace[blockId].getFieldValue("SEL_ROOBIN_KEY_PRESSED")&&(o=blockIdToCode(blockId),runCode(o="var keyPressed = '"+e.code+"';\n"+o))}}function setLanguageRelatedProps(){"fa"===workspaceLang||"ar"===workspaceLang?borderStylePropertyName="border-right-color":"en"===workspaceLang&&(borderStylePropertyName="border-left-color")}function setCodingLang(e){codingLangSelected=e}function showCode(){"js"===codingLangSelected?showJs():"py"===codingLangSelected&&showPython()}function showPython(){Blockly.Python.INFINITE_LOOP_TRAP=null;var e=Blockly.Python.workspaceToCode(getWorkspace());alert(e)}function showJs(){Blockly.JavaScript.INFINITE_LOOP_TRAP=null;var e=Blockly.JavaScript.workspaceToCode(getWorkspace());alert(e)}function rotate(e){mqttSend(e)}function initApi(e,o){var t=function(e){return alert(arguments.length?e:"")};e.setProperty(o,"alert",e.createNativeFunction(t)),t=function(e){return prompt(e)},e.setProperty(o,"prompt",e.createNativeFunction(t))}function createWorker(){try{var o,t=Array.prototype.map.call(document.querySelectorAll("script[type='text/js-worker']"),function(e){return e.textContent});try{o=new Blob(t,{type:"text/javascript"})}catch(e){for(var r=new(window.BlobBuilder||window.WebKitBlobBuilder||window.MozBlobBuilder),n=0;n<t.length;++n)r.append(t[n]);o=r.getBlob("text/javascript")}var e=(window.URL||window.webkitURL).createObjectURL(o);document.worker=new Worker(e)}catch(e){}}function runCode(code){if(arguments.length<1&&(code=Blockly.JavaScript.workspaceToCode(getWorkspace())),code){if(void 0!==window.Worker){for(var i=0;i<runButtons.length;++i)if(runButtons[i].classList.contains("disabled"))return;resetInterpreter();for(var i=0;i<runButtons.length;++i)runButtons[i].classList.add("disabled");if(Blockly.JavaScript.addReservedWords("code,timeouts,checkTimeout,exit"),createWorker(),document.worker){document.worker.onmessage=function(oEvent){"fin"===oEvent.data?resetInterpreter():-1!==oEvent.data.indexOf("alert")||-1!==oEvent.data.indexOf("prompt")||-1!==oEvent.data.indexOf("callStt")||-1!==oEvent.data.indexOf("callTts")||-1!==oEvent.data.indexOf("rotate")||-1!==oEvent.data.indexOf("requestServer")?eval(oEvent.data):-1!==oEvent.data.indexOf("error:")&&(alert(oEvent.data.substr(oEvent.data.indexOf("error:")+"error:".length)),resetInterpreter())};var url=window.location.protocol+"//"+window.location.host+window.location.pathname,url=url.replace(/\/?\w+\.html\/?/gi,"");isAndroidUserAgent()||(url+="/"),document.worker.postMessage({url:url}),document.worker.postMessage({code:code})}else{Blockly.JavaScript.INFINITE_LOOP_TRAP="checkTimeout();\n";var timeouts=0,checkTimeout=function(){if(1e6<timeouts++)throw Blockly.Msg.TIMEOUT};Blockly.JavaScript.INFINITE_LOOP_TRAP=null;try{eval(code)}catch(e){alert(Blockly.Msg.BAD_CODE.replace("%1",e))}resetInterpreter()}}}else resetInterpreter()}function resetInterpreter(){if(runButtons)for(var e=0;e<runButtons.length;++e)runButtons[e].classList.remove("disabled");document.worker&&(document.worker.terminate(),document.worker=void 0)}function stopCode(){resetInterpreter(),isAndroidUserAgent()&&Android.stopTts()}function changeServerIndicatorColor(){for(var e=0;e<serverIndicatorIcons.length;++e)serverIndicatorIcons[e].classList.contains(serverOnIndicatorColor)?(serverIndicatorIcons[e].classList.remove(serverOnIndicatorColor),serverIndicatorIcons[e].classList.add(serverOffIndicatorColor),$(".server-indicator-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.SERVER_INDICATOR_PENDING})})):serverIndicatorIcons[e].classList.contains(serverOffIndicatorColor)&&(serverIndicatorIcons[e].classList.remove(serverOffIndicatorColor),serverIndicatorIcons[e].classList.add(serverOnIndicatorColor),$(".server-indicator-popup").each(function(e){$(this).popup({position:"bottom center",content:Blockly.Msg.SERVER_INDICATOR_ONLINE})}))}function checkServerConnected(){href="http://localhost:1234",req=new XMLHttpRequest,req.open("GET",href,!0),req.onreadystatechange=function(){4==req.readyState&&200==req.status&&(clearInterval(serverIndicatorInterval),changeServerIndicatorColor())},req.send(null)}function requestServer(e){var o;navigator.userAgent.match(/Android/i)||(o="http://localhost:1234/"+e,req=new XMLHttpRequest,req.open("GET",o,!0),req.send(null))}function callTts(e){isAndroidUserAgent()?Android.tts(e,workspaceLang):alert("not supported!")}function callStt(){if(isAndroidUserAgent()){var e="";try{e=Android.stt(workspaceLang)}catch(e){alert(e)}return e}alert("Sorry!! Stt is available only on Android!!")}function openUrl(e,o){isAndroidUserAgent()||(o?window.open(e,"_blank"):window.open(e))}function redirectToUri(e){isAndroidUserAgent()?Android.loadWebview(e):window.location.replace(e)}function isAndroidUserAgent(){return navigator.userAgent.match(/Android/i)}function callAndroidStt(e){string="default string",string=Android.stt(),alert(string)}function loadWorkspace(){for(defineRoobinTheme();-1!==BLOCKLY_TOOLBOX_XML.standard.indexOf("Blockly.Msg");)BLOCKLY_TOOLBOX_XML.standard=BLOCKLY_TOOLBOX_XML.standard.replace(/Blockly\.Msg\[\"(\w+)\"\]/,function(e,o){return Blockly.Msg[o]});return Blockly.inject("blocklyDiv",{toolbox:BLOCKLY_TOOLBOX_XML.standard,collapse:!0,comments:!0,disable:!0,maxBlocks:1/0,trashcan:!0,horizontalLayout:!1,toolboxPosition:"start",css:!0,rtl:!0,scrollbars:!0,sounds:!0,oneBasedIndex:!0,grid:{spacing:20,length:1,colour:"#888",snap:!1},theme:Blockly.Themes.Roobin_Theme,renderer:"zelos",media:"static/media/",rtl:isRtl(),zoom:{controls:!0,wheel:!0,startScale:1,maxScale:3,minScale:.3,scaleSpeed:1.2}})}function addEventsToBluetoothButton(){document.querySelector(".connect-bluetooth-device").addEventListener("click",function(e){if(deviceArray&&0!==deviceArray.length){if(bluetoothDeviceSelected=$(".select-bluetooth-device").dropdown("get value"),!bluetoothDeviceSelected||bluetoothDeviceSelected===Blockly.Msg.BLUETOOTH_SELECT_DEVICE)return;isAndroidUserAgent()&&Android.connectBluetooth(bluetoothDeviceSelected)}else populateBluetoothDevicesForm()}),document.querySelector(".select-bluetooth-device").addEventListener("click",populateBluetoothDevicesForm)}function populateBluetoothDevicesForm(){isAndroidUserAgent()&&(deviceArray=Android.list().split(","),$.each(deviceArray,function(e,o){o=(o=o.trim()).split(" ").join("-").split("_").join("-"),$(".bluetooth-drop ."+o).length||$(".bluetooth-drop").append($("<div>",{value:e.toString(),text:o.toString(),class:"item "+o}))}))}function convertCategoriesTosemantic(){var e=new MutationObserver(function(e){e.forEach(function(e){var o,t,r;adjustFlyoutPostion(!1,workspaceLang),"attributes"==e.type&&(t=(o=e.target).querySelector(".blocklyTreeRow"),r=window.getComputedStyle(t).getPropertyValue(borderStylePropertyName),addClassToElement(t,"item"),"false"===o.attributes["aria-selected"].value?t.style.color=r:t.style.color="white",onresizeFunc())})}),o=document.querySelector("#blockly-0");addClassToElement(o,"ui vertical inverted labeled icon fluid menu"),addClassToElement(o=document.querySelector(".blocklyToolboxDiv"),"ui inverted menu"),addClassToElement(o=document.querySelector(".blocklyTreeRoot"),"ui inverted item menu"),convertCategoryToSemantic("#blockly-1",Blockly.Msg.LOGIC_CATEGORY,"random icon",e),convertCategoryToSemantic("#blockly-2",Blockly.Msg.LOOPS_CATEGORY,"redo alternate icon",e),convertCategoryToSemantic("#blockly-3",Blockly.Msg.MATH_CATEGORY,"calculator icon",e),convertCategoryToSemantic("#blockly-4",Blockly.Msg.TEXT_CATEGORY,"text width icon",e),convertCategoryToSemantic("#blockly-5",Blockly.Msg.LISTS_CATEGORY,"list ol icon",e),convertCategoryToSemantic("#blockly-6",Blockly.Msg.COLOUR_CATEGORY,"palette icon",e),convertCategoryToSemantic("#blockly-8",Blockly.Msg.VARIABLES_CATEGORY,"buffer icon",e),convertCategoryToSemantic("#blockly-9",Blockly.Msg.FUNCTIONS_CATEGORY,"percentage icon",e),convertCategoryToSemantic("#blockly-a",Blockly.Msg.ROOBIN_CATEGORY,"robot icon",e)}function convertCategoryToSemantic(e,o,t,r){var n={};$.each($(e).find(".blocklyTreeRow")[0].attributes,function(e,o){n[o.nodeName]=o.value}),$(e).find(".blocklyTreeRow").replaceWith(function(){return $("<a />",n).append($(this).contents())});var c=document.querySelector(e).querySelector(".blocklyTreeRow"),l=window.getComputedStyle(c).getPropertyValue(borderStylePropertyName);$(e).find(".blocklyTreeRow").addClass("item").append(o).css("padding","0px").css("color",l).find(".blocklyTreeLabel").replaceWith('<i class="blocklyTreeLabel '+t+'"></i>'),r.observe(document.querySelector(e),{attributes:!0})}function addClassToElement(e,o){-1==e.className.split(" ").indexOf(o)&&(e.className+=" "+o)}function adjustFlyoutPostion(e){var o,t,r,n,c,l,a=$(window).width(),i=document.querySelectorAll(".blocklyFlyout")[1],s=document.querySelector(".blocklyToolboxDiv.blocklyNonSelectable"),d=+window.getComputedStyle(s).width.slice(0,-2),u=+window.getComputedStyle(i).width.slice(0,-2),p=window.getComputedStyle(i).transform;"none"!==p&&(t=+(o=p.split(","))[4],r=+o[5].slice(0,-1),"fa"===workspaceLang||"ar"===workspaceLang?c=n=a-d-u:"en"===workspaceLang&&(c=(n=t)+u-15),l=document.querySelectorAll(".blocklyScrollbarVertical.blocklyFlyoutScrollbar")[1],e?$(document).ready(function(){i.style.transform="translate("+n+"px, "+r+"px)",l.style.transform="translate("+c+"px, 2.5px)"}):(i.style.transform="translate("+n+"px, "+r+"px)",l.style.transform="translate("+c+"px, 2.5px)"))}function defineRoobinTheme(){Blockly.Themes.Roobin_Theme={},Blockly.Themes.Roobin_Theme.categoryStyles={colour_category:{colour:"#CF63CF"},list_category:{colour:"#9966FF"},logic_category:{colour:"#4C97FF"},loop_category:{colour:"#0fBD8C"},math_category:{colour:"#59C059"},procedure_category:{colour:"#FF6680"},text_category:{colour:"#FFBF00"},variable_category:{colour:"#DC143C"},function_category:{colour:"FF6680"},roobin_category:{colour:"#F2711C"}},Blockly.Themes.Roobin_Theme.defaultBlockStyles={colour_blocks:{colourPrimary:"#CF63CF",colourSecondary:"#C94FC9",colourTertiary:"#BD42BD"},list_blocks:{colourPrimary:"#9966FF",colourSecondary:"#855CD6",colourTertiary:"#774DCB"},logic_blocks:{colourPrimary:"#4C97FF",colourSecondary:"#4280D7",colourTertiary:"#3373CC"},loop_blocks:{colourPrimary:"#0fBD8C",colourSecondary:"#0DA57A",colourTertiary:"#0B8E69"},math_blocks:{colourPrimary:"#59C059",colourSecondary:"#46B946",colourTertiary:"#389438"},procedure_blocks:{colourPrimary:"#FF6680",colourSecondary:"#FF4D6A",colourTertiary:"#FF3355"},text_blocks:{colourPrimary:"#FFBF00",colourSecondary:"#E6AC00",colourTertiary:"#CC9900"},variable_blocks:{colourPrimary:"#DC143C",colourSecondary:"#FF8000",colourTertiary:"#DB6E00"},roobin_blocks:{colourPrimary:"#F2711C",colourSecondary:"#FF8000",colourTertiary:"#BB6E00"},roobin_motor_blocks:{colourPrimary:"#00c9b0",colourSecondary:"#FF8000",colourTertiary:"#007d55"},roobin_setup_blocks:{colourPrimary:"#93195b",colourSecondary:"#FF8000",colourTertiary:"#BB6E00"},function_blocks:{colourPrimary:"#FF6680",colourSecondary:"#FF8000",colourTertiary:"#DB6E00"},hat_blocks:{colourPrimary:"#4C97FF",colourSecondary:"#4280D7",colourTertiary:"#3373CC",hat:"cap"}},Blockly.Themes.Roobin_Theme=new Blockly.Theme("Roobin_Theme",Blockly.Themes.Roobin_Theme.defaultBlockStyles,Blockly.Themes.Roobin_Theme.categoryStyles),"ar"===workspaceLang?Blockly.Themes.Roobin_Theme.setFontStyle({family:"Helvetica Neue, Segoe UI, Helvetica, sans-serif",weight:"normal",size:12}):"fa"!==workspaceLang&&"en"!==workspaceLang||Blockly.Themes.Roobin_Theme.setFontStyle({family:"Yekan, Helvetica Neue, Segoe UI, Helvetica, sans-serif",weight:"normal",size:12})}window.onload=function(){init(),loadLastWorkspaceBlocks(),runButtons=document.querySelectorAll("#runBtn"),serverIndicatorIcons=document.querySelectorAll("#serverIndicator"),serverIndicatorInterval=setInterval(checkServerConnected,5e3)},window.addEventListener("resize",onresizeFunc,!1),"ar"===workspaceLang&&document.write('<style type="text/css">\n.ui {\nfont-family: "Helvetica Neue", "Segoe UI", Helvetica, sans-serif !important;\n}\n</style>'),document.write('<script src="static/js/jquery.min.js" type="text/javascript"><\/script>\n'),document.write('<link rel="stylesheet" type="text/css" href="static/css/semantic'+(isRtl()?".rtl":"")+'.min.css">\n'),document.write('<script src="static/js/semantic.min.js" type="text/javascript"><\/script>\n'),document.write('<script src="static/js/blockly_compressed.js"><\/script>\n'),document.write('<script src="static/js/blocks_compressed.js"><\/script>\n'),document.write('<script src="static/js/javascript_compressed.js"><\/script>\n'),document.write('<script src="static/js/python_compressed.js"><\/script>\n'),document.write('<script src="static/js/toolbox_standard.js"><\/script>\n'),document.write('<script src="static/msg/js/'+workspaceLang+'.js"><\/script>\n'),document.write('<script src="static/js/acorn_interpreter.js"><\/script>\n');