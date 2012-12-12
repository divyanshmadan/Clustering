/* 
 * Data Mining and Bioinformatics Project 2
 * Team Members: Anup Shahi, Divyansh Madan, Jayanti Dabhere
 * Problem Statement: Implementation of Clustering Algorithms  
 * File: Hierarchical Clustering Algorithm 
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;


public class Hierarchical {

	static float[][] dataMatrix=new float[400][16];
	static HashMap<String,Float> distanceMap=new HashMap<String,Float>();
	static int level=2;
	static TreeSet<Float> sortDifference= new TreeSet<Float>();
	static int count=0;
	public static void main(String args[]) throws IOException{
		Scanner userInput = new Scanner(System.in);
		String file_name;
		
		//User Interface for Input
		System.out.println("*********** IMPLEMENTATION OF HIERARCHICAL CLUSTERING ALGORITHM **********");
		System.out.println("Enter the filename for data set: ");
		file_name=userInput.next();
		
		//Getting data matrix and calculating distances between each
		getData(file_name);
		calculateDistances();
		
		//Check the minimum distances and print levels according to it
		printLevels();
		
		
		/*for (String key : distanceMap.keySet() ) {
			// Get the String value that goes with the key
			float value = distanceMap.get( key );
			System.out.println( key +"="+value);
			count++;
		}*/
		
		//System.out.println( count);
		
		/*for(int i=0;i<386;i++){
			for(int j=0;j<16;j++)
			System.out.print(dataMatrix[i][j]+" ");		
			System.out.println();
		}*/
		
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
	
	public static void calculateDistances(){
		
		for(int i=0;i<386;i++)
		{
			for(int j=i+1;j<386;j++){	
				float value=0;
				for(int k=0;k<16;k++){					
					value+=Math.abs(dataMatrix[i][k]-dataMatrix[j][k]);
				}					
				String my_key=String.valueOf(i)+"with"+String.valueOf(j);
				distanceMap.put(my_key, value);
				sortDifference.add(value);
			}
		}
//		for (String keydis :distanceMap.keySet())
//		{
//			System.out.println(keydis);
//		}
//		System.out.println("final count " + count);
	}
	
	public static void printLevels(){		
		int next=385;		
		System.out.println("Printing All the Levels:");
		System.out.println("******************************** LEVEL 1 **********************************");
		
		for(int i=0;i<386;i++)System.out.print("{"+i+"}, ");		
		HashSet<String> levelSet= new HashSet<String>();	
		Map<Integer,Integer> clusterBuffer = new HashMap<Integer,Integer>();
		
		for(int a=0;a<386;a++)
		{
			clusterBuffer.put(a,a);
		}				
		Iterator<Float> sortValues = sortDifference.iterator();
		
//		while(sortValues.hasNext())
//		{
//			//System.out.println(sortValues.next());
//		}		
		//Printing all other levels in iteration
		int cluster_count=0;
		while(level!=6 )
		{
		
		cluster_count=0;	
		Float min_value=sortValues.next();
		System.out.println();
		System.out.println("******************************** LEVEL" + level +" **********************************");
		for (String key : distanceMap.keySet() ) {
			// Get the String value that goes with the key
			float value = distanceMap.get( key );
			if (key != null)
			{
			if(value==min_value) {
				String[] elements=key.split("with");
				levelSet.add("{"+elements[0]+","+elements[1]+"}");	
				next++;
				clusterBuffer.put(next,next);
				if(clusterBuffer.get(Integer.parseInt(elements[0])) == Integer.parseInt(elements[0]))
				{
					clusterBuffer.put(Integer.parseInt(elements[0]),next);
				}
				else
				{
					if (clusterBuffer.get(Integer.parseInt(elements[0]))!=null)
					clusterBuffer.put(clusterBuffer.get(Integer.parseInt(elements[0])),next);
					
					int value_element0=0;
					 for (Integer keyBuffer : clusterBuffer.keySet())
						{
						   value_element0=clusterBuffer.get(keyBuffer);
							if(value_element0==clusterBuffer.get(Integer.parseInt(elements[0])))
									{
	                               						
							        
									clusterBuffer.put(keyBuffer, next);
								}
					}
				  
					clusterBuffer.put(Integer.parseInt(elements[0]),next);
				}
				
//				if(level==8)
//				{	
//					System.out.print("{");
//					for (Integer keyBuffercluster : clusterBuffer.keySet())
//					{
//						
//						
//							
//								System.out.println(keyBuffercluster+"," +clusterBuffer.get(keyBuffercluster));
//								
//						
//					}
//					System.out.print("}");	
//				}
				
				if(clusterBuffer.get(Integer.parseInt(elements[1])) == Integer.parseInt(elements[1]))
				{
					clusterBuffer.put(Integer.parseInt(elements[1]),next);
				}
				else
				{
					if (clusterBuffer.get(Integer.parseInt(elements[1]))!=null)
					clusterBuffer.put(clusterBuffer.get(Integer.parseInt(elements[1])),next);
					
					
					int val=clusterBuffer.get(Integer.parseInt(elements[1]));
					int value_element1=0;
					 for (Integer keyBuffer : clusterBuffer.keySet())
						{
						 value_element1=clusterBuffer.get(keyBuffer);
						 
						 
							if(value_element1==val)
									{
	                               						
							       
									clusterBuffer.put(keyBuffer, next);
								}
					}
					
					clusterBuffer.put(Integer.parseInt(elements[1]),next);
				}
			
			
			
			//Printing the new clusters	
				
			int label=0;
			int bracket_flag=0;
			
			while(label<=next)
			{
				
				bracket_flag=0;
				for (Integer keyBuffercluster : clusterBuffer.keySet())
				{
				
				  if(!(keyBuffercluster==null))
				  {	  
					if(keyBuffercluster<=385)
					{	  
						int value_cluster=clusterBuffer.get(keyBuffercluster);		
					
						if(value_cluster ==(label))
						{
							bracket_flag++;
							if(bracket_flag==1)
								System.out.print("{");
							if(keyBuffercluster!=null&&keyBuffercluster!=next)
								System.out.print(keyBuffercluster+",");
							
						}
				 	
					}
				  }	
				}
				if(bracket_flag!=0)
				{	
					System.out.print("}");
					cluster_count++;
				}	
				
					
				label++;
					
				
			}
			
			
			}
				
				
			}
			
			
		}
		
		level++;
		}
		
		
						
    }
	
}