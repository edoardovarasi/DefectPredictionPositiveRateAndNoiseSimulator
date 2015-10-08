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
 * launches experiments for noise variations
 * for a certain dataset and classifier
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class ClassifierExperimentNoiseCtrl {

	public ClassifierExperimentNoiseCtrl(ExperimentData experimentData) {

		experimentData.setExperimentType(ExperimentType.NOISE);
		experimentData.setNoiseType(Settings.noiseType);
		Boolean independentFpAndFn = (Settings.noiseType.equals(NoiseType.INDEPENDENT_FP_FN));

		// dataset
		Instances dataset = experimentData.getDataset();

		// classifier
		Classifier classifier = experimentData.getClassifier();

		// number of folds for cross validation
		int folds = Settings.nFold;

		// resets random generator and dataset
		RandomizationManager.inizializeRandomGenerator();
		experimentData.resetDataset();

		// independent values for fp and fn
		if(independentFpAndFn){

			BigDecimal fpPercentage = new BigDecimal(Settings.initialFalsePositivesPercentage.doubleValue());
			BigDecimal fnPercentage = new BigDecimal(Settings.initialFalseNegativesPercentage.doubleValue());

			NfoldCrossValidationManager nfoldCrossValidationManager = new NfoldCrossValidationManager();
			StatsManager statsManager = new StatsManager();
			Stats stats;
			OutputFileManager outputFileManager = new OutputFileManager(experimentData);

			// control variable for FP e FN
			Boolean isFpToIncrease = true;
			Boolean isFnToIncrease = true;

			// next percentages value
			BigDecimal fpPercentageNextValue;
			BigDecimal fnPercentageNextValue;

			// increments percentages and check if within limits
			// if yes, does experiment iterations. If not, exits.
			while(isFpToIncrease || isFnToIncrease){

				System.out.println("   - NOISE LEVEL: %fp= " + fpPercentage + " %fn= " + fnPercentage);
				statsManager = new StatsManager();

				System.out.println("     Repetitions");
				for(int i=0; i<Settings.numberOfReps; i++){
					stats = nfoldCrossValidationManager.crossValidateWithNoise(classifier, dataset, folds, fpPercentage, fnPercentage);
					statsManager.registerIterationStats(stats);
					System.out.println("       repetition results "+ i + ": " + stats.toString());
				}
				System.out.println("     AVERAGES level    :   " + statsManager.getAvgStats().toString());
				System.out.println("     VARIANCES level :   " + statsManager.getVarStats().toString());
				outputFileManager.printStatsToFile(fpPercentage, fnPercentage, statsManager);


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
			}
		}

		// combined values for fp and fn
		if(!independentFpAndFn){
			BigDecimal fpAndFnPercentage = new BigDecimal(Settings.initialFalseNegativesAndFalsePositivesPercentage.doubleValue());

			NfoldCrossValidationManager nfoldCrossValidationManager = new NfoldCrossValidationManager();
			StatsManager statsManager = new StatsManager();
			Stats stats;
			OutputFileManager outputFileManager = new OutputFileManager(experimentData);

			// control variable for FP e FN
			Boolean isFpAndFnToIncrease = true;

			// next percentages value
			BigDecimal fpAndFnPercentageNextValue;

			// increments percentages and check if within limits
			// if yes, does experiment iterations. If not, exits.
			while(isFpAndFnToIncrease){

				System.out.println("   - NOISE LEVEL: %fp&fn= " + fpAndFnPercentage);
				statsManager = new StatsManager();

				System.out.println("     Repetitions");
				for(int i=0; i<Settings.numberOfReps; i++){
					//System.out.print(".");
					stats = nfoldCrossValidationManager.crossValidateWithNoise(classifier, dataset, folds, fpAndFnPercentage);
					statsManager.registerIterationStats(stats);
					System.out.println("       repetition results "+ i + ": " + stats.toString());
				}
				System.out.println("     AVERAGES level    :   " + statsManager.getAvgStats().toString());
				System.out.println("     VARIANCES level :   " + statsManager.getVarStats().toString());
				outputFileManager.printStatsToFile(fpAndFnPercentage, statsManager);


				fpAndFnPercentageNextValue = fpAndFnPercentage.add(Settings.fpAndFnIncreaseStep);

				isFpAndFnToIncrease = ((fpAndFnPercentageNextValue).compareTo(Settings.maxFalseNegativesAndFalsePositivesPercentage)<=0)&&(Settings.fpAndFnIncreaseStep.doubleValue()!=0);

				if(isFpAndFnToIncrease){
					fpAndFnPercentage = fpAndFnPercentageNextValue;
				}
			}
		}

		experimentData.resetDataset();

	}

}
