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
import org.irri.genotype.components.LoaderForm;

import chado.loader.model.Db;
import chado.loader.model.Organism;
import chado.loader.service.DbService;
import chado.loader.service.OrganismService;

public class OrganismForm extends Composite implements LoaderForm {

	private FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Text txtGenusbox;
	private Text txtSpeciesbox;
	private Text txtCommonBox;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public OrganismForm(Composite parent, int style) {
		super(parent, SWT.BORDER);
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
		nameLbl.setText("Genus");

		txtGenusbox = new Text(this, SWT.BORDER);
		txtGenusbox.setBounds(128, 7, 224, 21);
		toolkit.adapt(txtGenusbox, true, true);

		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setBounds(10, 37, 82, 21);
		toolkit.adapt(lblDescription, true, true);
		lblDescription.setText("Species");

		txtSpeciesbox = new Text(this, SWT.BORDER);
		txtSpeciesbox.setBounds(128, 34, 224, 21);
		toolkit.adapt(txtSpeciesbox, true, true);

		Label lblUrlPrefix = new Label(this, SWT.NONE);
		lblUrlPrefix.setBounds(10, 61, 112, 18);
		toolkit.adapt(lblUrlPrefix, true, true);
		lblUrlPrefix.setText("Common Name");

		txtCommonBox = new Text(this, SWT.BORDER);
		txtCommonBox.setBounds(128, 58, 224, 21);
		toolkit.adapt(txtCommonBox, true, true);

	}

	@Override
	public void save() {
		OrganismService ds = new OrganismService();

		Organism org = new Organism();
		org.setGenus(txtGenusbox.getText());
		org.setSpecies(txtSpeciesbox.getText());
		org.setCommonName(txtCommonBox.getText());

		ds.insertRecord(org);

	}

	@Override
	public void initDialog(Dialog dialog) {

	}

}
