package org.irri.genotype.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DropDownBoxGenotype extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DropDownBoxGenotype(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
