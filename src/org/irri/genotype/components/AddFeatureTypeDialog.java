package org.irri.genotype.components;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import chado.loader.model.Cvterm;
import chado.loader.model.Db;
import chado.loader.model.Organism;
import chado.loader.model.Variantset;

import org.eclipse.swt.layout.GridData;

public class AddFeatureTypeDialog extends Dialog {

	private Composite container;

	public AddFeatureTypeDialog(Display display, Shell parentShell, FileChooserListener fcl, FileChooserListener fcl2, Cvterm cvterm, Organism organism, Db db, Variantset vSet) {
		super(parentShell);
		
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
		lblNewLabel.setText("This Feature type doesn't exists, do you want to create one?");

		return container;
	}

	@Override
	protected void okPressed() {
		// form.save();
		super.okPressed();
	}

}
