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
import entity.NoiseType;
import entity.OutputFileManager;
import entity.RandomizationManager;
import entity.Settings;
import entity.Stats;
import entity.StatsManager;

/**
 * launches experiments for combination of PR and Noise
 * for a certain dataset and classifier
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class ClassifierExperimentDiffAndNoiseCtrl {


	/**
	 * class constructor. 
	 * lanches and orchestrates the experiments
	 * 
	 * @param experimentData
	 */
	public ClassifierExperimentDiffAndNoiseCtrl(ExperimentData experimentData) {

		boolean verbose = false;

		experimentData.setExperimentType(ExperimentType.DIFFICULTYandNOISE);
		experimentData.setNoiseType(Settings.noiseType);
		Boolean independentFpAndFn = (Settings.noiseType.equals(NoiseType.INDEPENDENT_FP_FN));

		// same size among PR levels
		boolean sameDimensionAmongProportions = Settings.sameSubDatasetSizeAmongProportions;

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
			if(verbose)System.out.println("[ClassifierExperimentDiffAndNoiseCtrl] calc same size among PR levels: " + subdatasetDimensions);
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

			// if fp and fn levels are independent
			if(independentFpAndFn){

				BigDecimal fpPercentage = new BigDecimal(Settings.initialFalsePositivesPercentage.doubleValue());
				BigDecimal fnPercentage = new BigDecimal(Settings.initialFalseNegativesPercentage.doubleValue());

				// control variables for FP e FN
				Boolean isFpToIncrease = true;
				Boolean isFnToIncrease = true;

				// next fp and fn values
				BigDecimal fpPercentageNextValue;
				BigDecimal fnPercentageNextValue;

				// increments percentages and check if within limits
				// if yes, does experiment iterations. If not, exits.
				while(isFpToIncrease || isFnToIncrease){

					System.out.println("   - NOISE LEVEL: %fp= " + fpPercentage + " %fn= " + fnPercentage);
					statsManager = new StatsManager();

					System.out.println("     Resampling and noise injection");
					for(int i=0; i<Settings.numberOfResamplingsPerLevel; i++){

						// generating resampling for dataset with a given PR
						Instances resampledDataset = difficultyResamplingManager.generateResampledSubdataset(dataset, currPositiveExamplePercentProportion, subdatasetDimensions);

						// cross validation with noise injection in train dataset 
						stats = nfoldCrossValidationManager.crossValidateWithNoise(classifier, resampledDataset, folds, fpPercentage, fnPercentage);
						statsManager.registerIterationStats(stats);
						System.out.println("       risults for resampling and noise injection "+ i + ": " + stats.toString());
					}

					System.out.println("     AVERAGES level %pr-noise    :   " + statsManager.getAvgStats().toString());
					System.out.println("     VARIANCES level %pr-noise :   " + statsManager.getVarStats().toString());

					// registering level stats to file
					outputFileManager.printStatsToFile(currPositiveExamplePercentProportion, fpPercentage, fnPercentage, statsManager);

					fpPercentageNextValue = fpPercentage.add(Settings.fpIncreaseStep);
					fnPercentageNextValue = fnPercentage.add(Settings.fnIncreaseStep);

					isFpToIncrease = ((fpPercentageNextValue).compareTo(Settings.maxFalsePositivesPercentage)<=0)&&(Settings.fpIncreaseStep.doubleValue()!=0);
					isFnToIncrease = ((fnPercentageNextValue).compareTo(Settings.maxFalseNegativesPercentage)<=0)&&(Settings.fnIncreaseStep.doubleValue()!=0);

					if(isFpToIncrease){
						fpPercentage = fpPercentageNextValue;
					}

					if(isFnToIncrease){
						fnPercentage = fnPercentageNextValue;
					}
					experimentData.resetDataset();


				}
			}
			
			// if fp and fn are combined
			else{
				BigDecimal fpAndFnPercentage = new BigDecimal(Settings.initialFalseNegativesAndFalsePositivesPercentage.doubleValue());

				// control variable for FP e FN
				Boolean isFpAndFnToIncrease = true;

				// next percentage value
				BigDecimal fpAndFnPercentageNextValue;

				// increments percentages and check if within limits
				// if yes, does experiment iterations. If not, exits.
				while(isFpAndFnToIncrease){

					System.out.println("   - NOISE LEVEL: %fp&fn= " + fpAndFnPercentage);
					statsManager = new StatsManager();

					System.out.println("     Resampling and noise injection");
					for(int i=0; i<Settings.numberOfResamplingsPerLevel; i++){

						// generating resampling for dataset with a given PR
						Instances resampledDataset = difficultyResamplingManager.generateResampledSubdataset(dataset, currPositiveExamplePercentProportion, subdatasetDimensions);

						// cross validation with noise injection in train dataset 
						stats = nfoldCrossValidationManager.crossValidateWithNoise(classifier, resampledDataset, folds, fpAndFnPercentage);
						statsManager.registerIterationStats(stats);
						System.out.println("       results resampling and noise injection "+ i + ": " + stats.toString());
					}

					System.out.println("     AVERAGES level %pr-noise    :   " + statsManager.getAvgStats().toString());
					System.out.println("     VARIANCES level %pr-noise :   " + statsManager.getVarStats().toString());

					// registering level stats to file
					outputFileManager.printStatsToFile(currPositiveExamplePercentProportion, fpAndFnPercentage, statsManager);

					fpAndFnPercentageNextValue = fpAndFnPercentage.add(Settings.fpAndFnIncreaseStep);

					isFpAndFnToIncrease = ((fpAndFnPercentageNextValue).compareTo(Settings.maxFalseNegativesAndFalsePositivesPercentage)<=0)&&(Settings.fpAndFnIncreaseStep.doubleValue()!=0);

					if(isFpAndFnToIncrease){
						fpAndFnPercentage = fpAndFnPercentageNextValue;
					}
					experimentData.resetDataset();


				}
			}


			nextPositiveExamplePercentProportion = currPositiveExamplePercentProportion.add(Settings.positiveExamplePercentProportionIncreaseStep);

			isPpToIncrease = (nextPositiveExamplePercentProportion).compareTo(Settings.maxPositiveExamplePercentProportion)<=0 && (Settings.positiveExamplePercentProportionIncreaseStep.doubleValue()!=0);

			if(isPpToIncrease){
				currPositiveExamplePercentProportion = nextPositiveExamplePercentProportion;
				
				// if size among PR is variable, sets the new dimension
				if(!sameDimensionAmongProportions){
					subdatasetDimensions = (difficultyResamplingManager.calculateSubdatasetDimensionsForProportion(dataset, currPositiveExamplePercentProportion)).getTotalNumberOfElements();
					if(verbose)System.out.println("[ClassifierExperimentDifficultyCtrl] calc dimension for proportion: " + currPositiveExamplePercentProportion +" -> " + subdatasetDimensions);

				}
			}
			experimentData.resetDataset();

		}
	}

}

