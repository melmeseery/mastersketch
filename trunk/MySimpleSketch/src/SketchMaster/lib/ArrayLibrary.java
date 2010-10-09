/**
 * 
 */
package SketchMaster.lib;

import java.util.ArrayList;

/**
 * @author maha
 *         <h2></h2>
 */
public class ArrayLibrary {
	static public void addSorted(ArrayList list, int index) {
		int temp;
		for (int i = 0; i < list.size(); i++) {
			temp = (Integer) list.get(i);

			if (temp > index) {
				list.add(i + 1, index);
				return;
			}
		}
		// upp till now it is the largest
		// add it now
		list.add(index);
	}

	static public boolean indexExist(ArrayList list, int index) {
		int temp;
		for (int i = 0; i < list.size(); i++) {
			temp = (Integer) list.get(i);
			if (temp == index)
				return true;
			if (temp > index)
				return false;
		}

		return false;
	}

	static public ArrayList mergeSortedListNonRepeating(
			ArrayList<Comparable> list1, ArrayList<Comparable> list2) {
		// merge list2 into list 1

		// first check if list 1 exist
		if (list1 == null)
			return list2;
		else {
			int previous = 0;
			int compare = 0;
			for (int i = 0; i < list2.size(); i++) {
				for (int j = previous; j < list1.size(); j++) {
					compare = list1.get(j).compareTo(list2.get(i));

					if (compare >= 0) {
						if (compare == 0) {
							// now if they are equal skip this
							// lopp and set previous with next j
							previous = j + 1;
							break;
						}
						// theitem in list2 is larger than the one in list1
						// and we have to add it to list 1 in the previous
						// location
						else {
							previous = j - 1;
							if (previous >= 0)
								list1.add(previous, list2.get(i));
							else {
								list1.add(0, list2.get(i));
								previous = 0;

							}
							break;

						}
					}

				}
			}
			// now loop arround list
			return list1;

		}

	}

}
