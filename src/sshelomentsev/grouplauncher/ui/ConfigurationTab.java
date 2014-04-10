package sshelomentsev.grouplauncher.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationTreeContentProvider;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchViewerComparator;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

import sshelomentsev.grouplauncher.GroupLauncher;
import sshelomentsev.grouplauncher.Model;



public class ConfigurationTab extends AbstractLaunchConfigurationTab {		
	private CheckboxTreeViewer treeViewer;
	private ITreeContentProvider contentProvider;
	private ViewerFilter viewerFilter;
	
	@Override
	public void createControl(Composite parent) {
		treeViewer = new ContainerCheckedTreeViewer(parent, SWT.MULTI);
		setControl(treeViewer.getTree());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		treeViewer.setLabelProvider(new DecoratingLabelProvider(DebugUITools.newDebugModelPresentation(), PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
		treeViewer.setComparator(new WorkbenchViewerComparator());
		contentProvider = new LaunchConfigurationTreeContentProvider(getLaunchConfigurationDialog().getMode(), null);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		treeViewer.resetFilters();
		viewerFilter = new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element.toString().compareTo(configuration.getName()) == 0) {
					return false;
				}
				return true;
			}
		};
		treeViewer.addFilter(viewerFilter);
		
		List<Object> checkedElements = new ArrayList<Object>();
		
		try {
			for (ILaunchConfiguration nested : Model.getGroupLaunchConfiguration(configuration)) {
				checkedElements.add(nested);
			}
		} catch (CoreException e) {
			GroupLauncher.log(e);
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
		String nestedConfigurationAttribute = null;
		for (Object element : checkedElements) {
			nestedConfigurationAttribute += element.toString() + "@";
		}
		try {
			Model.setGroupLaunchConfigurationAttributes(configuration, nestedConfigurationAttribute);
		} catch (CoreException exc) {
			GroupLauncher.log(exc);
		}
	}
	
	@Override
	public String getName() {
		return "Launches";
	}
	
	public boolean isValid(ILaunchConfiguration configuration) {
		List<Object> checkedElements = Arrays.asList(treeViewer.getCheckedElements());
		Set<String> nestedCongigurationNames = new HashSet<String>();
		for (Object element : checkedElements) {
			nestedCongigurationNames.add(element.toString());
		}
		try {
			if (!Model.hasLoopDependent(nestedCongigurationNames)) {
				setErrorMessage("A loop dependency in configurations");
				return false;
			}
		} catch (CoreException exc) {
			GroupLauncher.log(exc);
		}
		
		setErrorMessage(null);
		return true;
	}	
}
