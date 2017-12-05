// This is the script used by the server when render-tempilate calls are made
var page = require('webpage').create();
var system = require('system');

var url = system.args[1];
var showConsole = system.args[2];

var maxMillisecondsPhantomCanBeOpen = 1 * 60 * 1000; // 1 minutes * 60 seconds in a minute * 1000 ms in a second

// phantom.outputEncoding = "utf8";


page.onError = function() { };

function closePage() {
    console.log('PhantomJsTimeout');
    page.close();
    phantom.exit();
}

if (showConsole === "true") {
    page.onConsoleMessage = function(msg) {
        system.stdout.writeLine(msg +"<br>");
    };
    page.onCallback = closePage;
    page.open(url);
} else {
    page.onCallback = function (status) {
        console.log(page.content);
        closePage();
    };
    page.open(url, function(status){
        if (status === 'fail') {
            // What does russell want to do when it fails?
	    console.log(status);
            closePage();
        }
    });
}

// Safeguard for page never calling the phantom callback, times out
setTimeout(closePage, maxMillisecondsPhantomCanBeOpen);

