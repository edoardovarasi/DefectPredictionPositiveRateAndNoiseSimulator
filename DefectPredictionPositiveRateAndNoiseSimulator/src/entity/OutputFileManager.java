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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Manages the output on file.
 * 
 * NOTE: commented parts print also CONFIDENCE INTERVALS of the averages of interest, 
 * they should still work if substituted to the non commented parts.
 * 
 * A future development of the tool could be to make the choose the output.
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class OutputFileManager {

	boolean verbose = false;

	String outputDirPath;
	File outputDir;
	String outputFilePath;
	File outputFile;
	BufferedWriter writer;

	/**
	 * 
	 * Constructor initialize output file with informations from the recieved ExperimentData object.
	 * File layout depends on the type of experiment in execution.
	 * 
	 * @param experimentData: dati dell'esperimento in esecuzione
	 */
	public OutputFileManager(ExperimentData experimentData){

		String datasetfileInfo = this.stripExtension(experimentData.getDatasetName());
		String classifierInfo = experimentData.getClassifier().getClass().getSimpleName();
		String experimentTypeInfo = experimentData.getExperimentType().toString();
		DateFormat date = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		String fileName = date.format(new Date()) + "_stats.csv";

		outputDirPath = (Settings.OUTPUT_PATH + "\\" + datasetfileInfo + "\\" + classifierInfo + "\\" + experimentTypeInfo);
		outputDir = new File(outputDirPath);
		boolean dirCreated = outputDir.mkdirs();
		if(verbose)System.out.println("[OutputFileManager] directory created? " + dirCreated);

		outputFilePath = outputDirPath + "\\" + fileName;

		outputFile = new File(outputFilePath); 
		try {
			writer = new BufferedWriter (new FileWriter(outputFile));
			
			// write the first line of the output csv, changes with respect to the experiment
			switch(experimentData.getExperimentType()){
			case NOISE :
				if(experimentData.getNoiseType().equals(NoiseType.INDEPENDENT_FP_FN)){
					//writer.write("%FP;%FN;Precision AVG;Precision VAR;Precision CI inf;Precision CI sup;Recall AVG;Recall VAR;Recall CI inf;Recall CI sup;F-Measure AVG;F-Measure VAR;F-Measure CI inf;F-Measure CI sup\n");
					writer.write("%FP;%FN;classTP AVG;classTN AVG;classFP AVG;classFN AVG;kappa AVG;Precision AVG;Recall AVG;F-Measure AVG\n");

				}
				else{
					//writer.write("%(FP&FN);Precision AVG;Precision VAR;Precision CI inf;Precision CI sup;Recall AVG;Recall VAR;Recall CI inf;Recall CI sup;F-Measure AVG;F-Measure VAR;F-Measure CI inf;F-Measure CI sup\n");
					writer.write("%(FP&FN);classTP AVG;classTN AVG;classFP AVG;classFN AVG;kappa AVG;Precision AVG;Recall AVG;F-Measure AVG\n");
				}
				break;
			case DIFFICULTY:
				//writer.write("%PE;Precision AVG;Precision VAR;Precision CI inf;Precision CI sup;Recall AVG;Recall VAR;Recall CI inf;Recall CI sup;F-Measure AVG;F-Measure VAR;F-Measure CI inf;F-Measure CI sup\n");
				writer.write("%PR;classTP AVG;classTN AVG;classFP AVG;classFN AVG;kappa AVG;Precision AVG;Recall AVG;F-Measure AVG\n");
				break;
			case DIFFICULTYandNOISE:
				if(experimentData.getNoiseType().equals(NoiseType.INDEPENDENT_FP_FN)){

					//writer.write("%PE;%FP;%FN;Precision AVG;Precision VAR;Precision CI inf;Precision CI sup;Recall AVG;Recall VAR;Recall CI inf;Recall CI sup;F-Measure AVG;F-Measure VAR;F-Measure CI inf;F-Measure CI sup\n");
					writer.write("%PR;%FP;%FN;classTP AVG;classTN AVG;classFP AVG;classFN AVG;kappa AVG;Precision AVG;Recall AVG;F-Measure AVG\n");

				}
				else{
					//writer.write("%PE;%(FP&FN);Precision AVG;Precision VAR;Precision CI inf;Precision CI sup;Recall AVG;Recall VAR;Recall CI inf;Recall CI sup;F-Measure AVG;F-Measure VAR;F-Measure CI inf;F-Measure CI sup\n");
					writer.write("%PR;%(FP&FN);classTP AVG;classTN AVG;classFP AVG;classFN AVG;kappa AVG;Precision AVG;Recall AVG;F-Measure AVG\n");
				}
				break;
			}

			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes a line of the output file for an iteration of experiment NOISE (with independent FP and FN)
	 * OR
	 * Writes a line of the output file for an iteration of experiment DIFFICULTYandNOISE (with common FP and FN )
	 * 
	 * @param fpPercentage_or_positiveExamplesPercentage
	 * @param fnPercentage_Or_FpAndFnPercentage
	 * @param avgStats
	 * @param varStats
	 */
	public void printStatsToFile(BigDecimal fpPercentage_or_positiveExamplesPercentage, BigDecimal fnPercentage_Or_FpAndFnPercentage, StatsManager statsManager){

		//		Stats avgStats = statsManager.getAvgStats();
		//		Stats varStats = statsManager.getVarStats();
		//		Stats cIinfStats = statsManager.getCIinfStats();
		//		Stats cIsupStats = statsManager.getCIsupStats();
		//
		//		try {
		//			writer.write(fpPercentage_or_positiveExamplesPercentage.toPlainString()+";"
		//					+fnPercentage_Or_FpAndFnPercentage.toPlainString()+";"
		//
		//						+avgStats.getPrecision()+";"
		//						+varStats.getPrecision()+";"
		//						+cIinfStats.getPrecision()+";"
		//						+cIsupStats.getPrecision()+";"
		//						+avgStats.getRecall()+";"
		//						+varStats.getRecall()+";"
		//						+cIinfStats.getRecall()+";"
		//						+cIsupStats.getRecall()+";"
		//						+avgStats.getFmeasure()+";"
		//						+varStats.getFmeasure()+";"
		//						+cIinfStats.getFmeasure()+";"
		//						+cIsupStats.getFmeasure()+"\n");
		//			writer.flush();
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}


		Stats avgStats = statsManager.getAvgStats();

		try {
			writer.write(fpPercentage_or_positiveExamplesPercentage.toPlainString()+";"
					+fnPercentage_Or_FpAndFnPercentage.toPlainString()+";"

						+avgStats.getClassificationTP()+";"
						+avgStats.getClassificationTN()+";"
						+avgStats.getClassificationFP()+";"
						+avgStats.getClassificationFN()+";"
						+avgStats.getKappa()+";"
						+avgStats.getPrecision()+";"
						+avgStats.getRecall()+";"
						+avgStats.getFmeasure()+"\n");

			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	


	/**
	 Writes a line of the output file for an iteration of experiment DIFFICULTYandNOISE (with independent FP and FN)
	 * 
	 * @param positiveExamplesPercentage
	 * @param fpPercentage
	 * @param fnPercentage
	 * @param avgStats
	 * @param varStats
	 */
	public void printStatsToFile(BigDecimal positiveExamplesPercentage, BigDecimal fpPercentage, BigDecimal fnPercentage, StatsManager statsManager){

		//		Stats avgStats = statsManager.getAvgStats();
		//		Stats varStats = statsManager.getVarStats();
		//		Stats cIinfStats = statsManager.getCIinfStats();
		//		Stats cIsupStats = statsManager.getCIsupStats();
		//
		//		try {
		//			writer.write(positiveExamplesPercentage+";"
		//					+fpPercentage+";"
		//					+fnPercentage+";"
		//
		//						+avgStats.getPrecision()+";"
		//						+varStats.getPrecision()+";"
		//						+cIinfStats.getPrecision()+";"
		//						+cIsupStats.getPrecision()+";"
		//						+avgStats.getRecall()+";"
		//						+varStats.getRecall()+";"
		//						+cIinfStats.getRecall()+";"
		//						+cIsupStats.getRecall()+";"
		//						+avgStats.getFmeasure()+";"
		//						+varStats.getFmeasure()+";"
		//						+cIinfStats.getFmeasure()+";"
		//						+cIsupStats.getFmeasure()+"\n");
		//			writer.flush();
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		Stats avgStats = statsManager.getAvgStats();

		try {
			writer.write(positiveExamplesPercentage+";"
					+fpPercentage+";"
					+fnPercentage+";"

					+avgStats.getClassificationTP()+";"
					+avgStats.getClassificationTN()+";"
					+avgStats.getClassificationFP()+";"
					+avgStats.getClassificationFN()+";"
					+avgStats.getKappa()+";"
					+avgStats.getPrecision()+";"
					+avgStats.getRecall()+";"
					+avgStats.getFmeasure()+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	/**
	 * Writes a line of the output file for an iteration of experiment DIFFICULTY
	 * oppure
	 * Writes a line of the output file for an iteration of experiment NOISE (with common FP and FN)
	 * 
	 * @param positiveExamplesPercentage_Or_FpAndFnPercentage: can be PR or FP&FN
	 * @param avgStats
	 * @param varStats
	 */
	public void printStatsToFile(BigDecimal positiveExamplesPercentage_Or_FpAndFnPercentage, StatsManager statsManager){

//		Stats avgStats = statsManager.getAvgStats();
//		Stats varStats = statsManager.getVarStats();
//		Stats cIinfStats = statsManager.getCIinfStats();
//		Stats cIsupStats = statsManager.getCIsupStats();
//
//		try {
//			writer.write(positiveExamplesPercentage_Or_FpAndFnPercentage+";"
//
//						+avgStats.getPrecision()+";"
//						+varStats.getPrecision()+";"
//						+cIinfStats.getPrecision()+";"
//						+cIsupStats.getPrecision()+";"
//						+avgStats.getRecall()+";"
//						+varStats.getRecall()+";"
//						+cIinfStats.getRecall()+";"
//						+cIsupStats.getRecall()+";"
//						+avgStats.getFmeasure()+";"
//						+varStats.getFmeasure()+";"
//						+cIinfStats.getFmeasure()+";"
//						+cIsupStats.getFmeasure()+"\n");
//
//			writer.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Stats avgStats = statsManager.getAvgStats();

		try {
			writer.write(positiveExamplesPercentage_Or_FpAndFnPercentage+";"

				+avgStats.getClassificationTP()+";"
				+avgStats.getClassificationTN()+";"
				+avgStats.getClassificationFP()+";"
				+avgStats.getClassificationFN()+";"
				+avgStats.getKappa()+";"
				+avgStats.getPrecision()+";"
				+avgStats.getRecall()+";"
				+avgStats.getFmeasure()+"\n");

			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * Deletes the extension from a filename.
	 * 
	 * @param str
	 * @return
	 */
	private String stripExtension (String str) {
		// Handle null case specially.
		if (str == null) return null;

		// Get position of last '.'.
		int pos = str.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.
		if (pos == -1) return str;

		// Otherwise return the string, up to the dot.
		return str.substring(0, pos);
	}
}
