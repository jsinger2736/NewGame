import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class GoblinArcher extends Mob{
 public GoblinArcher(Board parenti, int x, int y){
  super(parenti, x, y);
  position[0]=x;
  position[1]=y;
  origin[0]=x;
  origin[1]=y;
  gold=parent.stats.goblinarchermaxgold;
  parent=parenti;
  type=8;
  name="Goblin Archer";
  timer=new Timer(parent.stats.goblinarcherspeed,this);
  timer.start();
 }

 public void actionPerformed(ActionEvent e){
  timer.setDelay(parent.stats.goblinarcherspeed);
  if (stab!=0){
   stab=0;
  }
  if (gold<=0){
   timer.stop();
   parent.enemies.remove(this);
   return;
  }
  regenerate();
  counter = !counter;
  if (counter){
   tryMove(home());
  }
 }

 public void interact(int xChange, int yChange){
  int orientation = 1;
  if (xChange==0 && yChange<0){
   lastMove=1;
   orientation=1;
  } else if (xChange>0 && yChange==0){
   lastMove=2;
   orientation=2;
  } else if (xChange==0 && yChange>0){
   lastMove=3;
   orientation=3;
  } else if (xChange<0 && yChange==0){
   lastMove=4;
   orientation=4;
  } else if (xChange>0 && yChange<0){
   lastMove=1;
   orientation=5;
  } else if (xChange>0 && yChange>0){
   lastMove=2;
   orientation=6;
  } else if (xChange<0 && yChange>0){
   lastMove=3;
   orientation=7;
  } else if (xChange<0 && yChange<0){
   lastMove=4;
   orientation=8;
  }
  if (Math.abs(xChange)+Math.abs(yChange)==1){
   stab = 1;
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
   for (int i=0; i<parent.allies.size(); i++){
    if (parent.allies.get(i).position[0]==target[0] && parent.allies.get(i).position[1]==target[1]){
     parent.allies.get(i).gold=parent.allies.get(i).gold-parent.stats.goblinarcherdamage;
     parent.player.gold=parent.player.gold+parent.stats.goblinarcherdamage;
    }
   }
  } else {
   parent.projectiles.add(new Arrow(parent,position[0],position[1],position[0]+xChange,position[1]+yChange,orientation,2,parent.stats.goblinarcherdamage,parent.stats.goblinarcherrange));
  }
 }

 public void regenerate(){
  regeneration++;
  if (regeneration>=parent.stats.goblinarcherregenerate){
   regeneration=0;
   if (gold<parent.stats.goblinarchermaxgold){
    gold++;
   }
  }
 }

 public int[] home(){
  double xChange = 500;
  double yChange = 500;
  double tempxChange = 500;
  double tempyChange = 500;
  for (int i=0; i<parent.allies.size(); i++){
   if (parent.allies.get(i).name.equals("Wall") || parent.allies.get(i).name.equals("Rock")){
    continue;
   }
   tempxChange=parent.allies.get(i).position[0]-position[0];//origin[0];
   tempyChange=parent.allies.get(i).position[1]-position[1];//origin[1];
   if ((int)Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<=(int)Math.sqrt(xChange*xChange+yChange*yChange)){
    xChange=tempxChange;
    yChange=tempyChange;
   }
  }/*
  if (xChange==radius/Math.sqrt(2) && yChange==radius/Math.sqrt(2)){
   return new int[]{origin[0],origin[1]};
  }*/
  tempxChange=parent.player.position[0]-position[0];
  tempyChange=parent.player.position[1]-position[1];
  if (Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<Math.sqrt(xChange*xChange+yChange*yChange)){
   xChange=tempxChange;
   yChange=tempyChange;
  }
  if ((int)Math.sqrt(xChange*xChange+yChange*yChange)<=parent.stats.goblinarcherrange){
   interact((int)xChange,(int)yChange);
   return new int[]{2736,2736};
  }
  return new int[]{position[0]+(int)xChange,position[1]+(int)yChange};
 }

 public void tryMove(int[] target){
  if (target[0]==2736 && target[1]==2736){
   return;
  }
  int[] direction = {1,2,3,4};
  int xChange = target[0]-position[0];
  int yChange = target[1]-position[1];
  if (xChange==1 && yChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=2;
   interact(xChange,yChange);
  } else if (xChange==-1 && yChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=4;
   interact(xChange,yChange);
  } else if (yChange==1 && xChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=3;
   interact(xChange,yChange);
  } else if (yChange==-1 && xChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=1;
   interact(xChange,yChange);
  } else if (!(target[0]==position[0] && target[1]==position[1] && position[0]==origin[0] && position[1]==origin[1])){
   if (xChange>0 && yChange<0){
    if (Math.random()>.5){
     direction = new int[]{1,2,4,3};
    } else {
     direction = new int[]{2,1,3,4};
    }
   } else if (xChange>0 && yChange>0){
    if (Math.random()>.5){
     direction = new int[]{2,3,1,4};
    } else {
     direction = new int[]{3,2,4,1};
    }
   } else if (xChange<0 && yChange<0){
    if (Math.random()>.5){
     direction = new int[]{1,4,2,3};
    } else {
     direction = new int[]{4,1,3,2};
    }
   } else if (xChange<0 && yChange>0){
    if (Math.random()>.5){
     direction = new int[]{4,3,1,2};
    } else {
     direction = new int[]{3,4,2,1};
    }
   } else if (xChange>0){
    if (Math.random()>.5){
     direction = new int[]{2,1,3,4};
    } else {
     direction = new int[]{2,3,1,4};
    }
   } else if (xChange<0){
    if (Math.random()>.5){
     direction = new int[]{4,1,3,2};
    } else {
     direction = new int[]{4,3,1,2};
    }
   } else if (yChange<0){
    if (Math.random()>.5){
     direction = new int[]{1,2,4,3};
    } else {
     direction = new int[]{1,4,2,3};
    }
   } else if (yChange>0){
    if (Math.random()>.5){
     direction = new int[]{3,2,4,1};
    } else {
     direction = new int[]{3,4,2,1};
    }
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
  int[] target = new int[2];
  if (direction==1){
   target[0]=position[0];
   target[1]=position[1]-1;
  } else if (direction==2){
   target[0]=position[0]+1;
   target[1]=position[1];
  } else if (direction==3){
   target[0]=position[0];
   target[1]=position[1]+1;
  } else if (direction==4){
   target[0]=position[0]-1;
   target[1]=position[1];
  }
  lastMove=direction;
  for (int i=0; i<parent.allies.size(); i++){
   if (parent.allies.get(i).position[0]==target[0] && parent.allies.get(i).position[1]==target[1]){
    return false;
   }
  }
  for (int i=0; i<parent.allies.size(); i++){
   if (parent.allies.get(i).position[0]==target[0] && parent.allies.get(i).position[1]==target[1]){
    return false;
   }
  }
  if (parent.player.position[0]==target[0] && parent.player.position[1]==target[1]){
   return false;
  }
  position[0]=target[0];
  position[1]=target[1];
  return true;
 }

}




















