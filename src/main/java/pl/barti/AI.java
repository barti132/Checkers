package pl.barti;

import javafx.scene.media.AudioClip;
import pl.barti.enums.PieceType;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class AI{

    private int depth;
    private AudioClip mediaPlayer;

    public AI(AudioClip mediaPlayer){
        depth = 3;
        this.mediaPlayer = mediaPlayer;
    }

    public Piece move(Tile[][] board, ArrayList<Piece> pieces, AtomicBoolean playerMove){

        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        //random brute force
        Piece piece = null;
        for(Piece p : pieces){
            if(p.getType() == PieceType.WHITE)
                continue;

            int x = p.getX();
            int y = p.getY();
            int left = x - 1;
            int right = x + 1;
            int up = y - 1;
            int down = y + 1;
            //w góre
            if(p.isKing()){
                if(left >= 0 && up >= 0){
                    if(board[left][up].hasPiece() && board[left][up].getPiece().getType() == PieceType.WHITE && x - 2 >= 0 && y - 2 >= 0 && !board[x - 2][y - 2].hasPiece()){
                        piece = board[left][up].getPiece();
                        killOne(board, x, y, x - 2, y - 2, left, up);
                        break;
                    }
                    else if(!board[left][up].hasPiece()){
                        move(board, x, y, left, up);
                        break;
                    }
                }
                if(right < Game.WIDTH && up >= 0){
                    if(board[right][up].hasPiece() && board[right][up].getPiece().getType() == PieceType.WHITE && x + 2 < Game.WIDTH && y - 2 >= 0 && !board[x + 2][y - 2].hasPiece()){
                        piece = board[right][up].getPiece();
                        killOne(board, x, y, x + 2, y - 2, right, up);
                        break;
                    }
                    else if(!board[right][up].hasPiece()){
                        move(board, x, y, right, up);
                        break;
                    }
                }
            }
            //w dół
            //lewo
            if(left >= 0 && down < Game.HEIGHT){
                if(board[left][down].hasPiece() && board[left][down].getPiece().getType() == PieceType.WHITE && x - 2 >= 0 && y + 2 < Game.HEIGHT && !board[x - 2][y + 2].hasPiece()){
                    piece = board[left][down].getPiece();
                    killOne(board, x, y, x - 2, y + 2, left, down);
                    break;
                }
                else if(!board[left][down].hasPiece()){
                    move(board, x, y, left, down);
                    break;
                }
            }
            //prawo
            if(right < Game.WIDTH && down < Game.HEIGHT){
                if(board[right][down].hasPiece() && board[right][down].getPiece().getType() == PieceType.WHITE && x + 2 < Game.WIDTH && y + 2 < Game.HEIGHT && !board[x + 2][y + 2].hasPiece()){
                    piece = board[right][down].getPiece();
                    killOne(board, x, y, x + 2, y + 2, right, down);
                    break;
                }
                else if(!board[right][down].hasPiece()){
                    move(board, x, y, right,down);
                    break;
                }
            }
        }

        playerMove.set(true);
        return piece;
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
