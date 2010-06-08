package SketchMaster.gui.optionDialogs;

import org.apache.log4j.Logger;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Rectangle;

public class CategoryAdd extends JDialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CategoryAdd.class);

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTextField jTextCategoryName = null;

	private JLabel jLabel = null;

	private JButton jButton = null;

	private JButton jButton1 = null;
	private String[] CurrentCategorys = null;
	private String NewCategory = null;

	/**
	 * @param owner
	 */
	public CategoryAdd(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 200);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setModal(true);

		this.setContentPane(getJContentPane());
		this.setVisible(false);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setText("Category Name ");
			jLabel.setBounds(new Rectangle(6, 42, 151, 23));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJTextCategoryName(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton1(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextCategoryName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextCategoryName() {
		if (jTextCategoryName == null) {
			jTextCategoryName = new JTextField();
			jTextCategoryName.setBounds(new Rectangle(163, 42, 192, 26));
			jTextCategoryName.setText("");// New Category Name
		}
		return jTextCategoryName;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(22, 147, 83, 24));
			jButton.setText("Ok");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// check if ther is a new
					if (getJTextCategoryName() != null) {
						if (checkCategoryText()) {
							NewCategory = getJTextCategoryName().getText();
						} else {
							NewCategory = null;
							return;
						}
						closeDialog();
					}

					// logger.info("actionPerformed()");

				}

			});
		}
		return jButton;
	}

	private boolean checkCategoryText() {

		String temp = getJTextCategoryName().getText();
		if (temp.equals("")) {
			JOptionPane
					.showMessageDialog(this, " please write a category name");
			return false;
		}
		// now try to remove all sapaces and try again
		String temp2 = temp.replaceAll(" ", "");
		if (temp2.equals("")) {
			JOptionPane
					.showMessageDialog(this, " please write a category name");
			return false;
		}
		if (CurrentCategorys != null) {
			for (int i = 0; i < CurrentCategorys.length; i++) {
				if (CurrentCategorys[i].equalsIgnoreCase(temp)) {
					JOptionPane.showMessageDialog(this,
							" Sorry Cateogry already exist");
					return false;
				}
			}
			return true;

		}

		return true;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(202, 144, 125, 24));
			jButton1.setText("Cancel");
			jButton1.setName("CancelButtton");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (logger.isDebugEnabled()) {
						//  logger.debug("$java.awt.event.ActionListener.actionPerformed(java.awt.event.ActionEvent) -  cancel button line 166 in category add.  "); //$NON-NLS-1$
					}

					NewCategory = null;
					closeDialog();

				}
			});
		}
		return jButton1;
	}

	private void closeDialog() {
		this.setVisible(false);
	}

	/**
	 * @param currentCategorys
	 *            the currentCategorys to set
	 */
	public void setCurrentCategorys(String[] currentCategorys) {
		CurrentCategorys = currentCategorys;
	}

	/**
	 * @return the newCategory
	 */
	public String getNewCategory() {
		return NewCategory;
	}
}
