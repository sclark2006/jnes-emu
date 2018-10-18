package com.fclark.emu;

import java.nio.ByteBuffer;

public interface GameMedia {

	ByteBuffer[] getData();

	void load();

}
