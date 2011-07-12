import gda.images.camera.ImageListener;
import gda.images.camera.MotionJpegOverHttpReceiver;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import org.embl.BaseException;
import org.embl.data.Logger;
import org.embl.imaging.ImageSource.TimerUpdatedImageSource;

public class GdaMjpegImageSource extends TimerUpdatedImageSource implements ImageListener<Image> {
	
	// A new source object is created every time the source is selected in the HC GUI
	
	private static final String JAVA_NET_USE_SYSTEM_PROXIES = "java.net.useSystemProxies";
	
	private MotionJpegOverHttpReceiver receiver;
	
	private Properties properties;
	
	// Called every time the image source is selected
	protected void onInit() throws BaseException {
		// NB cannot get the interval here. It's set after onInit is called
		
		log("Initialising image source");
		
		log("Creating video receiver...");
		receiver = new MotionJpegOverHttpReceiver();
		receiver.addImageListener(this);
		
		log("Loading properties...");
		properties = new Properties();
		final String propertiesFilename = getClass().getSimpleName() + ".properties";
		final URL propertiesUrl = GdaMjpegImageSource.class.getResource(propertiesFilename);
		if (propertiesUrl == null) {
			throw new BaseException("Could not load image source properties - " + propertiesFilename + " not found");
		}
		try {
			properties.load(propertiesUrl.openStream());
		} catch (Exception e) {
			throw new BaseException("Could not load image source properties", e);
		}
		
		// Work out which source to use
		log(String.format("source = \"%s\", parameters = \"%s\"", getSourceName(), getSourcePars()));
		final Set<String> propertyNamesInOrder = new TreeSet<String>(properties.stringPropertyNames());
		log("Valid source names are: " + propertyNamesInOrder);
		final String source = getSourceName();
		if (!properties.containsKey(source)) {
			throw new BaseException("Unrecognised source \"" + source + "\". Valid sources are: " + propertyNamesInOrder);
		}
		final String sourceUrl = properties.getProperty(source);
		
		// Try parsing the URL to see if it's valid
		try {
			new URL(sourceUrl);
		} catch (MalformedURLException e) {
			throw new BaseException("Video URL does not seem to be valid. Check the properties file (" + propertiesFilename + ").");
		}
		
		// The org.jdesktop.application.Application class in appframework sets
		// the useSystemProxies property to true. This causes problems when
		// connecting to the video source: URLConnection.getInputStream()
		// (called in FrameCaptureTask) hangs. For more information, see:
		// http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
		System.setProperty(JAVA_NET_USE_SYSTEM_PROXIES, Boolean.FALSE.toString());
		
		log("Connecting to " + sourceUrl);
		receiver.setUrl(sourceUrl);
		receiver.createConnection();
		log("Video receiver started");
	}
	
	private volatile BufferedImage image;
	
	@Override
	public void processImage(Image image) {
		this.image = (BufferedImage) image;
	}
	
	protected void onTimer() throws BaseException {
		// Called once when the source is initialised, then at regular intervals after that
		if (image != null) {
			pushNewImage(image);
		}
	}
	
	@Override
	public void dispose() {
		// Overriding this method seems to be the only way that we can detect when the image
		// source is no longer being used. There's no 'onStop' or similar method
		log("Disposing image source");
		receiver.closeConnection();
		super.dispose();
	}
	
	@Override
	public void log(Object origin, String description, Level level) {
		// Override so messages go to the GUI and to the console
		super.log(origin, description, level);
		Logger.logToStdout(origin, description, level);
	}
	
	@Override
	public void log(Object origin, Exception ex, Level level) {
		// Override so messages go to the GUI and to the console
		super.log(origin, ex, level);
		Logger.logToStdout(origin, ex, level);
	}
	
	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

}
