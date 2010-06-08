/**
 * 
 */
package SketchMaster.io.db;

import org.apache.log4j.Logger;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.geometric.PGpath;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;

/**
 * @author Maha
 *
 */
public class DataBaseManager {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DataBaseManager.class);

	/**
	 * @param args
	 */
	PostQreDataBaseConnector db=new PostQreDataBaseConnector();
	
	public void setSketchOption(int id){
		db.setSketchOption(id);
		
	}
	public void setAuthorOption(int id){
		
		db.setAuthorOption(id);
	}
	
	
	public ArrayList<Stroke> getStrokes(){
		
		ArrayList<Stroke> strokes=new ArrayList<Stroke>();
		
//		logger.info("connect to the database"+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		db.ConnectDB();
//		logger.info("get strokes "+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		ResultSet rs=db.getStrokes();
		Stroke s;
		if (rs!=null){
		
			try {
				int countS=0;
				 while (rs.next()) {
						
					 
					 
//						logger.info("adding a new storke "+countS+" ("
//								+ this.getClass().getSimpleName()
//								+ "    "
//								+ (new Throwable()).getStackTrace()[0]
//										.getLineNumber() + "  )  ");
						countS++;
						 s=new Stroke();
						
						PGpath path;
				path = (PGpath)rs.getObject("path");
			Array timesArray=(Array)rs.getArray("times");
			//ArrayList<PointData> points = s.getPoints();
			
			int[] times=(int[])timesArray.getArray();
				
			for (int i = 0; i < times.length; i++) {
				PointData pTemp=new  PointData();
				
				pTemp.x=path.points[i].x;
				pTemp.y=path.points[i].y;
				pTemp.setTime(times[i]);
				s.addPoint(pTemp);
				//points.add(pTemp);
//				logger.info(pTemp+" ("
//						+ this.getClass().getSimpleName() + "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
			}
			//s.setPoints(points);
			s.setStartPoint(s.getPoints().get(0));
			s.setEndPoint(s.getPoints().get(s.getPoints().size()-1));
			s.setOpen(path.isOpen());
			s.setClosed(path.isClosed());
			strokes.add(s);
			}
				 
			
				
			}
			 catch (SQLException e) {
				 
				logger.error("getStrokes()", e); //$NON-NLS-1$
			}
    		
		  


	    	
			
		}
		db.closeConnection();
		
		return strokes;
	}
	
	public ArrayList<Integer> getSketchID(){
		db.ConnectDB();
		ArrayList<Integer> temp = db.getSketchesId();
		db.closeConnection();
		return temp;
	} 

	public ArrayList<Integer> getSketchID(int study, int author){
		db.ConnectDB();
		db.setAuthorOption(author);
		db.setStudyOption(study);
		ArrayList<Integer> temp = db.getSketchesId();
		db.closeConnection();
		return temp;
	} 
	public Stroke getStroke(){
		
		Stroke s=null;
		db.ConnectDB();
		
		ResultSet rs=db.getStroke();
		
		if (rs!=null){
			 s=new Stroke();
			
			PGpath path;
			try {
				path = (PGpath)rs.getObject("path");
			Array timesArray=(Array)rs.getArray("times");
			ArrayList<PointData> points = s.getPoints();
			
			int[] times=(int[])timesArray.getArray();
				
			for (int i = 0; i < times.length; i++) {
				PointData pTemp=new  PointData();
				
				pTemp.x=path.points[i].x;
				pTemp.y=path.points[i].y;
				pTemp.setTime(times[i]);
				points.add(pTemp);
		
			}
			s.setPoints(points);
			s.setStartPoint(points.get(0));
			s.setEndPoint(points.get(points.size()-1));
			s.setOpen(path.isOpen());
			s.setClosed(path.isClosed());
			
			} catch (SQLException e) {
				logger.error("getStroke()", e); //$NON-NLS-1$
			}
		}
		db.closeConnection();
		return s;
	}
	public void setStrokeOption(int i) {
	db.setStrokeOption(i);
	}

}
