/**
 * 
 */
package SketchMaster.classifier;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import SketchMaster.collection.NumericalComparator;
import SketchMaster.collection.SortedValueMap;

/**
 * @author maha
 * 
 */
public class Classification extends SortedValueMap // TreeMap<Double,Category>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7505351837520558540L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.collection.SortedValueMap#toString()
	 */
	// @Override
	// public String toString() {
	// String s=" ";
	// //this.keySet()
	// for (Iterator iter = keySet().iterator(); iter.hasNext();) {
	// Category element = (Category) iter.next();
	// s+= element.toString()+ " /n ";
	// }
	// return s;
	//		
	//	
	// }

	/**
	 * 
	 */
	public Classification() {
		super(new NumericalComparator());

	}

}
