package com.example.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestBug {
	private final ExecutorService pool = Executors.newFixedThreadPool(1);

	public static void main(String[] args) {
		TestBug testBug = new TestBug();
		for (int i = 0; i < 3; i++) {
			testBug.doSomething();
		}
	}

	public void doSomething() {
		pool.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("doing something");
			}
		});
	}
}
