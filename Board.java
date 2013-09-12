
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
  timer.start();
  start();
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
 }

 public void actionPerformed(ActionEvent e){
  spawn(1,player.position);
  repaint();
  if (player.gold<0){
   player.gold=0;
  }
  status = "("+player.position[0]+","+(-player.position[1])+")   Gold:"+player.gold;
  statusbar.setText(status);
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
   timer.stop();
  } else {
   statusbar.setText(status);
   player.timer.start();
   for (int i=0; i<enemies.size(); i++){
    enemies.get(i).timer.start();
   }
   for (int i=0; i<allies.size(); i++){
    allies.get(i).timer.start();
   }
   timer.start();
  }
 }

 public void spawn(int type, int[] target){
  if (enemies.size()<2){
   int positivex=1;
   int positivey=1;
   if (Math.random()>.5){
    positivex=-1;
   }
   if (Math.random()>.5){
    positivey=-1;
   }
   if (type==1){
    enemies.add(new Goblin(this, (int)(Math.random()*10*positivex+target[0]+(4*positivex)), (int)(Math.random()*10*positivey+target[1]+(4*positivey))));
   }
  }
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
  //Background drawing
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth+boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth+boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth+boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-2*boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-2*boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-2*boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-3*boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-3*boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  g.drawImage(backgroundimg, -(player.position[0]%boardWidth-3*boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), squareWidth()*boardWidth, squareHeight()*boardHeight, null);
  int boardTop = 0;//(int) size.getHeight() - boardHeight * squareHeight();
  if (curView[0]<player.position[0]-boardWidth/4 || curView[0]>player.position[0]+boardWidth/4 || curView[1]<player.position[1]-boardHeight/4 || curView[1]<player.position[1]+boardHeight/4){
   curView[0]=player.position[0]-(int)size.getWidth()/(squareWidth()*2);//boardWidth/2;  player.position[0]-
   curView[1]=player.position[1]-(int)size.getHeight()/(squareHeight()*2);//boardHeight/2;
  }
  for (int i=curView[0]; i<curView[0]+(int)size.getWidth()/squareWidth(); i++){
   for (int j=curView[1]; j<curView[1]+(int)size.getHeight()/squareHeight(); j++){
    for (int k=0; k<enemies.size(); k++){
     if (enemies.get(k).position[0]==i && enemies.get(k).position[1]==j){
      drawSquare(g,2+(enemies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(enemies.get(k).position[1]-curView[1])*squareHeight(),enemies.get(k).type,enemies.get(k).lastMove,enemies.get(k).stab);
     }
    }
    for (int k=0; k<allies.size(); k++){
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
 }

 public void drawSquare(Graphics g, int x, int y, int type, int direction, int attack){
  Image img = null;
  Color color;
  color = new Color(0,0,0);
  if (type==1){ //Player
   color = new Color(50,50,200);
   g.setColor(color);
   g.fillRect(x+2,y+2,squareWidth()-3,squareHeight()-3);
   g.setColor(color.brighter());
   g.drawLine(x+1,y+squareHeight()-2,x+1,y+1);
   g.drawLine(x+1,y+1,x+squareWidth()-2,y+1);
   g.setColor(color.darker());
   g.drawLine(x+2,y+squareHeight()-2,x+squareWidth()-2,y+squareHeight()-2);
   g.drawLine(x+squareWidth()-2,y+squareHeight()-2,x+squareWidth()-2,y+2);
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
   color = new Color(150,150,150);
  }
  if (type==4){ //FootSoldier
   color = new Color (175,175,200);
   g.setColor(color);
   g.fillRect(x+2,y+2,squareWidth()-3,squareHeight()-3);
   g.setColor(color.darker());
   g.drawRect(x+1,y+1,squareWidth()-3,squareHeight()-3);
  }
  if (type!=0 && type!=1 && type!=2 && type!=4){
   g.setColor(color);
   g.fillRect(x+1,y+1,squareWidth()-2,squareHeight()-2);
   g.setColor(color.brighter());
   g.drawLine(x,y+squareHeight()-1,x,y);
   g.drawLine(x,y,x+squareWidth()-1,y);
   g.setColor(color.darker());
   g.drawLine(x+1,y+squareHeight()-1,x+squareWidth()-1,y+squareHeight()-1);
   g.drawLine(x+squareWidth()-1,y+squareHeight()-1,x+squareWidth()-1,y+1);
  }
  if (type==1 || type==4){
   color = new Color(200,200,200);
   g.setColor(color);
   if (direction==1){
    g.fillRect(x+1+squareWidth()/5,y+2,squareWidth()/5,squareHeight()/4);
    g.fillRect(x+1+3*squareWidth()/5,y+2,squareWidth()/5,squareHeight()/4);
   } else if (direction==2){
    g.fillRect(x-2+3*squareWidth()/4,y+1+squareHeight()/5,squareWidth()/4,squareHeight()/5);
    g.fillRect(x-2+3*squareWidth()/4,y+1+3*squareHeight()/5,squareWidth()/4,squareHeight()/5);
   } else if (direction==3){
    g.fillRect(x+1+squareWidth()/5,y-2+3*squareHeight()/4,squareWidth()/5,squareHeight()/4);
    g.fillRect(x+1+3*squareWidth()/5,y-2+3*squareHeight()/4,squareWidth()/5,squareHeight()/4);
   } else if (direction==4){
    g.fillRect(x+3,y+1+squareHeight()/5,squareWidth()/4,squareHeight()/5);
    g.fillRect(x+3,y+1+3*squareHeight()/5,squareWidth()/4,squareHeight()/5);
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
   if (player.gold>=3){
    player.gold=player.gold-3;
    allies.add(new Wall(this,target[0],target[1]));
   }
  } else if (parent.allyChoice==1){
   if (player.gold>=10){
    player.gold=player.gold-10;
    allies.add(new FootSoldier(this,target[0],target[1]));
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
   }
  }
 }

 class TAdapter extends KeyAdapter{
  public void keyPressed(KeyEvent e){
   int keycode = e.getKeyCode();
   int keychar = e.getKeyChar();
   if (!isStarted){
    return;
   }
   if (keychar=='p' || keychar=='P' || keycode==KeyEvent.VK_ESCAPE){
    pause();
   }
   if (isPaused){
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
   } else if (keycode==KeyEvent.VK_TAB){
    if (!isPaused){
     pause();
    }
    Object[] list = new Object[2];
    list[0]="Wall";
    list[1]="FootSoldier";
    try{
     String choice = JOptionPane.showInputDialog(null,"Choose the ally you'd like to spawn.","Choice of Ally",JOptionPane.PLAIN_MESSAGE,null,list,list[parent.allyChoice]).toString();
     parent.allyChoice = Mob.allyIdentifier(choice);
     if (isPaused){
      pause();
     }
    } catch (Exception exception){
     System.out.println(exception);
    }
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














