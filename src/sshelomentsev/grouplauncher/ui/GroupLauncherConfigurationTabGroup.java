package sshelomentsev.grouplauncher.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class GroupLauncherConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {
	
	public GroupLauncherConfigurationTabGroup () {
		
	}
	
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(new ILaunchConfigurationTab[] { new GroupLauncherConfigurationTab(), new CommonTab()});
	}

}
