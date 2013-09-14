import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Rock extends Mob{
 
 public Rock(Board parenti, int x, int y){
  super(parenti, x, y);
  position[0]=x;
  position[1]=y;
  origin[0]=x;
  origin[1]=y;
  gold=3;
  parent=parenti;
  type=6;
  radius=2;
  name="Rock";
  timer=new Timer(200,this);
  timer.start();
 }

 public void actionPerformed(ActionEvent e){
  if (stab!=0){
   stab=0;
  }
  if (gold<=0){
   timer.stop();
   parent.allies.remove(this);
   return;
  }
 }
}










