import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;



public class GUI extends JPanel implements ActionListener, KeyListener, Runnable{


    int GamesPlayed;
    int RedWins,BlueWins;
    private JComponent component;
    private Timer timer;
    private Map<String, Point> pressedKeys = new HashMap<String, Point>();


    /**player movement speeds on both axi**/
    int XjumpDistance = 85;
    int YjumpDistance = 85;

    /**ints for both players, that show the X and Y axi for the bullets**/
    int bulletX, bulletY;
    int bulletX2, bulletY2;

    /**Ints for storing how many lives left for the players**/



    /**booleans for both players, that show if the bullet s shot, and ready to shoot again**/
    boolean readyToFire, shot = false;
    boolean readyToFire2, shot2 = false;

    /**Created a rectangle base for both players bullets**/
    Rectangle bullet;
    Rectangle bullet2;


    Graphics g;  /**Graphics instance called g**/
    Thread theThread;  /**Thread called theThread**/
    boolean gameOn;    /**Boolean called gameon**/


    /**Creates a JFrame for the GUI**/
    private JFrame frame = new JFrame("StandOff(TM)");

    /**Creates a frame and panel for the winning players frame**/
    private JFrame winFrame = new JFrame();
    private JPanel winPanel = new JPanel();




    /**Creates 2 Players from the player class**/
    public Player leftPlayer = new Player("leftPlayer", 160, 350,ImageIO.read(new File("images/leftPlayer.png")),5);
    public Player rightPlayer = new Player("rightPlayer", 1450, 350,ImageIO.read(new File("images/rightPlayer.png")),5);

    int BLUElivesRemaining;
    int REDlivesRemaining;





    /**Some the images used**/
    /** https://stackoverflow.com/questions/13011705/how-to-add-an-imageicon-to-a-jframe**/
    Image background  = new ImageIcon("images/background1.png").getImage();
    Image leftBullet  = new ImageIcon("images/blueBullet.png").getImage();
    Image rightBullet = new ImageIcon("images/redBullet.png").getImage();
    ImageIcon electricBlue = new ImageIcon("images/lightningBlue.png");
    ImageIcon electricRed = new ImageIcon("images/lightningRed.png");
    ImageIcon REDHIT = new ImageIcon("images/redHit.png");
    ImageIcon BLUEHIT = new ImageIcon("images/blueHit.png");


    /**The constructor and GUI of the game**/
    public GUI() throws IOException {
        GamesPlayed=getData("data/TotalGamesPLayed");
        System.out.println(GamesPlayed);
        frame.setSize(1920,1200);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(this);
        setLayout(new FlowLayout());
        frame.setBackground(new Color(52, 46, 47));
        frame.addKeyListener(this);
        gameOn = true;


        saveData("data/TotalGamesPLayed", GamesPlayed++);


        leftPlayer.setPlayerMode(new ModeInfiniteHealth());
        String message=leftPlayer.configureMode(leftPlayer).toString();
        JOptionPane.showMessageDialog(null, message, "Mode", JOptionPane.INFORMATION_MESSAGE);

        String messageRetroR;
        leftPlayer.setPlayerMode(new ModeRetro());
        messageRetroR=leftPlayer.configureMode(leftPlayer).toString();
        JOptionPane.showMessageDialog(null, messageRetroR, "Mode", JOptionPane.INFORMATION_MESSAGE);



        BLUElivesRemaining= leftPlayer.getLives();
        REDlivesRemaining = rightPlayer.getLives();

    }




