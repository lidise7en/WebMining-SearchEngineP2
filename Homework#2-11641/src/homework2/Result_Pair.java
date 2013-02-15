package homework2;
/*
 * This class is used to store docID - docScore pair 
 * and provide many methods of operating these pairs
 */
public class Result_Pair {

	private int docID;
	private double docScore;
	
	public Result_Pair()
	{
		this.docID = 0;
		this.docScore = 0;
	}
	public void set_docID(int id)
	{
		this.docID = id;
	}
	public int get_docID()
	{
		return this.docID;
	}
	public void set_docScore(double score)
	{
		this.docScore = score;
	}
	public double get_docScore()
	{
		return this.docScore;
	}
}
