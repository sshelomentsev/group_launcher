package ru.sshelomentsev.grouplaunch.ui;

import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationTreeContentProvider;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import ru.sshelomentsev.grouplaunch.Configuration;
import ru.sshelomentsev.grouplaunch.LaunchModel;
import ru.sshelomentsev.grouplaunch.Plugin;
import ru.sshelomentsev.grouplaunch.ui.ConfigurationDialog;

public class ConfigurationTab extends AbstractLaunchConfigurationTab {
	@SuppressWarnings("unused")
	private Composite view;
	private Table table;
	private Button buttonAdd, buttonRemove, buttonUp, buttonDown;
	
	LaunchModel model;
	
	
	@Override
	public void createControl(Composite parent) {
		Composite view = new Composite(parent, SWT.MULTI);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		view.setLayout(layout);
		
		table = new ConfigurationTabElements().createConfigurationTable(view);
		
		buttonAdd = new ConfigurationTabElements().createButton(view, "Add");
		buttonRemove = new ConfigurationTabElements().createButton(view, "Remove");
		buttonUp = new ConfigurationTabElements().createButton(view, "Up");
		buttonDown = new ConfigurationTabElements().createButton(view, "Down");
		
		view.pack();
		setControl(view);
		
		buttonAdd.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("restriction")
				ConfigurationDialog dialog = new ConfigurationDialog(getControl().getShell(),
						new DecoratingLabelProvider(DebugUITools.newDebugModelPresentation(), PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()),
						new LaunchConfigurationTreeContentProvider(getLaunchConfigurationDialog().getMode(), getControl().getShell()));
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				if (dialog.open() == org.eclipse.jface.window.Window.OK) {
					Object[] elements = dialog.getResult();
					int postLaunchAction = dialog.getPostLaunchAction();
					for (Object element : elements) {
						if (element instanceof ILaunchConfiguration) {
							try {
								model.add(((ILaunchConfiguration) element).getName(),postLaunchAction);
							} catch (CoreException exc) {
								Plugin.log(exc);
							}
						}
					}
					updateTableContent(table, model);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		buttonRemove.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				for (TableItem item : items) {
					model.remove(item.getText(0));
				}
				updateTableContent(table, model);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		buttonUp.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionCount() == 1 && table.getSelectionIndex() > 0) {
					TableItem item1 = table.getItem(table.getSelectionIndex());
					TableItem item2 = table.getItem(table.getSelectionIndex()-1);
					Image tmpImage = item1.getImage();
					item1.setImage(item2.getImage());
					item2.setImage(tmpImage);
					String tmpString = item1.getText(0);
					item1.setText(0, item2.getText(0));
					item2.setText(0, tmpString);
					tmpString = item1.getText(1);
					item1.setText(1, item2.getText(1));
					item2.setText(1, tmpString);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		buttonDown.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionCount() == 1 && (table.getItemCount() - table.getSelectionCount() > 1)) {
					TableItem item1 = table.getItem(table.getSelectionIndex());
					TableItem item2 = table.getItem(table.getSelectionIndex()+1);
					Image tmpImage = item1.getImage();
					item1.setImage(item2.getImage());
					item2.setImage(tmpImage);
					String tmpString = item1.getText(0);
					item1.setText(0, item2.getText(0));
					item2.setText(0, tmpString);
					tmpString = item1.getText(1);
					item1.setText(1, item2.getText(1));
					item2.setText(1, tmpString);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			model = new LaunchModel(configuration);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		updateTableContent(table, model);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		model.saveSettings();
	}

	@Override
	public boolean isValid(ILaunchConfiguration configuration) {
		String removed = model.getRemovedConfigurationNames();
		if (removed != null) {
			setErrorMessage("Configuration " + removed + " was removed");
			return false;
		}
		try {
			if (model.hasLoopDependency()) {
				setErrorMessage("Has loop dependency in configuration");
				return false;
			}
		} catch (CoreException e) {
			Plugin.log(e);
		}
		setErrorMessage(null);
		setMessage("The order of configurations in the table sets an order of their launch");
		return true;
	}
	
	
	@Override
	public String getName() {
		return "Launches";
	}
	
	public void updateTableContent(Table table, LaunchModel model) {
		table.removeAll();
		List<Configuration> configurations = model.getConfigurationList();
		for (Configuration conf : configurations) {
			TableItem item = new TableItem(table, SWT.LEFT);
			item.setText(0, conf.getName());
			item.setText(1, conf.getPostLaunchActionString());
			item.setImage(conf.getImage());
		}
		updateLaunchConfigurationDialog();
	}
}
