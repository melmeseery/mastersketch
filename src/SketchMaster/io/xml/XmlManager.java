package SketchMaster.io.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import SketchMaster.Stroke.StrokeData.Stroke;

public class XmlManager {
///  this control the xml manngment of the database. 
	 XmlParser xml=null;
		/**
		 * 
		 */
		public XmlManager() {
			
		}
	public  void readXML(String filename){
		if (xml==null)
		xml=new XmlParser();
		xml.ReadXmlFile(filename);
		//xml.iterateRootChildren();
	}
	public void closeFile(){
		xml.closeParser();
	}

	
    public void SaveXML(String filename){
    	if (xml==null)
    		xml=new XmlParser();
    		xml.WriteXmlFile(filename);
    }
    

     
    
	public ArrayList<String> getCategoriesList() {
		
		if (xml==null)
		return null;
		else {
			return (ArrayList<String>) xml.getTypes();
		//	return new ArrayList<String>();
		}
		
	}
	public ArrayList<ArrayList<Stroke>> getExampleForCat(String string) {
//		 ArrayList<ArrayList<Stroke>> tem = getExampleForCatPosNeg(string,-1);
//		  tem.addAll( getExampleForCatPosNeg(string,1) );
		return   getExampleForCatPosNeg(string,1);
	}
	
	public ArrayList<ArrayList<Stroke>> getExampleForCatPosNeg(String string,int posNeg) {
		if (xml==null)
		        return new ArrayList<ArrayList<Stroke>>();
		
		
		String pos="";
		if (posNeg<0)
			pos="-";
		else 
			
			pos="+";
		
		List samples=xml.getElements(string);
		
		ArrayList<ArrayList<Stroke>> result=new ArrayList<ArrayList<Stroke>>();
		for (Iterator iterator = samples.iterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			ArrayList<Stroke> temp = xml.getExampleFromElements(e,pos);
			if (temp!=null)
			   result.add(temp);
			
		}
		
		
		return result;
		
	}
	
	public void openParser() {
		  
		if (xml==null)
    		xml=new XmlParser();
		
		xml.InitNewDoc();
		
	}
	public void addType(String type, ArrayList<ArrayList<Stroke>> arrayList, ArrayList<Integer> i) {
		xml.addType(type, arrayList, i);
		
	}
	public void writeFile(String filename) {
		xml.WriteXmlFile(filename);
		
	}
	public ArrayList<ArrayList<Stroke>> getPosExampleForCat(String string) {
	
	
	//return result; 
		return getExampleForCatPosNeg(string,1);
	}
	public ArrayList<ArrayList<Stroke>> getNegExampleForCat(String string) {
		 
		return getExampleForCatPosNeg(string,-1);
	}
}
