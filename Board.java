
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.*;
import java.io.*;

public class Board extends JPanel implements ActionListener{
 int boardWidth = 25;
 int boardHeight = 25;
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
 Image img = null;
 
 public Board(NewGame parenti){
  setFocusable(true);
  timer = new Timer(50, this);
  parent = parenti;
  statusbar = parent.getStatusBar();
  addKeyListener(new TAdapter());
  try {
   img = ImageIO.read(new File("background.jpg"));
  } catch (IOException e){
  }
  player = new Player(this,0,0);
  timer.start();
  start();
 }

 public void actionPerformed(ActionEvent e){
  if (enemies.size()==0){
   int positivex=1;
   int positivey=1;
   if (Math.random()>.5){
    positivex=-1;
   }
   if (Math.random()>.5){
    positivey=-1;
   }
   enemies.add(new Goblin(this, (int)(Math.random()*16*positivex+player.position[0]), (int)(Math.random()*16*positivey+player.position[1])));
  }
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
   timer.stop();
  } else {
   statusbar.setText(status);
   timer.start();
  }
 }


 int squareWidth(){return (int) getSize().getWidth() / boardWidth;}
 int squareHeight(){return (int) getSize().getHeight() / boardHeight;}

 public void paint(Graphics g){
  super.paint(g);
  //Background drawing
  g.drawImage(img, -(player.position[0]%boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth+boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth+boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth+boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth-boardWidth)*squareWidth(), -(player.position[1]%boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth-boardWidth)*squareWidth(), -(player.position[1]%boardHeight+boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  g.drawImage(img, -(player.position[0]%boardWidth-boardWidth)*squareWidth(), -(player.position[1]%boardHeight-boardHeight)*squareHeight(), (int)getSize().getWidth(), (int)getSize().getHeight(), null);
  Dimension size = getSize();
  int boardTop = (int) size.getHeight() - boardHeight * squareHeight();
  if (curView[0]<player.position[0]-boardWidth/4 || curView[0]>player.position[0]+boardWidth/4 || curView[1]<player.position[1]-boardHeight/4 || curView[1]<player.position[1]+boardHeight/4){
   curView[0]=player.position[0]-boardWidth/2;
   curView[1]=player.position[1]-boardHeight/2;
  }
  drawSquare(g,2+(player.position[0]-curView[0])*squareWidth(),boardTop+(player.position[1]-curView[1])*squareHeight(),1,player.lastMove,player.stab);
  for (int i=curView[0]; i<curView[0]+boardWidth; i++){
   for (int j=curView[1]; j<curView[1]+boardHeight; j++){
    for (int k=0; k<enemies.size(); k++){
     if (enemies.get(k).position[0]==i && enemies.get(k).position[1]==j){
      drawSquare(g,2+(enemies.get(k).position[0]-curView[0])*squareWidth(),boardTop+(enemies.get(k).position[1]-curView[1])*squareHeight(),enemies.get(k).type,enemies.get(k).lastMove,enemies.get(k).stab);
     }
    }
   }
  }
  drawSquare(g,2+(player.position[0]-curView[0])*squareWidth(),boardTop+(player.position[1]-curView[1])*squareHeight(),0,player.lastMove,player.stab);
 }

 public void drawSquare(Graphics g, int x, int y, int type, int direction, int attack){
  Color color;
  color = new Color(0,0,0);
  if (type==1){ //player
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
  if (type==2){ //goblin
   color = new Color(75,75,75);
   g.setColor(color);
   g.fillRect(x+2,y+2,squareWidth()-3,squareHeight()-3);
   g.setColor(color.darker());
   g.drawRect(x+1,y+1,squareWidth()-3,squareHeight()-3);
  }
  if (type!=0 && type!=1 && type!=2){
   g.setColor(color);
   g.fillRect(x+2,y+2,squareWidth()-3,squareHeight()-3);
   g.setColor(color.brighter());
   g.drawLine(x+1,y+squareHeight()-2,x+1,y+1);
   g.drawLine(x+1,y+1,x+squareWidth()-2,y+1);
   g.setColor(color.darker());
   g.drawLine(x+2,y+squareHeight()-2,x+squareWidth()-2,y+squareHeight()-2);
   g.drawLine(x+squareWidth()-2,y+squareHeight()-2,x+squareWidth()-2,y+2);
  }
  if (type==0 || type==1 || type==2){
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
   if (attack==0){
   } else if (attack==1){
    color = new Color(204,204,204);
    g.setColor(color);
    if (direction==1){
     g.fillPolygon(new int[]{x-1+squareWidth()/2,x-1+2*squareWidth()/3,x-1+5*squareWidth()/6},new int[]{y-squareHeight()/4,y-(int)3.5*squareHeight()/4,y-squareHeight()/4},3);
    } else if (direction==2){
     g.fillPolygon(new int[]{x+5*squareWidth()/4,x+(int)7.5*squareWidth()/4,x+5*squareWidth()/4},new int[]{y-1+squareHeight()/2,y-1+2*squareHeight()/3,y-1+5*squareHeight()/6},3);
    } else if (direction==3){
     g.fillPolygon(new int[]{x+squareWidth()/2,x+squareWidth()/3,x+squareWidth()/6},new int[]{y+5*squareHeight()/4,y+(int)7.5*squareHeight()/4,y+5*squareHeight()/4},3);
    } else if (direction==4){
     g.fillPolygon(new int[]{x-squareWidth()/4,x-(int)3.5*squareWidth()/4,x-squareWidth()/4},new int[]{y+squareHeight()/2,y+squareHeight()/3,y+squareHeight()/6},3);
    }
    color = new Color(0,0,0);
    g.setColor(color);
    if (direction==1){
     g.drawPolygon(new int[]{x-1+squareWidth()/2,x-1+2*squareWidth()/3,x-1+5*squareWidth()/6},new int[]{y-squareHeight()/4,y-(int)3.5*squareHeight()/4,y-squareHeight()/4},3);
    } else if (direction==2){
     g.drawPolygon(new int[]{x+5*squareWidth()/4,x+(int)7.5*squareWidth()/4,x+5*squareWidth()/4},new int[]{y-1+squareHeight()/2,y-1+2*squareHeight()/3,y-1+5*squareHeight()/6},3);
    } else if (direction==3){
     g.drawPolygon(new int[]{x+squareWidth()/2,x+squareWidth()/3,x+squareWidth()/6},new int[]{y+5*squareHeight()/4,y+(int)7.5*squareHeight()/4,y+5*squareHeight()/4},3);
    } else if (direction==4){
     g.drawPolygon(new int[]{x-squareWidth()/4,x-(int)3.5*squareWidth()/4,x-squareWidth()/4},new int[]{y+squareHeight()/2,y+squareHeight()/3,y+squareHeight()/6},3);
    }
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
   if (keychar=='p' || keychar=='P'){
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














