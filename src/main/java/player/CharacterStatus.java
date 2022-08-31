package player;

import game.GamePanel;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CharacterStatus {
    public LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

    public CharacterStatus(GamePanel gp) {
        parameters.put("Nivel", String.valueOf(gp.player.level));
        parameters.put("Vieti", gp.player.life + "/" + gp.player.maxLife);
        parameters.put("Putere", String.valueOf(gp.player.strength));
        parameters.put("Dexteritate", String.valueOf(gp.player.dexterity));
        parameters.put("Atac", String.valueOf(gp.player.attack));
        parameters.put("Aparare", String.valueOf(gp.player.defense));
        parameters.put("Exp", String.valueOf(gp.player.exp));
        parameters.put("Nivel urm", String.valueOf(gp.player.nextLevelExp));
        parameters.put("Bani", String.valueOf(gp.player.coin));
        parameters.put("Arma", gp.player.currentWeapon.image);
        parameters.put("Scut", gp.player.currentShield.image);
    }

    public int numImages() {
        int numImg = 0;
        for (Object value : parameters.values()) {
            if (value.getClass() == BufferedImage.class)
                numImg++;
        }
        return 50*(numImg+1);
    }
}
