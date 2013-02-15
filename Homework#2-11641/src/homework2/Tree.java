package homework2;
import homework2.Tree;
import homework2.TreeNode;
import java.util.*;
/*
 * This class is designed for parsing Tree structure 
 * and accomplish the construct tree process
 */
public class Tree {

	private TreeNode root;// root of the tree
	private String query;// query used to construct the tree
	private int counter;// count the number of nodes in the tree
	private int Op_Num;//the operator number in this tree
	private ArrayList<String> str_count = new ArrayList<String>();
	private ArrayList<Integer> num_count = new ArrayList<Integer>();
	public Tree()
	{
		this.root = null;
		this.query = "";
		this.counter = 0;
		this.Op_Num = 0;
		
	}
	public ArrayList<String> get_str_count()
	{
		return this.str_count;
	}
	public ArrayList<Integer> get_num_count()
	{
		return this.num_count;
	}
	public boolean No_Operator(String command)
	{
		boolean no_op = true;
		for(int i = 0; i <= command.length() - 1;i ++)
		{
			if(command.charAt(i) == '#')
			{
				no_op = false;
				break;
			}
				
		}
		return no_op;
	}
	public void ConstructTree(TreeNode subroot,String command)
	{
		while(command.charAt(counter) != ')')
		{
			
			if(command.charAt(counter) == ' ')
				counter ++;
			if(command.charAt(counter) == '#')
			{
				if(command.charAt(counter + 1) == 'S')
				{
					TreeNode newnode = new TreeNode("SUM");
					newnode.set_isop_node(true);
					this.Op_Num ++;
					subroot.get_child().add(newnode);
					counter += 5;
					ConstructTree(newnode,command);
					
				}
				
				else if(command.charAt(counter + 1) == 'A')
				{
					TreeNode newnode = new TreeNode("AND");
					newnode.set_isop_node(true);
					this.Op_Num ++;
					subroot.get_child().add(newnode);
					counter += 5;
					ConstructTree(newnode,command);
				}
				else if(command.charAt(counter + 1) == 'N')
				{
					if(command.charAt(counter + 7) == '(')
					{
						TreeNode newnode = new TreeNode(command.substring(counter + 1, counter + 7));
						counter += 8;
						newnode.set_isop_node(true);
						this.Op_Num ++;
						subroot.get_child().add(newnode);
						ConstructTree(newnode,command);
					}
					else
					{
						TreeNode newnode = new TreeNode(command.substring(counter + 1, counter + 8));
						counter += 9;
						newnode.set_isop_node(true);
						this.Op_Num ++;
						subroot.get_child().add(newnode);
						ConstructTree(newnode,command);
					}
					
				}
				else if(command.charAt(counter + 1) == 'U')
				{
					if(command.charAt(counter + 5) == '(')
					{
						TreeNode newnode = new TreeNode(command.substring(counter + 1, counter + 5));
						counter += 6;
						newnode.set_isop_node(true);
						this.Op_Num ++;
						subroot.get_child().add(newnode);
						ConstructTree(newnode,command);
					}
					else
					{
						TreeNode newnode = new TreeNode(command.substring(counter + 1, counter + 6));
						counter += 7;
						newnode.set_isop_node(true);
						this.Op_Num ++;
						subroot.get_child().add(newnode);
						ConstructTree(newnode,command);
					}
				}
				else if(command.charAt(counter + 1) == 'W')
				{
					TreeNode newnode = new TreeNode("WEIGHT");
					newnode.set_isop_node(true);
					this.Op_Num ++;
					subroot.get_child().add(newnode);
					counter += 8;
					ConstructTree(newnode,command);
				}
				
				
			}
			if(counter == command.length() - 1)
				return;
			if(command.charAt(counter) == ' ')
			     counter ++;
			if(command.charAt(counter) != '#')
			{
				
				if(command.charAt(counter) == ' ')
				     counter ++;
				int begin = counter;
				
				while((command.charAt(counter) != ' ') && (command.charAt(counter) != ')'))
				{
					counter ++;
				}
				if(begin != counter)
				{
					String word = new String(command.substring(begin, counter));
					//calculate qtf
					if(this.str_count.contains(word) == true)
					{
						int temp = this.num_count.get(this.str_count.indexOf(word));
						this.num_count.set(this.str_count.indexOf(word),temp + 1);
					}
					else
					{	
						
						this.str_count.add(word);
						this.num_count.add(1);
					}
					//////////////
					if(word != null)// final fix might error here
					{
						TreeNode newnode = new TreeNode(word);
						newnode.set_isop_node(false);
						subroot.get_child().add(newnode);
					}
				}
				
				
			}
			
		}
		if(counter == command.length() - 1)
		{
			
		}
		else
		    counter ++;
		return;
	}
	public void setRoot(TreeNode e)
	{
		this.root = e;
	}
	public TreeNode getRoot()
	{
		return this.root;
	}
	public String get_query()
	{
		return this.query;
	}
	public int get_op_num()
	{
		return this.Op_Num;
	}
	public void print_Tree(TreeNode rootnode)
	{
		
		System.out.println(rootnode.get_vl());
		if(rootnode.get_child().size() != 0)
			for(int i = 0;i <= rootnode.get_child().size() - 1;i ++)
			{
				print_Tree(rootnode.get_child().get(i));
			}
	}
	 public static void main(String[] args){// This is just for test
			
		    String query1 = "#WEIGHT(0.7 #AND(espn sports) 0.2 #AND(#NEAR/1(espn sports)) 0.1 #AND(#UW/8(espn sports)))";
		    Tree tree1 = new Tree();
		    TreeNode RootNode = new TreeNode("");
	        tree1.setRoot(RootNode);
	        tree1.ConstructTree(RootNode, query1);
	        
	        tree1.print_Tree(RootNode);
	        
		}
}
