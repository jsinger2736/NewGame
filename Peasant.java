import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Peasant extends Mob{
 public Peasant(Board parenti, int x, int y){
  super(parenti, x, y);
  position[0]=x;
  position[1]=y;
  origin[0]=x;
  origin[1]=y;
  gold=parent.stats.peasantmaxgold;
  parent=parenti;
  type=5;
  radius=1;
  name="Peasant";
  timer=new Timer(parent.stats.peasantspeed,this);
  timer.start();
 }

 public void actionPerformed(ActionEvent e){
  timer.setDelay(parent.stats.peasantspeed);
  if (stab!=0){
   stab=0;
  }
  if (gold<=0){
   timer.stop();
   parent.allies.remove(this);
   return;
  }
  regenerate();
  counter = !counter;
  if (counter){
   tryMove(home());
  }
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
  for (int i=0; i<parent.enemies.size(); i++){
   if (parent.enemies.get(i).position[0]==target[0] && parent.enemies.get(i).position[1]==target[1]){
    parent.enemies.get(i).gold=parent.enemies.get(i).gold-parent.stats.peasantdamage;
    parent.player.gold=parent.player.gold+parent.stats.peasantdamage;
   }
  }
 }

 public void regenerate(){
  regeneration++;
  if (regeneration>=parent.stats.peasantregenerate){
   parent.player.gold=parent.player.gold+parent.stats.peasantgoldgain;
   regeneration=0;
   if (gold<parent.stats.peasantmaxgold){
    gold++;
   }
  }
 }

 public int[] home(){
  int xChange = radius;
  int yChange = radius;
  int tempxChange = 0;
  int tempyChange = 0;
  for (int i=0; i<parent.enemies.size(); i++){
   if (parent.enemies.get(i).name.equals("Wall")){
    continue;
   }
   tempxChange=parent.enemies.get(i).position[0]-origin[0];
   tempyChange=parent.enemies.get(i).position[1]-origin[1];
   if (Math.sqrt(tempxChange*tempxChange+tempyChange*tempyChange)<Math.sqrt(xChange*xChange+yChange*yChange)){
    xChange=tempxChange;
    yChange=tempyChange;
   }
  }
  if (xChange==radius && yChange==radius){
   return new int[]{origin[0],origin[1]};
  }
  return new int[]{origin[0]+xChange,origin[1]+yChange};
 }

 public void tryMove(int[] target){
  int[] direction = {1,2,3,4};
  int xChange = target[0]-position[0];
  int yChange = target[1]-position[1];
  if (xChange==1 && yChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=2;
   interact();
  } else if (xChange==-1 && yChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=4;
   interact();
  } else if (yChange==1 && xChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=3;
   interact();
  } else if (yChange==-1 && xChange==0 && !(target[0]==origin[0] && target[1]==origin[1])){
   lastMove=1;
   interact();
  }
 }

}