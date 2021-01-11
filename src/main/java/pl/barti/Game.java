package pl.barti;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import pl.barti.enums.MoveType;
import pl.barti.enums.PieceType;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game{
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board;
    private Group tileGroup;
    private Group pieceGroup;
    private AI ai;
    private AtomicBoolean playerMove;
    private Pane pane;

    Game(){
        board = new Tile[WIDTH][HEIGHT];
        tileGroup = new Group();
        pieceGroup = new Group();
        ai = new AI();
        playerMove = new AtomicBoolean(true);
    }

    public Parent createContent(){
        pane = new Pane();
        pane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        pane.getChildren().addAll(tileGroup, pieceGroup);


        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Piece piece = null;
                if(y <= 2 && (x + y) % 2 != 0){
                    piece = makePiece(PieceType.RED, x, y);
                }
                if(y >= 5 && (x + y) % 2 != 0){
                    piece = makePiece(PieceType.WHITE, x, y);
                }

                if(piece != null){
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }
        }
        return pane;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY){
        if(board[newX][newY].hasPiece() || (newX + newY) % 2 == 0){
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        if(Math.abs(newX - x0) == 1 && (newY - y0 == piece.getType().moveDir || piece.isKing() && newY - y0 == (piece.getType().moveDir * -1))) {
            return new MoveResult(MoveType.NORMAL);
        }else if (Math.abs(newX - x0) == 2 && (newY - y0 == piece.getType().moveDir * 2 || piece.isKing() && newY - y0 == (piece.getType().moveDir * -2))) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType())
                return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
        }

        return new MoveResult(MoveType.NONE);
    }

    private Piece makePiece(PieceType type, int x, int y){
        Piece piece = new Piece(type, x, y);

        piece.setOnMouseReleased(e -> {
            if(playerMove.get()){
                int newX = toBoard(piece.getLayoutX());
                int newY = toBoard(piece.getLayoutY());

                MoveResult result;
                if(newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT)
                    result = new MoveResult(MoveType.NONE);
                else
                    result = tryMove(piece, newX, newY);

                int x0 = toBoard(piece.getOldX());
                int y0 = toBoard(piece.getOldY());

                switch(result.getType()){
                    case NONE:
                        piece.abortMove();
                        break;

                    case KILL:
                        Piece otherPiece = result.getPiece();
                        board[toBoard(otherPiece.getLayoutX())][toBoard(otherPiece.getLayoutY())].setPiece(null);
                        update(otherPiece);

                    case NORMAL:
                        if(newY == 0 && !piece.isKing())
                            piece.setKing(true);

                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);
                        board[newX][newY].setPiece(piece);

                        aiMove();
                        break;
                }
            }
            else{
                piece.abortMove();
            }
        });

        return piece;
    }

    private void aiMove(){
        playerMove.set(false);
        Thread thread = new Thread(new Runnable(){
            Piece piece;
            Runnable updater = new Runnable(){
                @Override
                public void run(){
                    update(piece);
                }
            };
            @Override
            public void run(){
                piece = ai.move(board, playerMove);
                Platform.runLater(updater);
            }
        });
        thread.start();
    }

    private void update(Piece piece){
        if(piece != null)
            pieceGroup.getChildren().remove(piece);

        for(int i = 0; i < Game.WIDTH; i+=2){
            if(board[i][7].hasPiece() && board[i][7].getPiece().getType() == PieceType.RED && !board[i][7].getPiece().isKing())
                board[i][7].getPiece().setKing(true);
        }
    }

    private int toBoard(double pixel){
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }
}
