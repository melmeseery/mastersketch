/**
 * 
 */
package SketchMaster.classifier;

import org.apache.log4j.Logger;

import java.io.Serializable;

import SketchMaster.classifier.rubine.RubineCategory;

/**
 * @author maha
 * 
 */
public class Category implements Comparable<Category>, Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Category.class);

	protected String CategoryName = "";

	/**
	 * 
	 */
	public Category() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return this.CategoryName + "  \n";
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return CategoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public int compareTo(Category b) throws ClassCastException {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - start"); //$NON-NLS-1$
//		}

		if (!(b instanceof Category))
			throw new ClassCastException("Rubine category expected");
		Category temp = ((Category) b);

		int returnint = this.CategoryName.compareTo(temp.CategoryName);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - end"); //$NON-NLS-1$
//		}
		return returnint;
	}

}
