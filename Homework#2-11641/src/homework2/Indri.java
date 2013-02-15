package homework2;

import homework2.Pretreatment;
import homework2.Tree;
import homework2.TreeNode;
import homework2.Result_Pair;

import java.io.*;
import java.util.*;
import java.lang.*;
/*
 *  This class is to implement Indri with operator #AND #WEIGHT #NEAR #UW
 */
public class Indri {

	
	private String query;//a query provided by users
	private Tree query_tree;//query tree constructed with query
	private int operator_counter;// count the number of operators in query 
	private TreeNode RootNode;//root of this query tree
	private Map<Integer,Double> Indri_Map = new HashMap<Integer,Double>();
	private ArrayList<Result_Pair> Indri_final_result = new ArrayList<Result_Pair>();
	private double x; //lamada
	private double u;  //miu
	
	public Indri(String que,double x,double u)//Constructor
	{
		this.query = que;
		this.query_tree = new Tree();
		this.RootNode = new TreeNode("this is root");
        this.query_tree.setRoot(this.RootNode);
		this.query_tree.ConstructTree(this.RootNode, this.query);
		this.operator_counter = 0;
		this.x = x;
		this.u = u;
		
	}
	public void set_query(String que)
	{
		this.query = que;
	}
	public TreeNode get_root()
	{
		return this.RootNode;
	}
	/* Implement the whole Indri ranking process using a parsing tree*/
	public String Indri_rank(TreeNode branchNode)
	{
		String file_path_head = new String("resource/");// set file path 
		
		if(branchNode.get_isop_node() == true)
		{
			this.operator_counter ++;
			int op_counter = this.operator_counter;
			/* if the current operator is #AND */
			if(branchNode.get_vl() == "AND")
			{
				File newfile = new File(file_path_head + this.operator_counter+".txt");
				
				ArrayList<Result_Pair> AND_result = new ArrayList<Result_Pair>();
				try
				{
					newfile.createNewFile();
					
					for(int i = 0; i < branchNode.get_child().size(); i ++)
					{
						ArrayList<Result_Pair> temp_score = new ArrayList<Result_Pair>();
						String file_name = new String(Indri_rank(branchNode.get_child().get(i)));
						if(i == 0)
						{
							if(file_name.charAt(file_name.length()-1) == 'v' || branchNode.get_child().get(i).get_vl().charAt(0) == 'N' || branchNode.get_child().get(i).get_vl().charAt(0) == 'U')
							{
								Transfer_Score(file_name,AND_result,branchNode);
							}
							else
							{
								
								Copy_Score(file_name,AND_result,branchNode);
							}
						}
						else
						{
							if(file_name.charAt(file_name.length()-1) == 'v' || branchNode.get_child().get(i).get_vl().charAt(0) == 'N' || branchNode.get_child().get(i).get_vl().charAt(0) == 'U')
							{
								Transfer_Score(file_name,temp_score,branchNode);
							}
							else
							{
								Copy_Score(file_name,temp_score,branchNode);
							}
							Merge_List(AND_result,temp_score);
						}
						
						
						
					}
					FileWriter fw =  new FileWriter(newfile);
					for(int r = 0;r < AND_result.size();r ++)
					{
						fw.write(Integer.toString(AND_result.get(r).get_docID()));
						fw.write(" ");
						fw.write(Double.toString(AND_result.get(r).get_docScore()));
						fw.write("\n");
						
					}
					fw.close();
				}
				catch(IOException e)
				{
					System.out.println("Error in AND");
				}
			}
			/* if the current operator is WEIGHT */
			else if(branchNode.get_vl() == "WEIGHT")
			{
				File newfile = new File(file_path_head + this.operator_counter+".txt");
				
				ArrayList<Result_Pair> WEIGHT_result = new ArrayList<Result_Pair>();
				try
				{
					newfile.createNewFile();
					
					for(int i = 1; i < branchNode.get_child().size(); i += 2)
					{
						ArrayList<Result_Pair> temp_score = new ArrayList<Result_Pair>();
						String file_name = new String(Indri_rank(branchNode.get_child().get(i)));
						if(i == 1)
						{
							if(file_name.charAt(file_name.length()-1) == 'v' || branchNode.get_child().get(i).get_vl().charAt(0) == 'N' || branchNode.get_child().get(i).get_vl().charAt(0) == 'U')
							{
								Transfer_WEIGHT_Score(file_name,WEIGHT_result,branchNode,i);
							}
							else
							{
								Copy_WEIGHT_Score(file_name,WEIGHT_result,branchNode,i);
							}
						}
						else
						{
							if(file_name.charAt(file_name.length()-1) == 'v' || branchNode.get_child().get(i).get_vl().charAt(0) == 'N' || branchNode.get_child().get(i).get_vl().charAt(0) == 'U')
							{
								Transfer_WEIGHT_Score(file_name,temp_score,branchNode,i);
							}
							else
							{
								Copy_WEIGHT_Score(file_name,temp_score,branchNode,i);
							}
							Merge_List(WEIGHT_result,temp_score);
						}
						
						
						
					}
					FileWriter fw =  new FileWriter(newfile);
					for(int r = 0;r < WEIGHT_result.size();r ++)
					{
						fw.write(Integer.toString(WEIGHT_result.get(r).get_docID()));
						fw.write(" ");
						fw.write(Double.toString(WEIGHT_result.get(r).get_docScore()));
						fw.write("\n");
						
					}
					fw.close();
				}
				catch(IOException e)
				{
					System.out.println("Error in WEIGHT");
				}
			}
			/* if the current operator is #NEAR */
			else if(branchNode.get_vl().charAt(0) == 'N')
			{
				if(branchNode.get_child().size() == 1)
				{
					File readfile = new File("resource/" + branchNode.get_child().get(0).get_vl() + ".inv");
					File writefile = new File("resource/" + this.operator_counter + ".txt");
					try{
						FileInputStream fis = new FileInputStream(readfile);     
				        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				        BufferedReader reader = new BufferedReader(isr);
				        
				        writefile.createNewFile();
				        FileWriter fw =  new FileWriter(writefile);
				        
				        reader.readLine();
				        String t = new String();
				        while((t = reader.readLine()) != null)
				        {
				        	fw.write(t);
				        	fw.write("\n");
				        }
				        reader.close();
				        fw.close();
					}
					catch(IOException e)
					{
						System.out.println("Error in one child NEAR.");
					}
				}
				else
				{
					int n_near = get_n(branchNode);
					Execute_NEAR(branchNode,n_near,"NEAR_result");
					File fix_tf = new File("resource/NEAR_result.txt");
					Fix_tf(fix_tf,Integer.toString(this.operator_counter));
				}
				
			}
			/* if the current operator is #UW */
			else if(branchNode.get_vl().charAt(0) == 'U')
			{
				if(branchNode.get_child().size() == 1)
				{
					File readfile = new File("resource/" + branchNode.get_child().get(0).get_vl() + ".inv");
					File writefile = new File("resource/" + this.operator_counter + ".txt");
					try{
						FileInputStream fis = new FileInputStream(readfile);     
				        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				        BufferedReader reader = new BufferedReader(isr);
				        
				        writefile.createNewFile();
				        FileWriter fw =  new FileWriter(writefile);
				        
				        reader.readLine();
				        String t = new String();
				        while((t = reader.readLine()) != null)
				        {
				        	fw.write(t);
				        	fw.write("\n");
				        }
				        reader.close();
				        fw.close();
					}
					catch(IOException e)
					{
						System.out.println("Error in one child NEAR.");
					}
				}
				else
				{
					int n_uw = get_n(branchNode);
					
					int child_num = branchNode.get_child().size();
					
					ArrayList<BufferedReader> uw_buf = new ArrayList<BufferedReader>();
					ArrayList<String> uw_str = new ArrayList<String>();
					Integer [] docid = new Integer[child_num]; 
					
					for(int i = 0;i < child_num;i ++)
					{
						File file = new File("resource/" + branchNode.get_child().get(i).get_vl() + ".inv");
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
					File fix_tf = new File("resource/UW_result.txt");
					Fix_tf(fix_tf,Integer.toString(this.operator_counter));
				}
				
				
			}
			
			return new String(op_counter + ".txt");
		}
		else
		{
			return new String(branchNode.get_vl() + ".inv");
		}
	}
	/* Merge two score lists under the Indri operators */
	public void Merge_List(ArrayList<Result_Pair> final_result,ArrayList<Result_Pair> temp_result)
	{
		int final_index = 0;
		int temp_index = 0;
		double final_zero_score = final_result.get(0).get_docScore();
		//System.out.println("The size is " + temp_result.size());
		double temp_zero_score = temp_result.get(0).get_docScore();
		while((final_index < final_result.size()) && (temp_index < temp_result.size()))
		{
			if(final_result.get(final_index).get_docID() < temp_result.get(temp_index).get_docID())
			{
				final_result.get(final_index).set_docScore(final_result.get(final_index).get_docScore() + temp_zero_score);
				final_index ++;
			}
			else if(final_result.get(final_index).get_docID() > temp_result.get(temp_index).get_docID())
			{
				Result_Pair insert_pair = new Result_Pair();
				insert_pair.set_docID(temp_result.get(temp_index).get_docID());
				insert_pair.set_docScore(temp_result.get(temp_index).get_docScore() + final_zero_score);
				final_result.add(final_index, insert_pair);
				temp_index ++;
				final_index ++;
			}
			else
			{
				final_result.get(final_index).set_docScore(final_result.get(final_index).get_docScore() +temp_result.get(temp_index).get_docScore());
				final_index ++;
				temp_index ++;
			}
		}
		if(final_index == final_result.size() && temp_index != temp_result.size())
		{
			while(temp_index != temp_result.size())
			{
				Result_Pair insert_pair = new Result_Pair();
				insert_pair.set_docID(temp_result.get(temp_index).get_docID());
				insert_pair.set_docScore(temp_result.get(temp_index).get_docScore() + final_zero_score);
				final_result.add(insert_pair);
				temp_index ++;
			}
		}
		else if(final_index != final_result.size() && temp_index == temp_result.size())
		{
			while(final_index != final_result.size())
			{
				final_result.get(final_index).set_docScore(final_result.get(final_index).get_docScore() + temp_zero_score);
				final_index ++;
			}
		}
	}
	/* Transfer inverted lists to score lists under the operator #AND */
	public void Transfer_Score(String file_name,ArrayList<Result_Pair> temp_score,TreeNode branchNode)
	{
		File indri_file = new File("resource/" + file_name);
		System.out.println("resource/" + file_name);
		try{
			
			String temp_str = new String();
		    FileInputStream fistm = new FileInputStream(indri_file);     
		    InputStreamReader isrtm = new InputStreamReader(fistm, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrtm);
		    if(file_name.charAt(file_name.length()-1) == 'v')
		    {
		    	temp_str = readert.readLine();
		    }
		    
		    int ctf = 0;
		    while((temp_str = readert.readLine()) != null)
		    {
		    	ctf += extractTF(temp_str);
		    }
		    
		    readert.close();
		    FileInputStream fist = new FileInputStream(indri_file);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader reader = new BufferedReader(isrt);
		    if(file_name.charAt(file_name.length()-1) == 'v')
		    {
		    	temp_str = reader.readLine();
		    }
		    int test = 0;
		    int q_num = branchNode.get_child().size();
		   
		    temp_str = "0 0 1301 1";
		    Calculate_Indri(temp_str,ctf,(double)q_num,temp_score);
		    while((temp_str = reader.readLine()) != null)
		    {
		    	test ++;
		    	Calculate_Indri(temp_str,ctf,(double)q_num,temp_score);
		    	System.out.print(test + "\n");
		    	
		    }
		   
		    reader.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in Transfer Score");
		}
	}
	/* Transfer the inverted lists to score lists under #WEIGHT */
	public void Transfer_WEIGHT_Score(String file_name,ArrayList<Result_Pair> temp_score,TreeNode branchNode,int i)
	{
		File indri_file = new File("resource/" + file_name);
		try{
			
			String temp_str = new String();
		    FileInputStream fistm = new FileInputStream(indri_file);     
		    InputStreamReader isrtm = new InputStreamReader(fistm, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrtm);
		    if(file_name.charAt(file_name.length()-1) == 'v')
		    {
		    	temp_str = readert.readLine();
		    }
		    
		    int ctf = 0;
		    while((temp_str = readert.readLine()) != null)
		    {
		    	ctf += extractTF(temp_str);
		    }
		    
		    readert.close();
		    FileInputStream fist = new FileInputStream(indri_file);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader reader = new BufferedReader(isrt);
		    if(file_name.charAt(file_name.length()-1) == 'v')
		    {
		    	temp_str = reader.readLine();
		    }
		    int test = 0;
		    double q_num = 1.0/(Double.valueOf(branchNode.get_child().get(i-1).get_vl()));
		   
		    temp_str = "0 0 1301 1";
		    Calculate_Indri(temp_str,ctf,(double)q_num,temp_score);
		    while((temp_str = reader.readLine()) != null)
		    {
		    	test ++;
		    	Calculate_Indri(temp_str,ctf,(double)q_num,temp_score);
		    	System.out.print(test + "\n");
		    	
		    }
		   
		    reader.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in Transfer  WEIGHT Score");
		}
	}
	/* when encounter first term, using this function to copy scores to result lists under #WEIGHT */
	public void Copy_WEIGHT_Score(String file_name,ArrayList<Result_Pair> temp_score,TreeNode branchNode,int j)
	{
		File indri_file = new File("resource/" + file_name);
		try{
			String temp_str = new String();
		    FileInputStream fistm = new FileInputStream(indri_file);     
		    InputStreamReader isrtm = new InputStreamReader(fistm, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrtm);
		    
		    while((temp_str = readert.readLine()) != null)
		    {
		    	int i = 0;;
		    	for(;i < temp_str.length();i ++)
		    	{
		    		if(temp_str.charAt(i) == ' ')
		    			break;
		    	}
		    	Result_Pair temp_pair = new Result_Pair();
		    	temp_pair.set_docID(Integer.valueOf(temp_str.substring(0, i)));
		    	System.out.println("temp_str is" + temp_str);
		    	System.out.println("file name is " + "resource/" + file_name);
		    	
					System.out.println("**&&^%%$%$$$" + branchNode.get_child().get(j).get_vl());
				
		    	temp_pair.set_docScore(Double.valueOf(branchNode.get_child().get(j-1).get_vl())*Double.valueOf(temp_str.substring(i + 1)));
		    	temp_score.add(temp_pair);
		    }
		    
		}
		catch(IOException e)
		{
			System.out.println("Error in Copy Score");
		}
	}
	/* when encounter first argument, using this function to copy score list to result list under #AND */
	public void Copy_Score(String file_name,ArrayList<Result_Pair> temp_score,TreeNode branchNode)
	{
		File indri_file = new File("resource/" + file_name);
		try{
			String temp_str = new String();
		    FileInputStream fistm = new FileInputStream(indri_file);     
		    InputStreamReader isrtm = new InputStreamReader(fistm, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrtm);
		    
		    while((temp_str = readert.readLine()) != null)
		    {
		    	int i = 0;;
		    	for(;i < temp_str.length();i ++)
		    	{
		    		if(temp_str.charAt(i) == ' ')
		    			break;
		    	}
		    	Result_Pair temp_pair = new Result_Pair();
		    	temp_pair.set_docID(Integer.valueOf(temp_str.substring(0, i)));
		    	
		    	temp_pair.set_docScore(Double.valueOf(temp_str.substring(i + 1)));
		    	temp_score.add(temp_pair);
		    }
		    
		}
		catch(IOException e)
		{
			System.out.println("Error in Copy Score");
		}
	}
	/* Calculate Indri score using formula */
	public void Calculate_Indri(String temp_str,int ctf,double q_length,ArrayList<Result_Pair> temp_score)
	{
		double Indri_score;
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
		
		double part1 = (tf + ((this.u * (double)ctf)/1158815080L)) * this.x;
		double part2 = d_length + this.u;
		double part3 = ((1 - this.x) * (double)ctf)/1158815080L;
		Indri_score = (1.0/q_length) * Math.log((part1/part2) + part3);///////////////////////////////////////miss the weight
		
		Result_Pair temp_pair = new Result_Pair();
		temp_pair.set_docID((int)docID);
		temp_pair.set_docScore(Indri_score);
		temp_score.add(temp_pair);
	}
	public int JudgeEnd(ArrayList<String> str,int child_num)
	{
		for(int i = 0;i < child_num;i ++)
		{
			if(str.get(i) == null)
				return 0;
		}
		return 1;
	}
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
	public int extractTF(String str)// extract score(TF) from query string
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
	public int get_n(TreeNode branchNode)// get n from NEAR/n
    {
    	
    	int i = 0;
    	System.out.print(branchNode.get_vl() + "\n");
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
	/* Specific #NEAR process */
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
		 	        tempstr = reader.readLine();
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
	/* merge two inverted lists under the condition of #NEAR */
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
	public String get_head(String str)// get the first three element in one line of inverted lists
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
	public void add_pos(String file,ArrayList<Integer> file_pos)// add qualified NEAR position in file_pos for later use
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
	public int extractInteger(String str)//extract docID from query string
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
	public void Fix_tf(File fix_tf,String type)
	{
		try{
		FileInputStream fist = new FileInputStream(fix_tf);     
	    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
	    BufferedReader readert = new BufferedReader(isrt);
	    
	    File final_file = new File("resource/" + type + ".txt");
	    FileWriter fw =  new FileWriter(final_file);
	    String tempstr = new String();
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
	    }
	    fw.close();
	    readert.close();
		}
		catch(IOException e)
		{
			System.out.print("Error in fix_tf\n");
		}
	}
	/* get the whole reuslts as input, rank them according to scores and get the first 100 files */
	public void final_rank()
	{
		File final_file = new File("resource/1.txt");
		try{
			FileInputStream fist = new FileInputStream(final_file);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrt);
		    String temp_str = new String();
		    ArrayList<Result_Pair> final_result = new ArrayList<Result_Pair>();
		    while((temp_str = readert.readLine()) != null)
		    {
		    	int i = 0;
		    	while(temp_str.charAt(i) != ' ')
		    	{
		    		i ++;
		    	}
		    	int docID = Integer.valueOf(temp_str.substring(0, i));
		    	double docScore = Double.valueOf(temp_str.substring(i + 1));
		    	if(final_result.size() < 100)
		    	{
		    		if(final_result.size() == 0)
		    		{
		    			Result_Pair newpair = new Result_Pair();
		    			newpair.set_docID(docID);
		    			newpair.set_docScore(docScore);
		    			final_result.add(newpair);
		    		}
		    		else
		    		{
		    			int j;
		    			for(j = 0;j < final_result.size();j ++)
		    			{
		    				if(docScore > final_result.get(j).get_docScore())
		    				{
		    					Result_Pair newpair = new Result_Pair();
				    			newpair.set_docID(docID);
				    			newpair.set_docScore(docScore);
				    			final_result.add(j, newpair);
				    			break;
		    				}
		    				
		    			}
		    			if(j == final_result.size())
		    			{
		    				Result_Pair newpair = new Result_Pair();
			    			newpair.set_docID(docID);
			    			newpair.set_docScore(docScore);
			    			final_result.add(newpair);
		    			}
		    		}
		    	}
		    	else
		    	{
		    		int d;
		    		for(d = 0;d < 100;d ++)
		    		{
		    			if(docScore > final_result.get(d).get_docScore())
		    			{
		    				Result_Pair newpair = new Result_Pair();
			    			newpair.set_docID(docID);
			    			newpair.set_docScore(docScore);
			    			final_result.add(d, newpair);
			    			break;
		    			}
		    		}
		    		if(d != 100)
		    		{
		    			final_result.remove(100);
		    		}
		    	}
		    }
		    File output = new File("resource/finalIndri.txt");
		    output.createNewFile();
		    FileWriter fw =  new FileWriter(output);
		    for(int t = 0;t < final_result.size();t ++)
		    {
		    	fw.write(Integer.toString(final_result.get(t).get_docID()));
		    	fw.write(" ");
		    	fw.write(Double.toString(final_result.get(t).get_docScore()));
		    	fw.write("\n");
		    }
		    
		    fw.close();
		    
		}
		catch(IOException e)
		{
			System.out.println("Error in final_rank");
		}
		
	}
	/* Write results according to TREC format */
	public void Write_Result(int q_id,File exp_file)
	{
		File read_ptr = new File("resource/finalIndri.txt");
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
		    	if(docid != 0)
		    	{
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
		    	
		    	
		    }
		    fw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in Write_Result.");
		}
	}
	/* write results according to csv file format */
	public void Write_Sample(int q_id,File exp_file)
    {
    	File read_ptr = new File("resource/finalIndri.txt");
		try{
			
			FileInputStream fist = new FileInputStream(read_ptr);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrt);
		    ArrayList<Result_Pair> t = new ArrayList<Result_Pair>();
		    FileWriter fw =  new FileWriter(exp_file,true);
		    String temp_str = new String();
		    if(q_id == 1)
		    {
		    	fw.write("rank,docid,score");
		    	fw.write("\n");
		    }
		    int j = 1;
		    while(j <= 100)
		    {
		    	temp_str = readert.readLine();
		    	int i = 0;
		    	for(;i < temp_str.length();i ++)
		    	{
		    		if(temp_str.charAt(i) == ' ')
		    			break;
		    	}
		    	
		    	int docid = Integer.valueOf(temp_str.substring(0, i));
		    	double docscore = Double.valueOf(temp_str.substring(i + 1));
		    	Result_Pair e = new Result_Pair();
		    	e.set_docID(docid);
		    	e.set_docScore(docscore);
		    	if(t.size() == 0)
		    		t.add(e);
		    	else
		    	{
		    		int y = 0;
		    		for(;y < t.size();y ++)
		    		{
		    			if(e.get_docScore() >= t.get(y).get_docScore())
		    			{
		    				t.add(y, e);
		    				break;
		    			}
		    		}
		    		if(y == t.size())
		    		{
		    			t.add(e);
		    		}
		    	}
		    	
		    	j ++;
		    	
		    }
		    for(int k = 0;k < 50;k ++)
		    {
		    	fw.write(String.valueOf(k + 1));
		    	fw.write(",");
		    	fw.write(String.valueOf(t.get(k).get_docID()));
		    	fw.write(",");
		    	fw.write(Double.toString(t.get(k).get_docScore()));
		    	
		    	fw.write("\n");
		    }
		    fw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in Write_Result.");
		}
    }
	 /*
     * This is the entry point of Indri ranking. I have designed many inputs and outputs formats.
     * The codes commented in the first four lines are for separate query. The ouput is in the file finalIndri.txt
     * The second format is for csv sample. The output is in the Experiment_result.txt  
     * The third format is for BOW-F.txt and BOW-S.txt STR-SDM.txt and STR-HW1.txt. 
     * You can comment the line parse_Tree.Write_Sample(q_id, exp_file);
     * and uncomment the line parse_Tree.Write_Result(q_id,exp_file);
     * For four experiement sets, you should also be careful about the initial query. I have designed to formats which will add 
     * the default #AND and the other is not. Use them properly. 
     * Important: The file Experiment_result.txt is additional written, so you have to delete it every time you run the program.
     * I am sure that my program can run normally, so if there is an error please contact me. 
     */
	public static void main(String[] args){
	/*	String query_str = new String("#AND(#NEAR/2(obama family tree))");
		Indri parse_Tree = new Indri(query_str,0.6,1000);
	    TreeNode testnode = parse_Tree.get_root();
	    
	    parse_Tree.Indri_rank(parse_Tree.get_root().get_child().get(0));
	    parse_Tree.final_rank();*/
	    
		File target_file = new File("resource/sample1.txt");
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
		    	String q = new String("#AND(" + temp_str.substring(i + 1) + ")");//especially for BOW-F.txt and BOW-S.txt 
		    	//String q = new String(temp_str.substring(i + 1));  //for STR-SDM.txt STR-HW1.txt
		    	Pretreatment newpretreat = new Pretreatment(q);
			    newpretreat.Treat_query();
			    String aft_query = newpretreat.get_aft_str();
				
		    	String true_q = new String(aft_query);
		    	
		    	Indri parse_Tree = new Indri(true_q,0.6,1000.0);
			    TreeNode testnode = parse_Tree.get_root();
			    
			    parse_Tree.Indri_rank(parse_Tree.get_root().get_child().get(0));
			    parse_Tree.final_rank();
			    
			   // parse_Tree.Write_Result(q_id,exp_file);
			      parse_Tree.Write_Sample(q_id, exp_file);  // especially for csv
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
