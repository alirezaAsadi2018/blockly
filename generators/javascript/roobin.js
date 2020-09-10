'use strict';

goog.provide('Blockly.JavaScript.roobin');

goog.require('Blockly.JavaScript');

//my block for TTS
Blockly.JavaScript['TTS'] = function(block) {
	var text = Blockly.JavaScript.valueToCode(block, 'TTS_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '';
	var query = 'begoo/';
	if(!text)
		return '';
	return 'if(' + text +')\nrequestServer(\'' + query + '\' + ' + text + ');\n';
};

//my block for STT
Blockly.JavaScript['STT'] = function(block) {
  var code = "callStt()";
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['motor_change_rotation_to_given_degree'] = function(block){
	var SEL_HEAD_NECK_MOTOR = block.getFieldValue('SEL_HEAD_NECK_MOTOR');
	var arg = Blockly.JavaScript.valueToCode(block, 'DEGREES',
        Blockly.JavaScript.ORDER_NONE) || '0';
	if(+arg > 80)
		arg = 80;
	if(+arg < 0){
		arg = 0;
	}
	var code = 'rotate(\'';
	if(SEL_HEAD_NECK_MOTOR == 'HEAD_ROTATE'){
		code += '0m' + arg;
	}else if (SEL_HEAD_NECK_MOTOR == 'NECK_ROTATE'){
		code += '1m' + arg;
	}
	code += '\');';
	return code;
};

Blockly.JavaScript['motor_rotate_by_given_degree'] = function(block){
	var SEL_HEAD_NECK_MOTOR = block.getFieldValue('SEL_HEAD_NECK_MOTOR');
	var arg = Blockly.JavaScript.valueToCode(block, 'DEGREES',
        Blockly.JavaScript.ORDER_NONE) || '0';
	if(arg && arg[0] != '+' && arg[0] != '-'){
		arg = '+' + arg;
	}
	var code = 'rotate(\'';
	if(SEL_HEAD_NECK_MOTOR == 'HEAD_ROTATE'){
		code += '0m' + arg;
	}else if (SEL_HEAD_NECK_MOTOR == 'NECK_ROTATE'){
		code += '1m' + arg;
	}
	code += '\');';
	return code;
};

Blockly.JavaScript['roobin_keyBoard_event'] = function(block) {
	var code = 'var keyPressed;\n';
	if (Blockly.JavaScript.STATEMENT_PREFIX) {
		// Automatic prefix insertion is switched off for this block.  Add manually.
		code += Blockly.JavaScript.injectId(Blockly.JavaScript.STATEMENT_PREFIX,
			block);
	}
	var keyCode = block.getFieldValue('SEL_ROOBIN_KEY_PRESSED');
	var conditionCode = 'keyPressed === \'' + keyCode + '\'';
	var branchCode = Blockly.JavaScript.statementToCode(block, 'DO');
	if (Blockly.JavaScript.STATEMENT_SUFFIX) {
		branchCode = Blockly.JavaScript.prefixLines(
			Blockly.JavaScript.injectId(Blockly.JavaScript.STATEMENT_SUFFIX,
			block), Blockly.JavaScript.INDENT) + branchCode;
	}
	code += 'if (' + conditionCode + ') {\n' + branchCode + '}';
	return code + '\n';
};

Blockly.JavaScript['roobin_set_lang'] = function(block) {
	var lang = block.getFieldValue('SEL_ROOBIN_SET_LANG');
	var query = 'set_language/' + lang;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_set_speak_speed'] = function(block) {
	var arg = Blockly.JavaScript.valueToCode(block, 'SPEED',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var query = 'set_speak_speed/';
	return 'if(' + arg +' || ' + arg + ' === 0)requestServer(\'' + query + '\' + ' + arg + ');\n';
};

Blockly.JavaScript['roobin_set_speak_pitch'] = function(block) {
	var arg = Blockly.JavaScript.valueToCode(block, 'PITCH',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var query = 'set_speak_pitch/';
	return 'if(' + arg +' || ' + arg + ' === 0)requestServer(\'' + query + '\' + ' + arg +');\n';
};

Blockly.JavaScript['roobin_change_speak_pitch'] = function(block) {
	var arg = Blockly.JavaScript.valueToCode(block, 'PITCH',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var query = 'change_speak_pitch/';
	return 'if(' + arg +' || ' + arg + ' === 0)requestServer(\'' + query + '\' + ' + arg +');\n';
};

Blockly.JavaScript['roobin_set_stt_var'] = function(block) {
	var query = 'set_stt_var';
	var code = 'requestServer(\'' + query + '\')'
	return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['roobin_change_eye'] = function(block) {
	var dir = block.getFieldValue('SEL_LEFT_RIGHT_EYE');
	var category = block.getFieldValue('SEL_EYE_CATEGORY');
	var query = 'change_eye/' + dir + '/' + category;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_change_mouth'] = function(block) {
	var mouthForm = block.getFieldValue('SEL_MOUTH_FORM');
	var query = 'change_mouth/' + mouthForm;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_random_gen'] = function(block) {
	var min = Blockly.JavaScript.valueToCode(block, 'LOWER_BOUND',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var max = Blockly.JavaScript.valueToCode(block, 'UPPER_BOUND',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var code = 'Math.floor(Math.random() * (' + max + ' - ' + min + ') + ' + min + ')';
	return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['roobin_recovery'] = function(block) {
	var query = 'recovery';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_introduce'] = function(block) {
	var query = 'introduce';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_say_hello'] = function(block) {
	var query = 'say_hello';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_chuckle'] = function(block) {
	var query = 'chuckle';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_ask_wait'] = function(block) {
	var text = Blockly.JavaScript.valueToCode(block, 'ASK_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '';
	var query = 'askNwait/';
	if(!text)
		return '';
	return 'if(' + text +')\nrequestServer(\'' + query + '\' + ' + text + ');\n';
};

Blockly.JavaScript['roobin_search_in_wikipedia'] = function(block) {
	var query = 'search_in_wikipedia';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_search_word_in_wikipedia'] = function(block) {
	var text = Blockly.JavaScript.valueToCode(block, 'WIKI_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '';
	var query = 'search_sth_in_wikipedia/';
	if(!text)
		return '';
	return 'if(' + text +')\nrequestServer(\'' + query + '\' + ' + text + ');\n';
};

Blockly.JavaScript['roobin_today_date'] = function(block) {
	var query = 'today';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_find_day'] = function(block) {
	var query = 'chan_shanbeh';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_riddle_game'] = function(block) {
	var query = 'riddle_game';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_arrow_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	var query = 'arrow_game/' + difficulty;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_pattern_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	var query = 'repeating_pattern_game2/' + difficulty;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_numbers_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	var query = 'number_series/' + difficulty;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_amazing_facts'] = function(block) {
	var query = 'amazing_facts';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_games_explanation'] = function(block) {
	var game = block.getFieldValue('SEL_CHOOSE_GAME');
	var query = 'games_explanation/' + game;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_story_telling'] = function(block) {
	var story = block.getFieldValue('SEL_CHOOSE_STORY');
	var query = 'story_telling/' + story;
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_move_motor'] = function(block) {
	var headOrNeck = block.getFieldValue('SEL_NECK_HEAD');
	var arg = Blockly.JavaScript.valueToCode(block, 'ROTATION_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var query = 'move_motor/' + headOrNeck + '/';
	return 'if(' + arg +' || ' + arg + ' === 0)\nrequestServer(\'' + query + '\' + ' + arg +');\n';
};

Blockly.JavaScript['roobin_rotate_motor'] = function(block) {
	var headOrNeck = block.getFieldValue('SEL_NECK_HEAD');
	var arg = Blockly.JavaScript.valueToCode(block, 'ROTATION_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var query = 'move_motor_droplist/' + headOrNeck + '/';
	return 'if(' + arg +' || ' + arg + ' === 0)\nrequestServer(\'' + query + '\' + ' + arg +');\n';
};

Blockly.JavaScript['roobin_blink'] = function(block) {
	var query = 'roobinBlink';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_look_around'] = function(block) {
	var query = 'roobinLookSides';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_neutral'] = function(block) {
	var query = 'roobinNeutral';
	return 'requestServer(\'' + query + '\');\n';
};

Blockly.JavaScript['roobin_draw_on_eyes'] = function(block) {
	var leftOrRight = block.getFieldValue('SEL_LEFT_RIGHT');
	var onOrOff = block.getFieldValue('SEL_ON_OFF');
	var arg1 = Blockly.JavaScript.valueToCode(block, 'X',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var arg2 = Blockly.JavaScript.valueToCode(block, 'Y',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var query = 'draw_on_eyes/' + leftOrRight;
	return 'if((' + arg1 +' || ' + arg1 + ' === 0) && (' + arg2 +' || ' + arg2 + ' === 0))\nrequestServer(\'' + query + '/\' + ' + arg1 + ' + \'/\' +' + arg2 + ' + \'/\' + \'' + onOrOff + '\');\n';
};

Blockly.JavaScript['roobin_draw_on_mouth'] = function(block) {
	var onOrOff = block.getFieldValue('SEL_ON_OFF');
	var arg1 = Blockly.JavaScript.valueToCode(block, 'X',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var arg2 = Blockly.JavaScript.valueToCode(block, 'Y',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var query = 'draw_on_mouth/';
	return 'if((' + arg1 +' || ' + arg1 + ' === 0) && (' + arg2 +' || ' + arg2 + ' === 0))\nrequestServer(\'' + query + '\' + ' + arg1 + ' + \'/\' +' + arg2 + ' + \'/\' + \'' + onOrOff + '\');\n';
};

Blockly.JavaScript['roobin_clean_matrices'] = function(block) {
	var selection = block.getFieldValue('SEL_PART');
	var query = 'clean_the_matrices/' + selection;
	return 'requestServer(\'' + query + '\');\n';
};