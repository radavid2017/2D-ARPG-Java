package ai;

import entity.Entity;
import game.GamePanel;
import interactive_tile.DestructibleTile;
import npc.NPC_OldMan;
import tile.Tile;

import java.util.ArrayList;

// HANDLER PATHFINDING
public class PathFinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalreached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        /** Creez cate un nod pentru fiecare tile de pe harta */
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            // Reseteaza open, checked si solid state
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        // Resetez restul setarilor
        openList.clear();
        pathList.clear();
        goalreached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        // Setare nod de start si nod final
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            // Setare nod solid
            // Verific tile
            int tileNum = gp.tiles.mapTileNum[gp.currentMap][col][row];
            for (Tile tile : gp.tiles.generalTiles) {
                if (tile.idTile == tileNum) {
                    if (tile.isColliding) {
                        node[col][row].solid = true;
                        break;
                    }
                }
            }

            // Verific tile-urile interactive
            checkInteractiveTiles(gp.interactiveTiles.get(gp.currentMap));

            // Setez costul
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    private void checkInteractiveTiles(ArrayList<Entity> interactiveTiles) {
        for (int i = 0; i < gp.interactiveTiles.size(); i++) {
            if (interactiveTiles.get(i) != null &&
                    interactiveTiles.get(i) instanceof DestructibleTile) {
                // obiect solid
                int itCol = (int) (interactiveTiles.get(i).worldX/gp.tileSize);
                int itRow = (int) (interactiveTiles.get(i).worldY/gp.tileSize);
                node[itCol][itRow].solid = true;
            }
        }
    }

    private void getCost(Node node) {
        // G cost - distanta de la nodul de start la nodul curent
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        // H cost - distanta de la nodul curent la nodul relativ final
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        // F cost - distanta totala parcursa
        node.fCost = node.gCost + node.hCost;
    }

    /** SCOP: Scanam lista de noduri deschise si gasim
     *  cel mai bun nod de deschis la urmatorul pas */
    public boolean search() {
        while (!goalreached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            // Verific nodul curent
            currentNode.checked = true;
            openList.remove(currentNode);

            // Deschid Nodul de sus
            if (row - 1 >= 0) {
                openNode(node[col][row-1]);
            }

            // Deschid nodul din stanga
            if (col - 1 >= 0) {
                openNode(node[col-1][row]);
            }

            // Deschid nodul de jos
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[col][row+1]);
            }

            // Deschid nodul din dreapta
            if (col + 1 < gp.maxWorldCol) {
                openNode(node[col+1][row]);
            }

            /** Gaseste cel mai bun nod */
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // Verific daca costul F al nodului i este mai bun
                if (openList.get(i).fCost < bestNodefCost) {
                    // Actualizez nodul cu cel mia bun f cost
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                // Daca F cost este egal
                // Verificam cu G cost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // Daca nu exista nod in lista curenta, inchidem ciclul
            if (openList.size() == 0) {
                break;
            }

            // Dupa ciclu, openList[bestNodeindex] este pasul urmator
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalreached = true;
                trackThePath();
            }
            step++;
        }
        return goalreached;
    }

    private void trackThePath() {
        Node current = goalNode;

        while(current != startNode) {
            /** Mereu adaug primul slot
             * deci ultimul nod se afla pe pozitia 0 */
            pathList.add(0, current);
            current = current.parent;
        }
    }

    private void openNode(Node node) {

        /** Conditii deschidere nod
         * nodul nu trebuie sa fie deschis deja
         * sa nu fie verificat deja
         * sa nu fie solid */
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
}
