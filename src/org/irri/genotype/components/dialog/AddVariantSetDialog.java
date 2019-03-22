package org.irri.genotype.components.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.components.form.CvTermForm;
import org.irri.genotype.components.form.VariantSetForm;

public class AddVariantSetDialog extends Dialog {

	private Composite container;
	private VariantSetForm form;

	public AddVariantSetDialog(Shell parentShell) {
		super(parentShell);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);

		form = new VariantSetForm(container, SWT.NONE);

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
		shell.setText("Add VariantSet");
	}
}
