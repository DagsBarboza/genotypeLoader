package org.irri.genotype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

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
import org.irri.genotype.components.FileChooserListener;

import chado.loader.model.Cvterm;
import chado.loader.model.Db;
import chado.loader.model.Feature;
import chado.loader.model.Organism;
import chado.loader.model.SnpFeature;
import chado.loader.model.SnpFeatureloc;
import chado.loader.model.SnpFeatureprop;
import chado.loader.model.Stock;
import chado.loader.model.Variantset;
import chado.loader.service.FeatureService;
import chado.loader.service.SnpFeatureLocService;
import chado.loader.service.SnpFeaturePropService;
import chado.loader.service.SnpFeatureService;
import chado.loader.service.StockService;
import chado.loader.service.VariantVariantSetService;

public class LoadingDialogUsingExecutor extends Dialog {

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
	private Label lblStatus;
	private Cvterm ctTermfeature;
	private Cvterm nonSynTerm;
	private Cvterm spliceAccTerm;
	private Cvterm spliceDnrTerm;
	private Button cancel;
	private SampleFileOperation loader;

	protected LoadingDialogUsingExecutor(Display display, Shell parentShell, FileChooserListener fcl, FileChooserListener fcl2,
			FileChooserListener fcl3, FileChooserListener fcl4, FileChooserListener fcl5, Cvterm cvterm,
			Organism organism, Db db, Variantset vSet, Cvterm ctTermfeature, Cvterm nonSynTerm, Cvterm spliceAccTerm,
			Cvterm spliceDnrTerm) {
		super(parentShell);
		setShellStyle(SWT.APPLICATION_MODAL);
		this.display = display;
		this.fcl = fcl;
		this.fcl2 = fcl2;
		this.fcl3 = fcl3;
		this.fcl4 = fcl4;
		this.fcl5 = fcl5;
		this.cvterm = cvterm;
		this.organism = organism;
		this.db = db;
		this.vset = vSet;
		this.ctTermfeature = ctTermfeature;
		this.nonSynTerm = nonSynTerm;
		this.spliceDnrTerm = spliceDnrTerm;

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		new Label(container, SWT.NONE);

		text = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
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

		lblStatus = new Label(container, SWT.NONE);
		GridData gd_lblStatus = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblStatus.widthHint = 261;
		lblStatus.setLayoutData(gd_lblStatus);
		lblStatus.setText("/");

		loader = new SampleFileOperation();

		loader.start();

		System.out.println("RUNNING");

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
		GridLayout layout = (GridLayout) parent.getLayout();

		createButton(parent, IDialogConstants.OK_ID, "OK", false).setEnabled(false);

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

		public void run() {
			final StockService ds = new StockService();
			final SnpFeatureService snf_ds = new SnpFeatureService();
			final FeatureService featureDs = new FeatureService();
			final SnpFeatureLocService snpLocds = new SnpFeatureLocService();
			final VariantVariantSetService vvSetDs = new VariantVariantSetService();
			final SnpFeaturePropService snpFeaturePropDs = new SnpFeaturePropService();

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

				lines = getlineNumber(samplefile) + getlineNumber(positionfile) + getlineNumber(nonSynfile)
						+ getlineNumber(spliceAcceptorfile) + getlineNumber(spliceDonorFile);

				startProgressBar(lines);

				String line;

				counter = 1;

				if (sampleFileReader != null) {
					writeProgress("Loading Sample File.... \n");
					while ((line = sampleFileReader.readLine()) != null) {

						final String textContent = line;

						Stock stock = new Stock();
						stock.setCvterm(cvterm);
						stock.setOrganism(organism);
						stock.setIsObsolete(false);
						stock.setName(textContent);
						stock.setUniquename(textContent);

						ds.insertRecord(stock);

						stock = null;
						line = null;

						updateProgressBar();
						counter++;

					}
				}

				if (posFileReader != null) {
					int posCounter = 1;
					writeProgress("Loading SnpFeature.... \n");
					while ((line = posFileReader.readLine()) != null) {

						SnpFeature snpFeature;
						List<SnpFeature> resultSnpFeature = snf_ds.getSnpFeatureByIdAndVariantSet(posCounter, vset);

						if (resultSnpFeature.isEmpty()) {

							snpFeature = new SnpFeature();
							snpFeature.setSnpFeatureId(posCounter);
							snpFeature.setVariantset(vset);

							snf_ds.insertRecord(snpFeature);
						} else
							snpFeature = resultSnpFeature.get(0);

						String token[] = line.split("\t");
						// System.out.println(token.length);
						//
						// System.out.println(
						// "CHR" + token[0] + ":Position " + token[1] + " :Allele1" + token[2] + "
						// :Allele2" + token[3]);
						List<Feature> listFeature = featureDs.getFeatureByNameTypeOrganism("Chr" + token[0],
								ctTermfeature, organism);

						Feature feature = null;

						if (listFeature.isEmpty()) {
							feature = new Feature();
							feature.setCvterm(ctTermfeature);
							feature.setOrganism(organism);
							feature.setName("Chr" + token[0]);
							feature.setUniquename("Chr" + token[0]);

							featureDs.insertRecord(feature);

						} else
							feature = listFeature.get(0);

						// text.append("\n" + token[0] + " - " + token[1] + " - " + token[2]);
						SnpFeatureloc snpFeatureLoc = new SnpFeatureloc();
						snpFeatureLoc.setOrganism(organism);
						snpFeatureLoc.setFeature(feature);
						snpFeatureLoc.setRefcall(token[2]);
						snpFeatureLoc.setPosition(Integer.parseInt(token[1]));
						snpFeatureLoc.setSnpFeature(snpFeature);

						snpLocds.insertRecord(snpFeatureLoc);
						//
						// VariantVariantset vvSet = new VariantVariantset();
						// vvSet.setVariantFeatureId(snpFeature.getSnpFeatureId());
						// vvSet.setHdf5Index(0); // TODO
						// vvSet.setVariantset(vset);
						//
						// vvSetDs.insertRecord(vvSet);
						//

						// SnpFeatureprop snpFeatureProp = new SnpFeatureprop();
						// snpFeatureProp.setCvterm(cvtermNonSyn); // TODO nonsynonymous_variant
						// snpFeatureProp.setSnpFeature(snpFeature);
						// snpFeatureProp.setValue(""); // TODO VALUE
						// //
						// snpFeaturePropDs.insertRecord(snpFeatureProp);
						//
						// SnpFeatureprop snpFeatureProp = new SnpFeatureprop();
						// snpFeatureProp.setCvterm(cvtermNonSyn); // TODO nonsynonymous_variant
						// snpFeatureProp.setSnpFeature(snpFeature);
						// snpFeatureProp.setValue(""); // TODO VALUE
						// //
						// snpFeaturePropDs.insertRecord(snpFeatureProp);

						// vvSet = null;
						
						line = null;
						

						updateProgressBar();
						snpFeatureLoc = null;
						snpFeature = null;
						posCounter++;
						counter++;

					}
				}

				writeProgress("Loading Non Synch.... \n");
				if (nonSynfileReader != null) {
					display.asyncExec(new Runnable() {
						public void run() {
							String line;

							try {
								while ((line = nonSynfileReader.readLine()) != null) {
									updateProgressBar();
									String token[] = line.split(",");

									List<SnpFeatureloc> result = snpLocds.findBySnpFeatureByPosAndFeatureName(
											Integer.parseInt(token[1]), "Chr" + token[0]);

									SnpFeatureloc snpFeatureLoc = result.get(0);

									SnpFeatureprop snpFeatureProp = new SnpFeatureprop();
									snpFeatureProp.setCvterm(nonSynTerm);
									snpFeatureProp.setSnpFeature(snpFeatureLoc.getSnpFeature());
									snpFeatureProp.setValue(token[2]);

									snpFeaturePropDs.insertRecord(snpFeatureProp);

									counter++;
								}
							} catch (NumberFormatException e) {

								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					});
				}

				writeProgress("Loading Splice Acc.... \n");
				if (spliceAccReader != null) {
					display.asyncExec(new Runnable() {
						public void run() {
							String line;

							try {
								while ((line = spliceAccReader.readLine()) != null) {
									updateProgressBar();
									String token[] = line.split(",");

									List<SnpFeatureloc> result = snpLocds.findBySnpFeatureByPosAndFeatureName(
											Integer.parseInt(token[1]), "Chr" + token[0]);

									SnpFeatureloc snpFeatureLoc = result.get(0);

									SnpFeatureprop snpFeatureProp = new SnpFeatureprop();
									snpFeatureProp.setCvterm(spliceAccTerm);
									snpFeatureProp.setSnpFeature(snpFeatureLoc.getSnpFeature());
									snpFeatureProp.setValue(token[2]);

									snpFeaturePropDs.insertRecord(snpFeatureProp);

									counter++;
								}
							} catch (NumberFormatException e) {

								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					});
				}

				writeProgress("Loading Splice Donor.... \n");

				if (spliceDnrReader != null) {
					display.asyncExec(new Runnable() {

						public void run() {
							String line;

							try {
								while ((line = spliceDnrReader.readLine()) != null) {
									updateProgressBar();
									String token[] = line.split(",");

									List<SnpFeatureloc> result = snpLocds.findBySnpFeatureByPosAndFeatureName(
											Integer.parseInt(token[1]), "Chr" + token[0]);

									SnpFeatureloc snpFeatureLoc = result.get(0);

									SnpFeatureprop snpFeatureProp = new SnpFeatureprop();
									snpFeatureProp.setCvterm(spliceDnrTerm);
									snpFeatureProp.setSnpFeature(snpFeatureLoc.getSnpFeature());
									snpFeatureProp.setValue(token[2]);

									snpFeaturePropDs.insertRecord(snpFeatureProp);

									counter++;
								}
							} catch (NumberFormatException e) {

								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}

				sampleFileReader.close();
				posFileReader.close();
				nonSynfileReader.close();
				spliceAccReader.close();
				spliceDnrReader.close();

			} catch (

			FileNotFoundException e1) {
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

		private void updateProgressBar() {
			display.asyncExec(new Runnable() {
				public void run() {
					if (!progressBar.isDisposed() && !lblStatus.isDisposed()) {
						progressBar.setSelection(counter);
						lblStatus.setText(counter + " / " + lines);
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
