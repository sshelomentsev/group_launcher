package ru.sshelomentsev.grouplaunch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

public class Delegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		LaunchModel model = new LaunchModel(configuration.getWorkingCopy());
		if (!model.isValid()) {
			Messages.fail("The configuration consists a loop dependency or removed child configuration");
		}
		for (Configuration conf : model.getConfigurationList()) {
			LaunchProcess launchProcess = new LaunchProcess(conf.getILaunchConfiguration().launch(mode, monitor));
			launch.addProcess(launchProcess);
			if (conf.getPostLaunchAction() == -1) {
				while (!launchProcess.isTerminated()) {
				}
			} else if (conf.getPostLaunchAction() > 0) {
				try {
					Thread.sleep(conf.getPostLaunchAction() * 1000);	
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
