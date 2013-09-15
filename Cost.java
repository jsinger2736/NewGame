import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Cost implements ActionListener{
 Board parent;
 int lifetime = 0;
 Timer timer;

 public Cost(Board parenti){
  parent=parenti;
  timer = new Timer(1000,this);
  timer.start();
 }

 public void actionPerformed(ActionEvent e){
  lifetime++;
  if (lifetime>=2){
   timer.stop();
   parent.cost=null;
  }
 }

 public void drawCost(Graphics g, int halfwidth, int height){
  int choice=parent.parent.allyChoice;
  Color tcolor = new Color(0,0,0);
  Color fcolor = new Color(50,175,150);
  if (choice==0){
   g.setColor(fcolor);
   g.fillRect(halfwidth+choice*24+1,height-44,36,17);
   g.setColor(tcolor);
   g.drawRect(halfwidth+choice*24,height-45,37,18);
   g.drawString("5 gold",halfwidth+choice*24+2,height-31);
  } else if (choice==1){
   g.setColor(fcolor);
   g.fillRect(halfwidth+choice*24+1,height-44,36,17);
   g.setColor(tcolor);
   g.drawRect(halfwidth+choice*24,height-45,37,18);
   g.drawString("5 gold",halfwidth+choice*24+2,height-31);
  } else if (choice==2){
   g.setColor(fcolor);
   g.fillRect(halfwidth+choice*24+1,height-44,43,17);
   g.setColor(tcolor);
   g.drawRect(halfwidth+choice*24,height-45,44,18);
   g.drawString("10 gold",halfwidth+choice*24+2,height-31);
  } else if (choice==3){
   g.setColor(fcolor);
   g.fillRect(halfwidth+choice*24+1,height-44,43,17);
   g.setColor(tcolor);
   g.drawRect(halfwidth+choice*24,height-45,44,18);
   g.drawString("25 gold",halfwidth+choice*24+2,height-31);
  } else if (choice==4){
   g.setColor(fcolor);
   g.fillRect(halfwidth+choice*24+1,height-44,43,17);
   g.setColor(tcolor);
   g.drawRect(halfwidth+choice*24,height-45,44,18);
   g.drawString("40 gold",halfwidth+choice*24+2,height-31);
  }
 }

}