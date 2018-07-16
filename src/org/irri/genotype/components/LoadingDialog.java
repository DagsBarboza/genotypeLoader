package org.irri.genotype.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.irri.genotype.LoaderProperties;
import org.irri.genotype.loader.object.LoaderSnpFeature;
import org.irri.genotype.loader.object.LoaderSnpFeatureLoc;
import org.irri.genotype.loader.object.LoaderSnpFeatureProp;
import org.irri.genotype.mapping.SnpFeatureLocMapping;
import org.irri.genotype.mapping.SnpFeaturePropMapping;
import org.irri.genotype.mapping.SnpfeatureMapping;

import chado.loader.model.Cvterm;
import chado.loader.model.Db;
import chado.loader.model.Feature;
import chado.loader.model.GenotypeRun;
import chado.loader.model.Organism;
import chado.loader.model.Platform;
import chado.loader.model.Stock;
import chado.loader.model.Variantset;
import chado.loader.service.FeatureService;
import chado.loader.service.GenotypeRunService;
import chado.loader.service.PlatformService;
import chado.loader.service.SnpFeatureService;
import chado.loader.service.StockService;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;

public class LoadingDialog extends Dialog {

	private Composite container;
	private ProgressBar progressBar;
	private Display display;

	private Cvterm cvterm;
	private Organism organism;
	private Db db;
	private Variantset vset;
	private Text text;
	private FileChooserListener fcl;
	private FileChooserListener fcl2;
	private FileChooserListener fcl3;
	private FileChooserListener fcl4;
	private FileChooserListener fcl5;
	private Cvterm ctTermfeature;
	private Cvterm nonSynTerm;
	private Cvterm spliceAccTerm;
	private Cvterm spliceDnrTerm;
	private Button cancel;
	private SampleFileOperation loader;
	private Button okButton;
	private FileChooserListener fcl6;
	private LoaderProperties prop;

	public LoadingDialog(Display display, Shell parentShell, FileChooserListener fcl, FileChooserListener fcl2,
			FileChooserListener fcl3, FileChooserListener fcl4, FileChooserListener fcl5, FileChooserListener fcl6,
			Cvterm cvterm, Organism organism, Db db, Variantset vSet, Cvterm ctTermfeature, Cvterm nonSynTerm,
			Cvterm spliceAccTerm, Cvterm spliceDnrTerm, LoaderProperties prop) {
		super(parentShell);
		setShellStyle(SWT.APPLICATION_MODAL);
		this.display = display;
		this.fcl = fcl;
		this.fcl2 = fcl2;
		this.fcl3 = fcl3;
		this.fcl4 = fcl4;
		this.fcl5 = fcl5;
		this.fcl6 = fcl6;
		this.cvterm = cvterm;
		this.organism = organism;
		this.db = db;
		this.vset = vSet;
		this.ctTermfeature = ctTermfeature;
		this.nonSynTerm = nonSynTerm;
		this.spliceDnrTerm = spliceDnrTerm;
		this.spliceAccTerm = spliceAccTerm;

		this.prop = prop;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		new Label(container, SWT.NONE);

		text = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setEditable(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = 148;
		text.setLayoutData(gd_text);
		text.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				if (text.isDisposed() && progressBar.isDisposed())
					loader.interrupt();

			}
		});

		progressBar = new ProgressBar(container, SWT.NONE);
		GridData gd_progressBar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_progressBar.widthHint = 442;
		progressBar.setLayoutData(gd_progressBar);
		progressBar.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				if (text.isDisposed() && progressBar.isDisposed())
					loader.interrupt();
			}
		});

		loader = new SampleFileOperation();

		loader.start();

