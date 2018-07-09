package org.irri.genotype.components;

import java.io.File;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class FileChooserListener implements Listener {

	public File file;

	public Text logTextBox;

	public FileChooserListener(Text logTextBox) {
		this.logTextBox = logTextBox;
	}

	@Override
	public void handleEvent(Event e) {
		switch (e.type) {
		case SWT.Selection:
			final JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(new FileChooserDemo());
			System.out.println("RETURN VAL"+ returnVal);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				logTextBox.setText(file.getAbsolutePath());
			}
			break;
		}

	}

	public File getFile() {
		return file;
	}

}
