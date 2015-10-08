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

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import control.MainExperimentCtrl;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigDecimal;

import javax.swing.JCheckBox;

import entity.InvalidInputException;
import entity.NoiseType;
import entity.Settings;

import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Toolkit;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EtchedBorder;

/**
 * 
 * Manages the main window of the tool. All experiment settings can be controlled from here.
 * Manages exceptions from input logic or format errors.
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class MainWindow {

	private JFrame frmBugPredictionNoise;

	private SettingsView settingsView = new SettingsView();

	private JCheckBox chckbxNoise;
	private JCheckBox chckbxDifficulty;
	private JCheckBox chckbxDifficultyAndNoise;

	private JCheckBox independentFpAndFnCheckBox;
	JCheckBox combinedFpAndFnCheckBox;

	private JTextField inputPathTextField;
	private JTextField outputPathTextField;
	private JTextField numOfRepsTextField;
	private JTextField initFPtextField;
	private JTextField initFNtextField;
	private JTextField increaseStepFPtextField;
	private JTextField increaseStepFNtextField;
	private JTextField maxFPtextField;
	private JTextField maxFNtextField;

	private JButton btnLaunchSimulations;

	/**
	 * control variable, set as false if an input exception is raised
	 * 
	 */
	private boolean okToRun = true;

	private JTextField numberOfResamplingsPerLevelTextField;
	private JTextField minPositiveExamplePercentProportionTextField;
	private JTextField positiveExamplePercentProportionIncreaseStepTextField;
	private JTextField maxPositiveExamplePercentProportionTextField;
	private JTextField buggyLabelTextField;
	private JTextField nonbuggyLabelTextField;
	private JTextField classificationChoiceTextField;
	private JTextField nFoldTextField;

	boolean isClassifierSelectionWindowOpen = false;
	ClassifierSelectionDialog classifierSelectionDialog;

	private JPanel noiseInjectionPanel;
	private JPanel difficultyResamplingPanel;
	private JTextField initialFalseNegativesAndFalsePositivesPercentageTextField;
	private JTextField fpAndFnIncreaseStepTextField;
	private JTextField maxFalseNegativesAndFalsePositivesPercentageTextField;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmBugPredictionNoise.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBugPredictionNoise = new JFrame();
		frmBugPredictionNoise.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/img/logotv.gif")));
		frmBugPredictionNoise.setTitle("Defect Prediction Positive Rate and Noise Simulator");
		frmBugPredictionNoise.setBounds(100, 100, 558, 615);
		frmBugPredictionNoise.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBugPredictionNoise.getContentPane().setLayout(null);

		JPanel fileLocationPanel = new JPanel(); 
		fileLocationPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		fileLocationPanel.setBounds(6, 6, 528, 86);
		frmBugPredictionNoise.getContentPane().add(fileLocationPanel);
		fileLocationPanel.setLayout(null);

		JLabel filesLocationsLabel = new JLabel("Files Locations");
		filesLocationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		filesLocationsLabel.setBounds(201, 0, 105, 16);
		fileLocationPanel.add(filesLocationsLabel);

		JLabel lblInput = new JLabel("Input");
		lblInput.setBounds(12, 26, 61, 16);
		fileLocationPanel.add(lblInput);

		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(12, 56, 61, 16);
		fileLocationPanel.add(lblOutput);

		// input directory selection button
		JButton btnSelectInputDirectory = new JButton("Select Input Directory");
		btnSelectInputDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					System.out.println("selected input: directory" + selectedFile.getName());
					settingsView.setInputPath(selectedFile.getAbsolutePath());
					inputPathTextField.setText(settingsView.getInputPath());
				}
			}
		});

		btnSelectInputDirectory.setBounds(342, 24, 166, 20);
		fileLocationPanel.add(btnSelectInputDirectory);

		// output directory selection button
		JButton btnSelectOutputDirectory = new JButton("Select Output Directory");
		btnSelectOutputDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					System.out.println("selezionata cartella output: " + selectedFile.getName());
					settingsView.setOutputPath(selectedFile.getAbsolutePath());
					outputPathTextField.setText(settingsView.getOutputPath());
				}
			}
		});

		btnSelectOutputDirectory.setBounds(342, 54, 166, 20);
		fileLocationPanel.add(btnSelectOutputDirectory);

		// text to visualize input dir
		inputPathTextField = new JTextField();
		inputPathTextField.setEditable(false);
		inputPathTextField.setText(settingsView.getInputPath());
		inputPathTextField.setBounds(58, 24, 268, 20);
		fileLocationPanel.add(inputPathTextField);
		inputPathTextField.setColumns(10);

		// text to visualize output dir
		outputPathTextField = new JTextField();
		outputPathTextField.setEditable(false);
		outputPathTextField.setText(settingsView.getOutputPath());
		outputPathTextField.setBounds(58, 54, 268, 20);
		fileLocationPanel.add(outputPathTextField);
		outputPathTextField.setColumns(10);

		JPanel experimentTypesPanel = new JPanel();
		experimentTypesPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		experimentTypesPanel.setBounds(6, 93, 528, 42);
		frmBugPredictionNoise.getContentPane().add(experimentTypesPanel);
		experimentTypesPanel.setLayout(null);

		JLabel lblExperimentTypes = new JLabel("Experiment Factors");
		lblExperimentTypes.setLabelFor(experimentTypesPanel);
		lblExperimentTypes.setBounds(205, 0, 113, 14);
		experimentTypesPanel.add(lblExperimentTypes);

		// buttons for experiment type choice
		chckbxDifficulty = new JCheckBox("Positive Rate");
		chckbxDifficulty.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {

			}
		});
		chckbxDifficulty.setBounds(205, 17, 103, 23);
		chckbxDifficulty.setSelected(Settings.DifficultyExperiments);
		experimentTypesPanel.add(chckbxDifficulty);

		chckbxNoise = new JCheckBox("Noise");
		chckbxNoise.setBounds(8, 17, 97, 23);
		chckbxNoise.setSelected(Settings.NoiseExperiments);
		experimentTypesPanel.add(chckbxNoise);

		chckbxDifficultyAndNoise = new JCheckBox("Positive Rate and Noise");
		chckbxDifficultyAndNoise.setBounds(353, 17, 167, 23);
		chckbxDifficultyAndNoise.setSelected(Settings.DifficultyAndNoiseExperiments);
		experimentTypesPanel.add(chckbxDifficultyAndNoise);

		// launch simulation button
		btnLaunchSimulations = new JButton("Launch Simulations");
		btnLaunchSimulations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (okToRun==false)return;

				// set selected experiments
				settingsView.setDifficultyExperiments(chckbxDifficulty.isSelected());
				settingsView.setNoiseExperiments(chckbxNoise.isSelected());
				settingsView.setDifficultyAndNoiseExperiments(chckbxDifficultyAndNoise.isSelected());

				try {
					//System.out.println("CLICK");
					settingsView.checkConsistencyOfSettings();
					startExperiments();

				} catch (InvalidInputException e) {
					visualizeInputErrorDialog(e.getMessage());
					//initialize();
				}
			}
		});
		btnLaunchSimulations.setBounds(198, 524, 146, 42);
		frmBugPredictionNoise.getContentPane().add(btnLaunchSimulations);

		noiseInjectionPanel = new JPanel();
		noiseInjectionPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		noiseInjectionPanel.setBounds(6, 136, 528, 204);
		frmBugPredictionNoise.getContentPane().add(noiseInjectionPanel);
		noiseInjectionPanel.setLayout(null);

		JLabel lblNoiseInjection = new JLabel("Noise Injection");
		lblNoiseInjection.setLabelFor(noiseInjectionPanel);
		lblNoiseInjection.setBounds(226, 0, 101, 14);
		noiseInjectionPanel.add(lblNoiseInjection);

		// number of repetitions for noise
		numOfRepsTextField = new JTextField();
		numOfRepsTextField.setText(new String(new Integer(settingsView.getNumberOfReps()).toString()));
		numOfRepsTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					Integer inputValue = new Integer(numOfRepsTextField.getText());
					settingsView.setNumberOfReps(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("The number of repetitions must be an integer number.");
					numOfRepsTextField.setText(new String(new Integer(settingsView.getNumberOfReps()).toString()));
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					numOfRepsTextField.setText(new String(new Integer(settingsView.getNumberOfReps()).toString()));
				}

			}
		});
		numOfRepsTextField.setBounds(145, 20, 51, 20);
		noiseInjectionPanel.add(numOfRepsTextField);
		numOfRepsTextField.setColumns(10);

		JLabel numOfRepsLabel = new JLabel("Number of Repetitions");
		numOfRepsLabel.setLabelFor(numOfRepsTextField);
		numOfRepsLabel.setBounds(12, 23, 136, 14);
		noiseInjectionPanel.add(numOfRepsLabel);

		JPanel classicNoisePanel = new JPanel();
		classicNoisePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		classicNoisePanel.setBounds(9, 65, 508, 67);
		noiseInjectionPanel.add(classicNoisePanel);
		classicNoisePanel.setLayout(null);

		// initial FP
		initFPtextField = new JTextField();
		initFPtextField.setBounds(171, 12, 30, 20);
		classicNoisePanel.add(initFPtextField);
		initFPtextField.setText(settingsView.getInitialFalsePositivesPercentage().toString());
		initFPtextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				try {
					BigDecimal inputValue = new BigDecimal(initFPtextField.getText());
					settingsView.setInitialFalsePositivesPercentage(inputValue);
				}
				catch (NumberFormatException e){
					visualizeInputErrorDialog("FP initial percentage must be a number in this form: \"10.0\".");
					initFPtextField.setText(settingsView.getInitialFalsePositivesPercentage().toString());
				}
				catch (InvalidInputException e1){
					visualizeInputErrorDialog(e1.getMessage());
					initFPtextField.setText(settingsView.getInitialFalsePositivesPercentage().toString());
				}
			}
		});
		initFPtextField.setColumns(10);

		JLabel lblInitialOf = new JLabel("Initial % of False  Positives");
		lblInitialOf.setBounds(12, 15, 147, 14);
		classicNoisePanel.add(lblInitialOf);

		// initial FN 	
		initFNtextField = new JTextField();
		initFNtextField.setBounds(171, 40, 30, 20);
		classicNoisePanel.add(initFNtextField);
		initFNtextField.setText(settingsView.getInitialFalseNegativesPercentage().toString());
		initFNtextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					BigDecimal inputValue = new BigDecimal(initFNtextField.getText());
					settingsView.setInitialFalseNegativesPercentage(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("FN initial percentage must be a number in this form: \"10.0\".");
					initFNtextField.setText(settingsView.getInitialFalseNegativesPercentage().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					initFNtextField.setText(settingsView.getInitialFalseNegativesPercentage().toString());
				}
			}
		});
		initFNtextField.setColumns(10);

		JLabel lblInitialOf_1 = new JLabel("Initial % of False  Negatives");
		lblInitialOf_1.setBounds(12, 41, 153, 14);
		classicNoisePanel.add(lblInitialOf_1);

		// step FP
		increaseStepFPtextField = new JTextField();
		increaseStepFPtextField.setBounds(326, 12, 32, 20);
		classicNoisePanel.add(increaseStepFPtextField);
		increaseStepFPtextField.setText(settingsView.getFpIncreaseStep().toString());
		increaseStepFPtextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {	
				try {
					BigDecimal inputValue = new BigDecimal(increaseStepFPtextField.getText());
					settingsView.setFpIncreaseStep(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("FP step percentage must be a number in this form: \"10.0\".");
					increaseStepFPtextField.setText(settingsView.getFpIncreaseStep().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					increaseStepFPtextField.setText(settingsView.getFpIncreaseStep().toString());
				}
			}
		});
		increaseStepFPtextField.setColumns(10);

		JLabel lblFalsePositivesIncrease = new JLabel("FP Increase Steps");
		lblFalsePositivesIncrease.setBounds(216, 15, 115, 14);
		classicNoisePanel.add(lblFalsePositivesIncrease);

		// step FN
		increaseStepFNtextField = new JTextField();
		increaseStepFNtextField.setBounds(327, 38, 32, 20);
		classicNoisePanel.add(increaseStepFNtextField);
		increaseStepFNtextField.setText(settingsView.getFnIncreaseStep().toString());
		increaseStepFNtextField.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				try {
					BigDecimal inputValue = new BigDecimal(increaseStepFNtextField.getText());
					settingsView.setFnIncreaseStep(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("FN step percentage must be a number in this form: \"10.0\".");
					increaseStepFNtextField.setText(settingsView.getFnIncreaseStep().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					increaseStepFNtextField.setText(settingsView.getFnIncreaseStep().toString());
				}
			}
		});
		increaseStepFNtextField.setColumns(10);

		JLabel lblFalseNegativesIncrease = new JLabel("FN Increase Steps");
		lblFalseNegativesIncrease.setBounds(217, 41, 112, 14);
		classicNoisePanel.add(lblFalseNegativesIncrease);

		// max FP
		maxFPtextField = new JTextField();
		maxFPtextField.setBounds(463, 12, 40, 20);
		classicNoisePanel.add(maxFPtextField);
		maxFPtextField.setText(settingsView.getMaxFalsePositivesPercentage().toString());
		maxFPtextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					BigDecimal inputValue = new BigDecimal(maxFPtextField.getText());
					settingsView.setMaxFalsePositivesPercentage(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("Max FP percentage must be a number in this form: \"10.0\".");
					maxFPtextField.setText(settingsView.getMaxFalsePositivesPercentage().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					maxFPtextField.setText(settingsView.getMaxFalsePositivesPercentage().toString());
				}
			}
		});
		maxFPtextField.setColumns(10);

		JLabel maxFPlabel = new JLabel("Max % of FP");
		maxFPlabel.setBounds(383, 15, 80, 14);
		classicNoisePanel.add(maxFPlabel);

		// max FN
		maxFNtextField = new JTextField();
		maxFNtextField.setBounds(464, 38, 40, 20);
		classicNoisePanel.add(maxFNtextField);
		maxFNtextField.setText(settingsView.getMaxFalseNegativesPercentage().toString());
		maxFNtextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					BigDecimal inputValue = new BigDecimal(maxFNtextField.getText());
					settingsView.setMaxFalseNegativesPercentage(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("Max FN percentage must be a number in this form: \"10.0\".");
					maxFNtextField.setText(settingsView.getMaxFalseNegativesPercentage().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					maxFNtextField.setText(settingsView.getMaxFalseNegativesPercentage().toString());
				}
			}
		});
		maxFNtextField.setColumns(10);

		JLabel maxFNlabel = new JLabel("Max % of FN");
		maxFNlabel.setBounds(383, 41, 71, 14);
		classicNoisePanel.add(maxFNlabel);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setLayout(null);
		panel.setBounds(10, 157, 508, 41);
		noiseInjectionPanel.add(panel);

		initialFalseNegativesAndFalsePositivesPercentageTextField = new JTextField();
		initialFalseNegativesAndFalsePositivesPercentageTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {

				try {
					BigDecimal inputValue = new BigDecimal(initialFalseNegativesAndFalsePositivesPercentageTextField.getText());
					settingsView.setInitialFalseNegativesAndFalsePositivesPercentage(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("FP&FN initial percentage must be a number in this form: \"10.0\".");
					initialFalseNegativesAndFalsePositivesPercentageTextField.setText(settingsView.getInitialFalseNegativesAndFalsePositivesPercentage().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					initialFalseNegativesAndFalsePositivesPercentageTextField.setText(settingsView.getInitialFalseNegativesAndFalsePositivesPercentage().toString());
				}


			}
		});
		initialFalseNegativesAndFalsePositivesPercentageTextField.setText(settingsView.getInitialFalseNegativesAndFalsePositivesPercentage().toString());
		initialFalseNegativesAndFalsePositivesPercentageTextField.setColumns(10);
		initialFalseNegativesAndFalsePositivesPercentageTextField.setBounds(119, 12, 30, 20);
		panel.add(initialFalseNegativesAndFalsePositivesPercentageTextField);

		JLabel lblInitialOf_2 = new JLabel("Initial % of FP & FN");
		lblInitialOf_2.setBounds(12, 15, 147, 14);
		panel.add(lblInitialOf_2);

		fpAndFnIncreaseStepTextField = new JTextField();
		fpAndFnIncreaseStepTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				try {
					BigDecimal inputValue = new BigDecimal(fpAndFnIncreaseStepTextField.getText());
					settingsView.setFpAndFnIncreaseStep(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("FP&FN percentage step must be a number in this form: \"10.0\".");
					fpAndFnIncreaseStepTextField.setText(settingsView.getFpAndFnIncreaseStep().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					fpAndFnIncreaseStepTextField.setText(settingsView.getFpAndFnIncreaseStep().toString());
				}
			}
		});
		fpAndFnIncreaseStepTextField.setText(settingsView.getFpAndFnIncreaseStep().toString());
		fpAndFnIncreaseStepTextField.setColumns(10);
		fpAndFnIncreaseStepTextField.setBounds(304, 12, 32, 20);
		panel.add(fpAndFnIncreaseStepTextField);

		JLabel lblNoiseIncreaseSteps = new JLabel("FP & FN Increase Steps");
		lblNoiseIncreaseSteps.setBounds(167, 15, 138, 14);
		panel.add(lblNoiseIncreaseSteps);

		maxFalseNegativesAndFalsePositivesPercentageTextField = new JTextField();
		maxFalseNegativesAndFalsePositivesPercentageTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				try {
					BigDecimal inputValue = new BigDecimal(maxFalseNegativesAndFalsePositivesPercentageTextField.getText());
					settingsView.setMaxFalseNegativesAndFalsePositivesPercentage(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("FP&FN maximum percentage must be a number in this form: \"10.0\".");
					maxFalseNegativesAndFalsePositivesPercentageTextField.setText(settingsView.getMaxFalseNegativesAndFalsePositivesPercentage().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					maxFalseNegativesAndFalsePositivesPercentageTextField.setText(settingsView.getMaxFalseNegativesAndFalsePositivesPercentage().toString());
				}
			}
		});
		maxFalseNegativesAndFalsePositivesPercentageTextField.setText(settingsView.getMaxFalseNegativesAndFalsePositivesPercentage().toString());
		maxFalseNegativesAndFalsePositivesPercentageTextField.setColumns(10);
		maxFalseNegativesAndFalsePositivesPercentageTextField.setBounds(463, 12, 40, 20);
		panel.add(maxFalseNegativesAndFalsePositivesPercentageTextField);

		JLabel lblMaxOf_1 = new JLabel("Max % of FP & FN");
		lblMaxOf_1.setBounds(355, 15, 103, 14);
		panel.add(lblMaxOf_1);

		independentFpAndFnCheckBox = new JCheckBox("Independent Specified levels");
		independentFpAndFnCheckBox.setSelected(Settings.noiseType.equals(NoiseType.INDEPENDENT_FP_FN));
		independentFpAndFnCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (independentFpAndFnCheckBox.isSelected()) {
					combinedFpAndFnCheckBox.setSelected(false);
					settingsView.setNoiseType(NoiseType.INDEPENDENT_FP_FN);
				}
				else {
					combinedFpAndFnCheckBox.setSelected(true);
					settingsView.setNoiseType(NoiseType.COMBINED_FP_FN);
				}
			}
		});
		independentFpAndFnCheckBox.setBounds(11, 44, 229, 24);
		noiseInjectionPanel.add(independentFpAndFnCheckBox);

		combinedFpAndFnCheckBox = new JCheckBox("Combined Level");
		combinedFpAndFnCheckBox.setSelected(Settings.noiseType.equals(NoiseType.COMBINED_FP_FN));
		combinedFpAndFnCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				if (combinedFpAndFnCheckBox.isSelected()) {
					independentFpAndFnCheckBox.setSelected(false);
					settingsView.setNoiseType(NoiseType.COMBINED_FP_FN);
				}
				else {
					independentFpAndFnCheckBox.setSelected(true);
					settingsView.setNoiseType(NoiseType.INDEPENDENT_FP_FN);
				}
			}
		});
		combinedFpAndFnCheckBox.setBounds(13, 136, 130, 24);
		noiseInjectionPanel.add(combinedFpAndFnCheckBox);

		difficultyResamplingPanel = new JPanel();
		difficultyResamplingPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		difficultyResamplingPanel.setBounds(6, 341, 528, 71);
		frmBugPredictionNoise.getContentPane().add(difficultyResamplingPanel);
		difficultyResamplingPanel.setLayout(null);

		JLabel lblDifficultyResampling = new JLabel("Positive Rate Resampling");
		lblDifficultyResampling.setBounds(186, 0, 166, 16);
		difficultyResamplingPanel.add(lblDifficultyResampling);

		// number of resamplings per level (PR)
		numberOfResamplingsPerLevelTextField = new JTextField();
		numberOfResamplingsPerLevelTextField.setText(new String(new Integer(settingsView.getNumberOfResamplingsPerLevel()).toString()));
		numberOfResamplingsPerLevelTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					Integer inputValue = new Integer(numberOfResamplingsPerLevelTextField.getText());
					settingsView.setNumberOfResamplingsPerLevel(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("The number of resamplings must be an integer number.");
					numberOfResamplingsPerLevelTextField.setText(new String(new Integer(settingsView.getNumberOfResamplingsPerLevel()).toString()));
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					numberOfResamplingsPerLevelTextField.setText(new String(new Integer(settingsView.getNumberOfResamplingsPerLevel()).toString()));
				}
			}
		});
		numberOfResamplingsPerLevelTextField.setColumns(10);
		numberOfResamplingsPerLevelTextField.setBounds(146, 20, 51, 20);
		difficultyResamplingPanel.add(numberOfResamplingsPerLevelTextField);

		JLabel lblNumberOfResamplings = new JLabel("Number of Resamplings");
		lblNumberOfResamplings.setBounds(8, 23, 136, 14);
		difficultyResamplingPanel.add(lblNumberOfResamplings);

		// minimum PR
		minPositiveExamplePercentProportionTextField = new JTextField();
		minPositiveExamplePercentProportionTextField.setText(settingsView.getMinPositiveExamplePercentProportion().toString());
		minPositiveExamplePercentProportionTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					BigDecimal inputValue = new BigDecimal(minPositiveExamplePercentProportionTextField.getText());
					settingsView.setMinPositiveExamplePercentProportion(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("Maximum percentage of negatives  must be a number in this form: \"10.0\".");
					minPositiveExamplePercentProportionTextField.setText(settingsView.getMinPositiveExamplePercentProportion().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					minPositiveExamplePercentProportionTextField.setText(settingsView.getMinPositiveExamplePercentProportion().toString());
				}
			}
		});
		minPositiveExamplePercentProportionTextField.setColumns(10);
		minPositiveExamplePercentProportionTextField.setBounds(109, 44, 30, 20);
		difficultyResamplingPanel.add(minPositiveExamplePercentProportionTextField);

		JLabel lblMaxOf = new JLabel("Min Positive Rate");
		lblMaxOf.setBounds(8, 47, 147, 14);
		difficultyResamplingPanel.add(lblMaxOf);

		// step PR
		positiveExamplePercentProportionIncreaseStepTextField = new JTextField();
		positiveExamplePercentProportionIncreaseStepTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					BigDecimal inputValue = new BigDecimal(positiveExamplePercentProportionIncreaseStepTextField.getText());
					settingsView.setPositiveExamplePercentProportionIncreaseStep(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("Positive examples percentage increase step must be a number in this form: \"10.0\".");
					positiveExamplePercentProportionIncreaseStepTextField.setText(settingsView.getPositiveExamplePercentProportionIncreaseStep().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					positiveExamplePercentProportionIncreaseStepTextField.setText(settingsView.getPositiveExamplePercentProportionIncreaseStep().toString());
				}
			}
		});
		positiveExamplePercentProportionIncreaseStepTextField.setText(settingsView.getPositiveExamplePercentProportionIncreaseStep().toString());
		positiveExamplePercentProportionIncreaseStepTextField.setColumns(10);
		positiveExamplePercentProportionIncreaseStepTextField.setBounds(320, 44, 32, 20);
		difficultyResamplingPanel.add(positiveExamplePercentProportionIncreaseStepTextField);

		JLabel lblPositivesIncrease = new JLabel("Positive Rate Increase Step");
		lblPositivesIncrease.setBounds(154, 47, 164, 14);
		difficultyResamplingPanel.add(lblPositivesIncrease);

		// min PR
		maxPositiveExamplePercentProportionTextField = new JTextField();
		maxPositiveExamplePercentProportionTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					BigDecimal inputValue = new BigDecimal(maxPositiveExamplePercentProportionTextField.getText());
					settingsView.setMaxPositiveExamplePercentProportion(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("Maximum percentage of positives must be a number in this form: \"10.0\".");
					maxPositiveExamplePercentProportionTextField.setText(settingsView.getMaxPositiveExamplePercentProportion().toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					maxPositiveExamplePercentProportionTextField.setText(settingsView.getMaxPositiveExamplePercentProportion().toString());
				}
			}
		});
		maxPositiveExamplePercentProportionTextField.setText(settingsView.getMaxPositiveExamplePercentProportion().toString());
		maxPositiveExamplePercentProportionTextField.setColumns(10);
		maxPositiveExamplePercentProportionTextField.setBounds(480, 44, 40, 20);
		difficultyResamplingPanel.add(maxPositiveExamplePercentProportionTextField);

		JLabel lblMinOf = new JLabel("Max Positive Rate");
		lblMinOf.setBounds(374, 47, 106, 14);
		difficultyResamplingPanel.add(lblMinOf);

		// checkbox same size among levels
		JCheckBox sameSubDatasetSizeAmongProportionsCheckBox = new JCheckBox("Same subdataset size among PR levels");
		sameSubDatasetSizeAmongProportionsCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (sameSubDatasetSizeAmongProportionsCheckBox.isSelected()) {
					settingsView.setSameSubDatasetSizeAmongProportions(true);
				}
				else {
					settingsView.setSameSubDatasetSizeAmongProportions(false);
				}
			}
		});
		sameSubDatasetSizeAmongProportionsCheckBox.setBounds(256, 18, 262, 24);
		difficultyResamplingPanel.add(sameSubDatasetSizeAmongProportionsCheckBox);

		JPanel datasetCharacteristicsPanel = new JPanel();
		datasetCharacteristicsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		datasetCharacteristicsPanel.setBounds(6, 413, 528, 54);
		frmBugPredictionNoise.getContentPane().add(datasetCharacteristicsPanel);
		datasetCharacteristicsPanel.setLayout(null);

		JLabel lblDatasetCharacteristics = new JLabel("Dataset Characteristics");
		lblDatasetCharacteristics.setBounds(195, 0, 147, 16);
		datasetCharacteristicsPanel.add(lblDatasetCharacteristics);

		JLabel lblBuggyLabel = new JLabel("Buggy Label");
		lblBuggyLabel.setBounds(12, 26, 75, 16);
		datasetCharacteristicsPanel.add(lblBuggyLabel);

		buggyLabelTextField = new JTextField();
		buggyLabelTextField.setText(settingsView.getBuggyLabel());
		buggyLabelTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				try {
					settingsView.setBuggyLabel(buggyLabelTextField.getText());
				} catch (InvalidInputException e1) {
					visualizeInputErrorDialog(e1.getMessage());
					buggyLabelTextField.setText(settingsView.getBuggyLabel());
				}
			}
		});
		buggyLabelTextField.setColumns(10);
		buggyLabelTextField.setBounds(83, 24, 75, 20);
		datasetCharacteristicsPanel.add(buggyLabelTextField);

		JLabel lblNonbuggyLabel = new JLabel("Non-Buggy Label");
		lblNonbuggyLabel.setBounds(176, 26, 95, 16);
		datasetCharacteristicsPanel.add(lblNonbuggyLabel);

		nonbuggyLabelTextField = new JTextField();
		nonbuggyLabelTextField.setText(settingsView.getNonbuggyLabel());
		nonbuggyLabelTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					settingsView.setNonbuggyLabel(nonbuggyLabelTextField.getText());
				} catch (InvalidInputException e1) {
					visualizeInputErrorDialog(e1.getMessage());
					nonbuggyLabelTextField.setText(settingsView.getNonbuggyLabel());
				}
			}
		});
		nonbuggyLabelTextField.setColumns(10);
		nonbuggyLabelTextField.setBounds(275, 24, 75, 20);
		datasetCharacteristicsPanel.add(nonbuggyLabelTextField);

		JLabel lblClassificationChoice = new JLabel("Classification Choice");
		lblClassificationChoice.setBounds(363, 26, 126, 16);
		datasetCharacteristicsPanel.add(lblClassificationChoice);

		classificationChoiceTextField = new JTextField();
		classificationChoiceTextField.setText(new Integer(settingsView.getClassificationChoice()).toString());
		classificationChoiceTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					Integer inputValue = new Integer(classificationChoiceTextField.getText());
					settingsView.setClassificationChoice(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("Classification Choice must be an integer number.");
					classificationChoiceTextField.setText(new Integer(settingsView.getClassificationChoice()).toString());
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					classificationChoiceTextField.setText(new Integer(settingsView.getClassificationChoice()).toString());
				}
			}
		});
		classificationChoiceTextField.setColumns(10);
		classificationChoiceTextField.setBounds(486, 22, 30, 20);
		datasetCharacteristicsPanel.add(classificationChoiceTextField);

		JPanel classifierChoicePanel = new JPanel();
		classifierChoicePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		classifierChoicePanel.setBounds(6, 469, 206, 46);
		frmBugPredictionNoise.getContentPane().add(classifierChoicePanel);
		classifierChoicePanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Classifiers Choice");
		lblNewLabel.setBounds(46, 0, 125, 16);
		classifierChoicePanel.add(lblNewLabel);


		JButton btnChoseClassifiers = new JButton("Choose Classifiers");
		btnChoseClassifiers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				classifierSelectionDialog = new ClassifierSelectionDialog(settingsView);
			}

		});
		btnChoseClassifiers.setBounds(26, 23, 145, 16);
		classifierChoicePanel.add(btnChoseClassifiers);

		JPanel nFoldCrossValidationPanel = new JPanel();
		nFoldCrossValidationPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		nFoldCrossValidationPanel.setBounds(213, 469, 321, 46);
		frmBugPredictionNoise.getContentPane().add(nFoldCrossValidationPanel);
		nFoldCrossValidationPanel.setLayout(null);

		JLabel lblNfoldCrossvalidationSetting = new JLabel("n-fold cross-validation setting");
		lblNfoldCrossvalidationSetting.setBounds(79, 0, 219, 16);
		nFoldCrossValidationPanel.add(lblNfoldCrossvalidationSetting);

		//n-fold cross-validation		
		nFoldTextField = new JTextField();
		nFoldTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				try {
					Integer inputValue = new Integer(nFoldTextField.getText());
					settingsView.setnFold(inputValue);
				}
				catch (NumberFormatException e1){
					visualizeInputErrorDialog("The number of folds for the n-folds cross-validation must be an integer number greater than 0.");
					nFoldTextField.setText(new String(new Integer(settingsView.getnFold()).toString()));
				}
				catch (InvalidInputException e2){
					visualizeInputErrorDialog(e2.getMessage());
					nFoldTextField.setText(new String(new Integer(settingsView.getnFold()).toString()));
				}
			}
		});
		nFoldTextField.setText(new String(new Integer(settingsView.getnFold()).toString()));

		nFoldTextField.setColumns(10);
		nFoldTextField.setBounds(124, 21, 40, 20);
		nFoldCrossValidationPanel.add(nFoldTextField);

		JLabel lblNumberNOf = new JLabel("number n of folds");
		lblNumberNOf.setBounds(12, 23, 116, 14);
		nFoldCrossValidationPanel.add(lblNumberNOf);

	}


	/**
	 * Visualize input error message
	 * (experiment launch is inhibited until error is corrected).
	 * @param message
	 */
	private void visualizeInputErrorDialog(String message){

		okToRun=false;
		JOptionPane.showMessageDialog(frmBugPredictionNoise, message,
				"Input Error",
				JOptionPane.ERROR_MESSAGE);
		okToRun=true;
	}

	/**
	 *  Starts the controller class that launches the experiments.
	 *  Visualize message at the end of the simulations.
	 *  
	 */
	private void startExperiments(){
		new MainExperimentCtrl(settingsView);
		JOptionPane.showMessageDialog(frmBugPredictionNoise,
				"All experiments are completed. Check out the results in your output directory.",
				"Experiments Completed",
				JOptionPane.PLAIN_MESSAGE);
	}
}
