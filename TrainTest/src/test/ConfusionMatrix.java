/**
 * 
 */
package test;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Maha
 *
 */
public class ConfusionMatrix {

	
   ArrayList<String> classnames;
   
   int Matrix[][];
   boolean Init=false;
   
   
   public HSSFSheet  writeToWorkbook( HSSFSheet sheet){
	   HSSFRow row = sheet.createRow((short)0);
	   row.createCell((short)0).setCellValue( new HSSFRichTextString ( " Categories "));
	   for (int i = 0; i < classnames.size(); i++) {
		   row.createCell((short)(i+1)).setCellValue( new HSSFRichTextString (  classnames.get(i) ));
	}
	   
	  // int r=1;
	   ///after header now the she
	   
	   for (int i = 0; i < Matrix.length; i++) {
		   
		   HSSFRow row2 = sheet.createRow((short)(i+1));
		   row2.createCell((short)(0)).setCellValue( new HSSFRichTextString (  classnames.get(i) ));
		   
		for (int j = 0; j < Matrix[i].length; j++) {
			row2.createCell((short)(j+1)).setCellValue( Matrix[i][j]);
		}
	}
	  
	  
	  
	   return sheet;
   }
   public void initClases( ArrayList<String> classn){
	   
	   classnames=new ArrayList<String>();
	   for (int i = 0; i < classn.size(); i++) {
		classnames.add(classn.get(i));
	}
	   
	   
	   //now inti the matrix 
	   
	   Matrix=new int[classnames.size()][classnames.size()];
	   
	   for (int i = 0; i < Matrix.length; i++) {
		for (int j = 0; j <  Matrix[i].length; j++) {
			Matrix[i][j]=0;
		}
	}
	   
	   Init=true;
   }
   
   public void AddCorrectSample(String className){
	   int j=-1;
	   for (int i = 0; i < classnames.size(); i++) {
		if (classnames.get(i).equals(className))
		{ j=i;
		break;
			}
	}
	   
	   /////////
	   
	   if(j>=0){
		   
		   Matrix[j][j]++;
	   }
	   
	   
   }
   public void AddSample(String required, String result){
	   int j=-1;
	   
	   for (int i = 0; i < classnames.size(); i++) {
			if (classnames.get(i).equals(required))
			{ j=i;
			break;
				}
	   }
			int k=-1;
			   for (int i = 0; i < classnames.size(); i++) {
					if (classnames.get(i).equals(result))
					{ k=i;
					break;
						}
			   }
	   //////////////////now add to matrix....
			   
			   if (j>=0 && k>=0){
				   
				   Matrix[j][k]++;
			   }
   }
/**
 * @return the init
 */
public boolean isInit() {
	return Init;
}
   
	
}
