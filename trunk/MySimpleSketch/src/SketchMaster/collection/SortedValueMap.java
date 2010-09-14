//// See bottom of source code for software license.

package SketchMaster.collection;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * A Map with any kind of uniquely named Keys with numerical Values, sorted by
 * the values rather than by the keys. You can set whether the Map is sorted
 * each value has a threshold max value or not, and whether the sum of the
 * values has a threshold max value or not.
 * 
 * <P>
 * This software is distributed under the <A
 * HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt"> Berkeley Software
 * License</A>.
 * 
 * <PRE>
 * Revisions:  - GUIRLib-v1.0-1.0.0, Jun 16 1997, JH
 *               Created class
 *             - GUIRLib-v1.2-1.0.0, Jun 22 2000, JH
 *               Touched for GUIRLib release
 *             - GUIRLib-v1.3-1.0.0, Aug 11 2000, JH
 *               Touched for GUIRLib release
 *             - GUIRLib-v1.4-1.0.0, Aug 31 2000, JH
 *               Touched for GUIRLib release
 * </PRE>
 * 
 * @author (<A HREF="mailto:jasonh@cs.berkeley.edu">jasonh@cs.berkeley.edu</A>)
 * @since JDK 1.2
 * @version GUIRLib-v1.4-1.0.0, Aug 31 2000
 */
