import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Raider extends Mob{
 int stupidity = 0;

 public Raider(Board parenti, int x, int y){
  super(parenti, x, y);
  position[0]=x;
  position[1]=y;
  origin[0]=x;
  origin[1]=y;
  gold = 10;
  parent=parenti;
  type=9;
  //regenerate=24;
  //damage=1;
  name="Raider";
  timer = new Timer(parent.stats.raiderspeed,this);
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
  regenerate();
  counter = !counter;
  if (counter){
   tryMove(home(true));
  }
  //System.out.println("("+position[0]+","+position[1]+")");
 }

 public void interact(){
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
  if (parent.player.position[0]==target[0] && parent.player.position[1]==target[1]){
   parent.player.gold=parent.player.gold-parent.stats.raiderdamage;
  }
  for (int i=0; i<parent.allies.size(); i++){
   if (parent.allies.get(i).position[0]==target[0] && parent.allies.get(i).position[1]==target[1]){
    parent.allies.get(i).gold=parent.allies.get(i).gold-parent.stats.raiderdamage;
   }
  }
 }

 public void regenerate(){
  regeneration++;
  if (regeneration>=parent.stats.raiderregenerate){
   regeneration=0;
   if (gold<parent.stats.raidermaxgold){
    gold++;
   }
  }
 }

 public int[] home(boolean search){
  int xChange = 500;
  int yChange = 500;
  int tempxChange = 500;
  int tempyChange = 500;
  int[] target = new int[]{1,1};
  boolean found = false;
  if (search){
   for (int i=0; i<parent.allies.size(); i++){
    if (!parent.allies.get(i).name.equals("Peasant")){
     continue;
    }
    tempxChange=parent.allies.get(i).position[0]-position[0];
    tempyChange=parent.allies.get(i).position[1]-position[1];
    if (Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<Math.sqrt(xChange*xChange+yChange*yChange)){
      xChange=tempxChange;
     yChange=tempyChange;
    }
   }
   if (xChange!=500 || yChange!=500){
    target = new int[]{position[0]+xChange,position[1]+yChange};
    found = true;
   }
   for (int i=0; i<parent.allies.size(); i++){
    if (parent.allies.get(i).name.equals("Wall") || parent.allies.get(i).name.equals("Rock")){
     continue;
    }
    tempxChange=parent.allies.get(i).position[0]-position[0];
    tempyChange=parent.allies.get(i).position[1]-position[1];
    if (found){
     if (Math.abs(tempxChange)+Math.abs(tempyChange)==1){
      target = new int[]{position[0]+tempxChange,position[1]+tempyChange};
     }
    } else {
     if (Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<Math.sqrt(xChange*xChange+yChange*yChange)){
      xChange=tempxChange;
      yChange=tempyChange;
     }
    }
   }
   tempxChange=parent.player.position[0]-position[0];
   tempyChange=parent.player.position[1]-position[1];
   if (found){
    System.out.println("EY");
    if (Math.abs(tempxChange)+Math.abs(tempyChange)==1){
     target = new int[]{position[0]+tempxChange,position[1]+tempyChange};
     System.out.println("YO "+target[0]+","+target[1]);
    }
   } else {
    if (Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<Math.sqrt(xChange*xChange+yChange*yChange)){
     xChange=tempxChange;
     yChange=tempyChange;
    }
    target = new int[]{position[0]+xChange,position[1]+yChange};
   }
  } else {
   for (int i=0; i<parent.allies.size(); i++){
    if (parent.allies.get(i).name.equals("Wall") || parent.allies.get(i).name.equals("Rock")){
     continue;
    }
    tempxChange=parent.allies.get(i).position[0]-position[0];
    tempyChange=parent.allies.get(i).position[1]-position[1];
    if (Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<Math.sqrt(xChange*xChange+yChange*yChange)){
     xChange=tempxChange;
     yChange=tempyChange;
    }
   }
   tempxChange=parent.player.position[0]-position[0];
   tempyChange=parent.player.position[1]-position[1];
   if (Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<Math.sqrt(xChange*xChange+yChange*yChange)){
    xChange=tempxChange;
    yChange=tempyChange;
   }
   target = new int[]{position[0]+xChange,position[1]+yChange};
  }
  System.out.println(target[0]+","+target[1]);
  return target;
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public void tryMove(int[] target){
  int xChange = target[0]-position[0];
  int yChange = target[1]-position[1];
  if (xChange==1 && yChange==0){
   lastMove=2;
   interact();
   return;
  } else if (xChange==-1 && yChange==0){
   lastMove=4;
   interact();
   return;
  } else if (yChange==1 && xChange==0){
   lastMove=3;
   interact();
   return;
  } else if (yChange==-1 && xChange==0){
   lastMove=1;
   interact();
   return;
  }
  path = new ArrayList<Integer>();
  for (int i=0; i<500; i++){
   path.add(0);
  }
  for (int i=0; i<50; i++){
   pathSeek(target);
  }
  try {
   if (path.get(0)==0){
    path.add(0,3);
   }
  } catch (Exception e){
   path.add(3);
  }
  if (position[0]==target[0] && position[1]==target[1]){
   path.add(0,3);
  }
  if (path.size()>=495){
   System.out.println("LOOOOOOOOOP");
   stupidity++;
   if (stupidity<4){
    tryMove(home(false));
    return;
   }
   stupidity=0;
  }
  if (path.get(0)==1){
   //System.out.println("UP");
   lastMove=1;
   position[1]--;
  } else if (path.get(0)==2){
   //System.out.println("RIGHT");
   lastMove=2;
   position[0]++;
  } else if (path.get(0)==3){
   //System.out.println("DOWN");
   lastMove=3;
   position[1]++; 
  } else if (path.get(0)==4){
   //System.out.println("LEFT");
   lastMove=4;
   position[0]--;
  }
 }

 public void pathSeek(int[] target){
  tempPath = new ArrayList<Integer>();
  phantomPosition[0]=position[0];
  phantomPosition[1]=position[1];
  for (int k=1; k<500; k++){
   int[] direction = {1,2,3,4};
   int xChange = target[0]-phantomPosition[0];
   int yChange = target[1]-phantomPosition[1];
   if (xChange>0 && yChange<0){
    if (Math.random()>.65){
     direction = new int[]{1,2,3,4};
    } else {
     direction = new int[]{2,1,3,4};
    }
   } else if (xChange>0 && yChange>0){
    if (Math.random()>.65){
     direction = new int[]{2,3,4,1};
    } else {
     direction = new int[]{3,2,4,1};
    }
   } else if (xChange<0 && yChange<0){
    if (Math.random()>.65){
     direction = new int[]{4,1,2,3};
    } else {
     direction = new int[]{1,4,2,3};
    }
   } else if (xChange<0 && yChange>0){
    if (Math.random()>.65){
     direction = new int[]{3,4,1,2};
    } else {
     direction = new int[]{4,3,1,2};
    }
   } else if (xChange>0){
    if (Math.random()>.65){
     direction = new int[]{2,1,3,4};
    } else {
     direction = new int[]{2,3,1,4};
    }
   } else if (xChange<0){
    if (Math.random()>.65){
     direction = new int[]{4,3,1,2};
    } else {
     direction = new int[]{4,1,3,2};
    }
   } else if (yChange<0){
    if (Math.random()>.65){
     direction = new int[]{1,2,4,3};
    } else {
     direction = new int[]{1,4,2,3};
    }
   } else if (yChange>0){
    if (Math.random()>.65){
     direction = new int[]{3,4,2,1};
    } else {
     direction = new int[]{3,2,4,1};
    }
   }
   int temp;
   double random = Math.random();
   if (random<.8 && random>.4){
    temp = direction[1];
    direction[1]=direction[2];
    direction[2]=temp;
   } else if (random>.8 && random<.84){
    temp = direction[2];
    direction[2]=direction[3];
    direction[3]=temp;
   } else if (random>.84 && random<.88){
    temp = direction[1];
    direction[1]=direction[3];
    direction[3]=temp;
   } else if (random>.92 && random<.95){
    temp = direction[1];
    direction[1]=direction[0];
    direction[0]=temp;
   } else if (random>.95 && random<.98){
    temp=direction[2];
    direction[2]=direction[0];
    direction[0]=temp;
   } else if (random>.98){
    temp=direction[3];
    direction[3]=direction[0];
    direction[0]=temp;
   }
   for (int i=0; i<4; i++){
    if (attempt(direction[i])){
     tempPath.add(direction[i]);
     break;
    }
   }
   /*if (phantomPosition[0]==target[0] && phantomPosition[1]==target[1]){
    System.out.println("GOTCHA");
    break;
   }*/
   if (Math.abs(target[0]-phantomPosition[0])+Math.abs(target[1]-phantomPosition[1])<=1){
    break;
   }
  }
  if (tempPath.size()<path.size()){
   path=tempPath;
  }
 }

 public boolean attempt(int direction){
  int[] target = new int[2];
  if (direction==1){
   target[0]=phantomPosition[0];
   target[1]=phantomPosition[1]-1;
  } else if (direction==2){
   target[0]=phantomPosition[0]+1;
   target[1]=phantomPosition[1];
  } else if (direction==3){
   target[0]=phantomPosition[0];
   target[1]=phantomPosition[1]+1;
  } else if (direction==4){
   target[0]=phantomPosition[0]-1;
   target[1]=phantomPosition[1];
  }
  for (int i=0; i<parent.enemies.size(); i++){
   if (parent.enemies.get(i).position[0]==target[0] && parent.enemies.get(i).position[1]==target[1]){
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
  phantomPosition[0]=target[0];
  phantomPosition[1]=target[1];
  return true;
 }
 /////////////////////////////////////////////////////////////////////////////////////////////
}