package pl.barti;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import pl.barti.enums.PieceType;

public class Piece extends StackPane{

    private PieceType type;

    private boolean king;
    private double mouseX, mouseY;
    private double oldX, oldY;
    private int x, y;


    public Piece(PieceType type, int x, int y){
        this.type = type;
        king = false;
        move(x, y);

        relocate(x * Game.TILE_SIZE, y * Game.TILE_SIZE);

        Ellipse background = new Ellipse(Game.TILE_SIZE * .3125, Game.TILE_SIZE * 0.26);
        background.setFill(Color.BLACK);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(Game.TILE_SIZE * 0.03);
        background.setTranslateX((Game.TILE_SIZE - Game.TILE_SIZE * 0.3125 * 2) / 2);
        background.setTranslateY((Game.TILE_SIZE - Game.TILE_SIZE * 0.26 * 2) / 2 + Game.TILE_SIZE * 0.07);

        Ellipse ellipse = new Ellipse(Game.TILE_SIZE * .3125, Game.TILE_SIZE * 0.26);
        if(type == PieceType.RED)
            ellipse.setFill(Color.RED);
        else
            ellipse.setFill(Color.WHITE);

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(Game.TILE_SIZE * 0.03);
        ellipse.setTranslateX((Game.TILE_SIZE - Game.TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((Game.TILE_SIZE - Game.TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(background, ellipse);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e ->{
            if(type == PieceType.WHITE)
                relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y){
        this.x = x;
        this.y = y;
        oldX = x * Game.TILE_SIZE;
        oldY = y * Game.TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove(){
        relocate(oldX, oldY);
    }

    public void setKing(boolean king){
        if(!this.king){
            Text t = new Text();
            t.setText("K");
            t.setFont(Font.font ("Verdana", 20));
            t.setStrokeWidth(Game.TILE_SIZE * 0.03);
            t.setTranslateX((Game.TILE_SIZE - Game.TILE_SIZE * 0.3125 * 2) / 2);
            t.setTranslateY((Game.TILE_SIZE - Game.TILE_SIZE * 0.26 * 2) / 2);
            getChildren().add(t);
        }
        this.king = king;
    }

    public boolean isKing(){
        return king;
    }

    public PieceType getType(){
        return type;
    }

    public double getOldX(){
        return oldX;
    }

    public double getOldY(){
        return oldY;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
