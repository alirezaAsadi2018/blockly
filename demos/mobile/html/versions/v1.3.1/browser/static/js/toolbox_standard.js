var BLOCKLY_TOOLBOX_XML = BLOCKLY_TOOLBOX_XML || Object.create(null);

// toolbox
/* BEGINNING BLOCKLY_TOOLBOX_XML ASSIGNMENT. DO NOT EDIT. USE BLOCKLY DEVTOOLS. */
BLOCKLY_TOOLBOX_XML['standard'] =
// From XML string/file, replace ^\s?(\s*)?(<.*>)$ with \+$1'$2'
// Tweak first and last line.
'<xml xmlns="https://developers.google.com/blockly/xml">'
+ '<category name="Logic" categorystyle="logic_category">'
+   '<block type="controls_if"></block>'
+ 	'<block type="controls_ifelse"></block>'
+   '<block type="logic_compare">'
+     '<field name="OP">EQ</field>'
+   '</block>'
+   '<block type="logic_operation">'
+     '<field name="OP">AND</field>'
+   '</block>'
+   '<block type="logic_negate"></block>'
+   '<block type="logic_boolean">'
+     '<field name="BOOL">TRUE</field>'
+   '</block>'
+   '<block type="logic_null"></block>'
+   '<block type="logic_ternary"></block>'
+ '</category>'
+ '<category name="Loops" categorystyle="loop_category">'
+   '<block type="controls_repeat_ext">'
+     '<value name="TIMES">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="controls_whileUntil">'
+     '<field name="MODE">WHILE</field>'
+   '</block>'
+   '<block type="loop_forever">'
+   '</block>'
+   '<block type="controls_for">'
+     '<field name="VAR" id="DzE:Yo@lUe-!K(WujX#_">i</field>'
+     '<value name="FROM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="TO">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="BY">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="controls_forEach">'
+     '<field name="VAR" id="T*HejW|)fme#2k.s!$=f">j</field>'
+   '</block>'
+   '<block type="controls_flow_statements">'
+     '<field name="FLOW">BREAK</field>'
+   '</block>'
+ '</category>'
+ '<category name="Math" categorystyle="math_category">'
+   '<block type="math_number">'
+     '<field name="NUM">0</field>'
+   '</block>'
+   '<block type="math_arithmetic">'
+     '<field name="OP">ADD</field>'
+     '<value name="A">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="B">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_single">'
+     '<field name="OP">ROOT</field>'
+     '<value name="NUM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">9</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_trig">'
+     '<field name="OP">SIN</field>'
+     '<value name="NUM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">45</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_constant">'
+     '<field name="CONSTANT">PI</field>'
+   '</block>'
+   '<block type="math_number_property">'
+     '<mutation divisor_input="false"></mutation>'
+     '<field name="PROPERTY">EVEN</field>'
+     '<value name="NUMBER_TO_CHECK">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_round">'
+     '<field name="OP">ROUND</field>'
+     '<value name="NUM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">3.1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_on_list">'
+     '<mutation op="SUM"></mutation>'
+     '<field name="OP">SUM</field>'
+   '</block>'
+   '<block type="math_modulo">'
+     '<value name="DIVIDEND">'
+       '<shadow type="math_number">'
+         '<field name="NUM">64</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="DIVISOR">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_constrain">'
+     '<value name="VALUE">'
+       '<shadow type="math_number">'
+         '<field name="NUM">50</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="LOW">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="HIGH">'
+       '<shadow type="math_number">'
+         '<field name="NUM">100</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_random_int">'
+     '<value name="FROM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="TO">'
+       '<shadow type="math_number">'
+         '<field name="NUM">100</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="math_random_float"></block>'
+ '</category>'
+ '<category name="Text" categorystyle="text_category">'
+   '<block type="text">'
+     '<field name="TEXT"></field>'
+   '</block>'
+   '<block type="text_print">'
+     '<value name="TEXT">'
+       '<shadow type="text">'
+         '<field name="TEXT">abc</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="text_join">'
+     '<mutation items="2"></mutation>'
+   '</block>'
+   '<block type="text_append">'
+     '<field name="VAR" id="{vCAW/-rvM?[i$ls^g#C">item</field>'
+     '<value name="TEXT">'
+       '<shadow type="text">'
+         '<field name="TEXT"></field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="text_length">'
+     '<value name="VALUE">'
+       '<shadow type="text">'
+         '<field name="TEXT">abc</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="text_isEmpty">'
+     '<value name="VALUE">'
+       '<shadow type="text">'
+         '<field name="TEXT"></field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="text_indexOf">'
+     '<field name="END">FIRST</field>'
+     '<value name="VALUE">'
+       '<block type="variables_get">'
+         '<field name="VAR" id="342/sJReMg:H1J.$)2ia">text</field>'
+       '</block>'
+     '</value>'
+     '<value name="FIND">'
+       '<shadow type="text">'
+         '<field name="TEXT">abc</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="text_charAt">'
+     '<mutation at="true"></mutation>'
+     '<field name="WHERE">FROM_START</field>'
+     '<value name="VALUE">'
+       '<block type="variables_get">'
+         '<field name="VAR" id="342/sJReMg:H1J.$)2ia">text</field>'
+       '</block>'
+     '</value>'
+   '</block>'
+   '<block type="text_getSubstring">'
+     '<mutation at1="true" at2="true"></mutation>'
+     '<field name="WHERE1">FROM_START</field>'
+     '<field name="WHERE2">FROM_START</field>'
+     '<value name="STRING">'
+       '<block type="variables_get">'
+         '<field name="VAR" id="342/sJReMg:H1J.$)2ia">text</field>'
+       '</block>'
+     '</value>'
+   '</block>'
+   '<block type="text_changeCase">'
+     '<field name="CASE">UPPERCASE</field>'
+     '<value name="TEXT">'
+       '<shadow type="text">'
+         '<field name="TEXT">abc</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="text_trim">'
+     '<field name="MODE">BOTH</field>'
+     '<value name="TEXT">'
+       '<shadow type="text">'
+         '<field name="TEXT">abc</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="text_prompt_ext">'
+     '<mutation type="TEXT"></mutation>'
+     '<field name="TYPE">TEXT</field>'
+     '<value name="TEXT">'
+       '<shadow type="text">'
+         '<field name="TEXT">abc</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+ '</category>'
+ '<category name="Lists" categorystyle="list_category">'
+   '<block type="lists_create_with">'
+     '<mutation items="0"></mutation>'
+   '</block>'
+   '<block type="lists_create_with">'
+     '<mutation items="3"></mutation>'
+   '</block>'
+   '<block type="lists_repeat">'
+     '<value name="NUM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">5</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="lists_length"></block>'
+   '<block type="lists_isEmpty"></block>'
+   '<block type="lists_indexOf">'
+     '<field name="END">FIRST</field>'
+     '<value name="VALUE">'
+       '<block type="variables_get">'
+         '<field name="VAR" id="(408GdsD]@PR!ZaZ)iC?">list</field>'
+       '</block>'
+     '</value>'
+   '</block>'
+   '<block type="lists_getIndex">'
+     '<mutation statement="false" at="true"></mutation>'
+     '<field name="MODE">GET</field>'
+     '<field name="WHERE">FROM_START</field>'
+     '<value name="VALUE">'
+       '<block type="variables_get">'
+         '<field name="VAR" id="(408GdsD]@PR!ZaZ)iC?">list</field>'
+       '</block>'
+     '</value>'
+   '</block>'
+   '<block type="lists_setIndex">'
+     '<mutation at="true"></mutation>'
+     '<field name="MODE">SET</field>'
+     '<field name="WHERE">FROM_START</field>'
+     '<value name="LIST">'
+       '<block type="variables_get">'
+         '<field name="VAR" id="(408GdsD]@PR!ZaZ)iC?">list</field>'
+       '</block>'
+     '</value>'
+   '</block>'
+   '<block type="lists_getSublist">'
+     '<mutation at1="true" at2="true"></mutation>'
+     '<field name="WHERE1">FROM_START</field>'
+     '<field name="WHERE2">FROM_START</field>'
+     '<value name="LIST">'
+       '<block type="variables_get">'
+         '<field name="VAR" id="(408GdsD]@PR!ZaZ)iC?">list</field>'
+       '</block>'
+     '</value>'
+   '</block>'
+   '<block type="lists_split">'
+     '<mutation mode="SPLIT"></mutation>'
+     '<field name="MODE">SPLIT</field>'
+     '<value name="DELIM">'
+       '<shadow type="text">'
+         '<field name="TEXT">,</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="lists_sort">'
+     '<field name="TYPE">NUMERIC</field>'
+     '<field name="DIRECTION">1</field>'
+   '</block>'
+ '</category>'
+ '<category name="Colour" categorystyle="colour_category">'
+   '<block type="colour_picker">'
+     '<field name="COLOUR">#ff0000</field>'
+   '</block>'
+   '<block type="colour_random"></block>'
+   '<block type="colour_rgb">'
+     '<value name="RED">'
+       '<shadow type="math_number">'
+         '<field name="NUM">100</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="GREEN">'
+       '<shadow type="math_number">'
+         '<field name="NUM">50</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="BLUE">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="colour_blend">'
+     '<value name="COLOUR1">'
+       '<shadow type="colour_picker">'
+         '<field name="COLOUR">#ff0000</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="COLOUR2">'
+       '<shadow type="colour_picker">'
+         '<field name="COLOUR">#3333ff</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="RATIO">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0.5</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+ '</category>'
+ '<sep></sep>'
+ '<category name="Variables" categorystyle="variable_category" custom="VARIABLE"></category>'
+ '<category name="Functions" categorystyle="function_category" custom="PROCEDURE"></category>'
+ '<category name = "Roobin" categorystyle="roobin_category">'
+   '<block type="roobin_search_word_in_wikipedia" toolboxitemid="wiki" disabled="true">'
+     '<value name="WIKI_INPUT">'
+       '<shadow type="text">'
+         '<field name="TEXT">Blockly.Msg["ROOBIN_WIKI_SEARCH"]</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="roobin_keyBoard_event">'
+     '<field name="SEL_ROOBIN_KEY_PRESSED">ArrowUp</field>'
+	'</block>'
+   '<block type="roobin_set_stt_var" disabled="true">'
+	'</block>'
+   '<block type="TTS" disabled="true">'
+     '<value name="TTS_INPUT">'
+       '<shadow type="text">'
+         '<field name="TEXT">Blockly.Msg["ROOBIN_TTS_TEXT"]</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="roobin_set_lang" disabled="true">'
+     '<field name="SEL_ROOBIN_SET_LANG">fa</field>'
+	'</block>'
+   '<block type="roobin_set_speak_speed" disabled="true">'
+     '<value name="SPEED">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+	'</block>'
+   '<block type="roobin_set_speak_pitch" disabled="true">'
+     '<value name="PITCH">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+	'</block>'
+   '<block type="roobin_change_speak_pitch" disabled="true">'
+     '<value name="PITCH">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+	'</block>'
+   '<block type="roobin_change_eye" disabled="true">'
+     '<field name="SEL_LEFT_RIGHT_EYE">چپ</field>'
+     '<field name="SEL_EYE_CATEGORY">سالیوان</field>'
+	'</block>'
+   '<block type="roobin_change_mouth" disabled="true">'
+     '<field name="SEL_MOUTH_FORM">روبین</field>'
+	'</block>'
+   '<block type="roobin_random_gen">'
+     '<value name="LOWER_BOUND">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="UPPER_BOUND">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+	'</block>'
+   '<block type="roobin_recovery" disabled="true">'
+	'</block>'
+   '<block type="roobin_introduce" disabled="true">'
+	'</block>'
+   '<block type="roobin_say_hello" disabled="true">'
+	'</block>'
+   '<block type="roobin_chuckle" disabled="true">'
+	'</block>'
+   '<block type="roobin_ask_wait" disabled="true">'
+     '<value name="ASK_INPUT">'
+       '<shadow type="text">'
+         '<field name="TEXT">Blockly.Msg["ROOBIN_ASK_WAIT_TEXT"]</field>'
+       '</shadow>'
+     '</value>'
+	'</block>'
+   '<block type="roobin_today_date" disabled="true">'
+	'</block>'
+   '<block type="roobin_find_day" disabled="true">'
+	'</block>'
+   '<block type="roobin_move_motor" disabled="true">'
+     '<field name="SEL_NECK_HEAD">گردن</field>'
+     '<value name="ROTATION_INPUT">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+	'</block>'
+   '<block type="roobin_rotate_motor" disabled="true">'
+     '<field name="SEL_NECK_HEAD">گردن</field>'
+     '<value name="ROTATION_INPUT">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+	'</block>'
+   '<block type="roobin_blink" disabled="true">'
+	'</block>'
+   '<block type="roobin_look_around" disabled="true">'
+	'</block>'
+   '<block type="roobin_neutral" disabled="true">'
+	'</block>'
+   '<block type="roobin_draw_on_eyes" disabled="true">'
+     '<field name="SEL_LEFT_RIGHT">راست</field>'
+     '<value name="X">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="Y">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+     '<field name="SEL_ON_OFF">روشن</field>'
+	'</block>'
+   '<block type="roobin_draw_on_mouth" disabled="true">'
+     '<value name="X">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="Y">'
+       '<shadow type="math_number">'
+         '<field name="NUM">0</field>'
+       '</shadow>'
+     '</value>'
+     '<field name="SEL_ON_OFF">روشن</field>'
+	'</block>'
+   '<block type="roobin_clean_matrices" disabled="true">'
+     '<field name="SEL_PART">چشم راست</field>'
+	'</block>'
+ '</category>'
+'</xml>'