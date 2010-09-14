/**
 * 
 */
package SketchMaster.gui;

import java.awt.Graphics2D;
import java.util.Observable;

import org.apache.log4j.Logger;

import SketchMaster.io.log.FileLog;
import SketchMaster.swarm.Solution;

/**
 * @author maha
 * 
 */
public class GraphWatched extends Observable {
	private static final Logger logger = Logger.getLogger(GraphWatched.class);
	Graphics2D g;

	void counter(int period) {
		for (; period >= 0; period--) {
			setChanged();
			notifyObservers(Integer.valueOf(period));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//  logger.trace("Sleep interrupted");
			}
		}
	}

	public Graphics2D getG() {
		return g;
	}

	public void setG(Graphics2D g) {
		this.g = g;
		setChanged();
	}

}
