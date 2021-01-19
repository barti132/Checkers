package pl.barti;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    private static Game game;
    @Override
    public void start(Stage stage){
        Scene scene = new Scene(game.createContent());
        stage.setTitle("Warcaby");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        game = new Game();
        launch(args);
    }
}
