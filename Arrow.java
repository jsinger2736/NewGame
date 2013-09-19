import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Arrow extends Projectile{
 public Arrow(Board parenti, int o0, int o1, int d0, int d1, int orientationo, int allegianceo, int damageo, int rangeo){
  parent=parenti;
  origin[0]=o0;
  origin[1]=o1;
  orientation=orientationo;
  /*if (orientation==1){
   position[0]=o0;
   position[1]=o1-1;
  } else if (orientation==2){
   position[0]=o0+1;
   position[1]=o1;
  } else if (orientation==3){
   position[0]=o0;
   position[1]=o1+1;
  } else if (orientation==4){
   position[0]=o0-1;
   position[1]=o1;
  } else if (orientation==5){
   position[0]=o0+1;
   position[1]=o1-1;
  } else if (orientation==6){
   position[0]=o0+1;
   position[1]=o1+1;
  } else if (orientation==7){
   position[0]=o0-1;
   position[1]=o1+1;
  } else {
   position[0]=o0-1;
   position[1]=o1-1;
  }*/
  position[0]=o0;
  position[1]=o1;
  dposition[0]=position[0];
  dposition[1]=position[1];
  destination[0]=d0;
  destination[1]=d1;
  allegiance=allegianceo;
  damage=damageo;
  range=(int)(1.5*rangeo);
  type=1;
  name="Arrow";
  timer=new Timer(100,this);
  timer.start();
  xChange=(float)(destination[0]-position[0]);
  yChange=(float)(destination[1]-position[1]);
  if (Math.abs(xChange)>Math.abs(yChange)){
   turns = (int)Math.abs(xChange);
  } else {
   turns = (int)Math.abs(yChange);
  }
  //System.out.println("("+origin[0]+","+origin[1]+")  ("+destination[0]+","+destination[1]+")  "+(int)xChange+" "+(int)yChange+" "+turns);
 }

 public void actionPerformed(ActionEvent e){
  turn++;
  dposition[0]=dposition[0]+xChange/turns;
  dposition[1]=dposition[1]+yChange/turns;
  position[0]=Math.round(dposition[0]);
  position[1]=Math.round(dposition[1]);
  checkHit();
  if (turn==range || dead){
   timer.stop();
   parent.projectiles.remove(this);
   return;
  }
 }

 public void checkHit(){
  if (allegiance==1){ //good guys
   for (int i=0; i<parent.enemies.size(); i++){
    if (parent.enemies.get(i).position[0]==position[0] && parent.enemies.get(i).position[1]==position[1]){
     if (parent.enemies.get(i).name.equals("Ghost") && Math.random()>.25){
      continue;
     }
     parent.enemies.get(i).gold=parent.enemies.get(i).gold-damage;
     parent.player.gold=parent.player.gold+damage;
     dead=true;
     //System.out.println("HIT");
    }
   }
  } else if (allegiance==2){
   for (int i=0; i<parent.allies.size(); i++){
    if (parent.allies.get(i).position[0]==position[0] && parent.allies.get(i).position[1]==position[1]){
     if (parent.allies.get(i).name.equals("Wall") || parent.allies.get(i).name.equals("Rock")){
      //dead=true;
      //return;
     } else {
      parent.allies.get(i).gold=parent.allies.get(i).gold-damage;
      dead=true;
     }
    }
   }
   if (parent.player.position[0]==position[0] && parent.player.position[1]==position[1]){
     parent.player.gold=parent.player.gold-damage;
     dead=true;
   }
  }
 }

}
















