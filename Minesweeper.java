import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Minesweeper 
{
	JFrame frame;
	JButton[][] buttons;
	JPanel buttonLayout;
	JButton restart;
	int[][] counts = new int[20][20];
	ButtonClick click;
	
	public Minesweeper()
	{
		frame = new JFrame();
		restart = new JButton("RESTART");
		buttonLayout = new JPanel(new GridLayout(20, 20));
		buttons = new JButton[20][20];
		click = new ButtonClick();
		int i;
		int j;

		restart.addMouseListener(click);
		frame.add(restart, BorderLayout.NORTH);
		for(i = 0; i < counts[0].length; i++)
		{
			for(j = 0; j < counts.length; j++)
			{
				buttons[i][j] = new JButton();
				buttonLayout.add(buttons[i][j]);
				buttons[i][j].addMouseListener(click);
				buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
			}
		}
		frame.add(buttonLayout);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.setLocationRelativeTo(null);							
		frame.setVisible(true);	
		setMines();
	}
	
	public void clearBoard()
	{
		int row;
		int col;
		
		for(row = 0; row < counts.length; row++)
		{
			for(col = 0; col < counts[0].length; col++)
			{
				buttons[row][col].setEnabled(false);
				
				if(counts[row][col] == 10)
				{
					buttons[row][col].setText("X");
					buttons[row][col].setBackground(Color.RED);
				}
				else if(counts[row][col] != 0)
				{
					buttons[row][col].setText(""+counts[row][col]);
				}
			}
		}
	}
	
	public void clearZeroes(int i, int j)
	{		
		for(int x = (i == 0) ? i : i-1; x < counts.length && x < i+2; x++)
		{
			for(int y = (j == 0) ? j : j-1; y < counts[0].length && y < j+2; y++)
			{
				if(counts[x][y] != 10 && buttons[x][y].isEnabled())
				{
					buttons[x][y].setEnabled(false);
					if(counts[x][y] == 0)
					{
						buttons[x][y].setText("");
						clearZeroes(x, y);
					}
					else
					{
						buttons[x][y].setText(""+counts[x][y]);
					}					
				}		
			}
		}
		
		
		
	}
	
	public void setMines()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		int i;
		int j;
		int mine;
		
		for(i = 0; i < counts.length; i++) 
		{
			for(j = 0; j < counts[0].length; j++) 
			{
				list.add(i*100+j);
			}
		}
		
		for(i = 0; i < 100; i++)
		{
			mine = (int) (Math.random() * list.size());	
			counts[list.get(mine)/100][list.get(mine)%100] = 10; //10 = mine
			list.remove(mine);
		}
		
		 for(i = 0; i < counts.length; i++) 
		 {
			 for(j = 0; j < counts[0].length; j++) 
			 {
				 if(counts[i][j] != 10) 
				 {
					int neighborcount = 0;
					 
					for(int x = (i == 0) ? i : i-1; x < counts.length && x < i+2; x++)
					{
						for(int y = (j == 0) ? j : j-1; y < counts[0].length && y < j+2; y++)
						{
							if(counts[x][y] == 10)
							{
								neighborcount++;
							}	
						}
					}
					counts[i][j] = neighborcount;
				 }				 
			 }
		 }
		
		for(i = 0; i < counts.length; i++)
    	{
    		for(j = 0; j < counts[0].length; j++) 
    		{
    			System.out.print(counts[i][j]+" ");
    		}
    		System.out.println();
    	}
	}
	
	public boolean checkWin()
	{
		int i;
		int j;
		int count = 0;
		boolean check;
		
		for(i = 0; i < counts.length; i++)
		{
			for(j = 0; j < counts[0].length; j++)
			{
				if(buttons[i][j].getText().equals("Flag") && counts[i][j] == 10)
				{
					count++;
				}
			}
		}
		
		if(count == 30)
		{
			check = true;
		}
		else
		{
			check = false;
		}
		
		return check;
	}
	
	public void restart()
	{
		int i;
		int j;
		
		for(i = 0; i < buttons.length; i++)
		{
			for(j = 0; j < buttons[0].length; j++)
			{
				buttons[i][j].setText("");
				buttons[i][j].setBackground(null);
				buttons[i][j].setEnabled(true);
				counts[i][j] = 0;
			}
		}
		
		setMines();
	}
	
	public void freezeBoard()
	{
		int i;
		int j;
		
		for(i = 0; i < buttons.length; i++)
		{
			for(j = 0; j < buttons[0].length; j++)
			{
				buttons[i][j].setEnabled(false);
			}
		}
	}
	
	private class ButtonClick implements MouseListener 
	{
		public void mouseClicked(MouseEvent arg0) 
		{
			JButton hold = (JButton) arg0.getSource();
			int row;
			int col;
			boolean check = false;
			if(hold.getText().equals("RESTART"))
			{
				restart();
			}
			
			for(row = 0; row < buttons.length && !check; row++) 
			{
				for(col = 0; col < buttons[0].length && !check; col++)
				{
					if(hold.equals(buttons[row][col]))
					{
						check = true;
							
						if(SwingUtilities.isLeftMouseButton(arg0))
						{	
							if(hold.isEnabled() && hold.getText().equals(""))
							{
								hold.setEnabled(false);
								if(counts[row][col] == 10)
								{
									clearBoard();
									hold.setBackground(Color.RED);
									JOptionPane.showMessageDialog(frame, "YOU LOSE!");
								}
								else if(counts[row][col] == 0)
								{
									clearZeroes(row, col);
								}	
								else 
								{
									hold.setText(""+counts[row][col]);		
									if(checkWin())
									{
										JOptionPane.showMessageDialog(frame, "YOU WIN!");
									}	
								}
							}
							else if(hold.getText().equals("Flag"))
							{
								hold.setText("");
							}		
							
						}
						else
						{
							if(hold.isEnabled())
							{
								hold.setText("Flag");
								checkWin();	
							}
						}
					}
				}
			}
		}

		public void mouseEntered(MouseEvent arg0) 
		{	
		}		
		public void mouseExited(MouseEvent arg0) 
		{				
		}
		public void mousePressed(MouseEvent arg0) 
		{	
		}
		
		public void mouseReleased(MouseEvent arg0) 
		{	
		}
	}
	
    public static void main(String[] args)
    {
    	new Minesweeper();
    	
    }
}