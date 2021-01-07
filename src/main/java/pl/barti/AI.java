package pl.barti;

import pl.barti.enums.PieceType;

public class AI{

    private int depth;

    public AI(){
        depth = 3;
    }

    public Tile[][] move(Tile[][] board){
        //random brute force
        for(int y = 0; y < Game.HEIGHT; y++){
            for(int x = 0; x < Game.WIDTH; x++){
                if(board[x][y].hasPiece() && board[x][y].getPiece().getType() == PieceType.RED){
                    if(x - 1 >= 0 && y + 1 <= Game.HEIGHT){
                        if(board[x-1][y+1].hasPiece()){

                        }
                        else{
                            Piece piece = board[x][y].getPiece();
                            piece.move(x-1, y+1);
                            board[x][y].setPiece(null);
                            board[x-1][y+1].setPiece(piece);
                            return board;
                        }
                    }
                    else if(x + 1 <= Game.WIDTH && y + 1 <= Game.HEIGHT){
                        if(board[x+1][y+1].hasPiece()){

                        }
                        else{
                            Piece piece = board[x][y].getPiece();
                            piece.move(x+1, y+1);
                            board[x][y].setPiece(null);
                            board[x+1][y+1].setPiece(piece);
                            return board;
                        }
                    }
                }
            }
        }
        return board;
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
