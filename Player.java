import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Player extends Mob{

 public Player(Board parenti, int x, int y){
  super(parenti, x, y);
  position[0]=x;
  position[1]=y;
  gold = 0;
  parent=parenti;
  type=1;
  damage=1;
  name = "You";
  timer = new Timer(200,this);
 }

 public void actionPerformed(ActionEvent e){
  if (stab!=0){
   stab=0;
  }
  timer.stop();
 }

 public void tryMove(int direction, boolean move){
  int[] target = new int[2];
  int[] behindtarget = new int[2];
  if (direction==1){
   target[0]=position[0];
   target[1]=position[1]-1;
   behindtarget[0]=position[0];
   behindtarget[1]=position[1]-2;
   lastMove=1;
  } else if (direction==2){
   target[0]=position[0]+1;
   target[1]=position[1];
   behindtarget[0]=position[0]+2;
   behindtarget[1]=position[1];
   lastMove=2;
  } else if (direction==3){
   target[0]=position[0];
   target[1]=position[1]+1;
   behindtarget[0]=position[0];
   behindtarget[1]=position[1]+2;
   lastMove=3;
  } else if (direction==4){
   target[0]=position[0]-1;
   target[1]=position[1];
   behindtarget[0]=position[0]-2;
   behindtarget[1]=position[1];
   lastMove=4;
  }
  if (move){
   for (int i=0; i<parent.enemies.size(); i++){
    if (parent.enemies.get(i).position[0]==target[0] && parent.enemies.get(i).position[1]==target[1]){
     return;
    }
   }
   for (int i=0; i<parent.allies.size(); i++){
    if (parent.allies.get(i).position[0]==target[0] && parent.allies.get(i).position[1]==target[1]){
     if (parent.allies.get(i).name.equals("Wall")){
      return;
     }
     if (parent.allies.get(i).name.equals("Rock")){
      for (int j=0; j<parent.enemies.size(); j++){
       if (parent.enemies.get(j).position[0]==behindtarget[0] && parent.enemies.get(j).position[1]==behindtarget[1]){
        return;
       }
      }
      for (int j=0; j<parent.allies.size(); j++){
       if (parent.allies.get(j).position[0]==behindtarget[0] && parent.allies.get(j).position[1]==behindtarget[1]){
        return;
       }
      }
      parent.allies.get(i).position[0]=behindtarget[0];
      parent.allies.get(i).position[1]=behindtarget[1];
      break;
     }
    }
   }
   position[0]=target[0];
   position[1]=target[1];
  }
/*
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
    for (int i=0; i<parent.allies.size(); i++){
     if (parent.allies.get(i).position[0]==position[0] && parent.allies.get(i).position[1]==position[1]-1){
      if (parent.allies.get(i).name.equals("Wall")){
       return;
      }
     }
    }
    position[1] = position[1]-1;
   } else if (direction==2){           //RIGHT
    for (int i=0; i<parent.enemies.size(); i++){
     if (parent.enemies.get(i).position[0]==position[0]+1 && parent.enemies.get(i).position[1]==position[1]){
      return;
     }
    }
    for (int i=0; i<parent.allies.size(); i++){
     if (parent.allies.get(i).position[0]==position[0]+1 && parent.allies.get(i).position[1]==position[1]){
      if (parent.allies.get(i).name.equals("Wall")){
       return;
      }
     }
    }
    position[0] = position[0]+1;
   } else if (direction==3){           //DOWN
    for (int i=0; i<parent.enemies.size(); i++){
     if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]+1){
      return;
     }
    }
    for (int i=0; i<parent.allies.size(); i++){
     if (parent.allies.get(i).position[0]==position[0] && parent.allies.get(i).position[1]==position[1]+1){
      if (parent.allies.get(i).name.equals("Wall")){
       return;
      }
     }
    }
    position[1] = position[1]+1;
   } else if (direction==4){           //LEFT
    for (int i=0; i<parent.enemies.size(); i++){
     if (parent.enemies.get(i).position[0]==position[0]-1 && parent.enemies.get(i).position[1]==position[1]){
      return;
     }
    }
    for (int i=0; i<parent.allies.size(); i++){
     if (parent.allies.get(i).position[0]==position[0]-1 && parent.allies.get(i).position[1]==position[1]){
      if (parent.allies.get(i).name.equals("Wall")){
       return;
      }
     }
    }
    position[0] = position[0]-1;
   }
  }*/
 }

 public void interact(){//// FIX SO USES TARGET INT[]
  if (stab!=0){
   return;
  }
  stab = parent.weapon;
  int[] target = new int[2];
  if (lastMove==1){
   target[0]=position[0];
   target[1]=position[1]-1;
  } else if (lastMove==2){
   target[0]=position[0]+1;
   target[1]=position[1];
  } else if (lastMove==3){
   target[0]=position[0];
   target[1]=position[1]+1;
  } else if (lastMove==4){
   target[0]=position[0]-1;
   target[1]=position[1];
  }
  for (int i=0; i<parent.enemies.size(); i++){
   if (parent.enemies.get(i).position[0]==target[0] && parent.enemies.get(i).position[1]==target[1]){
    parent.enemies.get(i).gold=parent.enemies.get(i).gold-parent.stats.playerdamage;
    parent.player.gold=parent.player.gold+parent.stats.playerdamage;
   }
  }
  timer.setDelay(parent.stats.playerspeed);
  timer.start();

  /*
  stab=parent.weapon; 
  if (stab==1){
   for (int i=0; i<parent.enemies.size(); i++){
    if (lastMove==1){
     if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]-1){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-damage;
      gold = gold+damage;
     }
    } else if (lastMove==2){
     if (parent.enemies.get(i).position[0]==position[0]+1 && parent.enemies.get(i).position[1]==position[1]){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-damage;
      gold = gold+damage;
     }
    } else if (lastMove==3){
     if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]+1){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-damage;
      gold = gold+damage;
     }
    } else if (lastMove==4){
     if (parent.enemies.get(i).position[0]==position[0]-1 && parent.enemies.get(i).position[1]==position[1]){
      parent.enemies.get(i).gold=parent.enemies.get(i).gold-damage;
      gold = gold+damage;
     }
    }
   }
   timer.setDelay(200);
  }
  timer.start();*/
 }

}