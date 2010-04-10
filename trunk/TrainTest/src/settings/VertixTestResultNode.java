package settings;

public class VertixTestResultNode {
	
	int vertixCount=0;
	int StrokePointsCount=0;
	double Error;
//	String categoryName;
	
	int CountOfSegments=0;//number of  segments types produced 

	/**
	 * @return the vertixCount
	 */
	public int getVertixCount() {
		return vertixCount;
	}

	/**
	 * @param vertixCount the vertixCount to set
	 */
	public void setVertixCount(int vertixCount) {
		this.vertixCount = vertixCount;
	}

	/**
	 * @return the strokePointsCount
	 */
	public int getStrokePointsCount() {
		return StrokePointsCount;
	}

	/**
	 * @param strokePointsCount the strokePointsCount to set
	 */
	public void setStrokePointsCount(int strokePointsCount) {
		StrokePointsCount = strokePointsCount;
	}

	/**
	 * @return the error
	 */
	public double getError() {
		return Error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(double error) {
		Error = error;
	}

	/**
	 * @return the countOfSegments
	 */
	public int getCountOfSegments() {
		return CountOfSegments;
	}

	/**
	 * @param countOfSegments the countOfSegments to set
	 */
	public void setCountOfSegments(int countOfSegments) {
		CountOfSegments = countOfSegments;
	}
	
}
