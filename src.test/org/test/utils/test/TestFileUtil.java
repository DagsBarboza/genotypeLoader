package org.test.utils.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class TestFileUtil {

	@Test
	public void testPositionFile() {
		File file = new File("D:\\MyDocuments\\work\\sorghum_rawfiles\\Sorghum.positions");

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;

			Integer count = 1;
			while ((line = reader.readLine()) != null) {
				String token[] = line.split("\t");
				System.out.println(token.length);

				System.out.println(
						"CHR" + token[0] + ":Position " + token[1] + " :Allele1" + token[2] + " :Allele2" + token[3]);
				count++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
