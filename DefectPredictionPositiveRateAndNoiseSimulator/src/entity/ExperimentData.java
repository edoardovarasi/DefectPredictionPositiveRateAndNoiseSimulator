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

import weka.classifiers.Classifier;
import weka.core.Instances;



/**
 * This class contains data relative to an experiment 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class ExperimentData {
	
	// original dataset
	private Instances originalDataset;
	
	// current dataset 
	private Instances dataset;
	
	// current dataset name (name of file arff)
	//NOTA: impostato nel main (per ora)
	private String datasetName;
	
	// type of current experiment
	private ExperimentType experimentType;
	
	// type of noise 
	private NoiseType noiseType;
	
	// current classifier
	private Classifier classifier;
	
	
	/**
	 * Constructor. 
	 * Initialize new dataset. Ensures that datasets are new objects and not references
	 * @param originalDataset
	 */
	public ExperimentData (Instances originalDataset){
		this.originalDataset  = new Instances(originalDataset);
		this.setDataset(new Instances(this.originalDataset));
		}
	
	/**
	 * resets dataset to initial file conditions
	 */
	public void resetDataset(){
		// does this, otherwise it's just a reference whereas we want an actual copy
		this.setDataset(new Instances(this.originalDataset));
	}

	/**
	 * return a reference to the dataset
	 * @return
	 */
	public Instances getDataset() {
		return dataset;
	}

	/**
	 * copies a given dataset into the current
	 * 
	 */
	public void setDataset(Instances dataset) {
		this.dataset = new Instances(dataset);
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public ExperimentType getExperimentType() {
		return experimentType;
	}

	public void setExperimentType(ExperimentType experimentType) {
		this.experimentType = experimentType;
	}

	public NoiseType getNoiseType() {
		return noiseType;
	}

	public void setNoiseType(NoiseType noiseType) {
		this.noiseType = noiseType;
	}

}
