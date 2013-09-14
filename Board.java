
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.*;
import java.io.*;

public class Board extends JPanel implements ActionListener{
 int boardWidth = 31;
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
 
 public Board(NewGame parenti){
  setFocusable(true);
  setFocusTraversalKeysEnabled(false);
  timer = new Timer(50, this);
  parent = parenti;
  statusbar = parent.getStatusBar();
  addKeyListener(new TAdapter());
  try {
   backgroundimg = ImageIO.read(new File("pictures/background.jpg"));
  } catch (IOException e){
  }
  player = new Player(this,0,0);
  stats = new Stats();
  timer.start();
  start();
  Toolkit tk = getToolkit();
  Cursor cursor = tk.createCustomCursor(tk.getImage(""), new Point(), "cursor");
  setCursor(cursor);
  allies.add(new Wall(this,0,2));
  allies.add(new Wall(this,0,-2));
  allies.add(new Wall(this,1,2));
  allies.add(new Wall(this,1,-2));
  allies.add(new Wall(this,-1,2));
  allies.add(new Wall(this,-1,-2));
  allies.add(new Wall(this,2,-2));
  allies.add(new Wall(this,2,-1));
  allies.add(new Wall(this,2,0));
  allies.add(new Wall(this,2,1));
  allies.add(new Wall(this,2,2));
  allies.add(new Wall(this,-2,-2));
  allies.add(new Wall(this,-2,-1));
  allies.add(new Wall(this,-2,0));
  allies.add(new Wall(this,-2,1));
  allies.add(new Wall(this,-2,2));
  player.gold=1200;
 }

 public void actionPerformed(ActionEvent e){
  if (player.gold<0){
   player.gold=0;
  }
  if (!isPaused){
   spawn();
   //status = "("+player.position[0]+","+(-player.position[1])+")   Gold:"+player.gold;
   status = "("+(curView[0]+(int)getSize().getWidth()/(squareWidth()*2))+","+(curView[1]+(int)getSize().getHeight()/(squareHeight()*2))+")   Gold:"+player.gold;
   statusbar.setText(status);
  } else {
   status = "Paused   ("+(curView[0]+(int)getSize().getWidth()/(squareWidth()*2))+","+(curView[1]+(int)getSize().getHeight()/(squareHeight()*2))+")   Gold:"+player.gold;
   statusbar.setText(status);
  }
  repaint();
 }

 public void start(){
  isStarted=true;
 }
 
