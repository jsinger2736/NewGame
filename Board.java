
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.*;
import java.io.*;

public class Board extends JPanel implements ActionListener{
 int boardWidth = 50;
 int boardHeight = 31;
 int[] curView = {0,0};
 boolean isStarted = false;
 boolean isPaused = false;
 JLabel statusbar;
 Timer timer;
 int weapon = 1;
 NewGame parent;
 Player player;
 ArrayList<Mob> enemies = new ArrayList<Mob>();
 ArrayList<Mob> allies = new ArrayList<Mob>();
 ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
 Stats stats;
 String status = "";
 Image backgroundimg = null;
 Cost cost = null;
 Tutorial tutorial;
 boolean gameover = false;
 
 public Board(NewGame parenti){
  setFocusable(true);
  setFocusTraversalKeysEnabled(false);
  timer = new Timer(60, this);
  parent = parenti;
  statusbar = parent.getStatusBar();
  addKeyListener(new TAdapter());
  try {
   backgroundimg = ImageIO.read(new File("pictures/background.jpg"));
  } catch (IOException e){
  }
  player = new Player(this,0,1);
  stats = new Stats();
  timer.start();
  //start();
  Toolkit tk = getToolkit();
  Cursor cursor = tk.createCustomCursor(tk.getImage(""), new Point(), "cursor");
  setCursor(cursor);
  tutorial = new Tutorial(this);

  for (int i=-3; i<4; i++){
   for (int j=-3; j<4; j++){
    if ((i==-3 || i==3) && j!=0){
     allies.add(new Wall(this,i,j));
    }
    if ((j==-3 || j==3) && i!=0){
     allies.add(new Wall(this,i,j));
    }
   }
  }

  player.gold=300;
  allies.add(new King(this,0,0));
 }

 public void actionPerformed(ActionEvent e){
  if (player.gold<0){
   player.gold=0;
  }
  player.calculateValue();
  if (gameover){
   status = "Game over!   ("+(curView[0]+(int)getSize().getWidth()/(squareWidth()*2))+","+(curView[1]+(int)getSize().getHeight()/(squareHeight()*2))+")   Value:"+player.value+"   Gold:"+player.gold;
   statusbar.setText(status);
  } else if (!isPaused){
   status = "("+(curView[0]+(int)getSize().getWidth()/(squareWidth()*2))+","+(curView[1]+(int)getSize().getHeight()/(squareHeight()*2))+")   Value:"+player.value+"   Gold:"+player.gold;
   statusbar.setText(status);
  } else {
   status = "Paused   ("+(curView[0]+(int)getSize().getWidth()/(squareWidth()*2))+","+(curView[1]+(int)getSize().getHeight()/(squareHeight()*2))+")   Value:"+player.value+"   Gold:"+player.gold;
   statusbar.setText(status);
  }
  if (isStarted){
   if (!isPaused){
    spawn();
   }
  }
  repaint();
 }

 public void restart(){
  enemies = new ArrayList<Mob>();
  allies = new ArrayList<Mob>();
  projectiles = new ArrayList<Projectile>();
  player = new Player(this,0,1);
  stats = new Stats();
  tutorial = new Tutorial(this);
  timer.start();

  for (int i=-3; i<4; i++){
   for (int j=-3; j<4; j++){
    if ((i==-3 || i==3) && j!=0){
     allies.add(new Wall(this,i,j));
    }
    if ((j==-3 || j==3) && i!=0){
     allies.add(new Wall(this,i,j));
    }
   }
  }

  player.gold=10;
  allies.add(new King(this,0,0));
  gameover=false;
  isPaused=false;
  isStarted=false;
 }

 public void start(){
  isStarted=true;
 }
 
 public void pause(){
  if (!isStarted){
   //return;
  }
  isPaused = !isPaused;
  if (isPaused){
   statusbar.setText("Paused");
   player.timer.stop();
   for (int i=0; i<enemies.size(); i++){
    enemies.get(i).timer.stop();
   }
   for (int i=0; i<allies.size(); i++){
    allies.get(i).timer.stop();
   }
   for (int i=0; i<projectiles.size(); i++){
    projectiles.get(i).timer.stop();
   }
   //timer.stop();
  } else {
   statusbar.setText(status);
   player.timer.start();
   for (int i=0; i<enemies.size(); i++){
    enemies.get(i).timer.start();
   }
   for (int i=0; i<allies.size(); i++){
    allies.get(i).timer.start();
   }
   for (int i=0; i<projectiles.size(); i++){
    projectiles.get(i).timer.start();
   }
   //timer.start();
  }
 }

 public void kingIsDed(){
  player.timer.stop();
  for (int i=0; i<enemies.size(); i++){
   enemies.get(i).timer.stop();
  }
  for (int i=0; i<allies.size(); i++){
   allies.get(i).timer.stop();
  }
  for (int i=0; i<projectiles.size(); i++){
   projectiles.get(i).timer.stop();
  }
  gameover = true;
 }

