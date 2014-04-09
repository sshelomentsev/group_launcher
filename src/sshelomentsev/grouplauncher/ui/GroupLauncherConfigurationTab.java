package sshelomentsev.grouplauncher.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.tree.VariableHeightLayoutCache;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationTreeContentProvider;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchViewerComparator;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

import sshelomentsev.grouplauncher.GroupLauncher;



public class GroupLauncherConfigurationTab extends AbstractLaunchConfigurationTab {		
	private CheckboxTreeViewer treeViewer;
	private ITreeContentProvider contentProvider;
	
	@Override
	public void createControl(Composite parent) {
		treeViewer = new ContainerCheckedTreeViewer(parent, SWT.MULTI);
		setControl(treeViewer.getTree());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		treeViewer.setLabelProvider(new DecoratingLabelProvider(DebugUITools.newDebugModelPresentation(), PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
		treeViewer.setComparator(new WorkbenchViewerComparator());
		contentProvider = new LaunchConfigurationTreeContentProvider(getLaunchConfigurationDialog().getMode(), null);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		List<Object> checkedElements = new ArrayList<Object>();
		try {
			for (ILaunchConfiguration conf : GroupLauncher.getDefault().getConfigurations(configuration)) {
				checkedElements.add(conf);
			}
		} catch (CoreException exc) {
			GroupLauncher.log(exc);
		}
		treeViewer.setCheckedElements(checkedElements.toArray());
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateLaunchConfigurationDialog();
			}
		});
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		List<Object> checkedElements = Arrays.asList(treeViewer.getCheckedElements());
		String configurationAttribute = null;
		for (Object element : checkedElements) {
			configurationAttribute += element.toString() + "@";
		}
		configuration.setAttribute(GroupLauncher.CHILD_CONFIGURATIONS, configurationAttribute);
	}
	
	@Override
	public String getName() {
		return "Launches";
	}
	
	public boolean isValid(ILaunchConfiguration configuration) {
		List<Object> checkedElements = Arrays.asList(treeViewer.getCheckedElements());
		for (Object element : checkedElements) {
			if (element.toString().contains(configuration.getName())) {
				setErrorMessage("A loop dependency in the configuration");
				return false;
			}
		}
		setErrorMessage(null);
		return true;
	}
	
	
}
