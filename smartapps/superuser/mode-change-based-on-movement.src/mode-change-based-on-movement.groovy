/**
 *  Mode change based on Movement
 *
 *  Author: Chris Meyer
 *  Date: 2015-8-14
 */

// Automatically generated. Make future change here.
definition(
    name: "Mode Change based on Movement",
    namespace: "",
    author: "Chris Meyer",
    description: "Changes between two modes based on the state of a movement sensor",
    category: "Mode Magic",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)

preferences {
	section("Select sensor to monitor"){
		input "accelerationSensor", "capability.accelerationSensor"
	}
    section("Select the mode to change from"){
    	input "mode1", "mode", title: "Start Mode...", required: false
    }
    section("Select the mode to change to"){
    	input "mode2", "mode", title: "Movement Mode..."
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated(settings) {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
    initialize()
}

def accelerationActiveHandler(evt) {
	log.debug "Received on from $accelerationSensor"
    currentMode()
    accelerationSensor.active()
}
def currentMode(evt) {
    if(mode1 != null){
    	if(location.mode == mode1) {
    		modeChange()
        }
        else {
    		if(mode2 != null){
            	if(location.mode == mode2) {
    				modeRevert()
            	}
            	else {
            		log.debug "Current mode is $location.mode not $mode1 or $mode2"
                }
        	}
            else{
           		if(mode2 == null) {
        		modeRevert()}
            }
    	}
    }
    else{
    	if(mode1 == null) {
        	modeChange()}
	}
}

def modeChange() {
	setLocationMode(mode2)
    log.debug "Mode changed to $mode2"
}

def modeRevert() {
	setLocationMode(mode1)
    log.debug "Mode changed to $mode1"
}

def accelerationInactiveHandler(evt) {
	log.debug "No movement on $accelerationSensor, current mode $location.mode"
}

def initialize() {
	subscribe(accelerationSensor, "acceleration.active", accelerationActiveHandler)
    subscribe(location, "location.mode", currentMode)
    subscribe(accelerationSensor, "acceleration.inactive", accelerationInactiveHandler)
    log.debug "mode currently $location.mode"
    log.debug "mode1 = $mode1 mode2= $mode2"
}