import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Goblin extends Mob{
 String name = "Goblin";

 public Goblin(Board parenti, int x, int y){
  super(parenti, x, y);
  position[0]=x;
  position[1]=y;
  origin[0]=x;
  origin[1]=y;
  gold = 5;
  parent=parenti;
  type=2;
  timer = new Timer(250,this);
  timer.start();
 }

 public void actionPerformed(ActionEvent e){
  if (stab!=0){
   stab=0;
  }
  if (gold<=0){
   timer.stop();
   parent.enemies.remove(this);
   return;
  }
  counter = !counter;
  if (counter){
   tryMove(parent.player.position);
  }
 }

 public void interact(){
  stab = 1;
  if (lastMove==1){
   if (parent.player.position[0]==position[0] && parent.player.position[1]==position[1]-1){
    parent.player.gold=parent.player.gold-1;
   }
  } else if (lastMove==2){
   if (parent.player.position[0]==position[0]+1 && parent.player.position[1]==position[1]){
    parent.player.gold=parent.player.gold-1;
   }
  } else if (lastMove==3){
   if (parent.player.position[0]==position[0] && parent.player.position[1]==position[1]+1){
    parent.player.gold=parent.player.gold-1;
   }
  } else if (lastMove==4){
   if (parent.player.position[0]==position[0]-1 && parent.player.position[1]==position[1]){
    parent.player.gold=parent.player.gold-1;
   }
  }
 }

 public void tryMove(int[] target){
  int[] direction = {1,2,3,4};
  int xChange = target[0]-position[0];
  int yChange = target[1]-position[1];
  if (xChange==1 && yChange==0){
   lastMove=2;
   interact();
  } else if (xChange==-1 && yChange==0){
   lastMove=4;
   interact();
  } else if (yChange==1 && xChange==0){
   lastMove=3;
   interact();
  } else if (yChange==-1 && xChange==0){
   lastMove=1;
   interact();
  } else {
   if (xChange>0 && yChange<0){
    if (Math.random()>.5){
     direction = new int[]{1,2,4,3};
    } else {
     direction = new int[]{2,1,4,3};
    }
   } else if (xChange>0 && yChange>0){
    direction = new int[]{2,3,1,4};
   } else if (xChange<0 && yChange<0){
    if (Math.random()>.5){
     direction = new int[]{1,4,2,3};
    } else {
     direction = new int[]{4,1,2,3};
    }
   } else if (xChange<0 && yChange>0){
    direction = new int[]{4,3,1,2};
   } else if (xChange>0){
    direction = new int[]{2,1,3,4};
   } else if (xChange<0){
    direction = new int[]{4,1,3,2};
   } else if (yChange<0){
    direction = new int[]{1,2,4,3};
   } else if (yChange>0){
    direction = new int[]{3,2,4,1};
   }
   int temp;
   for (int i=0; i<4; i++){
    if (attempt(direction[i])){
     break;
    }
   }
  }
 }

 public boolean attempt(int direction){
  
  if (direction==1){
   position[1]--;
   lastMove=1;
   return true;
  } else if (direction==2){
   position[0]++;
   lastMove=2;
   return true;
  } else if (direction==3){
   position[1]++;
   lastMove=3;
   return true;
  } else if (direction==4){
   position[0]--;
   lastMove=4;
   return true;
  }
  return false;
 }

}