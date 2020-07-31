'use strict';

goog.provide('Blockly.JavaScript.roobin');

goog.require('Blockly.JavaScript');

//my block for TTS
Blockly.JavaScript['TTS'] = function(block) {
  // Input statement.
  var msg = Blockly.JavaScript.valueToCode(block, 'TTS_INPUT',
      Blockly.JavaScript.ORDER_NONE) || '\'\'';
  return 'callTts(' + msg + ');\n';
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
}

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
}