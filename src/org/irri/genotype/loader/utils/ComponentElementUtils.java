package org.irri.genotype.loader.utils;

import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.swt.widgets.Combo;
import org.irri.genotype.ComponentConstants;

public class ComponentElementUtils {

	// public static HashMap<Integer, Object> comboBoxInit(Combo comboBox,
	// HashMap<Integer, Object> map) {
	//
	// return map;
	//
	// }

	public static void initComboItem(Combo combo, Object[] items) {

		String[] comboContent = new String[items.length + 2];
		comboContent[0] = ComponentConstants.ADD_NEW;
		comboContent[1] = ComponentConstants.COMBO_SEPARATOR;
		System.arraycopy(items, 0, comboContent, 2, items.length);

		combo.setVisibleItemCount(30);
		new AutoCompleteField(combo, new ComboContentAdapter(), comboContent);
		String[] trimArray = Arrays.copyOfRange(comboContent, 0, 30);
		if (comboContent.length > 100)
			combo.setItems(trimArray);
		else
			combo.setItems(comboContent);

	}

}
