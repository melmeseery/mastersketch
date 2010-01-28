/**
 * 
 */
package SketchMaster.swarm.curvefit;

import java.util.Random;

import SketchMaster.swarm.polygonApproximations.polygonAgent;
import SketchMaster.swarm.polygonApproximations.polygonSolution;

/**
 * @author maha
 */
public class StrokeCurveAgent extends polygonAgent {

	/**
	 * 
	 */
	public StrokeCurveAgent() {

		C1 = 2.0;
		C2 = 2.0;
		w = 1.0;
		Vmax = 10.0;

	}

	public double getCurrentFitness() {
		return currentSolution.eval();
	}

	@Override
	public void move() {
		// System.out.println("----------------------------------------------------------------");
		// get the array of variables of pi , local best and global best
		double[] pi = ((StrokeCurveSolution) currentSolution)
				.getParticlesPoints();

		double[] pbest = ((StrokeCurveSolution) localbest).getParticlesPoints();
		double[] gbest = ((StrokeCurveSolution) globalBest)
				.getParticlesPoints();
		//		
		// double [] pi=null;
		// double[] pbest=null;
		// double [] gbest=null;

		// generate the random numbers
		double r1, r2;// ,r3;

//		Random  r1Random,r2Random,r3Random;
//		r1Random=new Random(11);
//		r2Random=new Random(22);
		double[] Oldpi = ((StrokeCurveSolution) currentSolution)
				.getParticlesPoints();
		double[] Oldvi = ((StrokeCurveSolution) currentSolution).getVelocity();

		// r3=Math.random();
		// now get the velocity array of the location

		double[] vi = ((StrokeCurveSolution) currentSolution).getVelocity();

		double gbestterm, pbestterm;
		boolean repeatTrue = false;
		// do {
		// repeatTrue=false;
		pi = ((StrokeCurveSolution) currentSolution).getParticlesPoints();
		vi = ((StrokeCurveSolution) currentSolution).getVelocity();

		// double templocation;
		for (int j = 0; j < pi.length; j++) {
			r1 =r1Random.nextDouble();//Math.random();
			r2 =r2Random.nextDouble();

			// computer terms speratly
			pbestterm = C1 * r1 * (pbest[j] - pi[j]);
			gbestterm = C2 * r2 * (gbest[j] - pi[j]);

			// the velocity equaiton.
			vi[j] = w * vi[j] + pbestterm + gbestterm;
		//	if (vi[j] > Vmax)
				// use s(v) to restrice the velocity to vmax
		//		vi[j] = 0.5 + (vi[j] / (2.0 * Vmax));
			
			
			if (vi[j]>0)
				vi[j] = 0.5 + (vi[j] / (2.0 * Vmax));
			else {
				vi[j] = 0.5 + (vi[j] / (-2.0 * Vmax));
			}

			// the function pj = pj(old) +vj(new)
			pi[j] = pi[j] + vi[j];
			// // now set the new location of the particle useing the eq 3 p 245
			// of paper.
			// if (templocation>=r3)
			// pi[j]=1;
			// else
			// pi[j]=0;
		}

		// if (pi[0]<0)
		// pi[0]=-pi[0];
		// now set the velocity and loction to the currentsolution
		((StrokeCurveSolution) currentSolution).setParticlesPoints(pi);
		((polygonSolution) currentSolution).setVelocity(vi);
		((StrokeCurveSolution) currentSolution).calculateSolutionParameters();

		((StrokeCurveSolution) currentSolution).refineSolution();

		// if (!((StrokeCurveSolution)currentSolution).checkSolution()){
		// repeatTrue=true;
		// //now set the velocity and loction to the currentsolution
		// //((StrokeCurveSolution)currentSolution).radomizeSolution();
		//			
		// // ((StrokeCurveSolution)currentSolution).setParticlesPoints(Oldpi);
		// // ((polygonSolution)currentSolution).setVelocity(Oldvi);
		// //
		// ((StrokeCurveSolution)currentSolution).calculateSolutionParameters();
		//			
		// // System.out.println(" i a repeating ");
		// }

		// }while(repeatTrue);
		//		
		// //now set the velocity and loction to the currentsolution
		// ((StrokeCurveSolution)currentSolution).setParticlesPoints(pi);
		// ((polygonSolution)currentSolution).setVelocity(vi);
		// ((StrokeCurveSolution)currentSolution).calculateSolutionParameters();

	}

}
