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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import weka.classifiers.Classifier;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Toolkit;

/**
 * Dialog window for the selection of classifers for experiments
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 * 
 *
 */
public class ClassifierSelectionDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel checkPanel = new JPanel();
	private SettingsView settingsView;
	private Vector<JCheckBox> checkBoxes;
	private Vector<Classifier> newSelectedClassifiers = new Vector<Classifier>();

	/**
	 * Create the dialog.
	 */
	public ClassifierSelectionDialog(SettingsView settingsView) {
		setTitle("Classifiers Selection ");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClassifierSelectionDialog.class.getResource("/img/logotv.gif")));
		this.settingsView = settingsView;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 656, 471);
		getContentPane().setLayout(new BorderLayout());
		checkPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(checkPanel, BorderLayout.CENTER);
		checkPanel.setLayout(new GridLayout(0, 4));

		this.checkBoxes = this.generateCheckboxesForPotentialClassifiers(settingsView.getPotentialClassifiers());
		for (JCheckBox jCheckBox : checkBoxes) {
			checkPanel.add(jCheckBox);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						settingsView.setSelectedClassifiers(newSelectedClassifiers);
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		setVisible(true);
	}

	private Vector<JCheckBox> generateCheckboxesForPotentialClassifiers(Vector<Classifier>potentialClassifiers){

		Vector<JCheckBox> checkBoxes = new Vector<JCheckBox>();

		for (Classifier classifier : potentialClassifiers) {
			
			JCheckBox jCheckBox = new JCheckBox(classifier.getClass().getSimpleName());
			
			// if already selected, checks it and inserts it in the selected list
			if(settingsView.getSelectedClassifiers().contains(classifier)){
				this.newSelectedClassifiers.addElement(classifier);
				jCheckBox.setSelected(true);
			}
			
			jCheckBox.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					int index = 0;
					Object source = e.getItemSelectable();
					index = checkBoxes.indexOf((JCheckBox)source);
					Classifier changedClassifier = settingsView.getPotentialClassifiers().elementAt(index);

					System.out.println("toccato " + changedClassifier.getClass().getSimpleName());
					

					if (e.getStateChange() == ItemEvent.SELECTED) {
						
						System.out.println("selezionato " + changedClassifier.getClass().getSimpleName());
						newSelectedClassifiers.add(changedClassifier);
					}
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						System.out.println("deselezionato " + changedClassifier.getClass().getSimpleName());
						newSelectedClassifiers.remove(changedClassifier);
					}
				}
			});
			checkBoxes.add(jCheckBox);
		}
		return checkBoxes;
	} 
}
