package com.accezz.main.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AccezzController {
	public void request(File file) {
		AccezzProducer accezzProducer = new AccezzProducer();
		Queue<String> queue = accezzProducer.fetchTasks(file);
		dispatch(queue);
	}

	private void dispatch(Queue<String> queue) {
		final int threadPoolSize = 10;
		ExecutorService executorService = Executors
				.newFixedThreadPool(threadPoolSize);

		Collection<Callable<String>> tasks = new ArrayList<>();
		for (int i = 0; i < threadPoolSize; i++) {
			tasks.add(new AccezzConsumer(queue));
		}
		List<Future<String>> futures;
		try {
			futures = executorService.invokeAll(tasks);
			waitForTheFuture(futures);
		} catch (InterruptedException | ExecutionException e) {
			//TODO log
			e.printStackTrace();
		}

		executorService.shutdown();
	}

	private void waitForTheFuture(List<Future<String>> futures)
			throws InterruptedException, ExecutionException {
		for (Future<String> future : futures) {
			try {
				future.get(2L,TimeUnit.MINUTES);
			} catch (TimeoutException e) {
				//TODO log
				e.printStackTrace();
			}
		}
	}
}
