'use strict';

goog.provide('Blockly.Blocks.roobin');  // Deprecated
goog.provide('Blockly.Constants.Roobin');

goog.require('Blockly');
goog.require('Blockly.Blocks');
goog.require('Blockly.FieldDropdown');
goog.require('Blockly.FieldImage');
goog.require('Blockly.FieldMultilineInput');
goog.require('Blockly.FieldTextInput');
goog.require('Blockly.FieldVariable');
goog.require('Blockly.Mutator');


Blockly.defineBlocksWithJsonArray([  // BEGIN JSON EXTRACT
  // my Block for tts
  {
    "type": "TTS",
	//"message0": Blockly.Msg['TTS'], -> not available yet!
    "message0": "تبدیل متن: %1 به صوت",
	"args0": [{
      "type": "input_value",
      "name": "TTS_INPUT",
	  "check": "String"
    }],
	"previousStatement": null,
    "nextStatement": null,
    "style": "text_blocks",
    "tooltip": "متن موجود در ورودی را به صوت تبدیل می کند."
  },
  // my Block for stt
  {
    "type": "STT",
	//"message0": Blockly.Msg['STT'], -> not available yet!
    "message0": "تبدیل صوت به متن",
    "output": "String",
    "style": "text_blocks",
	"tooltip": "صوت شما را به متن تبدیل می کند و آن را برمی گرداند."
  },
  {
	  "type": "motor_rotate_to_given_degree",
	  
  }
]);