package SketchMaster.swarm;

import java.util.Random;

import org.apache.log4j.Logger;

/**
 * @author maha Represent a a single solution the agent may represent at any
 *         given time.
 */
public abstract class Solution implements Comparable, Cloneable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Solution.class);

	
	static protected Random  r1Random=new Random(55);
	/*
	 * {null=represent a valid solution for a problem the swarms solve. ,
	 * src_lang=Java}
	 */

	public Solution() {

	}

	public Solution(int size) {

		velocities = new double[size];
		// now itialize the values with random parameter
		//Random  r1Random;
		//r1Random=new Random(size);
		for (int i = 0; i < velocities.length; i++) {
			velocities[i] = r1Random.nextDouble();
		}

	}

	protected double Error = 0.0;

	public double getError() {
		return Error;
	}

	public  abstract double getComparableError();
//		return Double.MAX_VALUE;
//	}
	/**
	 * evaluate the current solution [the higher the value the better the
	 * solution]
	 * 
	 * @return the evaluation of the solution .
	 */
	abstract public double eval();

	/**
	 * calculate approximate error of the solution [the solution must minimize
	 * the error]
	 * 
	 * @return the value of the error for the solution.
	 */
	abstract public double error();

	/**
	 * The velocity of the location of particle at a specific location
	 * 
	 * @uml.property name="Velocity" multiplicity="(0 -1)" dimension="1"
	 */
	protected double[] velocities;

	/**
	 * Getter of the property <tt>Velocity</tt>
	 * 
	 * @return Returns the velocities.
	 * @uml.property name="Velocity"
	 */
	public double[] getVelocity() {
		return velocities;
	}

	/**
	 * Setter of the property <tt>Velocity</tt>
	 * 
	 * @param Velocity
	 *            The velocities to set.
	 * @uml.property name="Velocity"
	 */
	public void setVelocity(double[] velocity) {
		velocities = velocity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() {

		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {

			logger.error("clone()", e); //$NON-NLS-1$
			return null;
		}
	}

}