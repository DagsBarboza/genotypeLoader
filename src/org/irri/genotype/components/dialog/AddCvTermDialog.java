package org.irri.genotype.components.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.components.form.CvTermForm;
import org.eclipse.swt.layout.GridData;

public class AddCvTermDialog extends Dialog {

	private Composite container;
	private CvTermForm form;

	public AddCvTermDialog(Shell parentShell) {
		super(parentShell);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);

		form = new CvTermForm(container, SWT.NONE);
		GridData gd_form = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_form.heightHint = 135;
		gd_form.widthHint = 399;
		form.setLayoutData(gd_form);

		return container;
	}

	@Override
	protected void okPressed() {
		form.save();
		super.okPressed();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Add CvTerm");
	}

}
