package com.fclark.emu;

public interface ProcessingUnit extends ClockCyclesSubscriber{
	void process();
	void onInit();
	void onReset();
}
