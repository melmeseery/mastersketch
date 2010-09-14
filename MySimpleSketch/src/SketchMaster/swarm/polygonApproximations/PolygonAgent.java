package SketchMaster.swarm.polygonApproximations;

import java.util.Random;

import SketchMaster.swarm.Agent;
import SketchMaster.swarm.Solution;
import SketchMaster.system.SystemSettings;

public class PolygonAgent extends Agent {

	static protected double Vmax =  SystemSettings.SWARM_CONSTANTS_VMAX;
	/* {src_lang=Java} */

	static protected double C1 =  SystemSettings.SWARM_CONSTANTS_C1, C2 = SystemSettings.SWARM_CONSTANTS_C2;
	static protected double w = SystemSettings.SWARM_CONSTANTS_W;

	public double getCurrentFitness() {
		return currentSolution.eval();
	}

	@Override
	public void move() {
		// get the array of variables of pi , local best and global best
		int[] pi = ((PolygonSolution) currentSolution).getParticlePoints();
		int[] pbest = ((PolygonSolution) localbest).getParticlePoints();
		int[] gbest = ((PolygonSolution) globalBest).getParticlePoints();
		// generate the random numbers
		double r1, r2, r3;
//		r1 = Math.random();
//		r2 = Math.random();
//		r3 = Math.random();
		
//		Random  r1Random,r2Random,r3Random;
//		r1Random=new Random(11);
//		r2Random=new Random(22);
//		r3Random=new Random(33);
		r1 =r1Random.nextDouble();//
		//Math.random();
		r2 =r2Random.nextDouble();
		r3 =r3Random.nextDouble();
		// now get the velocity array of the location

		double[] vi = ((PolygonSolution) currentSolution).getVelocity();

		double gbestterm, pbestterm;
		double templocation;
		for (int j = 0; j < pi.length; j++) {
//			r1 = Math.random();
//			r2 = Math.random();
//			r3 = Math.random();
			r1 =r1Random.nextDouble();//Math.random();
			r2 =r2Random.nextDouble();
			r3 =r3Random.nextDouble();
			// computer terms speratly
			pbestterm = C1 * r1 * (pbest[j] - pi[j]);
			gbestterm = C2 * r2 * (gbest[j] - pi[j]);

			// the velocity equaiton.
			vi[j] = w * vi[j] + pbestterm + gbestterm;
			// logger.info("velcoity s "+vi[j]);
		//	if (vi[j] >= (Vmax))  // commenting the condditon to make it every time after take with dina july 20009
				// use s(v) to restrice the velocity to vmax
			if (vi[j]>0)
				vi[j] = 0.5 + (vi[j] / (2.0 * Vmax));
			else {
				vi[j] = 0.5 + (vi[j] / (-2.0 * Vmax));
			}

		/*	if (vi[j] <(-Vmax))// changes after mona talk on 3-03-2008  
				//to add condition when vi is  larger than -vman ex. vi=-5 and vmax =-3
				// use s(v) to restrice the velocity to vmax
				vi[j] = 0.5 - (vi[j] / (2.0 * Vmax));
			
			*/
			// the function pj = pj(old) +vj(new)
			templocation = vi[j];
			// logger.info("velcoity computeed fianal is "+vi[j]+" and r3
			// is "+r3);
			// now set the new location of the particle useing the eq 3 p 245 of
			// paper.
			if (templocation >= ((r3)))
				pi[j] = 1;
			else
				pi[j] = 0;

		}

		// first and last musst p 1
		if (pi.length >0){
		pi[0] = 1;
		pi[pi.length - 1] = 1;
		}
		// now set the velocity and loction to the currentsolution
		((PolygonSolution) currentSolution).setParticlePoints(pi);
		((PolygonSolution) currentSolution).setVelocity(vi);

		((PolygonSolution) currentSolution).refineSolution();

		((PolygonSolution) currentSolution).calculateSolutionParameters();

	}

