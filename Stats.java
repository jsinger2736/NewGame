

public class Stats{
 //TYPES
//goblin          2
//wall            3
//footsoldier     4
//peasant         5
//rock            6
//bowman          7
//goblinarcher    8
//raider          9
//runner         10
//ghost          11
//knight         12
//king           13
//hunter         14

 //allies
 int playerdamage=1;
 //int playergold=0;
 int playerspeed=200;
 int wallcost=5;
 int footsoldierdamage=1;
 int footsoldiermaxgold=10;
 int footsoldierspeed=375;
 int footsoldierregenerate=2;
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
 int bowmanmaxgold=4;
 int bowmanspeed=550;
 int bowmanregenerate=6;
 int bowmancost=40;
 int knightdamage=2;
 int knightmaxgold=25;
 int knightspeed=275;
 int knightregenerate=4;
 int knightcost=100;
 int kingdamage=10;
 int kingmaxgold=50;
 int kingspeed=500;
 int kingregenerate=4;
 int kingcost=99999;
 int hunterdamage=1;
 int huntermaxgold=20;
 int hunterspeed=140;
 int hunterregenerate=5;
 int huntercost=100;

 //enemies
 int goblindamage=1;
 int goblinmaxgold=5;
 int goblinspeed=350;
 int goblinregenerate=20;
 int goblinarcherdamage=1;
 int goblinarcherrange=6;
 int goblinarchermaxgold=3;
 int goblinarcherspeed=550;
 int goblinarcherregenerate=10;
 int raiderdamage=2;
 int raidermaxgold=14;
 int raiderspeed=300;
 int raiderregenerate=15;
 int runnerdamage=1;
 int runnermaxgold=6;
 int runnerspeed=135;
 int runnerregenerate=6;
 int ghostdamage=1;
 int ghostmaxgold=7;
 int ghostspeed=325;
 int ghostregenerate=10;

 public Stats(){
 }

 public int cost(int type){ //for player.calculateValue() and board.remove()
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
  } else if (type==12){
   return knightcost;
  } else if (type==14){
   return huntercost;
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
  } else if (number==5){
   return knightcost;
  } else if (number==6){
   return huntercost;
  } else {
   return 99999999;
  }
 }
}