    /**Graphics (ABSTRACT) method called paint, which paints all the images and instances onto the screen**/
    public void paint(Graphics g)
    {




        /**Draws the background image**/
        g.drawImage(background,0,0,null);

        /**Draws left and right players**/
        leftPlayer.drawPlayer(g);
        rightPlayer.drawPlayer(g);

        /**Starts the thread**/
        start();


        /**if a shot is true, it paints a bullet to the screen, and increments along horizontally**/
        if(shot)
        {
            g.drawImage(leftBullet,bulletY+200,bulletX+7,null);
            bulletY+= 70;

        }


        /**if a shot is true, it paints a bullet to the screen, and increments along horizontally**/
        if(shot2)
        {
            g.drawImage(rightBullet,bulletY2-80,bulletX2+25,null);
            bulletY2 -= 70;
        }


        /**Methods for determining bullet hit detection**/
        rightPlayerBulletLand();
        leftPlayerBulletLand();
    }








    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {


    }





    @Override
    public void keyPressed(KeyEvent e) {

        /**These events occur when the specific key is pressed, it makes the player move in the respective direction..
         it sets their new position to their current position, plus jumpDistance**/

        if (e.getKeyCode() == KeyEvent.VK_W) {
            leftPlayer.setyPosition(leftPlayer.getyPosition() - YjumpDistance);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            leftPlayer.setyPosition(leftPlayer.getyPosition() + YjumpDistance);


        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            rightPlayer.setyPosition(rightPlayer.getyPosition() - YjumpDistance);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            rightPlayer.setyPosition(rightPlayer.getyPosition() + YjumpDistance);


        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            leftPlayer.setxPosition(leftPlayer.getxPosition() - XjumpDistance);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            leftPlayer.setxPosition(leftPlayer.getxPosition() + XjumpDistance);


        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            rightPlayer.setxPosition(rightPlayer.getxPosition() - XjumpDistance);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPlayer.setxPosition(rightPlayer.getxPosition() + XjumpDistance);


            /**Shooting key
             Since the shooting was definately the hardest part, i had to research videos and stack overflow for some tips on the general shooting mechanics*
             https://www.youtube.com/watch?v=0MKLBh0fBbg - Youtube video by BrandonioProductions
             https://stackoverflow.com/questions/14432816/how-to-move-an-image-animation - Stack Overflow**/
        } else if (e.getKeyCode() == KeyEvent.VK_P) {


            /**if there is no bullet, then it is ready to fire again**/
            if (bullet2 == null)
                readyToFire2 = true;

            /**if it is ready to fire, then set bullets X and Y to the players current X and Y**/
            if (readyToFire2 == true)
                bulletY2 = rightPlayer.getxPosition();
            bulletX2 = rightPlayer.getyPosition();

            /**Create a rectangle basis with the new coordinates and make shot true**/
            bullet2 = new Rectangle(bulletY2 + 65, bulletX2 + 45, 50, 5);
            shot2 = true;



        } else if (e.getKeyCode() == KeyEvent.VK_T) {


            if (bullet == null)
                readyToFire = true;

            if (readyToFire == true)
                bulletY = leftPlayer.getxPosition();
            bulletX = leftPlayer.getyPosition();
            bullet = new Rectangle(bulletY + 265, bulletX + 45, 50, 5);
            shot = true;






        }


        /**Collision Detection for hitting the walls**/
        leftPlayerWallHit();
        rightPlayerWallHit();




        /**Allows the frame to refresh**/
        repaint();

    }





    /**Method makes sure that the bullet has actually been fired**/
    public void shoot()
    {
        if(shot)
            bullet.x += 100;
    }

