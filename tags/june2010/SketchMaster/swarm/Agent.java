package SketchMaster.swarm;

import java.util.Random;

/**
 * @author maha
 * 
 */
public abstract class Agent {
	
	
	protected static Random r1Random=new Random(11);
	protected 	static Random r2Random=new Random(22);
	protected	static Random r3Random=new Random(33);
	
	/*
	 * {null=Agent that is part of the swarm system. each agent is responisible
	 * for search on , src_lang=Java}
	 */
	/**
	 * @uml.property name="globalBest"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="polygonAgent:SketchMaster.swarm.Solution"
	 * @uml.association name="globalBestLocation"
	 */
	protected static Solution globalBest;

	/**
	 * Getter of the property <tt>globalBest</tt>
	 * 
	 * @return Returns the globalBest.
	 * @uml.property name="globalBest"
	 */
	public Solution getGlobalBest() {
		return globalBest;
	}

	/**
	 * Setter of the property <tt>globalBest</tt>
	 * 
	 * @param globalBest
	 *            The globalBest to set.
	 * @uml.property name="globalBest"
	 */
	public void setGlobalBest(Solution globalBest) {
		this.globalBest = globalBest;
	}

	/**
	 * perform single movement step of agent according to the agenst parameter
	 * and movement equations
	 * 
	 */
	abstract public void move();

	/**
	 * 
	 * @return evaluation of thhe current fitness value
	 */
	abstract public double getCurrentFitness();

	/**
	 * the local best location this agent have pased found
	 * 
	 * @uml.property name="localbest"
	 * @uml.associationEnd aggregation="composite"
	 *                     inverse="agent:SketchMaster.swarm.Solution"
	 * @uml.association name="localbest"
	 */
	protected Solution localbest;

	/**
	 * Getter of the property <tt>localbest</tt>
	 * 
	 * @return Returns the localbest.
	 * @uml.property name="localbest"
	 */
	public Solution getLocalbest() {
		return localbest;
	}

	/**
	 * Setter of the property <tt>localbest</tt>
	 * 
	 * @param localbest
	 *            The localbest to set.
	 * @uml.property name="localbest"
	 */
	public void setLocalbest(Solution localbest) {
		this.localbest = localbest;
	}

	/**
	 * @uml.property name="CurrentSolution"
	 * @uml.associationEnd multiplicity="(1 1)" aggregation="composite"
	 *                     inverse="CurrentSol:SketchMaster.swarm.Solution"
	 * @uml.association name="CurrentSolutionLocation"
	 */
	protected Solution currentSolution;

	/**
	 * Getter of the property <tt>CurrentSolution</tt>
	 * 
	 * @return Returns the currentSolution.
	 * @uml.property name="CurrentSolution"
	 */
	public Solution getCurrentSolution() {
		return currentSolution;
	}

	/**
	 * Setter of the property <tt>CurrentSolution</tt>
	 * 
	 * @param CurrentSolution
	 *            The currentSolution to set.
	 * @uml.property name="CurrentSolution"
	 */
	public void setCurrentSolution(Solution currentSolution) {
		this.currentSolution = currentSolution;
	}

	abstract public void move(Solution agent1, Solution agent2);

}