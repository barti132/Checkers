package pl.barti;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends Rectangle{

    private Piece piece;

    public Tile(boolean color, int x, int y){
        setWidth(Game.TILE_SIZE);
        setHeight(Game.TILE_SIZE);

        relocate(x * Game.TILE_SIZE, y * Game.TILE_SIZE);
        if(color){
            setFill(Color.LIGHTGRAY);
        }
        else{
            setFill(Color.valueOf("#A8AAAC"));
        }

    }

    public Piece getPiece(){
        return piece;
    }
    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public boolean hasPiece(){
        if(piece == null)
            return false;
        return true;
    }
}
