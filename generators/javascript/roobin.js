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