//		System.out.println("RUNNING");

		return container;
	}

	@Override
	protected void okPressed() {
		// form.save();
		super.okPressed();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayoutData(new GridData(SWT.FILL, SWT.RIGHT, true, false));

		// Update layout of the parent composite to count the spacer
		// GridLayout layout = (GridLayout) parent.getLayout();

		okButton = createButton(parent, IDialogConstants.OK_ID, "OK", false);
		okButton.setEnabled(false);

		cancel = createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
		cancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				progressBar.dispose();
				text.dispose();
				loader.interrupt();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});
	}

	class SampleFileOperation extends Thread {

		private int counter;
		private int lines;
		private Connection conn;
		private Object platformm;
		private long startTime;

		public void run() {
			final StockService ds = new StockService();
			final FeatureService featureDs = new FeatureService();
			final SnpFeatureService sf_ds = new SnpFeatureService();

			Integer id = (Integer) sf_ds.getSnpFeatureCurrentSeqNumber();

			conn = null;

			startTime = System.nanoTime();

			try {
				System.out.println("jdbc:postgresql://" + prop.getHostname() + "/" + prop.getDatabasename()+ ","+
						prop.getUsername()+","+ prop.getPassword());
				conn = DriverManager.getConnection(
						"jdbc:postgresql://" + prop.getHostname() + "/" + prop.getDatabasename(), prop.getUsername(),
						prop.getPassword());
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			try {
				File samplefile = fcl.getFile();
				File positionfile = fcl2.getFile();
				File nonSynfile = fcl3.getFile();
				File spliceAcceptorfile = fcl4.getFile();
				File spliceDonorFile = fcl5.getFile();

				BufferedReader sampleFileReader = getBufferedReader(samplefile);
				BufferedReader posFileReader = getBufferedReader(positionfile);
				final BufferedReader nonSynfileReader = getBufferedReader(nonSynfile);
				final BufferedReader spliceAccReader = getBufferedReader(spliceAcceptorfile);
				final BufferedReader spliceDnrReader = getBufferedReader(spliceDonorFile);

				int sampleFileNumber = getlineNumber(samplefile);
				int positionFile = getlineNumber(positionfile);
				lines = sampleFileNumber + getlineNumber(positionfile) + getlineNumber(nonSynfile)
						+ getlineNumber(spliceAcceptorfile) + getlineNumber(spliceDonorFile);

				startProgressBar(lines);

				String line;

				counter = 1;

				if (sampleFileReader != null) {
					writeProgress("Loading Sample File.... \n");
					writeProgress("Loading " + sampleFileNumber + " records .... \n");
					while ((line = sampleFileReader.readLine()) != null) {

						final String textContent = line;

						Stock stock = new Stock();
						stock.setCvterm(cvterm);
						stock.setOrganism(organism);
						stock.setIsObsolete(false);
						stock.setName(textContent);
						stock.setUniquename(textContent);

						if (ds.findByStockyByOrganismTypeName(cvterm, organism, textContent).isEmpty())
							ds.insertRecord(stock);

						stock = null;
						line = null;

						counter++;

					}
				}

				updateProgressBar();

				LoaderSnpFeature loaderSnpFeature;
				LoaderSnpFeatureLoc loaderSnpFeatureLoc;

				List<LoaderSnpFeature> snpFeatureList = new ArrayList<>();
				List<LoaderSnpFeatureLoc> snpfeatureLocList = new ArrayList<>();
				// HashMap<String, HashMap<String, Integer>> snpFeatureMap = new HashMap<>();
				HashMap<String, Integer> snpFeatureMap = new HashMap<>();
				// HashMap<String, Integer> posMap;

				Feature feature = null;

				if (posFileReader != null) {
					// int posCounter = 1;
					// int lines = getlineNumber(positionfile);

					writeProgress("Loading SnpFeature.... \n");
					writeProgress("Loading " + positionFile + " records .... \n");
					// for (int i = 1; i <= lines; i++) {
					int i = id +1;

					String pos = "";
					while ((line = posFileReader.readLine()) != null) {
						String token[] = line.split("\t");

						if (!pos.equals(token[0])) {

							List<Feature> result = featureDs.getFeatureByNameTypeOrganism("Chr" + token[0],
									ctTermfeature, organism);

							if (result.size() == 0) {
								pos = token[0];
								feature = new Feature();
								feature.setCvterm(ctTermfeature);
								feature.setOrganism(organism);
								feature.setName("Chr" + token[0]);
								feature.setUniquename("Chr0" + token[0]);

								featureDs.insertRecord(feature);
							} else
								feature = result.get(0);

						}

						// if (!snpFeatureMap.containsKey(pos)) {
						// posMap = new HashMap<>();
						// posMap.put(token[1].trim(), i);
						//
						// } else {
						// posMap = snpFeatureMap.get(pos);
						// posMap.put(token[1].trim(), i);
						//
						// }
						snpFeatureMap.put(token[1].trim(), i);

						loaderSnpFeature = new LoaderSnpFeature();
						loaderSnpFeature.setSnpFeatureId(i);
						loaderSnpFeature.setVariantSetId(vset.getVariantsetId());

						snpFeatureList.add(loaderSnpFeature);

						loaderSnpFeature = null;

						loaderSnpFeatureLoc = new LoaderSnpFeatureLoc();
						loaderSnpFeatureLoc.setOrganismId(organism.getOrganismId());
						loaderSnpFeatureLoc.setSrcFeatureid(feature.getFeatureId());
						loaderSnpFeatureLoc.setRefCall(token[2]);
						loaderSnpFeatureLoc.setPosition(Integer.parseInt(token[1]));
						loaderSnpFeatureLoc.setSnpfeatureId(i);

						snpfeatureLocList.add(loaderSnpFeatureLoc);

						counter++;
						i++;

					}
					updateProgressBar();

					writeProgress("Inserting Records.... \n");

				}

				try {

					// // Create the BulkInserter:
					PgBulkInsert<LoaderSnpFeature> bulkInsert = new PgBulkInsert<LoaderSnpFeature>(
							new SnpfeatureMapping("public", "snp_feature"));

					PgBulkInsert<LoaderSnpFeatureLoc> bulkInsertSnpFeatureLoc = new PgBulkInsert<LoaderSnpFeatureLoc>(
							new SnpFeatureLocMapping("public", "snp_featureloc"));

					bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(conn), snpFeatureList.stream());

					bulkInsertSnpFeatureLoc.saveAll(PostgreSqlUtils.getPGConnection(conn), snpfeatureLocList.stream());

					// // Now save all entities of a given stream:
					// bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection),
					// persons.stream());

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				writeProgress("Loading Non Synch.... \n");
				if (nonSynfileReader != null) {
					insertSnpFeatureProp(nonSynfileReader, snpFeatureMap, nonSynTerm);
				}

				updateProgressBar();

				writeProgress("Loading Splice Acc.... \n");
				if (spliceAccReader != null) {
					insertSnpFeatureProp(spliceAccReader, snpFeatureMap, spliceAccTerm);
				}

				writeProgress("Loading Splice Donor.... \n");

				if (spliceDnrReader != null) {
					insertSnpFeatureProp(spliceDnrReader, snpFeatureMap, spliceDnrTerm);
					// display.asyncExec(new Runnable() {
					//
					// public void run() {
					// String line;
					// LoaderSnpFeatureProp snpFeatureProp;
					// List<LoaderSnpFeatureProp> listfeatureProp = new ArrayList<>();
					// try {
					// while ((line = spliceDnrReader.readLine()) != null) {
					// String token[] = line.split(",");
					//
					// HashMap<String, Integer> pos = snpFeatureMap.get(token[0]);
					// // System.out.println("SIze "+ token[0]+"->"+ pos.size());
					// snpFeatureProp = new LoaderSnpFeatureProp();
					// snpFeatureProp.setTypeId(spliceDnrTerm.getCvtermId());
					// snpFeatureProp.setSnpFeatureId(pos.get(token[1].trim()));
					// snpFeatureProp.setValue(token[2]);
					//
					// listfeatureProp.add(snpFeatureProp);
					//
					// // System.out.println(snpFeatureProp.getTypeId() +": "+
					// // snpFeatureProp.getSnpFeatureId() +" : "+ snpFeatureProp.getValue());
					//
					// snpFeatureProp = null;
					// counter++;
					//
					// }
					// spliceDnrReader.close();
					//
					// try {
					//
					// PgBulkInsert<LoaderSnpFeatureProp> bulkInsertSnpFeatureProp = new
					// PgBulkInsert<LoaderSnpFeatureProp>(
					// new SnpFeaturePropMapping("public", "snp_featureprop"));
					//
					// bulkInsertSnpFeatureProp.saveAll(PostgreSqlUtils.getPGConnection(conn),
					// listfeatureProp.stream());
					//
					// // // Now save all entities of a given stream:
					// // bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection),
					// // persons.stream());
					//
					// } catch (SQLException e1) {
					// // TODO Auto-generated catch block
					// e1.printStackTrace();
					// }
					//
					// } catch (NumberFormatException e) {
					//
					// e.printStackTrace();
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					//
					// }
					// });
				}

				sampleFileReader.close();
				posFileReader.close();
				// nonSynfileReader.close();
				// spliceAccReader.close();
				// spliceDnrReader.close();

				display.syncExec(new Runnable() {
					public void run() {
//						System.out.println("Starting to 2nd part of loading");
						Platform platform = insertPlatForm();
						if (fcl6.getFile() != null)
							insertGenotypeRun(platform);

						insertSnpGenotype();
					}

					private void insertGenotypeRun(Platform platformm) {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						GenotypeRunService ds = new GenotypeRunService();

						GenotypeRun entity = new GenotypeRun();
						entity.setPlatformId(platformm.getPlatformId());
						entity.setDatePerformed(date);
						entity.setDataLocation(fcl6.getFile().getName().toString());

						ds.insertRecord(entity);

					}

					private Platform insertPlatForm() {
						PlatformService ds = new PlatformService();

						Platform platform = new Platform();
						platform.setDb(db);
						platform.setVariantset(vset);

						ds.insertRecord(platform);

						return platform;

					}

					private void insertSnpGenotype() {

					}
				});

				endProgress();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {

				e1.printStackTrace();
			} catch (SWTException ew) {
				ew.printStackTrace();
			}

			// performComplete();

		}

		/*
		 * private void loadValues() { display.syncExec(new Runnable() { public void
		 * run() { progressBar.setSelection(2);
		 * text.append(genotypePage.getDbCombo().getText() + "\n");
		 * progressBar.setSelection(3);
		 * text.append(genotypePage.getOrganismCombo().getText());
		 * 
		 * } });
		 * 
		 * }
		 */

		// private void performComplete() {
		// display.asyncExec(new Runnable() {
		// public void run() {
		// // progressBar.dispose();
		// setPageComplete(true);
		//
		// }
		// });
		//
		// }

		private void insertSnpFeatureProp(BufferedReader file, HashMap<String, Integer> snpFeatureMap, Cvterm cvTerm) {
			display.asyncExec(new Runnable() {
				public void run() {
					String line;
					LoaderSnpFeatureProp snpFeatureProp;
					List<LoaderSnpFeatureProp> snpFeaturePropList = new ArrayList<>();

					try {
						while ((line = file.readLine()) != null) {
							String token[] = line.split(",");
//							System.out.println("LINE:" + line);
//							System.out.println("Token: " + token[0] + "->" + token[1]);

							// HashMap<String, Integer> pos = snpFeatureMap.get(token[0]);
//							System.out.println("SNP FEATURE MAP: " + snpFeatureMap.size());
//							System.out.println("snpFeature size: " + snpFeatureMap.size());

							snpFeatureProp = new LoaderSnpFeatureProp();
							snpFeatureProp.setTypeId(cvTerm.getCvtermId());
							snpFeatureProp.setSnpFeatureId(snpFeatureMap.get(token[1].trim()));
							snpFeatureProp.setValue(token[2]);

							snpFeaturePropList.add(snpFeatureProp);

							// System.out.println(snpFeatureProp.getTypeId() +": "+
							// snpFeatureProp.getSnpFeatureId() +" : "+ snpFeatureProp.getValue());

							snpFeatureProp = null;
							counter++;

						}
						file.close();

						try {

							PgBulkInsert<LoaderSnpFeatureProp> bulkInsertSnpFeatureProp = new PgBulkInsert<LoaderSnpFeatureProp>(
									new SnpFeaturePropMapping("public", "snp_featureprop"));

							bulkInsertSnpFeatureProp.saveAll(PostgreSqlUtils.getPGConnection(conn),
									snpFeaturePropList.stream());

							// // Now save all entities of a given stream:
							// bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection),
							// persons.stream());

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} catch (NumberFormatException e) {

						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			});

		}

		private void endProgress() {
			display.asyncExec(new Runnable() {
				public void run() {
					long difference = System.nanoTime() - startTime;
					writeProgress("Total execution time: " + String.format("%d min, %d sec",
							TimeUnit.NANOSECONDS.toHours(difference), TimeUnit.NANOSECONDS.toSeconds(difference)
									- TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(difference))));

					okButton.setEnabled(true);
					okButton.setText("Finish");
					cancel.setEnabled(false);

				}
			});

		}

		private void updateProgressBar() {
			display.asyncExec(new Runnable() {
				public void run() {
					if (!progressBar.isDisposed()) {
						progressBar.setSelection(counter);

					}
				}
			});

		}

		private void writeProgress(final String content) {
			display.asyncExec(new Runnable() {
				public void run() {
					if (!text.isDisposed())
						text.append(content);
				}
			});

		}

		private BufferedReader getBufferedReader(File file) {

			if (file != null)
				try {
					return new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			return null;
		}

		private int getlineNumber(File file) {
			int numberOflines = 0;
			if (file != null) {
				try {
					LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
					lineNumberReader.skip(Long.MAX_VALUE);

					numberOflines = lineNumberReader.getLineNumber();

					lineNumberReader.close();

					return numberOflines;

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return numberOflines;
		}

		private void startProgressBar(final int lines) {
			display.syncExec(new Runnable() {
				public void run() {
					progressBar.setMaximum(lines);
					if (progressBar.isDisposed())
						return;
					progressBar.setSelection(1);
				}
			});

		}
	}

}
