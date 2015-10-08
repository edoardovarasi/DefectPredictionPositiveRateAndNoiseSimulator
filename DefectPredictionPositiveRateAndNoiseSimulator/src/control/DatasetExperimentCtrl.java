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

package control;

import java.util.Vector;

import weka.classifiers.Classifier;
import entity.ExperimentData;
import entity.RandomizationManager;
import entity.Settings;

/**
 * 
 * Iterates over the selected classifiers and launches the necessary
 * experiment controllers
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class DatasetExperimentCtrl {
	
	private ExperimentData experimentData;
	
	public DatasetExperimentCtrl(ExperimentData experimentData , Vector<Classifier> classifiers) {
		
		this.experimentData = experimentData;
		// iterates over the list of selected classifiers and launches the selected experiments for each
		for (Classifier cls: classifiers){
			launchExperimentsWithClassifier(cls);
		}
	}
	
    private void launchExperimentsWithClassifier(Classifier classifier){
    	RandomizationManager.inizializeRandomGenerator();
    	// resets dataset to initial conditions
    	this.experimentData.resetDataset();
    	// sets the classifier in experimentData
    	this.experimentData.setClassifier(classifier);
    	
    	System.out.println("**** tests with classifier " + classifier.getClass().getSimpleName() + " ****");
    	
    	// creates controllers for the selected experiment types 
    	if(Settings.DifficultyExperiments) new ClassifierExperimentDifficultyCtrl(this.experimentData);
    	if(Settings.NoiseExperiments) new ClassifierExperimentNoiseCtrl(this.experimentData);
    	if(Settings.DifficultyAndNoiseExperiments) new ClassifierExperimentDiffAndNoiseCtrl(this.experimentData);

    	
    }


}
