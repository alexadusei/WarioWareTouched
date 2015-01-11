//Subclass for the MainScreen and the ScoreScreen. This class deals with the scores and retaining their information and sorting it.

public class Scores implements Comparable<Scores>
{
	// Declare global variables.
	private String name;
	private Integer score;
	public static int sortOrder;
	public final static int SORT_BY_NAME = 0;
	public final static int SORT_BY_SCORE = 1;

	//Declare constructor with two parameters; the name and the score.
	public Scores(String name, Integer score)
	{
		this.name = name;
		this.score = score;
	}

	//Accessor methods
	public String getName()
	{
		return this.name;
	}

	public Integer getScore()
	{
		return this.score;
	}

	public String toString()
	{
		return String.format("%-25s%5s\n",name, score);
	}

	/* The compareTo method. This methods calls itself to compare two sets of data, and deal with it according to that information. The
	 * if-statements will decide if its going to sort judging by the name or the score.
	 */
	public int compareTo(Scores c)
	{
		if (sortOrder == SORT_BY_SCORE)
		{
			int comparison = score.compareTo(c.getScore());
			return comparison;
		}
		else
		{
			int comparison = name.compareToIgnoreCase(c.getName());

			if (comparison == 0)
			{
				int otherComp = score.compareTo(c.getScore());
				return otherComp;
			}
			else
			{
				return comparison;
			}
		}
	}
}