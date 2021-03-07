/**
 * Author: Isaac Aeshliman
 * Date: Mar 6, 2021
 * Description:
 *
 * TODO: 
 */

package aeshliman.minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.FlowLayout;
import javax.swing.JLayeredPane;

public class Window
{
	// Constants
	private final Dimension CELL_DIMENSION = new Dimension(30, 30);
	private final int BEGINNER_WIDTH = 10;
	private final int BEGINNER_HEIGHT = 10;
	private final int BEGINNER_MINE_COUNT = 10;
	private final int INTERMEDIATE_WIDTH = 16;
	private final int INTERMEDIATE_HEIGHT = 16;
	private final int INTERMEDIATE_MINE_COUNT = 40;
	private final int EXPERT_WIDTH = 30;
	private final int EXPERT_HEIGHT = 16;
	private final int EXPERT_MINE_COUNT = 99;
	
	// Instance Variables
	protected JFrame frame;
	private JLayeredPane layerPanel;
	private JMinePanel[][] grid;
	private Difficulty difficulty;
	private JLabel mineLabel;
	private JLabel timeLabel;
	private JPanel contentPanel;
	private JLabel resultLabel;
	private JButton restartButton;
	private int time;
	private int totalMines;
	private int minesLeft;
	private int clickedCount;
	private boolean started;

	/**
	 * Create the application.
	 */
	public Window()
	{
		initialize();
		int choice = JOptionPane.showOptionDialog(null, "Select a difficulty","",
				JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,
				Difficulty.values(),Difficulty.BEGINNER);
		difficulty = Difficulty.values()[choice];
		initializeGrid();
		initializeCells();
		run();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setTitle("MineSweeper");
		frame.setBounds(100, 100, 720, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel headPanel = new JPanel();
		headPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frame.getContentPane().add(headPanel, BorderLayout.NORTH);
		headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.X_AXIS));
		
		Component leftStrut = Box.createHorizontalStrut(20);
		headPanel.add(leftStrut);
		
		mineLabel = new JLabel("999");
		mineLabel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		mineLabel.setPreferredSize(new Dimension(50, 25));
		mineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headPanel.add(mineLabel);
		
		Component leftGlue = Box.createHorizontalGlue();
		headPanel.add(leftGlue);		
		
