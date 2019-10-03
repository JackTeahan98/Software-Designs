import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends JPanel{

    private String name;
    private int xPosition;
    private int yPosition;
    private BufferedImage player;


    //Getters and Setters - Name
    public String getName()
    {return name;}
    public void setName(String name)
    {this.name = name;}


    //Getters and Setters - X Position
    public int getxPosition()
    {return xPosition;}
    public void setxPosition(int xPosition)
    {this.xPosition = xPosition;}



    //Getters and Setters - Y Position
    public int getyPosition()
    {return yPosition;}
    public void setyPosition(int yPosition)
    {this.yPosition = yPosition;}


    //Getters and Setters - Buffered Image
    public BufferedImage getImage()
    {return player;}
    public void setImage(BufferedImage player)
    {this.player = player;}



    //No Argument Constructor (Was mainly used for testing)
    public Player()
    {
        this("Unknown",0,0,null);
    }


    // 4 Argument Constructor
    public Player(String name, int xPosition, int yPosition, BufferedImage player) {
        setName(name);
        setxPosition(xPosition);
        setyPosition(yPosition);
        setImage(player);
    }


    // Graphics method, Used to draw the Player Objects Image
    // Collborated with Petrit Krasniqi on tips in how to use it
    public void drawPlayer(Graphics g){
        ImageIcon img = new ImageIcon(player);
        g.drawImage(img.getImage(),getxPosition(),getyPosition(),null);
    }


}


