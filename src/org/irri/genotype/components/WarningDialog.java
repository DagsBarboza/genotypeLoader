package org.irri.genotype.components;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.ErrorType;

public class WarningDialog extends Dialog {

	private Composite container;
	private Integer error;

	public WarningDialog(Shell parentShell, Integer error) {
		super(parentShell);

		this.error = error;

		setShellStyle(SWT.APPLICATION_MODAL);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label lblNewLabel = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 383;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		if (error == ErrorType.EMPTY_ERROR)
			lblNewLabel.setText("Do not leave empty values!");
		if (error == ErrorType.NO_FILE)
			lblNewLabel.setText("Check File Path!");

		return container;
	}

	@Override
	protected void okPressed() {
		// form.save();
		super.okPressed();
	}

}
