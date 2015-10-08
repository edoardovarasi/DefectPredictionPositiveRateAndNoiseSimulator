/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package entity;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


/**
 * This class manages the creation of weka Instances from a dataset file (arff or csv)
 * NOTE: the last attribute is (must be) the classification one (buggy/nonbuggy)
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class DatasetFileManager {

	private boolean verbose = false;

	public Instances generateWekaInstancesForDataset(String datasetFilePath){

		// datasource creation from file
		DataSource datasource = null;
		try {
			datasource = new DataSource(datasetFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Instances instances = null;

		// instances creation from datasource
		try {
			instances = datasource.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// last attribute is for classification
		instances.setClassIndex(instances.numAttributes() - 1);

		if(verbose) System.out.println("\nDataset:\n");
		if(verbose) System.out.println(instances);
		
		return instances;
	}

}
