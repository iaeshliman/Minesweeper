/**
 * Author: Isaac Aeshliman
 * Date: Mar 6, 2021
 * Description:
 *
 * TODO: 
 */

package aeshliman.minesweeper;

import java.awt.LayoutManager;
import javax.swing.JPanel;

public class JMinePanel extends JPanel
{
	// Instance Variables
	private static final long serialVersionUID = 4357357301477561180L;
	private MinesweeperCell cell;
	private boolean hasFlag;
	private boolean isClicked;
	
	// Constructors
	public JMinePanel() { super(); }
	public JMinePanel(boolean isDoubleBuffered) { super(isDoubleBuffered); }
	public JMinePanel(LayoutManager layout) { super(layout); }
	public JMinePanel(LayoutManager layout, boolean isDoubleBuffered) { super(layout, isDoubleBuffered); }
	
	{
		cell = new MinesweeperCell();
		hasFlag = false;
		isClicked = false;
	}
	
	// Getters and Setters
	public boolean hasFlag() { return this.hasFlag; }
	public boolean isClicked() { return this.isClicked; }
	public boolean isMine() { return cell.isMine(); }
	public int getValue() { return cell.getValue(); }
	public void setFlag(boolean hasFlag) { this.hasFlag = hasFlag; }
	public void setClicked(boolean isClicked) { this.isClicked = isClicked; }
	public void setMine(boolean isMine) { cell.setMine(isMine); }
	public void setValue(int value) { cell.setValue(value); }
}
