/**
 * 
 */
package SketchMaster.Stroke.StrokeData;
import java.util.ArrayList;

/**
 * @author maha
 * 
 */
public class DominatePointStructure {
	PointData point = null;

	int indexInInk = 0;

	ArrayList<Integer> InFunctions = null;

	int FunctionCount = 0;

	double Certainty = 0.0;

	public void addFunction(int f) {
		if (InFunctions == null)
			InFunctions = new ArrayList<Integer>();

		InFunctions.add(Integer.valueOf(f));
		FunctionCount++;
	}

	public double getCertainty() {
		return Certainty;
	}

	public void setCertainty(double certainty) {
		Certainty = certainty;
	}

	public int getIndexInInk() {
		return indexInInk;
	}

	public void setIndexInInk(int indexInInk) {
		this.indexInInk = indexInInk;
	}

	public PointData getPoint() {
		return point;
	}

	public void setPoint(PointData point) {
		this.point = point;
	}

	public int getFunctionCount() {
		return FunctionCount;
	}

	public ArrayList<Integer> getInFunctions() {
		return InFunctions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(Certainty);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + FunctionCount;
		result = prime * result
				+ ((InFunctions == null) ? 0 : InFunctions.hashCode());
		result = prime * result + indexInInk;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DominatePointStructure other = (DominatePointStructure) obj;
		if (Double.doubleToLongBits(Certainty) != Double
				.doubleToLongBits(other.Certainty))
			return false;
		if (FunctionCount != other.FunctionCount)
			return false;
		if (InFunctions == null) {
			if (other.InFunctions != null)
				return false;
		} else if (!InFunctions.equals(other.InFunctions))
			return false;
		if (indexInInk != other.indexInInk)
			return false;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	String string="  point  index is "+this.indexInInk+"   with certinaty "+this.Certainty+"    ";
		return string;
	}

}
