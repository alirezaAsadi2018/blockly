'use strict';

goog.provide('Blockly.Python.roobin');

goog.require('Blockly.Python');

//my block for TTS
Blockly.Python['TTS'] = function(block) {
  // Input statement.
  var msg = Blockly.JavaScript.valueToCode(block, 'TTS_INPUT',
      Blockly.JavaScript.ORDER_NONE) || '\'\'';
  return 'callTts(' + msg + ')\n';
};

//my block for STT
Blockly.Python['STT'] = function(block) {
  var code = "callStt()";
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};