package ru.sshelomentsev.grouplaunch.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "Testing";
	public static String denied = "denied";
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
