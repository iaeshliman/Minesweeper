/**
 * Author: Isaac Aeshliman
 * Date: Mar 6, 2021
 * Description:
 *
 * TODO: 
 */

package aeshliman.minesweeper;

public class MinesweeperCell
{
	// Instance Variables
	private boolean isMine;
	private int value;
	
	// Constructors
	{
		isMine = false;
		value = -1;
	}
	
	// Getters and Setters
	public boolean isMine() { return this.isMine; }
	public int getValue() { return this.value; }
	public void setMine(boolean isMine) { this.isMine = isMine; }
	public void setValue(int value) { this.value = value; }
}
