package org.irri.genotype.components.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.components.form.OrganismForm;

public class AddOrganismDialog extends Dialog {

	private Composite container;
	private OrganismForm form;

	public AddOrganismDialog(Shell parentShell) {
		super(parentShell);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);

		form = new OrganismForm(container, SWT.NONE);

		return container;
	}

	@Override
	protected void okPressed() {
		form.save();
		super.okPressed();
	}

	// public void setContainer(LoaderForm form) {
	// this.form = form;
	//
	// }

}
