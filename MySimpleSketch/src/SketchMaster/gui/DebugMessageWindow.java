/**
 * 
 */
package SketchMaster.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import SketchMaster.io.log.FileLog;
import java.awt.Dimension;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.Point;
import java.awt.ComponentOrientation;

/**
 * 
 * @author maha
 * 
 */
public class DebugMessageWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private Timer timer = null; // @jve:decl-index=0:visual-constraint="224,367"

	private JScrollPane jScrollPane = null;

	private JTextArea jTextArea1 = null;

	private JScrollPane jScrollPaneDrawing = null;

	private DrawingSheet drawingSheet = null;

	/**
	 * This is the default constructor
	 */
	public DebugMessageWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(706, 305);
		this.setContentPane(getJContentPane());
		this.setName("Log Messages");
		this.setTitle("JFrame");
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent e) {
				Dimension d = getContentPane().getSize();
				getJScrollPane().setSize(d.width - 50, d.height - 50);
				// getJTextArea().setSize(getContentPane().getSize().width-20,getContentPane().getSize().height-20);
				// System.out.println("componentResized()");
			}
		});
		getTimer();
		// timer.setDelay(10000);
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
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getJScrollPaneDrawing(), null);  // Generated
		}
		return jContentPane;
	}

	public void addText(String text) {

		getJTextArea1().setText(getJTextArea1().getText() + text);

	}

	public void setString(String text) {
		getJTextArea1().setText(text);
	}

	/**
	 * This method initializes timer
	 * 
	 * @return javax.swing.Timer
	 */
	private Timer getTimer() {
		if (timer == null) {
			timer = new Timer(500, new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {

					getJTextArea1().setText("");
					getJTextArea1().setText(FileLog.getLogString());
					// etJTextArea().setText(FileLog.getLogString());
					// System.out.println(FileLog.getLogString());
					getJTextArea1().setCaretPosition(
							getJTextArea1().getDocument().getLength());
				}

			});
			timer.setDelay(500);
		}
		return timer;
	}

	public void EnableTimer(boolean b) {

		if (b)
			timer.start();
		else
			timer.stop();
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(8, 15, 367, 244));
			jScrollPane.setViewportView(getJTextArea1());

		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextArea1
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea1() {
		if (jTextArea1 == null) {
			jTextArea1 = new JTextArea();
			jTextArea1
					.setText(" This is the only viewed on the debug mode of operation of the system. ");
			jTextArea1.addFocusListener(new java.awt.event.FocusListener() {
				public void focusGained(java.awt.event.FocusEvent e) {

					EnableTimer(false);
				}

				public void focusLost(java.awt.event.FocusEvent e) {
					EnableTimer(true);
				}
			});

		}
		return jTextArea1;
	}

	/**
	 * This method initializes jScrollPaneDrawing	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneDrawing() {
		if (jScrollPaneDrawing == null) {
			jScrollPaneDrawing = new JScrollPane();
			jScrollPaneDrawing.setBounds(new Rectangle(387, 15, 302, 241));  // Generated
			jScrollPaneDrawing.setViewportView(getDrawingSheet());  // Generated
		}
		return jScrollPaneDrawing;
	}

	/**
	 * This method initializes drawingSheet	
	 * 	
	 * @return SketchMaster.gui.DrawingSheet	
	 */
	public DrawingSheet getDrawingSheet() {
		if (drawingSheet == null) {
			drawingSheet = new DrawingSheet();
		}
		return drawingSheet;
	}

} // @jve:decl-index=0:visual-constraint="10,3"