	@Override
	public void move( Solution agent1, Solution agent2) {
		
		// save this for compare with the new fitnessss. 
		int []  Oldpi=((PolygonSolution) currentSolution).getParticlePoints();	
		double oldFitness=((PolygonSolution) currentSolution).eval();
		double[] viOld = ((PolygonSolution) currentSolution).getVelocity();
		 // try to move to new agents......\\
		// get the array of variables of pi , local best and global best
		int[] pi = ((PolygonSolution) currentSolution).getParticlePoints();

		
		int[] pj = ((PolygonSolution) agent1).getParticlePoints();
		int[] pl = ((PolygonSolution) agent2).getParticlePoints();		
		int[] gbest = ((PolygonSolution) globalBest).getParticlePoints();
		// generate the random numbers
		double r1, r2, r3;
		r1 =r1Random.nextDouble();
		r2 =r2Random.nextDouble();
		r3 =r3Random.nextDouble();
		// now get the velocity array of the location
		double[] vi = ((PolygonSolution) currentSolution).getVelocity();

		double gbestterm, pbestterm;
		double templocation;
		for (int j = 0; j < pi.length; j++) {
//			r1 = Math.random();
//			r2 = Math.random();
//			r3 = Math.random();
			r1 =r1Random.nextDouble();//Math.random();
			r2 =r2Random.nextDouble();
			r3 =r3Random.nextDouble();
			// computer terms speratly
			pbestterm = C1 * r1 * (pj[j] - pl[j]);  // the new term.......................
			gbestterm = C2 * r2 * (gbest[j] - pi[j]);

			// the velocity equaiton.
			vi[j] = w * vi[j] + pbestterm + gbestterm;
			// logger.info("velcoity s "+vi[j]);
		//	if (vi[j] >= (Vmax))  // commenting the condditon to make it every time after take with dina july 20009
				// use s(v) to restrice the velocity to vmax
//			if (vi[j]>0)
//				vi[j] = 0.5 + (vi[j] / (2.0 * Vmax));
//			else {
//				vi[j] = 0.5 + (vi[j] / (-2.0 * Vmax));
//			}
 
			templocation = vi[j];
			if (templocation >= ((r3)))
				pi[j] = 1;
			else
				pi[j] = 0;

		}
		// first and last musst p 1
		if (pi.length >0){
		pi[0] = 1;
		pi[pi.length - 1] = 1;
		}
		// now set the velocity and loction to the currentsolution
		((PolygonSolution) currentSolution).setParticlePoints(pi);
		((PolygonSolution) currentSolution).setVelocity(vi);
		((PolygonSolution) currentSolution).refineSolution();
		((PolygonSolution) currentSolution).calculateSolutionParameters();
		double newfit=((PolygonSolution) currentSolution).eval();
		
		if (compare(oldFitness,newfit)<0){  // if not better... 
			// return to the old particle 
			((PolygonSolution) currentSolution).setParticlePoints(Oldpi);
			((PolygonSolution) currentSolution).setVelocity(viOld);
			move();
		}// else return... 
		
		
		
	}

	private double compare(double oldFitness, double newfit) {
	          return newfit-oldFitness;
	}

	// /**
	// * @uml.property name="localBest"
	// * @uml.associationEnd
	// inverse="polygonAgent:SketchMaster.swarm.polygonApproximations.polygonSolution"
	// * @uml.association name="LocalBest"
	// */
	// private polygonSolution localBest = new
	// SketchMaster.swarm.polygonApproximations.polygonSolution();
	//
	// /**
	// * Getter of the property <tt>localBest</tt>
	// * @return Returns the localBest.
	// * @uml.property name="localBest"
	// */
	// public polygonSolution getLocalBest() {
	// return localBest;
	// }
	//
	// /**
	// * Setter of the property <tt>localBest</tt>
	// * @param localBest The localBest to set.
	// * @uml.property name="localBest"
	// */
	// public void setLocalBest(polygonSolution localBest) {
	// this.localBest = localBest;
	// }
}