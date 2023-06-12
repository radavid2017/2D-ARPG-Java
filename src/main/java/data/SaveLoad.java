package data;

import entity.Entity;
import game.GamePanel;
import interactive_tile.IT_MetalPlate;
import item.Item;
import item.consumable.Tent;
import item.consumable.key.KeyGold;
import item.consumable.potion.PotionRed;
import item.equipable.light.Lantern;
import item.equipable.shield.BlueShield;
import item.equipable.shield.NormalShield;
import item.equipable.shield.Shield;
import item.equipable.weapon.Weapon;
import item.equipable.weapon.axe.Baltag;
import item.equipable.weapon.pickaxe.Tarnacop;
import item.equipable.weapon.sword.NormalSword;
import object.obstacle.door.IronDoor;
import object.obstacle.OBJ_Chest;
import object.obstacle.WoodChest;
import object.obstacle.door.WoodDoor;

import java.io.*;

public class SaveLoad {
    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public Entity getObject(String itemName) {
        Entity obj = null;

        switch (itemName) {
            case "Baltag" -> obj = new Baltag(gp);
            case "Cheie de aur" -> obj = new KeyGold(gp);
            case "Licoare rosie" -> obj = new PotionRed(gp);
            case "Sabie Normala" -> obj = new NormalSword(gp);
            case "Scut Normal" -> obj = new NormalShield(gp);
            case "Scut Albastru" -> obj = new BlueShield(gp);
            case "Cort" -> obj = new Tent(gp);
            case "Lanterna" -> obj = new Lantern(gp);
            case "Usa de lemn" -> obj = new WoodDoor(gp);
            case "Cufarul Padurarului" -> obj = new WoodChest(gp);
            case "Tarnacop" -> obj = new Tarnacop(gp);
            case "Usa din fier" -> obj = new IronDoor(gp);
            case "Placa metalica" -> obj = new IT_MetalPlate(gp);
        }

        return obj;
    }

    public void save() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"));

            DataStorage ds = new DataStorage();

            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;
            ds.characterClassPath = gp.characterClassPath;

            // PLAYER INVENTORY
            for (int i = 0; i < gp.player.inventory.size(); i++) {
                System.out.println(gp.player.inventory.get(i).name);
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }

            // PLAYER EQUIPMENT
            ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
            ds.currentShieldSlot = gp.player.getCurrentShieldSlot();

            // OBJECTS ON MAP
            ds.mapObjectNames = new String[gp.maxMap][gp.objects.get(0).size()];
            ds.mapObjectWorldX = new int[gp.maxMap][gp.objects.get(0).size()];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.objects.get(0).size()];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.objects.get(0).size()];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.objects.get(0).size()];

            for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                if (gp.objects.get(mapNum) != null) {
                    for (int i = 0; i < gp.objects.get(mapNum).size(); i++) {
                        if (gp.objects.get(mapNum) != null) {
                            if (gp.objects.get(mapNum).get(i) == null) {
                                ds.mapObjectNames[mapNum][i] = "NA";
                            } else {
                                ds.mapObjectNames[mapNum][i] = gp.objects.get(mapNum).get(i).name;
                                ds.mapObjectWorldX[mapNum][i] = (int) gp.objects.get(mapNum).get(i).worldX;
                                ds.mapObjectWorldY[mapNum][i] = (int) gp.objects.get(mapNum).get(i).worldY;
                                if (gp.objects.get(mapNum).get(i) instanceof OBJ_Chest chest) {
                                    ds.mapObjectLootNames[mapNum][i] = chest.getLoot().name;
                                    ds.mapObjectOpened[mapNum][i] = chest.getIsOpened();
                                }
                            }
                        }
                    }
                }
            }

            // Write the DataStorage object
            oos.writeObject(ds);

            System.out.println("SALVAT CU SUCCES!");

        } catch (IOException e) {
            System.out.println("Save Exception!");
            throw new RuntimeException(e);
        }

    }

    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));

            // Read the DataStorage object
            DataStorage ds = (DataStorage) ois.readObject();

            gp.characterClassPath = ds.characterClassPath;

            gp.startGame();

            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;

            // PLAYER INVENTORY
            gp.player.inventory.clear();
            for (int i = 0; i < ds.itemNames.size(); i++) {
                System.out.println(ds.itemNames.get(i));
                gp.player.inventory.add((Item) getObject(ds.itemNames.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
            }

            // PLAYER EQUIPMENT
            gp.player.currentWeapon = (Weapon) gp.player.inventory.get(ds.currentWeaponSlot);
            gp.player.currentShield = (Shield) gp.player.inventory.get(ds.currentShieldSlot);
            gp.player.updateAttack();
            gp.player.updateDefense();

            // OBJECTS ON MAP
            for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                if (gp.objects.get(mapNum) != null) {
                    for (int i = 0; i < ds.mapObjectNames[mapNum].length; i++) {
                        if (ds.mapObjectNames[mapNum][i] != null) {
                            if (ds.mapObjectNames[mapNum][i].equals("NA")) {
                                gp.objects.get(mapNum).set(i, null);
                            } else {
                                gp.objects.get(mapNum).set(i, getObject(ds.mapObjectNames[mapNum][i]));
                                if (gp.objects.get(mapNum).get(i) != null) {
                                    gp.objects.get(mapNum).get(i).worldX = ds.mapObjectWorldX[mapNum][i];
                                    gp.objects.get(mapNum).get(i).worldY = ds.mapObjectWorldY[mapNum][i];
                                    if (gp.objects.get(mapNum).get(i) instanceof OBJ_Chest chest) {
                                        chest.setLoot((Item) getObject(ds.mapObjectLootNames[mapNum][i]));
                                        chest.setOpened(ds.mapObjectOpened[mapNum][i]);
                                        if (chest.getIsOpened()) {
                                            chest.image = chest.nextState();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Load Exception!");
            throw new RuntimeException(e);
        }
    }
}
