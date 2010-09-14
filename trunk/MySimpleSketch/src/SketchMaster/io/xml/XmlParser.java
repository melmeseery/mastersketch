/**
 * 
 */
package SketchMaster.io.xml;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.system.SystemSettings;


/**
 * @author Maha
 *
 */
public class XmlParser {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(XmlParser.class);

 private Document doc;
 public void closeParser(){
	 doc=null;
 }

public void ReadXmlFile(String filename) {
	     
	    try {
			if (logger.isDebugEnabled()) {
				//  logger.debug("ReadXmlFile(String) - reading file " + filename + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
	    	  File aFile=new File(filename);
	    	  
	  	    SAXReader xmlReader = new SAXReader();
	  	    xmlReader.setValidation(false);
			this.doc = xmlReader.read(aFile);
			if (logger.isDebugEnabled()) {
				//  logger.debug("ReadXmlFile(String) - finish reading the file  (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		       
		} catch (Exception e) {

			logger.error("ReadXmlFile(String)", e); //$NON-NLS-1$
	
		}
//    XmlPullParserFactory factory;
//	try {
//		factory = XmlPullParserFactory.newInstance(
//		        System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
//		 factory.setNamespaceAware(true);
//        xpp = factory.newPullParser();
//         xpp.setInput ( new FileReader ( filename ) );
//	} catch (XmlPullParserException e) {
//		
//		e.printStackTrace();
//	} catch (FileNotFoundException e) {
//	
//		e.printStackTrace();
//	}
//        
	
	
	
	
	
	
 }
@Deprecated
public void processDocument(XmlPullParser xpp)
throws XmlPullParserException, IOException
{
int eventType = xpp.getEventType();
int count=0;
do {
	try{
    if(eventType == xpp.START_DOCUMENT) {
					if (logger.isDebugEnabled()) {
						//  logger.debug("processDocument(XmlPullParser) - Start document"); //$NON-NLS-1$
					}
    } else if(eventType == xpp.END_DOCUMENT) {
					if (logger.isDebugEnabled()) {
						//  logger.debug("processDocument(XmlPullParser) - End document"); //$NON-NLS-1$
					}
    } else if(eventType == xpp.START_TAG) {
        processStartElement(xpp);
    } else if(eventType == xpp.END_TAG) {
        processEndElement(xpp);
    } else if(eventType == xpp.TEXT) {
        processText(xpp);
    }
    eventType = xpp.next();
    count++;
	}
	catch (Exception ex){
				logger.error("processDocument(XmlPullParser)", ex); //$NON-NLS-1$
	}
} while (eventType != xpp.END_DOCUMENT&&count<=7000);
}
@Deprecated
public void processStartElement (XmlPullParser xpp)
{
    //String name = xpp.getName();
    String uri = xpp.getNamespace();
//    if ("".equals (uri)) {
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("processStartElement(XmlPullParser) - Start element: " + name); //$NON-NLS-1$
//			}
//    } else {
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("processStartElement(XmlPullParser) - Start element: {" + uri + "}" + name); //$NON-NLS-1$ //$NON-NLS-2$
//			}
//    }
}


int holderForStartAndLength[] = new int[2];
private XmlPullParser xpp;

private Element     root;


@Deprecated
public void processText (XmlPullParser xpp) throws XmlPullParserException
{
    char ch[] = xpp.getTextCharacters(holderForStartAndLength);
    int start = holderForStartAndLength[0];
    int length = holderForStartAndLength[1];
		if (logger.isDebugEnabled()) {
			//  logger.debug("processText(XmlPullParser) - Characters:    \""); //$NON-NLS-1$
		}
    for (int i = start; i < start + length; i++) {
        switch (ch[i]) {
            case '\\':
				if (logger.isDebugEnabled()) {
					//  logger.debug("processText(XmlPullParser) - \\");
				}
                break;
            case '"':
if (logger.isDebugEnabled()) {
//  logger.debug("processText(XmlPullParser) - \\"); //$NON-NLS-1$
}
                break;
            case '\n':
if (logger.isDebugEnabled()) {
//  logger.debug("processText(XmlPullParser) - \n"); //$NON-NLS-1$
}
                break;
            case '\r':
if (logger.isDebugEnabled()) {
//  logger.debug("processText(XmlPullParser) - \r"); //$NON-NLS-1$
}
                break;
            case '\t':
if (logger.isDebugEnabled()) {
//  logger.debug("processText(XmlPullParser) - \t"); //$NON-NLS-1$
}
                break;
            default:
if (logger.isDebugEnabled()) {
//  logger.debug("processText(XmlPullParser) - " + ch[i]); //$NON-NLS-1$
}
                break;
        }
    }
if (logger.isDebugEnabled()) {
//  logger.debug("processText(XmlPullParser) - \"n"); //$NON-NLS-1$
}
}
@Deprecated
public void processEndElement (XmlPullParser xpp)
{
  //  String name = xpp.getName();
    String uri = xpp.getNamespace();
    if ("".equals (uri))
			if (logger.isDebugEnabled()) {
				//  logger.debug("processEndElement(XmlPullParser) - End element: " + name); //$NON-NLS-1$
			}
    else
 if (logger.isDebugEnabled()) {
				//  logger.debug("processEndElement(XmlPullParser) - End element:   {" + uri + "}" + name); //$NON-NLS-1$ //$NON-NLS-2$
			}
}

@Deprecated
public void iterateRootChildren() {
//	try {
//		processDocument(xpp);
//	} catch (XmlPullParserException e) {
//		
//		e.printStackTrace();
//	} catch (IOException e) {
//		
//		e.printStackTrace();
//	}
//	
    Element root = this.doc.getRootElement();
    Iterator elementIterator = root.elementIterator();
   // while(elementIterator.hasNext()){
      Element element = (Element)elementIterator.next();
		if (logger.isDebugEnabled()) {
			//  logger.debug("iterateRootChildren() -   name = " + element.getName()); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("iterateRootChildren() - value  " + element.attributeValue(element.attribute(0).getQName()) + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
//      logger.info(" text  "+element.getText()+" (" + this.getClass().getSimpleName() + "    "
//			+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
		if (logger.isDebugEnabled()) {
			//  logger.debug("iterateRootChildren() -   atribcount= " + element.attributeCount() + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
           
      List subElements = element.elements();
      
for (Iterator iter = subElements.iterator(); iter.hasNext();) {
	Element elementTemp = (Element) iter.next();
	List subsubElements = elementTemp.elements();
	for (Iterator iterator = subsubElements.iterator(); iterator.hasNext();) {
		Element elementT = (Element) iterator.next();
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("iterateRootChildren() -   name = " + elementT.getName()); //$NON-NLS-1$
//				}
//		
//		
//
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("iterateRootChildren() - value  " + elementT.attributeValue(elementT.attribute(0).getQName()) + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//				}
	      
	      String s=elementT.attributeValue(elementT.attribute(0).getQName());
	      String [] temp = null;
	      temp=s.split(" ");
	      for (int i = 0; i < temp.length-3; i+=3) {
			double x,y;
			long t;
			
	    	  x=Double.parseDouble(temp[i]);
	    	  y=Double.parseDouble(temp[i+1]);
	    	  t=Long.parseLong(temp[i+2]);
//					if (logger.isDebugEnabled()) {
//						//  logger.debug("iterateRootChildren() -  x = " + x + " y = " + y + "  t  = " + t + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
//					}
	    	  
		}
	      
//	      logger.info(" text  "+element.getText()+" (" + this.getClass().getSimpleName() + "    "
//				+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("iterateRootChildren() -   atribcount= " + elementT.attributeCount() + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//				}
	}
	
	   
	
	
	
	
	
}              
      
      
      
   // }
  }

public List getElements(String typeName){
	//get all elments of specific types 
	
	 Element root = this.doc.getRootElement();
	    Iterator elementIterator = root.elementIterator();
	    while(elementIterator.hasNext()){
	      Element element = (Element)elementIterator.next();
			if (logger.isDebugEnabled()) {
				//  logger.debug("getElements(String) -   name = " + element.getName()); //$NON-NLS-1$
			}
	      
	      if (element.getName().equals("type")){
	    	  String Test=element.attribute("name").getValue();
	    	  

				if (logger.isDebugEnabled()) {
					//  logger.debug("getElements(String) -    get type = " + typeName + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
	    	  
	    	  if (Test.equals(typeName)){
	    		//  logger.error( "  the type is "+typeName+"  read from the file "+Test+ "  having  "+element.elements("example").size()+" elements ");
	    
	    		 return element.elements("example");
	    		  
	    	  }
	      }
	    }   
	
	return null;
} 


public Element getExampleElement( ArrayList<Stroke> strokes, int sampletype){
	Element example =DocumentFactory.getInstance().createElement("example");
	Element StrokeElment ;
	if (sampletype>=0)
		example.addAttribute("label","+");
	else 
		example.addAttribute("label","-");
	
	
	example.addAttribute("numStrokes",""+strokes.size());
	for (int i = 0; i < strokes.size(); i++) {
		
	   StrokeElment = getStrokeElement(strokes.get(i));
		
		example.add(StrokeElment);
		
	}
	
	return example;
}


public 	Element getStrokeElement(Stroke s){
	
	
	Element  strokee  =DocumentFactory.getInstance().createElement("stroke");
	 
	//	 Element strokee = element.addElement("stroke");
		
		 
		 StringBuilder pointsString=new StringBuilder();
		 ArrayList<PointData> points = s.getPoints();
		 if (points!=null){
		 
		 for (int i = 0; i < points.size(); i++) {
			 if (i>0){
			 pointsString.append(" ");
			 }
			
			 pointsString.append( points.get(i).x); 
			 pointsString.append( " "); 
			 pointsString.append( points.get(i).y); 
			 pointsString.append( " "); 
			 pointsString.append( points.get(i).getTime());
			 
			
			 
			 
		}
		 
//		 if (logger.isDebugEnabled()){
//			 
//			 logger.debug("write the points "+pointsString);
//			 
//		 }
		 strokee.addAttribute("points", pointsString.toString());
		 
		 
		 
		 }

	    return strokee;	    

		
	}


public void addType(String name, ArrayList<ArrayList<Stroke>> examples,  ArrayList<Integer> SampleLabels ){
	
	if (root==null)
	{
		this.InitNewDoc();
	}
	logger.info("  adding type "+name);
	Element typeElments = root.addElement("type");
	
	typeElments.addAttribute("name", name);
	
	 Element exp;
	 
	for (int i = 0; i < examples.size(); i++) {
		
		//logger.info("  adding  element "+i);
	 exp = getExampleElement(examples.get(i), SampleLabels.get(i));
		
	   
	   typeElments.add(exp);
		
	}
	
	
	
}
public void InitNewDoc(){
	
	doc = DocumentFactory.getInstance().createDocument();
     root = doc.addElement("MSTrainingModel");
}

public Stroke getStrokeFromElements( Element element){
	
	/// <example label="+" numStrokes="1">
//	<stroke points="221.0 115.0 1033752000866 222.0 112.0 1033752000926 219.0 112.0 1033752000966 215.0 112.0 1033752000986 207.0 114.0 1033752001006 198.0 117.0 1033752001026 189.0 121.0 1033752001046 181.0 128.0 1033752001076 174.0 137.0 1033752001086 169.0 149.0 1033752001116 167.0 163.0 1033752001126 168.0 177.0 1033752001156 173.0 191.0 1033752001216 199.0 209.0 1033752001246 231.0 219.0 1033752001266 247.0 215.0 1033752001286 260.0 208.0 1033752001306 270.0 199.0 1033752001326 276.0 188.0 1033752001346 277.0 175.0 1033752001366 275.0 161.0 1033752001386 270.0 148.0 1033752001406 263.0 137.0 1033752001426 254.0 129.0 1033752001446 242.0 124.0 1033752001466 232.0 121.0 1033752001486 224.0 119.0 1033752001506 219.0 117.0 1033752001526 215.0 117.0 1033752001547"/>
//	</example>
	
	if (element.getName().equals("stroke")){
		// this is a storke element and can be converted to 
		
		Stroke stroke=new Stroke();
		
		
		  String s=element.attributeValue(element.attribute(0).getQName());
		//  logger.info("s ="+s);
	      String [] temp = null;
	      temp=s.split(" ");
	      PointData point=new PointData(0,0,0,0);
	   //   logger.info("temp size "+temp.length);
	      // now split on the space to get the values 
	      for (int i = 0; i < temp.length-3; i+=3) {
			double x,y;
			long t;
		//	 logger.info(" temp[i] "+temp[i]);
		//	 logger.info(" temp[i+1] "+temp[i+1]);
		//	 logger.info(" temp[i+2] "+temp[i+2]);
	    	  x=Double.parseDouble(temp[i]);
	    	  y=Double.parseDouble(temp[i+1]);
	    	  t=Long.parseLong(temp[i+2]);
	    
	    		 point=new PointData(x,y,t,0.0);
	    		stroke.addPoint(point);
	    		if (i==0)
	    		{
	    			stroke.setStartPoint(point);
	    		}
	    	
	    	  

//				if (logger.isDebugEnabled()) {
  //logger.debug("getStrokeFromElements(Element) -  x = " + x + " y = " + y + "  t  = " + t + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
//				}
	    	  
		}
	      stroke.setEndPoint(point);
		return stroke;
		
	
		//
		
	}
	
	return null;
}
public ArrayList<Stroke> getExampleFromElements( Element element,String pos){
	
	/// <example label="+" numStrokes="1">
//	<stroke points="221.0 115.0 1033752000866 222.0 112.0 1033752000926 219.0 112.0 1033752000966 215.0 112.0 1033752000986 207.0 114.0 1033752001006 198.0 117.0 1033752001026 189.0 121.0 1033752001046 181.0 128.0 1033752001076 174.0 137.0 1033752001086 169.0 149.0 1033752001116 167.0 163.0 1033752001126 168.0 177.0 1033752001156 173.0 191.0 1033752001216 199.0 209.0 1033752001246 231.0 219.0 1033752001266 247.0 215.0 1033752001286 260.0 208.0 1033752001306 270.0 199.0 1033752001326 276.0 188.0 1033752001346 277.0 175.0 1033752001366 275.0 161.0 1033752001386 270.0 148.0 1033752001406 263.0 137.0 1033752001426 254.0 129.0 1033752001446 242.0 124.0 1033752001466 232.0 121.0 1033752001486 224.0 119.0 1033752001506 219.0 117.0 1033752001526 215.0 117.0 1033752001547"/>
//	</example>
	
	if (element.getName().equals("example")){
		  String tempos;
		//if (SystemSettings.CheckLabel)
		  tempos= element.attribute("label").getValue();
//		  else 
//		   tempos=pos;
		  if (tempos.equalsIgnoreCase(pos)){
		
			//  logger.info("  label from file "+tempos+"   reading labelssssss"+pos);
		ArrayList<Stroke> resultSet=new ArrayList<Stroke>();
		// this is a storke element and can be converted to 
		 List subElements = element.elements();  // get strokes elemtns 
	      
		 for (Iterator iter = subElements.iterator(); iter.hasNext();) {
		 
		 		Stroke stroke=getStrokeFromElements((Element) iter.next());
		 		 resultSet.add(stroke);	
		
		
	}
		return resultSet;
		  }
	}
	
	
	return null;
}


public List getTypes(){
	ArrayList<String > result=new ArrayList<String>();
	 Element root = this.doc.getRootElement();
	    Iterator elementIterator = root.elementIterator();
	    while(elementIterator.hasNext()){
	      Element element = (Element)elementIterator.next();
//			if (logger.isDebugEnabled()) {
	    //  logger.debug("getTypes() -   name = " + element.getName()); //$NON-NLS-1$
//			}
	      
	      if (element.getName().equals("type")){
	    	  String Test=element.attribute("name").getValue();
	    	  result.add(Test);
//	    	  logger.error(" L("
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ ")" + " - " + "   the type = "+Test);
//	    	  if (Test.equals(typeName)){
//	    		  
//	    		 return element.elements();
//	    		  
//	    	  }
	      }
	    }   
	
	return result;
	
}


 public void WriteXmlFile(String filename){

	try {
		
		FileOutputStream fos = new FileOutputStream(filename);
	 OutputFormat format = OutputFormat.createPrettyPrint();
	 XMLWriter writer;
		writer = new XMLWriter(fos, format);
		 writer.write(doc);
		 writer.flush();
	} catch (UnsupportedEncodingException e) {
		 
		e.printStackTrace();
		logger.error(e);
		
	} catch (FileNotFoundException e) {
		 
		e.printStackTrace();
		logger.error(e);
	} catch (IOException e) {
	 
		e.printStackTrace();
		logger.error(e);
	}
	
 }
public XmlParser() {
	super();
}
 
 
}