 public void spawn(){
  int failsafe = 0;
  int[] enemyAmount = new int[20];
  for (int i=0; i<enemies.size(); i++){
   enemyAmount[enemies.get(i).type]=enemyAmount[enemies.get(i).type]+1;
  }
  if (player.value>=0 && player.value<45){
   if (enemyAmount[2]<2){
    spawnInArea(2,1,new int[]{0,0},8,20);
   }
  } else if (player.value>=45 && player.value<119){
   if (enemyAmount[2]<3){
    spawnInArea(2,1,new int[]{30,0},0,10);
   }
   if (enemyAmount[8]<1){
    spawnInArea(8,1,new int[]{35,0},0,5);
   }
  } else if (player.value>=120 && player.value<209){
   if (enemyAmount[2]<4){
    spawnInArea(2,1,new int[]{30,0},0,10);
   }
   if (enemyAmount[8]<1){
    spawnInArea(8,2,new int[]{35,0},0,0);
   }
   if (enemyAmount[9]<1){
    spawnInArea(9,1,new int[]{35,0},0,10);
   }
  } else if (player.value>=210 && player.value<599){
   if (enemyAmount[2]<3 && enemyAmount[8]<2 && enemyAmount[9]<1 && enemyAmount[11]<2){
    spawnInArea(2,6,new int[]{40,0},0,7);
    spawnInArea(9,1,new int[]{45,0},0,10);
    spawnInArea(8,1,new int[]{33,-8},0,4);
    spawnInArea(8,1,new int[]{33,8},0,4);
    spawnInArea(8,1,new int[]{33,0},0,4);
   }
  } else if (player.value>=600 && player.value<1199){
   if (enemyAmount[2]<5 && enemyAmount[8]<4 && enemyAmount[9]<2 && enemyAmount[11]<2){
    spawnInArea(2,4,new int[]{45,0},0,7);
    spawnInArea(8,1,new int[]{37,-8},0,4);
    spawnInArea(8,1,new int[]{37,8},0,4);
    spawnInArea(9,1,new int[]{45,0},0,10);

    spawnInArea(2,4,new int[]{-45,0},0,7);
    spawnInArea(8,1,new int[]{-37,-8},0,4);
    spawnInArea(8,1,new int[]{-37,8},0,4);
    spawnInArea(9,1,new int[]{-45,0},0,10);

    spawnInArea(2,4,new int[]{0,45},0,7);
    spawnInArea(8,1,new int[]{-8,37},0,4);
    spawnInArea(8,1,new int[]{8,37},0,4);
    spawnInArea(9,1,new int[]{0,45},0,10);

    spawnInArea(2,4,new int[]{0,-45},0,7);
    spawnInArea(8,1,new int[]{-8,-37},0,4);
    spawnInArea(8,1,new int[]{8,-37},0,4);
    spawnInArea(9,1,new int[]{0,-45},0,10);
   }
  } else if (player.value>=1200 && player.value<2199){
   if (enemyAmount[2]<5 && enemyAmount[8]<4 && enemyAmount[9]<2 && enemyAmount[10]<2 && enemyAmount[11]<2){
    spawnInArea(2,3,new int[]{47,0},0,7);
    spawnInArea(8,2,new int[]{39,-8},0,4);
    spawnInArea(8,1,new int[]{39,8},0,4);
    spawnInArea(9,3,new int[]{47,0},0,10);

    spawnInArea(2,3,new int[]{-47,0},0,7);
    spawnInArea(8,2,new int[]{-39,-8},0,4);
    spawnInArea(8,1,new int[]{-39,8},0,4);
    spawnInArea(9,3,new int[]{-47,0},0,10);

    spawnInArea(2,3,new int[]{0,47},0,7);
    spawnInArea(8,2,new int[]{-8,39},0,4);
    spawnInArea(8,1,new int[]{8,39},0,4);
    spawnInArea(9,3,new int[]{0,47},0,10);

    spawnInArea(2,3,new int[]{0,-47},0,7);
    spawnInArea(8,2,new int[]{-8,-39},0,4);
    spawnInArea(8,1,new int[]{8,-39},0,4);
    spawnInArea(9,3,new int[]{0,-47},0,10);

    spawnInArea(10,1,new int[]{25,25},0,5);
    spawnInArea(10,1,new int[]{-25,25},0,5);
    spawnInArea(10,1,new int[]{25,-25},0,5);
    spawnInArea(10,1,new int[]{-25,-25},0,5);
   }
  } else if (player.value>=2200 && player.value<3499){
   if (enemyAmount[2]<6 && enemyAmount[8]<4 && enemyAmount[9]<2 && enemyAmount[10]<2 && enemyAmount[11]<2){
    spawnInArea(2,10,new int[]{0,0},40,50);
    spawnInArea(8,12,new int[]{0,0},40,50);
    spawnInArea(9,14,new int[]{0,0},40,50);
    spawnInArea(10,14,new int[]{0,0},40,50);
   }
  } else if (player.value>=3500 && player.value<4999){
   if (enemyAmount[9]<3 && enemyAmount[10]<4 && enemyAmount[11]<2){
    spawnInArea(9,15,new int[]{0,0},40,50);
    spawnInArea(10,30,new int[]{0,0},50,60);
    spawnInArea(8,6,new int[]{0,0},35,40);
    spawnInArea(11,6,new int[]{0,0},40,45);
   }
  } else if (player.value>=5000 && player.value<6999){
   if (enemyAmount[10]<3 && enemyAmount[11]<3 && enemyAmount[8]<3){
    spawnInArea(10,18,new int[]{0,40},0,10);
    spawnInArea(10,18,new int[]{0,-40},0,10);
    spawnInArea(11,15,new int[]{40,0},0,10);
    spawnInArea(11,15,new int[]{-40,0},0,10);
    spawnInArea(8,10,new int[]{0,0},30,40);
   }
  } else if (player.value>=7000){
   if (enemyAmount[10]<1 && enemyAmount[11]<1 && enemyAmount[8]<1){
    spawnInArea(11,100,new int[]{0,0},50,60);
   }
  }
 }

