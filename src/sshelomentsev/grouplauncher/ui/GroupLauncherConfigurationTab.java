package sshelomentsev.grouplauncher.ui;

import javax.swing.tree.VariableHeightLayoutCache;

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
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.model.WorkbenchViewerComparator;

import sshelomentsev.grouplauncher.GroupLauncher;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GroupLauncherConfigurationTab extends AbstractLaunchConfigurationTab {		
	private CheckboxTreeViewer viewer;
	private ITreeContentProvider contentProvider;
	private TableViewer tableViewer;
	
	@Override
	public void createControl(Composite parent) {
		viewer = new ContainerCheckedTreeViewer(parent, SWT.MULTI);
		setControl(viewer.getTree());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("restriction")
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		
		viewer.setLabelProvider(new DecoratingLabelProvider(DebugUITools.newDebugModelPresentation(), PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
		viewer.setComparator(new WorkbenchViewerComparator());
		contentProvider = new LaunchConfigurationTreeContentProvider(getLaunchConfigurationDialog().getMode(), null);
		viewer.setContentProvider(contentProvider);
		viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
				
		
		
		try {
			ILaunchConfiguration[] allConfigurations = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
			String[] configurationAttr = configuration.getAttribute(GroupLauncher.CHILD_CONFIGURATIONS, "").split("@");
			Set<String> congigurationNames = new HashSet<String>(Arrays.asList(configurationAttr));
			for (ILaunchConfiguration conf : allConfigurations) {
				if (congigurationNames.contains(conf.getName())) {
					viewer.setChecked(conf, true);
				} else {
					viewer.setChecked(conf, false);
				}
			}
		} catch (CoreException exc) {
			GroupLauncher.log(exc);
		}
		viewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateLaunchConfigurationDialog();
			}
		});
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		List<Object> checkedItem = Arrays.asList(viewer.getCheckedElements());
		String configurationAttribute = null;
		for (Object item : checkedItem) {
			configurationAttribute += item.toString() + "@";
		}
		configuration.setAttribute(GroupLauncher.CHILD_CONFIGURATIONS, configurationAttribute);
	}

	@Override
	public String getName() {
		return "Launches";
	}

}
