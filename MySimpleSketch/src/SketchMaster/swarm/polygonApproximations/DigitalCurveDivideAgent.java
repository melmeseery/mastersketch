/**
 * 
 */
package SketchMaster.swarm.polygonApproximations;

import java.util.Random;

/**
 * @author maha
 * 
 */
public class DigitalCurveDivideAgent extends PolygonAgent {

	/**
	 * 
	 */
	public DigitalCurveDivideAgent() {

	}

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
//		Random  r1Random,r2Random,r3Random;
//		r1Random=new Random(11);
//		r2Random=new Random(22);
//		r3Random=new Random(33);
		r1 =r1Random.nextDouble();//Math.random();
		r2 =r2Random.nextDouble();
		r3 =r3Random.nextDouble();
		// now get the velocity array of the location

		double[] vi = ((PolygonSolution) currentSolution).getVelocity();

		double gbestterm, pbestterm;
		double templocation;
		for (int j = 0; j < pi.length; j++) {
			r1 =r1Random.nextDouble();//Math.random();
			r2 =r2Random.nextDouble();
			r3 =r3Random.nextDouble();
			// computer terms speratly
			pbestterm = C1 * r1 * (pbest[j] - pi[j]);
			gbestterm = C2 * r2 * (gbest[j] - pi[j]);

			// the velocity equaiton.
			vi[j] = w * vi[j] + pbestterm + gbestterm;
			// logger.info("velcoity s "+vi[j]);
		//	if (vi[j] >= (Vmax))
				// use s(v) to restrice the velocity to vmax
			//	vi[j] = 0.5 + (vi[j] / (2 * Vmax));
			if (vi[j]>0)
				vi[j] = 0.5 + (vi[j] / (2.0 * Vmax));
			else {
				vi[j] = 0.5 + (vi[j] / (-2.0 * Vmax));
			}

			// the function pj = pj(old) +vj(new)
			templocation = vi[j];
			// logger.info("velcoity computeed fianal is "+vi[j]+" and r3
			// is "+r3);
			// now set the new location of the particle useing the eq 3 p 245 of
			// paper.
			if (templocation >= (r3))
				pi[j] = 1;
			else
				pi[j] = 0;

		}

		// now set the velocity and loction to the currentsolution
		((PolygonSolution) currentSolution).setParticlePoints(pi);
		((PolygonSolution) currentSolution).setVelocity(vi);
		((DigitalCurveDivideSolution) currentSolution).refineSolution();
		((DigitalCurveDivideSolution) currentSolution)
				.calculateSolutionParameters();

	}

}
