Blockly.JavaScript['TTS'] = function(block) {
	var text = Blockly.JavaScript.valueToCode(block, 'TTS_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '';
	query = 'begoo/';
	if(!text)
		return '';
	return 'if(' + text +')\nrequestServer(\'' + query + '\' + ' + text + ');\n';
};


var keyBoardEventDefinitionJson = {
	"type": "roobin_keyBoard_event",
	"message0": "%{BKY_ROOBIN_WHEN_KEY_PRESSED}",   //   Blockly.Msg['ROOBIN_SET_LANG'], -> not available yet!
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
};

Blockly.Blocks['roobin_keyBoard_event'] = {
	init: function() {
		this.jsonInit(keyBoardEventDefinitionJson);
	}
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


var setLangDefinitionJson = {
	"type": "roobin_set_lang",
	"message0": "%{BKY_ROOBIN_SET_LANG}",   //   Blockly.Msg['ROOBIN_SET_LANG'], -> not available yet!
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
};

Blockly.Blocks['roobin_set_lang'] = {
	init: function() {
		this.jsonInit(setLangDefinitionJson);
	}
};

Blockly.JavaScript['roobin_set_lang'] = function(block) {
	var lang = block.getFieldValue('SEL_ROOBIN_SET_LANG');
	query = 'set_language/' + lang;
	return 'requestServer(\'' + query + '\');\n';
};


var setSpeakSpeedDefinitionJson = {
	"type": "roobin_set_speak_speed",
	"message0": "%{BKY_ROOBIN_SET_SPEAK_SPEED}",   //   Blockly.Msg['ROOBIN_SET_SPEAK_SPEED'], -> not available yet!
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
};

Blockly.Blocks['roobin_set_speak_speed'] = {
	init: function() {
		this.jsonInit(setSpeakSpeedDefinitionJson);
	}
};

Blockly.JavaScript['roobin_set_speak_speed'] = function(block) {
	var arg = Blockly.JavaScript.valueToCode(block, 'SPEED',
		Blockly.JavaScript.ORDER_NONE) || '0';
	query = 'set_speak_speed/';
	return 'if(' + arg +' || ' + arg + ' === 0)requestServer(\'' + query + '\' + ' + arg + ');\n';
};


var setSpeakPitchDefinitionJson = {
	"type": "roobin_set_speak_pitch",
	"message0": "%{BKY_ROOBIN_SET_SPEAK_PITCH}",   //   Blockly.Msg['ROOBIN_SET_SPEAK_PITCH'], -> not available yet!
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
};

Blockly.Blocks['roobin_set_speak_pitch'] = {
	init: function() {
		this.jsonInit(setSpeakPitchDefinitionJson);
	}
};

Blockly.JavaScript['roobin_set_speak_pitch'] = function(block) {
	var arg = Blockly.JavaScript.valueToCode(block, 'PITCH',
		Blockly.JavaScript.ORDER_NONE) || '0';
	query = 'set_speak_pitch/';
	return 'if(' + arg +' || ' + arg + ' === 0)requestServer(\'' + query + '\' + ' + arg +');\n';
};


var changeSpeakPitchDefinitionJson = {
	"type": "roobin_change_speak_pitch",
	"message0": "%{BKY_ROOBIN_CHANGE_SPEAK_PITCH}",   //   Blockly.Msg['ROOBIN_CHANGE_SPEAK_PITCH'], -> not available yet!
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
};

Blockly.Blocks['roobin_change_speak_pitch'] = {
	init: function() {
		this.jsonInit(changeSpeakPitchDefinitionJson);
	}
};

Blockly.JavaScript['roobin_change_speak_pitch'] = function(block) {
	var arg = Blockly.JavaScript.valueToCode(block, 'PITCH',
		Blockly.JavaScript.ORDER_NONE) || '0';
	query = 'change_speak_pitch/';
	return 'if(' + arg +' || ' + arg + ' === 0)requestServer(\'' + query + '\' + ' + arg +');\n';
};


var setSttVarDefinitionJson = {
	"type": "roobin_set_stt_var",
	"message0": "%{BKY_ROOBIN_SET_STT_VAR}",   //   Blockly.Msg['ROOBIN_SET_STT_VAR'], -> not available yet!
	"style": "roobin_blocks",
	"output": "String"
	// "tooltip": "صوت شما را به متن تبدیل می کند و آن را برمی گرداند."
};

Blockly.Blocks['roobin_set_stt_var'] = {
	init: function() {
		this.jsonInit(setSttVarDefinitionJson);
	}
};

Blockly.JavaScript['roobin_set_stt_var'] = function(block) {
	query = 'set_stt_var';
	var code = 'requestServer(\'' + query + '\')'
	return [code, Blockly.JavaScript.ORDER_NONE];
};


var changeEyeDefinitionJson = {
	"type": "roobin_change_eye",
	"message0": "%{BKY_ROOBIN_CHANGE_EYE}",   //   Blockly.Msg['ROOBIN_CHANGE_EYE'], -> not available yet!
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
};

Blockly.Blocks['roobin_change_eye'] = {
	init: function() {
		this.jsonInit(changeEyeDefinitionJson);
	}
};

Blockly.JavaScript['roobin_change_eye'] = function(block) {
	var dir = block.getFieldValue('SEL_LEFT_RIGHT_EYE');
	var category = block.getFieldValue('SEL_EYE_CATEGORY');
	query = 'change_eye/' + dir + '/' + category;
	return 'requestServer(\'' + query + '\');\n';
};



var changeMouthDefinitionJson = {
	"type": "roobin_change_mouth",
	"message0": "%{BKY_ROOBIN_CHANGE_MOUTH}",   //   Blockly.Msg['ROOBIN_CHANGE_MOUTH'], -> not available yet!
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
};

Blockly.Blocks['roobin_change_mouth'] = {
	init: function() {
		this.jsonInit(changeMouthDefinitionJson);
	}
};

Blockly.JavaScript['roobin_change_mouth'] = function(block) {
	var mouthForm = block.getFieldValue('SEL_MOUTH_FORM');
	query = 'change_mouth/' + mouthForm;
	return 'requestServer(\'' + query + '\');\n';
};


var randomGenDefinitionJson = {
	"type": "roobin_random_gen",
	"message0": "%{BKY_ROOBIN_RANDOM_GEN}",   //   Blockly.Msg['ROOBIN_RANDOM_GEN'], -> not available yet!
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
};

Blockly.Blocks['roobin_random_gen'] = {
	init: function() {
		this.jsonInit(randomGenDefinitionJson);
	}
};

Blockly.JavaScript['roobin_random_gen'] = function(block) {
	var min = Blockly.JavaScript.valueToCode(block, 'LOWER_BOUND',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var max = Blockly.JavaScript.valueToCode(block, 'UPPER_BOUND',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var code = 'Math.floor(Math.random() * (' + max + ' - ' + min + ') + ' + min + ')';
	return [code, Blockly.JavaScript.ORDER_NONE];
};


var recoveryDefinitionJson = {
	"type": "roobin_recovery",
	"message0": "%{BKY_ROOBIN_RECOVERY}",   //   Blockly.Msg['ROOBIN_RECOVERY'], -> not available yet!
	"style": "roobin_setup_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "روبین را ریستارت می کند."
};

Blockly.Blocks['roobin_recovery'] = {
	init: function() {
		this.jsonInit(recoveryDefinitionJson);
	}
};

Blockly.JavaScript['roobin_recovery'] = function(block) {
	query = 'recovery';
	return 'requestServer(\'' + query + '\');\n';
};


var introduceDefinitionJson = {
	"type": "roobin_introduce",
	"message0": "%{BKY_ROOBIN_INTRODUCE}",   //   Blockly.Msg['ROOBIN_INTRODUCE'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "روبین را معرفی می کند."
};

Blockly.Blocks['roobin_introduce'] = {
	init: function() {
		this.jsonInit(introduceDefinitionJson);
	}
};

Blockly.JavaScript['roobin_introduce'] = function(block) {
	query = 'introduce';
	return 'requestServer(\'' + query + '\');\n';
};


var sayHelloDefinitionJson = {
	"type": "roobin_say_hello",
	"message0": "%{BKY_ROOBIN_SAY_HELLO}",   //   Blockly.Msg['ROOBIN_SAY_HELLO'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "به روبین سلام کنید تا جوابتان را بدهد."
};

Blockly.Blocks['roobin_say_hello'] = {
	init: function() {
		this.jsonInit(sayHelloDefinitionJson);
	}
};

Blockly.JavaScript['roobin_say_hello'] = function(block) {
	query = 'say_hello';
	return 'requestServer(\'' + query + '\');\n';
};


var chuckleDefinitionJson = {
	"type": "roobin_chuckle",
	"message0": "%{BKY_ROOBIN_CHUCKLE}",   //   Blockly.Msg['ROOBIN_CHUCKLE'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "رو این دکمه بزنید تا روبین رو خوشحال کنید."
};

Blockly.Blocks['roobin_chuckle'] = {
	init: function() {
		this.jsonInit(chuckleDefinitionJson);
	}
};

Blockly.JavaScript['roobin_chuckle'] = function(block) {
	query = 'chuckle';
	return 'requestServer(\'' + query + '\');\n';
};



var askNwaitDefinitionJson = {
	"type": "roobin_ask_wait",
	"message0": "%{BKY_ROOBIN_ASK_WAIT}",   //   Blockly.Msg['ROOBIN_ASK_WAIT'], -> not available yet!
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
};

Blockly.Blocks['roobin_ask_wait'] = {
	init: function() {
		this.jsonInit(askNwaitDefinitionJson);
	}
};

Blockly.JavaScript['roobin_ask_wait'] = function(block) {
	var text = Blockly.JavaScript.valueToCode(block, 'ASK_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '';
	query = 'askNwait/';
	if(!text)
		return '';
	return 'if(' + text +')\nrequestServer(\'' + query + '\' + ' + text + ');\n';
};



var searchInWikipediaDefinitionJson = {
	"type": "roobin_search_in_wikipedia",
	"message0": "%{BKY_ROOBIN_SEARCH_IN_WIKI}",   //   Blockly.Msg['ROOBIN_SEARCH_IN_WIKI'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "صدای شما را می شنود و صفحه ای که گفتید را در ویکیپدیا جستجو می کند."
};

Blockly.Blocks['roobin_search_in_wikipedia'] = {
	init: function() {
		this.jsonInit(searchInWikipediaDefinitionJson);
	}
};

Blockly.JavaScript['roobin_search_in_wikipedia'] = function(block) {
	query = 'search_in_wikipedia';
	return 'requestServer(\'' + query + '\');\n';
};


var searchWordInWikipediaDefinitionJson = {
	"type": "roobin_search_word_in_wikipedia",
	"message0": "%{BKY_ROOBIN_SEARCH_WORD_IN_WIKI}",   //   Blockly.Msg['ROOBIN_SEARCH_WORD_IN_WIKI'], -> not available yet!
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
};

Blockly.Blocks['roobin_search_word_in_wikipedia'] = {
	init: function() {
		this.jsonInit(searchWordInWikipediaDefinitionJson);
	}
};

Blockly.JavaScript['roobin_search_word_in_wikipedia'] = function(block) {
	var text = Blockly.JavaScript.valueToCode(block, 'WIKI_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '';
	query = 'search_sth_in_wikipedia/';
	if(!text)
		return '';
	return 'if(' + text +')\nrequestServer(\'' + query + '\' + ' + text + ');\n';
};


var todayDateDefinitionJson = {
	"type": "roobin_today_date",
	"message0": "%{BKY_ROOBIN_TODAY_DATE}",   //   Blockly.Msg['ROOBIN_TODAY_DATE'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "تاریخ و ساعت امروز را اعلام میکند."
};

Blockly.Blocks['roobin_today_date'] = {
	init: function() {
		this.jsonInit(todayDateDefinitionJson);
	}
};

Blockly.JavaScript['roobin_today_date'] = function(block) {
	query = 'today';
	return 'requestServer(\'' + query + '\');\n';
};


var findDayDefinitionJson = {
	"type": "roobin_find_day",
	"message0": "%{BKY_ROOBIN_FIND_DAY}",   //   Blockly.Msg['ROOBIN_FIND_DAY'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "در جعبه ی باز شده تاریخ را بنویسید تا بفهمید چند شنبه بوده."
};

Blockly.Blocks['roobin_find_day'] = {
	init: function() {
		this.jsonInit(findDayDefinitionJson);
	}
};

Blockly.JavaScript['roobin_find_day'] = function(block) {
	query = 'chan_shanbeh';
	return 'requestServer(\'' + query + '\');\n';
};


var riddleGameDefinitionJson = {
	"type": "roobin_riddle_game",
	"message0": "%{BKY_ROOBIN_RIDDLE_GAME}",   //   Blockly.Msg['ROOBIN_RIDDLE_GAME'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "روبین یک چیستان را می گوید و جوابش را میشنود."
};

Blockly.Blocks['roobin_riddle_game'] = {
	init: function() {
		this.jsonInit(riddleGameDefinitionJson);
	}
};

Blockly.JavaScript['roobin_riddle_game'] = function(block) {
	query = 'riddle_game';
	return 'requestServer(\'' + query + '\');\n';
};


var arrowGameDefinitionJson = {
	"type": "roobin_arrow_game",
	"message0": "%{BKY_ROOBIN_ARROW_GAME}",   //   Blockly.Msg['ROOBIN_ARROW_GAME'], -> not available yet!
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
};

Blockly.Blocks['roobin_arrow_game'] = {
	init: function() {
		this.jsonInit(arrowGameDefinitionJson);
	}
};

Blockly.JavaScript['roobin_arrow_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	query = 'arrow_game/' + difficulty;
	return 'requestServer(\'' + query + '\');\n';
};


var patternGameDefinitionJson = {
	"type": "roobin_pattern_game",
	"message0": "%{BKY_ROOBIN_PATTERN_GAME}",   //   Blockly.Msg['ROOBIN_PATTERN_GAME'], -> not available yet!
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
};

Blockly.Blocks['roobin_pattern_game'] = {
	init: function() {
		this.jsonInit(patternGameDefinitionJson);
	}
};

Blockly.JavaScript['roobin_pattern_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	query = 'repeating_pattern_game2/' + difficulty;
	return 'requestServer(\'' + query + '\');\n';
};


var numbersGameDefinitionJson = {
	"type": "roobin_numbers_game",
	"message0": "%{BKY_ROOBIN_NUMBERS_GAME}",   //   Blockly.Msg['ROOBIN_NUMBERS_GAME'], -> not available yet!
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
};

Blockly.Blocks['roobin_numbers_game'] = {
	init: function() {
		this.jsonInit(numbersGameDefinitionJson);
	}
};

Blockly.JavaScript['roobin_numbers_game'] = function(block) {
	var difficulty = block.getFieldValue('SEL_SET_DIFFICULTY');
	query = 'number_series/' + difficulty;
	return 'requestServer(\'' + query + '\');\n';
};


var amazingFactsDefinitionJson = {
	"type": "roobin_amazing_facts",
	"message0": "%{BKY_ROOBIN_AMAZING_FACTS}",   //   Blockly.Msg['ROOBIN_AMAZING_FACTS'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "روبین یک آیا میدانستید برای شما تعریف میکند."
};

Blockly.Blocks['roobin_amazing_facts'] = {
	init: function() {
		this.jsonInit(amazingFactsDefinitionJson);
	}
};

Blockly.JavaScript['roobin_amazing_facts'] = function(block) {
	query = 'amazing_facts';
	return 'requestServer(\'' + query + '\');\n';
};


var gamesExplanationDefinitionJson = {
	"type": "roobin_games_explanation",
	"message0": "%{BKY_ROOBIN_GAMES_EXPLANATION}",   //   Blockly.Msg['ROOBIN_GAMES_EXPLANATION'], -> not available yet!
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
};

Blockly.Blocks['roobin_games_explanation'] = {
	init: function() {
		this.jsonInit(gamesExplanationDefinitionJson);
	}
};

Blockly.JavaScript['roobin_games_explanation'] = function(block) {
	var game = block.getFieldValue('SEL_CHOOSE_GAME');
	query = 'games_explanation/' + game;
	return 'requestServer(\'' + query + '\');\n';
};


var storyTellingDefinitionJson = {
	"type": "roobin_story_telling",
	"message0": "%{BKY_ROOBIN_STORY_TELLING}",   //   Blockly.Msg['ROOBIN_STORY_TELLING'], -> not available yet!
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
};

Blockly.Blocks['roobin_story_telling'] = {
	init: function() {
		this.jsonInit(storyTellingDefinitionJson);
	}
};

Blockly.JavaScript['roobin_story_telling'] = function(block) {
	var story = block.getFieldValue('SEL_CHOOSE_STORY');
	query = 'story_telling/' + story;
	return 'requestServer(\'' + query + '\');\n';
};


var moveMotorDefinitionJson = {
	"type": "roobin_move_motor",
	"message0": "%{BKY_ROOBIN_MOVE_MOTOR}",   //   Blockly.Msg['ROOBIN_MOVE_MOTOR'], -> not available yet!
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
};

Blockly.Blocks['roobin_move_motor'] = {
	init: function() {
		this.jsonInit(moveMotorDefinitionJson);
	}
};

Blockly.JavaScript['roobin_move_motor'] = function(block) {
	var headOrNeck = block.getFieldValue('SEL_NECK_HEAD');
	var arg = Blockly.JavaScript.valueToCode(block, 'ROTATION_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '0';
	query = 'move_motor/' + headOrNeck + '/';
	return 'if(' + arg +' || ' + arg + ' === 0)\nrequestServer(\'' + query + '\' + ' + arg +');\n';
};


var rotateMotorDefinitionJson = {
	"type": "roobin_rotate_motor",
	"message0": "%{BKY_ROOBIN_ROTATE_MOTOR}",   //   Blockly.Msg['ROOBIN_ROTATE_MOTOR'], -> not available yet!
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
};

Blockly.Blocks['roobin_rotate_motor'] = {
	init: function() {
		this.jsonInit(rotateMotorDefinitionJson);
	}
};

Blockly.JavaScript['roobin_rotate_motor'] = function(block) {
	var headOrNeck = block.getFieldValue('SEL_NECK_HEAD');
	var arg = Blockly.JavaScript.valueToCode(block, 'ROTATION_INPUT',
		Blockly.JavaScript.ORDER_NONE) || '0';
	query = 'move_motor_droplist/' + headOrNeck + '/';
	return 'if(' + arg +' || ' + arg + ' === 0)\nrequestServer(\'' + query + '\' + ' + arg +');\n';
};


var blinkDefinitionJson = {
	"type": "roobin_blink",
	"message0": "%{BKY_ROOBIN_BLINK}",   //   Blockly.Msg['ROOBIN_BLINK'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "روبین برای شما چشمک میزند."
};

Blockly.Blocks['roobin_blink'] = {
	init: function() {
		this.jsonInit(blinkDefinitionJson);
	}
};

Blockly.JavaScript['roobin_blink'] = function(block) {
	query = 'roobinBlink';
	return 'requestServer(\'' + query + '\');\n';
};


var roobinLookAroundDefinitionJson = {
	"type": "roobin_look_around",
	"message0": "%{BKY_ROOBIN_LOOK_AROUND}",   //   Blockly.Msg['ROOBIN_LOOK_AROUND'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "روبین چشمهایش را به دور و بر میچرخاند و اطراف را نگاه می کند."
};

Blockly.Blocks['roobin_look_around'] = {
	init: function() {
		this.jsonInit(roobinLookAroundDefinitionJson);
	}
};

Blockly.JavaScript['roobin_look_around'] = function(block) {
	query = 'roobinLookSides';
	return 'requestServer(\'' + query + '\');\n';
};


var roobinNeutralDefinitionJson = {
	"type": "roobin_neutral",
	"message0": "%{BKY_ROOBIN_LOOK_NEUTRAL}",   //   Blockly.Msg['ROOBIN_LOOK_NEUTRAL'], -> not available yet!
	"style": "roobin_blocks",
	"previousStatement": null,
	"nextStatement": null
	// "tooltip": "روبین چشمهایش را درشت می کند تا به روبرو نگاه کند."
};

Blockly.Blocks['roobin_neutral'] = {
	init: function() {
		this.jsonInit(roobinNeutralDefinitionJson);
	}
};

Blockly.JavaScript['roobin_neutral'] = function(block) {
	query = 'roobinNeutral';
	return 'requestServer(\'' + query + '\');\n';
};


var roobinDrawOnEyesDefinitionJson = {
	"type": "roobin_draw_on_eyes",
	"message0": "%{BKY_ROOBIN_DRAW_ON_EYES}",   //   Blockly.Msg['ROOBIN_DRAW_ON_EYES'], -> not available yet!
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
};

Blockly.Blocks['roobin_draw_on_eyes'] = {
	init: function() {
		this.jsonInit(roobinDrawOnEyesDefinitionJson);
	}
};

Blockly.JavaScript['roobin_draw_on_eyes'] = function(block) {
	var leftOrRight = block.getFieldValue('SEL_LEFT_RIGHT');
	var onOrOff = block.getFieldValue('SEL_ON_OFF');
	var arg1 = Blockly.JavaScript.valueToCode(block, 'X',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var arg2 = Blockly.JavaScript.valueToCode(block, 'Y',
		Blockly.JavaScript.ORDER_NONE) || '0';
	query = 'draw_on_eyes/' + leftOrRight;
	return 'if((' + arg1 +' || ' + arg1 + ' === 0) && (' + arg2 +' || ' + arg2 + ' === 0))\nrequestServer(\'' + query + '/\' + ' + arg1 + ' + \'/\' +' + arg2 + ' + \'/\' + \'' + onOrOff + '\');\n';
};


var roobinDrawOnMouthDefinitionJson = {
	"type": "roobin_draw_on_mouth",
	"message0": "%{BKY_ROOBIN_DRAW_ON_MOUTH}",   //   Blockly.Msg['ROOBIN_DRAW_ON_MOUTH'], -> not available yet!
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
};

Blockly.Blocks['roobin_draw_on_mouth'] = {
	init: function() {
		this.jsonInit(roobinDrawOnMouthDefinitionJson);
	}
};

Blockly.JavaScript['roobin_draw_on_mouth'] = function(block) {
	var onOrOff = block.getFieldValue('SEL_ON_OFF');
	var arg1 = Blockly.JavaScript.valueToCode(block, 'X',
		Blockly.JavaScript.ORDER_NONE) || '0';
	var arg2 = Blockly.JavaScript.valueToCode(block, 'Y',
		Blockly.JavaScript.ORDER_NONE) || '0';
	query = 'draw_on_mouth/';
	return 'if((' + arg1 +' || ' + arg1 + ' === 0) && (' + arg2 +' || ' + arg2 + ' === 0))\nrequestServer(\'' + query + '\' + ' + arg1 + ' + \'/\' +' + arg2 + ' + \'/\' + \'' + onOrOff + '\');\n';
};


var roobinCleanMatricesDefinitionJson = {
	"type": "roobin_clean_matrices",
	"message0": "%{BKY_ROOBIN_CLEAN_MATRICES}",   //   Blockly.Msg['ROOBIN_CLEAN_MATRICES'], -> not available yet!
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
};

Blockly.Blocks['roobin_clean_matrices'] = {
	init: function() {
		this.jsonInit(roobinCleanMatricesDefinitionJson);
	}
};

Blockly.JavaScript['roobin_clean_matrices'] = function(block) {
	var selection = block.getFieldValue('SEL_PART');
	query = 'clean_the_matrices/' + selection;
	return 'requestServer(\'' + query + '\');\n';
};