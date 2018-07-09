package org.irri.genotype;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.page.LoadingSamplePage;
import org.irri.genotype.page.SelectGenotypePage;

public class GenotypeLoaderWizard extends Wizard implements IWizard {

	private Display display;
	private Shell shell;

	public GenotypeLoaderWizard(Display display, Shell shlGenotypeLoader) {
		this.display = display;
		this.shell = shlGenotypeLoader;
		
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		SelectGenotypePage page1 = new SelectGenotypePage("Select Genotype", shell);
		LoadingSamplePage page2 = new LoadingSamplePage("Loading Sample", page1, display);
		addPage(page1);
		addPage(page2);

	}

	@Override
	public boolean performFinish() {

		return true;
	}

	public boolean performCancel() {
		boolean ans = MessageDialog.openConfirm(getShell(), "Confirmation", "Are you sure to cancel the task?");
		if (ans)
			return true;
		else
			return false;
	}

}
