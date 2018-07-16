package org.irri.genotype;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
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
import org.irri.genotype.components.SettingsDialog;
import org.irri.genotype.components.dialog.AddCvTermDialog;
import org.irri.genotype.components.dialog.AddDbDialog;
import org.irri.genotype.components.dialog.AddOrganismDialog;
import org.irri.genotype.components.dialog.AddVariantSetDialog;

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

public class GenotypeLoaderUI {

	private static final String HOSTNAME = "172.29.4.215";

	private static final String PORT = "5432";

	private static final String DATABASE = "lbarboza";

	private static final String USERNAME = "iric";

	private static final String PASSWORD = "iric-dev";

	protected Shell shlGenotypeLoader;

	// private static final String LCL = "abcdefghijklmnopqrstuvwxyz";
	// private static final String UCL = LCL.toUpperCase();
	// private static final String NUMS = "0123456789";

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

	private Combo comboCvTermFeature;

	private Combo comboCvFeature;
	private Text TextH5;

	private FileChooserListener fcl6;
	private Text textSnpGenotype;

	private FileChooserListener fcl7;

	private LoaderProperties prop;

	private MenuSettingsSelectionAdapter msa;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GenotypeLoaderUI window = new GenotypeLoaderUI();
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

		initComboItem(comboOrganism, org_ds.getAllOrganismName().toArray());
		comboOrganism.setText("");

		initComboItem(comboCvTerm, cvTerm_ds.getAllCvTermName().toArray());
		comboCvTerm.setText("");

		initComboItem(comboDb, db_ds.getAllDbName().toArray());
		comboDb.setText("");

		txtSampleFile.setText("");

		txtPosfile.setText("");

		initComboItem(comboVariantSet, v_ds.getAllVariantSetName().toArray());
		comboVariantSet.setText("");

		// initComboItem(comboCvTermFeature, cvTerm_ds.getAllCvTermName().toArray());
		// comboCvTermFeature.setText("");
		//
		// initComboItem(comboCvFeature, cv_ds.findAllName().toArray());
		// comboCvFeature.setText("");

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
		shlGenotypeLoader = new Shell();
		shlGenotypeLoader.setSize(1013, 767);
		shlGenotypeLoader.setText("GENOTYPE LOADER");
		shlGenotypeLoader.setLayout(layout);

		Menu menu = new Menu(shlGenotypeLoader, SWT.BAR);
		shlGenotypeLoader.setMenuBar(menu);

		MenuItem mntmSettings = new MenuItem(menu, SWT.NONE);
		msa = new MenuSettingsSelectionAdapter(shlGenotypeLoader, cache, properties, this);
		mntmSettings.addSelectionListener(msa);
		msa.setPropertyFile(prop);

		// mntmSettings.addSelectionListener(new SelectionAdapter() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		//
		// dialog = new SettingsDialog(shlGenotypeLoader, cache, properties);
		// int answer = dialog.open();
		//
		// if (answer == 0) {
		// initializeItems();
		// properties.put(LoaderConstants.HOSTNAME, dialog.getHostname());
		// properties.put(LoaderConstants.PORT, dialog.getPort());
		// properties.put(LoaderConstants.DATABASE, dialog.getDatabasename());
		// properties.put(LoaderConstants.USER, dialog.getUsername());
		// properties.put(LoaderConstants.PASSWORD, dialog.getPassword());
		//
		// properties.put(LoaderConstants.CONFIG_NAME, dialog.getConfigName());
		// }
		//
		// }
		//
		// });

		/**
		 * 
		 */
		db_ds = new DbService();
		org_ds = new OrganismService();
		cvTerm_ds = new CvTermService();
		v_ds = new VariantSetService();
		cv_ds = new CvService();

