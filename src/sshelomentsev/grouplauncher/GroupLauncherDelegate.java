package sshelomentsev.grouplauncher;

import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import sshelomentsev.grouplauncher.ui.Messages;
import sshelomentsev.grouplauncher.Model;

public class GroupLauncherDelegate extends LaunchConfigurationDelegate {
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		if (!Model.hasLoopDependent(configuration)) {
			throw new AssertionError(Messages.incorrectConfiguration);
		}
		LinkedList<ILaunch> launches = new LinkedList<ILaunch>();
		for (ILaunchConfiguration nested : Model.getGroupLaunchConfiguration(configuration)) {
			launches.add(nested.launch(mode, monitor));
		}
		launch.addProcess(new GroupProcess(launch, launches));
	}
}
