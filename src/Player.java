import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends JPanel{

    private String name;
    private int xPosition;
    private int yPosition;
    private BufferedImage player;
    private PlayerMode playerMode;
    private int lives;


    public void setPlayerMode(PlayerMode playerMode){this.playerMode = playerMode;}
    public String configureMode(Player player) throws IOException {return playerMode.configureMode(player);}

    //Getters and Setters - Name
    public String getName()
    {return name;}
    public void setName(String name)
    {this.name = name;}



    public int getLives()
    {return lives;}
    public void setLives(int lives)
    {this.lives = lives;}


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


    public Player()
    {
        this("Unknown",0,0,null,0);
    }


    public Player(String name, int xPosition, int yPosition, BufferedImage player, int lives) {
        setName(name);
        setxPosition(xPosition);
        setyPosition(yPosition);
        setImage(player);
        setLives((lives));}


    public void drawPlayer(Graphics g){
        ImageIcon img = new ImageIcon(player);
        g.drawImage(img.getImage(),getxPosition(),getyPosition(),null);
    }


}


