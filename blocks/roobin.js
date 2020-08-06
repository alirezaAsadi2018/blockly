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
  // block for tts
  {
    "type": "TTS",
	"message0": "%{BKY_ROOBIN_TTS}",   //   Blockly.Msg['TTS'], -> not available yet!
	"style": "roobin_blocks",
    //"message0": "تبدیل متن: %1 به صوت",
	"args0": [{
      "type": "input_value",
      "name": "TTS_INPUT",
	  "check": "String"
    }],
	"previousStatement": null,
    "nextStatement": null,
    "tooltip": "متن موجود در ورودی را به صوت تبدیل می کند."
  },
  // block for stt
  {
    "type": "STT",
	"message0": "%{BKY_ROOBIN_STT}", //	Blockly.Msg['STT'], -> not available yet!
	"style": "roobin_blocks",
    //"message0": "تبدیل صوت به متن",
    "output": "String",
	"tooltip": "صوت شما را به متن تبدیل می کند و آن را برمی گرداند."
  },
  // block for rotating to a given degree
  {
	"type": "motor_change_rotation_to_given_degree",
	"message0": "%{BKY_ROOBIN_CHANGE_MOTOR_ROTATION_DEGREES}",
	"style": "roobin_blocks",
    "args0": [{
        "type": "field_dropdown",
        "name": "SEL_HEAD_NECK_MOTOR",
        "options": [
			  ["%{BKY_ROOBIN_HEAD}", 'HEAD_ROTATE'],
			  ["%{BKY_ROOBIN_NECK}", 'NECK_ROTATE']
		]
	  },
	  {
			"type": "input_value",
			"name": "DEGREES",
			"check": "Number"
	  }
	],
	"previousStatement": null,
    "nextStatement": null
  },
  // block for rotating by a given degree
  {
	"type": "motor_rotate_by_given_degree",
	"message0": "%{BKY_ROOBIN_ROTATE_MOTOR_BY_DEGREES}",
	"style": "roobin_blocks",
    "args0": [{
        "type": "field_dropdown",
        "name": "SEL_HEAD_NECK_MOTOR",
        "options": [
			  ["%{BKY_ROOBIN_HEAD}", 'HEAD_ROTATE'],
			  ["%{BKY_ROOBIN_NECK}", 'NECK_ROTATE']
		]
	  },
	  {
			"type": "input_value",
			"name": "DEGREES",
			"check": "Number"
	  }
	],
	"previousStatement": null,
    "nextStatement": null
  }
]);