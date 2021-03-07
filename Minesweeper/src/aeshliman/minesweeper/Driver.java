/**
 * Author: Isaac Aeshliman
 * Date: Mar 6, 2021
 * Description:
 *
 * TODO: 
 */

package aeshliman.minesweeper;

import java.awt.EventQueue;

public class Driver
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Window window = new Window();
					window.frame.setVisible(true);
				}
				catch(Exception e) { e.printStackTrace(); }
			}
		});
	}
}
