package test;
import java.util.*;

import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.io.xml.XmlManager;
import SketchMaster.io.xml.XmlParser;
import org.dom4j.Element;
/**
 * 
 */

/**
 * @author Maha
 *
 */
public class HandleDataSetEvents extends  XmlManager {
	HashMap<String, ArrayList<ArrayList<Stroke>> > catData;
public void initData(){
	catData=new HashMap<String, ArrayList<ArrayList<Stroke>> >();
}
	public void SaveXML(String filename) {
		 ///save all the data into a new xml 
	    	
	    	if (catData!=null){
	    	 
	    		this.openParser();
		
				for (Iterator iterator =  catData.keySet().iterator(); iterator.hasNext();) {
					String cat = (String) iterator.next();
					ArrayList<ArrayList<Stroke>> data = catData.get(cat);
					ArrayList<Integer> labels=new ArrayList<Integer>();
					int count=data.size();
		
//					double diff=Math.abs(avgCountPerCat-count);
//					System.out.println(" count of this cat "+cat+" = "+count+" diff = "+diff);
//					if (diff<maxDifferentSymbolCount){
						for (int i = 0; i < data.size(); i++) {
							labels.add(new Integer(1));
						}
						
					addType(cat,data, labels);
						ArrayList<ArrayList<Stroke>> newData=new ArrayList<ArrayList<Stroke>>();
						catData.put(cat, newData);
//						  catCounts.put(cat, new Integer (0));
//						}

			
				}
				writeFile(filename);
				//dbwrite.writeFile("ReOrganizedDataDB_"+DataBase+"No"+SaveCount+".xml");
				closeFile();
				
				}
	    	

	    
			
		}
	
	   public void	AddDataToCat(String cat, ArrayList<ArrayList<Stroke>>  list){
	    	if ( catData!=null){
	    		  if (	  catData.containsKey(cat))
	    		  {
	    			  ArrayList<ArrayList<Stroke>> temp=new   ArrayList<ArrayList<Stroke>> ();
	    			   temp.addAll(catData.get(cat));
	    			   temp.addAll(list); 
	    			  catData.put(cat, temp);
	    		  }
	    		  else{
	    			  ArrayList<ArrayList<Stroke>>  temp=new  ArrayList<ArrayList<Stroke>> ();
	   		//	   temp.addAll(catData.get(cat));
	   			   temp.addAll(list); 
	    			  
	    			  catData.put(cat, temp);
	    			  
	    		  }
	    		
	    	}
	    }
	   public void	AddListToCat(String cat, ArrayList<Stroke>  list){
	    	if ( catData!=null){
	    		  if (	  catData.containsKey(cat))
	    		  {
	    			  //ArrayList<ArrayList<Stroke>> temp=new   ArrayList<ArrayList<Stroke>> ();
	    			  catData.get(cat).add(list);
	    			  //temp.addAll(catData.get(key)); 
	    			  //catData.put(cat, temp);
	    		  }
	    		  else{
	    			  ArrayList<ArrayList<Stroke>>  temp=new  ArrayList<ArrayList<Stroke>> ();
	   		//	   temp.addAll(catData.get(cat));
	   			   temp.add(list); 
	    			  
	    			  catData.put(cat, temp);
	    			  
	    		  }
	    		
	    	}
	    }
}
