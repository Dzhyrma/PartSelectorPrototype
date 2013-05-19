package com.techsoft.partselector.ui.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.techsoft.partselector.consts.Paths;
import com.techsoft.partselector.util.SimpleLogger;

public class ImageUtility {

	private static final Logger LOGGER = SimpleLogger.getLogger(ImageUtility.class);

	public static ImageIcon createImageIcon(String path) {
		File file = new File(Paths.IMAGES_PATH + path);
		if (!file.exists()) {
			LOGGER.log(Level.INFO, "Couldn't find file: " + path);
			return null;
		}
		URL imgURL;
		try {
			imgURL = file.toURI().toURL();
			if (imgURL != null)
				return new ImageIcon(imgURL);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.INFO, "URL exception: " + Paths.IMAGES_PATH + path);
			return null;
		}
		LOGGER.log(Level.INFO, "Couldn't find file: " + path);
		return null;
	}
	
	/*public static BufferedImage loadIcon(String path) {
		File file = new File(path);
		if (!file.exists()) {
			LOGGER.log(Level.INFO, "Couldn't find file: " + path);
			return null;
		}
		URL imgURL;
		try {
			imgURL = file.toURI().toURL();
			if (imgURL != null)
				return ImageIO.read(imgURL);
		} catch (IOException e) {
			LOGGER.log(Level.INFO, "URL exception: " + path);
			return null;
		}
		LOGGER.log(Level.INFO, "Couldn't find file: " + path);
		return null;
	}*/
}
