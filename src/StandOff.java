import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class StandOff{



    public static void main(String[] args) throws IOException {


        Player leftPLayer = new Player();
        leftPLayer.setyPosition(1500);

        int GamesPlayed, RedWins, BlueWins;

        JPanel panel = new JPanel();  //Creating the JPanel
        JFrame frame = new JFrame();  //Creating the JFrame
        JLabel picture = new JLabel(new ImageIcon("images//MainMenu.jpg")); //Creating the Background Image Label
        picture.setSize(1920, 1200);    //Sets the Image Size and Position
        picture.setLocation(0, 0);
        frame.setSize(1920, 1200);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(picture);
        frame.setLayout(null);
        frame.setVisible(true);


        //Creates the menu buttons
        JButton startGame = new JButton("Play Game");
        JButton howToPlay = new JButton("How to Play...");
        JButton Scores = new JButton("High Scores");
        JButton quitting = new JButton("Quit Game");

        JButton Save = new JButton("Save Scores");
        Save.setFont(new Font("Impact", Font.PLAIN, 35));
        Save.setSize(300, 75);
        Save.setLocation(825, 660);
        Save.setForeground(Color.cyan);
        frame.add(Save);

        //Sets up basic info on game button
        startGame.setFont(new Font("Impact", Font.PLAIN, 35));
        startGame.setSize(300, 75);
        startGame.setLocation(825, 260);
        startGame.setForeground(Color.cyan);


        //Sets up basic info on instructions button
        howToPlay.setFont(new Font("Impact", Font.PLAIN, 35));
        howToPlay.setSize(300, 75);
        howToPlay.setLocation(825, 360);
        howToPlay.setForeground(Color.cyan);


        Scores.setFont(new Font("Impact", Font.PLAIN, 35));
        Scores.setSize(300, 75);
        Scores.setLocation(825, 460);
        Scores.setForeground(Color.cyan);


        //Sets up basic info on quitting button
        quitting.setFont(new Font("Impact", Font.PLAIN, 35));
        quitting.setSize(300, 75);
        quitting.setLocation(825, 560);
        quitting.setForeground(Color.cyan);


        //Adds the buttons to the frame
        frame.add(startGame);
        frame.add(howToPlay);
        frame.add(Scores);
        frame.add(quitting);

        //Changes the buttons background color
        startGame.setBackground(Color.DARK_GRAY);
        howToPlay.setBackground(Color.DARK_GRAY);
        quitting.setBackground(Color.DARK_GRAY);
        Scores.setBackground(Color.DARK_GRAY);
        Save.setBackground(Color.DARK_GRAY);


        //ActionListeber for the start button
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //closes previous frame
                frame.setVisible(false);


                //Runs the main GUI
                try {
                    GUI game = new GUI();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
        });


        //Acyion Listener for the instructions button
        howToPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //Displays instructions in a JOp on click
                JOptionPane.showMessageDialog(null, "STAND-OFF\n\nA two Player competitive shooter where the aim \nof the game is to hit your enemy while avoiding\noncoming bullets...first person to land 3 hits on the oponent wins the game...\n\nNO WEAPON SPAMMING!!!" +
                        "\n\n\n\n\nBLUE PLAYER CONTROLS\n\nUP : W\nDOWN : S\nLEFT : A\nRIGHT : D\nSHOOT : SPACEBAR\n\n\n\n\nRED PLAYER CONTROLS\n\nUP : UP-ARROW\nDOWN : DOWN-ARROW\nLEFT : LEFT-ARROW\nRIGHT : RIGHT-ARROW\nSHOOT : NUMPAD 0", "How to Play", JOptionPane.PLAIN_MESSAGE);

            }
        });


        //Action listener for the quit button
        quitting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Takes in your yes or no answer (1 or 0)
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quitter", JOptionPane.YES_NO_OPTION);


                //if the answer is yes, cancel, otherwise do nothing
                if (answer == 0) {
                    System.exit(0);
                } else {
                }

            }
        });


        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JOptionPane.showMessageDialog(null, "You Have Nothing to Save!");





            }
        });


        Scores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, "Games Played: " + getData("data/TotalGamesPLayed")+ "\n\nRed has " + getData("data/RightWins") + " wins...\nBlue has " + getData("data/LeftWins") + " wins...");


            }
        });


    }
    public static int getData(String fileName)
    {
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
