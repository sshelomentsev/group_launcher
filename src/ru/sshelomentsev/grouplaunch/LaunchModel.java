package ru.sshelomentsev.grouplaunch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import ru.sshelomentsev.grouplaunch.Configuration;

/**
 * The class describes the model of Group Launch Configuration
 * @author sshelomentsev
 *
 */
public class LaunchModel {
	ILaunchConfigurationWorkingCopy configuration;
	List<Configuration> configurations = new LinkedList<Configuration>();
	
	public LaunchModel(ILaunchConfiguration configuration) throws CoreException {
		this.configuration = (ILaunchConfigurationWorkingCopy) configuration;
		unserialize(configuration, configurations);
	}
	
	public LaunchModel(ILaunchConfigurationWorkingCopy configuration) throws CoreException {
		this.configuration = configuration;
		unserialize(configuration, configurations);
	}
	
	public void saveSettings() {
		configuration.setAttribute(Configuration.GROUP_LAUNCH_CONFIGURATION, serialize());
	}
	
	public String getAttribute() throws CoreException {
		//return configuration.getAttribute(GROUP_LAUNCH_CONFIGURATION, "");
		return Integer.toString(configurations.size());
	}
	
	private String serialize() {
		String result = "";
		for (Configuration configuration : configurations) {
			result += configuration.serialize() + "@";
		}
		return result;
	}
	
	private void unserialize(ILaunchConfiguration configuration, List<Configuration> configurations) throws CoreException {
		List<ILaunchConfiguration> availableConfigurations = 
				new ArrayList<ILaunchConfiguration>(Arrays.asList(DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()));
		if (configuration.hasAttribute(Configuration.GROUP_LAUNCH_CONFIGURATION)) {
			String attribute = configuration.getAttribute(Configuration.GROUP_LAUNCH_CONFIGURATION, "");
			if (attribute != "") {
				String[] attributes = attribute.split("@");
				for (String attr : attributes) {
					String[] attrs = attr.split("&");
					boolean flag = false;
					for (ILaunchConfiguration conf : availableConfigurations) {
						if (conf.getName().compareTo(attrs[0]) == 0) {
							configurations.add(new Configuration(conf, Integer.parseInt(attrs[2])));
							flag = true;
							break;
						}
					}
					if (!flag) {
						configurations.add(new Configuration(attrs[0]));
					}
				}
			}
		}
	}
	
	public List<Configuration> getConfigurationList() {
		return configurations;
	}
	
	public void add(ILaunchConfiguration configuration, int postLaunchAction) {
		configurations.add(new Configuration(configuration, postLaunchAction));
	}
	
	public void add(String configurationName, int postLaunchAction) throws CoreException {
		configurations.add(new Configuration(configurationName, postLaunchAction));
	}
	
	public boolean remove(String configurationName) {
		for (Configuration configuration : configurations) {
			if (configuration.getName().compareTo(configurationName) == 0) {
				configurations.remove(configuration);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check on loop dependency existence in the graph of launches
	 * @return 
	 * @throws CoreException
	 */
	public boolean hasLoopDependency() throws CoreException {
		Set<ILaunchConfiguration> listenedConfigurations = new HashSet<ILaunchConfiguration>();
		Queue<ILaunchConfiguration> unlistenedConfigurations = new LinkedList<ILaunchConfiguration>();
		
		for (Configuration configuration : configurations) {
			if (configuration.isCorrect()) {
				unlistenedConfigurations.add(configuration.getILaunchConfiguration());
			}
		}
		
		while (!unlistenedConfigurations.isEmpty()) {
			ILaunchConfiguration configuration = unlistenedConfigurations.remove();
			if (listenedConfigurations.contains(configuration)) {
				return true;
			} else {
				listenedConfigurations.add(configuration);
			}
			
			Configuration conf = new Configuration(configuration);
			if (conf.isGroupLaunchConfiguration()) {
				List<Configuration> nestedConfigurations = new LinkedList<Configuration>();
				unserialize(configuration, nestedConfigurations);
				for (Configuration nested : nestedConfigurations) {
					if (nested.isCorrect()) {
						unlistenedConfigurations.add(nested.getILaunchConfiguration());
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Check on existence of removed configurations
	 * @return configuration names as string, if exists or NULL
	 */
	public String getRemovedConfigurationNames() {
		String removed = "";
		for (Configuration configuration : configurations) {
			if (!configuration.isCorrect()) {
				removed += configuration.getName() + ", ";
			}
		}
		if (removed == "") {
			removed = null;
		}
		return removed;
	}
	
	/**
	 * Check before launch
	 * @return
	 * @throws CoreException
	 */
	public boolean isValid() throws CoreException {
		if ((getRemovedConfigurationNames() == null) && !hasLoopDependency()) {
			return true;
		}
		return false;
	}
}
