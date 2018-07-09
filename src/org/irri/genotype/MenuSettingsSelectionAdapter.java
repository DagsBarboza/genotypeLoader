package org.irri.genotype;

import java.util.HashMap;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.components.SettingsDialog;

public class MenuSettingsSelectionAdapter extends SelectionAdapter {

	private Shell shlGenotypeLoader;
	private Cache cache;
	private HashMap<String, String> properties;
	private GenotypeLoaderUI genotypeLoaderUI;

	public MenuSettingsSelectionAdapter(Shell shlGenotypeLoader, Cache cache, HashMap<String, String> properties,
			GenotypeLoaderUI genotypeLoaderUI) {
		this.shlGenotypeLoader = shlGenotypeLoader;
		this.cache = cache;
		this.properties = properties;
		this.genotypeLoaderUI = genotypeLoaderUI;

	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		SettingsDialog dialog = new SettingsDialog(shlGenotypeLoader, cache, genotypeLoaderUI);
		int answer = dialog.open();

	}
}
