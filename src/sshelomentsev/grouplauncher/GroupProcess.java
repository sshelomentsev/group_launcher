package sshelomentsev.grouplauncher;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

import java.util.LinkedList;

public class GroupProcess extends PlatformObject implements IProcess {
	private ILaunch groupLaunch;
	private LinkedList<ILaunch> launches;
	
	public GroupProcess(ILaunch groupLaunch, LinkedList<ILaunch> launches) {
		this.groupLaunch = groupLaunch;
		this.launches = launches;
	}
	
	
	@Override
	public boolean canTerminate() {
		for (ILaunch launch : launches) {
			if (launch.canTerminate()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isTerminated() {
		for (ILaunch launch : launches) {
			if (!launch.isTerminated()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void terminate() throws DebugException {
		for (ILaunch launch : launches) {
			if (launch.canTerminate()) {
				launch.terminate();
			}
		}
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunch getLaunch() {
		return groupLaunch;
	}

	@Override
	public IStreamsProxy getStreamsProxy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAttribute(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getExitValue() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

}
