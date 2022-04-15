package clueGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class GameControlPanel extends JPanel implements ActionListener {
	
	private JTextField guessLabel; 
	private JTextField guessResultLabel; 
	private JTextField rollNumber; 
	private JTextField turn; 
	
	private int roll, playerTurn;
	
	private static Board board = null;
	
	public GameControlPanel() {
		super();
		addMouseListener(board);
        playerTurn = -1;
		setUpObjects();
	}
	
	public GameControlPanel(Board board) {
		this.board = board;
		setUpObjects();
	}
	
	public void setUpObjects() {
		// 2 rows
		setLayout(new GridLayout(2, 0));
		
		// top panel (1 row, 4 columns)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 4));

        // this adds the first panel to the top panel
        JPanel turnPanel = createTurnPanel();
        topPanel.add(turnPanel);

        // adds the second panel to the top panel
        JPanel rollPanel = createRollPanel();
        topPanel.add(rollPanel); 

        // adds third panel to top panel
        JPanel button1 = createButton1();
    	topPanel.add(button1);
        
        // adds fourth panel to the top panel
        JPanel button2 = createButton2();
    	topPanel.add(button2);
        
        // adds top panel to the Layout
        add(topPanel);
        
        
        // bottom panel (2 columns)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(0, 2));
        
        // adds first panel to bottom panel
        JPanel border1 = createBorder1();
        bottomPanel.add(border1);
        
        // adds second panel to the bottom panel
        JPanel border2 = createBorder2();
        bottomPanel.add(border2);
        
        // adds bottom panel to the Layout
        add(bottomPanel);
	}
	
	private JPanel createTurnPanel() {
		// first panel in the top panel (2 rows)
        JPanel turnPanel = new JPanel(new GridLayout(2, 0));
        
        // first sub-panel in the turn panel
        JLabel label = new JLabel("Whose turn?", SwingConstants.CENTER);
        turnPanel.add(label);
        
        // second sub-panel in the turn panel
        turn = new JTextField();
        turn.setEditable(false);
        turnPanel.add(turn);
        return turnPanel;
	}
	
	private JPanel createRollPanel() {
		// second panel in the top panel 
        JPanel rollPanel = new JPanel(new GridLayout(2, 0)); 
		
		// first sub-panel in the roll panel
        JPanel topRoll = new JPanel();
        JLabel roll = new JLabel("Roll:", SwingConstants.RIGHT);
        topRoll.add(roll);
        
        rollNumber = new JTextField(10);
        rollNumber.setEditable(false);
        topRoll.add(rollNumber);
        rollPanel.add(topRoll);
        
        // second sub-panel in the roll panel
        JPanel blanks = new JPanel();
        JLabel blank1 = new JLabel(" ");
        blanks.add(blank1);
        rollPanel.add(blanks);
        return rollPanel;
	}
	
	private JPanel createButton1() {
		// third panel in the top panel
        JPanel button1 = new JPanel();

        JButton	accusation = new JButton("Make Accusation");
        button1.setLayout(new BorderLayout(0, 0));
        button1.add(accusation);
    	return button1;
	}
	
	private JPanel createButton2() {
		// fourth panel in the top panel
        JPanel button2 = new JPanel();
		
        JButton next = new JButton("NEXT!");
        button2.setLayout(new BorderLayout(0, 0));
    	button2.add(next);
    	return button2;
	}
	
	private JPanel createBorder1() {
		// first panel in the bottom panel
        JPanel border1 = new JPanel(new GridLayout(1, 0));
        
        this.guessLabel = new JTextField();
        border1.add(guessLabel);
        border1.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
        return border1;
	}
	
	private JPanel createBorder2() {
		 // second panel in the bottom panel
        JPanel border2 = new JPanel(new GridLayout(1, 0));
        
        this.guessResultLabel = new JTextField();
        border2.add(guessResultLabel);	
        border2.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		return border2;
	}
	
	public void setTurn(Player p, int roll) {
		turn.setText(p.getPlayerName());
		turn.setBackground(p.getColor());
		rollNumber.setText(String.valueOf(roll));
	}
	
	public void setGuess(Player p, String s) {
		guessLabel.setText(s);
		guessLabel.setBackground(p.getColor());
		
	}
	
	public void setGuessResult(Player p, String s) {
		guessResultLabel.setText(s);
		guessResultLabel.setBackground(p.getColor());
	}
	
	public void rollDice() {
    	int min = 1;
    	int max = 6;
    	roll = (int)Math.floor(Math.random() * (max - min + 1) + min);
    }
	
	public void handleNextTurn() {
    	playerTurn++;
    	if (playerTurn > 5) {
    		playerTurn = 0;
    	}
    	int row = board.getPlayers().get(playerTurn).getRow();
    	int col = board.getPlayers().get(playerTurn).getColumn();
    	
    	rollDice();
    	BoardCell c = board.getCell(row, col);
    	board.calcTargets(c, roll);
    	if (playerTurn != 0) {
    		ComputerPlayer pc = (ComputerPlayer) board.getPlayers().get(playerTurn);
    		BoardCell selected = pc.selectTarget(board, roll);
    		row = selected.getRow();
    		col = selected.getColumn();
    		pc.setLocation(row, col);
    		for (BoardCell target: board.getTargets()) {
    			target.setMarkedTarget(false);
    		}
    		setTurn(pc, roll);
    		setGuess(pc, "PLACEHOLDER");
    		setGuessResult(pc, "PLACEHOLDER");
    	}
    	
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("NEXT!")) {
			if (board != null) {
				handleNextTurn();
			}
		}
		repaint();
		
	}
	
	public static void main(String[] args) {
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GameControlPanel");
		frame.setSize(800, 130);
		GameControlPanel gui = new GameControlPanel();
		
		ComputerPlayer p = new ComputerPlayer("Marvin", Color.decode("#FFC080"), 19, 0);
		gui.setTurn(p, 5);
		gui.setGuess(p, "I have no guess!");
		gui.setGuessResult(p, "So you have nothing?");
		
		frame.add(gui, BorderLayout.CENTER);
		frame.setVisible(true);
	}
}