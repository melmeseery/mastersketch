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
	 * 
	 */
	private static final long serialVersionUID = 5223827890094748107L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Category.class);

	protected String categoryName = "";

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

		return this.categoryName + "  \n";
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int compareTo(Category b) throws ClassCastException {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - start"); //$NON-NLS-1$
//		}

		if (!(b instanceof RubineCategory))
			throw new ClassCastException("Rubine category expected");
		Category temp = ((Category) b);

		int returnint = this.categoryName.compareTo(temp.categoryName);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - end"); //$NON-NLS-1$
//		}
		return returnint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryName == null) ? 0 : categoryName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		return true;
	}

}
