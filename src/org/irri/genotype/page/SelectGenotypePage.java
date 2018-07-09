package org.irri.genotype.page;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.irri.genotype.components.FileChooserListener;
import org.irri.genotype.components.WizardLabels;
import org.irri.genotype.components.dialog.AddCvTermDialog;
import org.irri.genotype.components.dialog.AddDbDialog;
import org.irri.genotype.components.dialog.AddOrganismDialog;
import org.irri.genotype.components.dialog.AddVariantSetDialog;

import chado.loader.model.Cvterm;
import chado.loader.model.Db;
import chado.loader.model.Organism;
import chado.loader.model.Variantset;
import chado.loader.service.CvTermService;
import chado.loader.service.DbService;
import chado.loader.service.OrganismService;
import chado.loader.service.VariantSetService;
import javafx.scene.control.TableColumn;

public class SelectGenotypePage extends WizardPage implements IWizardPage {

	private Combo cvTermCombo;
	private Combo dbCombo;
	public Combo organismCombo;
	private Combo variantSetCombo;

	private DbService dbs;
	private CvTermService cvTermDs;
	private VariantSetService vSetDs;

	private FileChooserListener fcl;
	private FileChooserListener fcl2;

	private HashMap<Integer, Organism> organismMap;
	private HashMap<Integer, Variantset> variantSetMap;
	private HashMap<Integer, Cvterm> cvTermMap;
	private OrganismService orgDs;
	private Shell parentShell;

	private Text text;
	private Text txtPosition;

	public SelectGenotypePage(String pageName, Shell parentShell) {
		super(pageName);

		URL imgUrl = getClass().getResource(WizardLabels.IMG);
		ImageDescriptor imgDescriptor = ImageDescriptor.createFromURL(imgUrl);

		setTitle(WizardLabels.GENOTYPE_PAGE_TITLE);
		setDescription(WizardLabels.GENOTYPE_PAGE_DESC);

		setImageDescriptor(imgDescriptor);
		this.parentShell = parentShell;

		cvTermMap = new HashMap<>();
		organismMap = new HashMap<>();
		variantSetMap = new HashMap<>();

		cvTermDs = new CvTermService();
		vSetDs = new VariantSetService();
		dbs = new DbService();

	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(3, false));

		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		// init
		initSelectOrganism(composite);
		initselectCvTerm(composite);
		initselectDB(composite);
		initSelectRawFile(composite);
		initSelectPositionFile(composite);
		initselectVariantSet(composite);

		setControl(composite);
		setPageComplete(false);

	}

	public Combo getCvTermCombo() {
		return cvTermCombo;
	}

	public HashMap<Integer, Cvterm> getCvTermMap() {
		return cvTermMap;
	}

	public HashMap<Integer, Variantset> getVariantSetMap() {
		return variantSetMap;
	}

	public Combo getDbCombo() {
		return dbCombo;
	}

	public Combo getVSetCombo() {
		return variantSetCombo;
	}

	public File getSampleFile() {
		return fcl.getFile();
	}

	public File getPositionFile() {
		return fcl2.getFile();
	}

	public Combo getOrganismCombo() {
		return organismCombo;
	}

	public HashMap<Integer, Organism> getOrganismMap() {
		return organismMap;
	}

	private void initItemsCvTermComboBox() {
		cvTermCombo.removeAll();
		cvTermMap.clear();

		int index = 0;
		for (Cvterm cvTerm : cvTermDs.findAll()) {
			cvTermCombo.add(cvTerm.getName(), index);
			cvTermMap.put(index, cvTerm);
		}

	}

	private void initItemsDbComboBox() {
		dbCombo.removeAll();

		for (Db db : dbs.getAllDb()) {
			dbCombo.add(db.getName());

		}

	}

	private void initItemsOrganismComboBox() {
		organismCombo.removeAll();

		organismMap.clear();
		int index = 0;
		for (Organism org : orgDs.getAllOrganism()) {
			organismCombo.add(org.getCommonName(), index);
			organismMap.put(index, org);
		}

	}

	private void initItemsVariantSetComboBox() {
		variantSetCombo.removeAll();
		int index = 0;
		for (Variantset vSet : vSetDs.getAllVariantSet()) {
			variantSetCombo.add(vSet.getName(), index);
			variantSetMap.put(index, vSet);
		}

	}

	private void initselectCvTerm(Composite composite) {

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("CV Term");

		cvTermCombo = new Combo(composite, SWT.NONE);
		cvTermCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 92;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("New CV Term");
		btnNewButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {

				AddCvTermDialog dialog = new AddCvTermDialog(parentShell);
				dialog.open();

				initItemsCvTermComboBox();

			}

		});

		// init items in comboBox
		initItemsCvTermComboBox();

	}

	private void initselectDB(Composite composite) {

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Database");

		dbCombo = new Combo(composite, SWT.NONE);
		dbCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 91;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("New Database");
		btnNewButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {

				AddDbDialog dialog = new AddDbDialog(parentShell);
				dialog.open();

				initItemsDbComboBox();

			}
		});

		// init items in comboBox
		initItemsDbComboBox();

	}

	private void initSelectOrganism(Composite composite) {
		orgDs = new OrganismService();

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Organism");

		organismCombo = new Combo(composite, SWT.NONE);
		organismCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("New Organism");
		btnNewButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {

				AddOrganismDialog dialog = new AddOrganismDialog(parentShell);
				dialog.open();

				initItemsOrganismComboBox();

			}

		});

		// init items in comboBox
		initItemsOrganismComboBox();

	}

	private void initSelectPositionFile(Composite composite) {
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Select Position File");

		txtPosition = new Text(composite, SWT.BORDER);
		txtPosition.setEditable(false);
		txtPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtPosition.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				// if (!txtPosition.getText().isEmpty())
				// setPageComplete(true);
				// else
				// setPageComplete(false);
			}
		});

		fcl2 = new FileChooserListener(txtPosition);

		Button sampleBrwseBtn = new Button(composite, SWT.NONE);
		GridData gd_sampleBrwseBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sampleBrwseBtn.widthHint = 93;
		sampleBrwseBtn.setLayoutData(gd_sampleBrwseBtn);
		sampleBrwseBtn.setText("browse");
		sampleBrwseBtn.addListener(SWT.Selection, fcl2);
	}

	private void initSelectRawFile(Composite composite) {
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Select Genotype");

		text = new Text(composite, SWT.BORDER);
		text.setEditable(false);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!text.getText().isEmpty())
					setPageComplete(true);
				else
					setPageComplete(false);
			}
		});

		fcl = new FileChooserListener(text);

		Button sampleBrwseBtn = new Button(composite, SWT.NONE);
		GridData gd_sampleBrwseBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sampleBrwseBtn.widthHint = 91;
		sampleBrwseBtn.setLayoutData(gd_sampleBrwseBtn);
		sampleBrwseBtn.setText("browse");
		sampleBrwseBtn.addListener(SWT.Selection, fcl);

	}

	private void initselectVariantSet(Composite composite) {

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Variant Set");

		variantSetCombo = new Combo(composite, SWT.NONE);
		variantSetCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("New Variant Set");
		btnNewButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {

				AddVariantSetDialog dialog = new AddVariantSetDialog(parentShell);
				dialog.open();

				initItemsVariantSetComboBox();

			}

		});

		initItemsVariantSetComboBox();

	}

}
