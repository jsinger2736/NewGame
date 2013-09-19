
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NewGame extends JFrame{
 JMenuBar menubar;
 JMenu file, options;
 JMenuItem newgame, exit, allychoice;
 JLabel statusbar;
 Board board;
 MenuHandler menuhandler;
 int allyChoice = 0;
 
 public NewGame(){
  menubar = new JMenuBar();
  menuhandler = new MenuHandler();
  file = new JMenu("File");
  newgame = new JMenuItem("New Game");
  newgame.addActionListener(menuhandler);
  exit = new JMenuItem("Exit");
  exit.addActionListener(menuhandler);
  file.add(newgame);
  file.add(exit);
  menubar.add(file);
  options = new JMenu("Options");
  allychoice = new JMenuItem("Choice of Ally");
  allychoice.addActionListener(menuhandler);
  options.add(allychoice);
  menubar.add(options);
  statusbar = new JLabel("statusbar", SwingConstants.CENTER);
  add(statusbar, BorderLayout.SOUTH);
  board = new Board(this);
  add(board);
  setJMenuBar(menubar);
  setSize(board.boardWidth*21,board.boardHeight*21+30);
  setExtendedState(JFrame.MAXIMIZED_BOTH);
  setTitle("NewGame");
  setDefaultCloseOperation(EXIT_ON_CLOSE);
 }

 public JLabel getStatusBar(){
  return statusbar;
 }

 public static void main(String[] args){
  NewGame game = new NewGame();
  game.setLocationRelativeTo(null);
  game.setVisible(true);
  //game.board.start();
 }

 public void allyMenu(){
  if (!board.isPaused){
   board.pause();
  }
  Object[] list = new Object[7];
  list[0]="Wall";
  list[1]="Rock";
  list[2]="FootSoldier";
  list[3]="Peasant";
  list[4]="Bowman";
  list[5]="Knight";
  list[6]="Hunter";
  try{
   String choice = JOptionPane.showInputDialog(null,"Choose the ally you'd like to spawn.","Choice of Ally",JOptionPane.PLAIN_MESSAGE,null,list,list[allyChoice]).toString();
   allyChoice = Mob.allyIdentifier(choice);
   if (board.isPaused){
    board.pause();
   }
  } catch (Exception exception){
   System.out.println(exception);
   if (board.isPaused){
    board.pause();
   }
  }
 }

 public class MenuHandler implements ActionListener{
  public void actionPerformed(ActionEvent e){
   Object source = e.getSource();
   if (source==newgame){
    board.restart();
   }
   if (source==exit){
    System.exit(0);
   }
   if (source==allychoice){
    allyMenu();
   }
  }
 }
}


















