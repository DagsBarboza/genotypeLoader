package org.irri.genotype;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.irri.genotype.components.AddFeatureTypeDialog;
import org.irri.genotype.components.FileChooserListener;
import org.irri.genotype.components.LoadingDialog;
import org.irri.genotype.components.dialog.AddCvTermDialog;
import org.irri.genotype.components.dialog.AddDbDialog;
import org.irri.genotype.components.dialog.AddOrganismDialog;
import org.irri.genotype.components.dialog.AddVariantSetDialog;
import org.irri.genotype.loader.utils.ComponentElementUtils;

import chado.loader.AppContext;
import chado.loader.model.Cvterm;
import chado.loader.model.Db;
import chado.loader.model.Organism;
import chado.loader.model.Variantset;
import chado.loader.service.CvService;
import chado.loader.service.CvTermService;
import chado.loader.service.DbService;
import chado.loader.service.OrganismService;
import chado.loader.service.VariantSetService;

public class GenotypeLoaderUI2 extends GenotypeLoaderUI {

	protected Shell shlGenotypeLoader;

	private DbService db_ds;
	private OrganismService org_ds;
	private CvTermService cvTerm_ds;
	private VariantSetService v_ds;

	private Display display;

	private FileChooserListener fcl;

	private FileChooserListener fcl2;
	private Text textNonSyn;
	private Text textSpliceAcc;
	private Text textSpliceDnr;

	private CvService cv_ds;

	private FileChooserListener fcl3;

	private FileChooserListener fcl4;

	private FileChooserListener fcl5;

	private Cache cache;

	private HashMap<String, String> properties;

	private Combo comboOrganism;

	private Combo comboCvTerm;

	private Combo comboDb;

	private Text txtSampleFile;

	private Text txtPosfile;

	private Combo comboVariantSet;

	private Text TextH5;

	private FileChooserListener fcl6;
	private Text textSnpGenotype;

	private FileChooserListener fcl7;

	private LoaderProperties prop;

	private MenuSettingsSelectionAdapter msa;

