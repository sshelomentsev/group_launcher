package sshelomentsev.grouplauncher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GroupLauncher extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "sshelomentsev_group_launcher"; //$NON-NLS-1$
	public static final String NESTED_CONFIGURATIONS = "nestedConfigurations"; 

	// The shared instance
	private static GroupLauncher plugin;
	
	/**
	 * The constructor
	 */
	public GroupLauncher() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GroupLauncher getDefault() {
		return plugin;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static void log (CoreException exc) {
		GroupLauncher.getDefault().getLog().log(exc.getStatus());
	}
	
	/**
	 * 
	 * @param configuration -- group launch configuration
	 * @return -- list of a child configurations nested in the given group configuration
	 * @throws CoreException
	 */	
	public List<ILaunchConfiguration> getNestedConfigurations(ILaunchConfiguration configuration) throws CoreException {
		ILaunchConfiguration[] availableConfigurations = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
		String[] groupLaunchAttribute = configuration.getAttribute(GroupLauncher.NESTED_CONFIGURATIONS, "").split("@");
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
	
}