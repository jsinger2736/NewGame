

public class Stats{
 //TYPES
//goblin          2
//wall            3
//footsoldier     4
//peasant         5
//rock            6
//bowman          7
//goblinarcher    8

 //allies
 int playerdamage=1;
 int playergold=0;
 int playerspeed=200;
 int wallcost=5;
 int footsoldierdamage=1;
 int footsoldiermaxgold=10;
 int footsoldierspeed=400;
 int footsoldierregenerate=5;
 int footsoldiercost=10;
 int peasantdamage=1;
 int peasantmaxgold=2;
 int peasantspeed=250;
 int peasantregenerate=8;
 int peasantgoldgain=1;
 int peasantcost=25;
 int rockcost=5;
 int bowmandamage=1;
 int bowmanrange=7;
 int bowmanmaxgold=3;
 int bowmanspeed=500;
 int bowmanregenerate=10;
 int bowmancost=40;

 //enemies
 int goblindamage=1;
 int goblinmaxgold=5;
 int goblinspeed=350;
 int goblinregenerate=20;
 int goblinarcherdamage=1;
 int goblinarcherrange=6;
 int goblinarchermaxgold=3;
 int goblinarcherspeed=510;
 int goblinarcherregenerate=10;
 int raiderdamage=2;
 int raidermaxgold=15;
 int raiderspeed=300;
 int raiderregenerate=15;

 public Stats(){
 }

 public int cost(int type){ //for player.calculateValue()
  if (type==3){
   return wallcost;
  } else if (type==6){
   return rockcost;
  } else if (type==4){
   return footsoldiercost;
  } else if (type==5){
   return peasantcost;
  } else if (type==7){
   return bowmancost;
  } else {
   //return 99999999;
   return 0;
  }
 }

 public int allyCosts(int number){ //for board.place()
//wall rock footsoldier peasant bowman
  if (number==0){
   return wallcost;
  } else if (number==1){
   return rockcost;
  } else if (number==2){
   return footsoldiercost;
  } else if (number==3){
   return peasantcost;
  } else if (number==4){
   return bowmancost;
  } else {
   return 99999999;
  }
 }
}