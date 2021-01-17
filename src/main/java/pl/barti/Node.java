package pl.barti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node{
    private int value;
    private ArrayList<Node> children;

    private char[][] map;
    private ArrayList<int[]> piecesToKill;
    private Piece piece;
    private int nextX, nextY;

    public Node(){
        value = 0;
        children = new ArrayList<>();
        piecesToKill = new ArrayList<>();
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

    public Piece getPiece(){
        return piece;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public ArrayList<int[]> getPiecesToKill(){
        return piecesToKill;
    }

    public boolean isFinalNode(){
        if(children.size() == 0)
            return true;
        return false;
    }

}
