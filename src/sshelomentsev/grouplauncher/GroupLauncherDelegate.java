package sshelomentsev.grouplauncher;

import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

public class GroupLauncherDelegate extends LaunchConfigurationDelegate{
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		LinkedList<ILaunch> launches = new LinkedList<ILaunch>();
		for (ILaunchConfiguration child : GroupLauncher.getDefault().getConfigurations(configuration)) {
			launches.add(child.launch(mode, monitor));
		}
		launch.addProcess(new GroupProcess(launch, launches));
	}
}
