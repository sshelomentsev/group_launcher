package sshelomentsev.grouplauncher;

import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GroupLauncher extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "sshelomentsev_group_launcher"; //$NON-NLS-1$

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
	
	public static LinkedList<ILaunchConfiguration> getConfigurations (ILaunchConfiguration configuration, String mode) throws CoreException {
		LinkedList<ILaunchConfiguration> nestedConfiguration = new LinkedList<ILaunchConfiguration>();
		
		
		
		return nestedConfiguration;
	}
	
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		ILaunchConfiguration[] allConfigurations = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
		LinkedList<ILaunch> launches = new LinkedList<ILaunch>();
		for (ILaunchConfiguration child : allConfigurations) {
			launches.add(child.launch(mode, monitor));
		}
		launch.addProcess(new GroupProcess(launch, launches));
	}
	
	
}
