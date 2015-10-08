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

import java.io.File;

import entity.DatasetFileManager;
import entity.ExperimentData;
import entity.RandomizationManager;
import entity.Settings;
import boundary.SettingsView;

/**
 * 
 * Prepares the settings from SettingView coming from user interface
 * Launches experiments for each dataset in the input folder
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *  
 * @param args
 */
public class MainExperimentCtrl {

	SettingsView settingsView;

	public MainExperimentCtrl(SettingsView settingsView) {

		this.settingsView = settingsView;
		this.updateSettingsFromView();
		this.launchExperimentsForEachDataset();

	}


	/**
	 * Updates entity.settings with data coming from boundary.
	 * 
	 */
	private void updateSettingsFromView(){

		Settings.INPUT_PATH = this.settingsView.getInputPath();
		Settings.OUTPUT_PATH = this.settingsView.getOutputPath();
		
		Settings.DifficultyExperiments = settingsView.isDifficultyExperiments();
		Settings.NoiseExperiments = settingsView.isNoiseExperiments();
		Settings.DifficultyAndNoiseExperiments = settingsView.isDifficultyAndNoiseExperiments();
		
		Settings.numberOfReps = settingsView.getNumberOfReps();
		Settings.initialFalsePositivesPercentage = settingsView.getInitialFalsePositivesPercentage();
		Settings.initialFalseNegativesPercentage = settingsView.getInitialFalseNegativesPercentage();
		Settings.maxFalsePositivesPercentage = settingsView.getMaxFalsePositivesPercentage();
		Settings.maxFalseNegativesPercentage = settingsView.getMaxFalseNegativesPercentage();
		Settings.fnIncreaseStep = settingsView.getFnIncreaseStep();
		Settings.fpIncreaseStep = settingsView.getFpIncreaseStep();
		
		Settings.numberOfResamplingsPerLevel = settingsView.getNumberOfResamplingsPerLevel();
		Settings.minPositiveExamplePercentProportion = settingsView.getMinPositiveExamplePercentProportion();
		Settings.maxPositiveExamplePercentProportion = settingsView.getMaxPositiveExamplePercentProportion();
		Settings.positiveExamplePercentProportionIncreaseStep = settingsView.getPositiveExamplePercentProportionIncreaseStep();
		Settings.sameSubDatasetSizeAmongProportions = settingsView.isSameSubDatasetSizeAmongProportions();
		
		Settings.selectedClassifiers = settingsView.getSelectedClassifiers();
		
		Settings.classificationChoice = settingsView.getClassificationChoice();
		
		Settings.buggyLabel = settingsView.getBuggyLabel();
		Settings.nonbuggyLabel = settingsView.getNonbuggyLabel();
		
		Settings.nFold = settingsView.getnFold();
		
		Settings.noiseType = settingsView.getNoiseType();
		Settings.initialFalseNegativesAndFalsePositivesPercentage = settingsView.getInitialFalseNegativesAndFalsePositivesPercentage();
		Settings.fpAndFnIncreaseStep = settingsView.getFpAndFnIncreaseStep();
		Settings.maxFalseNegativesAndFalsePositivesPercentage = settingsView.getMaxFalseNegativesAndFalsePositivesPercentage();

	}

	/**
	 * Scans the input folder for weka-compatible dataset files and
	 * launches the experiments for each of them.
	 * 
	 */
	private void launchExperimentsForEachDataset(){
		File folder = new File(Settings.INPUT_PATH);
		File[] listOfFiles = folder.listFiles();
		String datasetFilePath;

		// Creates a DatasetFileManager for loading the weka-format dataset
		DatasetFileManager datasetFileManager = new DatasetFileManager();

		// launches the simulations for each dataset in the folder
		for (File file : listOfFiles) {
			if (file.isFile()) {

				System.out.println("*** Executing simulations for dataset " + file.getName()+" ***");
				datasetFilePath = file.getAbsolutePath();

				RandomizationManager.inizializeRandomGenerator();

				// initialization of experiment data with the loaded dataset
				ExperimentData experimentData = new ExperimentData(datasetFileManager.generateWekaInstancesForDataset(datasetFilePath));
				experimentData.setDatasetName(file.getName());

				// launches experiment controller for the dataset
				new DatasetExperimentCtrl(experimentData, Settings.selectedClassifiers);
				
				System.out.println("*** All simulations completed ***");

			}
		}
	}

}
