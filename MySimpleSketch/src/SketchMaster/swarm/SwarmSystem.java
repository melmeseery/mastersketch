package SketchMaster.swarm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;



import SketchMaster.io.log.FileLog;
import SketchMaster.system.SystemSettings;

public class SwarmSystem {
	/* {src_lang=Java} */
	private static final Logger logger = Logger.getLogger(SwarmSystem.class);
	private static final  Logger logE=Logger.getLogger("ExampleLogging");;
	private static int MaxIteration = SystemSettings.SWARM_SYSTEM_MAX_ITERATION;// was
			public static boolean DEBUG_SWARM=false;																	// 500
        ArrayList x;
    	protected static Random rRandom=new Random(100);
	/**
	 * performe a one iterateion of particle swarm algorithm where first
	 * evaluate the all agents then record global best and then move all agents
	 * to the new locations.
	 */
	private void moveAgents() {

	}

	/**
	 * Initalize the first population of the swarm system
	 */
	public void InitalizeSwarms() {
		globalBest = null;
		//  logger.trace(" Initalize the swarm sysstem");

	}

	/**
	 * Called every iteration to check if the system reached a solution or a
	 * stabilization level to quit iterations or no.
	 */
	private void checkStabilization() {
	}
  
//	 public ArrayList<Solution>  SolveMultiSwarm(ArrayList<Agent[]> Agents, ArrayList<Solution> InitialSolution, int[] minmax, int maxIterations){
//			// firstly intialize.
//			InitalizeSwarms();
//			if (maxIterations!=-1)
//				MaxIteration=maxIterations;
//			double[] currentFit=new double [InitialSolution.size()];
//			double[] localFit=new double[InitialSolution.size()];
//			double []GlobalFit=new double[InitialSolution.size()];
//
//			//  logger.trace("In solve problem--------------------");
//			//get number of the agends 
//			 ArrayList<Solution> globalBestArray=new ArrayList<Solution>();
//			for(int i=0;i<InitialSolution.size();i++){
//			// let global best is the initial solution.
//				globalBest = InitialSolution.get(i);
//				globalBestArray.set(i, globalBest);
//				GlobalFit[i] = globalBest.eval();
//				Agents.get(i)[0].setGlobalBest(InitialSolution.get(0));
//			}
//			// //let local best of all swarms the inital solution.
//			// for (int j = 0; j < Agents.length; j++) {
//			// Agents[j].setLocalbest(InitialSolution);
//			// }
//			double fit;
//			int countfit = 0;
//			int count = 0;
//           
//			for (int i = 0; i < MaxIteration; i++) {
//
//				for(int j=0;j<InitialSolution.size();i++){
//					
//				// logger.info(" in iteration "+ i);
//				GlobalFit [j]=( globalBestArray.get(j)).eval();
//				}
//				//
//				// logger.info("global best "+GlobalFit);
//				// for each particle do the following
//				for (int j = 0; j < Agents.get(0).length; j++) {
//					// evaluate the current fnalysisitneess
//					currentFit = Agents.get(0)[j].getCurrentFitness();
//					// if (Double.isNaN(currentFit))
//					// logger.info(" the fitness is nan at iteration"+i+" nd
//					// agent "+j);
//					// //
//					// if (i==0)
//					// {
//					// logger.info("agent "+ j +" = "+currentFit);
//					// }
//					// change local best if applicable
//					localFit = Agents[j].getLocalbest().eval();
//					if (minmax == 1) // i found solution at max fittness
//					{
//						if (currentFit > localFit) {
//							// logger.info("changing the local");
//							// change local of this particle
//							Agents[j].setLocalbest((Solution) Agents[j]
//									.getCurrentSolution().clone());
//						}
//
//						// change global best if applicable
//						if (currentFit > GlobalFit) {
//							// change global fitness ,
//							Agents[j].setGlobalBest((Solution) Agents[j]
//									.getCurrentSolution().clone());
//							globalBest = (Solution) Agents[j].getCurrentSolution()
//									.clone();
//							// calculate global fitness again
//							GlobalFit = currentFit;
//							countfit++;
//							// logger.info("Changing global solution
//							// Iteration "+i);
//							// /logger.info("global best "+GlobalFit);
//							//  logger.trace("Changing global solution  Iteration "
//									//		+ i);
//
//						}
//					}
//
//					if (minmax == 0)// ia m minizing the fitness function
//					{
//						if (currentFit < localFit) {
//							// logger.info("changing the local");
//							// change local of this particle
//							Agents[j].setLocalbest((Solution) Agents[j]
//									.getCurrentSolution().clone());
//						}
//
//						// change global best if applicable
//						if (currentFit < GlobalFit) {
//							// change global fitness ,
//							Agents[j].setGlobalBest((Solution) Agents[j]
//									.getCurrentSolution().clone());
//							globalBest = (Solution) Agents[j].getCurrentSolution()
//									.clone();
//
//							// calculate global fitness again
//							GlobalFit = currentFit;
//							countfit++;
//							//  logger.trace("Changing global solution  Iteration "
//									//		+ i);
//							// logger.info("Changing global solution
//							// Iteration "+i);
//							// logger.info("the fit="+GlobalFit);
//
//						}
//
//					}
//
//					// logger.info("---------------move agent"+j+" in
//					// iteration "+i);
//					// move particles
//					Agents[j].move();
//
//				}
//				// logger.info("_______________________________"+i+"___________________________________");
//				// this code count how many time the global solutin didnot change
//				if (countfit != 0) {
//
//					count++;
//					// reacheed the error at iteraton
//					if (count >   MaxIteration/4) {
//						// let xy
//						// //  logger.trace("stablize at = "+i);
//						// logger.info("y
//						// ="+((polygonSolution)globalBest).eTolerance);
//						// logger.info("The global fit"+GlobalFit);
//						count = 0;
//						countfit = 0;
//						return globalBest;
//					}
//					// ((polygonSolution)globalBest).eTolerance=((polygonSolution)globalBest).eTolerance*2.0;
//
//				}
//				countfit = 0;
//
//			}
//			// logger.info("gloal best is the following "+globalBest.eval());
//			// solve agents
//
//			return globalBest;
//	 }
//	
	/**
	 * Solve a problem using swarms system using a set of agents from inital
	 * solutions.
	 * 
	 * @param Agents
	 *            array of agents.
	 * @param InitialSolution
	 * @param minmax
	 *            00 if min 1 if maximize
	 * @return the found best solution for this problem
	 */
	public Solution solve(Agent[] Agents, Solution InitialSolution, int minmax, int maxIterations) {
		// firstly intialize.
		InitalizeSwarms();
		if (maxIterations!=-1)
			MaxIteration=maxIterations;
		double currentFit, localFit, GlobalFit;

		//  logger.trace("In solve problem--------------------");
		// let global best is the initial solution.
		globalBest = InitialSolution;
		GlobalFit = globalBest.eval();
		Agents[0].setGlobalBest(InitialSolution);

		// //let local best of all swarms the inital solution.
		// for (int j = 0; j < Agents.length; j++) {
		// Agents[j].setLocalbest(InitialSolution);
		// }
		double fit;
		int countfit = 0;
		int count = 0;

		for (int i = 0; i < MaxIteration; i++) {

			// logger.info(" in iteration "+ i);
			GlobalFit = globalBest.eval();
			if (i%10==0){
			if (SystemSettings.DEBUG_MODE){
				logE.info("Iteration "+i+ " the global best fitness is "+GlobalFit+" and error value is "+this.globalBest.getError()
						+"  and the particle is "+globalBest);
                // logE.info(")
				}
			}
			//
			// logger.info("global best "+GlobalFit);
			// for each particle do the following
			for (int j = 0; j < Agents.length; j++) {
				// evaluate the current fnalysisitneess
				currentFit = Agents[j].getCurrentFitness();
				// if (Double.isNaN(currentFit))
				// logger.info(" the fitness is nan at iteration"+i+" nd
				// agent "+j);
				// //
				// if (i==0)
				// {
				// logger.info("agent "+ j +" = "+currentFit);
				// }
				// change local best if applicable
				localFit = Agents[j].getLocalbest().eval();
				if (minmax == 1) // i found solution at max fittness
				{
					
					if (currentFit > localFit) {
						// logger.info("changing the local");
						// change local of this particle
						Agents[j].setLocalbest((Solution) Agents[j]
								.getCurrentSolution().clone());
					}

					// change global best if applicable
					if (currentFit > GlobalFit) {
						// change global fitness ,
						Agents[j].setGlobalBest((Solution) Agents[j]
								.getCurrentSolution().clone());
						globalBest = (Solution) Agents[j].getCurrentSolution()
								.clone();
						// calculate global fitness again
						GlobalFit = currentFit;
						countfit++;
						// logger.info("Changing global solution
						// Iteration "+i);
						// /logger.info("global best "+GlobalFit);
						//  logger.trace("Changing global solution  Iteration "
								//		+ i);

					}
				}

				if (minmax == 0)// ia m minizing the fitness function
				{
					if (currentFit < localFit) {
						// logger.info("changing the local");
						// change local of this particle
						Agents[j].setLocalbest((Solution) Agents[j]
								.getCurrentSolution().clone());
					}

					// change global best if applicable
					if (currentFit < GlobalFit) {
						// change global fitness ,
						Agents[j].setGlobalBest((Solution) Agents[j]
								.getCurrentSolution().clone());
						globalBest = (Solution) Agents[j].getCurrentSolution()
								.clone();

						// calculate global fitness again
						GlobalFit = currentFit;
						countfit++;
						//  logger.trace("Changing global solution  Iteration "
								//		+ i);
						// logger.info("Changing global solution
						// Iteration "+i);
						// logger.info("the fit="+GlobalFit);

					}

				}

				// logger.info("---------------move agent"+j+" in
				// iteration "+i);
				// move particles
				if (SystemSettings.USE_SWARM_MODIFICATION){
				 int pj  =rRandom.nextInt(Agents.length);//
				 int pi  =rRandom.nextInt(Agents.length);//
				
				 if (pj ==j){ while (pj==j) { pj  =rRandom.nextInt(Agents.length); }}
				 if (pi ==j){ while (pi==j) { pi  =rRandom.nextInt(Agents.length); }}
				
				 
				 
					
					Agents[j].move(Agents[pj].getCurrentSolution(),Agents[pi].getCurrentSolution());
					
					
				}
				else {
				Agents[j].move();
				}

			}
			// logger.info("_______________________________"+i+"___________________________________");
			// this code count how many time the global solutin didnot change
			if (countfit != 0) {

				count++;
				// reacheed the error at iteraton
				if (count >   (double)MaxIteration/10.0) {
					// let xy
					// //  logger.trace("stablize at = "+i);
					// logger.info("y
					// ="+((polygonSolution)globalBest).eTolerance);
					// logger.info("The global fit"+GlobalFit);
					count = 0;
					countfit = 0;
					//logger.setLevel(Level.WARN);
					//logger.warn("  Exiting after  "+i+" iterations  ");
					return globalBest;
				}
				// ((polygonSolution)globalBest).eTolerance=((polygonSolution)globalBest).eTolerance*2.0;

			}
			countfit = 0;

		}
		// logger.info("gloal best is the following "+globalBest.eval());
		// solve agents
		GlobalFit = globalBest.eval();
	 
		if (SystemSettings.DEBUG_MODE){
			logE.info(" After swarm  the global best fitness is "+GlobalFit+" and error value is "+this.globalBest.getError()
					+"  and the particle is "+globalBest);
			logE.info("----------------------------------------------------------------");
            // logE.info(")
			}
	 
		return globalBest;
	}

