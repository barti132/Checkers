package pl.barti;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Node{
    private int value;
    private final ArrayList<Node> children;

    private char[][] map;
    private final ArrayList<int[]> piecesToKill;
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

    public boolean isFinalNode(){
        return children.size() == 0;
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
                if(!n.isCaptureMove()){
                    delete.add(n);
                }

            }

            children.removeAll(delete);
        }
        this.captureChildren = captureChildren;
    }
}
