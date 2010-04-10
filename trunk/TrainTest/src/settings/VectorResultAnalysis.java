package settings;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class VectorResultAnalysis {

	HashMap<String, ArrayList<VertixTestResultNode>> categoryResults=new  HashMap<String, ArrayList<VertixTestResultNode>>();
	ArrayList<DataStat> CatError=new 	ArrayList<DataStat>(); //this will give us the avg of each cat.
	ArrayList<DataStat> CatSegCont=new 	ArrayList<DataStat>();
	ArrayList<DataStat> CatPCont=new 	ArrayList<DataStat>();
	ArrayList<DataStat> CatVerCont=new 	ArrayList<DataStat>();
	ArrayList<String> Category=new 	ArrayList<String> ();
	
	//now total average for each data 
	DataStat ErrorStat;
	DataStat SegStat;
	DataStat PointsStat;
	DataStat VerStat;
    
	public HSSFSheet WriteToxls(  HSSFSheet sheet){
		// this work sheet will be as follow 
		
		// cat ...
		//error 
		//
		  
		    HSSFRow row = sheet.createRow((short)0);
		    
		    
		    row.createCell((short)0).setCellValue( new HSSFRichTextString ( "Catgory Name"));
		    
		    for (int i = 0; i <Category.size(); i++) {
		    	row.createCell((short)(i+1)).setCellValue( new HSSFRichTextString (  Category.get(i)));
			}
		    
		    row.createCell((short)(Category.size()+1)).setCellValue( new HSSFRichTextString ( "Average "));
		 //   row.createCell((short)(Category.size()+2)).setCellValue( new HSSFRichTextString ( " STD "));
		    
		    HSSFRow   rowE = sheet.createRow((short)1);
		    HSSFRow   rowE2 = sheet.createRow((short)2);
		    HSSFRow   rowE3 = sheet.createRow((short)3);
		    HSSFRow   rowE4 = sheet.createRow((short)4);
		    
		    HSSFRow   rowP = sheet.createRow((short)5);
		    HSSFRow   rowS = sheet.createRow((short)6);
		    HSSFRow   rowV = sheet.createRow((short)7);
		     HSSFRow   rowV2 = sheet.createRow((short)8);
		    HSSFRow   rowV3 = sheet.createRow((short)9);
		    HSSFRow   rowV4 = sheet.createRow((short)10);
		    
		    
		    rowE.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error AV"));
		    rowE2.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error MAx"));
		    rowE3.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error Min"));
		    rowE4.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error STD"));
		    rowP.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Points "));
		    rowS.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Segments Count "));
		    rowV.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count AV"));
		    rowV2.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count  Max"));
		    rowV3.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count Min"));
		    rowV4.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count STD"));
		    
		    
		    
		    for (int i = 0; i <Category.size(); i++) {
		    	rowE.createCell((short)(i+1)).setCellValue(CatError.get(i) .Average);
		        rowE2.createCell((short)(i+1)).setCellValue( CatError.get(i).Max );
			    rowE3.createCell((short)(i+1)).setCellValue( CatError.get(i).Min );
			    rowE4.createCell((short)(i+1)).setCellValue(  CatError.get(i).Std);
			    rowP.createCell((short)(i+1)).setCellValue( CatPCont.get(i) .Average );
			    rowS.createCell((short)(i+1)).setCellValue(    CatSegCont.get(i) .Average);
			    rowV.createCell((short)(i+1)).setCellValue(   CatVerCont .get(i) .Average);
			    rowV2.createCell((short)(i+1)).setCellValue(    CatVerCont .get(i).Max );
			    rowV3.createCell((short)(i+1)).setCellValue(  CatVerCont  .get(i).Min );
			    rowV4.createCell((short)(i+1)).setCellValue(   CatVerCont  .get(i).Std );
			    
			    
			    
			    
			}
		    
			rowE.createCell((short)(Category.size()+1)).setCellValue(ErrorStat.Average);
			
	        rowE2.createCell((short)(Category.size()+1)).setCellValue(ErrorStat.Max );
		    rowE3.createCell((short)(Category.size()+1)).setCellValue( ErrorStat.Min );
		    rowE4.createCell((short)(Category.size()+1)).setCellValue(  ErrorStat.Std);
		    rowP.createCell((short)(Category.size()+1)).setCellValue( PointsStat.Average  );
		    rowS.createCell((short)(Category.size()+1)).setCellValue( SegStat.Average  );
		    rowV.createCell((short)(Category.size()+1)).setCellValue(   VerStat.Average  );
		    rowV2.createCell((short)(Category.size()+1)).setCellValue(    VerStat.Max);
		    rowV3.createCell((short)(Category.size()+1)).setCellValue(    VerStat.Min);
		    rowV4.createCell((short)(Category.size()+1)).setCellValue(    VerStat.Std);
		    
		    
		    
		return sheet;
		
		
	}
	public void  SorStats(){}
	
	public void ComputeAllStats()
	{
		Iterator<Entry<String, ArrayList<VertixTestResultNode>>> col = categoryResults.entrySet().iterator();
		for (	Iterator iterator = col; iterator.hasNext();) {
			Entry<String, ArrayList<VertixTestResultNode>> dataVer = (Entry<String, ArrayList<VertixTestResultNode>>) iterator.next();
			
			 String cats=dataVer.getKey();
			 ArrayList<VertixTestResultNode> dataver=dataVer.getValue();
			 Category.add(cats);
			 ComputeCatStat(dataver);
			
			
		}
  ErrorStat=new DataStat();
	  SegStat=new DataStat();
	  PointsStat=new DataStat();
	  VerStat=new DataStat();
		
		for (int i = 0; i < Category.size(); i++) {
			for (int j = 0; j < CatError.get(i).getData().size(); j++) {

				  ErrorStat.AddData(CatError.get(i).getData().get(j) );
				  SegStat.AddData(CatSegCont.get(i).getData().get(j));
				  PointsStat.AddData( CatPCont.get(i).getData().get(j));
				  VerStat.AddData( CatVerCont.get(i).getData().get(j));
			}
			 
		}
		  ErrorStat.ComputeState();
		  SegStat.ComputeState();
		  PointsStat.ComputeState();
		  VerStat.ComputeState();
		
		
	}

	private void ComputeCatStat(ArrayList<VertixTestResultNode> data){
		DataStat TErrorStat=new DataStat() ;
		DataStat TSegStat=new DataStat() ;
		DataStat TPointsStat=new DataStat() ;
		DataStat TVerStat=new DataStat() ;
		
		for (int i = 0; i < data.size(); i++) {
			TErrorStat.AddData( data.get(i).Error);
			TSegStat.AddData( data.get(i).CountOfSegments);
			TPointsStat.AddData( data.get(i).StrokePointsCount);
			 TVerStat.AddData( data.get(i).vertixCount);
		}
		
		TErrorStat.ComputeState();
		TSegStat.ComputeState();
		TPointsStat.ComputeState();
		 TVerStat.ComputeState();
		 
		 
		  CatError.add(TErrorStat) ;
		  CatSegCont.add(TSegStat) ;
		  CatPCont.add(TPointsStat); 
		 CatVerCont.add( TVerStat) ;
		 
		 
		 
	
		
	}

	/**
	 * @param categoryResults the categoryResults to set
	 */
	public void setCategoryResults(
			HashMap<String, ArrayList<VertixTestResultNode>> categoryResults) {
		this.categoryResults = categoryResults;
		
		this.ComputeAllStats();
	}
	
}
