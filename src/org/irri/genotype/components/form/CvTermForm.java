package org.irri.genotype.components.form;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.irri.genotype.ComponentConstants;
import org.irri.genotype.components.LoaderForm;
import org.irri.genotype.components.dialog.AddCvDialog;
import org.irri.genotype.components.dialog.AddCvTermDialog;
import org.irri.genotype.components.dialog.AddDbXrefDialog;
import org.irri.genotype.loader.utils.ComponentElementUtils;

import chado.loader.model.Cv;
import chado.loader.model.Cvterm;
import chado.loader.model.Dbxref;
import chado.loader.service.CvService;
import chado.loader.service.CvTermService;
import chado.loader.service.DbXrefService;

public class CvTermForm extends Composite implements LoaderForm {

	private FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Text txtCvTermbox;
	private Label label;
	private Text textDefBox;
	private Combo dbXrefcomboBx;
	private Combo cvComboBx;
	// private Button btndbXref;
	// private Button btnNewCv;
	private CvService cv_ds;
	private DbXrefService dbXref_ds;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CvTermForm(final Composite parent, int style) {
		super(parent, SWT.BORDER);

		cv_ds = new CvService();
		dbXref_ds = new DbXrefService();

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
		nameLbl.setText("CV");

		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setBounds(10, 37, 82, 21);
		toolkit.adapt(lblDescription, true, true);
		lblDescription.setText("Cv Term Name");

		txtCvTermbox = new Text(this, SWT.BORDER);
		txtCvTermbox.setBounds(128, 34, 251, 25);
		toolkit.adapt(txtCvTermbox, true, true);

		Label lblUrlPrefix = new Label(this, SWT.NONE);
		lblUrlPrefix.setBounds(10, 94, 112, 18);
		toolkit.adapt(lblUrlPrefix, true, true);
		lblUrlPrefix.setText("Db XRef");

		label = new Label(this, SWT.NONE);
		label.setText("Definition");
		label.setBounds(10, 67, 112, 18);
		toolkit.adapt(label, true, true);

		textDefBox = new Text(this, SWT.BORDER);
		textDefBox.setBounds(128, 64, 251, 25);
		toolkit.adapt(textDefBox, true, true);

		// btnNewCv = new Button(this, SWT.NONE);
		// btnNewCv.setBounds(297, 6, 82, 25);
		// toolkit.adapt(btnNewCv, true, true);
		// btnNewCv.setText("New CV");
		// btnNewCv.addListener(SWT.Selection, new Listener() {
		//
		// @Override
		// public void handleEvent(Event e) {
		//
		// AddCvDialog dialog = new AddCvDialog(parent.getShell());
		// dialog.open();
		//
		// initItemsCvComboBox();
		//
		// }
		//
		// });

		dbXrefcomboBx = new Combo(this, SWT.NONE);
		dbXrefcomboBx.setBounds(128, 94, 251, 23);
		toolkit.adapt(dbXrefcomboBx);
		toolkit.paintBordersFor(dbXrefcomboBx);

		// btndbXref = new Button(this, SWT.NONE);
		// btndbXref.setText("New DB Xref");
		// btndbXref.setBounds(297, 93, 82, 25);
		// toolkit.adapt(btndbXref, true, true);
		// btndbXref.addListener(SWT.Selection, new Listener() {
		//
		// @Override
		// public void handleEvent(Event e) {
		//
		// AddDbXrefDialog dialog = new AddDbXrefDialog(parent.getShell());
		// dialog.open();
		//
		// initItemsDbXrefComboBox();
		//
		// }
		//
		// });

		cvComboBx = new Combo(this, SWT.NONE);
		cvComboBx.setBounds(128, 7, 251, 23);
		toolkit.adapt(cvComboBx);
		toolkit.paintBordersFor(cvComboBx);
		cvComboBx.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (cvComboBx.getText().equals(ComponentConstants.ADD_NEW)) {
					AddCvTermDialog dialog = new AddCvTermDialog(parent.getShell());
					if (Window.OK == dialog.open()) {
						cvComboBx.removeAll();

						ComponentElementUtils.initComboItem(cvComboBx, cv_ds.findAllName().toArray());

					} else {
						cvComboBx.deselectAll();
					}

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		//System.out.println(dbXref_ds.findByAllAccession().size());
		ComponentElementUtils.initComboItem(dbXrefcomboBx, dbXref_ds.findByAllAccession().toArray());
		ComponentElementUtils.initComboItem(cvComboBx, cv_ds.findAllName().toArray());

	}

	private void initItemsCvComboBox() {
		cvComboBx.removeAll();
		for (Cv cv : cv_ds.findAll()) {
			cvComboBx.add(cv.getName());
		}

	}

	private void initItemsDbXrefComboBox() {
		dbXrefcomboBx.removeAll();
		for (Dbxref dbxref : dbXref_ds.findByAll()) {
			dbXrefcomboBx.add(dbxref.getAccession());
		}

	}

	@Override
	public void save() {
		CvTermService ds = new CvTermService();
		CvService cvs = new CvService();
		DbXrefService dbX_ds = new DbXrefService();

		String cvName = cvComboBx.getText();
		String dbXrefName = dbXrefcomboBx.getText();

		Cv cv = cvs.findByCvName(cvName).get(0);

		Cvterm cvTerm = new Cvterm();
		cvTerm.setCv(cv);
		cvTerm.setName(txtCvTermbox.getText());
		cvTerm.setDbxref(dbX_ds.findByAccession(dbXrefName).get(0));
		cvTerm.setIsObsolete(0);
		cvTerm.setIsRelationshiptype(0);

		ds.insertRecord(cvTerm);

	}

	@Override
	public void initDialog(Dialog dialog) {

	}
}
