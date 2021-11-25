package decisionTree;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Form extends JFrame {

	private static final long serialVersionUID = 1L;
	private UtilClass util;
	private JComboBox cmbDataset;
	JCheckBox cbxRepeatContAttrs;
	private JTextField txtCorruption;
	private JTextField txtValidation;
	private JTextField txtRandomSeed;

	public Form(UtilClass util) {
		this.util = util;
		createMenu();
		createWidgets();
		pack();
		setSize(200, 250);
		setVisible(true);
		setTitle("Decision Tree");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class MenuListener implements ActionListener {
		private String command;

		public MenuListener(String command) {
			this.command = command;
		}

		public void actionPerformed(ActionEvent e) {
			if (this.command.equals("Quit")) {
				System.exit(0);
			} else if (this.command.equals("Read Files")) {
				util.readFiles();
			} else if (this.command.equals("Corrupt Data")) {
				util.corruptData();
			} else if (this.command.equals("Split to Train and Validation")) {
				util.split2TrainAndValidation();
			} else if (this.command.equals("Discretize Values")) {
				util.discretizeValues();
			} else if (this.command.equals("Learn (ID3) Tree")) {
				util.learnID3();
			} else if (this.command.equals("Test Tree")) {
				util.testTree();
			} else if (this.command.equals("Generate Rules from Tree")) {
				util.genRules();
			} else if (this.command.equals("Test Ruleset")) {
				util.testRuleset();
			} else if (this.command.equals("Prune Rules")) {
				util.pruneRules();
			} else if (this.command.equals("Test Pruned Rules")) {
				util.testPrunedRules();
			}

		}
	}

	private void createWidgets() {
		JPanel panel = new JPanel();
		add(panel);

		String[] cmbItems = { "iris", "tennis", "iris1" };
		cmbDataset = new JComboBox(cmbItems);
		panel.add(cmbDataset);

		JLabel lblValidation = new JLabel("Validation (percent):");
		panel.add(lblValidation);
		txtValidation = new JTextField("0", 10);
		panel.add(txtValidation);

		JLabel lblCorruption = new JLabel("Corruption (percent):");
		panel.add(lblCorruption);
		txtCorruption = new JTextField("0", 10);
		panel.add(txtCorruption);

		JLabel lblRandomSeed = new JLabel("Randomization Seed:");
		panel.add(lblRandomSeed);
		txtRandomSeed = new JTextField("1234", 10);
		panel.add(txtRandomSeed);
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");

		JMenuItem itemReadFile = new JMenuItem("Read Files");
		itemReadFile.addActionListener(new MenuListener("Read Files"));
		menuFile.add(itemReadFile);

		JMenuItem itemCorrupt = new JMenuItem("Corrupt Data");
		itemCorrupt.addActionListener(new MenuListener("Corrupt Data"));
		menuFile.add(itemCorrupt);

		JMenuItem itemSplit = new JMenuItem("Split to Train and Validation");
		itemSplit.addActionListener(new MenuListener(
				"Split to Train and Validation"));
		menuFile.add(itemSplit);

		JMenuItem itemDiscretize = new JMenuItem("Discretize Values");
		itemDiscretize.addActionListener(new MenuListener("Discretize Values"));
		menuFile.add(itemDiscretize);

		JMenuItem itemID3Learn = new JMenuItem("Learn (ID3) Tree");
		itemID3Learn.addActionListener(new MenuListener("Learn (ID3) Tree"));
		menuFile.add(itemID3Learn);

		JMenuItem itemTestTree = new JMenuItem("Test Tree");
		itemTestTree.addActionListener(new MenuListener("Test Tree"));
		menuFile.add(itemTestTree);

		JMenuItem itemGenRules = new JMenuItem("Generate Rules from Tree");
		itemGenRules.addActionListener(new MenuListener(
				"Generate Rules from Tree"));
		menuFile.add(itemGenRules);

		JMenuItem itemTestRuleset = new JMenuItem("Test Ruleset");
		itemTestRuleset.addActionListener(new MenuListener("Test Ruleset"));
		menuFile.add(itemTestRuleset);

		JMenuItem pruneItem = new JMenuItem("Prune Rules");
		pruneItem.addActionListener(new MenuListener("Prune Rules"));
		menuFile.add(pruneItem);

		JMenuItem itemTestPrunedRules = new JMenuItem("Test Pruned Rules");
		itemTestPrunedRules.addActionListener(new MenuListener(
				"Test Pruned Rules"));
		menuFile.add(itemTestPrunedRules);

		JMenuItem itemQuit = new JMenuItem("Quit");
		itemQuit.addActionListener(new MenuListener("Quit"));
		menuFile.add(itemQuit);

		menuBar.add(menuFile);
		setJMenuBar(menuBar);
	}

	public String getDatasetName() {
		return (String) cmbDataset.getSelectedItem();
	}

	public int getRandomSeed() {
		return Integer.parseInt(txtRandomSeed.getText());
	}

	public int getCorruptionPercent() {
		return Integer.parseInt(txtCorruption.getText());
	}

	public int getValidationPercent() {
		return Integer.parseInt(txtValidation.getText());
	}
}
