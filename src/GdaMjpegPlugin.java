import java.util.logging.Level;

import org.embl.BaseException;
import org.embl.ctrl.ControllerPlugin;
import org.embl.data.Logger;
import org.embl.imaging.ImageSource;

public class GdaMjpegPlugin extends ControllerPlugin {

	@Override
	protected void onStart() throws BaseException {
		log("Starting plugin");
		ImageSource.addKnownType(GdaMjpegImageSource.class);
	}
	
	@Override
	protected void onStop() throws BaseException {
		log("Stopping plugin");
	}
	
	protected void log(String description) {
		final String origin = getClass().getSimpleName();
		getController().log(origin, description, Level.INFO);
		Logger.trace(origin, description);
	}
	
}
