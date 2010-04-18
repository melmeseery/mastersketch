package SketchMaster.lib;

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
		if (path.contains(SEPERATOR_WINDOWS)& path.contains(":"))
		{
			
			//D:\\AUC\\Databases\\Arabic Digits Databases\\AHDBase\\
			return path;
		}
		else {
			
			String temp=path.replaceAll( SEPERATOR_LINUX,SEPERATOR_WINDOWS);
			temp=temp.replace("/windows/","");
			int index=temp.indexOf(SEPERATOR_WINDOWS);
			String temp2=temp;
		    if (index>0){
		    	temp2="";
		    	temp2=temp.substring(0, index-1)+":";
		    	temp2+=temp.substring(index);
			///windows/D/AUC/Databases/Arabic Digits Databases/AHDBase/
			
			
			
		}	
		    
		    return temp2;
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
