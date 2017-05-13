package com.accezz.main.utils;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.policies.CountPolicy;
import org.pmw.tinylog.writers.RollingFileWriter;

public class FileUtils {
	public static void initLogFile(){
		Configurator.currentConfig()
		   .writer(new RollingFileWriter(ConfigurationLoader.getLogPath(), 100, 
				   new CountPolicy(ConfigurationLoader.getRolloutAfter())))
		   .activate();
		Configurator.currentConfig()
		   .formatPattern("{message}")
		   .activate();
	}
}
