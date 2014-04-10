package sshelomentsev.grouplauncher.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	public static String incorrectConfiguration;
	public static String undefinedConfiguration;
	private static String BUNDLE_NAME = "sshelomentsev.grouplauncher.ui.messages";
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
}
