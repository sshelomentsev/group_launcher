package ru.sshelomentsev.grouplaunch;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

public class LaunchProcess extends PlatformObject implements IProcess {
	private ILaunch launch;
	
	public LaunchProcess(ILaunch launch) {
		this.launch = launch;
	}	
	
	@Override
	public boolean canTerminate() {
		return launch.canTerminate();
	}

	@Override
	public boolean isTerminated() {
		return launch.isTerminated();
	}

	@Override
	public void terminate() throws DebugException {		
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public ILaunch getLaunch() {
		return null;
	}

	@Override
	public IStreamsProxy getStreamsProxy() {
		return null;
	}

	@Override
	public void setAttribute(String key, String value) {
	}

	@Override
	public String getAttribute(String key) {
		return null;
	}

	@Override
	public int getExitValue() throws DebugException {
		return 0;
	}

}
