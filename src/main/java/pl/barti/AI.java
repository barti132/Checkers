package pl.barti;

import pl.barti.enums.PieceType;
import java.util.concurrent.atomic.AtomicBoolean;

public class AI{

    private int depth;

    public AI(){
        depth = 3;
    }

    public Piece move(Tile[][] board, AtomicBoolean playerMove){

        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        //random brute force
        for(int y = 0; y < Game.HEIGHT; y++){
            for(int x = 0; x < Game.WIDTH; x++){
                if(board[x][y].hasPiece() && board[x][y].getPiece().getType() == PieceType.RED){

                    //w góre
                    if(board[x][y].getPiece().isKing()){
                        if(x - 1 >= 0 && y - 1 >= 0){
                            if(board[x-1][y-1].hasPiece() && board[x-1][y-1].getPiece().getType() == PieceType.WHITE &&
                                    x - 2 >= 0 && y - 2 >= 0 && !board[x-2][y-2].hasPiece()){
                                Piece piece  = board[x-1][y-1].getPiece();
                                killOne(board, x, y, x-2, y-2, x-1, y-1);
                                playerMove.set(true);
                                return piece;
                            }
                            else if(!board[x-1][y-1].hasPiece()){
                                move(board, x, y, x - 1, y - 1);
                                playerMove.set(true);
                                return null;
                            }
                        }
                        //prawo
                        if(x + 1 < Game.WIDTH && y - 1 >= 0){
                            if(board[x+1][y-1].hasPiece() && board[x+1][y-1].getPiece().getType() == PieceType.WHITE &&
                                    x + 2 < Game.WIDTH && y - 2 >= 0 && !board[x+2][y-2].hasPiece()){
                                Piece piece  = board[x+1][y-1].getPiece();
                                killOne(board, x, y, x+2, y-2, x+1, y-1);
                                playerMove.set(true);
                                return piece;
                            }
                            else if(!board[x+1][y-1].hasPiece()){
                                move(board, x, y, x + 1, y - 1);
                                playerMove.set(true);
                                return null;
                            }
                        }
                    }

                    //w dół
                    //lewo
                    if(x - 1 >= 0 && y + 1 < Game.HEIGHT){
                        if(board[x-1][y+1].hasPiece() && board[x-1][y+1].getPiece().getType() == PieceType.WHITE &&
                                x - 2 >= 0 && y + 2 < Game.HEIGHT && !board[x-2][y+2].hasPiece()){
                            Piece piece  = board[x-1][y+1].getPiece();
                            killOne(board, x, y, x-2, y+2, x-1, y+1);
                            playerMove.set(true);
                            return piece;
                        }
                        else if(!board[x-1][y+1].hasPiece()){
                            move(board, x, y, x - 1, y + 1);
                            playerMove.set(true);
                            return null;
                        }
                    }
                    //prawo
                    if(x + 1 < Game.WIDTH && y + 1 < Game.HEIGHT){
                        if(board[x+1][y+1].hasPiece() && board[x+1][y+1].getPiece().getType() == PieceType.WHITE &&
                                x + 2 < Game.WIDTH && y + 2 < Game.HEIGHT && !board[x+2][y+2].hasPiece()){
                            Piece piece  = board[x+1][y+1].getPiece();
                            killOne(board, x, y, x+2, y+2, x+1, y+1);
                            playerMove.set(true);
                            return piece;
                        }
                        else if(!board[x+1][y+1].hasPiece()){
                            move(board, x, y, x + 1, y + 1);
                            playerMove.set(true);
                            return null;
                        }
                    }

                }
            }
        }
        playerMove.set(true);
        return null;
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
