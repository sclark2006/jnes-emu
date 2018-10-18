package com.fclark.emu;

import com.fclark.emu.devices.NESConsole;
import com.fclark.emu.devices.NTSCScreen;

public class NESEmulator {

	public static void main(String[] args) {
		DisplayDevice screen = new NTSCScreen();
		ConsoleDevice nes = new NESConsole();
		nes.castToDisplay(screen);
		screen.powerOn();
		nes.powerOn();
	}

}
