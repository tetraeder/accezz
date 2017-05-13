package com.accezz.main.controller;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Callable;

import com.accezz.main.actions.IAction;
import com.accezz.main.consts.AccezzConsts;

public class AccezzConsumer implements Callable<String> {
	Queue<String> commandQueue;

	public AccezzConsumer(Queue<String> commandQueue) {
		this.commandQueue = commandQueue;
	}

	@Override
	public String call() throws Exception {
		while (!AccezzConsts.COMMAND_FILE_COMPLETED.equals(commandQueue.peek())) {
			try {
				String command = commandQueue.remove();
				logProccessing(command);
				if (AccezzConsts.COMMAND_FILE_COMPLETED.equals(command)) {
					commandQueue.add(AccezzConsts.COMMAND_FILE_COMPLETED);
					break;
				}
				IAction action = ActionFactory.getAction(command);
				action.perform(command);
			} catch (NoSuchElementException e) {
				// The queue is empty, don't care
			}
		}

		return AccezzConsts.COMMAND_FILE_COMPLETED;
	}

	private void logProccessing(String command) {
		if (AccezzConsts.COMMAND_FILE_COMPLETED.equals(command)) {
			return;
		}
		System.out.println("Proccessing: " + command);
	}

}
