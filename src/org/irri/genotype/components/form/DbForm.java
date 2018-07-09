package org.irri.genotype.components.form;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.irri.genotype.components.GenotypeLoaderForm;
import org.irri.genotype.components.LoaderForm;

import chado.loader.GenotypeLoader;
import chado.loader.model.Db;
import chado.loader.service.DbService;

public class DbForm extends Composite implements LoaderForm{

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Text txtNamebox;
	private Text txtDescbox;
	private Text txtUrlPrefix;
	private Text txtUrl;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DbForm(Composite parent, int style) {
		super(parent, SWT.NONE);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		Label nameLbl = new Label(this, SWT.NONE);
		nameLbl.setBounds(10, 10, 55, 21);
		toolkit.adapt(nameLbl, true, true);
		nameLbl.setText("Name *");

		txtNamebox = new Text(this, SWT.BORDER);
		txtNamebox.setBounds(99, 10, 224, 21);
		toolkit.adapt(txtNamebox, true, true);

		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setBounds(10, 37, 83, 21);
		toolkit.adapt(lblDescription, true, true);
		lblDescription.setText("Description");

		txtDescbox = new Text(this, SWT.BORDER);
		txtDescbox.setBounds(99, 34, 224, 21);
		toolkit.adapt(txtDescbox, true, true);

		Label lblUrlPrefix = new Label(this, SWT.NONE);
		lblUrlPrefix.setBounds(10, 61, 55, 18);
		toolkit.adapt(lblUrlPrefix, true, true);
		lblUrlPrefix.setText("URL Prefix");

		txtUrlPrefix = new Text(this, SWT.BORDER);
		txtUrlPrefix.setBounds(99, 58, 224, 21);
		toolkit.adapt(txtUrlPrefix, true, true);

		Label lblUrl = new Label(this, SWT.NONE);
		lblUrl.setBounds(10, 89, 55, 18);
		toolkit.adapt(lblUrl, true, true);
		lblUrl.setText("URL");

		txtUrl = new Text(this, SWT.BORDER);
		txtUrl.setBounds(99, 86, 224, 21);
		toolkit.adapt(txtUrl, true, true);

	}

	@Override
	public void save() {
		DbService dbs = new DbService();

		Db db = new Db();
		db.setName(txtNamebox.getText());
		db.setDescription(txtDescbox.getText());
		db.setUrlprefix(txtUrlPrefix.getText());
		db.setUrl(txtUrl.getText());

		dbs.insertRecord(db);

	}

	@Override
	public void initDialog(Dialog dialog) {
		
		
	}

	

}
