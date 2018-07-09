package org.irri.genotype.components.form;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.irri.genotype.components.LoaderForm;
import org.irri.genotype.components.dialog.AddDbDialog;

import chado.loader.model.Db;
import chado.loader.model.Dbxref;
import chado.loader.service.DbService;
import chado.loader.service.DbXrefService;

public class dbXrefForm extends Composite implements LoaderForm {

	private FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Label lblAccession;
	private Text textAccessionBox;
	private Text textVersion;
	private Text textDesc;
	private Label lblDescription_1;
	private Combo comboDBName;
	private DbService db_ds;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public dbXrefForm(final Composite parent, int style) {
		super(parent, SWT.NONE);

		db_ds = new DbService();

		setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setBounds(10, 13, 82, 21);
		toolkit.adapt(lblDescription, true, true);
		lblDescription.setText("Database");

		Label lblUrlPrefix = new Label(this, SWT.NONE);
		lblUrlPrefix.setBounds(10, 77, 112, 18);
		toolkit.adapt(lblUrlPrefix, true, true);
		lblUrlPrefix.setText("Version");

		lblAccession = new Label(this, SWT.NONE);
		lblAccession.setText("Accession");
		lblAccession.setBounds(10, 43, 112, 18);
		toolkit.adapt(lblAccession, true, true);

		textAccessionBox = new Text(this, SWT.BORDER);
		textAccessionBox.setBounds(128, 40, 263, 25);
		toolkit.adapt(textAccessionBox, true, true);

		textVersion = new Text(this, SWT.BORDER);
		textVersion.setText("1");
		textVersion.setBounds(128, 70, 263, 25);
		toolkit.adapt(textVersion, true, true);

		textDesc = new Text(this, SWT.BORDER);
		textDesc.setBounds(128, 101, 263, 25);
		toolkit.adapt(textDesc, true, true);

		lblDescription_1 = new Label(this, SWT.NONE);
		lblDescription_1.setText("Description");
		lblDescription_1.setBounds(10, 108, 112, 18);
		toolkit.adapt(lblDescription_1, true, true);

		comboDBName = new Combo(this, SWT.NONE);
		comboDBName.setBounds(128, 10, 183, 23);
		toolkit.adapt(comboDBName);
		toolkit.paintBordersFor(comboDBName);

		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setBounds(316, 9, 75, 25);
		toolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("New DB");
		btnNewButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {

				AddDbDialog dialog = new AddDbDialog(parent.getShell());
				dialog.open();

				initItemsDbComboBox();

			}

		});

		initItemsDbComboBox();

	}

	private void initItemsDbComboBox() {
		comboDBName.removeAll();
		for (Db db : db_ds.getAllDb()) {
			comboDBName.add(db.getName());
		}

	}

	@Override
	public void save() {
		DbXrefService service = new DbXrefService();
		DbService dbService = new DbService();

		Dbxref dbxref = new Dbxref();
		dbxref.setAccession(textAccessionBox.getText());
		dbxref.setDb(dbService.getDbByName(comboDBName.getText()).get(0));
		dbxref.setVersion(textVersion.getText());
		dbxref.setDescription(textDesc.getText());

		service.insertRecord(dbxref);

	}

	@Override
	public void initDialog(Dialog dialog) {

	}
}