 public void pause(){
  if (!isStarted){
   return;
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

 public void spawn(){
  int failsafe = 0;
  int[] enemyAmount = new int[9];
  for (int i=0; i<enemies.size(); i++){
   enemyAmount[enemies.get(i).type]=enemyAmount[enemies.get(i).type]+1;
  }
  int positivex=1;
  int positivey=1;
  if (enemyAmount[2]<3){
   while (failsafe<10){
    positivex=1;
    positivey=1;
    if (Math.random()>.5){
     positivex=-1;
    }
    if (Math.random()>.5){
     positivey=-1;
    }
    if (spawnSpecific(1, new int[]{(int)(Math.random()*20*positivex+0+(4*positivex)), (int)(Math.random()*20*positivey+0+(4*positivey))})){
     failsafe=0;
     break;
    } else {
     failsafe++;
    }
    //enemies.add(new Goblin(this, (int)(Math.random()*20*positivex+0+(4*positivex)), (int)(Math.random()*20*positivey+0+(4*positivey))));
   }
  }
  if (enemyAmount[8]<1){
   while (failsafe<10){
    positivex=1;
    positivey=1;
    if (Math.random()>.5){
     positivex=-1;
    }
    if (Math.random()>.5){
     positivey=-1;
    }
    if (spawnSpecific(2, new int[]{(int)(Math.random()*20*positivex+0+(4*positivex)), (int)(Math.random()*20*positivey+0+(4*positivey))})){
     failsafe=0;
     break;
    } else {
     failsafe++;
    }
   }
   while (failsafe<10){
    positivex=1;
    positivey=1;
    if (Math.random()>.5){
     positivex=-1;
    }
    if (Math.random()>.5){
     positivey=-1;
    }
    if (spawnSpecific(2, new int[]{(int)(Math.random()*25*positivex+0+(4*positivex)), (int)(Math.random()*25*positivey+0+(4*positivey))})){
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
   if (Math.abs(target[0]-allies.get(i).position[0])<=allies.get(i).radius && Math.abs(target[1]-allies.get(i).position[1])<=allies.get(i).radius){
    return false;
   }
   /*if (allies.get(i).position[0]==target[0] && allies.get(i).position[1]==target[1]){
    return false;
   }*/
  }
  for (int i=0; i<enemies.size(); i++){
   if (enemies.get(i).position[0]==target[0] && enemies.get(i).position[1]==target[1]){
    return false;
   }
  }
  if (Math.abs(target[0]-player.position[0])<6 && Math.abs(target[1]-player.position[1])<6){
   return false;
  }
  /*if (player.position[0]==target[0] && player.position[1]==target[1]){
   return false;
  }*/
  if (type==1){ //goblin
   enemies.add(new Goblin(this, target[0], target[1]));
  } else if (type==2){ //goblinarcher
   enemies.add(new GoblinArcher(this, target[0], target[1]));
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
    for (int k=0; k<enemies.size(); k++){
     if (enemies.get(k).position[0]==i && enemies.get(k).position[1]==j){
      drawSquare(g,2+(enemies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(enemies.get(k).position[1]-curView[1])*squareHeight(),enemies.get(k).type,enemies.get(k).lastMove,enemies.get(k).stab);
     }
    }
    for (int k=0; k<allies.size(); k++){
     if (allies.get(k).origin[0]==i && allies.get(k).origin[1]==j && (allies.get(k).name.equals("FootSoldier"))){
      drawOrigin(g,2+(allies.get(k).origin[0]-curView[0])*squareWidth(),boardTop+(allies.get(k).origin[1]-curView[1])*squareHeight(),allies.get(k).type);
     }
     if (allies.get(k).position[0]==i && allies.get(k).position[1]==j){
      drawSquare(g,2+(allies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(allies.get(k).position[1]-curView[1])*squareHeight(),allies.get(k).type,allies.get(k).lastMove,allies.get(k).stab);
     }
    }
   }
  }
  drawSquare(g,2+(player.position[0]-curView[0])*squareWidth(),boardTop+(player.position[1]-curView[1])*squareHeight(),1,player.lastMove,player.stab);
  for (int i=curView[0]; i<curView[0]+(int)size.getWidth()/squareWidth(); i++){
   for (int j=curView[1]; j<curView[1]+(int)size.getHeight()/squareHeight(); j++){
    for (int k=0; k<enemies.size(); k++){
     if (enemies.get(k).position[0]==i && enemies.get(k).position[1]==j){
      drawSquare(g,2+(enemies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(enemies.get(k).position[1]-curView[1])*squareHeight(),0,enemies.get(k).lastMove,enemies.get(k).stab);
     }
    }
    for (int k=0; k<allies.size(); k++){
     if (allies.get(k).position[0]==i && allies.get(k).position[1]==j){
      drawSquare(g,2+(allies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(allies.get(k).position[1]-curView[1])*squareHeight(),0,allies.get(k).lastMove,allies.get(k).stab);
     }
    }
   }
  }
  drawSquare(g,2+(player.position[0]-curView[0])*squareWidth(),boardTop+(player.position[1]-curView[1])*squareHeight(),0,player.lastMove,player.stab);
  for (int i=curView[0]; i<curView[0]+(int)size.getWidth()/squareWidth(); i++){
   for (int j=curView[1]; j<curView[1]+(int)size.getHeight()/squareHeight(); j++){
    for (int k=0; k<projectiles.size(); k++){
     if (projectiles.get(k).position[0]==i && projectiles.get(k).position[1]==j){
      drawProjectile(g,2+(i-curView[0])*squareWidth(),boardTop+(j-curView[1])*squareHeight(),projectiles.get(k).type,projectiles.get(k).orientation);
     }
    }
   }
  }
 }

 public void drawSquare(Graphics g, int x, int y, int type, int direction, int attack){
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
  }
  if (type==8){
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
 }
 
 public void drawOrigin(Graphics g, int x, int y, int type){
  Color color = new Color(0,0,0);
  if (type==4){
   color = new Color(81,162,255);
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
   if (allies.get(i).position[0]==target[0] && allies.get(i).position[1]==target[1]){
    player.gold=player.gold+allies.get(i).gold;
    allies.get(i).gold=0;
    return;
   }
  }
  for (int i=0; i<allies.size(); i++){
   if (allies.get(i).origin[0]==target[0] && allies.get(i).origin[1]==target[1]){
    player.gold=player.gold+allies.get(i).gold;
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
   if (!isStarted){
    return;
   }
   if (keycode==KeyEvent.VK_TAB){
    parent.allyMenu();
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
   } else if (keychar=='q'){
    place();
   } else if (keychar=='Q'){
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














