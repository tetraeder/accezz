package com.accezz.main.controller.input.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

import com.accezz.main.consts.AccezzConsts;
import com.accezz.main.entity.AccezzException;

public class CommandExtractor implements Runnable {

	private final Queue<String> commandQueue;
	private final File file;

	public CommandExtractor(Queue<String> commandQueue, File file) {
		this.commandQueue = commandQueue;
		this.file = file;
	}

	@Override
	/**
	 * Read the file. Each not empty line, insert add to the queue.
	 * When file is read completely, add AccezzConsts.COMMAND_FILE_COMPLETED.
	 * A consumer should understand the producer has finished processing when
	 * the consumer meets the AccezzConsts.COMMAND_FILE_COMPLETED in the queue
	 */
	public void run() {
		try {
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				String line = bufferedReader.readLine();
				while (line != null) {
					if (!line.trim().isEmpty()) {
						commandQueue.add(line);
					}
					line = bufferedReader.readLine();
				}
				commandQueue.add(AccezzConsts.COMMAND_FILE_COMPLETED);
			}

		} catch (IOException e) {
			throw new AccezzException(e);
		}
	}

}
