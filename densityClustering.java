/* 
 * Data Mining and Bioinformatics Project 2
 * Team Members: Anup Shahi, Divyansh Madan, Jayanti Dabhere
 * Problem Statement: Implementation of Clustering Algorithms  
 * File: Density Based Clustering Algorithm 
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class densityClustering {

	static float[][] dataMatrix=new float[400][16];		
	static int level=2;	
	static int count=0;
	static int nextValue=0;
	static float epsilon=0;
	static int minimumPoints =0;	
	static Map<Integer, Integer> visitedMap = new HashMap<Integer, Integer>();
	static TreeSet<Integer> noisePoints=new TreeSet<Integer>();
	static String[] finalClusters=new String[386];
	static TreeSet<Integer> clusterPoints=new TreeSet<Integer>();
	static int clusterCount=0;
	
	public static void main(String args[]) throws IOException{
		Scanner userInput = new Scanner(System.in);
		String file_name;
		
		//User Interface for Input
		System.out.println("*********** IMPLEMENTATION OF DENSITY BASED CLUSTERING ALGORITHM **********");
		System.out.println("Enter the filename for data set: ");
		file_name=userInput.next();
		System.out.println("Enter the value of epsilon: ");
		epsilon=userInput.nextFloat();
		System.out.println("Enter the value of Minimum points: ");
		minimumPoints=userInput.nextInt();
		
		//Getting data matrix and calculating distances between each
		getData(file_name);
		
		//Initialize visited Map with 0 for all elements	
		for(int i=0;i<=385;i++){
			visitedMap.put(i, 0);
		}
		
		dbScan();
		
		System.out.println("************* Final Clusters ************* ");
		System.out.println("Total number of Clusters: "+clusterCount);
		
        for(int i=1;i<=clusterCount;i++){
        	  System.out.println("{ "+finalClusters[i]+" }");	
        }		
        
        System.out.println("************* Noise Points ************* ");
        System.out.println("Total number of Noise Points: "+noisePoints.size());
        
        Iterator<Integer> set_noisepts=noisePoints.iterator();
        while(set_noisepts.hasNext()){
        	System.out.println(set_noisepts.next());
        }
        
	}
	
	public static void dbScan()	
	{
		
		TreeSet<Integer> neighborPoints=new TreeSet<Integer>();
	    
		Iterator<Map.Entry<Integer,Integer>> it = visitedMap.entrySet().iterator();  
        Map.Entry<Integer,Integer> entry;  
        while (it.hasNext()) {  
        	entry = it.next();
        	
        	if(entry.getValue()==1)
        	continue;
        	else
        	visitedMap.put(entry.getKey(), 1);
        	
        	//Get neighbors according to epsilon
        	neighborPoints=calculateDistances(entry.getKey(),neighborPoints);
        	
        	if(neighborPoints.size()<minimumPoints)
        		noisePoints.add(entry.getKey()); 
        	else
        	{
        		clusterCount++;
        		int P=entry.getKey();	
        		expandCluster(P,neighborPoints);	
        	}
              
        } 		
	}
	
	
	public static void getData(String fileName) throws IOException{

		BufferedReader inFile = new BufferedReader(new FileReader(fileName));		
		int line_counter=0;
		String current_line;
		while((current_line=inFile.readLine()) != null){			
			StringTokenizer items = new StringTokenizer(current_line, "\t");	
			items.nextToken();items.nextToken();
            int column_count=0;
			while(items.hasMoreTokens()){
				String token=items.nextToken();
				dataMatrix[line_counter][column_count]=Float.parseFloat(token);
				column_count++;		
			}
			line_counter++;
		}	
	}
	
	public static TreeSet<Integer> calculateDistances(int P,TreeSet<Integer> distanceSet){
		
		distanceSet.clear();
		
		for(int i=0;i<386;i++)
		{
			float value=0;
			for(int j=0;j<16;j++)
			{
				value+=Math.abs(dataMatrix[P][j]-dataMatrix[i][j]);
			}
			if(value<=epsilon)
			distanceSet.add(i);					
		}
		return distanceSet;
		
	}
	public static void expandCluster(int P,TreeSet<Integer> neighborPoints)
	{	
		TreeSet<Integer> neighborPointsExpand=new TreeSet<Integer>();	
		//Copy of neighbor points-TEMPERORY
		TreeSet<Integer> neighborPointsCopy=new TreeSet<Integer>(neighborPoints);
		
		//Add the point to final Cluster array
		String clusterPoint=String.valueOf(P);
		if(finalClusters[clusterCount]==null)finalClusters[clusterCount]="";
		finalClusters[clusterCount]+=clusterPoint+",";
		
		
		Iterator<Integer> set_it=neighborPoints.iterator();
        while (set_it.hasNext()) {
            
        	int entry=set_it.next();
        	//If not visited then make it visited
        	
        	//System.out.println("Entry for null: "+entry);        
         	if(visitedMap.get(entry)==0)
         	{
         		visitedMap.put(entry, 1);
         		neighborPointsExpand=calculateDistances(entry,neighborPointsExpand);
         		if(neighborPointsExpand.size()>=minimumPoints){         
         			neighborPointsCopy.addAll(neighborPointsExpand);
         		}
         	}
         	
         	neighborPoints=new TreeSet<Integer>(neighborPointsCopy);
         	
         	//If the point is not in any of clusters, then add it to existing cluster
         	int present=0;
         	for(int i=1;i<=clusterCount;i++){
         		if(finalClusters[i].contains(String.valueOf(entry)+",")){
         			present=1;
         			break;
         		}
         	}
         	if(present==0){
         		String clusterPointdash=String.valueOf(entry);
         		if(finalClusters[clusterCount]==null)finalClusters[clusterCount]="";
        		finalClusters[clusterCount]+=clusterPointdash+",";
         	}
         	
        }
	}

}