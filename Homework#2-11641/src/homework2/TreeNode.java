package homework2;

import java.util.*;
/*
 * This class is designed to describe the nodes in query tree structure
 */
public class TreeNode {
	
	private String vl;// value store in this tree node
	private ArrayList<TreeNode> Child;//children of this node
	private boolean isLeave;//flag of whether this is a leave
	private boolean isop_node;//flag of whether this is a operator node
	private float weight_num;
	public TreeNode(String str)
	{
		this.vl = str;
		isLeave = false;
		this.Child = new ArrayList<TreeNode>();
		weight_num = -1;
	}
	public void set_isop_node(boolean t)
	{
		this.isop_node = t;
	}
	public boolean get_isop_node()
	{
		return this.isop_node;
	}
	public TreeNode()
	{
		
	}
	public void set_vl(String str)
	{
		this.vl = str;
	}
	public String get_vl()
	{
		return this.vl;
	}
	public void set_isLeave(boolean t)
	{
		this.isLeave = t;
	}
	public boolean get_isLeave()
	{
		return this.isLeave;
	}
	public void add_child(TreeNode e)
	{
		this.Child.add(e);
	}
	public ArrayList<TreeNode> get_child()
	{
		return this.Child;
	}
	public void set_weight_num(float num)
	{
		this.weight_num = num;
	}
	public float get_weight_num()
	{
		return this.weight_num;
	}
}

