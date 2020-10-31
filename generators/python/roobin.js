'use strict';

goog.provide('Blockly.Python.roobin');

goog.require('Blockly.Python');

Blockly.Python['TTS'] = function(block) {
	var text = Blockly.Python.valueToCode(block, 'TTS_INPUT',
	Blockly.Python.ORDER_NONE) || '';
	if(!text)
		return '';
	var code = 'arg = ' + text + '\n';
	code += 'if arg:\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'say\', arg)\n';
	return code;
};

Blockly.Python['STT'] = function(block) {
	var code = "callStt()";
	return [code, Blockly.Python.ORDER_ATOMIC];
};

Blockly.Python['motor_change_rotation_to_given_degree'] = function(block){
	var SEL_HEAD_NECK_MOTOR = block.getFieldValue('SEL_HEAD_NECK_MOTOR');
	var arg = Blockly.Python.valueToCode(block, 'DEGREES',
        Blockly.Python.ORDER_NONE) || '0';
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
	code += '\')';
	return code;
};

Blockly.Python['motor_rotate_by_given_degree'] = function(block){
	var SEL_HEAD_NECK_MOTOR = block.getFieldValue('SEL_HEAD_NECK_MOTOR');
	var arg = Blockly.Python.valueToCode(block, 'DEGREES',
        Blockly.Python.ORDER_NONE) || '0';
	if(arg && arg[0] != '+' && arg[0] != '-'){
		arg = '+' + arg;
	}
	var code = 'rotate(\'';
	if(SEL_HEAD_NECK_MOTOR == 'HEAD_ROTATE'){
		code += '0m' + arg;
	}else if (SEL_HEAD_NECK_MOTOR == 'NECK_ROTATE'){
		code += '1m' + arg;
	}
	code += '\')';
	return code;
};

Blockly.Python['roobin_keyBoard_event'] = function(block) {
	var code = 'var keyPressed;\n';
	if (Blockly.Python.STATEMENT_PREFIX) {
		// Automatic prefix insertion is switched off for this block.  Add manually.
		code += Blockly.Python.injectId(Blockly.Python.STATEMENT_PREFIX, block);
	}
	var keyCode = block.getFieldValue('SEL_ROOBIN_KEY_PRESSED');
	var conditionCode = 'keyPressed == \'' + keyCode + '\'';
	var branchCode = Blockly.Python.statementToCode(block, 'DO') || Blockly.Python.PASS;
	if (Blockly.Python.STATEMENT_SUFFIX) {
		branchCode = Blockly.Python.prefixLines(
      Blockly.Python.injectId(Blockly.Python.STATEMENT_SUFFIX,block), 
      Blockly.Python.INDENT) + branchCode;
	}
	code += 'if ' + conditionCode + ':\n' + branchCode;
	return code + '\n';
};

Blockly.Python['roobin_set_lang'] = function(block) {
	var lang = block.getFieldValue('SEL_ROOBIN_SET_LANG');
	return 'roobin(\'setLanguage\', \'' + lang + '\')\n';
};

