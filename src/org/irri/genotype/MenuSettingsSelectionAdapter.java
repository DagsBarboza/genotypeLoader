package org.irri.genotype;

import java.util.HashMap;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.irri.genotype.components.SettingsDialog;

public class MenuSettingsSelectionAdapter extends SelectionAdapter {

	private Shell shlGenotypeLoader;
	private Cache cache;
	private HashMap<String, String> properties;
	private GenotypeLoaderUI genotypeLoaderUI;
	private SettingsDialog dialog;
	private LoaderProperties prop;

	public MenuSettingsSelectionAdapter(Shell shlGenotypeLoader, Cache cache, HashMap<String, String> properties,
			GenotypeLoaderUI genotypeLoaderUI) {
		this.shlGenotypeLoader = shlGenotypeLoader;
		this.cache = cache;
		this.properties = properties;
		this.genotypeLoaderUI = genotypeLoaderUI;

	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		dialog = new SettingsDialog(shlGenotypeLoader, cache, properties, genotypeLoaderUI);
		if (Window.OK == dialog.open())
			prop = dialog.getProperties();

	}
	
	public LoaderProperties getPropertyFile() {
		return prop;
	}
	
	public void setPropertyFile(LoaderProperties prop) {
		this.prop = prop;
	}
}
