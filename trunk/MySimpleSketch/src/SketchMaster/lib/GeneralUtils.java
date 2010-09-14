package SketchMaster.lib;

import java.util.regex.Pattern;

import SketchMaster.system.SystemSettings;

 

public class GeneralUtils {
	public  static final String SEPERATOR_WINDOWS="\\";
public static final String SEPERATOR_LINUX="/";


public static int getCurrentOs(){
	
	
	   String nameOS = "os.name";        
	   String osName =System.getProperty(nameOS);
	   
	if (osName.startsWith("Lin"))
	{
		SystemSettings.OS=SystemSettings.OS_LINUX;
		return SystemSettings.OS_LINUX;
		
	}
	else {
		SystemSettings.OS=SystemSettings.OS_WINDOWS;
		return SystemSettings.OS_WINDOWS;
	}
	
}

public static String CorrectPath(String path, int toOs){
	
	if (	 toOs == SystemSettings.OS_WINDOWS)
	{
		return toWindows(path);
	}
	else {
		return toLinux(path);
	}
}
	
public static String CorrectPath(String path){
		
		if (	SystemSettings.OS == SystemSettings.OS_WINDOWS)
		{
			return toWindows(path);
		}
		else {
			return toLinux(path);
		}
	}
	
	public static String toWindows(String path){
		// check if is already windows 
		// get the letter path... 
		//System.out.println(" the path is "+path+"  SEPERATOR_LINUX  "+SEPERATOR_LINUX);
		if (path.contains(SEPERATOR_WINDOWS)&& path.contains(":"))
		{
			
			//D:\\AUC\\Databases\\Arabic Digits Databases\\AHDBase\\
			return path;
		}
		else {
			if(path.contains(SEPERATOR_LINUX)){
			String 	temp=path.replace("/windows/","");
			//Pattern.compile(SEPERATOR_LINUX).matcher(temp).replaceAll(SEPERATOR_WINDOWS);
			//temp=temp.replaceAll( SEPERATOR_LINUX,SEPERATOR_WINDOWS);
		
			int index=temp.indexOf(SEPERATOR_WINDOWS);
		//System.out.println(" the windows seperator is in "+index);
			String temp2=temp;
		    if (index>0){
		    	temp2="";
		    	temp2=temp.substring(0, index-1)+":";
		    	temp2+=temp.substring(index);
			///windows/D/AUC/Databases/Arabic Digits Databases/AHDBase/
		    	
		    }
		    else{
		    	
		    	
				int index2=temp.indexOf(SEPERATOR_LINUX);
				//System.out.println(" the linux seperator is in "+index2);
				 temp2=temp;
			    if (index2>0){
			    	temp2="";
			    	temp2=temp.substring(0, index2)+":";
			    	temp2+=temp.substring(index2);
				///windows/D/AUC/Databases/Arabic Digits Databases/AHDBase/
			    	
			    }
		    }
			
			 return temp2;
		}	
		    return path;
		   
		}
		    
	}
	public static String toLinux(String path){
		
		if (path.contains(SEPERATOR_LINUX)& (!path.contains(":")))
		{
			
			//D:\\AUC\\Databases\\Arabic Digits Databases\\AHDBase\\
			return path;
		}
		else {
			if (!path.contains( SEPERATOR_WINDOWS))
			{
				return path;
				
			}
			String temp=path.replaceAll(SEPERATOR_WINDOWS+ SEPERATOR_WINDOWS, SEPERATOR_LINUX);
			temp="/windows/"+temp.replace(":","");
		
			return temp;
		    
		    }
	
		
	}
	
	
}
