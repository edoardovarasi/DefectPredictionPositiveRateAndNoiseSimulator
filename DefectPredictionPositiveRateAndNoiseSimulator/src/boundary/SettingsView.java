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

package boundary;

import java.math.BigDecimal;
import java.util.Vector;

import weka.classifiers.Classifier;
import entity.InvalidInputException;
import entity.NoiseType;
import entity.Settings;


/**
 * 
 * Wrapper class for data and settings from the main window
 * Default settings can be set by modifying entity.Settings
 *  
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class SettingsView {

	// contructs the instance with default settings
	public SettingsView() {
		new Settings();
	}

	// input e output addresses
	private String inputPath = Settings.INPUT_PATH;
	private String outputPath = Settings.OUTPUT_PATH;

	// experiment types
	private boolean DifficultyExperiments = Settings.DifficultyExperiments; 
	private boolean NoiseExperiments = Settings.NoiseExperiments; 
	private boolean DifficultyAndNoiseExperiments = Settings.DifficultyAndNoiseExperiments; 

	// NOISE
	// noise type
	private NoiseType noiseType = Settings.noiseType;	
	// number of iterations per noise level
	private int numberOfReps = Settings.numberOfReps;
	// percentage of noise in train set
	private BigDecimal initialFalsePositivesPercentage = Settings.initialFalsePositivesPercentage;
	private BigDecimal initialFalseNegativesPercentage = Settings.initialFalseNegativesPercentage;
	private BigDecimal initialFalseNegativesAndFalsePositivesPercentage = Settings.initialFalseNegativesAndFalsePositivesPercentage;
	// noise increment step
	private BigDecimal fpIncreaseStep = Settings.fpIncreaseStep;
	private BigDecimal fnIncreaseStep = Settings.fnIncreaseStep;
	private BigDecimal fpAndFnIncreaseStep = Settings.fpAndFnIncreaseStep;
	// maximum noise level in the simulation
	private BigDecimal maxFalsePositivesPercentage = Settings.maxFalsePositivesPercentage;
	private BigDecimal maxFalseNegativesPercentage = Settings.maxFalseNegativesPercentage;
	private BigDecimal maxFalseNegativesAndFalsePositivesPercentage = Settings.maxFalseNegativesAndFalsePositivesPercentage;

	// resampling for PR
	// number of resamplings per PR level
	private int numberOfResamplingsPerLevel = Settings.numberOfResamplingsPerLevel;
	// if true, chooses a size for resamplet datasets whiich is constant among PR levels
	private boolean sameSubDatasetSizeAmongProportions = Settings.sameSubDatasetSizeAmongProportions;
	// minimum PR
	private BigDecimal minPositiveExamplePercentProportion = Settings.minPositiveExamplePercentProportion;
	// maximum PR
	private BigDecimal maxPositiveExamplePercentProportion = Settings.maxPositiveExamplePercentProportion;
	// PR increment step
	private BigDecimal positiveExamplePercentProportionIncreaseStep = Settings.positiveExamplePercentProportionIncreaseStep;

	// dataset characteristics 
	// class labels
	private String buggyLabel = Settings.buggyLabel;
	private String nonbuggyLabel = Settings.nonbuggyLabel;
	// set the class to retrieve (check order in dataset file)
	private int classificationChoice = Settings.classificationChoice;

	// classifiers
	// available classifiers
	private Vector<Classifier> potentialClassifiers = Settings.potentialClassifiers;
	// selected classifiers
	private Vector<Classifier> selectedClassifiers = Settings.selectedClassifiers;

	// n-fold cross-validation settings (value of n)
	private int nFold = Settings.nFold;

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public boolean isDifficultyExperiments() {
		return DifficultyExperiments;
	}

	public void setDifficultyExperiments(boolean difficultyExperiments) {
		DifficultyExperiments = difficultyExperiments;
	}

	public boolean isNoiseExperiments() {
		return NoiseExperiments;
	}

	public void setNoiseExperiments(boolean noiseExperiments) {
		NoiseExperiments = noiseExperiments;
	}

	public boolean isDifficultyAndNoiseExperiments() {
		return DifficultyAndNoiseExperiments;
	}

	public void setDifficultyAndNoiseExperiments(
			boolean difficultyAndNoiseExperiments) {
		DifficultyAndNoiseExperiments = difficultyAndNoiseExperiments;
	}

	public int getNumberOfReps() {
		return numberOfReps;
	}

	public void setNumberOfReps (int numberOfReps) throws InvalidInputException  {
		if (numberOfReps>0){
			this.numberOfReps = numberOfReps;
		}
		else{
			throw new InvalidInputException("The number of repetitions must be an integer greater or equal than 1.");
		}
	}

	public BigDecimal getInitialFalsePositivesPercentage() {
		return initialFalsePositivesPercentage;
	}

	public void setInitialFalsePositivesPercentage(
			BigDecimal initialFalsePositivesPercentage) throws InvalidInputException {

		if (initialFalsePositivesPercentage.doubleValue()>=0.0 && initialFalsePositivesPercentage.doubleValue()<=100.0){
			this.initialFalsePositivesPercentage = initialFalsePositivesPercentage;
		}
		else{
			throw new InvalidInputException("Initial percentage of false positives must be between 0.0 and 100.0.");
		}
	}

	public BigDecimal getInitialFalseNegativesPercentage() {
		return initialFalseNegativesPercentage;
	}

	public void setInitialFalseNegativesPercentage(
			BigDecimal initialFalseNegativesPercentage) throws InvalidInputException {

		if (initialFalseNegativesPercentage.doubleValue()>=0.0 && initialFalseNegativesPercentage.doubleValue()<=100.0){
			this.initialFalseNegativesPercentage = initialFalseNegativesPercentage;
		}
		else{
			throw new InvalidInputException("Initial percentage of false negatives must be between 0.0 and 100.0.");
		}
	}

	public void setInitialFalseNegativesAndFalsePositivesPercentage (
			BigDecimal initialFalseNegativesAndFalsePositivesPercentage) throws InvalidInputException{

		if (initialFalseNegativesAndFalsePositivesPercentage.doubleValue()>=0.0 && initialFalseNegativesAndFalsePositivesPercentage.doubleValue()<=100.0){
			this.initialFalseNegativesAndFalsePositivesPercentage = initialFalseNegativesAndFalsePositivesPercentage;
		}
		else{
			throw new InvalidInputException("Initial percentage of combined false negatives and false positives must be between 0.0 and 100.0.");
		}
	}

	public BigDecimal getInitialFalseNegativesAndFalsePositivesPercentage() {
		return initialFalseNegativesAndFalsePositivesPercentage;
	}

	public BigDecimal getFpIncreaseStep() {
		return fpIncreaseStep;
	}

	public void setFpIncreaseStep(BigDecimal fpIncreaseStep) throws InvalidInputException {

		if (fpIncreaseStep.doubleValue()>=0.0 && fpIncreaseStep.doubleValue()<=100.0){
			this.fpIncreaseStep = fpIncreaseStep;
		}
		else{
			throw new InvalidInputException("The steps for percentage of false positives must be between 0.0 and 100.0.");
		}
	}

	public BigDecimal getFnIncreaseStep() {
		return fnIncreaseStep;
	}

	public void setFnIncreaseStep(BigDecimal fnIncreaseStep) throws InvalidInputException {
		if (fnIncreaseStep.doubleValue()>=0.0 && fnIncreaseStep.doubleValue()<=100.0){
			this.fnIncreaseStep = fnIncreaseStep;
		}
		else{
			throw new InvalidInputException("The steps for percentage of false negatives must be between 0.0 and 100.0.");
		}	
	}

	public void setFpAndFnIncreaseStep(BigDecimal fpAndFnIncreaseStep) throws InvalidInputException {
		if (fpAndFnIncreaseStep.doubleValue()>=0.0 && fpAndFnIncreaseStep.doubleValue()<=100.0){
			this.fpAndFnIncreaseStep = fpAndFnIncreaseStep;
		}
		else{
			throw new InvalidInputException("The steps for combined percentage of false negatives and false positives must be between 0.0 and 100.0.");
		}	


	}

	public BigDecimal getFpAndFnIncreaseStep() {
		return fpAndFnIncreaseStep;
	}

	public BigDecimal getMaxFalsePositivesPercentage() {
		return maxFalsePositivesPercentage;
	}

	public void setMaxFalsePositivesPercentage(
			BigDecimal maxFalsePositivesPercentage) throws InvalidInputException {
		if (maxFalsePositivesPercentage.doubleValue()>=0.0 && maxFalsePositivesPercentage.doubleValue()<=100.0){
			this.maxFalsePositivesPercentage = maxFalsePositivesPercentage;
		}
		else{
			throw new InvalidInputException("Maximum percentage of false positives must be between 0.0 and 100.0.");
		}
	}

	public BigDecimal getMaxFalseNegativesPercentage() {
		return maxFalseNegativesPercentage;
	}

	public void setMaxFalseNegativesPercentage(
			BigDecimal maxFalseNegativesPercentage) throws InvalidInputException {
		if (maxFalseNegativesPercentage.doubleValue()>=0.0 && maxFalseNegativesPercentage.doubleValue()<=100.0){
			this.maxFalseNegativesPercentage = maxFalseNegativesPercentage;
		}
		else{
			throw new InvalidInputException("Maximum percentage of false negatives must be between 0.0 and 100.0.");
		}
	}

	public int getNumberOfResamplingsPerLevel() {
		return numberOfResamplingsPerLevel;
	}

	public void setNumberOfResamplingsPerLevel(int numberOfResamplingsPerLevel) throws InvalidInputException {	
		if (numberOfResamplingsPerLevel>0){
			this.numberOfResamplingsPerLevel = numberOfResamplingsPerLevel;
		}
		else{
			throw new InvalidInputException("The number of resamplings must be an integer greater or equal than 1.");
		}

	}

	public BigDecimal getMinPositiveExamplePercentProportion() {
		return minPositiveExamplePercentProportion;
	}

	public void setMinPositiveExamplePercentProportion(
			BigDecimal minPositiveExamplePercentProportion) throws InvalidInputException {
		if (minPositiveExamplePercentProportion.doubleValue()>=0.0 && minPositiveExamplePercentProportion.doubleValue()<=100.0){
			this.minPositiveExamplePercentProportion = minPositiveExamplePercentProportion;
		}
		else{
			throw new InvalidInputException("Minumum positive examples percentage must be between 0.0 and 100.0.");
		}		
	}

	public BigDecimal getMaxPositiveExamplePercentProportion() {
		return maxPositiveExamplePercentProportion;
	}

	public void setMaxPositiveExamplePercentProportion(
			BigDecimal maxPositiveExamplePercentProportion) throws InvalidInputException {
		if (maxPositiveExamplePercentProportion.doubleValue()>=0.0 && maxPositiveExamplePercentProportion.doubleValue()<=100.0){
			this.maxPositiveExamplePercentProportion = maxPositiveExamplePercentProportion;
		}
		else{
			throw new InvalidInputException("Maximum positive examples percentage must be between 0.0 and 100.0.");
		}				
	}

	public void setMaxFalseNegativesAndFalsePositivesPercentage(
			BigDecimal maxFalseNegativesAndFalsePositivesPercentage) throws InvalidInputException {

		if (maxFalseNegativesAndFalsePositivesPercentage.doubleValue()>=0.0 && maxFalseNegativesAndFalsePositivesPercentage.doubleValue()<=100.0){
			this.maxFalseNegativesAndFalsePositivesPercentage = maxFalseNegativesAndFalsePositivesPercentage;
		}
		else{
			throw new InvalidInputException("Maximum percentage of combined false negatives and false positives must be between 0.0 and 100.0.");
		}		
	}

	public BigDecimal getMaxFalseNegativesAndFalsePositivesPercentage() {
		return maxFalseNegativesAndFalsePositivesPercentage;
	}

	public BigDecimal getPositiveExamplePercentProportionIncreaseStep() {
		return positiveExamplePercentProportionIncreaseStep;
	}

	public void setPositiveExamplePercentProportionIncreaseStep(
			BigDecimal positiveExamplePercentProportionIncreaseStep) throws InvalidInputException {
		if (positiveExamplePercentProportionIncreaseStep.doubleValue()>=0.0 && positiveExamplePercentProportionIncreaseStep.doubleValue()<=100.0){
			this.positiveExamplePercentProportionIncreaseStep = positiveExamplePercentProportionIncreaseStep;
		}
		else{
			throw new InvalidInputException("Positive examples increase percentage step must be between 0.0 and 100.0.");
		}				
	}

	public int getnFold() {
		return nFold;
	}

	public void setnFold(int nFold) throws InvalidInputException {
		this.nFold = nFold;

		if (nFold>1){
			this.nFold = nFold;
		}
		else{
			throw new InvalidInputException("The number n for n-fold cross-validation must be an integer >1");
		}

	}

	public String getBuggyLabel() {
		return buggyLabel;
	}

	public void setBuggyLabel  (String buggyLabel) throws InvalidInputException {
		this.buggyLabel = buggyLabel;
	}

	public String getNonbuggyLabel() {
		return nonbuggyLabel;
	}

	public void setNonbuggyLabel(String nonbuggyLabel) throws InvalidInputException {
		this.nonbuggyLabel = nonbuggyLabel;
	}

	public int getClassificationChoice() {
		return classificationChoice;
	}

	public void setClassificationChoice (int classificationChoice) throws InvalidInputException{
		if(classificationChoice==0 || classificationChoice ==1){
			this.classificationChoice = classificationChoice;
		}
		else throw new InvalidInputException("The variable for the classification must be 0 (first value) or 1 (second value).");
	}

	/**
	 * Checks consistency of input settings before inserting them in the entity object
	 * 
	 * @throws InvalidInputException 
	 */
	public void checkConsistencyOfSettings() throws InvalidInputException{

		// checks that at least one experiment type is selected
		if(!this.NoiseExperiments && !this.DifficultyExperiments && !this.DifficultyAndNoiseExperiments){
			throw new InvalidInputException("At least one of the experiment types must be selected.");
		}

		// checks that minimum fp percent is not bigger than maximum and step not bigger than maximum
		if(!areMinMaxStepConsistent(this.initialFalsePositivesPercentage, this.maxFalsePositivesPercentage, this.fpIncreaseStep)){
			throw new InvalidInputException("The values regarding false positive injection are not consistent."
					+ "\nBe sure that the minimim value is not grater than the maximum, and that the step is not greater than the maximum.");
		}

		// checks that minimum fn percent is not bigger than maximum and step not bigger than maximum
		if(!areMinMaxStepConsistent(this.initialFalseNegativesPercentage, this.maxFalseNegativesPercentage, this.fnIncreaseStep)){
			throw new InvalidInputException("The values regarding false negatives injection are not consistent."
					+ "\nBe sure that the minimim value is not grater than the maximum, and that the step is not greater than the maximum.");
		}

		// checks that minimum fp&fn percent is not bigger than maximum and step not bigger than maximum
		if(!areMinMaxStepConsistent(this.initialFalseNegativesAndFalsePositivesPercentage, this.maxFalseNegativesAndFalsePositivesPercentage, this.fpAndFnIncreaseStep)){
			throw new InvalidInputException("The values regarding combined false positives and negatives injection are not consistent."
					+ "\nBe sure that the minimim value is not grater than the maximum, and that the step is not greater than the maximum.");
		}

		// checks that minimum pr percent is not bigger than maximum and step not bigger than maximum
		if(!areMinMaxStepConsistent(this.minPositiveExamplePercentProportion, this.maxPositiveExamplePercentProportion, this.positiveExamplePercentProportionIncreaseStep)){
			throw new InvalidInputException("The values regarding positive examples proportion for resamplings are not consistent."
					+ "\nBe sure that the minimim value is not grater than the maximum, and that the step is not greater than the maximum.");
		}
	}

	public Vector<Classifier> getPotentialClassifiers() {
		return potentialClassifiers;
	}

	public void setPotentialClassifiers(Vector<Classifier> potentialClassifiers) {
		this.potentialClassifiers = potentialClassifiers;
	}

	public Vector<Classifier> getSelectedClassifiers() {
		return selectedClassifiers;
	}

	public void setSelectedClassifiers(Vector<Classifier> selectedClassifiers) {
		this.selectedClassifiers = selectedClassifiers;
	}

	private boolean areMinMaxStepConsistent(BigDecimal min, BigDecimal max, BigDecimal step){

		if(min.compareTo(max)>0 || step.compareTo(max)>0)return false;
		else return true;

	}

	public NoiseType getNoiseType() {
		return noiseType;
	}

	public void setNoiseType(NoiseType noiseType) {
		this.noiseType = noiseType;
	}

	public boolean isSameSubDatasetSizeAmongProportions() {
		return sameSubDatasetSizeAmongProportions;
	}

	public void setSameSubDatasetSizeAmongProportions(
			boolean sameSubDatasetSizeAmongProportions) {
		this.sameSubDatasetSizeAmongProportions = sameSubDatasetSizeAmongProportions;
	}

}
