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
	"message0": "%{BKY_ROOBIN_TTS}",
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
	"message0": "%{BKY_ROOBIN_STT}",
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
  },
  {
	"type": "roobin_keyBoard_event",
	"message0": "%{BKY_ROOBIN_WHEN_KEY_PRESSED}",
	"style": "roobin_blocks",
	"args0": [{
		"type": "field_dropdown",
		"name": "SEL_ROOBIN_KEY_PRESSED",
		"options": [
			["Arrow up", 'ArrowUp'],
			["Arrow down", 'ArrowDown'],
			["Arrow left", 'ArrowLeft'],
			["Arrow right", 'ArrowRight'],
			["w", 'KeyW'],
			["a", 'KeyA'],
			["s", 'KeyS'],
			["d", 'KeyD'],
			["Space", 'Space']
		]
	}],
	"message1": "%{BKY_CONTROLS_IF_MSG_THEN} %1", // This is the same as if statement
    "args1": [
      {
        "type": "input_statement",
        "name": "DO"
      }
    ]
	// "tooltip": ""
	},
	{
		"type": "roobin_set_lang",
		"message0": "%{BKY_ROOBIN_SET_LANG}",
		"style": "roobin_setup_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_ROOBIN_SET_LANG",
			"options": [
				["%{BKY_ROOBIN_SET_LANG_FA}", 'fa'],
				["%{BKY_ROOBIN_SET_LANG_EN}", 'en']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "زبان روبات و حرف زدن و شنیدن آن را تغییر دهید."
	},
	{
		"type": "roobin_set_speak_speed",
		"message0": "%{BKY_ROOBIN_SET_SPEAK_SPEED}",
		"style": "roobin_setup_blocks",
		"args0": [
			{
				"type": "input_value",
				"name": "SPEED",
				"check": "Number"
			}
		],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "سرعت حرف زدن ربات را تغییر می دهد."
	},
	{
		"type": "roobin_set_speak_pitch",
		"message0": "%{BKY_ROOBIN_SET_SPEAK_PITCH}",
		"style": "roobin_setup_blocks",
		"args0": [
			{
				"type": "input_value",
				"name": "PITCH",
				"check": "Number"
			}
		],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "نازکی صدا را تنظیم میکند."
	},
	{
		"type": "roobin_change_speak_pitch",
		"message0": "%{BKY_ROOBIN_CHANGE_SPEAK_PITCH}",
		"style": "roobin_setup_blocks",
		"args0": [
			{
				"type": "input_value",
				"name": "PITCH",
				"check": "Number"
			}
		],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "نازکی صدا را روی مقدار داده شده تغییر می دهد."
	},
	{
		"type": "roobin_set_stt_var",
		"message0": "%{BKY_ROOBIN_SET_STT_VAR}",
		"style": "roobin_blocks",
		"output": "String"
		// "tooltip": "صوت شما را به متن تبدیل می کند و آن را برمی گرداند."
	},
	{
		"type": "roobin_change_eye",
		"message0": "%{BKY_ROOBIN_CHANGE_EYE}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_LEFT_RIGHT_EYE",
			"options": [
				["%{BKY_ROOBIN_LEFT}", 'چپ'],
				["%{BKY_ROOBIN_RIGHT}", 'راست']
			]
		},
		{
			"type": "field_dropdown",
			"name": "SEL_EYE_CATEGORY",
			"options": [
				["%{BKY_ROOBIN_SULLIVAN}", 'سالیوان'],
				["%{BKY_ROOBIN_SPONGE_BOB}", 'باب اسفنج'],
				["%{BKY_ROOBIN_KITTY}", 'کیتی'],
				["%{BKY_ROOBIN_MINION}", 'مینیون'],
				["%{BKY_ROOBIN_BATMAN}", 'بتمن'],
				["%{BKY_ROOBIN_ANGRY_BIRD}", 'پرنده عصبانی'],
				["%{BKY_ROOBIN_CIRCULAR}", 'دایره ای'],
				["%{BKY_ROOBIN_DIAMOND}", 'لوزی'],
				["%{BKY_ROOBIN_TRIANGULAR}", 'مثلثی'],
				["%{BKY_ROOBIN_ORIGINAL}", 'اصل']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "چشم راست یا چپ ربات را به شکلی که می خواهید در می آورد."
	},
	{
		"type": "roobin_change_mouth",
		"message0": "%{BKY_ROOBIN_CHANGE_MOUTH}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_MOUTH_FORM",
			"options": [
				["%{BKY_ROOBIN_ORIGINAL_MOUTH}", 'روبین'],
				["%{BKY_ROOBIN_PUCKER_MOUTH}", 'غنچه']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "شکل دهان ربات را تغییر می دهد."
	},
	{
		"type": "roobin_random_gen",
		"message0": "%{BKY_ROOBIN_RANDOM_GEN}",
		"style": "roobin_blocks",
		"args0": [
			{
				"type": "input_value",
				"name": "LOWER_BOUND",
				"check": "Number"
			},
			{
				"type": "input_value",
				"name": "UPPER_BOUND",
				"check": "Number"
			}
		],
		"output": "Number",
		// "tooltip": "یک عدد تصادفی بین اعداد داده شده تولید می کند."
	},
	{
		"type": "roobin_recovery",
		"message0": "%{BKY_ROOBIN_RECOVERY}",
		"style": "roobin_setup_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین را ریستارت می کند."
	},
	{
		"type": "roobin_introduce",
		"message0": "%{BKY_ROOBIN_INTRODUCE}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین را معرفی می کند."
	},
	{
		"type": "roobin_say_hello",
		"message0": "%{BKY_ROOBIN_SAY_HELLO}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "به روبین سلام کنید تا جوابتان را بدهد."
	},
	{
		"type": "roobin_chuckle",
		"message0": "%{BKY_ROOBIN_CHUCKLE}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "رو این دکمه بزنید تا روبین رو خوشحال کنید."
	},
	{
		"type": "roobin_ask_wait",
		"message0": "%{BKY_ROOBIN_ASK_WAIT}",
		"style": "roobin_blocks",
		"args0": [
			{
				"type": "input_value",
				"name": "ASK_INPUT",
				"check": "String"
			}
		],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": ""
	},
	{
		"type": "roobin_search_in_wikipedia",
		"message0": "%{BKY_ROOBIN_SEARCH_IN_WIKI}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "صدای شما را می شنود و صفحه ای که گفتید را در ویکیپدیا جستجو می کند."
	},
	{
		"type": "roobin_search_word_in_wikipedia",
		"message0": "%{BKY_ROOBIN_SEARCH_WORD_IN_WIKI}",
		"style": "roobin_blocks",
		"args0": [
			{
				"type": "input_value",
				"name": "WIKI_INPUT",
				"check": "String"
			}
		],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "صفحه ی نوشته شده را در ویکیپدیا جست و جو میکند."
	},
	{
		"type": "roobin_today_date",
		"message0": "%{BKY_ROOBIN_TODAY_DATE}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "تاریخ و ساعت امروز را اعلام میکند."
	},
	{
		"type": "roobin_find_day",
		"message0": "%{BKY_ROOBIN_FIND_DAY}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "در جعبه ی باز شده تاریخ را بنویسید تا بفهمید چند شنبه بوده."
	},
	{
		"type": "roobin_riddle_game",
		"message0": "%{BKY_ROOBIN_RIDDLE_GAME}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین یک چیستان را می گوید و جوابش را میشنود."
	},
	{
		"type": "roobin_arrow_game",
		"message0": "%{BKY_ROOBIN_ARROW_GAME}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_SET_DIFFICULTY",
			"options": [
				["%{BKY_ROOBIN_DIFFICULTY_EASY}", 'آسان'],
				["%{BKY_ROOBIN_DIFFICULTY_MEDIUM}", 'متوسط'],
				["%{BKY_ROOBIN_DIFFICULTY_HARD}", 'سخت'],
				["%{BKY_ROOBIN_DIFFICULTY_IMPOSSIBLE}", 'غیر ممکن']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "بازی جهت ها را درسه سطح مختلف می توانید بازی کنید."
	},
	{
		"type": "roobin_pattern_game",
		"message0": "%{BKY_ROOBIN_PATTERN_GAME}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_SET_DIFFICULTY",
			"options": [
				["%{BKY_ROOBIN_DIFFICULTY_EASY}", 'آسان'],
				["%{BKY_ROOBIN_DIFFICULTY_MEDIUM}", 'متوسط'],
				["%{BKY_ROOBIN_DIFFICULTY_HARD}", 'سخت'],
				["%{BKY_ROOBIN_DIFFICULTY_IMPOSSIBLE}", 'غیر ممکن']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "بازی الگو ها را درسه سطح مختلف می توانید بازی کنید."
	},
	{
		"type": "roobin_numbers_game",
		"message0": "%{BKY_ROOBIN_NUMBERS_GAME}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_SET_DIFFICULTY",
			"options": [
				["%{BKY_ROOBIN_DIFFICULTY_LEVEL_ONE}", 'سطح 1'],
				["%{BKY_ROOBIN_DIFFICULTY_LEVEL_TWO}", 'سطح 2'],
				["%{BKY_ROOBIN_DIFFICULTY_LEVEL_THREE}", 'سطح 3']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "بازی دنباله ی اعداد ها را درسه سطح مختلف می توانید بازی کنید."
	},
	{
		"type": "roobin_amazing_facts",
		"message0": "%{BKY_ROOBIN_AMAZING_FACTS}",   //   Blockly.Msg['ROOBIN_AMAZING_FACTS'], -> not available yet!
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین یک آیا میدانستید برای شما تعریف میکند."
	},
	{
		"type": "roobin_games_explanation",
		"message0": "%{BKY_ROOBIN_GAMES_EXPLANATION}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_CHOOSE_GAME",
			"options": [
				["%{BKY_ROOBIN_GAMES_LIST_SEARCH_IN_WIKI}", 'جست و جو در ویکی پدیا'],
				["%{BKY_ROOBIN_GAMES_LIST_RIDDLE}", 'چیستان'],
				["%{BKY_ROOBIN_GAMES_LIST_ARROWS_GAME}", 'بازی جهت ها'],
				["%{BKY_ROOBIN_GAMES_LIST_PATTERNS_GAME}", 'الگوها آفلاین'],
				["%{BKY_ROOBIN_GAMES_LIST_NUMBERS_GAME}", 'دنباله اعداد']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین توضیحاتی درباره ی نحوه ی انجام بازی ها به شما می دهد."
	},
	{
		"type": "roobin_story_telling",
		"message0": "%{BKY_ROOBIN_STORY_TELLING}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_CHOOSE_STORY",
			"options": [
				["%{BKY_ROOBIN_STORY_LIST_NOSE}", 'دماغ'],
				["%{BKY_ROOBIN_STORY_LIST_MY_GLASSES}", 'عینکم'],
				["%{BKY_ROOBIN_STORY_LIST_ONE_BELOW}", 'یکی زیر یکی رو'],
				["%{BKY_ROOBIN_STORY_LIST_BOY_IN_DRUM}", 'پسری در طبل'],
				["%{BKY_ROOBIN_STORY_LIST_EMPERORS_CLOTHES}", 'لباس پادشاه'],
				["%{BKY_ROOBIN_STORY_LIST_ANIMAL_FARM_ONE}", 'قلعه حیوانات 1'],
				["%{BKY_ROOBIN_STORY_LIST_ANIMAL_FARM_TWO}", 'قلعه حیوانات 2'],
				["%{BKY_ROOBIN_STORY_LIST_ANIMAL_FARM_THREE}", 'قلعه حیوانات 3'],
				["%{BKY_ROOBIN_STORY_LIST_LITTLE_PRINCE_ONE}", 'شازده کوچولو 1'],
				["%{BKY_ROOBIN_STORY_LIST_LITTLE_PRINCE_TWO}", 'شازده کوچولو 2'],
				["%{BKY_ROOBIN_STORY_LIST_FINGERBOY}", 'پسرک بند انگشتی'],
				["%{BKY_ROOBIN_STORY_LIST_SNOWMAN}", 'آدم برفی'],
				["%{BKY_ROOBIN_STORY_LIST_CINDERELLA}", 'سیندرلا'],
				["%{BKY_ROOBIN_STORY_LIST_GULLIVER}", 'گالیور'],
				["%{BKY_ROOBIN_STORY_LIST_PUSS_IN_BOOTS}", 'گربه چکمه پوش'],
				["%{BKY_ROOBIN_STORY_LIST_JACK_AND_BEANSTALK}", 'جک و لوبیای سحرآمیز']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "یک داستان ضبط شده برای شما پخش می شود."
	},
	{
		"type": "roobin_move_motor",
		"message0": "%{BKY_ROOBIN_MOVE_MOTOR}",
		"style": "roobin_motor_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_NECK_HEAD",
			"options": [
				["%{BKY_ROOBIN_HEAD}", 'سر'],
				["%{BKY_ROOBIN_NECK}", 'گردن']
			]
		},
		{
			"type": "input_value",
			"name": "ROTATION_INPUT",
			"check": "Number"
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "موتور گردن یا سر روبین را به زاویه ای که می خواهید می برد."
	},
	{
		"type": "roobin_rotate_motor",
		"message0": "%{BKY_ROOBIN_ROTATE_MOTOR}",
		"style": "roobin_motor_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_NECK_HEAD",
			"options": [
				["%{BKY_ROOBIN_HEAD}", 'سر'],
				["%{BKY_ROOBIN_NECK}", 'گردن']
			]
		},
		{
			"type": "input_value",
			"name": "ROTATION_INPUT",
			"check": "Number"
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "موتور گردن یا سر روبین را به اندازه ای که می خواهید جابجا می کند."
	},
	{
		"type": "roobin_blink",
		"message0": "%{BKY_ROOBIN_BLINK}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین برای شما چشمک میزند."
	},
	{
		"type": "roobin_look_around",
		"message0": "%{BKY_ROOBIN_LOOK_AROUND}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین چشمهایش را به دور و بر میچرخاند و اطراف را نگاه می کند."
	},
	{
		"type": "roobin_neutral",
		"message0": "%{BKY_ROOBIN_LOOK_NEUTRAL}",
		"style": "roobin_blocks",
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "روبین چشمهایش را درشت می کند تا به روبرو نگاه کند."
	},
	{
		"type": "roobin_draw_on_eyes",
		"message0": "%{BKY_ROOBIN_DRAW_ON_EYES}",
		"style": "roobin_setup_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_LEFT_RIGHT",
			"options": [
				["%{BKY_ROOBIN_RIGHT}", 'راست'],
				["%{BKY_ROOBIN_LEFT}", 'چپ']
			]
		},
		{
			"type": "input_value",
			"name": "X",
			"check": "Number"
		},
		{
			"type": "input_value",
			"name": "Y",
			"check": "Number"
		},
		{
			"type": "field_dropdown",
			"name": "SEL_ON_OFF",
			"options": [
				["%{BKY_ROOBIN_ON}", 'روشن'],
				["%{BKY_ROOBIN_OFF}", 'خاموش']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "می توانید چشم راست و چپ روبین را در هر نقطه که میخواهید خاموش و روشن کنید."
	},
	{
		"type": "roobin_draw_on_mouth",
		"message0": "%{BKY_ROOBIN_DRAW_ON_MOUTH}",
		"style": "roobin_setup_blocks",
		"args0": [{
			"type": "input_value",
			"name": "X",
			"check": "Number"
		},
		{
			"type": "input_value",
			"name": "Y",
			"check": "Number"
		},
		{
			"type": "field_dropdown",
			"name": "SEL_ON_OFF",
			"options": [
				["%{BKY_ROOBIN_ON}", 'روشن'],
				["%{BKY_ROOBIN_OFF}", 'خاموش']
			]
		}],
		"previousStatement": null,
		"nextStatement": null
		// "tooltip": "می توانید دهان روبین را در هر نقطه که میخواهید خاموش و روشن کنید."
	},
	{
		"type": "roobin_clean_matrices",
		"message0": "%{BKY_ROOBIN_CLEAN_MATRICES}",
		"style": "roobin_setup_blocks",
		"args0": [{
			"type": "field_dropdown",
			"name": "SEL_PART",
			"options": [
				["%{BKY_ROOBIN_RIGHT_EYE}", 'چشم راست'],
				["%{BKY_ROOBIN_LEFT_EYE}", 'چشم چپ'],
				["%{BKY_ROOBIN_MOUTH}", 'دهان']
			]
		}],
		"previousStatement": null,
		"nextStatement": null,
		"tooltip": ""
	},
	{
		"type": "roobin_wait",
		"message0": "%{BKY_ROOBIN_WAIT}",
		"style": "roobin_blocks",
		"args0": [{
			"type": "input_value",
			"name": "WAIT_INPUT",
			"check": "Number"
		}],
		"previousStatement": null,
		"nextStatement": null,
		"tooltip": ""
	},
	{
		"type": "roobin_weather",
		"message0": "%{BKY_ROOBIN_WEATHER}",
		"style": "roobin_blocks",
		"args0": [{
		  "type": "input_value",
		  "name": "WEATHER_INPUT",
		  "check": "String"
		}],
		"output": "String"
	}
]);