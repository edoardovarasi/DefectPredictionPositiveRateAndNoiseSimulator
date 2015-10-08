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
import weka.core.Instances;

/**
 * This class manages the resampling method for PR variation in a dataset
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class DifficultyResamplingManager {

	private boolean verbose = false;
	
	/**
	 * Return max dimensions of subdataset for a PR (total, p, n)
	 * @param originalDataset
	 * @param positiveExamplePercentProportion
	 * @return
	 */
	public SubdatasetDimensions calculateSubdatasetDimensionsForProportion (Instances originalDataset, BigDecimal positiveExamplePercentProportion){

		// size of subdataset, initialized to original size
		int total   = originalDataset.numInstances();
		// number of positive instances
		int p=0;
		// number of negative instances
		int n=0;
		// current PR
		int pp=0;
	
		// count positives
		for (int i = 0; i < total; i++) 
		{ 
			if (originalDataset.instance(i).stringValue(originalDataset.classIndex()).equals(Settings.buggyLabel)){
				p++;	
			}
		} 

		n = total - p;

		// finds actual PR
		pp = calculatePositivePercentCeil(p+n, p);

		if(verbose)System.out.println("[DifficultyResamplingManager , calculateSubdatasetDimensionsForProportion] attuale: p=" + p + " n=" + n + " pp = " + pp);

		// if current PR equals desired one, return current dimensions
		if (pp==positiveExamplePercentProportion.intValue())return new SubdatasetDimensions(p, n);

		// if current PR is greater than the desired one
		// decrements p until ceiling of current PR is greater than the desired one
		if (pp>positiveExamplePercentProportion.intValue()){
			while(pp>positiveExamplePercentProportion.intValue()){
				p--;
				pp = calculatePositivePercentCeil(p+n, p);
				if(verbose)System.out.println("[DifficultyResamplingManager , calculateSubdatasetDimensionsForProportion] p=" + p + " n=" + n + " pp = " + pp);
			}
			// goes back if the previous PR was "nearer" to the desired than the current one
			if(isPPPNearerThanPPToDesiredPercent(calculatePositivePercentCeil(p+1+n, p+1), pp, positiveExamplePercentProportion.intValue())){
				p++;
				pp = calculatePositivePercentCeil(p+n, p);
			}
		}

		// if current PR is less than the desired one
		// decrements n until ceiling of current PR is less than the desired one
		if (pp<positiveExamplePercentProportion.intValue()){
			while(pp<positiveExamplePercentProportion.intValue()){
				n--;
				pp = calculatePositivePercentCeil(p+n, p);
				if(verbose)System.out.println("[DifficultyResamplingManager , calculateSubdatasetDimensionsForProportion] p=" + p + " n=" + n + " pp = " + pp);
			}
			// goes back if the previous PR was "nearer" to the desired than the current one
			if(isPPPNearerThanPPToDesiredPercent(calculatePositivePercentCeil(p+n+1, p), pp, positiveExamplePercentProportion.intValue())){
				n++;
				pp = calculatePositivePercentCeil(p+n, p);
			}
		}

		if(verbose)System.out.println("[DifficultyResamplingManager , calculateSubdatasetDimensionsForProportion] finale p=" + p + " n=" + n + " pp = " + pp);
		return new SubdatasetDimensions(p, n);
	}



	
	/**
	 * 
	 * Calculates the sumbdataset size for all PR levels basing the decision on
	 * min PR, max PR and natural PR of the dataset
	 * 
	 * @param originalDataset
	 * @param maxPositiveExamplePercentProportion
	 * @param minPositiveExamplePercentProportion
	 * @return subdatset size
	 */
	public int calculateSubdatasetDimensionAmongProportions(Instances originalDataset, BigDecimal maxPositiveExamplePercentProportion, BigDecimal minPositiveExamplePercentProportion){


		SubdatasetDimensions maxPpDimensions = this.calculateSubdatasetDimensionsForProportion(originalDataset, maxPositiveExamplePercentProportion);
		SubdatasetDimensions minPpDimensions = this.calculateSubdatasetDimensionsForProportion(originalDataset, minPositiveExamplePercentProportion);
		
		if(verbose) System.out.println("[DifficultyResamplingManager, calculateSubdatasetDimensionAmongProportions] dimensioni maxPp: " + maxPpDimensions.toString());
		if(verbose) System.out.println("[DifficultyResamplingManager, calculateSubdatasetDimensionAmongProportions] dimensioni minPp: " + minPpDimensions.toString());

		// calculates size for max PR and for min PR, then chooses the smallest
		if(minPpDimensions.getTotalNumberOfElements()<= maxPpDimensions.getTotalNumberOfElements()){
			if(verbose) System.out.println("[DifficultyResamplingManager, calculateSubdatasetDimensionAmongProportions] chose size for minPp");
			return minPpDimensions.getTotalNumberOfElements();
		}
		else {
			if(verbose) System.out.println("[DifficultyResamplingManager, calculateSubdatasetDimensionAmongProportions] chose size for maxPp");
			return maxPpDimensions.getTotalNumberOfElements();
		}
	}
	
	/**
	 * 
	 * Generates a subdataset randomly resampling the original.
	 * The subdataset will have desired size and PR.
	 * Each time the original dataset is randomized, so the reampling can be repeated multiple times 
	 * 
	 * @param originalDataset
	 * @param pP
	 * @param dimension
	 * @return Instances resampledSubdataset
	 */
	public Instances generateResampledSubdataset (Instances originalDataset, BigDecimal pP, int dimension){
		
		int p = (int)Math.ceil((dimension * pP.doubleValue())/100.0);
		int n = dimension-p;
		SubdatasetDimensions subdatasetDimensions = new SubdatasetDimensions(p, n);
		
		if(verbose)System.out.println("[DifficultyResamplingManager, generateResampledSubdataset] resampling with: p=" + p +", n=" +n + ", tot=" + dimension + ", pp=" +pP + "%" );
		
		return this.generateResampledSubdataset(originalDataset, subdatasetDimensions);
		
	}



	/**
	 * called by generateResampledSubdataset
	 * 
	 * @param originalDataset
	 * @param subdatasetDimensions
	 * @return
	 */
	private Instances generateResampledSubdataset(Instances originalDataset, SubdatasetDimensions subdatasetDimensions){

		// creates an empty dataset
		Instances resampledSubdataset = new Instances(originalDataset);
		resampledSubdataset.delete();
		
		// randomize dataset instances order
		originalDataset.randomize(RandomizationManager.randomGenerator);

		// calc number of positives to insert
		int positivesToInsert = subdatasetDimensions.getP();
		if(verbose)System.out.println("[DifficultyResamplingManager, generateResampledSubdataset] positivesToInsert = " + positivesToInsert);

		// calc number of negatives to insert
		int negativesToInsert = subdatasetDimensions.getN();

		// iterates over the original dataset instances
		for (int i = 0; i < originalDataset.numInstances(); i++) 
		{ 
			// if instance is positive and more are needed in the new dataset, inserts into new dataset
			if ((positivesToInsert>0)&&(originalDataset.instance(i).stringValue(originalDataset.classIndex()).equals(Settings.buggyLabel))){
				resampledSubdataset.add(originalDataset.instance(i));
				positivesToInsert--;
			}

			// if instance is negative and more are needed in the new dataset, inserts into new dataset
			if ((negativesToInsert>0)&&(originalDataset.instance(i).stringValue(originalDataset.classIndex()).equals(Settings.nonbuggyLabel))){
				resampledSubdataset.add(originalDataset.instance(i));
				negativesToInsert--;
			}

		} 

		if(verbose)System.out.println("[DifficultyResamplingManager, generateResampledSubdataset] resampling terminato: " + this.printDatasetInfo(resampledSubdataset));
		return resampledSubdataset;
	}
	
	
	/**
	 * prints number of posive and negative instances and respective percentaghes
	 * @param dataset
	 * @return
	 */
	public String printDatasetInfo(Instances dataset){

		int positives = 0;
		int negatives = 0;

		for (int i = 0; i < dataset.numInstances(); i++) 
		{ 

			if (dataset.instance(i).stringValue(dataset.classIndex()).equals(Settings.buggyLabel)){
				positives++;	
			}

			if (dataset.instance(i).stringValue(dataset.classIndex()).equals(Settings.nonbuggyLabel)){
				negatives++;	
			}
		} 

		double percent = ((double)positives/(double)dataset.numInstances())*100;
		return new String ("totale istanze: " + dataset.numInstances() + ", p+n=" + (positives+negatives) 
				+ ", p: " + positives + ", n: " + negatives + ", %p : " + percent);
	}

	/**
	 * Given a number of positives and size, returns the ceiling of PR
	 * @param total
	 * @param p
	 * @return
	 */
	private int calculatePositivePercentCeil(int total, int p){
		double percent = (((double)p)/((double)total))*100;
		return (int)Math.ceil(percent);
	}

	/**
	 * 
	 * returns true if ppp is "nearer" to dpp than pp
	 * @param ppp
	 * @param pp
	 * @param dpp
	 * @return
	 */
	private boolean isPPPNearerThanPPToDesiredPercent(int ppp, int pp, int dpp){
		int dist1 = Math.abs(pp - dpp);
		int dist2 = Math.abs(ppp - dpp);

		if (dist1<=dist2) return false;
		else return true;
	}
}
