package pl.barti;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import pl.barti.boardelements.Piece;
import pl.barti.boardelements.Tile;
import pl.barti.enums.MoveType;
import pl.barti.enums.PieceType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game{
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private final Tile[][] board;
    private final Group pieceGroup;
    private final ArrayList<Piece> pieces;
    private final AI ai;
    private final AtomicBoolean playerMove;
    private final AudioClip mediaPlayer;

    Game(){
        mediaPlayer = new AudioClip(new Media(new File("src/main/resources/chessMove.wav").toURI().toString()).getSource());
        board = new Tile[WIDTH][HEIGHT];

        pieceGroup = new Group();
        ai = new AI(mediaPlayer);
        pieces = new ArrayList<>();
        playerMove = new AtomicBoolean(true);
    }

    public Parent createContent(){
        Pane pane = new Pane();
        Group tileGroup = new Group();
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
                    pieces.add(piece);
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

        if(Math.abs(
                newX - x0) == 1 && (newY - y0 == piece.getType().moveDir || piece.isKing() && newY - y0 == (piece.getType().moveDir * -1))){
            return new MoveResult(MoveType.NORMAL);
        }
        else if(Math.abs(
                newX - x0) == 2 && (newY - y0 == piece.getType().moveDir * 2 || piece.isKing() && newY - y0 == (piece.getType().moveDir * -2))){

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if(board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()){
                return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
            }
        }

        return new MoveResult(MoveType.NONE);
    }


    private Piece makePiece(PieceType type, int x, int y){

        Piece piece = new Piece(type, x, y);

        piece.setOnMouseReleased(e -> {
            if(playerMove.get()){
                boolean flag = false;
                int newX = toBoard(piece.getLayoutX());
                int newY = toBoard(piece.getLayoutY());

                MoveResult result;
                if(newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT){
                    result = new MoveResult(MoveType.NONE);
                }
                else{
                    result = tryMove(piece, newX, newY);
                }

                int x0 = toBoard(piece.getOldX());
                int y0 = toBoard(piece.getOldY());

                switch(result.getType()){
                    case NONE:
                        piece.abortMove();
                        break;

                    case KILL:
                        Piece otherPiece = result.getPiece();
                        board[toBoard(otherPiece.getLayoutX())][toBoard(otherPiece.getLayoutY())].setPiece(null);
                        update(List.of(otherPiece));

                        flag = true;

                    case NORMAL:
                        if(newY == 0 && !piece.isKing()){
                            piece.setKing(true);
                        }

                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);
                        board[newX][newY].setPiece(piece);

                        mediaPlayer.play();
                        if(!(flag && checkKillNext(piece))){
                            aiMove();
                        }

                        break;
                }
            }
            else{
                piece.abortMove();
            }
        });

        return piece;
    }

    private boolean checkKillNext(Piece p){
        int x = p.getX();
        int y = p.getY();
        int left = x - 1;
        int right = x + 1;
        int up = y - 1;
        int down = y + 1;

        if(left >= 0 && up >= 0 && board[left][up].hasPiece() && board[left][up].getPiece()
                .getType() == PieceType.RED && x - 2 >= 0 && y - 2 >= 0 && !board[x - 2][y - 2].hasPiece()){
            return true;
        }

        if(right < Game.WIDTH && up >= 0 && board[right][up].hasPiece() && board[right][up].getPiece()
                .getType() == PieceType.RED && x + 2 < Game.WIDTH && y - 2 >= 0 && !board[x + 2][y - 2].hasPiece()){
            return true;
        }

        if(p.isKing() && left >= 0 && down < Game.HEIGHT && board[left][down].hasPiece() && board[left][down].getPiece()
                .getType() == PieceType.RED && x - 2 >= 0 && y + 2 < Game.HEIGHT && !board[x - 2][y + 2].hasPiece()){
            return true;
        }

        return p.isKing() && right < Game.WIDTH && down < Game.HEIGHT && board[right][down].hasPiece() && board[right][down].getPiece()
                .getType() == PieceType.RED && x + 2 < Game.WIDTH && y + 2 < Game.HEIGHT && !board[x + 2][y + 2].hasPiece();

    }

    private void aiMove(){
        playerMove.set(false);
        Thread thread = new Thread(new Runnable(){
            List<Piece> pieceList;
            final Runnable updater = new Runnable(){
                @Override
                public void run(){
                    update(pieceList);
                }
            };

            @Override
            public void run(){
                pieceList = ai.move(board, playerMove);
                Platform.runLater(updater);
            }
        });
        thread.start();
    }

    private void update(List<Piece> pieceList){
        for(Piece p : pieceList){
            pieceGroup.getChildren().remove(p);
            pieces.remove(p);
        }

        for(int i = 0; i < Game.WIDTH; i += 2){
            if(board[i][7].hasPiece() && board[i][7].getPiece().getType() == PieceType.RED && !board[i][7].getPiece().isKing()){
                board[i][7].getPiece().setKing(true);
            }
        }
    }

    private int toBoard(double pixel){
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }
}