		mntmSettings.setText("Settings");
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);
		new Label(shlGenotypeLoader, SWT.NONE);

		// ORGANISM
		Label labelOrganism = new Label(shlGenotypeLoader, SWT.NONE);
		labelOrganism.setText("Organism");

		comboOrganism = new Combo(shlGenotypeLoader, SWT.BORDER | SWT.DROP_DOWN);
		comboOrganism.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOrganism.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {

				String text = comboOrganism.getText();
				boolean found = false;

				if (text == null || text.equals(""))
					return;

				String current = "";
				String[] items = comboOrganism.getItems();

				int caretPosition = comboOrganism.getSelection().x;

				for (int i = 0; i < items.length && !found; i++) {
					current = items[i];

					if (current.toUpperCase().startsWith(text.toUpperCase())) {
						comboOrganism.setText(current);
						found = true;
					}
				}
				if (found) {
					comboOrganism.setSelection(new Point(caretPosition, 256));

				}

			}
		});

		Button btnNewOrganism = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnNewOrganism = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewOrganism.widthHint = 130;
		btnNewOrganism.setLayoutData(gd_btnNewOrganism);
		btnNewOrganism.setText("New Organism");
		btnNewOrganism.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				AddOrganismDialog dialog = new AddOrganismDialog(shlGenotypeLoader);
				if (Window.OK == dialog.open()) {
					comboOrganism.removeAll();

					initComboItem(comboOrganism, org_ds.getAllOrganismName().toArray());
					
					
				}

				
			}
		});

		// CVTERM
		Label labelCvTerm = new Label(shlGenotypeLoader, SWT.NONE);
		labelCvTerm.setText("Cv Term (Organism)");

		comboCvTerm = new Combo(shlGenotypeLoader, SWT.NONE);
		comboCvTerm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewCvTerm = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnNewCvTerm = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewCvTerm.widthHint = 129;
		btnNewCvTerm.setLayoutData(gd_btnNewCvTerm);
		btnNewCvTerm.setText("New Cv Term");
		btnNewCvTerm.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				AddCvTermDialog dialog = new AddCvTermDialog(shlGenotypeLoader);
				if (Window.OK == dialog.open()) {
					comboCvTerm.removeAll();

					initComboItem(comboCvTerm, cvTerm_ds.getAllCvTermName().toArray());
					
					
				}

				

			}
		});

		// DB
		Label labelDb = new Label(shlGenotypeLoader, SWT.NONE);
		labelDb.setText("Database");

		comboDb = new Combo(shlGenotypeLoader, SWT.NONE);
		comboDb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewDb = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnNewDb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewDb.widthHint = 130;
		btnNewDb.setLayoutData(gd_btnNewDb);
		btnNewDb.setText("New Db");
		btnNewDb.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				AddDbDialog dialog = new AddDbDialog(shlGenotypeLoader);
				if (Window.OK == dialog.open()) {
					comboDb.removeAll();
					
					initComboItem(comboDb, db_ds.getAllDbName().toArray());
					
					
				}



			}
		});

		Label labelrawFile = new Label(shlGenotypeLoader, SWT.NONE);
		labelrawFile.setText("Select Sample File");

		txtSampleFile = new Text(shlGenotypeLoader, SWT.NONE);
		txtSampleFile.setEnabled(true);
		txtSampleFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtSampleFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
		txtPosfile.setEnabled(true);
		txtPosfile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtPosfile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fcl2 = new FileChooserListener(txtPosfile);

		Button btnPosFile = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnPosFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPosFile.widthHint = 128;
		btnPosFile.setLayoutData(gd_btnPosFile);
		btnPosFile.setText("browse");
		btnPosFile.addListener(SWT.Selection, fcl2);

		Label lblVariantSet = new Label(shlGenotypeLoader, SWT.NONE);
		lblVariantSet.setText("Variant Set");

		comboVariantSet = new Combo(shlGenotypeLoader, SWT.NONE);
		comboVariantSet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewVariantSet = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnNewVariantSet = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewVariantSet.widthHint = 128;
		btnNewVariantSet.setLayoutData(gd_btnNewVariantSet);
		btnNewVariantSet.setText("New Variant Set");
		btnNewVariantSet.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				AddVariantSetDialog dialog = new AddVariantSetDialog(shlGenotypeLoader);
				if (Window.OK == dialog.open()) {
					comboVariantSet.removeAll();

					initComboItem(comboVariantSet, v_ds.getAllVariantSet().toArray());
					
					
				}




			}
		});

		Label label = new Label(shlGenotypeLoader, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_label.widthHint = 980;
		label.setLayoutData(gd_label);
		label.setText("--------------");

		Label lblFeatureType = new Label(shlGenotypeLoader, SWT.NONE);
		lblFeatureType.setText("Feature Type");

		comboCvTermFeature = new Combo(shlGenotypeLoader, SWT.NONE);
		comboCvTermFeature.setEnabled(false);
		comboCvTermFeature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewFeatureType = new Button(shlGenotypeLoader, SWT.NONE);
		btnNewFeatureType.setEnabled(false);
		GridData gd_btnNewFeatureType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewFeatureType.widthHint = 129;
		btnNewFeatureType.setLayoutData(gd_btnNewFeatureType);
		btnNewFeatureType.setText("CV Term");
		btnNewFeatureType.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				AddCvTermDialog dialog = new AddCvTermDialog(shlGenotypeLoader);
				if (Window.OK == dialog.open()) {
					comboCvTermFeature.removeAll();

					initComboItem(comboCvTermFeature, cvTerm_ds.getAllCvTermName().toArray());
					
					
				}

				

			}
		});

		comboCvTermFeature.setText("chromosome");

		new Label(shlGenotypeLoader, SWT.NONE);

		comboCvFeature = new Combo(shlGenotypeLoader, SWT.NONE);
		comboCvFeature.setEnabled(false);
		comboCvFeature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewCv = new Button(shlGenotypeLoader, SWT.NONE);
		btnNewCv.setEnabled(false);
		GridData gd_btnNewCv = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewCv.widthHint = 128;
		btnNewCv.setLayoutData(gd_btnNewCv);
		btnNewCv.setText("New Cv");
		btnNewCv.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				AddCvTermDialog dialog = new AddCvTermDialog(shlGenotypeLoader);
				if (Window.OK == dialog.open()) {
					comboCvFeature.removeAll();

					initComboItem(comboCvFeature, cv_ds.findAllName().toArray());
					
					
				}




			}
		});

		comboCvFeature.setText("sequence");

		Label label_1 = new Label(shlGenotypeLoader, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_label_1.widthHint = 989;
		label_1.setLayoutData(gd_label_1);
		label_1.setText("--------------");

		Label lblNonsynonymousVariant = new Label(shlGenotypeLoader, SWT.NONE);
		lblNonsynonymousVariant.setText("Nonsynonymous Variant");
		lblNonsynonymousVariant.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		textNonSyn = new Text(shlGenotypeLoader, SWT.NONE);
		textNonSyn.setEnabled(true);
		textNonSyn.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textNonSyn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		fcl3 = new FileChooserListener(textNonSyn);
		Button btnNonSynBrowse = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnNonSynBrowse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNonSynBrowse.widthHint = 128;
		btnNonSynBrowse.setLayoutData(gd_btnNonSynBrowse);
		btnNonSynBrowse.setText("browse");
		btnNonSynBrowse.addListener(SWT.Selection, fcl3);

		new Label(shlGenotypeLoader, SWT.NONE);

		final Combo comboNonSyn = new Combo(shlGenotypeLoader, SWT.NONE);
		comboNonSyn.setEnabled(false);
		comboNonSyn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboNonSyn.add("nonsynonymous_variant", 0);
		comboNonSyn.select(0);

		Button button = new Button(shlGenotypeLoader, SWT.NONE);
		button.setEnabled(false);
		GridData gd_button = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button.widthHint = 127;
		button.setLayoutData(gd_button);
		button.setText("CV Term");

		Label lblSpliceAcceptorVariant = new Label(shlGenotypeLoader, SWT.NONE);
		lblSpliceAcceptorVariant.setText("Splice Acceptor Variant");

		textSpliceAcc = new Text(shlGenotypeLoader, SWT.NONE);
		textSpliceAcc.setEnabled(true);
		textSpliceAcc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textSpliceAcc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		fcl4 = new FileChooserListener(textSpliceAcc);
		Button btnBrowseSpliceAcc = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnBrowseSpliceAcc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowseSpliceAcc.widthHint = 128;
		btnBrowseSpliceAcc.setLayoutData(gd_btnBrowseSpliceAcc);
		btnBrowseSpliceAcc.setText("browse");
		btnBrowseSpliceAcc.addListener(SWT.Selection, fcl4);
		new Label(shlGenotypeLoader, SWT.NONE);

		final Combo comboSpliceAcc = new Combo(shlGenotypeLoader, SWT.NONE);
		comboSpliceAcc.setEnabled(false);
		comboSpliceAcc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSpliceAcc.add("splice_acceptor_variant");
		comboSpliceAcc.select(0);

		Button button_1 = new Button(shlGenotypeLoader, SWT.NONE);
		button_1.setEnabled(false);
		GridData gd_button_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button_1.widthHint = 127;
		button_1.setLayoutData(gd_button_1);
		button_1.setText("CV Term");

		Label lblSpliceDonorVariant = new Label(shlGenotypeLoader, SWT.NONE);
		lblSpliceDonorVariant.setText("Splice Donor Variant");

		textSpliceDnr = new Text(shlGenotypeLoader, SWT.NONE);
		textSpliceDnr.setEnabled(true);
		textSpliceDnr.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textSpliceDnr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		fcl5 = new FileChooserListener(textSpliceDnr);
		Button btnBrowseSpliceDnr = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnBrowseSpliceDnr = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowseSpliceDnr.widthHint = 129;
		btnBrowseSpliceDnr.setLayoutData(gd_btnBrowseSpliceDnr);
		btnBrowseSpliceDnr.setText("browse");
		btnBrowseSpliceDnr.addListener(SWT.Selection, fcl5);
		new Label(shlGenotypeLoader, SWT.NONE);

		final Combo comboSpliceDnr = new Combo(shlGenotypeLoader, SWT.NONE);
		comboSpliceDnr.setEnabled(false);
		comboSpliceDnr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSpliceDnr.add("splice_donor_variant");
		comboSpliceDnr.select(0);

		Button button_2 = new Button(shlGenotypeLoader, SWT.NONE);
		button_2.setEnabled(false);
		GridData gd_button_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button_2.widthHint = 127;
		button_2.setLayoutData(gd_button_2);
		button_2.setText("CV Term");

		Label lblH = new Label(shlGenotypeLoader, SWT.NONE);
		lblH.setText("H5 File");

		TextH5 = new Text(shlGenotypeLoader, SWT.BORDER);
		TextH5.setEnabled(true);
		TextH5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		TextH5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fcl6 = new FileChooserListener(TextH5);

		Button btnH5 = new Button(shlGenotypeLoader, SWT.NONE);
		GridData gd_btnH5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnH5.widthHint = 127;
		btnH5.setLayoutData(gd_btnH5);
		btnH5.addListener(SWT.Selection, fcl6);
		btnH5.setText("browse");

		Label lblSnpGenotypeFile = new Label(shlGenotypeLoader, SWT.NONE);
		lblSnpGenotypeFile.setText("Snp Genotype File");

		textSnpGenotype = new Text(shlGenotypeLoader, SWT.BORDER);
		textSnpGenotype.setEnabled(true);
		textSnpGenotype.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textSnpGenotype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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
		gd_resetButton.widthHint = 93;
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
					List<Cvterm> featureCvTermList = cvTerm_ds.getFeatureType(comboCvTermFeature.getText(),
							comboCvFeature.getText());

					Cvterm nonSynTerm = cvTerm_ds.findCvtermyName(comboNonSyn.getText()).get(0);
					Cvterm spliceAccTerm = cvTerm_ds.findCvtermyName(comboSpliceAcc.getText()).get(0);
					Cvterm spliceDnrTerm = cvTerm_ds.findCvtermyName(comboSpliceDnr.getText()).get(0);

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
				if (comboCvFeature.getText().isEmpty() || comboOrganism.getText().isEmpty()
						|| comboDb.getText().isEmpty() || comboVariantSet.getText().isEmpty()
						|| comboCvTermFeature.getText().isEmpty() || comboCvFeature.getText().isEmpty())
					return ErrorType.EMPTY_ERROR;
				return 0;

			}
		});

	}

	private void initConnectionMap() {
		properties = new HashMap<String, String>();
		properties.put(LoaderConstants.HOSTNAME, HOSTNAME);
		properties.put(LoaderConstants.PORT, PORT);
		properties.put(LoaderConstants.DATABASE, DATABASE);
		properties.put(LoaderConstants.USER, USERNAME);
		properties.put(LoaderConstants.PASSWORD, PASSWORD);

		properties.put(LoaderConstants.CONFIG_NAME, "DEFAULT");
		
		prop = new LoaderProperties();
		prop.setDatabasename(HOSTNAME);
		prop.setPort(PORT);
		prop.setDatabasename(DATABASE);
		prop.setUsername(USERNAME);
		prop.setPassword(PASSWORD);
		
		

	}

	private void initComboItem(Combo combo, Object[] items) {
		String[] comboContent = new String[items.length];
		System.arraycopy(items, 0, comboContent, 0, items.length);

		combo.setItems(comboContent);

	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

}
