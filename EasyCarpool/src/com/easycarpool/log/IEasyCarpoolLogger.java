package com.easycarpool.log;

import org.apache.log4j.Level;

public interface IEasyCarpoolLogger {

	public void info(String message);
	public void error(String message);
	public void debug(String message);
	public void warn(String message);
	public void log(Level level, String msg);
	public void log(Level level, String className, String funcName, String msg);
	public void sendErrorMail(Level level, String msg);
	
}