	private Label txtRiceSnpseekDatabase;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GenotypeLoaderUI2 window = new GenotypeLoaderUI2();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		cache = new Cache();
		display = Display.getDefault();
		createContents();
		initElements();
		shlGenotypeLoader.open();
		shlGenotypeLoader.layout();
		while (!shlGenotypeLoader.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
				// cache.close();
			}
		}
	}

	public void initElements() {

		db_ds = null;
		org_ds = null;
		cvTerm_ds = null;
		v_ds = null;
		cv_ds = null;

		db_ds = new DbService();
		org_ds = new OrganismService();
		cvTerm_ds = new CvTermService();
		v_ds = new VariantSetService();
		cv_ds = new CvService();

		ComponentElementUtils.initComboItem(comboOrganism, org_ds.getAllOrganismName().toArray());
		comboOrganism.setText("");

		ComponentElementUtils.initComboItem(comboCvTerm, cvTerm_ds.getAllCvTermName().toArray());
		comboCvTerm.setText("whole plant");

		ComponentElementUtils.initComboItem(comboDb, db_ds.getAllDbName().toArray());
		comboDb.setText("");

		txtSampleFile.setText("");

		txtPosfile.setText("");

		ComponentElementUtils.initComboItem(comboVariantSet, v_ds.getAllVariantSetName().toArray());
		comboVariantSet.setText("");

		textNonSyn.setText("");
		textSpliceAcc.setText("");
		textSpliceDnr.setText("");
		TextH5.setText("");
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		AppContext.createEntityManager();

		initConnectionMap();

		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 3;
		shlGenotypeLoader = new Shell();
		shlGenotypeLoader.setSize(728, 767);
		shlGenotypeLoader.setText("GENOTYPE LOADER");
		shlGenotypeLoader.setLayout(layout);

		Menu menu = new Menu(shlGenotypeLoader, SWT.BAR);
		shlGenotypeLoader.setMenuBar(menu);

		MenuItem mntmSettings = new MenuItem(menu, SWT.NONE);
		msa = new MenuSettingsSelectionAdapter(shlGenotypeLoader, cache, properties, this);
		mntmSettings.addSelectionListener(msa);
		msa.setPropertyFile(prop);

		db_ds = new DbService();
		org_ds = new OrganismService();
		cvTerm_ds = new CvTermService();
		v_ds = new VariantSetService();
		cv_ds = new CvService();

		mntmSettings.setText("Settings");
		new Label(shlGenotypeLoader, SWT.NONE);

		txtRiceSnpseekDatabase = new Label(shlGenotypeLoader, SWT.READ_ONLY | SWT.CENTER | SWT.None);
		txtRiceSnpseekDatabase.setFont(SWTResourceManager.getFont("Segoe UI Symbol", 14, SWT.BOLD));
		txtRiceSnpseekDatabase.setForeground(SWTResourceManager.getColor(0, 128, 0));
		txtRiceSnpseekDatabase.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		txtRiceSnpseekDatabase.setText(ComponentConstants.HEADER);
		GridData gd_txtRiceSnpseekDatabase = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtRiceSnpseekDatabase.heightHint = 50;
		gd_txtRiceSnpseekDatabase.widthHint = 130;
		txtRiceSnpseekDatabase.setLayoutData(gd_txtRiceSnpseekDatabase);
		new Label(shlGenotypeLoader, SWT.NONE);

		// ORGANISM
		Label labelOrganism = new Label(shlGenotypeLoader, SWT.NONE);
		labelOrganism.setText("Organism");

		comboOrganism = new Combo(shlGenotypeLoader, SWT.BORDER | SWT.DROP_DOWN);
		GridData gd_comboOrganism = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboOrganism.widthHint = 400;
		comboOrganism.setLayoutData(gd_comboOrganism);
		comboOrganism.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (comboOrganism.getText().equals(ComponentConstants.ADD_NEW)) {
					AddOrganismDialog dialog = new AddOrganismDialog(shlGenotypeLoader);

					if (Window.OK == dialog.open()) {
						comboOrganism.removeAll();

						ComponentElementUtils.initComboItem(comboOrganism, org_ds.getAllOrganismName().toArray());

					} else {
						comboOrganism.deselectAll();
					}

				}

				if (comboOrganism.getText().equals(ComponentConstants.COMBO_SEPARATOR))
					comboOrganism.deselectAll();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		comboOrganism.setToolTipText("test");
		new Label(shlGenotypeLoader, SWT.NONE);

		// CVTERM
		Label labelCvTerm = new Label(shlGenotypeLoader, SWT.NONE);
		labelCvTerm.setText("Cv Term (Organism)");

		comboCvTerm = new Combo(shlGenotypeLoader, SWT.NONE);
		GridData gd_comboCvTerm = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboCvTerm.widthHint = 400;
		comboCvTerm.setLayoutData(gd_comboCvTerm);
		comboCvTerm.setText("whole plant");
		comboCvTerm.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (comboCvTerm.getText().equals(ComponentConstants.ADD_NEW)) {
					AddCvTermDialog dialog = new AddCvTermDialog(shlGenotypeLoader);
					if (Window.OK == dialog.open()) {
						comboCvTerm.removeAll();

						ComponentElementUtils.initComboItem(comboCvTerm, cvTerm_ds.getAllCvTermName().toArray());

					} else {
						comboCvTerm.deselectAll();
					}

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				comboCvTerm.setText("whole plant");

			}
		});
		
		
		
		new Label(shlGenotypeLoader, SWT.NONE);

		// DB
		Label labelDb = new Label(shlGenotypeLoader, SWT.NONE);
		labelDb.setText("Database");

		comboDb = new Combo(shlGenotypeLoader, SWT.NONE);
		GridData gd_comboDb = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboDb.widthHint = 400;
		comboDb.setLayoutData(gd_comboDb);
		comboDb.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (comboDb.getText().equals(ComponentConstants.ADD_NEW)) {
					AddDbDialog dialog = new AddDbDialog(shlGenotypeLoader);
					if (Window.OK == dialog.open()) {
						comboDb.removeAll();

						ComponentElementUtils.initComboItem(comboDb, db_ds.getAllDbName().toArray());

					} else {
						comboDb.deselectAll();
					}

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		new Label(shlGenotypeLoader, SWT.NONE);

		Label lblVariantSet = new Label(shlGenotypeLoader, SWT.NONE);
		lblVariantSet.setText("Variant Set");

		comboVariantSet = new Combo(shlGenotypeLoader, SWT.NONE);
		GridData gd_comboVariantSet = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboVariantSet.widthHint = 400;
		comboVariantSet.setLayoutData(gd_comboVariantSet);
		comboVariantSet.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (comboVariantSet.getText().equals(ComponentConstants.ADD_NEW)) {
					AddVariantSetDialog dialog = new AddVariantSetDialog(shlGenotypeLoader);
					if (Window.OK == dialog.open()) {
						comboVariantSet.removeAll();

						ComponentElementUtils.initComboItem(comboVariantSet, v_ds.getAllVariantSetName().toArray());

					} else {
						comboVariantSet.deselectAll();
					}

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new Label(shlGenotypeLoader, SWT.NONE);

		Label label = new Label(shlGenotypeLoader, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_label.widthHint = 980;
		label.setLayoutData(gd_label);
		label.setText("--------------");

		Label fileSelectlbl = new Label(shlGenotypeLoader, SWT.READ_ONLY | SWT.CENTER | SWT.None);
		fileSelectlbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		fileSelectlbl.setAlignment(SWT.LEFT);
		fileSelectlbl.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.ITALIC));
		fileSelectlbl.setText("Select Files:");
		GridData gd_txtfileSelect = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtfileSelect.heightHint = 28;
		gd_txtfileSelect.widthHint = 130;
		fileSelectlbl.setLayoutData(gd_txtfileSelect);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);

		Label labelrawFile = new Label(shlGenotypeLoader, SWT.NONE);
		labelrawFile.setText("Select Sample File");

		txtSampleFile = new Text(shlGenotypeLoader, SWT.NONE);
		txtSampleFile.setEditable(false);
		txtSampleFile.setEnabled(true);
		txtSampleFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_txtSampleFile = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_txtSampleFile.widthHint = 420;
		txtSampleFile.setLayoutData(gd_txtSampleFile);
		fcl = new FileChooserListener(txtSampleFile);

		Button btnSampleSelect = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnSampleSelect = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSampleSelect.widthHint = 129;
		btnSampleSelect.setLayoutData(gd_btnSampleSelect);
		btnSampleSelect.setText("browse");
		btnSampleSelect.addListener(SWT.Selection, fcl);

		Label labelPosFile = new Label(shlGenotypeLoader, SWT.NONE);
		labelPosFile.setText("Select Position File");

		txtPosfile = new Text(shlGenotypeLoader, SWT.NONE);
		txtPosfile.setEditable(false);
		txtPosfile.setEnabled(true);
		txtPosfile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_txtPosfile = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_txtPosfile.widthHint = 420;
		txtPosfile.setLayoutData(gd_txtPosfile);
		fcl2 = new FileChooserListener(txtPosfile);

		Button btnPosFile = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnPosFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPosFile.widthHint = 128;
		btnPosFile.setLayoutData(gd_btnPosFile);
		btnPosFile.setText("browse");
		btnPosFile.addListener(SWT.Selection, fcl2);

		Label lblNonsynonymousVariant = new Label(shlGenotypeLoader, SWT.NONE);
		lblNonsynonymousVariant.setText("Nonsynonymous Variant");
		lblNonsynonymousVariant.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		textNonSyn = new Text(shlGenotypeLoader, SWT.NONE);
		textNonSyn.setEditable(false);
		GridData gd_textNonSyn = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_textNonSyn.widthHint = 420;
		textNonSyn.setLayoutData(gd_textNonSyn);
		textNonSyn.setEnabled(true);
		textNonSyn.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		fcl3 = new FileChooserListener(textNonSyn);
		Button btnNonSynBrowse = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnNonSynBrowse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNonSynBrowse.widthHint = 128;
		btnNonSynBrowse.setLayoutData(gd_btnNonSynBrowse);
		btnNonSynBrowse.setText("browse");
		btnNonSynBrowse.addListener(SWT.Selection, fcl3);

		Label lblSpliceAcceptorVariant = new Label(shlGenotypeLoader, SWT.NONE);
		lblSpliceAcceptorVariant.setText("Splice Acceptor Variant");

		textSpliceAcc = new Text(shlGenotypeLoader, SWT.NONE);
		textSpliceAcc.setEditable(false);
		GridData gd_textSpliceAcc = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_textSpliceAcc.widthHint = 420;
		textSpliceAcc.setLayoutData(gd_textSpliceAcc);
		textSpliceAcc.setEnabled(true);
		textSpliceAcc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		fcl4 = new FileChooserListener(textSpliceAcc);
		Button btnBrowseSpliceAcc = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnBrowseSpliceAcc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowseSpliceAcc.widthHint = 128;
		btnBrowseSpliceAcc.setLayoutData(gd_btnBrowseSpliceAcc);
		btnBrowseSpliceAcc.setText("browse");
		btnBrowseSpliceAcc.addListener(SWT.Selection, fcl4);

		Label lblSpliceDonorVariant = new Label(shlGenotypeLoader, SWT.NONE);
		lblSpliceDonorVariant.setText("Splice Donor Variant");

		textSpliceDnr = new Text(shlGenotypeLoader, SWT.NONE);
		textSpliceDnr.setEditable(false);
		GridData gd_textSpliceDnr = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_textSpliceDnr.widthHint = 420;
		textSpliceDnr.setLayoutData(gd_textSpliceDnr);
		textSpliceDnr.setEnabled(true);
		textSpliceDnr.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		fcl5 = new FileChooserListener(textSpliceDnr);
		Button btnBrowseSpliceDnr = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnBrowseSpliceDnr = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowseSpliceDnr.widthHint = 129;
		btnBrowseSpliceDnr.setLayoutData(gd_btnBrowseSpliceDnr);
		btnBrowseSpliceDnr.setText("browse");
		btnBrowseSpliceDnr.addListener(SWT.Selection, fcl5);

		Label lblH = new Label(shlGenotypeLoader, SWT.NONE);
		lblH.setText("H5 File");

		TextH5 = new Text(shlGenotypeLoader, SWT.NONE);
		TextH5.setEditable(false);
		GridData gd_TextH5 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_TextH5.widthHint = 420;
		TextH5.setLayoutData(gd_TextH5);
		TextH5.setEnabled(true);
		TextH5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fcl6 = new FileChooserListener(TextH5);

		Button btnH5 = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnH5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnH5.widthHint = 127;
		btnH5.setLayoutData(gd_btnH5);
		btnH5.addListener(SWT.Selection, fcl6);
		btnH5.setText("browse");

		Label lblSnpGenotypeFile = new Label(shlGenotypeLoader, SWT.NONE);
		lblSnpGenotypeFile.setText("Snp Genotype File");

		textSnpGenotype = new Text(shlGenotypeLoader, SWT.NONE);
		textSnpGenotype.setEditable(false);
		GridData gd_textSnpGenotype = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_textSnpGenotype.widthHint = 420;
		textSnpGenotype.setLayoutData(gd_textSnpGenotype);
		textSnpGenotype.setEnabled(true);
		textSnpGenotype.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		fcl7 = new FileChooserListener(textSnpGenotype);
		Button btnGenFile = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnGenFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnGenFile.widthHint = 128;
		btnGenFile.setLayoutData(gd_btnGenFile);
		btnGenFile.setText("browse");
		btnGenFile.addListener(SWT.Selection, fcl7);

		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);

		Button resetButton = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_resetButton = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_resetButton.widthHint = 117;
		resetButton.setLayoutData(gd_resetButton);
		resetButton.setText("RESET");
		resetButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				initElements();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		Button btnRun = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnRun = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRun.widthHint = 127;
		btnRun.setLayoutData(gd_btnRun);
		btnRun.setText("RUN");
		btnRun.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				Integer errType = checkInitValues();

				if (errType == 0) {
					Cvterm cvterm = cvTerm_ds.findCvtermyName(comboCvTerm.getText()).get(0);
					Organism organism = org_ds.findOrganismByCommonName(comboOrganism.getText()).get(0);
					Db db = db_ds.getDbByName(comboDb.getText()).get(0);
					Variantset vSet = v_ds.getVariantSetByName(comboVariantSet.getText()).get(0);
					List<Cvterm> featureCvTermList = cvTerm_ds.getFeatureType(ComponentConstants.CHROMOSOME,
							ComponentConstants.SEQUENCE);

					Cvterm nonSynTerm = cvTerm_ds.findCvtermyName(ComponentConstants.NON_SYNONYMOUS).get(0);
					Cvterm spliceAccTerm = cvTerm_ds.findCvtermyName(ComponentConstants.SPLICE_ACC).get(0);
					Cvterm spliceDnrTerm = cvTerm_ds.findCvtermyName(ComponentConstants.SPLICE_DNR).get(0);

					if (!featureCvTermList.isEmpty()) {
						Cvterm featureCvTerm = featureCvTermList.get(0);
						LoadingDialog loading = new LoadingDialog(display, shlGenotypeLoader, fcl, fcl2, fcl3, fcl4,
								fcl5, fcl6, cvterm, organism, db, vSet, featureCvTerm, nonSynTerm, spliceAccTerm,
								spliceDnrTerm, msa.getPropertyFile());
						loading.open();
					} else {
						AddFeatureTypeDialog loading = new AddFeatureTypeDialog(display, shlGenotypeLoader, fcl, fcl2,
								cvterm, organism, db, vSet);
						loading.open();
					}
				} else {

					if (errType == ErrorType.EMPTY_ERROR)
						MessageDialog.openError(shlGenotypeLoader, "Error", "Do not leave mandatory fields empty !");
					if (errType == ErrorType.NO_FILE)
						MessageDialog.openError(shlGenotypeLoader, "Error", "Cannot find file!");

					// WarningDialog warningDialog = new WarningDialog(shlGenotypeLoader, errType);
					// warningDialog.open();
				}

			}

			private Integer checkInitValues() {

				Integer err = checkComboBoxes();
				Integer err2 = checkFileBoxes();

				if (err != 0)
					return err;

				if (err2 != 0)
					return err2;

				return 0;

			}

			private Integer checkFileBoxes() {
				if (checkFile(textNonSyn) != 0)
					return ErrorType.NO_FILE;

				if (checkFile(textSnpGenotype) != 0)
					return ErrorType.NO_FILE;

				if (checkFile(textSpliceAcc) != 0)
					return ErrorType.NO_FILE;

				if (checkFile(textSpliceDnr) != 0)
					return ErrorType.NO_FILE;

				if (checkFile(TextH5) != 0)
					return ErrorType.NO_FILE;

				if (checkFile(txtSampleFile) != 0)
					return ErrorType.NO_FILE;

				if (checkFile(txtPosfile) != 0)
					return ErrorType.NO_FILE;

				return 0;
			}

			private int checkFile(Text txtBox) {

				if (!txtBox.getText().equals("")) {
					if (!new File(txtBox.getText()).exists())
						return ErrorType.NO_FILE;
				}

				return 0;
			}

			private Integer checkComboBoxes() {
				if (comboOrganism.getText().isEmpty() || comboDb.getText().isEmpty()
						|| comboVariantSet.getText().isEmpty())
					return ErrorType.EMPTY_ERROR;
				return 0;

			}
		});

	}

	private void initConnectionMap() {
		properties = new HashMap<String, String>();
		properties.put(LoaderConstants.HOSTNAME, ComponentConstants.HOSTNAME);
		properties.put(LoaderConstants.PORT, ComponentConstants.PORT);
		properties.put(LoaderConstants.DATABASE, ComponentConstants.DATABASE);
		properties.put(LoaderConstants.USER, ComponentConstants.USERNAME);
		properties.put(LoaderConstants.PASSWORD, ComponentConstants.PASSWORD);

		properties.put(LoaderConstants.CONFIG_NAME, "DEFAULT");

		prop = new LoaderProperties();
		prop.setDatabasename(ComponentConstants.HOSTNAME);
		prop.setPort(ComponentConstants.PORT);
		prop.setDatabasename(ComponentConstants.DATABASE);
		prop.setUsername(ComponentConstants.USERNAME);
		prop.setPassword(ComponentConstants.PASSWORD);

	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

}
