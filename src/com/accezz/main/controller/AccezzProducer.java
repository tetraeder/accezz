package com.accezz.main.controller;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.accezz.main.controller.input.readers.CommandExtractor;

public class AccezzProducer {

	public Queue<String> fetchTasks(File file) {
		Queue<String> commandQueue = new ConcurrentLinkedQueue<>();
		new Thread(new CommandExtractor(commandQueue, file)).start();
		return commandQueue;
	}
}
