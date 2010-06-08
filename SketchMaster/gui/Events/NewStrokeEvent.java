package SketchMaster.gui.Events;

import SketchMaster.Stroke.StrokeData.Stroke;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * @author Mahi
 */
public class NewStrokeEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	Stroke EventStroke;
	/**
	 * @uml.property name="flagConsumed"
	 */
	boolean flagConsumed = false;
	/**
	 * @uml.property name="evt"
	 */
	MouseEvent evt;
	int evtX;
	int evtY;

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// constructors
	public NewStrokeEvent(Object source) {
		super(source);
	} // of constructor

	/**
	 * 
	 */
	public NewStrokeEvent(Object source, Stroke stroke) {
		this(source);
		this.EventStroke = stroke;

	} // of constructor

	// -----------------------------------------------------------------

	/**
	 * Create a new StrokeEvent object.
	 * 
	 * @param source
	 *            is the object creating this StrokeEvent.
	 * @param evt
	 *            is an associated AWT Event.
	 */
	public NewStrokeEvent(Object source, Stroke stroke, MouseEvent evt) {
		this(source, stroke);
		this.evt = evt;
		this.evtX = evt.getX();
		this.evtY = evt.getY();
	} // of constructor

	/**
	 * @return the eventStroke
	 * @uml.property name="eventStroke"
	 */
	public Stroke getEventStroke() {
		return EventStroke;
	}

	/**
	 * @param eventStroke
	 *            the eventStroke to set
	 * @uml.property name="eventStroke"
	 */
	public void setEventStroke(Stroke eventStroke) {
		EventStroke = eventStroke;
	}

	/**
	 * @return the evt
	 * @uml.property name="evt"
	 */
	public MouseEvent getEvt() {
		return evt;
	}

	/**
	 * @param evt
	 *            the evt to set
	 * @uml.property name="evt"
	 */
	public void setEvt(MouseEvent evt) {
		this.evt = evt;
	}

	/**
	 * @return the flagConsumed
	 * @uml.property name="flagConsumed"
	 */
	public boolean isFlagConsumed() {
		return flagConsumed;
	}

	/**
	 * @param flagConsumed
	 *            the flagConsumed to set
	 * @uml.property name="flagConsumed"
	 */
	public void setFlagConsumed(boolean flagConsumed) {
		this.flagConsumed = flagConsumed;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// Function s

}
