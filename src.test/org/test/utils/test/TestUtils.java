package org.test.utils.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

import org.junit.Test;

public class TestUtils {

	
	public void testDate() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		System.out.println(date);
	}
	
	@Test
	public void testSortList() {

		TreeSet<String> ts = new TreeSet<String>();
		//Culm diameter - 1st internode, Flag leaf length, Flag leaf width, Grain weight, Time to flowering (from sowing), anthocyanin below apiculus, apiculus color, apiculus shape, auricle color, awn color, awn distribution, awn presence, basal leaf sheath anthocyanin, basal leaf sheath color, collar color, culm angle, culm internode anthocyanin, culm internode underlying colour, culm length, culm node anthocyanin, culm node underlying colour, culm number, culm strength, endosperm type, flag leaf angle, flowering date, grain width, leaf angle, leaf blade anthocyanin, leaf blade greenness, leaf blade pubescence, leaf length, leaf senescence, leaf width, lemma and palea color, lemma and palea pubescence, ligule color, ligule length, ligule shape, panicle axis attitude, panicle exsertion, panicle length, panicle number per plant, panicle shattering, panicle threshability, panicle type, pericarp colour, scent, secondary panicle branching, seeding date, seedling height, spikelet fertility, sterile lemma color, sterile lemma length
		ts.add("Culm diameter - 1st internode");
		ts.add("Flag leaf length");
		ts.add("Grain weight");
		ts.add("Time to flowering (from sowing)");
		ts.add("anthocyanin below apiculus");
		System.out.println(ts);
		for (String s: ts)
		    System.out.println(s);
	}

}
