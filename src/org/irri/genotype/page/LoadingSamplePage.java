package org.irri.genotype.page;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.irri.genotype.components.WizardLabels;

import chado.loader.model.Cvterm;
import chado.loader.model.Dbxref;
import chado.loader.model.Feature;
import chado.loader.model.Organism;
import chado.loader.model.SnpFeature;
import chado.loader.model.SnpFeatureloc;
import chado.loader.model.SnpFeatureprop;
import chado.loader.model.Stock;
import chado.loader.model.VariantVariantset;
import chado.loader.model.Variantset;
import chado.loader.service.CvTermService;
import chado.loader.service.SnpFeatureLocService;
import chado.loader.service.SnpFeaturePropService;
import chado.loader.service.SnpFeatureService;
import chado.loader.service.StockService;
import chado.loader.service.VariantVariantSetService;

public class LoadingSamplePage extends WizardPage {

	private SelectGenotypePage genotypePage;

	private Text text;

	private ProgressBar progressBar;

	private Display display;

	private HashMap<String, Dbxref> mapping;

	private Cvterm cvTerm;

	private Organism organism;

	private Variantset vSet;

	public LoadingSamplePage(String pageName) {
		super(pageName);

		URL imgUrl = getClass().getResource(WizardLabels.IMG);
		ImageDescriptor imgDescriptor = ImageDescriptor.createFromURL(imgUrl);

		setTitle(WizardLabels.LOADING_PAGE_TITLE);
		setDescription(WizardLabels.LOADING_PAGE_DESC);
		setImageDescriptor(imgDescriptor);

	}

