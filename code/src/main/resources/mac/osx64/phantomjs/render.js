var page = require('webpage').create();
var system = require('system');

var url = system.args[1];
var showConsole = system.args[2];

if (showConsole === "true") {
	page.onConsoleMessage = function(msg) {
		system.stdout.writeLine(msg +"<br>");
	};
	page.open(url, function (status) {
    	page.close();
		phantom.exit();
	});
} else {
	page.open(url, function (status) {
		console.log(page.content);
    	page.close();
		phantom.exit();
	});
}
