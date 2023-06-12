package entity;

import features.Direction;
import game.GamePanel;
import monster.MON_Bat;
import npc.NPC_OldMan;

import java.util.Random;

public abstract class ArtificialIntelligence extends Creature {

    public TypeAI typeAI;
    public int actionLockCounterDirection = 0;
    public int actionLockCounterInMotion = 0;
    public int timeToChangeInMotion;
    public int timeToChangeDirection;
    public boolean onPath = false;

    public ArtificialIntelligence(GamePanel gp) {
        super(gp);

    }

    public void defaultBehavior() {
        actionLockCounterDirection++;

        if (timeToChangeInMotion > 0) {
            actionLockCounterInMotion++;
            if (actionLockCounterInMotion >= timeToChangeInMotion) {

                Random random = new Random();
                int randomMotion = random.nextInt(100) + 1;

                int chancesToChange = random.nextInt(100) + 1;

                inMotion = randomMotion > chancesToChange; // 60% sanse de a se afla in miscare
                actionLockCounterInMotion = 0;
            }
        }
        if (actionLockCounterDirection >= timeToChangeDirection) {

            Random random = new Random();
            int randomDirection = random.nextInt(100) + 1; // primeste un nr random intre 1-100

            int wantToChange = random.nextInt(100) + 1;

            int chancesToChange = random.nextInt(100) + 1;

            if (wantToChange > chancesToChange) { // 50 % sanse de a schimba directia

                if (randomDirection <= 25) {
                    direction = Direction.UP;
                }
                if (randomDirection > 25 && randomDirection <= 50) {
                    direction = Direction.DOWN;
                }
                if (randomDirection > 50 && randomDirection <= 75) {
                    direction = Direction.LEFT;
                }
                if (randomDirection > 75) {
                    direction = Direction.RIGHT;
                }

                actionLockCounterDirection = 0;
            }
        }
    }

    public void AI() {
        if (onPath) {
            pathFinding();
        }
        else {
            defaultBehavior();
        }
    }

    public abstract void pathFinding();

    public void searchPath(int goalCol, int goalRow, boolean isFollowingPlayer) {
        inMotion = true;
        int startCol = (int) ((worldX + solidArea.x) / gPanel.tileSize);
        int startRow = (int) ((worldY + solidArea.y) / gPanel.tileSize);

        gPanel.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        /** Daca gaseste o cale spre zona tinta */
        if (gPanel.pFinder.search()) {
            // Urmatorii worldX & worldY
            int nextX = gPanel.pFinder.pathList.get(0).col * gPanel.tileSize;
            int nextY = gPanel.pFinder.pathList.get(0).row * gPanel.tileSize;

            // Pozitia curenta a entitatii (se ia drept aria solida)
            int enLeftX = (int) (worldX + solidArea.x);
            int enRightX = (int) (worldX + solidArea.x + solidArea.width);
            int enTopY = (int) (worldY + solidArea.x);
            int enBottomY = (int) (worldY + solidArea.y + solidArea.height);

            /** Bazat pe pozitia curenta, se gaseste
             *  directia relativa a urmatorului nod */
            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gPanel.tileSize) {
                direction = Direction.UP;
            }
            else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gPanel.tileSize) {
                direction = Direction.DOWN;
            }
            else if (enTopY >= nextY && enBottomY < nextY + gPanel.tileSize) {
                /** STANGA SAU DREAPTA */
                if (enLeftX > nextX) {
                    direction = Direction.LEFT;
                }
                if (enLeftX < nextX) {
                    direction = Direction.RIGHT;
                }
            }
            else if (enTopY > nextY && enLeftX > nextX) {
                /** SUS SAU STANGA */
                direction = Direction.UP;
                checkCollisions();
                if (collisionOn) {
                    direction = Direction.LEFT;
                    inMotion = true;
                }
            }
            else if (enTopY > nextY && enLeftX < nextX) {
                /** SUS SAU DREAPTA */
                direction = Direction.UP;
                checkCollisions();
                if (collisionOn) {
                    direction = Direction.RIGHT;
                    inMotion = true;
                }
            }
            else if (enTopY < nextY && enLeftX > nextX) {
                /** JOS SAU STANGA */
                direction = Direction.DOWN;
                checkCollisions();
                if (collisionOn) {
                    direction = Direction.LEFT;
                    inMotion = true;
                }
            }
            else if (enTopY < nextY && enLeftX < nextX) {
                /** JOS SAU DREPATA */
                direction = Direction.DOWN;
                checkCollisions();
                if (collisionOn) {
                    direction = Direction.RIGHT;
                    inMotion = true;
                }
            }

            // Daca a ajuns la destinatie, cautarea ia sfarsit
            int nextCol = gPanel.pFinder.pathList.get(0).col;
            int nextRow = gPanel.pFinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow) {
                if (isFollowingPlayer) {
                    inMotion = false;
                }
                else {
                onPath = false; }
            }

        }
    }
}
