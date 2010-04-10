/**
 * 
 */
package settings;

import java.util.ArrayList;

/**
 * @author Maha
 *
 */
public class DataStat {
 	double Average;
	double Max;
	int Maxind;
	int Minind;
	double Min;
	double Sum;
	double Std;
	double variance ;
	
	ArrayList<Double> data;
	String name;
     public void AddData( double d){
    	 
    	 if (data==null)
    		 data=new ArrayList<Double>();
    	 
    	 
    	 data.add(new Double(d));
     }
     
	public void ComputeState(){
		if ( data==null)
		return ;
		if (data.size()>0){
	 Sum=0;
	 Max=0;
	 Min=Double.MAX_VALUE;
	 Std=0;
	 Average=0;
	 
  //  	int cune=0;
    	
    	for (int i = 0; i <  data.size(); i++) {
			
    		Sum+=data.get(i);
    		
    		if (data.get(i)>Max)
    		{
    			Max=data.get(i);
    			Maxind=i;
    		}
    		
    		if (data.get(i)<Min)
    		{
    			Min=data.get(i);
    			Minind=i;
    		}
    		
		}
    	
    	Average=(double)Sum/(double)data.size();
    	///now compute the standard diviation 
    	 ComputeStd();
  
		}
		
		
	}

	
	private void ComputeStd(){
		if ( data==null)
			return ;
			if (data.size()>0){
		
		double diff=0;
	double 	StdTemp=0.0;
	for (int i = 0; i <  data.size(); i++) {
    		
    		diff=data.get(i)-Average;
    		
    		StdTemp+=diff*diff;
    		
    	}
	
       double temp=	StdTemp/(double)data.size();	
       variance =temp;
       Std=Math.sqrt(temp);
			}
		
	}
	/**
	 * @return the data
	 */
	public ArrayList<Double> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<Double> data) {
		this.data = data;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the average
	 */
	public double getAverage() {
		return Average;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return Max;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return Min;
	}

	/**
	 * @return the sum
	 */
	public double getSum() {
		return Sum;
	}

	/**
	 * @return the std
	 */
	public double getStd() {
		return Std;
	}
	
	
}
