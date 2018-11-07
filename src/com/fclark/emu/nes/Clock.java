package com.fclark.emu.nes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fclark.emu.ClockCyclesSubscriber;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class Clock implements Runnable  {
	private static final long ONE_NANOSECOND = (long) Math.pow(10, 9);
	
	private final ScheduledExecutorService clockTicker = Executors.newSingleThreadScheduledExecutor();
	private List<ClockCyclesSubscriber> processorsList = new ArrayList<>();
	long tickCounter = 0;
	
	private long frequencyHertzs;
	private long periodInNanoseconds;
	
	private Clock(long frequencyHertzs) {
		this.frequencyHertzs = frequencyHertzs;
		this.periodInNanoseconds = Math.round(ONE_NANOSECOND/this.frequencyHertzs);
	}

	public void tick() {
		//TODO: remove tickCounter. It's only used to test visually the clock cycles
		tickCounter = ++tickCounter % ONE_NANOSECOND;
		System.out.print("ck " + tickCounter+ " | ");
		processorsList.parallelStream().forEach(processor -> processor.onClockCycle());
	}
	
	@Override
	public void run() {
		ScheduledFuture<?> task = clockTicker.scheduleAtFixedRate( () -> this.tick(), periodInNanoseconds, periodInNanoseconds, TimeUnit.NANOSECONDS);
		try {
			task.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}

	public Clock subscribe(ClockCyclesSubscriber subscriber) {
		processorsList.add(subscriber);	
		return this;
	}
	
	public static Clock of(long frequencyInHertzs) {
		return new Clock(frequencyInHertzs);
	}
}
