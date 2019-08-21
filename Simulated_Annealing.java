import java.util.ArrayList;

public class Simulated_Annealing {
	
/*************************************/// Adjust parameter
	double temperature = 10000;
	double cooling_factor = 0.999;
/*************************************/
	
	public ArrayList<Integer> current_state;
	public ArrayList<Integer> next_state;
	int running_time = 0;
	int Delta_E = 0;
	boolean check_fitness = false;
	
	public Simulated_Annealing() {
		
	}

	public boolean Simulated_Annealing(ArrayList<Integer> initial_state, int N, int runtime_limit) {
		
		// Current state = Initial State
		current_state = (ArrayList<Integer>) initial_state.clone();
		
//-----------------------------------------------------------------------------------------------------
//------ Simulated Annealing --------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------
		while(!check_fitness && running_time < runtime_limit) {
			
			// Generate next state
			current_state = succesor_generator(current_state, N);
			
			// decrease the temperature 
			temperature = temperature * cooling_factor;
		}
		
		// true: have solution <=> false: no solution
		if(check_fitness == true) { return true;}
		else {return false;}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// Generate and return the next state
	public ArrayList<Integer> succesor_generator(ArrayList<Integer> parent, int N){
		
		while (true) {
			
			// random select a queen and position
            int Col = (int) (Math.random() * N);
            int Row = 1 + (int) (Math.random() * N);
            
            int parent_fitness_val = fitness_function(parent);
            next_state = (ArrayList<Integer>) parent.clone();
            next_state.set(Col, Row);
            int next_fitness_val = fitness_function(next_state);
            
            // Calculate delta E
            Delta_E = next_fitness_val - parent_fitness_val;

	    		if(Delta_E > 0) {
	    			isGoal(next_state, N);
	    			return next_state;
	    		}
	    		
	    		else {
	    			double exp = Math.exp(Delta_E / temperature);
	    			double probability = ( exp > 1 ) ? 1 : exp;

                if (Math.random() < probability) {            		
                		return next_state;
                }   
	    		}  
		}
	}
	
	// fitness function
	public int fitness_function(ArrayList<Integer> state) {
			
			int fitness = state.size()*(state.size()-1)/2;
			
		    for(int i = 0; i<state.size(); i++)
		    {
			   for(int j=i+1; j<state.size(); j++) {
				   		if((state.get(i) == state.get(j))) {
				   			fitness--;
				   		}
				   		if(i - state.get(i) == j - state.get(j)) {
				   			fitness--;
				   		}
				   		if(i + state.get(i) == j + state.get(j)){
				   			fitness--;
				   		}
			   }
		    }
	
		    	return fitness;
		}
	
	// check the population is goal state or not
	public void isGoal(ArrayList<Integer> next_state, int boardsize) {
		
		int fitness = boardsize*(boardsize-1)/2;
		int current_fitness = fitness_function(next_state);

		if(current_fitness == fitness) {
			//System.out.print(next_state);
			//System.out.println("  fitness_val: " + current_fitness);
			check_fitness = true;
		}
		
	}
	
}


