/**
 * 
 */
package SketchMaster.io.db;

import org.apache.log4j.Logger;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.geometric.*;
import org.postgresql.jdbc3.Jdbc3Array;
import org.postgresql.PGConnection;
import org.postgresql.fastpath.*;


/**
 * @author Maha
 *
 */
public class PostQreDataBaseConnector {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PostQreDataBaseConnector.class);

	
	
	static transient String connString="jdbc:postgresql://localhost/etchasketches";
	static  String username="Maha";
	static String password= "sketch";
	static Connection conn = null;
	static String strokeSelect="SELECT * FROM strokes ";
	private int Strokeid=-1;
	private int SketchId=-1;
	private int AuthorId=-1;
	private int StudyId=-1;
	static String sketchSelect="SELECT * FROM sketches ";
	static String authorsSelect="SELECT * FROM authors ";
	
	
	
	public PostQreDataBaseConnector(){
		try {
			  Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException cnfe) {
			logger.error("PostQreDataBaseConnector() - Couldn't find driver class: (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ", cnfe); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			logger.error("PostQreDataBaseConnector()", cnfe); //$NON-NLS-1$
			}
		 
	 }
	public void ConnectDB(){
		
		
		  
		  try {
		    // The second and third arguments are the username and password,
		    // respectively. They should be whatever is necessary to connect
		    // to the database.
		    conn = DriverManager.getConnection(connString,
		                                    username, password);
		  } catch (SQLException se) {
			if (logger.isDebugEnabled()) {
				//  logger.debug("ConnectDB() - Couldn't connect: print out a stack trace and exit."); //$NON-NLS-1$
			}
			logger.error("ConnectDB()", se); //$NON-NLS-1$
		    //System.exit(1);
		  }
		  
		  if (conn != null)
			if (logger.isDebugEnabled()) {
				//  logger.debug("ConnectDB() - Hooray! We connected to the database!"); //$NON-NLS-1$
			}
		  else
 if (logger.isDebugEnabled()) {
				//  logger.debug("ConnectDB() - We should never get here."); //$NON-NLS-1$
			}
		  
		
	}
	public void closeConnection(){
		try {
			conn.close();
			
			Strokeid=-1;
			 SketchId=-1;
			 AuthorId=-1;
			 StudyId=-1;
			
		} catch (Exception e) {
			logger.error("closeConnection()", e); //$NON-NLS-1$
		}
		
	}
/// get the get row  stroke with id = 
	// get list of authors //
	// get list of sketches 
	// get all stroke id in a sketches 
	
 
	public 		ArrayList<Integer> getAuthorsId(){
		 String 	 authorsIDSelect=authorsSelect.replace("*", " id ");
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery( authorsIDSelect);
		ArrayList<Integer>  ids=new ArrayList<Integer>();
		while (rs.next())
		{
			
			ids.add((Integer)rs.getObject(1));
			
		}
		
		
	
		stmt.close();
	        return ids;
	
	
	
		} catch (SQLException e) {
		

			logger.error("getAuthorsId()", e); //$NON-NLS-1$
			return null;
		}
		
		
	}
	public 		ArrayList<Integer> getSketchesId(){
		
		
		String sketchIdSelect=sketchSelect.replace("*", " id ");
		
		
		
//		logger.info(" sketchIdSelect "+sketchIdSelect+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		Statement stmt;
		try {
			stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery(sketchIdSelect + getSelectionAuthorStudyString());
		ArrayList<Integer>  ids=new ArrayList<Integer>();
		while (rs.next())
		{
			
			ids.add((Integer)rs.getObject(1));
			
			
		}
		
		
	
		rs.close();
		stmt.close();
			if (logger.isDebugEnabled()) {
				//  logger.debug("getSketchesId() - " + ids + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
	        return ids;
	
	
	        
	
		} catch (SQLException e) {
		

			logger.error("getSketchesId()", e); //$NON-NLS-1$
			return null;
		}
		
		
	}
	
	public ArrayList<Integer>  getStrokeId(){
	String sketchIdSelect=strokeSelect.replaceFirst("..", " id ");
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery( sketchIdSelect  + getOptionsSketchAuthorString());
		ArrayList<Integer>  ids=new ArrayList<Integer>();
		while (rs.next())
		{
			
			ids.add((Integer)rs.getObject(1));
			
		}
		
		rs.close();
		stmt.close();
	
	
	        return ids;
	
	
	
		} catch (SQLException e) {
		

			logger.error("getStrokeId()", e); //$NON-NLS-1$
			return null;
		}
		
	}
	//public 
	public ResultSet getStroke() {
	    
		Statement stmt;
		try {
			stmt = conn.createStatement();
			
		
		ResultSet rs = stmt.executeQuery(strokeSelect+getOptionsSketchAuthorString());
		rs.next();
		
		
		stmt.close();
		return rs;
	
	
	
	
	
	
		} catch (SQLException e) {
		

			logger.error("getStroke()", e); //$NON-NLS-1$
			return null;
		}
	}
	public ResultSet getStrokes() {
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			if (logger.isDebugEnabled()) {
				//  logger.debug("getStrokes() - Statment is " + strokeSelect + getOptionsSketchAuthorString() + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		ResultSet rs = stmt.executeQuery(strokeSelect+getOptionsSketchAuthorString());
		
		stmt.close();
		//rs.close();
		//rs.next();
		return rs;
	
	
	
	
	
	
		} catch (SQLException e) {
		

			logger.error("getStrokes()", e); //$NON-NLS-1$
			return null;
		}
	}
	private String getOptionsSketchAuthorString() {
		String cond="";
		if (SketchId!=-1)
		{
			cond+="  where  sketch='"+SketchId+"'  ";
			return cond;
		}
		if (AuthorId!=-1)
		{
			cond+="  where   author ='"+AuthorId+"'  ";
			return cond;
		}
		
		return cond;
	}
	private String getOptionsStrokeString() {
		
		if (Strokeid!=-1)
		
			return "   where id='"+Strokeid+"' ";
		else 
			return "";
	}
	private String getSelectionAuthorStudyString(){
		
		String cond="",auth=null,study=null;
		
		if (AuthorId!=-1)
		{
			auth="   author ='"+AuthorId+"'   ";
			
		}
		if (StudyId!=-1)
		{
			study="  study='"+StudyId+"'   ";
		}
		if (auth==null && study==null)
			return cond;
		else if ((auth!=null )&&(study!=null)) 
		{
			// both exist then 
			 
			 cond="   WHERE "+auth+"   AND  "+study;
			
			return cond;
		}
		else {
		if (auth!=null )
		{
			 cond="   WHERE  "+auth;
			
		}
		else {
			 cond="   WHERE  "+study;
		}
			
		return cond;
		}
		
		
		
		
		
	}
	public void setSketchOption(int id){
		
		SketchId=id;
		
	}
	public void setAuthorOption(int id){
		AuthorId=id;
		
	}
	public void setStrokeOption(int id){
		
		Strokeid=id;
		
	}
public void setStudyOption(int id){
		
		StudyId=id;
		
	}
	
}
