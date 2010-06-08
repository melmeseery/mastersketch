/**
 * 
 */
package SketchMaster.gui.Events;

import java.util.EventListener;

/**
 * @author Maha
 *
 */
public interface HandleFinishCluster extends EventListener {

	public abstract void HandleFinishedCluster(NewClusterEvent Evt);
}
