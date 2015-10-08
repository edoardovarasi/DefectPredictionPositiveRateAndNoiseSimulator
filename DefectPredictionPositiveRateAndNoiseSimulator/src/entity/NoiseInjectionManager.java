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

import weka.core.Attribute;
import weka.core.Instances;



/**
 * This class manages the injection of noise in a given dataset.
 * We use it to simulate noise in train dataset only, to see how
 * the classifer behaves when trained with a biased dataset.
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class NoiseInjectionManager {

	private boolean verbose = false;


	/**
	 * 
	 * Increments fp and fn by specified percentages.
	 * Randomize order of instances and modifies instances until noise quota is reached.
	 * Than randomized instances again.
	 * NOTE: It modifies the given dataset, because it is a reference.
	 *  
	 * @param origDataset
	 * @param fpPercentage
	 * @param fnPercentage
	 * @return Instances noisyDataset
	 */
	public Instances addNoiseToDataset(Instances origDataset, BigDecimal fpPercentage, BigDecimal fnPercentage){

		// exits if no noise must be added
		if(fnPercentage.equals(BigDecimal.ZERO) && fpPercentage.equals(BigDecimal.ZERO)){
			if(verbose)System.out.println("[NoiseManager , addNoiseToDataset] nessun errore da aggiungere");
			return origDataset;
		}

		// total instances in dataset
		int numInstances = origDataset.numInstances(); 

		// finds positive (buggy) and negative (non-buggy) instances numbers
		int numOfPositives = 0;
		int numOfNegatives = 0;

		for(int j=0; j<numInstances; j++){

			if(origDataset.instance(j).stringValue(origDataset.classIndex()).equals(Settings.buggyLabel)){
				numOfPositives++;
			}
			// this is a redundant control, but better safe than sorry
			else if(origDataset.instance(j).stringValue(origDataset.classIndex()).equals(Settings.nonbuggyLabel)){
				numOfNegatives++;
			}
		}


		// calculates the number of false positives to insert
		int fpToInsert = (int) Math.round(numOfNegatives * fpPercentage.doubleValue()/100);
		int fpInserted = 0;
		if(verbose)System.out.println("\n\n[NoiseManager , addNoiseToDataset] fpToInsert= "+fpToInsert+ ", totIntances= " + origDataset.numInstances() + " true negatives= " + numOfNegatives + " %fp= " + fpPercentage);

		// calculates the number of false negatives to insert
		int fnToInsert = (int) Math.round(numOfPositives * fnPercentage.doubleValue()/100);
		int fnInserted = 0;
		if(verbose)System.out.println("[NoiseManager , addNoiseToDataset] fnToInsert= "+fnToInsert+ ", totIntances= " + origDataset.numInstances() + " true positives= " + numOfPositives  + " %fn= " + fnPercentage);

		if(verbose)System.out.println("[NoiseManager , addNoiseToDataset] buggy label: " + Settings.buggyLabel + " - nonbuggy label: " + Settings.nonbuggyLabel);

		// randomize order of instances
		origDataset.randomize(RandomizationManager.randomGenerator);

		for (int i = 0; i < origDataset.numInstances(); i++) 
		{ 
			if(verbose)System.out.print("\nORIGINAL VALUES: " + origDataset.instance(i).value(origDataset.attribute(origDataset.classIndex())) + " - " +origDataset.instance(i).stringValue(origDataset.classIndex())); 

			// gets the classification attribute (it HAS to be the last)
			Attribute att = origDataset.instance(i).attribute(origDataset.classIndex());

			// if there are fn to add and this is a positive instances it turns it into a negative, making it a fn 
			if ((fnInserted<fnToInsert)&&(origDataset.instance(i).stringValue(origDataset.classIndex()).equals(Settings.buggyLabel))){

				origDataset.instance(i).setValue(att, Settings.nonbuggyLabel);
				fnInserted++;
				if(verbose)System.out.print(" - added FN, added " + fnInserted + " of " + fnToInsert + " ");				
			}

			// if there are fp to add and this is a negative instances it turns it into a positive, making it a fp 
			else if ((fpInserted<fpToInsert)&&(origDataset.instance(i).stringValue(origDataset.classIndex()).equals(Settings.nonbuggyLabel))){

				origDataset.instance(i).setValue(att, Settings.buggyLabel);
				fpInserted++;
				if(verbose)System.out.print(" - added FP, added " + fpInserted + " of " + fpToInsert + " ");

			}

			if(verbose)System.out.print(" FINAL ELEMENT VALUES: " + origDataset.instance(i).value(origDataset.attribute(origDataset.classIndex())) + " - " +origDataset.instance(i).stringValue(origDataset.classIndex()));
		} 

		// randomize order of instances
		origDataset.randomize(RandomizationManager.randomGenerator);
		return origDataset;
	}




	/**
	 * Increments fp and fn in combination by a specified percentages.
	 * Randomize order of instances and modifies instances until noise quota is reached.
	 * Than randomized instances again.
	 * NOTE: It modifies the given dataset, because it is a reference.
	 * 
	 * @param origDataset
	 * @param combinedFpFnPercentage
	 * @return noisydata
	 */
	public Instances addNoiseToDataset(Instances origDataset, BigDecimal combinedFpFnPercentage){

		// exits if no noise must be added
		if(combinedFpFnPercentage.equals(BigDecimal.ZERO)){
			if(verbose)System.out.println("[NoiseManager , addNoiseToDataset] nessun errore da aggiungere");
			return origDataset;
		}

		// total instances in dataset
		int numInstances = origDataset.numInstances(); 

		// finds positive (buggy) and negative (non-buggy) instances numbers
		int fpAndFnToInsert = (int) Math.round(numInstances * combinedFpFnPercentage.doubleValue()/100);
		int fpAndFnInserted = 0;
		if(verbose)System.out.println("\n\n[NoiseManager , addNoiseToDataset] fpAndFnToInsert= "+fpAndFnToInsert+ ", totIntances= " + origDataset.numInstances());

		if(verbose)System.out.println("[NoiseManager , addNoiseToDataset] buggy label: " + Settings.buggyLabel + " - nonbuggy label: " + Settings.nonbuggyLabel);

		// randomize order of instances
		origDataset.randomize(RandomizationManager.randomGenerator);

		for (int i = 0; i < origDataset.numInstances(); i++) 
		{ 
			if(verbose)System.out.print("\nORIGINAL VALUES: " + origDataset.instance(i).value(origDataset.attribute(origDataset.classIndex())) + " - " +origDataset.instance(i).stringValue(origDataset.classIndex())); 

			// gets the classification attribute (it HAS to be the last)
			Attribute att = origDataset.instance(i).attribute(origDataset.classIndex());

			// if there are fn or fp to add  
			if (fpAndFnInserted<fpAndFnToInsert){

				// if this is a positive instances it turns it into a negative, making it a fn
				if(origDataset.instance(i).stringValue(origDataset.classIndex()).equals(Settings.buggyLabel)){
					
					if(verbose)System.out.print(" - added FN, added " + fpAndFnInserted + " of " + fpAndFnToInsert + " ");
					origDataset.instance(i).setValue(att, Settings.nonbuggyLabel);
					fpAndFnInserted++;
				}

				// if this is a negative instances it turns it into a positive, making it a fp
				else if(origDataset.instance(i).stringValue(origDataset.classIndex()).equals(Settings.nonbuggyLabel)){
					
					if(verbose)System.out.print(" - added FP, added " + fpAndFnInserted + " of " + fpAndFnToInsert + " ");
					origDataset.instance(i).setValue(att, Settings.buggyLabel);
					fpAndFnInserted++;
				}
			}

			if(verbose)System.out.print(" FINAL ELEMENT VALUES: " + origDataset.instance(i).value(origDataset.attribute(origDataset.classIndex())) + " - " +origDataset.instance(i).stringValue(origDataset.classIndex()));
		} 

		// randomize order of instances
		origDataset.randomize(RandomizationManager.randomGenerator);
		return origDataset;
	}
}
