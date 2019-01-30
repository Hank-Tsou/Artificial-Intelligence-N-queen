import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class main {	

	public static void main(String[] args) {
		
		ArrayList<ArrayList<Integer>> Initial_state = new ArrayList<ArrayList<Integer>>();
		
	/*************************************/// Adjust parameter
		int cases = 500;
		int runtime_limit = 10000;
		int population = 50;
		int N = 21;
	/*************************************/
		
		int PassCases = 0;
		long startTime;
		long endTime;
		boolean Result;

//-----------------------------------------------------------------------------------------------------
//----- Genetic Algorithm -----------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------
		System.out.println("-- Genetic Algorithm --");
		
/*GO*/  startTime = System.currentTimeMillis();
		
		for(int G = 1; G <= cases; G++) {
			
			Initial_state = randomGenerator(population, N);
			
			Genetic Genetic_Algorithm = new Genetic();
			Result = Genetic_Algorithm.Genetic(Initial_state, population, N, runtime_limit);
			
			if(Result == true) {
				PassCases++;
				}
			
			running_bar(G);
		}
		
/*END*/ endTime   = System.currentTimeMillis();

		System.out.println("Percentage: " + (double)PassCases/(double)cases*100 + " %");
		System.out.println("Run Time: " + (endTime - startTime) +" ms");
		System.out.println();
		PassCases = 0;
		
//-----------------------------------------------------------------------------------------------------
//----- Simulated_Annealing ---------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------
		System.out.println("-- Simulated Annealing --");
		
/*GO*/  startTime = System.currentTimeMillis();
		
		for(int S = 1; S <= cases; S++) {
			
			Initial_state = randomGenerator(1, N);
			
			Simulated_Annealing SA_Algorithm = new Simulated_Annealing();
			Result = SA_Algorithm.Simulated_Annealing(Initial_state.get(0), N, runtime_limit);
			
			if(Result == true) {
				PassCases++;
				}
			
			running_bar(S);
		}
		
/*END*/ endTime   = System.currentTimeMillis();
		
		System.out.println("Percentage: " + (double)PassCases/(double)cases*100 + " %");
		System.out.println("Run Time: " + (endTime - startTime) +" ms");
//////////////////////////////////////////////////////////////////////////////////////////////////////
		
	}


//-----------------------------------------------------------------------------------------------------
//---- Generate random initial state ------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------

	public static ArrayList<ArrayList<Integer>> randomGenerator(int population, int N)
	{
		ArrayList<Integer> rndNumbers;
		ArrayList<ArrayList<Integer>> Initial_state = new ArrayList<ArrayList<Integer>>();
		
		for(int p=0; p<population; p++)
		{
			rndNumbers = new ArrayList<Integer>();
			
			for (int i=0; i<N; i++) 
			{
				rndNumbers.add(ThreadLocalRandom.current().nextInt(1, N+1));
			}	
			
			Initial_state.add(rndNumbers);
		}
		
		return Initial_state;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////


//-----------------------------------------------------------------------------------------------------
//---- Running bar >> >> >> >> >> just for tracking the running process. ------------------------------
//-----------------------------------------------------------------------------------------------------
	public static void running_bar(int step) {
		
		if(step % 10 == 0 ) {
			System.out.print(">> ");
		}
		
		if(step % 100 == 0 ) {
			System.out.print("  " + step + " cases");
			System.out.println();
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////

}
