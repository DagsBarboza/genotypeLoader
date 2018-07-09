package org.test.utils.test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import chado.loader.AppContext;
import chado.loader.model.Cvterm;
import chado.loader.model.Feature;
import chado.loader.model.Organism;
import chado.loader.service.CvTermService;
import chado.loader.service.FeatureService;
import chado.loader.service.OrganismService;

public class FeatureTest {

	@Test
	public void testfeature() {

		AppContext.createEntityManager();
		
		FeatureService ds = new FeatureService();

		OrganismService ods = new OrganismService();

		CvTermService cds = new CvTermService();

		
		
		Cvterm cvTerm = cds.findCvtermyId(3).get(0);

		Organism organism = ods.findOrganismId(2).get(0);

		List<Feature> result = ds.getFeatureByNameTypeOrganism("Chr1", cvTerm, organism);
		System.out.println("Size: " + result.size());

	}

	public void addfeature() {

		AppContext.createEntityManager();

		FeatureService ds = new FeatureService();

		OrganismService ods = new OrganismService();

		CvTermService cds = new CvTermService();

		Cvterm cvTerm = cds.findCvtermyId(3).get(0);

		Organism organism = ods.findOrganismId(2).get(0);

		Date date = new Date();

		Feature f = new Feature();
		f.setCvterm(cvTerm);
		f.setOrganism(organism);
		f.setUniquename("Chr1");
		f.setIsAnalysis(false);
		f.setIsObsolete(false);
		f.setTimeaccessioned(new Timestamp(date.getTime()));
		f.setTimelastmodified(new Timestamp(date.getTime()));

		ds.insertRecord(f);

	}

}
