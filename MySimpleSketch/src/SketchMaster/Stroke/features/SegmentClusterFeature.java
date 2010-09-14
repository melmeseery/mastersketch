/**
 * 
 */
package SketchMaster.Stroke.features;
/**
 * @author maha
 * 
 */
public abstract class SegmentClusterFeature {
	static double ZERO_REPLACMENT=-1;
	protected double Value = 0.0;
	protected double[] Values;
	protected boolean ValueOk = false;

	protected boolean computed = false;

	protected double minValue = 0.0;

	protected double maxValue = Double.MAX_VALUE;

	protected String Name="";
	protected String[] Names=null;

	public double[] getValues() {
		if (!computed) {
			computed = true;
			double[] returndoubleArray = Values();
			for (int i = 0; i < returndoubleArray.length; i++) {
				if (returndoubleArray[i]==0||Double.isInfinite(returndoubleArray[i])||Double.isNaN(returndoubleArray[i]))
				{
					returndoubleArray[i]=ZERO_REPLACMENT;
				}
			}
			return returndoubleArray;
			
			//return returndoubleArray;
		} else {
			
			for (int i = 0; i < Values.length; i++) {
				if (Values[i]==0||Double.isInfinite(Values[i])||Double.isNaN(Values[i]))
				{
					Values[i]=ZERO_REPLACMENT;
				}
			}
			return Values;
		}
		// return this.MinValue();
	}

//	public double getValue() {
//		if (!computed) {
//			computed = true;
//			double returndouble = Value();
//			return returndouble;
//		} else {
//			if (Value==0||Double.isInfinite(Value)||Double.isNaN(Value))
//				
//				Value=ZERO_REPLACMENT;
//			return Value;
//		}
//		// return this.MinValue();
//	}

//	abstract public double Value();

	abstract public double[] Values();

	abstract void setData(Object data);

	public boolean isValueOk() {
		return ValueOk;
	}

	public String toString() {
	
			
		
		StringBuilder string =new StringBuilder();
		string.append( "  Function " );
		string.append(Name ); 
		string.append("  and Value = "); 
		string.append(Value);
		string.append( " with Status " );
		string.append(ValueOk );
		string.append( "  ");
		
		if (NoOfValues()>1){
			//string =  "  Function " + Name + "  and Value = "   ;
			string =new StringBuilder();
			string.append( "  Function " );
			string.append(Name ); 
			string.append("  and Values = "); 
			//string.append(Value);
			for (int i = 0; i < Values.length; i++) {
				string.append( Values[i]);
				string.append("    ");
				
			}
			string.append("  with Status ") ;;
			string.append( ValueOk);
			string.append( "  ");
			
		}

		return string.toString();
	}

	public double MinValue() {
		return minValue;
	}

	public double MaxValue() {
		return maxValue;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getName() {
		return Name;
	}

	public int NoOfValues() {
		return 1;
	}

	/**
	 * @return the names
	 */
	public String[] getNames() {
		return Names;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(String[] names) {
		Names = names;
	}

}
