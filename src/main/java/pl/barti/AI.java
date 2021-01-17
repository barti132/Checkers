package pl.barti;

import javafx.scene.media.AudioClip;
import pl.barti.enums.PieceType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AI{

    private int depth;
    private AudioClip mediaPlayer;

    public AI(AudioClip mediaPlayer){
        depth = 3;
        this.mediaPlayer = mediaPlayer;
    }

    private void nextTree(Node node, int depth, PieceType type){
        if(type == PieceType.RED)
            createTree(node, depth - 1, PieceType.WHITE);
        else
            createTree(node, depth - 1, PieceType.RED);
    }

    private void multiKill(Node node, int x, int y){
        int left, right, up, down;
        char[][] map = node.getMap();

        while(true){
            left = x - 1;
            right = x + 1;
            up = y - 1;
            down = y + 1;

            if(p.isKing() && left >= 0 && up >= 0 && checkLeftUp(board, x, y, PieceType.WHITE)){
                pieceList.add(board[left][up].getPiece());
                killOne(board, x, y, x - 2, y - 2, left, up);
            }
            else if(p.isKing() && right < Game.WIDTH && up >= 0 && checkRightUp(board, x, y, PieceType.WHITE)){
                pieceList.add(board[right][up].getPiece());
                killOne(board, x, y, x + 2, y - 2, right, up);
            }
            else if(left >= 0 && down < Game.HEIGHT && checkLeftDown(board, x, y, PieceType.WHITE)){
                pieceList.add(board[left][down].getPiece());
                killOne(board, x, y, x - 2, y + 2, left, down);
            }
            else if(right < Game.WIDTH && down < Game.HEIGHT && checkRightDown(board, x, y, PieceType.WHITE)){
                pieceList.add(board[right][down].getPiece());
                killOne(board, x, y, x + 2, y + 2, right, down);
            }
            else
                break;
        }

        node.setMap(map);
    }

    private void createTree(Node node, int depth, PieceType type){
        if(depth == 0)
            return;

        for(int y = 0; y < Game.HEIGHT; y++){
            for(int x = 0; x < Game.WIDTH; x++){
                char[][] originMap = node.getMap();
                if(     originMap[x][y] == ' ' ||
                        type == PieceType.RED && (originMap[x][y] == 'w' || originMap[x][y] == 'e') ||
                        type == PieceType.WHITE && (originMap[x][y] == 'r' || originMap[x][y] == 't'))
                    continue;

                int left, right, up, down;
                left = x - 1;
                right = x + 1;
                up = y - 1;
                down = y + 1;


                char[][] map = node.getMap();
                if((type == PieceType.WHITE || map[x][y] == 't') && left >= 0 && up >= 0 && map[left][up] != ' '){
                    if(type == PieceType.WHITE && (map[left][up] == 'r' || map[left][up] == 't')){
                        if(originMap[left][up]
                    }
                    board[left][up].hasPiece() && board[left][up].getPiece().getType() == type && x - 2 >= 0 && y - 2 >= 0 && !board[x - 2][y - 2].hasPiece())
                    pieceList.add(board[left][up].getPiece());
                    killOne(board, x, y, x - 2, y - 2, left, up);
                }


                if(p.isKing() && right < Game.WIDTH && up >= 0 && checkRightUp(board, x, y, PieceType.WHITE)){
                    pieceList.add(board[right][up].getPiece());
                    killOne(board, x, y, x + 2, y - 2, right, up);
                }

                if(left >= 0 && down < Game.HEIGHT && checkLeftDown(board, x, y, PieceType.WHITE)){
                    pieceList.add(board[left][down].getPiece());
                    killOne(board, x, y, x - 2, y + 2, left, down);
                }

                if(right < Game.WIDTH && down < Game.HEIGHT && checkRightDown(board, x, y, PieceType.WHITE)){
                    pieceList.add(board[right][down].getPiece());
                    killOne(board, x, y, x + 2, y + 2, right, down);
                }

                if((type == PieceType.WHITE || originMap[x][y] == 't') && left >= 0 && up >= 0 && node.getMap()[left][up] == ' '){
                    char[][]map = setMove(node.getMap(), x, y, left, up);

                    Node n = new Node();
                    n.setMap(map);
                    n.setValue(5 * type.moveDir);
                    node.getChildren().add(n);

                    nextTree(n, depth, type);
                }
                if((type == PieceType.WHITE || originMap[x][y] == 't') && right < Game.WIDTH && up >= 0 && node.getMap()[right][up] == ' '){
                    char[][]map = setMove(node.getMap(), x, y, right, up);

                    Node n = new Node();
                    n.setMap(map);
                    n.setValue(5 * type.moveDir);
                    node.getChildren().add(n);

                    nextTree(n, depth, type);
                }
                if((type == PieceType.RED || originMap[x][y] == 'e') && left >= 0 && down < Game.HEIGHT && node.getMap()[left][down] == ' '){
                    char[][]map = setMove(node.getMap(), x, y, left, down);

                    Node n = new Node();
                    n.setMap(map);
                    n.setValue(5 * type.moveDir);
                    node.getChildren().add(n);

                    nextTree(n, depth, type);
                }
                if((type == PieceType.RED || originMap[x][y] == 'e') && right < Game.WIDTH && down < Game.HEIGHT && node.getMap()[right][down] == ' '){
                    char[][]map = setMove(node.getMap(), x, y, right, down);

                    Node n = new Node();
                    n.setMap(map);
                    n.setValue(5 * type.moveDir);
                    node.getChildren().add(n);

                    nextTree(n, depth, type);
                }
            }
        }
    }

    private char[][] setMove(char[][] map, int x, int y, int nextX, int nextY){
        map[nextX][nextY] = map[x][y];
        map[x][y] = ' ';

        return map;
    }

    private char[][] convertMap(Tile[][] board){
        char[][] map = new char[Game.WIDTH][Game.HEIGHT];

        for(int j = 0; j < Game.WIDTH; j++){
            for(int i = 0; i < Game.HEIGHT; i++){
                if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.RED &&  board[i][j].getPiece().isKing())
                    map[i][j] = 't';//red king
                else if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.RED)
                    map[i][j] = 'r';//red piece
                else if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.WHITE &&  board[i][j].getPiece().isKing())
                    map[i][j] = 'e';//white king
                else if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.WHITE)
                    map[i][j] = 'w';//whihte piece
                else
                    map[i][j] = ' ';//empty
            }
        }

        return map;
    }

    public List<Piece> move(Tile[][] board, ArrayList<Piece> pieces, AtomicBoolean playerMove){

        char[][] map = convertMap(board);

        Node node = new Node();
        node.setMap(map);

        createTree(node, depth, PieceType.RED);

        System.out.println(node.getChildren().size());

        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        List<Piece> pieceList = new ArrayList<>();
        for(Piece p : pieces){
            if(p.getType() == PieceType.WHITE)
                continue;

            boolean flag = false;
            int x, y, left, right, up, down;

            while(true){
                x = p.getX();
                y = p.getY();
                left = x - 1;
                right = x + 1;
                up = y - 1;
                down = y + 1;

                if(p.isKing() && left >= 0 && up >= 0 && checkLeftUp(board, x, y, PieceType.WHITE)){
                    pieceList.add(board[left][up].getPiece());
                    killOne(board, x, y, x - 2, y - 2, left, up);
                }
                else if(p.isKing() && right < Game.WIDTH && up >= 0 && checkRightUp(board, x, y, PieceType.WHITE)){
                    pieceList.add(board[right][up].getPiece());
                    killOne(board, x, y, x + 2, y - 2, right, up);
                }
                else if(left >= 0 && down < Game.HEIGHT && checkLeftDown(board, x, y, PieceType.WHITE)){
                    pieceList.add(board[left][down].getPiece());
                    killOne(board, x, y, x - 2, y + 2, left, down);
                }
                else if(right < Game.WIDTH && down < Game.HEIGHT && checkRightDown(board, x, y, PieceType.WHITE)){
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

    private boolean checkLeftUp(Tile[][] board, int x, int y, PieceType type){
        int left = x - 1;
        int up = y - 1;
        if(board[left][up].hasPiece() && board[left][up].getPiece().getType() == type && x - 2 >= 0 && y - 2 >= 0 && !board[x - 2][y - 2].hasPiece())
            return true;
        return false;
    }

    private boolean checkRightUp(Tile[][] board, int x, int y, PieceType type){
        int up = y - 1;
        int right = x + 1;
        if(board[right][up].hasPiece() && board[right][up].getPiece().getType() == type && x + 2 < Game.WIDTH && y - 2 >= 0 && !board[x + 2][y - 2].hasPiece())
            return true;
        return false;
    }

    private boolean checkLeftDown(Tile[][] board, int x, int y, PieceType type){
        int left = x - 1;
        int down = y + 1;
        if(board[left][down].hasPiece() && board[left][down].getPiece().getType() == type && x - 2 >= 0 && y + 2 < Game.HEIGHT && !board[x - 2][y + 2].hasPiece())
            return true;
        return false;
    }

    private boolean checkRightDown(Tile[][] board, int x, int y, PieceType type){
        int right = x + 1;
        int down = y + 1;
        if(board[right][down].hasPiece() && board[right][down].getPiece().getType() == type && x + 2 < Game.WIDTH && y + 2 < Game.HEIGHT && !board[x + 2][y + 2].hasPiece())
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

    private int minimax(Node node, int depth, boolean maximizingPlayer){
        int value;
        if (depth == 0 || node.isFinalNode())
             return node.getValue();

        if(maximizingPlayer){
            value = Integer.MIN_VALUE;
            for(Node child : node.getChildren()){
                value = Math.max(value, minimax(child, depth - 1, false));
                return value;
            }
        }
        else{
            value = Integer.MAX_VALUE;
            for(Node child : node.getChildren()){
                value = Math.min(value, minimax(child, depth - 1, true));
                return value;
            }
        }
        return value;
     }
}

/*
    private void createTree(Node node, int depth, PieceType type){
        if(depth == 0)
            return;

        for(Piece p : node.getPieceList()){
            if(p.getType() != type)
                continue;

            int x, y, left, right, up, down;
            x = p.getX();
            y = p.getY();
            left = x - 1;
            right = x + 1;
            up = y - 1;
            down = y + 1;


            if((type == PieceType.WHITE || p.isKing()) && left >= 0 && up >= 0 && !node.getMap()[left][up].hasPiece()){
                Node n = new Node();
                node.getChildren().add(n);
                n.setPieceList(node.getPieceList());
                n.setPiece(p);
                n.setValue(5 * type.moveDir);
                n.setNextX(left);
                n.setNextY(up);
                node.getMap()[x][y].setPiece(null);
                node.getMap()[left][up].setPiece(p);
                n.setMap(node.getMap());

                if(type == PieceType.RED)
                    createTree(n, depth - 1, PieceType.WHITE);
                else
                    createTree(n, depth - 1, PieceType.RED);
            }
            if((type == PieceType.WHITE || p.isKing()) && right < Game.WIDTH && up >= 0 && !node.getMap()[right][up].hasPiece()){
                Node n = new Node();
                node.getChildren().add(n);
                n.setPiece(p);
                n.setPieceList(node.getPieceList());
                n.setValue(5 * type.moveDir);
                n.setNextX(right);
                n.setNextY(up);
                node.getMap()[x][y].setPiece(null);
                node.getMap()[right][up].setPiece(p);
                n.setMap(node.getMap());

                if(type == PieceType.RED)
                    createTree(n, depth - 1, PieceType.WHITE);
                else
                    createTree(n, depth - 1, PieceType.RED);
            }
            if((type == PieceType.RED || p.isKing()) && left >= 0 && down < Game.HEIGHT && !node.getMap()[left][down].hasPiece()){
                Node n = new Node();
                node.getChildren().add(n);
                n.setPiece(p);
                n.setPieceList(node.getPieceList());
                n.setValue(5 * type.moveDir);
                n.setNextX(left);
                n.setNextY(down);
                node.getMap()[x][y].setPiece(null);
                node.getMap()[left][down].setPiece(p);
                n.setMap(node.getMap());

                if(type == PieceType.RED)
                    createTree(n, depth - 1, PieceType.WHITE);
                else
                    createTree(n, depth - 1, PieceType.RED);
            }
            if((type == PieceType.RED || node.getPiece().isKing()) && right < Game.WIDTH && down < Game.HEIGHT && !node.getMap()[right][down].hasPiece()){
                Node n = new Node();
                node.getChildren().add(n);
                n.setPiece(p);
                n.setPieceList(node.getPieceList());
                n.setValue(5 * type.moveDir);
                n.setNextX(right);
                n.setNextY(down);
                node.getMap()[x][y].setPiece(null);
                node.getMap()[right][down].setPiece(p);
                n.setMap(node.getMap());

                if(type == PieceType.RED)
                    createTree(n, depth - 1, PieceType.WHITE);
                else
                    createTree(n, depth - 1, PieceType.RED);
            }
        }
    }
 */