package com.easycarpool.log;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyCarpoolLoggerImpl implements IEasyCarpoolLogger{
	
	private Logger logger= LoggerFactory.getLogger("SelfCareLogger");

	@Override
	public void info(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

	@Override
	public void error(String message) {
		if (logger.isErrorEnabled()) {
			logger.error(message);
		}
	}

	@Override
	public void debug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

	@Override
	public void warn(String message) {
		if (logger.isWarnEnabled()) {
			logger.warn(message);
		}
	}

	@Override
	public void log(Level level, String msg) {
		if(level.equals(Level.ERROR) && logger.isErrorEnabled()){
			logger.error(msg);
		}
		else if(level.equals(Level.FATAL) && logger.isErrorEnabled()){
			logger.error(msg);
			sendErrorMail(level, msg);
		}
		else if(level.equals(Level.WARN) && logger.isWarnEnabled()){
			logger.warn(msg);
		}
		else if(level.equals(Level.INFO) && logger.isInfoEnabled()){
			logger.info(msg);
		}
		else if(level.equals(Level.DEBUG) && logger.isDebugEnabled()){
			logger.debug(msg);
		}
	}

	@Override
	public void log(Level level, String className, String funcName, String msg) {
		String logMessage="";
		logMessage=className + "."+funcName+"  :  "+msg;
		log(level, logMessage);
	}

	@Override
	public void sendErrorMail(Level level, String msg) {
		// TODO Auto-generated method stub
		
	}

}
