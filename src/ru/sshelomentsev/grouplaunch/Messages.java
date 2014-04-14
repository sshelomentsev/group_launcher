package ru.sshelomentsev.grouplaunch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class Messages {
	public static CoreException message(String message) {
		return new CoreException(new Status(IStatus.ERROR, Plugin.PLUGIN_ID, message)); 
	}
	
	public static void fail(String message) throws CoreException {
		throw message(message);
	}
}
