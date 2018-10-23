package com.fclark.emu;

public interface ConsoleDevice extends EmulationDevice {
	void onGameMediaInserted(GameMedia gameMedia);
	void castToDisplay(DisplayDevice display);
}
