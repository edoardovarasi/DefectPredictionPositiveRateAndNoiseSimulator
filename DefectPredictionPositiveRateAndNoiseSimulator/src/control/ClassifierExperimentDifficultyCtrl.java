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

import java.math.BigDecimal;

import weka.classifiers.Classifier;
import weka.core.Instances;
import entity.DifficultyResamplingManager;
import entity.ExperimentData;
import entity.ExperimentType;
import entity.NfoldCrossValidationManager;
import entity.OutputFileManager;
import entity.RandomizationManager;
import entity.Settings;
import entity.Stats;
import entity.StatsManager;

/**
 * launches experiments for PR variations
 * for a certain dataset and classifier
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class ClassifierExperimentDifficultyCtrl {

	/**
	 * class constructor. 
	 * lanches and orchestrates the experiments
	 * 
	 * @param experimentData
	 */
	public ClassifierExperimentDifficultyCtrl(ExperimentData experimentData) {
		
		boolean verbose = false;
		
		// same size among PR levels
		boolean sameDimensionAmongProportions = Settings.sameSubDatasetSizeAmongProportions;

		experimentData.setExperimentType(ExperimentType.DIFFICULTY);

		// dataset
		Instances dataset = experimentData.getDataset();

		// classifier
		Classifier classifier = experimentData.getClassifier();

		// number of folds for cross validation
		int folds = Settings.nFold;

		// resets random generator and dataset
		RandomizationManager.inizializeRandomGenerator();
		experimentData.resetDataset();

		// PR variation manager
		DifficultyResamplingManager difficultyResamplingManager = new DifficultyResamplingManager();

		// determines subdataset size for resamplings
		int subdatasetDimensions=0;
		
		// if necessary, determines common size
		if(sameDimensionAmongProportions){
			subdatasetDimensions = difficultyResamplingManager.calculateSubdatasetDimensionAmongProportions(dataset, Settings.maxPositiveExamplePercentProportion, Settings.minPositiveExamplePercentProportion);
			if(verbose)System.out.println("[ClassifierExperimentDifficultyCtrl] calc same size among PR levels: " + subdatasetDimensions);
		}

		// initial PR
		BigDecimal currPositiveExamplePercentProportion = Settings.minPositiveExamplePercentProportion;
		
		// is size is variable, set first decision
		if(!sameDimensionAmongProportions){
			subdatasetDimensions = (difficultyResamplingManager.calculateSubdatasetDimensionsForProportion(dataset, currPositiveExamplePercentProportion)).getTotalNumberOfElements();
			if(verbose)System.out.println("[ClassifierExperimentDifficultyCtrl] calc size for PR level: " + currPositiveExamplePercentProportion +" -> " + subdatasetDimensions);
	
		}

		NfoldCrossValidationManager nfoldCrossValidationManager = new NfoldCrossValidationManager();
		StatsManager statsManager = new StatsManager();
		Stats stats;
		OutputFileManager outputFileManager = new OutputFileManager(experimentData);

		// controls if PR must be incremented or not
		Boolean isPpToIncrease = true;

		// next PR value
		BigDecimal nextPositiveExamplePercentProportion;

		// increments PR and check if within limits
		// if yes, does experiment iterations. If not, exits.
		while(isPpToIncrease){

			System.out.println("   - PR: " + currPositiveExamplePercentProportion);
			statsManager = new StatsManager();

			System.out.println("     Resamplings");
			for(int i=0; i<Settings.numberOfResamplingsPerLevel; i++){
				// generating resampling for dataset with a given PR
				Instances resampledDataset = difficultyResamplingManager.generateResampledSubdataset(dataset, currPositiveExamplePercentProportion, subdatasetDimensions);
				// cross-validation
				stats = nfoldCrossValidationManager.crossValidate(classifier, resampledDataset, folds);
				statsManager.registerIterationStats(stats);
				System.out.println("       resampling results"+ i + ": " + stats.toString());
			}
			System.out.println("     AVERAGES level    :   " + statsManager.getAvgStats().toString());
			System.out.println("     VARIANCES level :   " + statsManager.getVarStats().toString());
			
			// registering level stats to file
			outputFileManager.printStatsToFile(currPositiveExamplePercentProportion, statsManager);

			nextPositiveExamplePercentProportion = currPositiveExamplePercentProportion.add(Settings.positiveExamplePercentProportionIncreaseStep);

			isPpToIncrease = (nextPositiveExamplePercentProportion).compareTo(Settings.maxPositiveExamplePercentProportion)<=0 && (Settings.positiveExamplePercentProportionIncreaseStep.doubleValue()!=0);

			if(isPpToIncrease){
				currPositiveExamplePercentProportion = nextPositiveExamplePercentProportion;
				// if size among PR is variable, sets the new dimension
				if(!sameDimensionAmongProportions){
					subdatasetDimensions = (difficultyResamplingManager.calculateSubdatasetDimensionsForProportion(dataset, currPositiveExamplePercentProportion)).getTotalNumberOfElements();
					if(verbose)System.out.println("[ClassifierExperimentDifficultyCtrl] calcolo dimensione per proporzione: " + currPositiveExamplePercentProportion +" -> " + subdatasetDimensions);

				}
			}

			
			experimentData.resetDataset();

		}

	}
}
