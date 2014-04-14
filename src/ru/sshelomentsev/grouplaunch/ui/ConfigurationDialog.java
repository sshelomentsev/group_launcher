package ru.sshelomentsev.grouplaunch.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class ConfigurationDialog extends ElementTreeSelectionDialog {
	private static final int POST_LAUNCH_ACTION_NONE = 0;
	private static final int POST_LAUNCH_ACTION_WAIT = 1;
	private static final int POST_LAUNCH_ACTION_DELAY = 2;
	
	private Combo postLaunchActionCombo;
	private Spinner delayValueSpinner;
	
	private Label lblPostLaunchAction;
	private Label lbldelayValue;
	
	private Set<String> configurationNames = new HashSet<String>();
	private int postLaunchAction;
	
	
	public ConfigurationDialog(Shell parent, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
	}
	
	public void create() {
		setTitle("Launch configuration selection dialog");
		setMessage("Please select configuration from tree");
		super.create();
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);
		createPostLaunchInput(container);
		createDelayValueInput(container);
	    return area;
	}
	
	protected void createPostLaunchInput(Composite container) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.NONE;
		gridData.verticalAlignment = SWT.NONE;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		
		lblPostLaunchAction = new Label(container, SWT.NONE);
		lblPostLaunchAction.setText("Post launch action:");
		lblPostLaunchAction.setSize(120, 20);
		
		postLaunchActionCombo = new Combo(container, SWT.NONE);
		postLaunchActionCombo.setLayoutData(gridData);
		String[] items = {"None", "Wait until terminate", "Delay"};
		postLaunchActionCombo.setItems(items);
		postLaunchActionCombo.select(POST_LAUNCH_ACTION_NONE);
		postLaunchActionCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (postLaunchActionCombo.getSelectionIndex() == POST_LAUNCH_ACTION_DELAY) {
					delayValueSpinner.setVisible(true);
					lbldelayValue.setVisible(true);
				} else {
					delayValueSpinner.setVisible(false);
					lbldelayValue.setVisible(false);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	protected void createDelayValueInput(Composite container) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.NONE;
		gridData.verticalAlignment = SWT.NONE;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		
		lbldelayValue = new Label(container, SWT.NONE);
		lbldelayValue.setText("Delay (sec)");
		lbldelayValue.setSize(120, 20);
		lbldelayValue.setVisible(false);
		
		delayValueSpinner = new Spinner(container, SWT.FILL);
		delayValueSpinner.setVisible(false);
		delayValueSpinner.setMinimum(1);
		delayValueSpinner.setMaximum(100000);
		delayValueSpinner.setSelection(1000);
		delayValueSpinner.setIncrement(1);
		delayValueSpinner.setLayoutData(gridData);
	}
	
	@Override
	protected void okPressed() {
		Object[] selectedItems = super.getResult();
		for (Object item : selectedItems) {
			configurationNames.add(item.toString());
		}
		switch (postLaunchActionCombo.getSelectionIndex()) {
		case POST_LAUNCH_ACTION_NONE : 
			postLaunchAction = POST_LAUNCH_ACTION_NONE;
			break;
		case POST_LAUNCH_ACTION_WAIT : 
			postLaunchAction = -1;
			break;
		case POST_LAUNCH_ACTION_DELAY:
			postLaunchAction = delayValueSpinner.getSelection();
			break;
		}
		super.okPressed();
	}
	
	public Set<String> getConfigurationName() {
		return configurationNames;
	}
	
	public int getPostLaunchAction() {
		return postLaunchAction;
	}
	
}
