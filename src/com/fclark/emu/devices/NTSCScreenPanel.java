package com.fclark.emu.devices;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial") class NTSCScreenPanel extends JPanel {
	private BufferedImage blankScreen;
	private BufferedImage currentFrame;
	private InputStream videoSource;
	private byte[] lastFrameBytes;

	public NTSCScreenPanel(int width, int height, InputStream videoSource) {
		this.videoSource = videoSource;
		blankScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		currentFrame = blankScreen;
		currentFrame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	public Dimension getPreferredSize() {
		return new Dimension(currentFrame.getWidth(null), currentFrame.getHeight(null));
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		renderFrame(graphics);		
	}
	

	private void renderFrame(Graphics graphics) {
		try {
			currentFrame = ImageIO.read(videoSource);
			if(currentFrame != null) {
				copyLastFrame();
  			}
			else {
				currentFrame = ImageIO.read(new ByteArrayInputStream(lastFrameBytes));
			}
							
		} catch (IOException e) {
			currentFrame = blankScreen;
		}
		graphics.drawImage(currentFrame, 0, 0, this.getWidth(), this.getHeight(), null);
	}

	private void copyLastFrame() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(currentFrame, "png", outputStream);
		outputStream.flush();
		lastFrameBytes = outputStream.toByteArray();
		outputStream.close();
	}
	
}