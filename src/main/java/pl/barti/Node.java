package pl.barti;

import java.util.ArrayList;
import java.util.Arrays;

public class Node{
    private int value;
    private ArrayList<Node> children;

    private char[][] map;
    private ArrayList<int[]> piecesToKill;
    private int nextX, nextY;
    private int x, y;
    private boolean captureMove;
    private boolean captureChildren;

    public Node(){
        value = 0;
        children = new ArrayList<>();
        piecesToKill = new ArrayList<>();
        captureMove = false;
        captureChildren = false;
    }

    public int getValue(){
        return value;
    }

    public ArrayList<Node> getChildren(){
        return children;
    }

    public char[][] getMap(){
        return Arrays.stream(map).map(char[]::clone).toArray(char[][]::new);
    }

    public void setMap(char[][] map){
        this.map = map;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getNextX(){
        return nextX;
    }

    public void setNextX(int nextX){
        this.nextX = nextX;
    }

    public int getNextY(){
        return nextY;
    }

    public void setNextY(int nextY){
        this.nextY = nextY;
    }

    public ArrayList<int[]> getPiecesToKill(){
        return piecesToKill;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }

    public boolean isFinalNode(){
        if(children.size() == 0)
            return true;
        return false;
    }

    public boolean isCaptureMove(){
        return captureMove;
    }

    public void setCaptureMove(boolean captureMove){
        this.captureMove = captureMove;
    }

    public boolean hasCaptureChildren(){
        return captureChildren;
    }

    public void setCaptureChildren(boolean captureChildren){
        if(!this.captureChildren){
            ArrayList<Node> delete = new ArrayList<>();
            for(Node n : children){
                if(!n.isCaptureMove())
                    delete.add(n);

            }

            children.removeAll(delete);
        }
        this.captureChildren = captureChildren;
    }
}
