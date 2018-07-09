package org.irri.genotype.components.form;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.irri.genotype.components.LoaderForm;

import chado.loader.model.Cv;
import chado.loader.model.Cvterm;
import chado.loader.service.CvService;
import chado.loader.service.CvTermService;

public class CvForm extends Composite implements LoaderForm {

	private FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Text txtCvbox;
	private Text txtDesc;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CvForm(Composite parent, int style) {
		super(parent, SWT.NONE);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setBounds(10, 37, 46, 21);
		toolkit.adapt(lblDescription, true, true);
		lblDescription.setText("Name");

		txtCvbox = new Text(this, SWT.BORDER);
		txtCvbox.setBounds(105, 29, 224, 25);
		toolkit.adapt(txtCvbox, true, true);

		Label lblDescription_1 = new Label(this, SWT.NONE);
		lblDescription_1.setText("Description");
		lblDescription_1.setBounds(10, 64, 89, 21);
		toolkit.adapt(lblDescription_1, true, true);

		txtDesc = new Text(this, SWT.BORDER);
		txtDesc.setBounds(105, 60, 224, 25);
		toolkit.adapt(txtDesc, true, true);

	}

	@Override
	public void save() {
		CvService cvDs = new CvService();

		Cv cv = new Cv();

		cv.setName(txtCvbox.getText());
		cv.setDefinition(txtDesc.getText());

		cvDs.insertRecord(cv);

	}

	@Override
	public void initDialog(Dialog dialog) {

	}
}
