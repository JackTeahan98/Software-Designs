import javax.swing.*;

public class ModeInfiniteHealth implements PlayerMode{

    @Override
    public String configureMode(Player player) {
        player.setLives(100000);

        if (player.getName().equals("leftPlayer")) {
            return "Left Player has activated invulnerability";
        } else
            {
            return "Right Player has activated invulnerability";
        }
    }
}
