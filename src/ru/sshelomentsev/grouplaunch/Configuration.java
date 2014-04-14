package ru.sshelomentsev.grouplaunch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.swt.graphics.Image;

/**
 * The class describes the characteristics inherent in nested configurations.
 * @author sshelomentsev
 *
 */
public class Configuration {
	public static final String GROUP_LAUNCH_CONFIGURATION = "sshGroupLaunchConfiguration";
	private ILaunchConfiguration configuration;
	private int postLaunchAction;
	private boolean correct;
	private String name;
	
	public Configuration(ILaunchConfiguration configuration) {
		this.configuration = configuration;
		this.postLaunchAction = 0;
		this.correct = true;
		this.name = configuration.getName();
	}
	
	/**
	 * 
	 * @param configuration
	 * @param postLaunchAction -- the action which carried out after launch of a configuration
	 * types:
	 * wait until terminate, Thread delay N seconds after launch a process, none action 
	 */
	public Configuration(ILaunchConfiguration configuration, int postLaunchAction) {
		this.configuration = configuration;
		this.postLaunchAction = postLaunchAction;
		this.correct = true;
		this.name = configuration.getName();
	}
	
	public Configuration(String name) {
		this.name = name;
		this.correct = false;
	}
	
	public Configuration(String configurationName, int postLaunchAction) throws CoreException {
		List<ILaunchConfiguration> availableConfigurations = 
				new ArrayList<ILaunchConfiguration>(Arrays.asList(DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()));
		this.correct = false;
		for (ILaunchConfiguration configuration : availableConfigurations) {
			if (configuration.getName().compareTo(configurationName) == 0) {
				this.configuration = configuration;
				this.name = configurationName;
				this.postLaunchAction = postLaunchAction;
				this.correct = true;
				break;
			}
		}
	}
	
	public String serialize() {
		String result = "";
		result += name;
		if (postLaunchAction == -1) {
			result += "&Wait&-1";
		} else if (postLaunchAction == 0) {
			result += "&None&0";
		} else {
			result += "&Delay&" + Integer.toString(postLaunchAction);
		}
		return result;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPostLaunchActionString() {
		if (postLaunchAction == -1) {
			return "Wait until terminate";
		} else if (postLaunchAction == 0) {
			return "";
		} else {
			return "Delay " + Integer.toString(postLaunchAction) + " seconds";
		}
	}
	
	public int getPostLaunchAction() {
		return postLaunchAction;
	}
	
	public Image getImage() {
		if (isCorrect()) {
			return DebugUITools.getDefaultImageDescriptor(configuration).createImage();
		} else {
			return Plugin.getImageDescriptor("icons/bad.png").createImage(); 
		}
	}
	
	public boolean isCorrect() {
		return correct;
	}
	
	public boolean isGroupLaunchConfiguration() throws CoreException {
		return configuration.hasAttribute(GROUP_LAUNCH_CONFIGURATION);
	}
	
	public ILaunchConfiguration getILaunchConfiguration() {
		return configuration;
	}
}
