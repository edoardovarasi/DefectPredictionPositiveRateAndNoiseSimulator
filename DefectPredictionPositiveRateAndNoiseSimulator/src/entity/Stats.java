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



/**
 * 
 * This class contains stats values for an iteration or a level
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class Stats {
	
	
	private double classificationTP;
	private double classificationTN;
	private double classificationFP;
	private double classificationFN;
	private double kappa;
	
	private double precision;
	private double recall;
	private double fmeasure;
	
	
	public Stats(double classificationTP, double classificationTN, double classificationFP, double classificationFN, double kappa,  double precision, double recall, double fmeasure){
		
		this.classificationTP = classificationTP;
		this.classificationTN = classificationTN;
		this.classificationFP = classificationFP;
		this.classificationFN = classificationFN;
		this.kappa = kappa;

		this.precision=precision;
		this.recall=recall;
		this.fmeasure=fmeasure;
	}
	
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getRecall() {
		return recall;
	}
	public void setRecall(double recall) {
		this.recall = recall;
	}
	public double getFmeasure() {
		return fmeasure;
	}
	public void setFmeasure(double fmeasure) {
		this.fmeasure = fmeasure;
	}
	
	@Override
	public String toString() {
		return new String ("P= " + this.getPrecision() + ", R= " + this.getRecall() + ", F1= "+ this.getFmeasure());
	}

	public double getClassificationTP() {
		return classificationTP;
	}

	public void setClassificationTP(double classificationTP) {
		this.classificationTP = classificationTP;
	}

	public double getClassificationTN() {
		return classificationTN;
	}

	public void setClassificationTN(double classificationTN) {
		this.classificationTN = classificationTN;
	}

	public double getClassificationFP() {
		return classificationFP;
	}

	public void setClassificationFP(double classificationFP) {
		this.classificationFP = classificationFP;
	}

	public double getClassificationFN() {
		return classificationFN;
	}

	public void setClassificationFN(double classificationFN) {
		this.classificationFN = classificationFN;
	}

	public double getKappa() {
		return kappa;
	}

	public void setKappa(double kappa) {
		this.kappa = kappa;
	}
}
