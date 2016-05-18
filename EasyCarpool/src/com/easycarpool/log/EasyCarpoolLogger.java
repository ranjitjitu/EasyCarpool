package com.easycarpool.log;

public class EasyCarpoolLogger {
	private static IEasyCarpoolLogger logger = new EasyCarpoolLoggerImpl();

	public static IEasyCarpoolLogger getLogger() {
		return logger;
	}
}
