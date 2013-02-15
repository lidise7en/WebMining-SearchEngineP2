package homework2;

import java.util.*;
import java.io.*;
/*
 * This class is designed to check whether a word is a stopword or not
 * 
 */
public class Stopwords {
	
	private Map<String,String> stop_map;// Store all the stopwords in this map and ready for O(1) search
	
	public Stopwords()// Constructor
	{
		stop_map = new HashMap<String,String>();
	}
	public void construct_map(String path)// read the stopword file and set up this map 
	{
		File file = new File(path);
		try
		{
			FileInputStream fis = new FileInputStream(file);     
	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        BufferedReader reader = new BufferedReader(isr);
	        String tempstr = new String("");
	        while((tempstr = reader.readLine()) != null)
	        {
	        	
	        	this.stop_map.put(tempstr, tempstr);
	        }
	        reader.close();
            isr.close();
            fis.close();
		}
		catch(IOException e)
		{
			System.out.println("The error!");
		}
		
		  
	}
	public Map<String,String> get_map()
	{
		return this.stop_map;
	}
	public static void main(String[] args){// this main is just for test 
		 Stopwords sw = new Stopwords();
		 sw.construct_map("resource/stoplist.txt");
		 System.out.println(sw.get_map().containsKey("paradise"));
		 System.out.println(sw.get_map().containsKey("still"));
	}

}
