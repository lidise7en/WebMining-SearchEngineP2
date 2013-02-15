package homework2;

import homework2.Pretreatment;
import homework2.Stopwords;

import java.io.*;
/*
 * This class is to process initial query to formalized query
 *  including considering stopwords and decision about dashes and slashes 
 */
public class Pretreatment {
	
	private String initial_str;// initial string of query
	private String aft_str;    // formalized query after all these processes
	
	public Pretreatment(String pre_str)//Constructor
	{
		this.initial_str = new String(pre_str);
		this.aft_str = new String("");
	}
	public String get_aft_str()
	{
		return this.aft_str;
	}
	public void Treat_query()//processing decisions listed above
	{
		int j = 0;
		String tt = new String();
		
		while(j <= this.initial_str.length() - 1)// decisions about dashes and slashes
		{
			
			if(this.initial_str.charAt(j) == '-')
			{
				
				tt = tt.concat(String.valueOf(' '));
				
			}
			else
			{
				
				tt = tt.concat(String.valueOf(this.initial_str.charAt(j)));
			}
			j ++;
		}
		this.aft_str = tt;
		
		
		Stopwords sw = new Stopwords();
		sw.construct_map("resource/stoplist.txt");
		
		int i = 0;
		int flag;
		String temp = new String();
		while(i <= this.aft_str.length() - 1)
		{
			System.out.println(i);
			System.out.println("current length is" + this.aft_str.length());
			if(this.aft_str.charAt(i) == '(')
			{
				if(this.aft_str.charAt(i + 1) != '#')
				{
					flag = i;
				
					while(this.aft_str.charAt(i) != ' ' && this.aft_str.charAt(i) != ')')
					{
						i ++;
					}
					temp = this.aft_str.substring(flag + 1, i);
					System.out.println(temp);
				
					if(sw.get_map().containsKey(temp) == true)//if this term is included in stopwords list, then delete it
					{
						delete_substring(flag + 1,i);
						i =flag ;
					}
				
				}
				else
				{
					i ++;
					while(this.aft_str.charAt(i) != '(')
					{
						i ++;
					}
				}
			}
			else if(this.aft_str.charAt(i) == ' ')
			{
				if(this.aft_str.charAt(i + 1) != '#')
				{
				flag = i;
				i ++;
				while((this.aft_str.charAt(i) != ' ') && (this.aft_str.charAt(i) != ')'))
				{
					i ++;
				}
				System.out.println("the final i is" + i);
				if(this.aft_str.charAt(i) == ' ')
				{
					temp = this.aft_str.substring(flag + 1, i);
					System.out.println(temp);
					
					if(sw.get_map().containsKey(temp) == true)////if this term is included in stopwords list, then delete it
					{
						delete_substring(flag + 1,i);
						i =flag;
					}
				
				}
				else
				{
					temp = this.aft_str.substring(flag + 1, i);
					System.out.println(temp);
					
					if(sw.get_map().containsKey(temp) == true)//if this term is included in stopwords list, then delete it
					{
						delete_substring(flag,i - 1);
						i =flag;
					}
			
				}
				}
				else
				{
					while(this.aft_str.charAt(i) != '(')
					{
						i ++;
					}
				}
				
			}
			else
				i ++;
			
		}
	}
	public void delete_substring(int begin,int end)//delete the part of string in aft_str
	{
		String deleted_str = new String();
		for(int i =  0;i <= this.aft_str.length() - 1;i ++)
		{
			if(i < begin || i > end)
			{
				
				deleted_str = deleted_str.concat(String.valueOf(this.aft_str.charAt(i)));
				
			}
		}
		this.aft_str = deleted_str;
	}
	public void delete_titlefield()//decisions about field-based retrieval
	{
		int i = 0;
		while(i <= this.initial_str.length() - 1)
		{
			
			if(this.initial_str.charAt(i) == '.')
			{
				String temp = new String();
				temp = temp.concat(String.valueOf(this.initial_str.charAt(i+1)));
				temp = temp.concat(String.valueOf(this.initial_str.charAt(i+2)));
				temp = temp.concat(String.valueOf(this.initial_str.charAt(i+3)));
				temp = temp.concat(String.valueOf(this.initial_str.charAt(i+4)));
				
				if(temp.equals("body") == true)// recognize the word "body" and delete it
				{
					this.delete_initial_string(i, i + 4);
					
				}
				else
					i ++;
			}
			else
				i ++;
		}
	}
	public void delete_initial_string(int begin,int end)// delete the substring in initial query
	{
		String deleted_str = new String();
		for(int i =  0;i <= this.initial_str.length() - 1;i ++)
		{
			if(i < begin || i > end)
			{
				
				deleted_str = deleted_str.concat(String.valueOf(this.initial_str.charAt(i)));
				
			}
		}
	
		this.initial_str = deleted_str;
		System.out.println(this.initial_str);
	}
	public static void main(String[] args){
		//This is just the test for this class, but not associated with the project
		Pretreatment pre_obj = new Pretreatment("#AND(sports united.body states #NEAR/1(neil young.title))");
		pre_obj.delete_titlefield();
		System.out.println(pre_obj.initial_str);
		pre_obj.Treat_query();
		System.out.println(pre_obj.aft_str);
		
	}

}
