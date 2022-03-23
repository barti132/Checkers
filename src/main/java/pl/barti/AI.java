package pl.barti;

import javafx.scene.media.AudioClip;
import pl.barti.boardelements.Piece;
import pl.barti.boardelements.Tile;
import pl.barti.enums.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class AI{

    private final int depth;
    private final AudioClip mediaPlayer;
    private final int moveValue;
    private final int captureValue;
    private final int promValue;
    private final int kingMoveValue;

    public AI(AudioClip mediaPlayer){
        depth = 7;
        this.mediaPlayer = mediaPlayer;
        moveValue = 5;
        kingMoveValue = 10;
        captureValue = 100;
        promValue = 50;
    }

    private void nextNode(Node node, int depth, PieceType type){
        if(type == PieceType.RED){
            createTree(node, depth - 1, PieceType.WHITE);
        }
        else{
            createTree(node, depth - 1, PieceType.RED);
        }
    }

    private void multiCapture(Node n, PieceType type){
        int x, y, left, right, up, down;
        char[][] map = n.getMap();

        while(true){
            x = n.getNextX();
            y = n.getNextY();
            left = n.getNextX() - 1;
            right = n.getNextX() + 1;
            up = n.getNextY() - 1;
            down = n.getNextY() + 1;

            if(((type == PieceType.WHITE || map[x][y] == 't') && left >= 0 && up >= 0 && map[left][up] != ' ')
                    && x - 2 >= 0 && y - 2 >= 0 &&
                    ((type == PieceType.WHITE && (map[left][up] == 'r' || map[left][up] == 't') && map[x - 2][y - 2] == ' ')
                            || (type == PieceType.RED && (map[left][up] == 'w' || map[left][up] == 'e') && map[x - 2][y - 2] == ' '))){
                char p = map[x][y];
                int[] toKill = {left, up};
                map[left][up] = ' ';
                map[x - 2][y - 2] = p;

                n.setValue(n.getValue() + 10 * type.moveDir);
                n.setNextX(x - 2);
                n.setNextY(y - 2);
                n.getPiecesToKill().add(toKill);
            }
            else if(((type == PieceType.WHITE || map[x][y] == 't') && right < Game.WIDTH && up >= 0 && map[right][up] != ' ') && x + 2 < Game.WIDTH && y - 2 > 0 && ((type == PieceType.WHITE && (map[right][up] == 'r' || map[right][up] == 't') && map[x + 2][y - 2] == ' ') || (type == PieceType.RED && (map[right][up] == 'w' || map[right][up] == 'e') && map[x + 2][y - 2] == ' '))){
                char p = map[x][y];
                int[] toKill = {right, up};
                map[right][up] = ' ';
                map[x + 2][y - 2] = p;

                n.setValue(n.getValue() + 10 * type.moveDir);
                n.setNextX(x + 2);
                n.setNextY(y - 2);
                n.getPiecesToKill().add(toKill);
            }
            else if(((type == PieceType.RED || map[x][y] == 'e') && left >= 0 && down < Game.HEIGHT && map[left][down] != ' ') && x - 2 >= 0 && y + 2 < Game.HEIGHT && ((type == PieceType.WHITE && (map[left][down] == 'r' || map[left][down] == 't') && map[x - 2][y + 2] == ' ') || (type == PieceType.RED && (map[left][down] == 'w' || map[left][down] == 'e') && map[x - 2][y + 2] == ' '))){
                char p = map[x][y];
                int[] toKill = {left, down};
                map[left][down] = ' ';
                map[x - 2][y + 2] = p;

                n.setValue(n.getValue() + 10 * type.moveDir);
                n.setNextX(x - 2);
                n.setNextY(y + 2);
                n.getPiecesToKill().add(toKill);
            }
            else if(((type == PieceType.RED || map[x][y] == 'e') && right < Game.WIDTH && down < Game.HEIGHT && map[right][down] != ' ') && x + 2 < Game.WIDTH && y + 2 < Game.HEIGHT && ((type == PieceType.WHITE && (map[right][down] == 'r' || map[right][down] == 't') && map[x + 2][y + 2] == ' ') || (type == PieceType.RED && (map[right][down] == 'w' || map[right][down] == 'e') && map[x + 2][y + 2] == ' '))){
                char p = map[x][y];
                int[] toKill = {right, down};
                map[right][down] = ' ';
                map[x + 2][y + 2] = p;

                n.setValue(n.getValue() + 10 * type.moveDir);
                n.setNextX(x + 2);
                n.setNextY(y + 2);
                n.getPiecesToKill().add(toKill);
            }
            else{
                break;
            }
        }

        n.setMap(map);
    }

    private void isPromo(Node n, PieceType type){
        if((type == PieceType.RED && n.getY() == Game.HEIGHT - 1 && n.getMap()[n.getX()][n.getY()] != 't') || (type == PieceType.WHITE && n.getY() == 0 && n.getMap()[n.getX()][n.getY()] != 'e')){
            n.setValue(n.getValue() + promValue * type.moveDir);
            char[][] map = n.getMap();
            if(type == PieceType.RED){
                map[n.getX()][n.getY()] = 't';
            }
            else{
                map[n.getX()][n.getY()] = 'e';
            }
            n.setMap(map);
        }
    }

    private Node createNode(int x, int y, char[][] map, int value, int nextX, int nextY){
        Node n = new Node();
        n.setX(x);
        n.setY(y);
        n.setMap(map);
        n.setValue(value);
        n.setNextX(nextX);
        n.setNextY(nextY);
        return n;
    }

    private void createTree(Node node, int depth, PieceType type){
        if(depth == 0){
            return;
        }

        for(int y = 0; y < Game.HEIGHT; y++){
            for(int x = y % 2 == 0 ? 1 : 0; x < Game.WIDTH; x += 2){
                char[][] originMap = node.getMap();
                if(originMap[x][y] == ' ' || type == PieceType.RED && (originMap[x][y] == 'w' || originMap[x][y] == 'e') || type == PieceType.WHITE && (originMap[x][y] == 'r' || originMap[x][y] == 't')){
                    continue;
                }

                int left, right, up, down;
                left = x - 1;
                right = x + 1;
                up = y - 1;
                down = y + 1;

                //capture
                if(((type == PieceType.WHITE || originMap[x][y] == 't') && left >= 0 && up >= 0 && originMap[left][up] != ' ') && x - 2 >= 0 && y - 2 >= 0 && ((type == PieceType.WHITE && (originMap[left][up] == 'r' || originMap[left][up] == 't') && originMap[x - 2][y - 2] == ' ') || (type == PieceType.RED && (originMap[left][up] == 'w' || originMap[left][up] == 'e') && originMap[x - 2][y - 2] == ' '))){

                    char[][] map = node.getMap();
                    char p = map[x][y];
                    int[] toKill = {left, up};
                    map[left][up] = ' ';
                    map[x - 2][y - 2] = p;

                    Node n = createNode(x, y, map, captureValue * type.moveDir, x - 2, y - 2);
                    n.setCaptureMove(true);
                    n.getPiecesToKill().add(toKill);
                    multiCapture(n, type);
                    isPromo(n, type);

                    node.setCaptureChildren(true);
                    node.getChildren().add(n);
                    nextNode(n, depth, type);
                }
                if(((type == PieceType.WHITE || originMap[x][y] == 't') && right < Game.WIDTH && up >= 0 && originMap[right][up] != ' ') && x + 2 < Game.WIDTH && y - 2 > 0 && ((type == PieceType.WHITE && (originMap[right][up] == 'r' || originMap[right][up] == 't') && originMap[x + 2][y - 2] == ' ') || (type == PieceType.RED && (originMap[right][up] == 'w' || originMap[right][up] == 'e') && originMap[x + 2][y - 2] == ' '))){
                    char[][] map = node.getMap();
                    char p = map[x][y];
                    int[] toKill = {right, up};
                    map[right][up] = ' ';
                    map[x + 2][y - 2] = p;

                    Node n = createNode(x, y, map, captureValue * type.moveDir, x + 2, y - 2);
                    n.getPiecesToKill().add(toKill);
                    n.setCaptureMove(true);
                    multiCapture(n, type);
                    isPromo(n, type);

                    node.setCaptureChildren(true);
                    node.getChildren().add(n);
                    nextNode(n, depth, type);
                }
                if(((type == PieceType.RED || originMap[x][y] == 'e') && left >= 0 && down < Game.HEIGHT && originMap[left][down] != ' ') && x - 2 >= 0 && y + 2 < Game.HEIGHT && ((type == PieceType.WHITE && (originMap[left][down] == 'r' || originMap[left][down] == 't') && originMap[x - 2][y + 2] == ' ') || (type == PieceType.RED && (originMap[left][down] == 'w' || originMap[left][down] == 'e') && originMap[x - 2][y + 2] == ' '))){
                    char[][] map = node.getMap();
                    char p = map[x][y];
                    int[] toKill = {left, down};
                    map[left][down] = ' ';
                    map[x - 2][y + 2] = p;

                    Node n = createNode(x, y, map, captureValue * type.moveDir, x - 2, y + 2);
                    n.getPiecesToKill().add(toKill);
                    n.setCaptureMove(true);
                    multiCapture(n, type);
                    isPromo(n, type);

                    node.setCaptureChildren(true);
                    node.getChildren().add(n);
                    nextNode(n, depth, type);
                }
                if(((type == PieceType.RED || originMap[x][y] == 'e') && right < Game.WIDTH && down < Game.HEIGHT && originMap[right][down] != ' ') && x + 2 < Game.WIDTH && y + 2 < Game.HEIGHT && ((type == PieceType.WHITE && (originMap[right][down] == 'r' || originMap[right][down] == 't') && originMap[x + 2][y + 2] == ' ') || (type == PieceType.RED && (originMap[right][down] == 'w' || originMap[right][down] == 'e') && originMap[x + 2][y + 2] == ' '))){
                    char[][] map = node.getMap();
                    char p = map[x][y];
                    int[] toKill = {right, down};
                    map[right][down] = ' ';
                    map[x + 2][y + 2] = p;

                    Node n = createNode(x, y, map, captureValue * type.moveDir, x + 2, y + 2);
                    n.getPiecesToKill().add(toKill);
                    n.setCaptureMove(true);
                    multiCapture(n, type);
                    isPromo(n, type);

                    node.setCaptureChildren(true);
                    node.getChildren().add(n);
                    nextNode(n, depth, type);
                }

                if(node.hasCaptureChildren()){
                    continue;
                }

                //move
                if((type == PieceType.WHITE || originMap[x][y] == 't') && left >= 0 && up >= 0 && node.getMap()[left][up] == ' '){
                    char[][] map = setMove(node.getMap(), x, y, left, up);

                    int val = moveValue * type.moveDir;
                    if(originMap[x][y] == 't' || originMap[x][y] == 'e'){
                        val = kingMoveValue * type.moveDir;
                    }
                    Node n = createNode(x, y, map, val, left, up);
                    isPromo(n, type);

                    node.getChildren().add(n);
                    nextNode(n, depth, type);
                }
                if((type == PieceType.WHITE || originMap[x][y] == 't') && right < Game.WIDTH && up >= 0 && node.getMap()[right][up] == ' '){
                    char[][] map = setMove(node.getMap(), x, y, right, up);

                    int val = moveValue * type.moveDir;
                    if(originMap[x][y] == 't' || originMap[x][y] == 'e'){
                        val = kingMoveValue * type.moveDir;
                    }
                    Node n = createNode(x, y, map, val, right, up);
                    isPromo(n, type);

                    node.getChildren().add(n);
                    nextNode(n, depth, type);
                }
                if((type == PieceType.RED || originMap[x][y] == 'e') && left >= 0 && down < Game.HEIGHT && node.getMap()[left][down] == ' '){
                    char[][] map = setMove(node.getMap(), x, y, left, down);

                    int val = moveValue * type.moveDir;
                    if(originMap[x][y] == 't' || originMap[x][y] == 'e'){
                        val = kingMoveValue * type.moveDir;
                    }
                    Node n = createNode(x, y, map, val, left, down);
                    isPromo(n, type);

                    node.getChildren().add(n);
                    nextNode(n, depth, type);
                }
                if((type == PieceType.RED || originMap[x][y] == 'e') && right < Game.WIDTH && down < Game.HEIGHT && node.getMap()[right][down] == ' '){
                    char[][] map = setMove(node.getMap(), x, y, right, down);

                    int val = moveValue * type.moveDir;
                    if(originMap[x][y] == 't' || originMap[x][y] == 'e'){
                        val = kingMoveValue * type.moveDir;
                    }
                    Node n = createNode(x, y, map, val, right, down);
                    isPromo(n, type);

                    node.getChildren().add(n);
                    nextNode(n, depth, type);
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
                if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.RED && board[i][j].getPiece().isKing()){
                    map[i][j] = 't';//red king
                }
                else if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.RED){
                    map[i][j] = 'r';//red piece
                }
                else if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.WHITE && board[i][j].getPiece()
                        .isKing()){
                    map[i][j] = 'e';//white king
                }
                else if(board[i][j].hasPiece() && board[i][j].getPiece().getType() == PieceType.WHITE){
                    map[i][j] = 'w';//whihte piece
                }
                else{
                    map[i][j] = ' ';//empty
                }
            }
        }

        return map;
    }

    private static int number = 0;

    private void count(Node node){
        if(node.getChildren().size() == 0){
            return;
        }

        number++;
        for(int i = 0; i < node.getChildren().size(); i++){
            count(node.getChildren().get(i));
        }
    }

    public List<Piece> move(Tile[][] board, AtomicBoolean playerMove){

        char[][] map = convertMap(board);

        long start = System.currentTimeMillis();

        Node node = new Node();
        node.setMap(map);
        createTree(node, depth, PieceType.RED);

        long finish = System.currentTimeMillis();
        System.out.println(finish - start);

        number = 0;
        count(node);
        System.out.println(number);

        // printChildrenOfNode(node);

        Node nextMove = null;
        int maxValue = 0;
        for(Node child : node.getChildren()){
            int val = minimax(child, depth, true);
            if(maxValue < val){
                maxValue = val;
                nextMove = child;
            }
        }

        ArrayList<Piece> toKill = new ArrayList<>();
        if(nextMove != null){
            move(board, nextMove.getX(), nextMove.getY(), nextMove.getNextX(), nextMove.getNextY());

            for(int i = 0; i < nextMove.getPiecesToKill().size(); i++){
                toKill.add(board[nextMove.getPiecesToKill().get(i)[0]][nextMove.getPiecesToKill().get(i)[1]].getPiece());
                board[nextMove.getPiecesToKill().get(i)[0]][nextMove.getPiecesToKill().get(i)[1]].setPiece(null);
            }
        }

        playerMove.set(true);
        return toKill;
    }

    private void move(Tile[][] board, int x, int y, int nextX, int nextY){
        Piece piece = board[x][y].getPiece();
        piece.move(nextX, nextY);
        board[x][y].setPiece(null);
        board[nextX][nextY].setPiece(piece);
        mediaPlayer.play();
    }

    private int minimax(Node node, int depth, boolean maximizingPlayer){
        int value;
        if(depth == 0 || node.isFinalNode()){
            return node.getValue();
        }

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