Blockly.Python['roobin_set_speak_speed'] = function(block) {
	var arg = Blockly.Python.valueToCode(block, 'SPEED',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'arg = ' + arg + '\n';
	code += 'if arg or arg == 0\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'setSpeakingSpeed\', str(arg))\n';
	return code;
};

Blockly.Python['roobin_set_speak_pitch'] = function(block) {
	var arg = Blockly.Python.valueToCode(block, 'PITCH',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'arg = ' + arg + '\n';
	code += 'if arg or arg == 0\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'setSpeakingPitch\', str(arg))\n';
	return code;
};

Blockly.Python['roobin_change_speak_pitch'] = function(block) {
	var arg = Blockly.Python.valueToCode(block, 'PITCH',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'arg = ' + arg + '\n';
	code += 'if arg or arg == 0\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'changeSpeakingPitch\', str(arg))\n';
	return code;
};

Blockly.Python['roobin_set_stt_var'] = function(block) {
	// var code = 'roobin(\'listenAndSave\')'
	var code = 'listen()';
	return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['roobin_change_eye'] = function(block) {
	var dir = block.getFieldValue('SEL_LEFT_RIGHT_EYE');
	var category = block.getFieldValue('SEL_EYE_CATEGORY');
	return 'roobin(\'changeEye\', \'' + dir + '\', \'' + category + '\')\n';
};

Blockly.Python['roobin_change_mouth'] = function(block) {
	var mouthForm = block.getFieldValue('SEL_MOUTH_FORM');
	return 'roobin(\'changeMouthForm\', \'' + mouthForm + '\')\n';
};

Blockly.Python['roobin_random_gen'] = function(block) {
	var min = Blockly.Python.valueToCode(block, 'LOWER_BOUND',
		Blockly.Python.ORDER_NONE) || '0';
	var max = Blockly.Python.valueToCode(block, 'UPPER_BOUND',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'randint(' + min + ', ' + max + ')';
	return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['roobin_recovery'] = function(block) {
	return 'roobin(\'recovery\')\n';
};

Blockly.Python['roobin_introduce'] = function(block) {
	return 'roobin(\'introduce\')\n';
};

Blockly.Python['roobin_say_hello'] = function(block) {
	return 'roobin(\'sayHello\')\n';
};

Blockly.Python['roobin_chuckle'] = function(block) {
	return 'roobin(\'chuckle\')\n';
};

Blockly.Python['roobin_ask_wait'] = function(block) {
	var text = Blockly.Python.valueToCode(block, 'ASK_INPUT',
		Blockly.Python.ORDER_NONE) || '';
	if(!text)
		return '';
	var code = 'arg = ' + text + '\n';
	code += 'if arg:\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'ask\', arg)\n';
	return code;
};

Blockly.Python['roobin_search_in_wikipedia'] = function(block) {
	return 'roobin(\'wikipediaSearch\')\n';
};

Blockly.Python['roobin_search_word_in_wikipedia'] = function(block) {
	var text = Blockly.Python.valueToCode(block, 'WIKI_INPUT',
		Blockly.Python.ORDER_NONE) || '';
	if(!text)
		return '';
	var code = 'arg = ' + text + '\n';
	code += 'if arg:\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'wikipediaTextSearch\', arg)\n';
	return code;
};

Blockly.Python['roobin_today_date'] = function(block) {
	return 'roobin(\'todaysDate\')\n';
};

Blockly.Python['roobin_find_day'] = function(block) {
	return 'roobin(\'whatDaysDate\')\n';
};

Blockly.Python['roobin_riddle_game'] = function(block) {
	return 'roobin(\'riddleGame\')\n';
};

Blockly.Python['roobin_arrow_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	return 'roobin(\'arrowGame\', \'' + difficulty + '\')\n';
};

Blockly.Python['roobin_pattern_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	return 'roobin(\'patternGameTwo\', \'' + difficulty + '\')\n';
};

Blockly.Python['roobin_numbers_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	return 'roobin(\'numberSeries\', \'' + difficulty + '\')\n';
};

Blockly.Python['roobin_amazing_facts'] = function(block) {
	return 'roobin(\'amazingFacts\')\n';
};

Blockly.Python['roobin_games_explanation'] = function(block) {
	var game = block.getFieldValue('SEL_CHOOSE_GAME');
	return 'roobin(\'gameExplanation\', \'' + game + '\')\n';
};

Blockly.Python['roobin_story_telling'] = function(block) {
	var story = block.getFieldValue('SEL_CHOOSE_STORY');
	return 'roobin(\'tellStory\', \'' + story + '\')\n';
};

Blockly.Python['roobin_move_motor'] = function(block) {
	var headOrNeck = block.getFieldValue('SEL_NECK_HEAD');
	var arg = Blockly.Python.valueToCode(block, 'ROTATION_INPUT',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'arg = ' + arg + '\n';
	code += 'if arg or arg == 0:\n';
	code += Blockly.Python.INDENT;
	code += 'roobinMotor(\'moveMotor\', \'' + headOrNeck +'\', str(arg))\n';
	return code;
};

Blockly.Python['roobin_rotate_motor'] = function(block) {
	var headOrNeck = block.getFieldValue('SEL_NECK_HEAD');
	var arg = Blockly.Python.valueToCode(block, 'ROTATION_INPUT',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'arg = ' + arg + '\n';
	code += 'if arg or arg == 0:\n';
	code += Blockly.Python.INDENT;
	code += 'roobinMotor(\'moveMotorDroplist\', \'' + headOrNeck +'\', str(arg))\n';
	return code;
};

Blockly.Python['roobin_blink'] = function(block) {
	return 'roobin(\'blink\')\n';
};

Blockly.Python['roobin_look_around'] = function(block) {
	return 'roobin(\'lookSides\')\n';
};

Blockly.Python['roobin_neutral'] = function(block) {
	return 'roobin(\'lookAhead\')\n';
};

Blockly.Python['roobin_draw_on_eyes'] = function(block) {
	var leftOrRight = block.getFieldValue('SEL_LEFT_RIGHT');
	var onOrOff = block.getFieldValue('SEL_ON_OFF');
	var arg1 = Blockly.Python.valueToCode(block, 'X',
		Blockly.Python.ORDER_NONE) || '0';
	var arg2 = Blockly.Python.valueToCode(block, 'Y',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'arg1 = ' + arg1 + '\n';
	code += 'arg2 = ' + arg2 + '\n';
	code += 'if (arg1 or arg1 == 0) and (arg2 or arg2 == 0):\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'drawOnEyes\', \'' + leftOrRight + '\', str(arg1), str(arg2), \'' + onOrOff + '\')\n';
	return code;
};

Blockly.Python['roobin_draw_on_mouth'] = function(block) {
	var onOrOff = block.getFieldValue('SEL_ON_OFF');
	var arg1 = Blockly.Python.valueToCode(block, 'X',
		Blockly.Python.ORDER_NONE) || '0';
	var arg2 = Blockly.Python.valueToCode(block, 'Y',
		Blockly.Python.ORDER_NONE) || '0';
	var code = 'arg1 = ' + arg1 + '\n';
	code += 'arg2 = ' + arg2 + '\n';
	code += 'if (arg1 or arg1 == 0) and (arg2 or arg2 == 0):\n';
	code += Blockly.Python.INDENT;
	code += 'roobin(\'drawOnMouth\', str(arg1), str(arg2), \'' + onOrOff + '\')\n';
	return code;
};

Blockly.Python['roobin_clean_matrices'] = function(block) {
	var selection = block.getFieldValue('SEL_PART');
	return 'roobin(\'turnOffEyeOrMouth\', \'' + selection + '\')\n';
};