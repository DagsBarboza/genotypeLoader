package org.irri.genotype.components;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.irri.genotype.Cache;
import org.irri.genotype.GenotypeLoaderUI;
import org.irri.genotype.LoaderConstants;
import org.irri.genotype.LoaderProperties;

import chado.loader.AppContext;

public class SettingsDialog extends Dialog {

	private Composite container;
	private Text username;
	private Text databasename;
	private Text port;
	private Text hostname;
	private Label lblNewLabel;
	private Label lblNewLabel_1;
	private Label lblNewLabel_2;
	private Label lblNewLabel_3;
	private Text password;
	private Label lblNewLabel_4;
	private Label lblConfig;
	private Button btnNewButton;
	private Combo combo;
	private Cache cache;
	private Button testConn;
	private Text connMsg;
	private LoaderProperties prop;
	private HashMap<String, String> properties;
	private GenotypeLoaderUI genotypeLoaderUI;

	public SettingsDialog(Shell parentShell, Cache cache, GenotypeLoaderUI genotypeLoaderUI) {
		super(parentShell);
		// setShellStyle(SWT.APPLICATION_MODAL);
		this.genotypeLoaderUI = genotypeLoaderUI;
		this.properties = genotypeLoaderUI.getProperties();
		this.cache = cache;

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		lblConfig = new Label(container, SWT.NONE);
		lblConfig.setText("Saved Config");

		combo = new Combo(container, SWT.NONE);
		GridData dataG = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		dataG.widthHint = convertWidthInCharsToPixels(100);

		combo.setLayoutData(dataG);
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				prop = cache.getCacheList().get(combo.getText());

				if (prop != null) {
					hostname.setText(prop.getHostname());
					databasename.setText(prop.getDatabasename());
					port.setText(prop.getPort());
					username.setText(prop.getUsername());
					password.setText(prop.getPassword());

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		initCombo();

		lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("Hostname");

		hostname = new Text(container, SWT.BORDER);
		hostname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		hostname.setText(properties.get(LoaderConstants.HOSTNAME));
		
		lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText("Port");

		port = new Text(container, SWT.BORDER);
		port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		port.setText(properties.get(LoaderConstants.PORT));
		

		lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("Database Name");

		databasename = new Text(container, SWT.BORDER);
		databasename.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		databasename.setText(properties.get(LoaderConstants.DATABASE));
		
		lblNewLabel_3 = new Label(container, SWT.NONE);
		lblNewLabel_3.setText("User Name");

		username = new Text(container, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		username.setText(properties.get(LoaderConstants.USER));
		
		lblNewLabel_4 = new Label(container, SWT.NONE);
		lblNewLabel_4.setText("Password");

		password = new Text(container, SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		password.setText(properties.get(LoaderConstants.PASSWORD));
		
		new Label(container, SWT.NONE);

		btnNewButton = new Button(container, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 101;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("Save");
		new Label(container, SWT.NONE);

		testConn = new Button(container, SWT.NONE);
		GridData gd_testConn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_testConn.widthHint = 99;
		testConn.setLayoutData(gd_testConn);
		testConn.setText("Test");
		testConn.addSelectionListener(new SelectionListener() {
			

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				if (loadConnection())
					connMsg.setText("Success!!");
				else
					connMsg.setText("Connection Failed!!");

				AppContext.revertEntityManager();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		new Label(container, SWT.NONE);

		connMsg = new Text(container, SWT.NONE);
		connMsg.setEditable(false);
		GridData gd_connMsg = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_connMsg.widthHint = 95;
		connMsg.setLayoutData(gd_connMsg);

		btnNewButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				prop = new LoaderProperties();
				prop.setHostname(hostname.getText());
				prop.setDatabasename(databasename.getText());
				prop.setPort(port.getText());
				prop.setUsername(username.getText());
				prop.setPassword(password.getText());

				cache.putProperty(combo.getText(), prop);

				cache.commit();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				combo.setText("Select--");

			}
		});

		// form = new DbForm(container, SWT.NONE);

		return container;
	}

	private void initCombo() {
		ConcurrentNavigableMap<String, LoaderProperties> configList = cache.getCacheList();

		for (Map.Entry<String, LoaderProperties> entry : configList.entrySet()) {
			combo.add(entry.getKey());
		}
		
		combo.setText(properties.get(LoaderConstants.CONFIG_NAME));

	}

	private boolean loadConnection() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("javax.persistence.jdbc.url",
				"jdbc:postgresql://" + hostname.getText() + ":" + port.getText() + "/" + databasename.getText());
		properties.put("javax.persistence.jdbc.user", username.getText());
		properties.put("javax.persistence.jdbc.password", password.getText());

		// Map<String, String> properties = new HashMap<String, String>();
		// properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		// properties.put("javax.persistence.jdbc.url",
		// "jdbc:postgresql://172.29.4.215:5432/iricsss");
		// properties.put("javax.persistence.jdbc.user", "iric");
		// properties.put("javax.persistence.jdbc.password", "iric-dev");

		return AppContext.createEntityManager(properties);

	}

	@Override
	protected void okPressed() {
		if (loadConnection()) {
			properties.put(LoaderConstants.HOSTNAME, getHostname());
			properties.put(LoaderConstants.PORT, getPort());
			properties.put(LoaderConstants.DATABASE, getDatabasename());
			properties.put(LoaderConstants.USER, getUsername());
			properties.put(LoaderConstants.PASSWORD, getPassword());
			
			properties.put(LoaderConstants.CONFIG_NAME, getConfigName());
			
			genotypeLoaderUI.setProperties(properties);
			
			genotypeLoaderUI.initElements();
			
			super.okPressed();
			
		}
		else 
			MessageDialog.openError(this.getShell(), "Error", "Cannot Load Connection!");
		
	}

	public String getUsername() {
		return username.getText();
	}

	public String getDatabasename() {
		return databasename.getText();
	}

	public String getPort() {
		return port.getText();
	}

	
	public String getHostname() {
		return hostname.getText();
	}

	
	public String getPassword() {
		return password.getText();
	}
	
	public String getConfigName() {
		return combo.getText();
	}

	

}
