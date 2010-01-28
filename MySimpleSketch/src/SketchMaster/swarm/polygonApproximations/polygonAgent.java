package SketchMaster.swarm.polygonApproximations;

import java.util.Random;

import SketchMaster.swarm.Agent;
import SketchMaster.system.SystemSettings;

public class polygonAgent extends Agent {

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
		int[] pi = ((polygonSolution) currentSolution).getParticlePoints();
		int[] pbest = ((polygonSolution) localbest).getParticlePoints();
		int[] gbest = ((polygonSolution) globalBest).getParticlePoints();
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

		double[] vi = ((polygonSolution) currentSolution).getVelocity();

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
			// System.out.println("velcoity s "+vi[j]);
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
			// System.out.println("velcoity computeed fianal is "+vi[j]+" and r3
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
		((polygonSolution) currentSolution).setParticlePoints(pi);
		((polygonSolution) currentSolution).setVelocity(vi);

		((polygonSolution) currentSolution).refineSolution();

		((polygonSolution) currentSolution).calculateSolutionParameters();

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