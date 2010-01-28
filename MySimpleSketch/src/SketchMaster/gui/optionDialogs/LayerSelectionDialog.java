package SketchMaster.gui.optionDialogs;

import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import java.awt.Color;

public class LayerSelectionDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JScrollPane jScrollPane = null;

	private JPanel jPanel = null;

	private JButton jButton = null;

	private JLabel jLabel = null;
	String[] Layers = null;
	boolean[] layersSelections = null;

	private int checkBoxCount = 0;

	/**
	 * @param owner
	 */
	public LayerSelectionDialog(Frame owner, String[] LayerNames) {
		super(owner);

		// Layers=LayerNames;
		// layersSelections=new boolean[Layers.length];
		// for (int i = 0; i < layersSelections.length; i++) {
		// layersSelections[i]=false;
		// }
		initialize();

		setModal(true);
		// setSize(250, 330);
		// setLocation(20, 100);

		setTitle("Choose layers to display");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// setDefaultClosesOperation(DO_NOTHING_ON_CLOSE);
		addAllJCheckBox(getJPanel(), LayerNames);
		this.Layers = LayerNames;
	}

	/**
	 * @param arg0
	 * @param layers
	 * @param layersSelections
	 * @throws HeadlessException
	 */
	public LayerSelectionDialog(Frame arg0, String[] layers,
			boolean[] layersSelections) throws HeadlessException {
		super(arg0);

		this.Layers = layers;
		this.layersSelections = layersSelections;
		initialize();

		setModal(true);
		// setSize(250, 330);
		// setLocation(20, 100);

		setTitle("Choose layers to display");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		// setDefaultClosesOperation(DO_NOTHING_ON_CLOSE);
		addAllJCheckBox(getJPanel(), Layers);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(422, 342);
		setLayout(null);
		setResizable(false);
		this.setContentPane(getJContentPane());

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			// jContentPane.add(getJCheckBox(), null);
			jContentPane.add(getJScrollPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private void addAllJCheckBox(JPanel panel, String[] LayerNames) {
		JCheckBox jLayerCheckBox;
		int x = 10, y = 20;
		// from the panel get the first
		if (panel.getComponents() != null) {
			for (int i = 0; i < panel.getComponents().length; i++) {
				if (panel.getComponents()[i] instanceof JLabel) {
					JLabel b = (JLabel) panel.getComponents()[i];
					x = b.getBounds().x + 10;
					y = b.getBounds().y + 20;
				}
			}

		}

		if (LayerNames != null) {
			for (int i = 0; i < LayerNames.length; i++) {
				jLayerCheckBox = new JCheckBox(LayerNames[i]);
				jLayerCheckBox.setBounds(x, y, 250, 31);
				y += 30;

				jLayerCheckBox.setName(LayerNames[i]);
				if (layersSelections != null) {
					jLayerCheckBox.setSelected(layersSelections[i]);
				} else {
					jLayerCheckBox.setSelected(false);
				}
				jLayerCheckBox.setVisible(true);
				panel.add(jLayerCheckBox);
				++checkBoxCount;

				// scroll.add(check);
				// scroll.add(check);
				// ypos+=25;

			}
			y += 30;
			if (panel.getComponents() != null) {
				for (int i = 0; i < panel.getComponents().length; i++) {
					if (panel.getComponents()[i] instanceof JButton) {
						JButton b = (JButton) panel.getComponents()[i];
						b.setLocation(b.getBounds().x, y);
						break;
					}
				}

			}
		}

		// if (jCheckBox == null) {
		// jCheckBox = new JCheckBox();
		// jCheckBox.setBounds(new Rectangle(0, 0, 300, 21));
		// }
		// return jCheckBox;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();

			jScrollPane.setBounds(new Rectangle(13, 7, 396, 322));
			jScrollPane
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			jScrollPane
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			jScrollPane.setViewportView(getJPanel());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(23, 12, 292, 21));
			jLabel.setText("Select the layer you would like to view");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			// jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJButton());
			jPanel.add(jLabel);
			// addAllJCheckBox(jPanel,Layers);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(146, 78, 174, 28));
			jButton.setText("Enter");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// System.out.println("actionPerformed()"); // TODO
					// Auto-generated Event stub actionPerformed()
					setCheckedLayers();
					closeDialog();

				}
			});
		}
		return jButton;
	}

	protected void setCheckedLayers() {

		JPanel panel = getJPanel();
		if (panel != null) {
			Component[] coms = panel.getComponents();
			if (coms != null) {
				int j = 0;
				if (layersSelections == null)
					layersSelections = new boolean[checkBoxCount];
				for (int i = 0; i < coms.length; i++) {
					if (coms[i] instanceof JCheckBox) {
						JCheckBox cki = (JCheckBox) coms[i];

						layersSelections[j] = cki.isSelected();
						// System.out.println(" layer "+j+ " is value "+
						// layersSelections[j] );
						j++;

					}
				}
			}
		}
	}

	private void closeDialog() {
		this.setVisible(false);
		// System.out.println("closing ");
	}

	public boolean[] getlayersSelections() {

		return layersSelections;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
