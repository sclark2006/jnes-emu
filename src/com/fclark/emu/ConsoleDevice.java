package com.fclark.emu;

public interface ConsoleDevice extends EmulationDevice {
	void onGameSelected();
	void castToDisplay(DisplayDevice display);
}