    public void shoot2()
    {
        if(shot2)
            bullet2.x -= 100;
    }





    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_W) {}
        else if(e.getKeyCode() == KeyEvent.VK_S) {}

        else if(e.getKeyCode() == KeyEvent.VK_UP) {}
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){}

        else if(e.getKeyCode() == KeyEvent.VK_T) {}



        /**Attempted to make the bullet only able to fire once, eg, no spamming it...attempted and failed*/
         if(e.getKeyCode() == KeyEvent.VK_P)
        {
            readyToFire2 = false;

            if(bullet2.x <= 10)
            {
                readyToFire2 = true;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_T)
        {
            readyToFire = false;

            if(bullet.x >= 1920)
            {
                readyToFire = true;
            }
        }

    }

    /**Method for wall collision**/
    public void leftPlayerWallHit(){

        /**If the player exceeds these boundaries...**/
        if(leftPlayer.getxPosition() < -1  || leftPlayer.getxPosition() >= 750  || leftPlayer.getyPosition() < -100 || leftPlayer.getyPosition() >= 800)
        {


            /**..and display this message and this method
             For the images with the Message Dialog i had help from Jack o Hara who showed me to just put the image as the last part of the JOp**/



            JOptionPane.showMessageDialog(null,"Blue has hit the wall...\n\nRed Player wins!","ERROR 404: DeathByElectrocution.yolo",JOptionPane.PLAIN_MESSAGE,electricBlue);
            frame.setVisible(false);
            redWinScreenGUI();
        }
    }


    public void rightPlayerWallHit()
    {
        if(rightPlayer.getxPosition() > 1650  || rightPlayer.getxPosition() <= 850  || rightPlayer.getyPosition() < -100 || rightPlayer.getyPosition() >= 800)
        {


            JOptionPane.showMessageDialog(null, "Red has hit the wall...\n\nBlue Player wins!","ERROR 404: DeathByElectrocution.yolo", JOptionPane.PLAIN_MESSAGE,electricRed);
            frame.setVisible(false);
            blueWinScreenGUI();


        }
    }


    public void rightPlayerBulletLand()
    {
        /**JB - has the left players bullet hit the right player
         Struggled to get this to work for ages, had help from John Brosnan
         If the bullet is at these boundaries...**/
        if(bullet2!=null && bullet2.getX() <= leftPlayer.getxPosition()  && bullet2.getX() >= 0      &&    bullet2.getY() <= leftPlayer.getyPosition() + 300 && bullet2.getY() >= leftPlayer.getyPosition()) {



            /**..Minus a life from the oponent**/
            BLUElivesRemaining --;

            /**Display this message**/
            JOptionPane.showMessageDialog(null,"Blue has taken a hit!\n\nHe has:\n"+BLUElivesRemaining+" lives remaining...","HIT!",JOptionPane.PLAIN_MESSAGE,BLUEHIT);

            bullet2 = null;  /**now the bullet (Rectangle) reference is set to null so we don't keep getting the same message**/
            shot2 = false;  /**now we set shot to false to make sure the bullet stops getting drawn on screen**/

            /**if the player has no more lives, then open up a new GUI**/
            if(BLUElivesRemaining == 0)
            {

                frame.setVisible(false);
                redWinScreenGUI(); }
        }
    }


    public void leftPlayerBulletLand()
    {
        if(bullet!=null && bullet.getX() >= rightPlayer.getxPosition() +300 && bullet.getX() <= frame.getWidth() && bullet.getY() <= rightPlayer.getyPosition() + 300 && bullet.getY() >= rightPlayer.getyPosition()) {



            REDlivesRemaining --;

            /**Display this message**/
            JOptionPane.showMessageDialog(null,"Red has taken a hit!\n\nHe has:\n"+REDlivesRemaining+" lives remaining...","HIT!",JOptionPane.PLAIN_MESSAGE,REDHIT);

            bullet = null; //now the bullet (Rectangle) reference is set to null so we don't keep getting the same message**/
            shot = false; //now we set shot to false to make sure the bullet stops getting drawn on screen**/

            /**if the player has no more lives, then open up a new GUI**/
            if(REDlivesRemaining == 0)
            {



                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                frame.setVisible(false);
                blueWinScreenGUI(); }
        }
    }










    /**JB - called automatically by the start() method below. Because the game needs to draw information onto the screen
     and listen for keyboard/other events at the same time, an extra thread of execution is recommended. The run()
     method basically ensures that the pane of the JFrame window gets painted/updated every 20 milliseconds
     giving us 50 frames per second as such. The thread sleeps in between these updates meaning that the rest
     of the time events can be listened for and handled without any conflict

     thread really needed only because we need it to continually repaint() the screen if we want to draw the bullets moving as well
     as the enemy invaders, otherwise we could have done without it as the key press actions could be set up to make calls to paint() as they go




     Threading, John Brosnan sent on the run and start methods shown below to show me how to use threading as it was a topic we never encountered before**/
    public void run()
    {
        g = getGraphics();

        /**while the game is running**/
        while(gameOn)
        {
            try
            {
                /**run these methods**/
                shoot();
                shoot2();
                paint(g);


                /**time between each thread**/
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                break;
            }
        }
        System.out.println("Game now over!");
    }








    public void start()
    {
        if (theThread == null)
        {
            System.out.println("Creating new thread");
            theThread = new Thread(this);
            theThread.start();
        }
    }



    public void MainGUI()
    {
        JPanel panel = new JPanel ( );
        JFrame frame = new JFrame ( );
        JLabel picture = new JLabel (new ImageIcon ("images//MainMenu.jpg"));
        picture.setSize (1920,1200);
        picture.setLocation (0,0);

        frame.setSize (1920, 1200);
        frame.setResizable (false);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.add(picture);
        frame.setLayout (null);


        panel.setLayout (new FlowLayout ( ));
        frame.setBackground (Color.blue);
        frame.setVisible (true);
        panel.setVisible (true);
        picture.setVisible (true);

        JButton startGame = new JButton ("Play Game");
        JButton howToPlay = new JButton ("How to Play...");
        JButton Scores = new JButton ("High Scores");
        JButton quitting = new JButton ("Quit Game");


        JButton Save = new JButton ("Save Scores");
        Save.setFont (new Font ("Impact", Font.PLAIN, 35));
        Save.setSize (300, 75);
        Save.setLocation (825, 660);
        Save.setForeground(Color.cyan);

        startGame.setFont (new Font ("Impact", Font.PLAIN, 35));
        startGame.setSize (300, 75);
        startGame.setLocation (825, 275);
        startGame.setForeground(Color.cyan);


        howToPlay.setFont (new Font ("Impact", Font.PLAIN, 35));
        howToPlay.setSize (300, 75);
        howToPlay.setLocation (825, 375);
        howToPlay.setForeground(Color.cyan);


        Scores.setFont (new Font ("Impact", Font.PLAIN, 35));
        Scores.setSize (300, 75);
        Scores.setLocation (825, 460);
        Scores.setForeground(Color.cyan);


        quitting.setFont (new Font ("Impact", Font.PLAIN, 35));
        quitting.setSize (300, 75);
        quitting.setLocation (825, 560);
        quitting.setForeground(Color.cyan);


        frame.add (startGame);
        frame.add (howToPlay);
        frame.add (quitting);
        frame.add (Scores);
        frame.add (Save);


        startGame.setBackground (Color.DARK_GRAY);
        howToPlay.setBackground (Color.DARK_GRAY);
        quitting.setBackground (Color.DARK_GRAY);
        Scores.setBackground (Color.DARK_GRAY);
        Save.setBackground (Color.DARK_GRAY);




        startGame.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible (false);



                try {
                    GUI game = new GUI();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
        });



        Save.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {


                JOptionPane.showMessageDialog(null,"Saved Successfully!");
                try {
                    save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
        });







        howToPlay.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {



                JOptionPane.showMessageDialog(null, "STAND-OFF\n\nA two Player competitive shooter where the aim \nof the game is to hit your enemy while avoiding\noncoming bullets...first person to land 3 hits on the oponent wins the game...\n\n\nNO WEAPON SPAMMING!!!" +
                        "\n\n\n\n\nBLUE PLAYER CONTROLS\n\nUP : W\nDOWN : S\nLEFT : A\nRIGHT : D\nSHOOT : SPACEBAR\n\n\n\n\nRED PLAYER CONTROLS\n\nUP : UP-ARROW\nDOWN : DOWN-ARROW\nLEFT : LEFT-ARROW\nRIGHT : RIGHT-ARROW\nSHOOT : NUMPAD 0", "How to Play", JOptionPane.PLAIN_MESSAGE);

            }
        });




        quitting.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {

                int answer = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?","Quitter",JOptionPane.YES_NO_OPTION);

                if(answer == 0)
                {
                    System.exit(0);
                }

                else {


                }
            }
        });


        Scores.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, "Games Played: " + getData("data/TotalGamesPLayed")+ "\n\nRed has " + getData("data/RightWins") + " wins...\nBlue has " + getData("data/LeftWins") + " wins...");





            }
        });
    }


    /**A simple red and blue winning GUIs below that displays who wins**/
    public void redWinScreenGUI()
    {
        RedWins=getData("data/RightWins");
        saveData("data/RightWins", RedWins++);

        JLabel picture = new JLabel (new ImageIcon ("images//redWinbackground.png"));
        picture.setSize (1920,1200);
        picture.setLocation (0,0);

        winFrame.setSize (1920, 1200);
        winFrame.setResizable (false);
        winFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        winFrame.add(picture);
        winFrame.setLayout (null);


        winPanel.setLayout (new FlowLayout ( ));
        winFrame.setBackground (Color.blue);
        winFrame.setVisible (true);
        winPanel.setVisible (true);
        picture.setVisible (true);


        JButton backToGame = new JButton ("Return to main menu...");

        backToGame.setFont (new Font ("Impact", Font.PLAIN, 100));
        backToGame.setSize (1000, 150);
        backToGame.setLocation (500, 600);
        backToGame.setForeground(Color.red);

        winFrame.add(backToGame);
        backToGame.setBackground (Color.BLACK);

        backToGame.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {

                winFrame.setVisible (false);

                MainGUI();



            }
        });

    }



    public void blueWinScreenGUI()
    {
        BlueWins=getData("data/LeftWins");
        saveData("data/LeftWins", BlueWins++);
        JLabel picture = new JLabel (new ImageIcon ("images//blueWinbackground.png"));
        picture.setSize (1920,1200);
        picture.setLocation (0,0);

        winFrame.setSize (1920, 1200);
        winFrame.setResizable (false);
        winFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        winFrame.add(picture);
        winFrame.setLayout (null);


        winPanel.setLayout (new FlowLayout ( ));
        winFrame.setBackground (Color.blue);
        winFrame.setVisible (true);
        winPanel.setVisible (true);
        picture.setVisible (true);

        JButton backToGame = new JButton ("Return to main menu...");

        backToGame.setFont (new Font ("Impact", Font.PLAIN, 100));
        backToGame.setSize (1000, 150);
        backToGame.setLocation (500, 600);
        backToGame.setForeground(Color.cyan );


        winFrame.add(backToGame);
        backToGame.setBackground (Color.BLACK);

        backToGame.addActionListener (new ActionListener ( ) {
            @Override
            public void actionPerformed(ActionEvent e) {

                winFrame.setVisible (false);

                MainGUI();



            }
        });
    }


    public void save() throws IOException
    {
        ObjectOutput os;
        os = new ObjectOutputStream(new FileOutputStream("score.dat"));
        os.writeObject(GamesPlayed);
        os.close();
    }
    
    public void saveData(String fileName, int value){

        int value2 = value+1;
        String valueAsString=Integer.toString(value2);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.write(valueAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public int getData(String fileName) {
        String paragraph="";
        String line = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

            while(true)
            {
                try {
                    if (!((line = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(line);
                paragraph+=line;
            }

        try {
            bufferedReader.close();
        } catch (IOException e) {

        }
        int paragphAsInt=Integer.parseInt(paragraph);
        return paragphAsInt;


    }

}