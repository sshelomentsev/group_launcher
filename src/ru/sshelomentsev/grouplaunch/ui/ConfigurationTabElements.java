package ru.sshelomentsev.grouplaunch.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ConfigurationTabElements {
	public Button createButton(Composite container, String buttonName) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.verticalAlignment = SWT.TOP;
		gridData.verticalIndent = 1;
		gridData.widthHint = 80;
		Button button = new Button(container, SWT.FLAT);
		button.setLayoutData(gridData);
		button.setText(buttonName);
		return button;
	}
	
	public Table createConfigurationTable(Composite container) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 5;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		
		Table table = new Table (container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLayoutData(gridData);

		TableColumn colConfigurationName = new TableColumn(table, SWT.LEFT);
		colConfigurationName.setText("Configuration name");
		colConfigurationName.setWidth(320);
		
		TableColumn colConfigurationAction = new TableColumn(table, SWT.LEFT);
		colConfigurationAction.setText("Action");
		colConfigurationAction.setWidth(200);
		table.setHeaderVisible(true);
		return table;
	}
}
