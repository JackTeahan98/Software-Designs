import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ModeRetro implements  PlayerMode{
    @Override
    public String configureMode(Player player) throws IOException {

        if (player.getName().equals("leftPlayer")) {
            player.setImage(ImageIO.read(new File("images/leftAlternate.png")));
            return "Retro Mode activated for left player";

        }

        else if (player.getName().equals("rightPlayer")) {
            player.setImage(ImageIO.read(new File("images/rightAlternate.png")));
            return "Retro Mode activated for right player";
        }

        else
            return "Retro Mode not activated";
    }
}
