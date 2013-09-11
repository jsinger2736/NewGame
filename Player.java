import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Player extends Mob{
 String name = "You";

 public Player(Board parenti, int x, int y){
  super(parenti, x, y);
  position[0]=x;
  position[1]=y;
  gold = 0;
  parent=parenti;
  type=1;
  timer = new Timer(200,this);
 }

 public void actionPerformed(ActionEvent e){
  if (stab!=0){
   stab=0;
  }
  timer.stop();
 }

 public void tryMove(int direction, boolean move){
  if (direction==1){
   lastMove=1;
  } else if (direction==2){
   lastMove=2;
  } else if (direction==3){
   lastMove=3;
  } else if (direction==4){
   lastMove=4;
  }
  if (move){
   if (direction==1){                  //UP
    for (int i=0; i<parent.enemies.size(); i++){
     if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]-1){
      return;
     }
    }
    position[1] = position[1]-1;
   } else if (direction==2){           //RIGHT
    for (int i=0; i<parent.enemies.size(); i++){
     if (parent.enemies.get(i).position[0]==position[0]+1 && parent.enemies.get(i).position[1]==position[1]){
      return;
     }
    }
    position[0] = position[0]+1;
   } else if (direction==3){           //DOWN
    for (int i=0; i<parent.enemies.size(); i++){
     if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]+1){
      return;
     }
    }
    position[1] = position[1]+1;
   } else if (direction==4){           //LEFT
    for (int i=0; i<parent.enemies.size(); i++){
     if (parent.enemies.get(i).position[0]==position[0]-1 && parent.enemies.get(i).position[1]==position[1]){
      return;
     }
    }
    position[0] = position[0]-1;
   }
  }
 }

 public void interact(){
  stab=parent.weapon; 
  if (stab==1){
   for (int i=0; i<parent.enemies.size(); i++){
    if (lastMove==1){
     if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]-1){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-1;
      gold = gold+1;
     }
    } else if (lastMove==2){
     if (parent.enemies.get(i).position[0]==position[0]+1 && parent.enemies.get(i).position[1]==position[1]){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-1;
      gold = gold+1;
     }
    } else if (lastMove==3){
     if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]+1){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-1;
      gold = gold+1;
     }
    } else if (lastMove==4){
     if (parent.enemies.get(i).position[0]==position[0]-1 && parent.enemies.get(i).position[1]==position[1]){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-1;
      gold = gold+1;
     }
    }
   }
   timer.setDelay(200);
  }
  timer.start();
 }

}