 public void spawnInArea(int type, int number, int[] target, int innerareaside, int areaside){ //spawns whatever enemy in a (diamond) donut around target
  int failsafe=0;
  int positivex = 0;
  int positivey = 0;
  double scale = 0;
  for(int i=0; i<number; i++){
   while(failsafe<10){
    positivex=1;
    positivey=1;
    if (Math.random()>.5){
     positivex=-1;
    }
    if (Math.random()>.5){
     positivey=-1;
    }
    scale = Math.random();
    if (spawnSpecific(type, new int[]{(int)(Math.random()*(areaside-innerareaside)*positivex+target[0]+scale*innerareaside*positivex),(int)(Math.random()*(areaside-innerareaside)*positivey+target[1]+(1-scale)*innerareaside*positivey)})){
     failsafe=0;
     break;
    } else {
     failsafe++;
    }
   }
  }
 }

 public boolean spawnSpecific(int type, int[] target){
  for (int i=0; i<allies.size(); i++){
   if (allies.get(i).name.equals("Hunter")){
    if (target[0]==allies.get(i).position[0] && target[1]==allies.get(i).position[1]){
     return false;
    }
   } else if (Math.abs(target[0]-allies.get(i).position[0])<=allies.get(i).radius && Math.abs(target[1]-allies.get(i).position[1])<=allies.get(i).radius){
    return false;
   }
  }
  /*for (int i=0; i<enemies.size(); i++){
   if (enemies.get(i).position[0]==target[0] && enemies.get(i).position[1]==target[1]){
    return false;
   }
  }*/
  if (Math.abs(target[0]-player.position[0])<6 && Math.abs(target[1]-player.position[1])<6){
   return false;
  }
  if (type==2){ //goblin
   enemies.add(new Goblin(this, target[0], target[1]));
  } else if (type==8){ //goblinarcher
   enemies.add(new GoblinArcher(this, target[0], target[1]));
  } else if (type==9){ //raider
   enemies.add(new Raider(this, target[0], target[1]));
  } else if (type==10){ //runner
   enemies.add(new Runner(this, target[0], target[1]));
  } else if (type==11){ //ghost
   enemies.add(new Ghost(this, target[0], target[1]));
  }
  return true;
 }

 int squareWidth(){
  //return (int) getSize().getWidth() / boardWidth;
  return 21;
 }

 int squareHeight(){
  //return (int) getSize().getHeight() / boardHeight;
  return 21;
 }

