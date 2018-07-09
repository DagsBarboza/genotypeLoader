package org.irri.genotype;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.page.LoadingSamplePage;

import chado.loader.AppContext;
import javafx.scene.control.Dialog;

public class GenotypeLoaderWizardUI {

	

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = Display.getDefault();
		Shell shlGenotypeLoader = new Shell();
		shlGenotypeLoader.setSize(585, 437);

		AppContext.createEntityManager();
		
		final GenotypeLoaderWizard gnpWizard = new GenotypeLoaderWizard(display, shlGenotypeLoader);
		WizardDialog dialog = new WizardDialog(shlGenotypeLoader, gnpWizard);
		dialog.addPageChangedListener(new IPageChangedListener() {

			@Override
			public void pageChanged(PageChangedEvent e) {
				IWizardPage page = gnpWizard.getContainer().getCurrentPage();
				if (page instanceof LoadingSamplePage) {
					((LoadingSamplePage) page).readFile();
				}
			}
		});
		dialog.open();

		while (!shlGenotypeLoader.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.dispose();
			}
		}
	}
}
