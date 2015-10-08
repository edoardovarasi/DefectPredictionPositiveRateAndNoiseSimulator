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
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.functions.SMO;
//import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.SPegasos;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.functions.VotedPerceptron;
import weka.classifiers.lazy.IB1;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.Bagging;
import weka.classifiers.mi.CitationKNN;
import weka.classifiers.mi.MIBoost;
import weka.classifiers.mi.MISMO;
import weka.classifiers.mi.MIWrapper;
import weka.classifiers.mi.SimpleMI;
import weka.classifiers.misc.HyperPipes;
import weka.classifiers.misc.VFI;
import weka.classifiers.rules.ConjunctiveRule;
import weka.classifiers.rules.DTNB;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.NNge;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.PART;
import weka.classifiers.rules.Ridor;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.BFTree;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.FT;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.J48graft;
import weka.classifiers.trees.LADTree;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.NBTree;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.classifiers.trees.SimpleCart;

/**
 * This class contains the settings of the tool.
 * Values specified here, are the default values visualized in the GUI 
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class Settings {

	// *** TYPES OF EXPERIMENTS TO CARRY OUT *** //
	// difficulty (positive rate)
	public static boolean DifficultyExperiments = true; 
	// noise
	public static boolean NoiseExperiments = false; 
	// interazione between PR and noise
	public static boolean DifficultyAndNoiseExperiments = false; 
   

	// *** CLASSIFIER TO USE ***
	// list of potential classifier, selectable in the GUI 
	public static Vector<Classifier> potentialClassifiers = new Vector<Classifier>();
	// list of classifier that will be used in the simulations
	public static Vector<Classifier> selectedClassifiers = new Vector<Classifier>();
	
	Classifier cls1 = new NaiveBayes();
	Classifier cls2 = new BayesNet();
	//Classifier cls3 = new NaiveBayesSimple();
	Classifier cls4 = new NaiveBayesUpdateable();
	Classifier cls5= new LibLINEAR();
	Classifier cls6 = new LibSVM();
	Classifier cls7 = new Logistic();
//	Classifier cls47 = new Logistic();
//	Classifier cls48 = new Logistic();
//	Classifier cls49 = new Logistic();
//	Classifier cls50 = new Logistic();
//	Classifier cls51 = new Logistic();
//	Classifier cls52 = new Logistic();
	Classifier cls8 = new MultilayerPerceptron();
	Classifier cls9 = new RBFNetwork();
	Classifier cls10 = new SimpleLogistic();
	Classifier cls11 = new SMO();
	Classifier cls12 = new SPegasos();
	Classifier cls13 = new VotedPerceptron();
	Classifier cls14 = new IB1();
	Classifier cls15 = new IBk(10); //?
	Classifier cls16 = new LWL();
	Classifier cls17 = new CitationKNN();
	Classifier cls18 = new MIBoost();
	Classifier cls19 = new MISMO();
	Classifier cls20 = new MIWrapper();
	Classifier cls21 = new SimpleMI();
	Classifier cls22 = new HyperPipes();
	Classifier cls23 = new VFI();
	Classifier cls24 = new ConjunctiveRule();
	Classifier cls25 = new DecisionTable();
	Classifier cls26 = new DTNB();
	Classifier cls27 = new JRip();
	Classifier cls28 = new NNge();
	Classifier cls29 = new OneR();
	Classifier cls30 = new PART();
	Classifier cls31 = new Ridor();
	Classifier cls32 = new ZeroR();
	Classifier cls33 = new ADTree();
	Classifier cls34 = new BFTree();
	Classifier cls35 = new DecisionStump();
	Classifier cls36 = new FT();
	Classifier cls37 = new J48();
	Classifier cls38 = new J48graft();
	Classifier cls39 = new LADTree();
	Classifier cls40 = new LMT();
	Classifier cls41 = new NBTree();
	Classifier cls42 = new RandomForest();
	Classifier cls43 = new RandomTree();
	Classifier cls44 = new REPTree();
	Classifier cls45 = new SimpleCart();
	Classifier cls46 = new Bagging();
	
	{
		potentialClassifiers.add(cls1);
		potentialClassifiers.add(cls2);
		potentialClassifiers.add(cls4);
		potentialClassifiers.add(cls5);
		potentialClassifiers.add(cls6);
//		potentialClassifiers.add(cls7);
		potentialClassifiers.add(setLogisticOptions(cls7, 10));
//		potentialClassifiers.add(setLogisticOptions(cls52, 100));
		potentialClassifiers.add(cls8);
		potentialClassifiers.add(cls9);
		potentialClassifiers.add(cls10);
		potentialClassifiers.add(cls11);
		potentialClassifiers.add(cls12);
		potentialClassifiers.add(cls13);
		potentialClassifiers.add(cls14);
		potentialClassifiers.add(cls15);
		potentialClassifiers.add(cls16);
		potentialClassifiers.add(cls17);
		potentialClassifiers.add(cls18);
		potentialClassifiers.add(cls19);
		potentialClassifiers.add(cls20);
		potentialClassifiers.add(cls21);
		potentialClassifiers.add(cls22);
		potentialClassifiers.add(cls23);
		potentialClassifiers.add(cls24);
		potentialClassifiers.add(cls25);
		potentialClassifiers.add(cls26);
		potentialClassifiers.add(cls27);
		potentialClassifiers.add(cls28);
		potentialClassifiers.add(cls29);
		potentialClassifiers.add(cls30);
		potentialClassifiers.add(cls31);
		potentialClassifiers.add(cls32);
		potentialClassifiers.add(cls33);
		potentialClassifiers.add(cls34);
		potentialClassifiers.add(cls35);
		potentialClassifiers.add(cls36);
		potentialClassifiers.add(cls37);
		potentialClassifiers.add(cls38);
		potentialClassifiers.add(cls39);
		potentialClassifiers.add(cls40);
		potentialClassifiers.add(cls41);
		potentialClassifiers.add(cls42);
		potentialClassifiers.add(cls43);
		potentialClassifiers.add(cls44);
		potentialClassifiers.add(cls45);
		potentialClassifiers.add(cls46);
		
		selectedClassifiers.add(cls1);
	}


	// *** FILE LOCATIONS ***
	public static String OUTPUT_PATH = "./output/";
	public static String INPUT_PATH = "./input/";



	// *** PSEUDORANDOM GENERATOR SEED ***
	public static final long generalRandomGeneratorSeed = 8374213;
	
	// *** N-FOLD CROSS-VALIDATION ***
	public static int nFold = 10;
	
	// *** CONFIDENCE INSTERVAL CALCULATION ***
	// quantile normal standard FOR 1-alfa=0.95 to compute confidence interval
	// for the average. Size of sample should be >30.
	public static final double quantile = 1.96;



	// *** DATASET ***
	// attribute values for classification category  
	public static String buggyLabel =  "TRUE"; //"bug";
	public static String nonbuggyLabel =  "FALSE"; //"non bug";
	// category to retrieve (e.g. if classification is: 0=false, 1=true, select 0 if you want to retrieve buggy instances)
	public static int classificationChoice = 0;


	// *** NOISE ***
	// type of noise (FP e FN independent or combined)
	public static NoiseType noiseType = NoiseType.COMBINED_FP_FN;	
	// number of iterations per noise level
	public static int numberOfReps = 10;
	// percentage of noise in train set
	public static BigDecimal initialFalsePositivesPercentage = new BigDecimal("0.0");
	public static BigDecimal initialFalseNegativesPercentage = new BigDecimal("0.0");
	public static BigDecimal initialFalseNegativesAndFalsePositivesPercentage = new BigDecimal("0.0");
	// noise increment step
	public static BigDecimal fpIncreaseStep = new BigDecimal("5.0");
	public static BigDecimal fnIncreaseStep = new BigDecimal("5.0");
	public static BigDecimal fpAndFnIncreaseStep = new BigDecimal("5.0");
	// maximum noise level in the simulation
	public static BigDecimal maxFalsePositivesPercentage = new BigDecimal("100.0"); //= new BigDecimal("0.5");
	public static BigDecimal maxFalseNegativesPercentage = new BigDecimal("100.0"); //= new BigDecimal("0.5");
	public static BigDecimal maxFalseNegativesAndFalsePositivesPercentage = new BigDecimal("100.0"); //= new BigDecimal("0.5");

	

	// *** RESAMPLING FOR DIFFICULTY (POSITIVE RATE) ***
	// number of resamplings per PR level
	public static int numberOfResamplingsPerLevel = 10;
	// if true, chooses a size for resamplet datasets whiich is constant among PR levels
	public static boolean sameSubDatasetSizeAmongProportions = false;
	// minimum PR
	public static BigDecimal minPositiveExamplePercentProportion = new BigDecimal("5.0");
	// maximum PR
	public static BigDecimal maxPositiveExamplePercentProportion = new BigDecimal("50.0");
	// PR increment step
	public static BigDecimal positiveExamplePercentProportionIncreaseStep = new BigDecimal("5.0");



	// sets parameters for logistic classifier
	private Classifier setLogisticOptions (Classifier cls, int M){
		String[] options = null;
		try {
			//options = weka.core.Utils.splitOptions("-D -M " + M);
			options = weka.core.Utils.splitOptions("-M " + M);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			cls.setOptions(options);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cls;
	}

}
