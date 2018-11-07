package com.fclark.emu;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public interface DisplayDevice extends EmulationDevice {
	void setVideoSource(PipedOutputStream stream);
	PipedInputStream getVideoInput();
}
