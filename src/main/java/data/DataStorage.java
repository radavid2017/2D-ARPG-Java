package data;

import item.Item;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DataStorage implements Serializable {

    // PLAYER STATUS
    int level;
    int maxLife;
    int life;
    int maxMana;
    int mana;
    int strength;
    int dexterity;
    int exp;
    int nextLevelExp;
    int coin;
    String characterClassPath;

    // PLAYER INVENTORY
    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<Integer> itemAmounts = new ArrayList<>();
    int currentWeaponSlot;
    int currentShieldSlot;

    // OBJECT ON MAP
    String[][] mapObjectNames;
    int[][] mapObjectWorldX;
    int[][] mapObjectWorldY;
    String[][] mapObjectLootNames;
    boolean[][] mapObjectOpened;
}
