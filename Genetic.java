import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;


public class Genetic {
	
/*************************************/// Adjust parameter
	int mutate_prob = 5;
/*************************************/
	
	private ArrayList<Node> current_state = new ArrayList<Node>();
	private ArrayList<Node> child_state = new ArrayList<Node>();
	boolean check_fitness = false;
	int running_time = 0;
	int fitness_value;
	
	public Genetic() {
		
	}
	
	public boolean Genetic(ArrayList<ArrayList<Integer>> initial_state, int population, int N, int runtime_limit) {
		
		// Current state = Initial state
		for(int i=0; i<initial_state.size(); i++) {
			Node state = new Node(initial_state.get(i), fitness_function(initial_state.get(i)));
			current_state.add(state);
		}
		
//-----------------------------------------------------------------------------------------------------
//----- Genetic Algorithm -----------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------
		
		while (!check_fitness && running_time < runtime_limit) {
			
			// Selection: "select" two parents with some probability 
			// ( in order to improve the result so I apply some probability to the child )
			for(int i=0; i<population; i++) {
				ArrayList<Integer> child_1 = Selection(current_state);
				ArrayList<Integer> child_2 = Selection(current_state);
				
				while(child_1.equals(child_2)){
					child_1 = Selection(current_state);
					child_2 = Selection(current_state);
				}
				
				// "Generate child" with "mutate function" (the mutate function is in generate process) 
				Child_Generator(child_1, child_2);
			}
			
			// Sort the new population
			Priority mc = new Priority();
			Collections.sort(child_state,mc);
			
			// Select the top N population 
			current_state = new ArrayList<Node>(child_state.subList(0, population));
			
			// Check the result
			isGoal(current_state, N);
			
			
			running_time = running_time+1;
			child_state.clear();
		}
		
		// true: have solution <=> false: no solution
		if(check_fitness == true) { return true;}
		else {return false;}

	}
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Selection function with some probability 
	// (also can use random selection but just want to improve the result)
	public ArrayList<Integer> Selection(ArrayList<Node> current_state) {
		
		int sum = 0;
		
		double[] multiPros = new double[current_state.size()];
		
		for(int i=0; i<current_state.size(); i++) {
			sum = sum + current_state.get(i).fitness_value;
			multiPros[i] = current_state.get(i).fitness_value;
		}
		
		for(int i=0; i<current_state.size(); i++) {
			multiPros[i] = multiPros[i]/sum;
		}
		
		return current_state.get(nextDiscrete(multiPros)).state;	
	}
	
	// generate new population by select a random cut point 
	public void Child_Generator(ArrayList<Integer> child_x, ArrayList<Integer> child_y) {
		
		ArrayList<Integer> part_1;
		ArrayList<Integer> part_2;
		ArrayList<Integer> new_child;
		int cutPoint = (int)(Math.random()*((child_x.size()-2)+1))+1;

		part_1 = new ArrayList<Integer>(child_x.subList(0, cutPoint));
		part_2 = new ArrayList<Integer>(child_y.subList(cutPoint, child_y.size()));
		new_child = new ArrayList<Integer>();
		new_child.addAll(part_1);
		new_child.addAll(part_2);
		
		Node child_1 = new Node(mutate_function(new_child, mutate_prob), fitness_function(new_child));
		child_state.add(child_1);
		
		part_1 = new ArrayList<Integer>(child_y.subList(0, cutPoint));
		part_2 = new ArrayList<Integer>(child_x.subList(cutPoint, child_y.size()));
		new_child = new ArrayList<Integer>();
		new_child.addAll(part_1);
		new_child.addAll(part_2);
		
		Node child_2 = new Node(mutate_function(new_child, mutate_prob), fitness_function(new_child));
		child_state.add(child_2);
	}
	
	// mutation function
	public ArrayList<Integer> mutate_function(ArrayList<Integer> new_child, int mutate_prob) {
		
		int probability = ThreadLocalRandom.current().nextInt(1, mutate_prob);
		
		if (probability == 1) {
			
			int column = ThreadLocalRandom.current().nextInt(0, new_child.size());
			int row = ThreadLocalRandom.current().nextInt(1, new_child.size()+1);

			new_child.set(column, row);

			return new_child;
		}
		
		return new_child;
	}
	
	// fitness function
	public int fitness_function(ArrayList<Integer> state) {
		
		int fitness = state.size()*(state.size()-1)/2;
		
	    for(int i = 0; i<state.size(); i++){

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
	public void isGoal(ArrayList<Node> child_state, int boardsize) {
		
		int fitness = boardsize*(boardsize-1)/2;
		
		for(int i=0; i<child_state.size(); i++) {
			if(child_state.get(i).fitness_value == fitness) {
				//System.out.print(child_state.get(i).state);
				//System.out.println("  fitness_val: " + child_state.get(i).fitness_value);
				check_fitness = true;
			}
		}
	}
	
//----------------------------------------------------------------------------------------------------	
//---- Support Function ------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------	
	
	// Calculate the probability for selection function
	public static int nextDiscrete(double[] probs)
    {
        double sum = 0.0;
        for (int i = 0; i < probs.length; i++)
            sum += probs[i];

        double r = Math.random()  * sum;
        sum = 0.0;
        for (int i = 0; i < probs.length; i++) {
            sum += probs[i];
            if (sum > r)
                return i;
        }
        return probs.length - 1;
    }
	
	// for sorting the new population
	private class Priority implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            int num1 = n1.fitness_value;
            int num2 = n2.fitness_value;
            return num2 - num1;
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////

	
//-----------------------------------------------------------------------------------------------------
//---- State Node -------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------
	private class Node {
        private ArrayList<Integer> state;
        private int fitness_value;

        public Node(ArrayList<Integer> state, int fitness_value) {
            this.state = state;
            this.fitness_value = fitness_value;
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////

}
