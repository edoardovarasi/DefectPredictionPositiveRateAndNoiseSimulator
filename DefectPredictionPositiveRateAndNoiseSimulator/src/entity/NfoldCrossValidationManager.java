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

import java.math.BigDecimal;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Implements cross validation algorithm, with or without noise (fp, fn, fp&fn)
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class NfoldCrossValidationManager {

	
	/**
	 * n fold cross validation without noise
	 * 
	 * @param classifier
	 * @param dataset
	 * @param folds
	 * @return
	 */
	public Stats crossValidate (Classifier classifier, Instances dataset, int folds){

		// randomizes order of instances
		Instances randDataset = new Instances(dataset);
		randDataset.randomize(RandomizationManager.randomGenerator);
		
		// cross-validation
		Evaluation eval = null;
		try {
			eval = new Evaluation(randDataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int n = 0; n < folds; n++) {
			Instances test = randDataset.testCV(folds, n);
			Instances train = randDataset.trainCV(folds, n, RandomizationManager.randomGenerator);

			// build and evaluate classifier
			Classifier clsCopy;
			try {
				clsCopy = Classifier.makeCopy(classifier);
				clsCopy.buildClassifier(train);
				eval.evaluateModel(clsCopy, test);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// output evaluation for the nfold cross validation
		Double precision = eval.precision(Settings.classificationChoice);
		Double recall = eval.recall(Settings.classificationChoice);
		Double fmeasure = eval.fMeasure(Settings.classificationChoice);
		Double classificationTP = eval.numTruePositives(Settings.classificationChoice);
		Double classificationTN = eval.numTrueNegatives(Settings.classificationChoice);
		Double classificationFP = eval.numFalsePositives(Settings.classificationChoice);
		Double classificationFN = eval.numFalseNegatives(Settings.classificationChoice);
		Double kappa = eval.kappa();

		return new Stats(classificationTP, classificationTN, classificationFP, classificationFN, kappa,  precision, recall, fmeasure);
	}
	
	/**
	 * n fold cross validation with noise (independent fp and fn)
	 * 
	 * @param classifier
	 * @param dataset
	 * @param folds
	 * @return
	 */
	public Stats crossValidateWithNoise (Classifier classifier, Instances dataset, int folds, BigDecimal fpPercentage, BigDecimal fnPercentage){

		// noise manager
		NoiseInjectionManager noiseInjectionManager = new NoiseInjectionManager();
		
		// randomizes order of instances
		Instances randDataset = new Instances(dataset);
		randDataset.randomize(RandomizationManager.randomGenerator);
		
		// cross-validation
		Evaluation eval = null;
		try {
			eval = new Evaluation(randDataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int n = 0; n < folds; n++) {
			Instances test = randDataset.testCV(folds, n);
			Instances train = randDataset.trainCV(folds, n, RandomizationManager.randomGenerator);
			
			// copies instances of train set to not modify the original
			Instances noisyTrain = new Instances (train);
			// injects level of noise in the copied train set
			noiseInjectionManager.addNoiseToDataset(noisyTrain, fpPercentage, fnPercentage);

			// build and evaluate classifier
			Classifier clsCopy;
			try {
				clsCopy = Classifier.makeCopy(classifier);
				// trains the model using a noisy train set
				clsCopy.buildClassifier(noisyTrain);
				eval.evaluateModel(clsCopy, test);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// output evaluation for the nfold cross validation
		Double precision = eval.precision(Settings.classificationChoice);
		Double recall = eval.recall(Settings.classificationChoice);
		Double fmeasure = eval.fMeasure(Settings.classificationChoice);
		Double classificationTP = eval.numTruePositives(Settings.classificationChoice);
		Double classificationTN = eval.numTrueNegatives(Settings.classificationChoice);
		Double classificationFP = eval.numFalsePositives(Settings.classificationChoice);
		Double classificationFN = eval.numFalseNegatives(Settings.classificationChoice);
		Double kappa = eval.kappa();

		return new Stats(classificationTP, classificationTN, classificationFP, classificationFN, kappa,  precision, recall, fmeasure);
	}
	
	/**
	 * n fold cross validation with noise (combined fp and fn)
	 * 
	 * @param classifier
	 * @param dataset
	 * @param folds
	 * @return
	 */
	
	
	public Stats crossValidateWithNoise (Classifier classifier, Instances dataset, int folds, BigDecimal combinedFpFnPercentage){

		// noise manager
		NoiseInjectionManager noiseInjectionManager = new NoiseInjectionManager();
		
		// randomizes order of instances
		Instances randDataset = new Instances(dataset);
		randDataset.randomize(RandomizationManager.randomGenerator);
		
		// cross-validation
		Evaluation eval = null;
		try {
			eval = new Evaluation(randDataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int n = 0; n < folds; n++) {
			Instances test = randDataset.testCV(folds, n);
			Instances train = randDataset.trainCV(folds, n, RandomizationManager.randomGenerator);
			
			// copies instances of train set to not modify the original
			Instances noisyTrain = new Instances (train);
			// injects level of noise in the copied train set
			noiseInjectionManager.addNoiseToDataset(noisyTrain, combinedFpFnPercentage);

			// build and evaluate classifier
			Classifier clsCopy;
			try {
				clsCopy = Classifier.makeCopy(classifier);
				// trains the model using a noisy train set
				clsCopy.buildClassifier(noisyTrain);
				eval.evaluateModel(clsCopy, test);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// output evaluation for the nfold cross validation
		Double precision = eval.precision(Settings.classificationChoice);
		Double recall = eval.recall(Settings.classificationChoice);
		Double fmeasure = eval.fMeasure(Settings.classificationChoice);
		Double classificationTP = eval.numTruePositives(Settings.classificationChoice);
		Double classificationTN = eval.numTrueNegatives(Settings.classificationChoice);
		Double classificationFP = eval.numFalsePositives(Settings.classificationChoice);
		Double classificationFN = eval.numFalseNegatives(Settings.classificationChoice);
		Double kappa = eval.kappa();

		return new Stats(classificationTP, classificationTN, classificationFP, classificationFN, kappa,  precision, recall, fmeasure);
	}
}


