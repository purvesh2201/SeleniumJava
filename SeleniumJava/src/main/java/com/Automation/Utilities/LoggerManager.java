package com.Automation.Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {
	
	// This Method return logger instance of class 
	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger();
	}

}
