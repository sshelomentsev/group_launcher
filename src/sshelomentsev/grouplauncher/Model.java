package sshelomentsev.grouplauncher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public class Model {
	public static final String GROUP_LAUNCH_CONFIGURATION = "groupLaunchConfigurations";
	
	/**
	 * check the configuration is a Group Launch Configuration 
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
	public static boolean isGroupLaunchConfiguration(ILaunchConfiguration configuration) throws CoreException {
		return configuration.hasAttribute(GROUP_LAUNCH_CONFIGURATION);
	}
	
	public static void setGroupLaunchConfigurationAttributes(ILaunchConfigurationWorkingCopy configuration, String attribute) throws CoreException {
		configuration.setAttribute(GROUP_LAUNCH_CONFIGURATION, attribute);
	}
	
	/**
	 * get nested configuration from Group Launch Configuration
	 * @param configuration -- group launch configuration
	 * @return -- list of a child configurations nested in the given group configuration
	 * @throws CoreException
	 */	
	public static List<ILaunchConfiguration> getGroupLaunchConfiguration(ILaunchConfiguration configuration) throws CoreException {
		ILaunchConfiguration[] availableConfigurations = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
		String[] groupLaunchAttribute = configuration.getAttribute(GROUP_LAUNCH_CONFIGURATION, "").split("@");
		Set<String> nestedCongigurationNames = new HashSet<String>(Arrays.asList(groupLaunchAttribute));
		List<ILaunchConfiguration> confs = new LinkedList<ILaunchConfiguration>();
		for (ILaunchConfiguration nested : availableConfigurations) {
			if (nestedCongigurationNames.contains(nested.getName())) {
				confs.add(nested);
				nestedCongigurationNames.remove(nested.getName());
			}
		}
		return confs;
	}
	
	public static Queue<ILaunchConfiguration> getGroupLaunchConfigurationByNames(Set<String> configurationNames) throws CoreException {
		Queue<ILaunchConfiguration> launchConfigurations = new LinkedList<ILaunchConfiguration>();
		try {
			ILaunchConfiguration[] availableConfigurations = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
			for (ILaunchConfiguration launch : availableConfigurations) {
				if (configurationNames.contains(launch.getName())) {
					launchConfigurations.add(launch);
				}
			}
		} catch (CoreException exc) {
			GroupLauncher.log(exc);
		}
		return launchConfigurations;
	}
	
	public static boolean isValidConfigurationsSet(Queue<ILaunchConfiguration> unlistenedConfigurations) throws CoreException {
		Set<ILaunchConfiguration> listenedConfigurations = new HashSet<ILaunchConfiguration>();
		while (!unlistenedConfigurations.isEmpty()) {
			ILaunchConfiguration launch = unlistenedConfigurations.remove();
			if (listenedConfigurations.contains(launch)) {
				return false;
			} else {
				listenedConfigurations.add(launch);
			}
			
			try {
				if (launch.hasAttribute(GROUP_LAUNCH_CONFIGURATION)) {
					for (ILaunchConfiguration nested : getGroupLaunchConfiguration(launch)) {
						unlistenedConfigurations.add(nested);
					}
				}
			} catch (CoreException exc) {
				GroupLauncher.log(exc);
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
	public static boolean hasLoopDependent(ILaunchConfiguration configuration) throws CoreException {
		Queue<ILaunchConfiguration> unlistenedConfigurations = new LinkedList<ILaunchConfiguration>();
		for (ILaunchConfiguration launch : getGroupLaunchConfiguration(configuration)) {
			unlistenedConfigurations.add(launch);
		}
		return isValidConfigurationsSet(unlistenedConfigurations);
		
	}
	
	public static boolean hasLoopDependent(Set<String> configurationNames) throws CoreException {
		Queue<ILaunchConfiguration> unlistenedConfigurations = getGroupLaunchConfigurationByNames(configurationNames);
		return isValidConfigurationsSet(unlistenedConfigurations);
	}
	
}
