package pl.barti;

public class Node{
    private int value;
    private boolean finalNode;

    public Node(){
        value = 0;
        finalNode = false;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public boolean isFinalNode(){
        return finalNode;
    }

    public void setFinalNode(boolean finalNode){
        this.finalNode = finalNode;
    }
}
