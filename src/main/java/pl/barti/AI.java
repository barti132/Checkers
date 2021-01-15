package pl.barti;

import javafx.scene.media.AudioClip;
import pl.barti.enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AI{

    private int depth;
    private AudioClip mediaPlayer;

    public AI(AudioClip mediaPlayer){
        depth = 3;
        this.mediaPlayer = mediaPlayer;
    }

    public List<Piece> move(Tile[][] board, ArrayList<Piece> pieces, AtomicBoolean playerMove){

        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        //random brute force
        List<Piece> pieceList = new ArrayList<>();
        for(Piece p : pieces){
            if(p.getType() == PieceType.WHITE)
                continue;

            boolean flag = false;
            int x = p.getX();
            int y = p.getY();
            int left = x - 1;
            int right = x + 1;
            int up = y - 1;
            int down = y + 1;

            while(true){
                x = p.getX();
                y = p.getY();
                left = x - 1;
                right = x + 1;
                up = y - 1;
                down = y + 1;

                if(p.isKing() && left >= 0 && up >= 0 && checkLeftUp(board, x, y)){
                    pieceList.add(board[left][up].getPiece());
                    killOne(board, x, y, x - 2, y - 2, left, up);
                }
                else if(p.isKing() && right < Game.WIDTH && up >= 0 && checkRightUp(board, x, y)){
                    pieceList.add(board[right][up].getPiece());
                    killOne(board, x, y, x + 2, y - 2, right, up);
                }
                else if(left >= 0 && down < Game.HEIGHT && checkLeftDown(board, x, y)){
                    pieceList.add(board[left][down].getPiece());
                    killOne(board, x, y, x - 2, y + 2, left, down);
                }
                else if(right < Game.WIDTH && down < Game.HEIGHT && checkRightDown(board, x, y)){
                    pieceList.add(board[right][down].getPiece());
                    killOne(board, x, y, x + 2, y + 2, right, down);

                }
                else{
                    break;
                }
                flag = true;
            }

            if(flag)
                break;

            if(p.isKing() && left >= 0 && up >= 0 && !board[left][up].hasPiece()){
                move(board, x, y, left, up);
                break;
            }
            if(p.isKing() && right < Game.WIDTH && up >= 0 && !board[right][up].hasPiece()){
                move(board, x, y, right, up);
                break;
            }
            if(left >= 0 && down < Game.HEIGHT && !board[left][down].hasPiece()){
                move(board, x, y, left, down);
                break;
            }
            if(right < Game.WIDTH && down < Game.HEIGHT && !board[right][down].hasPiece()){
                move(board, x, y, right,down);
                break;
            }
        }

        playerMove.set(true);
        return pieceList;
    }

    private boolean checkLeftUp(Tile[][] board, int x, int y){
        int left = x - 1;
        int up = y - 1;
        if(board[left][up].hasPiece() && board[left][up].getPiece().getType() == PieceType.WHITE && x - 2 >= 0 && y - 2 >= 0 && !board[x - 2][y - 2].hasPiece())
            return true;
        return false;
    }

    private boolean checkRightUp(Tile[][] board, int x, int y){
        int up = y - 1;
        int right = x + 1;
        if(board[right][up].hasPiece() && board[right][up].getPiece().getType() == PieceType.WHITE && x + 2 < Game.WIDTH && y - 2 >= 0 && !board[x + 2][y - 2].hasPiece())
            return true;
        return false;
    }

    private boolean checkLeftDown(Tile[][] board, int x, int y){
        int left = x - 1;
        int down = y + 1;
        if(board[left][down].hasPiece() && board[left][down].getPiece().getType() == PieceType.WHITE && x - 2 >= 0 && y + 2 < Game.HEIGHT && !board[x - 2][y + 2].hasPiece())
            return true;
        return false;
    }

    private boolean checkRightDown(Tile[][] board, int x, int y){
        int right = x + 1;
        int down = y + 1;
        if(board[right][down].hasPiece() && board[right][down].getPiece().getType() == PieceType.WHITE && x + 2 < Game.WIDTH && y + 2 < Game.HEIGHT && !board[x + 2][y + 2].hasPiece())
            return true;
        return false;
    }

    private void killOne(Tile[][] board, int x, int y, int nextX, int nextY, int enemyX, int enemyY){
        board[enemyX][enemyY].setPiece(null);
        move(board, x, y, nextX, nextY);
    }

    private void move(Tile[][] board, int x, int y, int nextX, int nextY ){
        Piece piece = board[x][y].getPiece();
        piece.move(nextX, nextY);
        board[x][y].setPiece(null);
        board[nextX][nextY].setPiece(piece);
        mediaPlayer.play();
    }

    /*   private int minimax(Node node, int depth, boolean maximizingPlayer){
        int value;
         if (depth == 0 || node.isFinalNode())
             return node.getValue();
         if(maximizingPlayer){
             value = Integer.MIN_VALUE;
             for(each child of node){
                 value = Math.max(value, minimax(child, depth − 1, false));
                 return value;
             }
         }
         else{
             value = Integer.MAX_VALUE;
             for(child of node){
                 value = Math.min(value, minimax(child, depth − 1, true));
                 return value;
             }
         }
     }*/
}