	/**
	 * the set of agents that are used in the current swarm system.
	 * 
	 * @uml.property name="agents"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     inverse="swarmSystem:SketchMaster.swarm.Agent"
	 * @uml.association name="TheAgents"
	 */
	private ArrayList<Agent> agents = new java.util.ArrayList();

	/**
	 * Getter of the property <tt>agents</tt>
	 * 
	 * @return Returns the agents.
	 * @uml.property name="agents"
	 */
	public ArrayList<Agent> getAgents() {
		return agents;
	}

	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index
	 *            index of element to return.
	 * @return the element at the specified position in this list.
	 * @see java.util.List#get(int)
	 * @uml.property name="agents"
	 */
	public Agent getAgents(int i) {
		return (Agent) agents.get(i);
	}

	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 * 
	 * @return an iterator over the elements in this list in proper sequence.
	 * @see java.util.List#iterator()
	 * @uml.property name="agents"
	 */
	public Iterator<Agent> agentsIterator() {
		return agents.iterator();
	}

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 * 
	 * @return <tt>true</tt> if this list contains no elements.
	 * @see java.util.List#isEmpty()
	 * @uml.property name="agents"
	 */
	public boolean isAgentsEmpty() {
		return agents.isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this list contains the specified element.
	 * 
	 * @param element
	 *            element whose presence in this list is to be tested.
	 * @return <tt>true</tt> if this list contains the specified element.
	 * @see java.util.List#contains(Object)
	 * @uml.property name="agents"
	 */
	public boolean containsAgents(Agent agent) {
		return agents.contains(agent);
	}

	/**
	 * Returns <tt>true</tt> if this list contains all of the elements of the
	 * specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this list.
	 * @return <tt>true</tt> if this list contains all of the elements of the
	 *         specified collection.
	 * @see java.util.List#containsAll(Collection)
	 * @uml.property name="agents"
	 */
	public boolean containsAllAgents(Collection<? extends Agent> agents) {
		return this.agents.containsAll(agents);
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list.
	 * @see java.util.List#size()
	 * @uml.property name="agents"
	 */
	public int agentsSize() {
		return agents.size();
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence.
	 * 
	 * @return an array containing all of the elements in this list in proper
	 *         sequence.
	 * @see java.util.List#toArray()
	 * @uml.property name="agents"
	 */
	public Agent[] agentsToArray() {
		return agents.toArray(new Agent[agents.size()]);
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence; the runtime type of the returned array is that of the specified
	 * array.
	 * 
	 * @param a
	 *            the array into which the elements of this list are to be
	 *            stored.
	 * @return an array containing all of the elements in this list in proper
	 *         sequence.
	 * @see java.util.List#toArray(Object[])
	 * @uml.property name="agents"
	 */
	public <T extends Agent> T[] agentsToArray(T[] agents) {
		return (T[]) this.agents.toArray(agents);
	}

	/**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation)
	 * 
	 * @param index
	 *            index at which the specified element is to be inserted.
	 * @param element
	 *            element to be inserted.
	 * @see java.util.List#add(int,Object)
	 * @uml.property name="agents"
	 */
	public void addAgents(int index, Agent agent) {
		agents.add(index, agent);
	}

	/**
	 * Appends the specified element to the end of this list (optional
	 * operation).
	 * 
	 * @param element
	 *            element to be appended to this list.
	 * @return <tt>true</tt> (as per the general contract of the
	 *         <tt>Collection.add</tt> method).
	 * @see java.util.List#add(Object)
	 * @uml.property name="agents"
	 */
	public boolean addAgents(Agent agent) {
		return agents.add(agent);
	}

	/**
	 * Removes the element at the specified position in this list (optional
	 * operation).
	 * 
	 * @param index
	 *            the index of the element to removed.
	 * @return the element previously at the specified position.
	 * @see java.util.List#remove(int)
	 * @uml.property name="agents"
	 */
	public Object removeAgents(int index) {
		return agents.remove(index);
	}

	/**
	 * Removes the first occurrence in this list of the specified element
	 * (optional operation).
	 * 
	 * @param element
	 *            element to be removed from this list, if present.
	 * @return <tt>true</tt> if this list contained the specified element.
	 * @see java.util.List#remove(Object)
	 * @uml.property name="agents"
	 */
	public boolean removeAgents(Agent agent) {
		return agents.remove(agent);
	}

	/**
	 * Removes all of the elements from this list (optional operation).
	 * 
	 * @see java.util.List#clear()
	 * @uml.property name="agents"
	 */
	public void clearAgents() {
		agents.clear();
	}

	/**
	 * Setter of the property <tt>agents</tt>
	 * 
	 * @param agents
	 *            the agents to set.
	 * @uml.property name="agents"
	 */
	public void setAgents(ArrayList<Agent> agents) {
		this.agents = agents;
	}

	/**
	 * The global best locaton of the swarm system.
	 * 
	 * @uml.property name="GlobalBest"
	 * @uml.associationEnd inverse="swarmSystem:SketchMaster.swarm.Solution"
	 * @uml.association name="SwarmSystem-globalBest"
	 */
	private Solution globalBest;

	/**
	 * Getter of the property <tt>GlobalBest</tt>
	 * 
	 * @return Returns the globalBest.
	 * @uml.property name="GlobalBest"
	 */
	public Solution getGlobalBest() {
		return globalBest;
	}

	/**
	 * Setter of the property <tt>GlobalBest</tt>
	 * 
	 * @param GlobalBest
	 *            The globalBest to set.
	 * @uml.property name="GlobalBest"
	 */
	public void setGlobalBest(Solution globalBest) {
		this.globalBest = globalBest;
	}

}