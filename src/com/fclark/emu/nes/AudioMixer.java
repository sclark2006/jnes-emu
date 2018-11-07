package com.fclark.emu.nes;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/***
 * Based on code from StdAudio class, created by Robert Sedgewick.
 * https://introcs.cs.princeton.edu/java/stdlib/StdAudio.java.html
 * 
 * @author sclark
 * @since Nov 7, 2018
 *
 */
public class AudioMixer {

	/**
	 * The sample rate - 44,100 Hz for CD quality audio.
	 */
	public static final int SAMPLE_RATE = 44100;

	private static final int BYTES_PER_SAMPLE = 2; // 16-bit audio
	private static final int BITS_PER_SAMPLE = 16; // 16-bit audio
	private static final double MAX_16_BIT = Short.MAX_VALUE; // 32,767
	private static final int SAMPLE_BUFFER_SIZE = 4096;

	private static SourceDataLine line; // to play the sound
	private static byte[] buffer; // our internal buffer
	private static int bufferSize = 0; // number of samples currently in internal buffer

	public static void main(String[] args) {
		  // 440 Hz for 1 sec
        double freq = 440.0;
        for (int i = 0; i <= SAMPLE_RATE; i++) {
            play(0.5 * Math.sin(2*Math.PI * freq * i / SAMPLE_RATE));
        }
        
        // scale increments
        int[] steps = { 0, 2, 4, 5, 7, 9, 11, 12 };
        for (int i = 0; i < steps.length; i++) {
            double hz = 440.0 * Math.pow(2, steps[i] / 12.0);
            play(note(hz, 1.0, 0.5));
        }


        // need to call this in non-interactive stuff so the program doesn't terminate
        // until all the sound leaves the speaker.
        close(); 
	}

	// static initializer
	static {
		init();
	}

	// open up an audio stream
	private static void init() {
		try {
			// 44,100 samples per second, 16-bit audio, mono, signed PCM, little Endian
			AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);

			// the internal buffer is a fraction of the actual buffer size, this choice is
			// arbitrary
			// it gets divided because we can't expect the buffered data to line up exactly
			// with when
			// the sound card decides to push out its samples.
			buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3];
		} catch (LineUnavailableException e) {
			System.out.println(e.getMessage());
		}

		// no sound gets made before this call
		line.start();
	}

	/**
	 * Closes standard audio.
	 */
	public static void close() {
		line.drain();
		line.stop();
	}

	
	  // create a note (sine wave) of the given frequency (Hz), for the given
    // duration (seconds) scaled to the given volume (amplitude)
    private static double[] note(double hz, double duration, double amplitude) {
        int n = (int) (SAMPLE_RATE * duration);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++)
            a[i] = amplitude * Math.sin(2 * Math.PI * i * hz / SAMPLE_RATE);
        return a;
    }
    
	/**
	 * Writes one sample (between -1.0 and +1.0) to standard audio. If the sample is
	 * outside the range, it will be clipped.
	 *
	 * @param sample
	 *            the sample to play
	 * @throws IllegalArgumentException
	 *             if the sample is {@code Double.NaN}
	 */
	public static void play(double sample) {

		// clip if outside [-1, +1]
		if (Double.isNaN(sample))
			throw new IllegalArgumentException("sample is NaN");
		if (sample < -1.0)
			sample = -1.0;
		if (sample > +1.0)
			sample = +1.0;

		// convert to bytes
		short s = (short) (MAX_16_BIT * sample);
		buffer[bufferSize++] = (byte) s;
		buffer[bufferSize++] = (byte) (s >> 8); // little Endian

		// send to sound card if buffer is full
		if (bufferSize >= buffer.length) {
			line.write(buffer, 0, buffer.length);
			bufferSize = 0;
		}
	}

	/**
	 * Writes the array of samples (between -1.0 and +1.0) to standard audio. If a
	 * sample is outside the range, it will be clipped.
	 *
	 * @param samples
	 *            the array of samples to play
	 * @throws IllegalArgumentException
	 *             if any sample is {@code Double.NaN}
	 * @throws IllegalArgumentException
	 *             if {@code samples} is {@code null}
	 */
	public static void play(double[] samples) {
		if (samples == null)
			throw new IllegalArgumentException("argument to play() is null");
		for (int i = 0; i < samples.length; i++) {
			play(samples[i]);
		}
	}

}
