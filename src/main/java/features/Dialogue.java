package features;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Dialogue {
    public ArrayList<String> textStack = new ArrayList<>();
    private boolean ended = false;
    public int indexText = -1;

    public Dialogue() {

    }

    public Dialogue(ArrayList<String> textStack) { // cpmstructor dialog
        this.textStack = textStack;
    }

    public Dialogue(String text) {
        textStack.add(text);
    }

    public void addText(String text) { // adauga text in dialog
        textStack.add(text);
    }

    public String peekText() { // returneaza textul curent
        return indexText >= 0 ? textStack.get(indexText) : textStack.get(0);
    }


    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public int size() {
        return textStack.size();
    }
}
