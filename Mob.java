import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Mob implements ActionListener{
 Board parent;
 int[] origin = new int[2];
 int[] position = new int[2];
 int gold = 0;
 Timer timer;
 int type = 0;
 String name = "Generic Mob";
 int lastMove = 1;
 int stab=0;
 boolean counter = false;
 
 public Mob(Board parenti, int x, int y){
  position[0]=x;
  position[1]=y;
  parent=parenti;
  timer = new Timer(500, this);
 }

 public void actionPerformed(ActionEvent e){
 }
}