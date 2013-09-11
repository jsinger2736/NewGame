
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NewGame extends JFrame{
 JMenuBar menubar;
 JMenu file;
 JMenuItem exit;
 JLabel statusbar;
 Board board;
 MenuHandler menuhandler;
 
 public NewGame(){
  menubar = new JMenuBar();
  menuhandler = new MenuHandler();
  file = new JMenu("File");
  exit = new JMenuItem("Exit");
  exit.addActionListener(menuhandler);
  file.add(exit);
  menubar.add(file);
  statusbar = new JLabel("statusbar", SwingConstants.CENTER);
  add(statusbar, BorderLayout.SOUTH);
  board = new Board(this);
  add(board);
  setJMenuBar(menubar);
  setSize(board.boardWidth*20,board.boardHeight*20+30);
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
  game.board.start();
 }

 public class MenuHandler implements ActionListener{
  public void actionPerformed(ActionEvent e){
   Object source = e.getSource();
   if (source==exit){
    System.exit(0);
   }
  }
 }
}


