		restartButton = new JButton("Restart");
		restartButton.setMinimumSize(new Dimension(80, 25));
		restartButton.setMaximumSize(new Dimension(80, 25));
		restartButton.setPreferredSize(new Dimension(80, 25));
		restartButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				started = false;
				resultLabel.setText("");
				contentPanel.removeAll();
				contentPanel.revalidate();
				contentPanel.repaint();
				initializeGrid();
				initializeCells();
			}
		});
		headPanel.add(restartButton);
		
		Component rightGlue = Box.createHorizontalGlue();
		headPanel.add(rightGlue);
		
		timeLabel = new JLabel("999");
		timeLabel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		timeLabel.setPreferredSize(new Dimension(50, 25));
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headPanel.add(timeLabel);
		
		Component rightStrut = Box.createHorizontalStrut(20);
		headPanel.add(rightStrut);
		
		JPanel paddingPanel = new JPanel();
		frame.getContentPane().add(paddingPanel, BorderLayout.CENTER);
		paddingPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		layerPanel = new JLayeredPane();
		layerPanel.setSize(new Dimension(200, 200));
		paddingPanel.add(layerPanel);
		layerPanel.setLayout(new BoxLayout(layerPanel, BoxLayout.Y_AXIS));
		
		contentPanel = new JPanel();
		layerPanel.add(contentPanel);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		resultLabel = new JLabel("");
		layerPanel.setLayer(resultLabel, 1);
		resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		layerPanel.add(resultLabel);
	}
	
	private void initializeGrid()
	{
		switch(difficulty) // Initializes grid and layout to correct dimensions
		{
		case BEGINNER:
			grid = new JMinePanel[BEGINNER_HEIGHT][BEGINNER_WIDTH];
			contentPanel.setLayout(new GridLayout(BEGINNER_HEIGHT, BEGINNER_WIDTH, 0, 0));
			mineLabel.setText(Integer.toString(BEGINNER_MINE_COUNT));
			frame.setBounds(100, 100, CELL_DIMENSION.width*(BEGINNER_WIDTH+1), CELL_DIMENSION.height*(BEGINNER_HEIGHT+3));
			layerPanel.setBounds(100, 100, CELL_DIMENSION.width*BEGINNER_WIDTH, CELL_DIMENSION.height*BEGINNER_HEIGHT);
			totalMines = BEGINNER_MINE_COUNT;
			break;
		case INTERMEDIATE:
			grid = new JMinePanel[INTERMEDIATE_HEIGHT][INTERMEDIATE_WIDTH];
			contentPanel.setLayout(new GridLayout(INTERMEDIATE_HEIGHT, INTERMEDIATE_WIDTH, 0, 0));
			mineLabel.setText(Integer.toString(INTERMEDIATE_MINE_COUNT));
			frame.setBounds(100, 100, CELL_DIMENSION.width*(INTERMEDIATE_WIDTH+1), CELL_DIMENSION.height*(INTERMEDIATE_HEIGHT+3));
			layerPanel.setBounds(100, 100, CELL_DIMENSION.width*INTERMEDIATE_WIDTH, CELL_DIMENSION.height*INTERMEDIATE_HEIGHT);
			totalMines = INTERMEDIATE_MINE_COUNT;
			break;
		case EXPERT:
			grid = new JMinePanel[EXPERT_HEIGHT][EXPERT_WIDTH];
			contentPanel.setLayout(new GridLayout(EXPERT_HEIGHT, EXPERT_WIDTH, 0, 0));
			mineLabel.setText(Integer.toString(EXPERT_MINE_COUNT));
			frame.setBounds(100, 100, CELL_DIMENSION.width*(EXPERT_WIDTH+1), CELL_DIMENSION.height*(EXPERT_HEIGHT+3));
			layerPanel.setBounds(100, 100, CELL_DIMENSION.width*EXPERT_WIDTH, CELL_DIMENSION.height*EXPERT_HEIGHT);
			totalMines = EXPERT_MINE_COUNT;
			break;
		}
		started = false;
		time = 999;
		timeLabel.setText(Integer.toString(time));
		
		for(int i=0; i<grid.length; i++) // Adds a panel to each grid cell
		{
			for(int j=0; j<grid[i].length; j++)
			{
				JMinePanel panel = new JMinePanel();
				panel.setMaximumSize(CELL_DIMENSION);
				panel.setPreferredSize(CELL_DIMENSION);
				panel.setMinimumSize(CELL_DIMENSION);
				panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
				panel.setLayout(new GridLayout(1, 1, 0, 0));
				panel.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e) // Adds one time click event to each panel
					{
						if(!started) started = true;
						JMinePanel panel = (JMinePanel)e.getSource();
						if(SwingUtilities.isLeftMouseButton(e)) // On left click reveal cell below
						{
							if(!panel.hasFlag())
							{
								JLabel label = new JLabel();
								if(panel.isMine()) { label.setText("B"); }
								else { label.setText(Integer.toString(panel.getValue())); }
								label.setHorizontalAlignment(SwingConstants.CENTER);
								panel.setClicked(true);
								panel.removeAll();
								panel.add(label);
								panel.setBorder(null);
								panel.revalidate();
								panel.repaint();
								panel.removeMouseListener(this);
								if(panel.isMine())
								{
									// TODO: Game Over - Lose
									started = false;
									removeAllEvents();
									uncoverAllBombs();
									resultLabel.setText("Game Over - You Lose");
								}
								else
								{
									// TODO: Game Over - Win
									if(++clickedCount==(grid.length*grid[0].length-totalMines))
									{
										started = false;
										removeAllEvents();
										resultLabel.setText("Game Over - You Win");
									}
								}
								if(panel.getValue()==0) clickAdjacent(panel);
							}
						}
						else if(SwingUtilities.isRightMouseButton(e))
						{
							panel.setFlag(!panel.hasFlag());
							if(panel.hasFlag())
							{
								minesLeft--;
								JLabel label = new JLabel("?");
								label.setHorizontalAlignment(SwingConstants.CENTER);
								panel.add(label);
							}
							else
							{
								minesLeft++;
								panel.removeAll();
							}
							panel.revalidate();
							panel.repaint();
							mineLabel.setText(Integer.toString(minesLeft));
						}
					}
				});
				contentPanel.add(panel);
				grid[i][j] = panel;
			}
		}
	}
	
	private void initializeCells()
	{
		Random ran = new Random();
		clickedCount = 0;
		minesLeft = 0;
		while(minesLeft<totalMines)
		{
			int x = ran.nextInt(grid.length);
			int y = ran.nextInt(grid[0].length);
			if(!grid[x][y].isMine())
			{
				grid[x][y].setMine(true);
				//grid[x][y].setBackground(Color.CYAN); // Uncomment to color all mine cells blue for debugging
				minesLeft++;
			}
		}
		for(int i=0; i<grid.length; i++)
		{
			for(int j=0; j<grid[i].length; j++)
			{
				if(grid[i][j].isMine()) continue;
				int value = 0;
				try { if(grid[i-1][j-1].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				try { if(grid[i-1][j].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				try { if(grid[i-1][j+1].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				try { if(grid[i][j-1].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				try { if(grid[i][j+1].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				try { if(grid[i+1][j-1].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				try { if(grid[i+1][j].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				try { if(grid[i+1][j+1].isMine()) value++; }
				catch(ArrayIndexOutOfBoundsException e) {  }
				grid[i][j].setValue(value);
			}
		}
	}
	
	private void run()
	{
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
		    public void run()
		    {
		    	if(started) timeLabel.setText(Integer.toString(--time));
		    }
		},1000/1,1000/1);
	}
	
	private void removeAllEvents()
	{
		for(JMinePanel[] out : grid)
			for(JMinePanel in : out)
				for(MouseListener listener : in.getMouseListeners())
					in.removeMouseListener(listener);
	}
	
	private void uncoverAllBombs()
	{
		for(JMinePanel[] out : grid)
		{
			for(JMinePanel in : out)
			{
				if(in.isMine())
				{
					JLabel label = new JLabel();
					label.setText("B");
					label.setHorizontalAlignment(SwingConstants.CENTER);
					in.removeAll();
					in.add(label);
					in.setBorder(null);
					in.revalidate();
					in.repaint();
				}
			}
		}
	}
	
	private void clickAdjacent(JMinePanel panel)
	{
		int[][] p = findPanel(panel);
		clickGrid(p[0][0]-1,p[0][1]);
		clickGrid(p[0][0],p[0][1]-1);
		clickGrid(p[0][0],p[0][1]+1);
		clickGrid(p[0][0]+1,p[0][1]);
	}
	
	private void clickGrid(int x, int y)
	{
		try
		{
			JMinePanel panel = grid[x][y];
			if(panel.isClicked()) return;
			if(!panel.hasFlag())
			{
				JLabel label = new JLabel(Integer.toString(panel.getValue()));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				panel.setClicked(true);
				panel.removeAll();
				panel.add(label);
				panel.setBorder(null);
				panel.revalidate();
				panel.repaint();
				for(MouseListener listener : panel.getMouseListeners()) panel.removeMouseListener(listener);
				if(++clickedCount==(grid.length*grid[0].length-totalMines))
				{
					removeAllEvents();
					resultLabel.setText("Game Over - You Win");
				}
			}
			if(panel.getValue()==0) clickAdjacent(panel);
		}
		catch(ArrayIndexOutOfBoundsException e) {  }
	}
	
	private int[][] findPanel(JMinePanel panel)
	{
		for(int i=0; i<grid.length; i++)
			for(int j=0; j<grid[i].length; j++)
				if(grid[i][j]==panel) return new int[][]{{i,j}};
		return null;
	}
}