	public LoadingSamplePage(String pageName, SelectGenotypePage page1, Display display) {
		super(pageName);

		URL imgUrl = getClass().getResource(WizardLabels.IMG);
		ImageDescriptor imgDescriptor = ImageDescriptor.createFromURL(imgUrl);

		setTitle(WizardLabels.LOADING_PAGE_TITLE);
		setDescription(WizardLabels.LOADING_PAGE_DESC);
		setImageDescriptor(imgDescriptor);

		this.genotypePage = page1;
		this.display = display;

	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, false));

		progressBar = new ProgressBar(composite, SWT.SMOOTH);
		progressBar.setMaximum(3);

		GridData gd_progressBar = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_progressBar.widthHint = 447;
		progressBar.setLayoutData(gd_progressBar);

		text = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setEditable(false);

		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 439;
		gd_text.heightHint = 234;
		text.setLayoutData(gd_text);

		setControl(composite);

		setPageComplete(false);

	}

	public File getSampleFile() {
		return genotypePage.getSampleFile();
	}

	public File getPositionFile() {
		return genotypePage.getPositionFile();
	}

	public void readFile() {

		Integer cvTermMapId = genotypePage.getCvTermCombo().getSelectionIndex();
		cvTerm = genotypePage.getCvTermMap().get(cvTermMapId);

		Integer OrganismMapId = genotypePage.getOrganismCombo().getSelectionIndex();
		organism = genotypePage.getOrganismMap().get(OrganismMapId);

		Integer variantMapId = genotypePage.getVSetCombo().getSelectionIndex();
		vSet = genotypePage.getVariantSetMap().get(variantMapId);

		// new SampleFileOperation().start();

		new PositionFileOperation().start();

	}

	class SampleFileOperation extends Thread {

		public void run() {
			final StockService ds = new StockService();

			startProgressBar();
			try {
				File file = genotypePage.getSampleFile();

				BufferedReader reader;

				reader = new BufferedReader(new FileReader(file));
				String line;

				while ((line = reader.readLine()) != null) {

					final String textContent = line;
					display.syncExec(new Runnable() {
						public void run() {
							progressBar.setSelection(1);

							Stock stock = new Stock();
							stock.setCvterm(cvTerm);
							stock.setOrganism(organism);
							stock.setIsObsolete(false);
							stock.setName(textContent);
							stock.setUniquename(textContent);

							ds.insertRecord(stock);

							stock = null;

						}
					});
				}

				reader.close();

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SWTException ew) {
				ew.printStackTrace();
			}

			loadValues();
			performComplete();

		}

		private void loadValues() {
			display.syncExec(new Runnable() {
				public void run() {
					progressBar.setSelection(2);
					text.append(genotypePage.getDbCombo().getText() + "\n");
					progressBar.setSelection(3);
					text.append(genotypePage.getOrganismCombo().getText());

				}
			});

		}

		private void performComplete() {
			display.asyncExec(new Runnable() {
				public void run() {
					// progressBar.dispose();
					setPageComplete(true);

				}
			});

		}

		private void startProgressBar() {
			display.syncExec(new Runnable() {
				public void run() {
					if (progressBar.isDisposed())
						return;
					progressBar.setSelection(1);
				}
			});

		}
	}

	class PositionFileOperation extends Thread {

		public void run() {
			final SnpFeatureService ds = new SnpFeatureService();
			final SnpFeatureLocService snpLocds = new SnpFeatureLocService();
			final VariantVariantSetService vvSetDs = new VariantVariantSetService();
			final SnpFeaturePropService snpFeaturePropDs = new SnpFeaturePropService();
			
			startProgressBar();
			try {
				File file = genotypePage.getPositionFile();

				BufferedReader reader;

				reader = new BufferedReader(new FileReader(file));
				String line;

				final Integer count = 1;
				while ((line = reader.readLine()) != null) {

					final String textContent = line;
					display.syncExec(new Runnable() {
						public void run() {
							progressBar.setSelection(1);

							SnpFeature snpFeature = new SnpFeature();
							snpFeature.setSnpFeatureId(count);
							snpFeature.setVariantset(vSet);

							ds.insertRecord(snpFeature);

							String token[] = textContent.split("\t");
							// System.out.println(token.length);
							//
							// System.out.println(
							// "CHR" + token[0] + ":Position " + token[1] + " :Allele1" + token[2] + "
							// :Allele2" + token[3]);
							Feature feature = new Feature();
							
							
							
							SnpFeatureloc snpFeatureLoc = new SnpFeatureloc();
							snpFeatureLoc.setOrganism(organism);
							snpFeatureLoc.setFeature(feature);
							snpFeatureLoc.setRefcall(token[2]);
							snpFeatureLoc.setPosition(Integer.parseInt(token[1]));
							snpFeatureLoc.setSnpFeature(snpFeature);

							
							
							snpLocds.insertRecord(snpFeatureLoc);
							
							
							VariantVariantset vvSet = new VariantVariantset();
							vvSet.setVariantFeatureId(snpFeature.getSnpFeatureId());
							vvSet.setHdf5Index(0); // TODO
							vvSet.setVariantset(vSet);
							
							
							vvSetDs.insertRecord(vvSet);
							
							
							
							SnpFeatureprop snpFeatureProp = new SnpFeatureprop();
							snpFeatureProp.setCvterm(cvTerm); //TODO nonsynonymous_variant
							snpFeatureProp.setSnpFeature(snpFeature);
							snpFeatureProp.setValue(""); // TODO VALUE
							
							
							snpFeaturePropDs.insertRecord(snpFeatureProp);
							
							
							
							vvSet = null;
							snpFeatureLoc = null;
							snpFeature = null;

						}
					});
				}

				reader.close();

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SWTException ew) {
				ew.printStackTrace();
			}

			loadValues();
			performComplete();

		}

		private void loadValues() {
			display.syncExec(new Runnable() {
				public void run() {
					progressBar.setSelection(2);
					text.append(genotypePage.getDbCombo().getText() + "\n");
					progressBar.setSelection(3);
					text.append(genotypePage.getOrganismCombo().getText());

				}
			});

		}

		private void performComplete() {
			display.asyncExec(new Runnable() {
				public void run() {
					// progressBar.dispose();
					setPageComplete(true);

				}
			});

		}

		private void startProgressBar() {
			display.syncExec(new Runnable() {
				public void run() {
					if (progressBar.isDisposed())
						return;
					progressBar.setSelection(1);
				}
			});

		}
	}

}
