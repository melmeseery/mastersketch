/**
 * 
 */
package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import SketchMaster.gui.*;
import java.awt.Dimension;

/**
 * @author Maha
 *
 */
public class SketchDisplay extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private DrawingSheet drawingSheet = null;

	/**
	 * This is the default constructor
	 */
	public SketchDisplay() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(567, 314);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent e) {
				//System.out.println("componentResized()"); // TODO Auto-generated Event stub componentResized()
				getDrawingSheet().setSize(getContentPane().getSize());
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDrawingSheet(), BorderLayout.CENTER);  // Generated
		}
		return jContentPane;
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

	public void clearView() {
//		getRecognizier().Clear();

		getDrawingSheet().repaint();

		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