public class SortedValueMap {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SortedValueMap.class);

	// ===========================================================================
	// === DEFAULTS AND CUSTOMIZATION ========================================

	/**
	 * Sort in ascending order?
	 */
	private boolean flagAscending = true;

	// -----------------------------------------------------------------

	/**
	 * Ascending or descending?
	 */
	private static boolean default_flagAscending = true;

	// -----------------------------------------------------------------

	/**
	 * Use this BEFORE you stick in items.
	 * 
	 * @param flag
	 *            is true if ascending order, false if descending.
	 */
	public void setAscending(boolean flag) {
		flagAscending = flag;
	} // of setAscending

	// -----------------------------------------------------------------

	/**
	 * Set the default value for ascending or descending order. This is the
	 * sorting regime that new SortedNumericalMaps will use.
	 * 
	 * @param flag
	 *            is true if new SortedNumericalMaps should sort in ascending
	 *            order, false if in descending order.
	 */
	public static void setDefaultAscending(boolean flag) {
		default_flagAscending = flag;
	} // of setDefaultAscending

	// === DEFAULTS AND CUSTOMIZATION ========================================
	// ===========================================================================

	// ===========================================================================
	// === NONLOCAL VARIABLES ================================================

	// // hashtable maps name->value
	Hashtable table;

	// // linkedlist maps value->name, value being sorted asc or descending
	// // soft-state, call updateList() to make it consistent
	LinkedList list;

	// // true if list is inconsistent and needs to be updated, false otherwise.
	boolean flagDirty = false;

	// // the comparator used to sort the values in this map
	Comparator comp;

	// // the comparator that knows about our internal data structs,
	// // and uses comp to sort the values
	Comparator internalComp = new InternalComparator();

	// === NONLOCAL VARIABLES ================================================
	// ===========================================================================

	// ===========================================================================
	// === INNER CLASS =======================================================

	class InternalComparator implements Comparator {

		public int compare(Object key1, Object key2) {
			// // 1. Compare the values.
			// // Each of these are Map.Entry objects.
			Map.Entry m1 = (Map.Entry) key1;
			Map.Entry m2 = (Map.Entry) key2;
			int returnVal = comp.compare(m1.getValue(), m2.getValue());

			// // 2. Allow multiple instances, so arbitrarily break ties.
			if (returnVal == 0) {
				if (m1.getKey().hashCode() > m2.getKey().hashCode()) {
					returnVal = 1;
				} else {
					returnVal = -1;
				}
			}

			// // 3. Flip the values if we are in descending order.
			if (flagAscending == true) {
				return (returnVal);
			} else {
				return (-1 * returnVal);
			}
		} // of compare

	} // of inner class InternalComparator

	// === INNER CLASS =======================================================
	// ===========================================================================

	// ===========================================================================
	// === CONSTRUCTORS ======================================================

	public SortedValueMap(Comparator newComp) {
		// // 1. Create the data struct to hold the values.
		table = new Hashtable();
		comp = newComp;
	} // of constructor

	// === CONSTRUCTORS ======================================================
	// ===========================================================================

	// ===========================================================================
	// === CONSISTENCY METHODS ===============================================

	/**
	 * Make the sorted list consistent.
	 */
	private void updateList() {
		// // 1. If we are inconsistent with the table...
		if (flagDirty == true) {
			// // 1.1. Create a new list and then sort it.
			list = new LinkedList(table.entrySet());
			Collections.sort(list, internalComp);
			flagDirty = false;
		}
	} // of method

	// === CONSISTENCY METHODS ===============================================
	// ===========================================================================

	// ===========================================================================
	// === MAP METHODS =======================================================

	public void clear() {
		flagDirty = true;
		table.clear();
	} // of clear

	// -----------------------------------------------------------------

	public boolean containsKey(Object key) {
		return (table.containsKey(key));
	} // of containsKey

	// -----------------------------------------------------------------

	public boolean containsValue(Object value) {
		return (table.containsValue(value));
	} // of containsValue

	// -----------------------------------------------------------------

	public Set entrySet() {
		return (table.entrySet());
	} // of entrySet

	// -----------------------------------------------------------------

	public boolean equals(Object obj) {
		if (obj==null) return false;
		return (obj.hashCode() == this.hashCode());
	} // of equals

	// -----------------------------------------------------------------

	public Object get(Object obj) {
		return (table.get(obj));
	} // of get

	// -----------------------------------------------------------------

	public boolean isEmpty() {
		return (table.isEmpty());
	} // of isEmpty

	// -----------------------------------------------------------------

	public Set keySet() {
		return (table.keySet());
	} // of keySet

	// -----------------------------------------------------------------

	public Object put(Object key, Object value) {
		flagDirty = true;
		return (table.put(key, value));
	} // of put

	// -----------------------------------------------------------------

	public void putAll(Map newMap) {
		Iterator it = newMap.keySet().iterator();
		Object key;

		while (it.hasNext()) {
			key = it.next();
			this.put(key, newMap.get(key));
		}
	} // of putAll

	// -----------------------------------------------------------------

	public Object remove(Object key) {
		flagDirty = true;
		return (table.remove(key));
	} // of remove

	// -----------------------------------------------------------------

	public int size() {
		return (table.size());
	} // of size

	// -----------------------------------------------------------------

	public Collection values() {
		return (table.values());
	} // of values

	// === MAP METHODS =======================================================
	// ===========================================================================

	// ===========================================================================
	// === OTHER MAP METHODS =================================================

	/**
	 * Get the key at the beginning of the map.
	 * 
	 * @return whatever key is at the beginning. Will have a value that is the
	 *         largest or smallest value, depending on how the map is sorted.
	 */

	public Object getFirstKey() {
		updateList();
		if (list != null) {// i hadded this line to solve the problem of non
							// added points
			// i mean point list that didnnot have any entry to get size.
			if (list.size() > 0) {
				Map.Entry m = (Map.Entry) list.getFirst();
				return (m.getKey());
			}
		}
		return (null);
	} // of getFirstKey

	// -----------------------------------------------------------------

	/**
	 * Get the key at the end of the map.
	 * 
	 * @return whatever key is at the end. Will have a value that is the largest
	 *         or smallest value, depending on how the map is sorted.
	 */
	public Object getLastKey() {
		updateList();
		if (list.size() > 0) {
			Map.Entry m = (Map.Entry) list.getLast();
			return (m.getKey());
		}
		return (null);
	} // of getLastKey

	// -----------------------------------------------------------------

	/**
	 * Get the value at the beginning of the map.
	 * 
	 * @return whatever value is at the beginning. Either the largest or
	 *         smallest value, depending on how the map is sorted.
	 */
	public Object getFirstValue() {
		return (get(getFirstKey()));
	} // of getFirstValue

	// -----------------------------------------------------------------

	/**
	 * Get the value at the end of the map.
	 * 
	 * @return whatever value is at the end. Either the largest or smallest
	 *         value, depending on how the map is sorted.
	 */
	public Object getLastValue() {
		return (get(getLastKey()));
	} // of getLastValue

	// -----------------------------------------------------------------

	/**
	 * Get the key with the largest value.
	 * 
	 * @return whatever key has the largest value.
	 */
	public Object getLargestKey() {
		if (flagAscending == true) {
			return (getLastKey());
		} else {
			return (getFirstKey());
		}
	} // of getLargestKey

	// -----------------------------------------------------------------

	/**
	 * Get the key with the smallest value.
	 * 
	 * @return whatever key has the smallest value.
	 */
	public Object getSmallestKey() {
		if (flagAscending == true) {
			return (getFirstKey());
		} else {
			return (getLastKey());
		}
	} // of getSmallestKey

	// -----------------------------------------------------------------

	/**
	 * Get the largest value stored.
	 * 
	 * @return whatever the largest value is.
	 */
	public Object getLargestValue() {
		if (flagAscending == true) {
			return (getLastValue());
		} else {
			return (getFirstValue());
		}
	} // of getLargestValue

	// -----------------------------------------------------------------

	/**
	 * Get the smallest value stored.
	 * 
	 * @return whatever the smallest value is.
	 */
	public Object getSmallestValue() {
		if (flagAscending == true) {
			return (getFirstValue());
		} else {
			return (getLastValue());
		}
	} // of getSmallestValue

	// === OTHER MAP METHODS =================================================
	// ===========================================================================

	// ===========================================================================
	// === TOSTRING ==========================================================

	
	
	public String toString() {
		updateList();
		return (list.toString());
	} // of toString

	// === TOSTRING ==========================================================
	// ===========================================================================

	// ===========================================================================
	// === SELF-TESTING MAIN =================================================

	public LinkedList getSortedList(){
		updateList();
		if (list != null) {// i hadded this line to solve the problem of non
							// added points
			// i mean point list that didnnot have any entry to get size.
			if (list.size() > 0) {
				
				return list;
			}
		}
		//if (list==null)
			return new LinkedList();
		//return (null);
	}
	
	public static void main(String[] argv) {
		SortedValueMap c = new SortedValueMap(new NumericalComparator());

		c.setAscending(false);

		c.put("lemur", new Float(0.11f));
		c.put("wallaby", new Float(0.11f));
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c); //$NON-NLS-1$
		}

		c.put("kiwi", new Float(0.11f));
		c.put("mandrill", new Float(0.11f));
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c); //$NON-NLS-1$
		}

		c.put("emu", new Float(0.11f));
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c); //$NON-NLS-1$
		}

		c.put("exceedsum", new Float(0.55f));
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c); //$NON-NLS-1$
		}

		c.put("exceedval", new Float(1.05f));
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getFirstKey()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getFirstValue()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getLastKey()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getLastValue()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getSmallestKey()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getSmallestValue()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getLargestKey()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.getLargestValue()); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[])"); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - " + c.get("exceedsum")); //$NON-NLS-1$ //$NON-NLS-2$
		}

	} // of main

	// === SELF-TESTING MAIN =================================================
	// ===========================================================================

} // of class

// ==============================================================================

/*
 * Copyright (c) 2000 Regents of the University of California. All rights
 * reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. All advertising materials mentioning features or use of this software must
 * display the following acknowledgement:
 * 
 * This product includes software developed by the Group for User Interface
 * Research at the University of California at Berkeley.
 * 
 * 4. The name of the University may not be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

