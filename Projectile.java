import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Projectile implements ActionListener{
 int[] origin = new int[2];
 int[] position = new int[2];
 float[] dposition = new float[2];
 int[] destination = new int[2];
 int damage = 1;
 Board parent = null;
 int type = 0;
 String name = "projectile";
 Timer timer;
 int orientation = 1;
 int allegiance = 0;
 float xChange = 1;
 float yChange = 1;
 int turns = 1;
 int turn = 0;
 boolean dead = false;

 public void actionPerformed(ActionEvent e){
 }
}