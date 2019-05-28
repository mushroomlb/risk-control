package com.ai.risk.analysis;

import java.util.concurrent.atomic.AtomicLong;

public class Test {
	public static void main(String[] args) {
		AtomicLong a = new AtomicLong(0L);
		a.addAndGet(1);
		a.addAndGet(2);
		a.addAndGet(3);
		System.out.println(a);
	}
}
