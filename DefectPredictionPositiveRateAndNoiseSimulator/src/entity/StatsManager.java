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

import java.util.Vector;

/**
 * Collects stats for all iteration of a level (noise, PR or combination).
 * When repetitions are over, gives averages and variances of each stat, for the level.
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class StatsManager {
	
	private Vector<Double> classificationTPs;
	private Vector<Double> classificationTNs;
	private Vector<Double> classificationFPs;
	private Vector<Double> classificationFNs;
	private Vector<Double> kappas;
	
	private Vector<Double> precisions;
	private Vector<Double> recalls;
	private Vector<Double> fmeasures;
	
	// sample size
	int n;
	// sample mean of vlues from n iterations
	// mean values are put in a stats objects
	private Stats avgStats = null;
	// sample variance
	// variance values are put in a stats objects
	private Stats varStats = null;
	// confidence interval
	// lower and upper bounds values are put in a stats objects
	private Stats cIinfStats = null; // lower bounds
	private Stats cIsupStats = null; // upper bounds
	
	public StatsManager(){
		
		this.classificationTPs= new Vector<Double>();
		this.classificationTNs= new Vector<Double>();
		this.classificationFPs= new Vector<Double>();
		this.classificationFNs= new Vector<Double>();
		this.kappas= new Vector<Double>();
		
		this.precisions = new Vector<Double>();
		this.recalls = new Vector<Double>();
		this.fmeasures = new Vector<Double>();
	}
	
	// register the stats for an iteration
	public void registerIterationStats (Stats stats){
		
		this.classificationTPs.add(stats.getClassificationTP());
		this.classificationTNs.add(stats.getClassificationTN());
		this.classificationFPs.add(stats.getClassificationFP());
		this.classificationFNs.add(stats.getClassificationFN());
		this.kappas.add(stats.getKappa());

		this.precisions.add(stats.getPrecision());
		this.recalls.add(stats.getRecall());
		this.fmeasures.add(stats.getFmeasure());
	}
	
	private double calcAvg(Vector<Double> data){
		int n=0;
		double X=0;
		for(Double x: data){
			n++;
			X+=x;
		}
		return X/n;
	}
	
	private double calcVar(Vector<Double> data, double avg){
		double S=0;
		double X=avg;
		double n=0;
		for(Double x: data){
			n++;
			S+=(Math.pow((x-X), 2));
		}
		return S/(n-1);
	}

	/**
	 * Calculates sample mean of collected stats from iterations
	 * 
	 * @return stat object with sample mean values for each metric
	 * 
	 */
	public Stats getAvgStats() {
		
		// computes only if null
		if(this.avgStats!=null)return this.avgStats;
		
		double precisionAvg = this.calcAvg(this.precisions);
		double recallAvg = this.calcAvg(this.recalls);
		double fmeasureAvg = this.calcAvg(this.fmeasures);
		double classificationTPavg = this.calcAvg(this.classificationTPs);
		double classificationTNavg = this.calcAvg(this.classificationTNs);
		double classificationFPavg = this.calcAvg(this.classificationFPs);
		double classificationFNavg = this.calcAvg(this.classificationFNs);
		double kappaAvg = this.calcAvg(this.kappas);
		
		
		this.avgStats= new Stats(classificationTPavg, classificationTNavg, classificationFPavg, classificationFNavg, kappaAvg, precisionAvg, recallAvg, fmeasureAvg);
		return this.avgStats;
	
	}
	
	/**
	 * Calculates sample variance of collected stats from iterations
	 * 
	 * @return  stat object with sample variance values for each metric
	 */
	public Stats getVarStats() {
		
		if(this.varStats!=null)return this.varStats;
		
		// calculates means if not yet done
		Stats avgStats=this.getAvgStats();
		
		double precisionVar = calcVar(this.precisions, avgStats.getPrecision());
		double recallVar = calcVar(this.recalls, avgStats.getRecall());
		double fmeasureVar = calcVar(this.fmeasures, avgStats.getFmeasure());		

		this.varStats = new Stats(0, 0, 0, 0, 0, precisionVar, recallVar, fmeasureVar);
		
		return varStats;
	}
	
	
	/**
	 * Confidence intervals lower bounds
	 * 
	 * @return stat object with lower bound values for each metric
	 */
	public Stats getCIinfStats(){
		
		if (this.cIinfStats==null){
			double cIinfPrecision = this.getAvgStats().getPrecision() - Settings.quantile*Math.sqrt((this.getVarStats().getPrecision())/this.precisions.size());
			double cIinfRecall = this.getAvgStats().getRecall() - Settings.quantile*Math.sqrt((this.getVarStats().getRecall())/this.recalls.size());
			double cIinfFmeasure = this.getAvgStats().getFmeasure() - Settings.quantile*Math.sqrt((this.getVarStats().getFmeasure())/this.fmeasures.size());

			this.cIinfStats = new Stats(0,0,0,0,0,cIinfPrecision,cIinfRecall,cIinfFmeasure);
			return this.cIinfStats;
			
		}
				
		else return this.cIinfStats;
	}
	
	/**
	 * Confidence intervals upper bounds
	 * 
	 * @return stat object with upper bound values for each metric
	 */
	public Stats getCIsupStats(){
		
		if (this.cIsupStats==null){
			double cIsupPrecision = this.getAvgStats().getPrecision() + Settings.quantile*Math.sqrt((this.getVarStats().getPrecision())/this.precisions.size());
			double cIsupRecall = this.getAvgStats().getRecall() + Settings.quantile*Math.sqrt((this.getVarStats().getRecall())/this.recalls.size());
			double cIsupFmeasure = this.getAvgStats().getFmeasure() + Settings.quantile*Math.sqrt((this.getVarStats().getFmeasure())/this.fmeasures.size());

			this.cIsupStats = new Stats(0,0,0,0,0,cIsupPrecision,cIsupRecall,cIsupFmeasure);
			return this.cIsupStats;
			
		}
				
		else return this.cIsupStats;
	}
	
	
	
}
