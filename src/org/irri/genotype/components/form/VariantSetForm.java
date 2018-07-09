package org.irri.genotype.components.form;

import java.util.HashMap;

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
import org.irri.genotype.components.dialog.AddCvTermDialog;

import chado.loader.model.Cvterm;
import chado.loader.model.Variantset;
import chado.loader.service.CvTermService;
import chado.loader.service.VariantSetService;

public class VariantSetForm extends Composite implements LoaderForm {

	private FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Text txtVNamebox;
	private Text txtDescbox;

	private Combo cvTermCombo;
	private HashMap<Integer, Cvterm> cvTermMap;
	private CvTermService cvTermDs;
	private Button btnNewButton;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public VariantSetForm(final Composite parent, int style) {
		super(parent, SWT.NONE);

		cvTermMap = new HashMap<>();
		cvTermDs = new CvTermService();

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
		nameLbl.setText("Name");

		txtVNamebox = new Text(this, SWT.BORDER);
		txtVNamebox.setBounds(128, 7, 240, 21);
		toolkit.adapt(txtVNamebox, true, true);

		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setBounds(10, 37, 82, 21);
		toolkit.adapt(lblDescription, true, true);
		lblDescription.setText("Description");

		txtDescbox = new Text(this, SWT.BORDER);
		txtDescbox.setBounds(128, 34, 240, 21);
		toolkit.adapt(txtDescbox, true, true);

		Label lblUrlPrefix = new Label(this, SWT.NONE);
		lblUrlPrefix.setBounds(10, 64, 112, 18);
		toolkit.adapt(lblUrlPrefix, true, true);
		lblUrlPrefix.setText("Variant Type");

		// cvTermCombo = new Combo(this, SWT.NONE);
		// cvTermCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
		// 1));
		// toolkit.adapt(cvTermCombo, true, true);
		//
		// Button btnNewButton = new Button(this, SWT.NONE);
		// btnNewButton.setText("New CV Term");

		cvTermCombo = new Combo(this, SWT.NONE);
		cvTermCombo.setBounds(128, 61, 159, 23);
		toolkit.adapt(cvTermCombo);
		toolkit.paintBordersFor(cvTermCombo);

		btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setBounds(293, 61, 75, 25);
		toolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("New CV Term");
		btnNewButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {

				AddCvTermDialog dialog = new AddCvTermDialog(parent.getShell());
				dialog.open();

				initItemsCvTermComboBox();

			}

		});

		initItemsCvTermComboBox();

	}

	private void initItemsCvTermComboBox() {
		cvTermCombo.removeAll();
		cvTermMap.clear();

		int index = 0;
		for (Cvterm cvTerm : cvTermDs.findAll()) {
			cvTermCombo.add(cvTerm.getName(), index);
			cvTermMap.put(index, cvTerm);
		}

	}

	@Override
	public void save() {
		VariantSetService ds = new VariantSetService();

		Integer sId = cvTermCombo.getSelectionIndex();
		Cvterm cvterm = cvTermMap.get(sId);

		Variantset vSet = new Variantset();
		vSet.setCvterm(cvterm);
		vSet.setDescription(txtDescbox.getText());
		vSet.setName(txtVNamebox.getText());
		
		ds.insertRecord(vSet);

	}

	@Override
	public void initDialog(Dialog dialog) {

	}

}
