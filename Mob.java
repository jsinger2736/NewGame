import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Mob implements ActionListener{
 Board parent;
 int[] origin = new int[2];
 int[] position = new int[2];
 int gold = 0;
 int maxgold = 0;
 int regeneration = 0;
 int regenerate = 0;
 Timer timer;
 int type = 0;
 int damage = 0;
 int radius = 10;
 int range = 5;
 String name = "Generic Mob";
 int lastMove = 3;
 int stab=0;
 boolean counter = false;
 int[] phantomPosition = new int[2];
 ArrayList<Integer> tempPath = new ArrayList<Integer>();
 ArrayList<Integer> path = new ArrayList<Integer>();
 double extravalue;
 
 public Mob(Board parenti, int x, int y){
  position[0]=x;
  position[1]=y;
  parent=parenti;
  timer = new Timer(500, this);
 }

 public void actionPerformed(ActionEvent e){
 }

 public static int allyIdentifier(String nameo){ //for placing allies
  if (nameo.equals("Wall")){
   return 0;
  } else if (nameo.equals("Rock")){
   return 1;
  } else if (nameo.equals("FootSoldier")){
   return 2;
  } else if (nameo.equals("Peasant")){
   return 3;
  } else if (nameo.equals("Bowman")){
   return 4;
  } else if (nameo.equals("Knight")){
   return 5;
  } else if (nameo.equals("Hunter")){
   return 6;
  } else {
   return 0;
  }
 }

}