 public void paint(Graphics g){
  super.paint(g);
  Dimension size = getSize();
  if (!isPaused){
   if (curView[0]<player.position[0]-boardWidth/4 || curView[0]>player.position[0]+boardWidth/4 || curView[1]<player.position[1]-boardHeight/4 || curView[1]<player.position[1]+boardHeight/4){
    curView[0]=player.position[0]-(int)size.getWidth()/(squareWidth()*2);//boardWidth/2;  player.position[0]-
    curView[1]=player.position[1]-(int)size.getHeight()/(squareHeight()*2);//boardHeight/2;
   }
  } else {
  }
  //Background drawing
  g.drawImage(backgroundimg, -(curView[0]%boardWidth)*squareWidth(), -(curView[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth)*squareWidth(), -(curView[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth)*squareWidth(), -(curView[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth+boardWidth)*squareWidth(), -(curView[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth+boardWidth)*squareWidth(), -(curView[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth+boardWidth)*squareWidth(), -(curView[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-boardWidth)*squareWidth(), -(curView[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-boardWidth)*squareWidth(), -(curView[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-boardWidth)*squareWidth(), -(curView[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-2*boardWidth)*squareWidth(), -(curView[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-2*boardWidth)*squareWidth(), -(curView[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-2*boardWidth)*squareWidth(), -(curView[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-3*boardWidth)*squareWidth(), -(curView[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-3*boardWidth)*squareWidth(), -(curView[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(curView[0]%boardWidth-3*boardWidth)*squareWidth(), -(curView[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  int boardTop = 0;//(int) size.getHeight() - boardHeight * squareHeight();
  for (int i=curView[0]; i<curView[0]+(int)size.getWidth()/squareWidth(); i++){
   for (int j=curView[1]; j<curView[1]+(int)size.getHeight()/squareHeight(); j++){
    for (int k=0; k<allies.size(); k++){
     if (allies.get(k).origin[0]==i && allies.get(k).origin[1]==j && (allies.get(k).name.equals("FootSoldier") || allies.get(k).name.equals("Knight"))){
      drawOrigin(g,2+(allies.get(k).origin[0]-curView[0])*squareWidth(),boardTop+(allies.get(k).origin[1]-curView[1])*squareHeight(),allies.get(k).type);
     }
     if (allies.get(k).position[0]==i && allies.get(k).position[1]==j){
      drawSquare(g,2+(allies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(allies.get(k).position[1]-curView[1])*squareHeight(),allies.get(k).type,allies.get(k).lastMove,allies.get(k).stab,allies.get(k).gold);
     }
    }
    for (int k=0; k<enemies.size(); k++){
     if (enemies.get(k).position[0]==i && enemies.get(k).position[1]==j){
      drawSquare(g,2+(enemies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(enemies.get(k).position[1]-curView[1])*squareHeight(),enemies.get(k).type,enemies.get(k).lastMove,enemies.get(k).stab,enemies.get(k).gold);
     }
    }
   }
  }
  drawSquare(g,2+(player.position[0]-curView[0])*squareWidth(),boardTop+(player.position[1]-curView[1])*squareHeight(),1,player.lastMove,player.stab,1);
  for (int i=curView[0]; i<curView[0]+(int)size.getWidth()/squareWidth(); i++){
   for (int j=curView[1]; j<curView[1]+(int)size.getHeight()/squareHeight(); j++){
    for (int k=0; k<allies.size(); k++){
     if (allies.get(k).position[0]==i && allies.get(k).position[1]==j){
      drawSquare(g,2+(allies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(allies.get(k).position[1]-curView[1])*squareHeight(),0,allies.get(k).lastMove,allies.get(k).stab,1);
     }
    }
    for (int k=0; k<enemies.size(); k++){
     if (enemies.get(k).position[0]==i && enemies.get(k).position[1]==j){
      drawSquare(g,2+(enemies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(enemies.get(k).position[1]-curView[1])*squareHeight(),0,enemies.get(k).lastMove,enemies.get(k).stab,1);
     }
    }
   }
  }
  drawSquare(g,2+(player.position[0]-curView[0])*squareWidth(),boardTop+(player.position[1]-curView[1])*squareHeight(),0,player.lastMove,player.stab,1);
  for (int i=curView[0]; i<curView[0]+(int)size.getWidth()/squareWidth(); i++){
   for (int j=curView[1]; j<curView[1]+(int)size.getHeight()/squareHeight(); j++){
    for (int k=0; k<projectiles.size(); k++){
     if (projectiles.get(k).position[0]==i && projectiles.get(k).position[1]==j){
      drawProjectile(g,2+(i-curView[0])*squareWidth(),boardTop+(j-curView[1])*squareHeight(),projectiles.get(k).type,projectiles.get(k).orientation);
     }
    }
   }
  }
  drawUI(g);
  tutorial.draw(g);
 }

 public void drawUI(Graphics g){
  Dimension size = getSize();
  int height = (int)size.getHeight();
  int width = (int)size.getWidth();
  //making outline of mini-map
  int minimapside=200;
  if (width<1100 || height<600){
   minimapside=100;
  }
  Color color = new Color(100,100,100);
  g.setColor(color.brighter());
  g.drawLine(width-minimapside-7,height-minimapside-7,width-1,height-minimapside-7);
  g.drawLine(width-minimapside-7,height-minimapside-7,width-minimapside-7,height-1);
  g.drawLine(width-minimapside-5,height-3,width-3,height-3);
  g.drawLine(width-3,height-minimapside-5,width-3,height-3);
  g.setColor(color);
  g.drawLine(width-minimapside-6,height-minimapside-6,width-2,height-minimapside-6);
  g.drawLine(width-minimapside-6,height-minimapside-6,width-minimapside-6,height-2);
  g.drawLine(width-minimapside-6,height-2,width-2,height-2);
  g.drawLine(width-2,height-minimapside-6,width-2,height-2);
  g.setColor(color.darker());
  g.drawLine(width-minimapside-5,height-minimapside-5,width-3,height-minimapside-5);
  g.drawLine(width-minimapside-5,height-minimapside-5,width-minimapside-5,height-3);
  g.drawLine(width-minimapside-6,height-1,width-1,height-1);
  g.drawLine(width-1,height-minimapside-6,width-1,height-1);
  //fill in mini-map
  color = new Color(10,10,10);
  g.setColor(color);
  g.fillRect(width-minimapside-4,height-minimapside-4,minimapside+1,minimapside+1);
  //place dots where player/allies/enemies are
  int[] position = new int[2];
  int[] difference = new int[2];
  color = new Color(200,30,30);
  g.setColor(color);
  for (int i=0; i<enemies.size(); i++){
   difference[0]=enemies.get(i).position[0]-player.position[0];
   difference[1]=enemies.get(i).position[1]-player.position[1];
   if (difference[0]<minimapside/4-1 && difference[0]>-minimapside/4-2 && difference[1]<minimapside/4-1 && difference[1]>-minimapside/4-2){
    g.fillRect(width-2-minimapside/2+2*difference[0],height-2-minimapside/2+2*difference[1],2,2);    
   }
  }
  color = new Color(30,185,30);
  Color wcolor = new Color(125,125,125);
  for (int i=0; i<allies.size(); i++){
   difference[0]=allies.get(i).position[0]-player.position[0];
   difference[1]=allies.get(i).position[1]-player.position[1];
   if (difference[0]<minimapside/4-1 && difference[0]>-minimapside/4-2 && difference[1]<minimapside/4-1 && difference[1]>-minimapside/4-2){
    if (allies.get(i).name.equals("Wall") || allies.get(i).name.equals("Rock")){
     g.setColor(wcolor);
     g.fillRect(width-2-minimapside/2+2*difference[0],height-2-minimapside/2+2*difference[1],2,2);
    } else {
     g.setColor(color);
     g.fillRect(width-2-minimapside/2+2*difference[0],height-2-minimapside/2+2*difference[1],2,2);
    }
   }
  }
  color = new Color(15,15,200);
  g.setColor(color.brighter());
  g.fillRect(width-2-minimapside/2,height-2-minimapside/2,2,2);

  //Ally Bar
  color = new Color(100,100,100);
  g.setColor(color);
       //288 long
  int halfwidth = width-minimapside-350;
  if (width>=615){
   halfwidth=width/2-144;
  }
  g.drawLine(halfwidth,height-25,halfwidth+288,height-25);
  g.drawLine(halfwidth,height-1,halfwidth+288,height-1);
  g.drawLine(halfwidth,height-25,halfwidth,height-1);
  g.drawLine(halfwidth+288,height-25,halfwidth+288,height-1);
  int separation = 0;
  int increment = 24;
  for (int i=0; i<12; i++){
   g.setColor(color);
   g.drawLine(halfwidth+separation,height-25,halfwidth+separation,height-1);
   if (player.gold>=stats.allyCosts(i)){
    g.setColor(new Color(150,150,150));
    g.fillRect(halfwidth+1+separation,height-24,23,23);    
   } else {
    g.setColor(new Color(25,25,25));
    g.fillRect(halfwidth+1+separation,height-24,23,23);
   }
   if (i==0){
    drawSquare(g,halfwidth+2+separation,height-23,3,3,0,1);
   } else if (i==1){
    drawSquare(g,halfwidth+2+separation,height-23,6,3,0,1);
   } else if (i==2){
    drawSquare(g,halfwidth+2+separation,height-23,4,3,0,stats.footsoldiermaxgold);
   } else if (i==3){
    drawSquare(g,halfwidth+2+separation,height-23,5,3,0,stats.peasantmaxgold);
   } else if (i==4){
    drawSquare(g,halfwidth+2+separation,height-23,7,3,0,stats.bowmanmaxgold);
   } else if (i==5){
    drawSquare(g,halfwidth+2+separation,height-23,12,3,0,stats.knightmaxgold);
   } else if (i==6){
    drawSquare(g,halfwidth+2+separation,height-23,14,3,0,stats.huntermaxgold);
   }
   if (parent.allyChoice==i){
    g.setColor(new Color(204,204,204));
    g.drawRect(halfwidth+2+separation-1,height-24,22,22);
    g.drawRect(halfwidth+2+separation,height-23,20,20);
   }
   separation=separation+increment;
  }

  //write cost of thing over ally bar
  try{
   cost.drawCost(g,halfwidth,height);
  } catch (Exception e){
  }
 }

 public void drawSquare(Graphics g, int x, int y, int type, int direction, int attack, int gold){
  int maxgold=0;
  Image img = null;
  Color color;
  color = new Color(0,0,0);
  if (type==1){ //Player
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/PlayerUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/PlayerRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/PlayerDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/PlayerLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
   /*color = new Color(50,50,200);
   g.setColor(color);
   g.fillRect(x+2,y+2,squareWidth()-3,squareHeight()-3);
   g.setColor(color.brighter());
   g.drawLine(x+1,y+squareHeight()-2,x+1,y+1);
   g.drawLine(x+1,y+1,x+squareWidth()-2,y+1);
   g.setColor(color.darker());
   g.drawLine(x+2,y+squareHeight()-2,x+squareWidth()-2,y+squareHeight()-2);
   g.drawLine(x+squareWidth()-2,y+squareHeight()-2,x+squareWidth()-2,y+2);*/
  }
  if (type==2){ //Goblin
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/GoblinUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/GoblinRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/GoblinDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/GoblinLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
   maxgold=stats.goblinmaxgold;
  }
  if (type==3){ //Wall
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/Wall.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/Wall.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/Wall.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/Wall.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
  }
  if (type==4){ //FootSoldier
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/FootSoldierUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/FootSoldierRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/FootSoldierDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/FootSoldierLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
   maxgold=stats.footsoldiermaxgold;
  }
  if (type==5){ //Peasant
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/PeasantUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/PeasantRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/PeasantDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/PeasantLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
   maxgold=stats.peasantmaxgold;
  }
  if (type==6){ //Rock
   try {
    img = ImageIO.read(new File("pictures/Rock.png"));
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
  }
  if (type==7){ //Bowman
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/BowmanUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/BowmanRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/BowmanDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/BowmanLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
   maxgold=stats.bowmanmaxgold;
  }
  if (type==8){ //GoblinArcher
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/GoblinArcherUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/GoblinArcherRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/GoblinArcherDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/GoblinArcherLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
   maxgold=stats.goblinarchermaxgold;
  }
  if (type==9){ //Raider
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/RaiderUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/RaiderRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/RaiderDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/RaiderLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
    System.out.println("darn");
   }
   maxgold=stats.raidermaxgold;
  }
  if (type==10){ //Runner
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/RunnerUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/RunnerRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/RunnerDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/RunnerLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
    System.out.println("darn");
   }
   maxgold=stats.runnermaxgold;
  }
  if (type==11){ //Ghost
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/GhostUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/GhostRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/GhostDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/GhostLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
    System.out.println("darn");
   }
   maxgold=stats.ghostmaxgold;
  }
  if (type==12){ //Knight
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/KnightUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/KnightRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/KnightDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/KnightLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
    System.out.println("darn");
   }
   maxgold=stats.knightmaxgold;
  }
  if (type==13){ //King
   try {
    if (!gameover){
     if (direction==1){
      img = ImageIO.read(new File("pictures/KingUp.png"));
     } else if (direction==2){
      img = ImageIO.read(new File("pictures/KingRight.png"));
     } else if (direction==3){
      img = ImageIO.read(new File("pictures/KingDown.png"));
     } else if (direction==4){
      img = ImageIO.read(new File("pictures/KingLeft.png"));
     }
    } else {
     if (direction==1){
      img = ImageIO.read(new File("pictures/KingDeadUp.png"));
     } else if (direction==2){
      img = ImageIO.read(new File("pictures/KingDeadRight.png"));
     } else if (direction==3){
      img = ImageIO.read(new File("pictures/KingDeadDown.png"));
     } else if (direction==4){
      img = ImageIO.read(new File("pictures/KingDeadLeft.png"));
     }
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
    System.out.println("darn");
   }
   maxgold=stats.kingmaxgold;
  }
  if (type==14){ //Hunter
   try {
    if (direction==1){
     img = ImageIO.read(new File("pictures/HunterUp.png"));
    } else if (direction==2){
     img = ImageIO.read(new File("pictures/HunterRight.png"));
    } else if (direction==3){
     img = ImageIO.read(new File("pictures/HunterDown.png"));
    } else if (direction==4){
     img = ImageIO.read(new File("pictures/HunterLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
    System.out.println("darn");
   }
   maxgold=stats.huntermaxgold;
  }
  if (type==0){
   if (attack==0){
   } else if (attack==1){
    if (direction==1){
     try {
      img = ImageIO.read(new File("pictures/Stab1Up.png"));
      g.drawImage(img,x,y-squareHeight(),squareWidth(),squareHeight(),null);
      img = null;
     } catch (IOException e){
    }
    } else if (direction==2){
     try {
      img = ImageIO.read(new File("pictures/Stab1Right.png"));
      g.drawImage(img,x+squareWidth(),y,squareWidth(),squareHeight(),null);
      img = null;
     } catch (IOException e){
     }
    } else if (direction==3){
     try {
      img = ImageIO.read(new File("pictures/Stab1Down.png"));
      g.drawImage(img,x,y+squareHeight(),squareWidth(),squareHeight(),null);
      img = null;
     } catch (IOException e){
     }
    } else if (direction==4){
     try {
      img = ImageIO.read(new File("pictures/Stab1Left.png"));
      g.drawImage(img,x-squareWidth(),y,squareWidth(),squareHeight(),null);
      img = null;
     } catch (IOException e){
     }
    }
   }
  }
  if (type!=0 && type!=1 && type!=3 && type!=6){
   if (gold!=maxgold){
    double goldpercent = (double)gold/maxgold;
    if (goldpercent>=.75){
     color = new Color(0,204,0);
    } else if (goldpercent>=.4 && goldpercent<.75){
     color = new Color(225,225,0);
    } else if (goldpercent<.4){
     color = new Color(245,0,0);
    }
    g.setColor(color);
    int goldint = (int)(19*goldpercent);
    try{
     g.fillRect(x+1,y+16,goldint,4);
    } catch (Exception e){
    }
   }
  }
 }
 
 public void drawOrigin(Graphics g, int x, int y, int type){
  Color color = new Color(0,0,0);
  if (type==4){
   color = new Color(81,162,255);
  } else if (type==12){
   color = new Color(100,100,100);
  }
  g.setColor(color);
  g.drawLine(x+squareWidth()/2-1,y+squareHeight()/2-1,x+squareWidth()/2+1,y+squareHeight()/2-1);
  g.drawLine(x+squareWidth()/2-1,y+squareHeight()/2,x+squareWidth()/2+1,y+squareHeight()/2);
  g.drawLine(x+squareWidth()/2-1,y+squareHeight()/2+1,x+squareWidth()/2+1,y+squareHeight()/2+1);
 }

 public void drawProjectile(Graphics g, int x, int y, int type, int orientation){
  Image img = null;
  if (type==1){ //Arrow
   try {
    if (orientation==1){
     img = ImageIO.read(new File("pictures/ArrowUp.png"));
    } else if (orientation==2){
     img = ImageIO.read(new File("pictures/ArrowRight.png"));
    } else if (orientation==3){
     img = ImageIO.read(new File("pictures/ArrowDown.png"));
    } else if (orientation==4){
     img = ImageIO.read(new File("pictures/ArrowLeft.png"));
    } else if (orientation==5){
     img = ImageIO.read(new File("pictures/ArrowUpRight.png"));
    } else if (orientation==6){
     img = ImageIO.read(new File("pictures/ArrowDownRight.png"));
    } else if (orientation==7){
     img = ImageIO.read(new File("pictures/ArrowDownLeft.png"));
    } else if (orientation==8){
     img = ImageIO.read(new File("pictures/ArrowUpLeft.png"));
    }
    g.drawImage(img,x,y,squareWidth(),squareHeight(),null);
    img = null;
   } catch (IOException e){
   }
  }
 }

 public void place(){
  int[] target = new int[2];
  if (player.lastMove==1){
   target[0]=player.position[0];
   target[1]=player.position[1]-1;
  } else if (player.lastMove==2){
   target[0]=player.position[0]+1;
   target[1]=player.position[1];
  } else if (player.lastMove==3){
   target[0]=player.position[0];
   target[1]=player.position[1]+1;
  } else if (player.lastMove==4){
   target[0]=player.position[0]-1;
   target[1]=player.position[1];
  }
  for (int i=0; i<allies.size(); i++){
   if (allies.get(i).position[0]==target[0] && allies.get(i).position[1]==target[1]){
    return;
   }
  }
  for (int i=0; i<enemies.size(); i++){
   if (enemies.get(i).position[0]==target[0] && enemies.get(i).position[1]==target[1]){
    return;
   }
  }
  if (parent.allyChoice==0){
   if (player.gold>=stats.wallcost){
    player.gold=player.gold-stats.wallcost;
    allies.add(new Wall(this,target[0],target[1]));
   }
  } else if (parent.allyChoice==1){
   if (player.gold>=stats.rockcost){
    player.gold=player.gold-stats.rockcost;
    allies.add(new Rock(this,target[0],target[1]));
   }
  } else if (parent.allyChoice==2){
   if (player.gold>=stats.footsoldiercost){
    player.gold=player.gold-stats.footsoldiercost;
    allies.add(new FootSoldier(this,target[0],target[1]));
   }
  } else if (parent.allyChoice==3){
   if (player.gold>=stats.peasantcost){
    player.gold=player.gold-stats.peasantcost;
    allies.add(new Peasant(this,target[0],target[1]));
   }
  } else if (parent.allyChoice==4){
   if (player.gold>=stats.bowmancost){
    player.gold=player.gold-stats.bowmancost;
    allies.add(new Bowman(this,target[0],target[1]));
   }
  } else if (parent.allyChoice==5){
   if (player.gold>=stats.knightcost){
    player.gold=player.gold-stats.knightcost;
    allies.add(new Knight(this,target[0],target[1]));
   }
  } else if (parent.allyChoice==6){
   if (player.gold>=stats.huntercost){
    player.gold=player.gold-stats.huntercost;
    allies.add(new Hunter(this,target[0],target[1]));
   }
  }
 }

 public void remove(){
  int[] target = new int[2];
  if (player.lastMove==1){
   target[0]=player.position[0];
   target[1]=player.position[1]-1;
  } else if (player.lastMove==2){
   target[0]=player.position[0]+1;
   target[1]=player.position[1];
  } else if (player.lastMove==3){
   target[0]=player.position[0];
   target[1]=player.position[1]+1;
  } else if (player.lastMove==4){
   target[0]=player.position[0]-1;
   target[1]=player.position[1];
  }
  for (int i=0; i<allies.size(); i++){
   if (allies.get(i).position[0]==target[0] && allies.get(i).position[1]==target[1] && !allies.get(i).name.equals("King")){
    player.gold=player.gold+stats.cost(allies.get(i).type);//allies.get(i).gold;
    allies.get(i).gold=0;
    return;
   }
  }
  for (int i=0; i<allies.size(); i++){
   if (allies.get(i).origin[0]==target[0] && allies.get(i).origin[1]==target[1] && !allies.get(i).name.equals("King") && !allies.get(i).name.equals("Hunter")){
    player.gold=player.gold+stats.cost(allies.get(i).type);//allies.get(i).gold;
    allies.get(i).gold=0;
    return;
   }
  }
 }

 public void moveView(int direction){
  if (direction==1){
   curView[1]=curView[1]-1;
  } else if (direction==2){
   curView[0]=curView[0]+1;
  } else if (direction==3){
   curView[1]=curView[1]+1;
  } else if (direction==4){
   curView[0]=curView[0]-1;
  }
 }

 class TAdapter extends KeyAdapter{
  public void keyPressed(KeyEvent e){
   int keycode = e.getKeyCode();
   int keychar = e.getKeyChar();
   if (gameover){
    isPaused=true;
   }
   if (!isStarted){
    if (keycode==KeyEvent.VK_ESCAPE){
     tutorial.end();
     return;
    }
    //return;
   }
   if (keycode==KeyEvent.VK_TAB){
    parent.allyMenu();
   }
   if (keychar=='q' || keychar=='Q'){
    parent.allyChoice=parent.allyChoice-1;
    if (parent.allyChoice<0){
     parent.allyChoice=6;
    }
    try{
     cost.timer.stop();
    } catch(Exception ex){
    }
    cost = new Cost(parent.board);
   } else if (keychar=='r' || keychar=='R'){
    parent.allyChoice=parent.allyChoice+1;
    if (parent.allyChoice>6){
     parent.allyChoice=0;
    }
    try{
     cost.timer.stop();
    } catch(Exception ex){
    }
    cost = new Cost(parent.board);
   }
   if (keychar=='p' || keychar=='P' || keycode==KeyEvent.VK_ESCAPE){
    pause();
   }
   if (isPaused){
    if (keychar=='w' || keychar=='W' || keycode==KeyEvent.VK_UP){
     moveView(1);
    } else if (keychar=='d' || keychar=='D' || keycode==KeyEvent.VK_RIGHT){
     moveView(2);
    } else if (keychar=='s' || keychar=='S' || keycode==KeyEvent.VK_DOWN){
     moveView(3);
    } else if (keychar=='a' || keychar=='A' || keycode==KeyEvent.VK_LEFT){
     moveView(4);
    }
    return;
   }
   if (keychar=='w'){
    player.tryMove(1,true);
   } else if (keychar=='d'){
    player.tryMove(2,true);
   } else if (keychar=='s'){
    player.tryMove(3,true);
   } else if (keychar=='a'){
    player.tryMove(4,true);
   } else if (keychar=='W'){
    player.tryMove(1,false);
   } else if (keychar=='D'){
    player.tryMove(2,false);
   } else if (keychar=='S'){
    player.tryMove(3,false);
   } else if (keychar=='A'){
    player.tryMove(4,false);
   } else if (keychar==' '){
    player.interact();
   } else if (keychar=='e'){
    place();
   } else if (keychar=='E'){
    remove();
   }
   switch (keycode){
    case KeyEvent.VK_UP:
     player.tryMove(1,true);
     break;
    case KeyEvent.VK_RIGHT:
     player.tryMove(2,true);
     break;
    case KeyEvent.VK_DOWN:
     player.tryMove(3,true);
     break;
    case KeyEvent.VK_LEFT:
     player.tryMove(4,true);
     break;
   }
  }
 }

}














