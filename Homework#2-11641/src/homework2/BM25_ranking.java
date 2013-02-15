package homework2;
import homework2.Tree;
import homework2.TreeNode;
import homework2.Result_Pair;
import java.io.*;
import java.util.*;
import java.lang.*;
/*
 * This class is to implement BM25 ranking with operator #SUM #NEAR #UW
 */
public class BM25_ranking {

	private String query;//a query provided by users
	private Tree query_tree;//query tree constructed with query
	private int operator_counter;// count the number of operators in query 
	private TreeNode RootNode;//root of this query tree
	
	
	private ArrayList<Integer> store_docID = new ArrayList<Integer>();
	private ArrayList<Double> store_docScore = new ArrayList<Double>();
	
	private ArrayList<Result_Pair> BM_final_result = new ArrayList<Result_Pair>();//This is used to store the first 100 results
	private double k1;
	private double b;
	private double k3;
	public BM25_ranking(String que,double k1,double b,double k3)//Constructor
	{
		this.query = que;
		this.query_tree = new Tree();
		this.RootNode = new TreeNode("this is root");
        this.query_tree.setRoot(this.RootNode);
		this.query_tree.ConstructTree(this.RootNode, this.query);
		this.operator_counter = 0;
		this.k1 = k1;
		this.b = b;
		this.k3 = k3;
	}
	public void set_query(String que)
	{
		this.query = que;
	}
	public TreeNode get_root()
	{
		return this.RootNode;
	}
	/*This function is designed to return proper results using the parsing tree*/
	public void Execute_BM25_ranking(TreeNode RootNode)
	{
		/*Handle #SUM*/ 
		if(RootNode.get_vl() == "SUM")
		{
			ArrayList<Result_Pair> final_list = new ArrayList<Result_Pair>();
			for(int i = 0;i < RootNode.get_child().size();i ++)
			{
				TreeNode sum_child = RootNode.get_child().get(i);
				/*if there is a NEAR inside the SUM operator */
				if((sum_child.get_vl().charAt(0) == 'N'))
				{
					int n_near = get_n(sum_child);
					Execute_NEAR(sum_child,n_near,"NEAR_result");
					File fix_tf = new File("resource/NEAR_result.txt");
					Fix_tf(fix_tf,"NEAR");
					
						
					File sum_file = new File("resource/finalNEAR.txt");
					
					try{
						FileInputStream fist = new FileInputStream(sum_file);     
					    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
					    BufferedReader reader = new BufferedReader(isrt);
					    String temp_str = new String();
					    int df = 0;
					   
					    
					    while((temp_str = reader.readLine()) != null)
					    {
					    	df ++;
					    }
					    
					    reader.close();
					    FileInputStream fistm = new FileInputStream(sum_file);     
					    InputStreamReader isrtm = new InputStreamReader(fistm, "UTF-8");
					    BufferedReader readert = new BufferedReader(isrtm);
					   
					    int qtf = 1;
					    int test = 0;
					    ArrayList<Result_Pair> temp_list = new ArrayList<Result_Pair>();
					    while((temp_str = readert.readLine()) != null)
					    {
					    	test ++;
					    	Calculate_BM25_SUM(temp_str,qtf,df,temp_list);
					    	
					    	
					    }
					    MergeList(final_list,temp_list);
					    readert.close();
					}
					catch(IOException e)
					{
						System.out.print("Error in SUM");
					}
					
				}
				/* if there is a #UW operator inside the #SUM */
				else if(sum_child.get_vl().charAt(0) == 'U')
				{
					int n_uw = get_n(sum_child);
					
					int child_num = sum_child.get_child().size();
					
					Execute_UW(sum_child,n_uw,"UW_result",child_num);
					File fix_tf = new File("resource/UW_result.txt");
					Fix_tf(fix_tf,"UW");
					
						
					File sum_file = new File("resource/finalUW.txt");
					
					try{
						FileInputStream fist = new FileInputStream(sum_file);     
					    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
					    BufferedReader reader = new BufferedReader(isrt);
					    String temp_str = new String();
					    int df = 0;
					   
					    
					    while((temp_str = reader.readLine()) != null)
					    {
					    	df ++;
					    }
					    
					    reader.close();
					    FileInputStream fistm = new FileInputStream(sum_file);     
					    InputStreamReader isrtm = new InputStreamReader(fistm, "UTF-8");
					    BufferedReader readert = new BufferedReader(isrtm);
					   
					    int qtf = 1;
					    int test = 0;
					    ArrayList<Result_Pair> temp_list = new ArrayList<Result_Pair>();
					    while((temp_str = readert.readLine()) != null)
					    {
					    	test ++;
					    	Calculate_BM25_SUM(temp_str,qtf,df,temp_list);
					    	
					    	
					    }
					    MergeList(final_list,temp_list);
					    readert.close();
					}
					catch(IOException e)
					{
						System.out.print("Error in SUM");
					}
				}
				/* no operator inside #SUM, so just calculate to score lists and combine them*/
				else
				{
					
						
					File sum_file = new File("resource/" + sum_child.get_vl() + ".inv");
					
					try{
						FileInputStream fist = new FileInputStream(sum_file);     
					    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
					    BufferedReader reader = new BufferedReader(isrt);
					    String temp_str = new String();
					    int df = 0;
					    reader.readLine();
					    
					    while((temp_str = reader.readLine()) != null)
					    {
					    	df ++;
					    }
					    
					    reader.close();
					    FileInputStream fistm = new FileInputStream(sum_file);     
					    InputStreamReader isrtm = new InputStreamReader(fistm, "UTF-8");
					    BufferedReader readert = new BufferedReader(isrtm);
					    readert.readLine();
					    int qtf = this.query_tree.get_num_count().get(this.query_tree.get_str_count().indexOf(sum_child.get_vl()));
					    int test = 0;
					    ArrayList<Result_Pair> temp_list = new ArrayList<Result_Pair>();
					    while((temp_str = readert.readLine()) != null)
					    {
					    	test ++;
					    	Calculate_BM25_SUM(temp_str,qtf,df,temp_list);
					    
					    	
					    }
					    MergeList(final_list,temp_list);
					    readert.close();
					}
					catch(IOException e)
					{
						System.out.print("Error in SUM");
					}
					
				}
				
			}
			//get sum_result and write it to file
			
			File output_file = new File("resource/SUM_result.txt");
			try{
				output_file.createNewFile();
				FileWriter fw =  new FileWriter(output_file);
				
				int p = 0;
				while(p < final_list.size())
				{
					
					int docID = final_list.get(p).get_docID();
					double docScore = final_list.get(p).get_docScore();
					if(this.BM_final_result.size() == 0)
					{
						Result_Pair r = new Result_Pair();
						r.set_docID(docID);
						r.set_docScore(docScore);
						this.BM_final_result.add(r);
					}
					else if(this.BM_final_result.size() == 100)
					{
						int i;
						for(i = this.BM_final_result.size() - 1;i >= 0;i --)
						{
							if(docScore < this.BM_final_result.get(i).get_docScore())
							{
								break;
							}
						}
						Result_Pair r = new Result_Pair();
						r.set_docID(docID);
						r.set_docScore(docScore);
						this.BM_final_result.add(i + 1, r);
						this.BM_final_result.remove(100);
					}
					else
					{
						int i;
						for(i = this.BM_final_result.size() - 1;i >= 0;i --)
						{
							if(docScore < this.BM_final_result.get(i).get_docScore())
							{
								break;
							}
						}
						Result_Pair r = new Result_Pair();
						r.set_docID(docID);
						r.set_docScore(docScore);
						this.BM_final_result.add(i + 1, r);
					}
					p ++;
				}
			
				if(this.BM_final_result.size() >= 100)
				{
					for(int j = 0;j <=99;j++)  
					{
						fw.write(this.BM_final_result.get(j).get_docID() + " " + this.BM_final_result.get(j).get_docScore() + "\n");
					}
				}
				else
				{
					for(int j = 0;j < this.BM_final_result.size();j++)  
					{
						fw.write(this.BM_final_result.get(j).get_docID() + " " + this.BM_final_result.get(j).get_docScore() + "\n");
					}
				}
				fw.close();
			}
			catch(IOException e)
			{
				System.out.print("Error in output file");
			}
			
		}
		/* if there is only #NEAR in the query*/
		else if(RootNode.get_vl().charAt(0) == 'N')
		{
			int n_near = get_n(RootNode);
			Execute_NEAR(RootNode,n_near,"NEAR_result");
			File fix_tf = new File("resource/NEAR_result.txt");
			Fix_tf(fix_tf,"NEAR");
		}
		/* if there is only #UW in the query*/ 
		else
		{
			int n_uw = get_n(RootNode);
			
			int child_num = RootNode.get_child().size();
			
			Execute_UW(RootNode,n_uw,"UW_result",child_num);
			File fix_tf = new File("resource/UW_result.txt");
			Fix_tf(fix_tf,"UW");
			
			
		
		}
	}
	/* Judge whether a list is to the end in the #UW implementation */
	public int JudgeEnd(ArrayList<String> str,int child_num)
	{
		for(int i = 0;i < child_num;i ++)
		{
			if(str.get(i) == null)
				return 0;
		}
		return 1;
	}
	/* Merge score lists in the #SUM implementation */
	public void MergeList(ArrayList<Result_Pair> final_list,ArrayList<Result_Pair> temp_list)
	{
		if(final_list.size() == 0)
		{
			for(int i = 0;i < temp_list.size();i ++)
			{
				Result_Pair newpair = new Result_Pair();
				newpair.set_docID(temp_list.get(i).get_docID());
				newpair.set_docScore(temp_list.get(i).get_docScore());
				final_list.add(newpair);
			}
		}
		else
		{
			int final_index = 0;
			int temp_index = 0;
			while(final_index < final_list.size() && temp_index < temp_list.size())
			{
				if(final_list.get(final_index).get_docID() < temp_list.get(temp_index).get_docID())
				{
					final_index ++;
				}
				else if(final_list.get(final_index).get_docID() > temp_list.get(temp_index).get_docID())
				{
					Result_Pair insertpair = new Result_Pair();
					insertpair.set_docID(temp_list.get(temp_index).get_docID());
					insertpair.set_docScore(temp_list.get(temp_index).get_docScore());
					final_list.add(final_index, insertpair);
					temp_index ++;
					final_index ++;
				}
				else
				{
					double t = final_list.get(final_index).get_docScore();
					final_list.get(final_index).set_docScore(t + temp_list.get(temp_index).get_docScore());
					temp_index ++;
					final_index ++;
				}
			}
			if(final_index == final_list.size() && temp_index != temp_list.size())
			{
				while(temp_index != temp_list.size())
				{
					Result_Pair temp_pair = new Result_Pair();
					temp_pair.set_docID(temp_list.get(temp_index).get_docID());
					temp_pair.set_docScore(temp_list.get(temp_index).get_docScore());
					final_list.add(temp_pair)
;					temp_index ++;
				}
			}
			else if(final_index != final_list.size() && temp_index == temp_list.size())
			{
				
			}
		}
	}
	/* Judge whether the current position in same docs are in a window */
	public int Judge_correct(ArrayList<ArrayList<Integer>> arr_pos,Integer[] arr_ptr,int child_num,int n_uw)
	{
		int flag = 1;
		for(int i = 0;i < child_num;i ++)
		{
			for(int j = 0;j < child_num;j ++)
			{
				if(Math.abs(arr_pos.get(i).get(arr_ptr[i]) - arr_pos.get(j).get(arr_ptr[j])) >= n_uw)
					flag = 0;
					
			}
		}
		if(flag == 1)
			return -1;
		int min = 0;
		for(int j = 1;j < child_num;j ++)
		{
			if(arr_pos.get(j).get(arr_ptr[j]) < arr_pos.get(min).get(arr_ptr[min]))
					min = j;
		}
		return min;
		
	}
	/* Judge whether the current pointers point to the same document */
	public int Judge_sameid(Integer docid[],int child_num)
	{
		int min = 0;
		int flag = 1;
		
		for(int n = 0;n < (child_num -1);n ++)
		{
			
			
			if(docid[n].intValue() != docid[n + 1].intValue())
				flag = 0;
			
		}
		
		if(flag == 1)
			return -1;// elements all the same
		
		
		for(int j = 1;j < child_num;j ++)
		{
			if(docid[j] < docid[min])
				min = j;
			
		}
		return min;
	}
	/* Judge whether position lists go to the end */
	public int Judge_arr_ptr(ArrayList<ArrayList<Integer>>pos_array,Integer[] arr_ptr,int child_num)
	{
		for(int i = 0;i < child_num;i ++)
		{
			if(arr_ptr[i] >= pos_array.get(i).size())//error happens
			{
				return -1;
			}
		}
		return 1;
	}
	/* use the formula the calculate specific BM25 scores */
	public void Calculate_BM25_SUM(String temp_str,int qtf,int df,ArrayList<Result_Pair> temp_list)
	{
		double BM_score;
		double docID;
		double tf;
		double d_length;
		int i = 0;
		while(temp_str.charAt(i) != ' ')
		{
			i ++;
		}
		docID = (double)Integer.parseInt(temp_str.substring(0, i));
		i ++;
		int j = i;
		while(temp_str.charAt(j) != ' ')
			j ++;
		tf = (double)Integer.parseInt(temp_str.substring(i,j));
		j ++;
		int m = j;
		while(temp_str.charAt(m) != ' ')
			m ++;
		d_length = (double)Integer.parseInt(temp_str.substring(j, m));
		
		double part1 = Math.log((double)(890630.0 - (double)df + 0.5)/(double)((double)df + 0.5));
		double part2 = tf/(double)(tf + (double)this.k1 * ((double)(1.0 - this.b) + (double)this.b*(double)(d_length/1301.0)));
		double part3 = (double)((k3 + 1) * (double)qtf) / (double)(k3 + (double)qtf);
		
		
		BM_score = part1 * part2 * part3;
		
		Result_Pair temp_pair = new Result_Pair();
		temp_pair.set_docID((int)docID);
		temp_pair.set_docScore(BM_score);
		temp_list.add(temp_pair);
		
		
		
	}
	/* Correct the tf in the inverted lists after #NEAR and #UW process */
	public void Fix_tf(File fix_tf,String type)
	{
		try{
		FileInputStream fist = new FileInputStream(fix_tf);     
	    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
	    BufferedReader readert = new BufferedReader(isrt);
	    
	    File final_file = new File("resource/" + "final" + type + ".txt");
	    FileWriter fw =  new FileWriter(final_file);
	    String tempstr = new String();
	    int temp = 0;
	    while((tempstr = readert.readLine()) != null)
	    {
	    	int count = 0;
	    	for(int i = 0;i < tempstr.length();i ++)
	    	{
	    		if(tempstr.charAt(i) == ' ')
	    			count ++; 
	    	}
	    	count -=2;
	    	
	    	int j;
	    	for(j = 0;tempstr.charAt(j) != ' ';j ++)
	    	{
	    		
	    	}
	    	String part1 = new String(tempstr.substring(0,j));
	    	j += 1;
	    	
	    	while(tempstr.charAt(j) != ' ')
	    	{
	    		j ++;
	    	}
	    	
	    	String part3 = new String(tempstr.substring(j + 1));
	    	fw.write(part1 + " " + count + " " + part3 + "\n");
	    	temp ++;
	    }
	   
	    fw.close();
	    readert.close();
		}
		catch(IOException e)
		{
			System.out.print("Error in fix_tf\n");
		}
	}
	/* The specific implementation of NEAR operator */
	public void Execute_NEAR(TreeNode RootNode,int n_near,String filename)
	{
		File result_file = new File("resource/" + filename+".txt");
		try{
			result_file.createNewFile();
			for(int i = 0;i < RootNode.get_child().size(); i ++)
			{
				if(i == 0)
				{
					BufferedWriter output = new BufferedWriter(new FileWriter(result_file));
					File file = new File("resource/" + RootNode.get_child().get(0).get_vl() + ".inv");//any problem??not sure
		    		FileInputStream fis = new FileInputStream(file);     
		 	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		 	        BufferedReader reader = new BufferedReader(isr);
		 	        String tempstr = new String("");
		 	        while((tempstr = reader.readLine()) != null)
		 	        {
		 	        	output.write(tempstr);
		 	        	output.write("\n");
		 	         }
		 	         output.close();
		 	         reader.close();
				}
				else
				{
					File temp_file = new File("resource/" + RootNode.get_child().get(i).get_vl() + ".inv");
		    		
					Exe_NEAR(result_file, temp_file,n_near);
				}
			}
		}
		catch(IOException e)
		{
			System.out.printf("Error in Execute_NEAR\n");
		}
	}
	/* The specific implementation of #UW operator */
	public void Execute_UW(TreeNode RootNode,int n_uw,String filename,int child_num)
	{
		ArrayList<BufferedReader> uw_buf = new ArrayList<BufferedReader>();
		ArrayList<String> uw_str = new ArrayList<String>();
		Integer [] docid = new Integer[child_num]; 
		
		for(int i = 0;i < child_num;i ++)
		{
			File file = new File("resource/" + RootNode.get_child().get(i).get_vl() + ".inv");
			try{
				FileInputStream fis = new FileInputStream(file);     
		        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		        BufferedReader reader = new BufferedReader(isr);
		        uw_buf.add(reader);
		        
			}
			catch(IOException e)
			{
				System.out.println("Error in UW");
			}
		}
		for(int j = 0;j < child_num; j ++)
		{
			try{
			uw_buf.get(j).readLine();
			String temp_str = new String(uw_buf.get(j).readLine());
			uw_str.add(temp_str);
			}
			catch(IOException e)
			{
				System.out.println("Error in UW readline");
			}
		}
		File uw_file = new File("resource/UW_result.txt");
		
		
		try{
			uw_file.createNewFile();
			FileWriter fw =  new FileWriter(uw_file);
			
			
			while(JudgeEnd(uw_str,child_num) != 0)// guarantee all are not null
			{
				ArrayList<Integer> output_str = new ArrayList<Integer>();
				for(int i = 0;i < child_num;i ++)
				{
					docid[i] = extractInteger(uw_str.get(i));
				}
				
			
				if(Judge_sameid(docid,child_num) == -1)
				{
					
					ArrayList<ArrayList<Integer>> pos_array = new ArrayList<ArrayList<Integer>>();
					for(int k = 0;k < child_num;k ++)
					{
						ArrayList<Integer> temp_arr = new ArrayList<Integer>();
						add_pos(uw_str.get(k),temp_arr);
						pos_array.add(temp_arr);
					}
					
					
					
					Integer[] arr_ptr = new Integer[child_num];
					for(int p = 0;p < child_num;p ++)
						arr_ptr[p] = 0;
					while(Judge_arr_ptr(pos_array,arr_ptr,child_num) == 1)
					{
						int min = Judge_correct(pos_array,arr_ptr,child_num,n_uw);
						
						if(min == -1)
						{
							
							int temp = pos_array.get(0).get(arr_ptr[0]);
							output_str.add(temp);
							for(int p = 0;p < child_num;p ++)
								arr_ptr[p] ++;
						}
						else
						{
						    arr_ptr[min] ++;
						}
					}
					
					if(output_str.size() != 0)
					{
						
						fw.write(get_head(uw_str.get(0)));
						for(int t = 0;t < output_str.size();t ++)
						{
							
							fw.write(Integer.toString(output_str.get(t)));
							if(t == output_str.size() - 1)
								fw.write("\n");
							else
								fw.write(" ");
						}
							
					}
					for(int q = 0;q < child_num;q ++)
					{
						uw_str.set(q, uw_buf.get(q).readLine());
					}
					
					
				}
				else
				{
					try{
						
					uw_str.set(Judge_sameid(docid,child_num), uw_buf.get(Judge_sameid(docid,child_num)).readLine());
					}
					catch(IOException f)
					{
						System.out.println("Error in UW");
					}
				}
				
				
			}
			fw.close();
			
		}
		catch(IOException e)
		{
			System.out.println("Error in writing results in UW");
			
		}
	}
	/* Combine two inverted lists using #NEAR */
	public void Exe_NEAR(File newfile, File file,int n_near)//detailed NEAR operating in newfile and file
	{
		ArrayList<String> output_str = new ArrayList<String>();
		try{
			FileInputStream fist = new FileInputStream(newfile);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrt);	
				
			FileInputStream fis = new FileInputStream(file);     
	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        BufferedReader reader = new BufferedReader(isr);
	  
	        String newfile_str = new String(readert.readLine());
	        String file_str = new String(reader.readLine());
	        
	        String newfilepath = new String(newfile.getPath());//first line fix
	        String filepath = new String(file.getPath());
	        
	        	newfile_str = readert.readLine();
	        
	        	file_str = reader.readLine();
	        while((newfile_str != null) && (file_str != null))
	        {
	        	if(extractInteger(newfile_str) > extractInteger(file_str))
	        	{
	        		file_str = reader.readLine();
	        	}
	        	else if(extractInteger(newfile_str) < extractInteger(file_str))
	        	{
	        		newfile_str = readert.readLine();
	        	}
	        	else
	        	{
	        		ArrayList<Integer> newfile_pos = new ArrayList<Integer>();
	        		ArrayList<Integer> file_pos = new ArrayList<Integer>();
	        		add_pos(newfile_str,newfile_pos);
	        		add_pos(file_str,file_pos);
	        		int newfile_pos_ptr = 0;
	        		int file_pos_ptr = 0;
	        		String term_result = new String();// save the result
	        		while((newfile_pos_ptr <= newfile_pos.size() - 1) && (file_pos_ptr <= file_pos.size() - 1))
	        		{
	        			if((file_pos.get(file_pos_ptr) - newfile_pos.get(newfile_pos_ptr)) > n_near)
	        			{
	        				newfile_pos_ptr ++;
	        			}
	        			else if((file_pos.get(file_pos_ptr) - newfile_pos.get(newfile_pos_ptr)) < 0)
	        			{
	        				file_pos_ptr ++;
	        			}
	        			else
	        			{
	        				if(term_result.isEmpty() == true)
	        				{
	        					term_result = term_result.concat(get_head(file_str));
	        					term_result = term_result.concat(file_pos.get(file_pos_ptr).toString());
	        				}
	        				else
	        				{
	        					term_result = term_result.concat(" ");
	        					term_result = term_result.concat(file_pos.get(file_pos_ptr).toString());
	        				}
	        				newfile_pos_ptr ++;
	        				file_pos_ptr ++;
	        			}
	        		}
	        		if(term_result.isEmpty() == false){
	        			output_str.add(term_result);
	        		}
	        		newfile_str = readert.readLine();
	        		file_str = reader.readLine();
	        	}
	        }
	        readert.close();
	        reader.close();
	        FileWriter fw =  new FileWriter(newfile);
	        
	        for(int i = 1; i <= output_str.size(); i ++)
	        {
	        	fw.write(output_str.get(i - 1));
	        	fw.write("\n");
	        }
	        
	        
	        fw.close();
		}
		catch(IOException e){
			System.out.println("Error in NEAR");
		}
	}
	/*extract docID from query string*/
	public int extractInteger(String str)
	{
		for(int i = 0;i <= str.length() - 1; i ++)
		{
			if(str.charAt(i) == ' ')
			{
				return Integer.parseInt(str.substring(0, i));
			}
		}
		return Integer.parseInt(str);
	}
	/* extract score(TF) from query string*/
	public int extractTF(String str)
	{
		
		int i = 0;
		while(str.charAt(i) != ' ')
		{
			i ++;
		}
		i ++;
		int temp = i;
		while((i <= str.length() - 1) && (str.charAt(i) != ' '))
		{
			i ++;
		}
		if(i > str.length() - 1)
		{
			return Integer.parseInt(str.substring(temp));
		}
		else
			
		    return Integer.parseInt(str.substring(temp, i));
	}
	/* add qualified NEAR position in file_pos for later use*/
	public void add_pos(String file,ArrayList<Integer> file_pos)
	{
		
		int i = 0;
		int count = 0;
		while(count != 3)
		{
			if(file.charAt(i) == ' ')
			{
				
				count ++;
			}
			i ++;
		}
		
		while(i <= file.length() - 1)
		{
			int temp_i = i;
			while(i <= file.length() - 1 && file.charAt(i) != ' ')
			{
				i ++;
			}
			
			if(i > file.length() - 1)
				file_pos.add(Integer.parseInt(file.substring(temp_i)));
			else
			{
                
				file_pos.add(Integer.parseInt(file.substring(temp_i, i)));
				
			}
			i ++;    
		}
	}
	/* get the first three element in one line of inverted lists*/
	public String get_head(String str)
	{
		int i = 0;
		int count = 0;
		while(count != 3)
		{
			if(str.charAt(i) == ' ')
			{
				count ++;
			}
			i ++;
		}
		return str.substring(0, i);
	}
	/* get n from NEAR/n */
	public int get_n(TreeNode branchNode)
    {
    	
    	int i = 0;
    	
    	String temp = new String(branchNode.get_vl());
    	for(; i <= temp.length() - 1;i ++)
    	{
    		if(temp.charAt(i) == '/')
    		{
    			i ++;
    			break;
    		}
    	}
    	
    	return Integer.parseInt(temp.substring(i));
    	
    }
    /* Write final first 100 results to file */
	public void Write_Result(int q_id,File exp_file)
	{
		File read_ptr = new File("resource/SUM_result.txt");
		try{
			
			FileInputStream fist = new FileInputStream(read_ptr);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrt);
		    
		    FileWriter fw =  new FileWriter(exp_file,true);
		    String temp_str = new String();
		    int j = 1;
		    while((temp_str = readert.readLine()) != null)
		    {
		    	int i = 0;
		    	for(;i < temp_str.length();i ++)
		    	{
		    		if(temp_str.charAt(i) == ' ')
		    			break;
		    	}
		    	
		    	int docid = Integer.valueOf(temp_str.substring(0, i));
		    	double docscore = Double.valueOf(temp_str.substring(i + 1));
		    	
		    	fw.write(Integer.toString(q_id));
		    	fw.write("\t");
		    	fw.write("Q0");
		    	fw.write("\t");
		    	fw.write(temp_str.substring(0, i));
		    	fw.write("\t");
		    	fw.write(Integer.toString(j));
		    	fw.write("\t");
		    	fw.write(temp_str.substring(i + 1));
		    	fw.write("\t");
		    	fw.write("run-1");
		    	fw.write("\n");
		    	j ++;
		    	
		    }
		    fw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in Write_Result.");
		}
	}
	/* The output formulation of csv file and return the first 50 files */
    public void Write_Sample(int q_id,File exp_file)
    {
    	File read_ptr = new File("resource/SUM_result.txt");
		try{
			
			FileInputStream fist = new FileInputStream(read_ptr);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrt);
		    
		    FileWriter fw =  new FileWriter(exp_file,true);
		    String temp_str = new String();
		    if(q_id == 1)
		    {
		    	fw.write("rank,docid,score");
		    	fw.write("\n");
		    }
		    int j = 1;
		    while(j <= 50 && (temp_str = readert.readLine()) != null)
		    {
		    	
		    	int i = 0;
		    	for(;i < temp_str.length();i ++)
		    	{
		    		if(temp_str.charAt(i) == ' ')
		    			break;
		    	}
		    	
		    	int docid = Integer.valueOf(temp_str.substring(0, i));
		    	double docscore = Double.valueOf(temp_str.substring(i + 1));
		    	fw.write(Integer.toString(j));
		    	fw.write(",");
		    	fw.write(temp_str.substring(0, i));
		    	fw.write(",");
		    	fw.write(temp_str.substring(i + 1));
		    	
		    	fw.write("\n");
		    	j ++;
		    	
		    }
		    fw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in Write_Result.");
		}
    }
    /*
     * This is the entry point of BM25 ranking. I have designed many inputs and outputs formats.
     * The codes commented in the first four lines are for separate query. The ouput is in the file SUM_result.txt
     * The second format is for csv sample. The output is in the Experiment_result.txt  
     * The third format is for BOW-F.txt and BOW-S.txt. You can only comment the line parse_Tree.Write_Sample(q_id, exp_file);
     * and uncomment the line parse_Tree.Write_Result(q_id,exp_file);
     * Important: The file Experiment_result.txt is additional written, so you have to delete it every time you run the program.
     * I am sure that my program can run normally, so if there is an error please contact me. 
     */
	public static void main(String[] args){
	/*	String query_str = new String("#SUM(#UW/5(neil young) #NEAR/2(family tree) #UW/2(united states))");
		BM25_ranking parse_Tree = new BM25_ranking(query_str,1.2,0.75,100);
	    TreeNode testnode = parse_Tree.get_root();
	    
	    parse_Tree.Execute_BM25_ranking(parse_Tree.get_root().get_child().get(0));
	    */
		File target_file = new File("resource/sample3.txt");
		File exp_file = new File("resource/Experiment_result.txt");
		long time_begin = System.currentTimeMillis();
		try
		{
			exp_file.createNewFile();
			FileInputStream fist = new FileInputStream(target_file);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrt);
		    
		    String temp_str = new String();
		    while((temp_str = readert.readLine()) != null)
		    {
		    	int i = 0;
		    	for(;i < temp_str.length();i ++)
		    	{
		    		if(temp_str.charAt(i) == ':')
		    			break;
		    	}
		    	int q_id = Integer.valueOf(temp_str.substring(0, i));
		    	//String true_q = new String(temp_str.substring(i + 1));
		    	String true_q = new String("#SUM(" + temp_str.substring(i + 1) + ")");
		    	
		    	BM25_ranking parse_Tree = new BM25_ranking(true_q,1.2,0.75,100);
			    TreeNode testnode = parse_Tree.get_root();
			    parse_Tree.Execute_BM25_ranking(parse_Tree.get_root().get_child().get(0));
			    
			    //parse_Tree.Write_Result(q_id,exp_file);
			    parse_Tree.Write_Sample(q_id, exp_file); //especially for csv
		    }
		    long time_end = System.currentTimeMillis();
    	    long time_consumed = time_end - time_begin;
    	    System.out.println("Run time is :" + time_consumed);
		}
		catch(IOException e)
		{
			System.out.println("Error in main");
		}
	}
}
