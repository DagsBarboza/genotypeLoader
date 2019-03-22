package org.irri.genotype.components.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.components.form.DbForm;
import org.eclipse.swt.layout.GridData;

public class AddDbDialog extends Dialog {

	private Composite container;
	private DbForm form;

	public AddDbDialog(Shell parentShell) {
		super(parentShell);
		

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);

		form = new DbForm(container, SWT.BORDER);
		GridData gd_form = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_form.widthHint = 349;
		gd_form.heightHint = 116;
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
		shell.setText("Add Db");
	}

}
