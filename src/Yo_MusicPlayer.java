/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.H;
import static javafx.scene.input.KeyCode.X;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *@author Yohannes Tz
 * <b>
 * this is the main class
 *</b> 
 */
public class Yo_MusicPlayer extends Application {
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        root.setOnMousePressed((MouseEvent event) -> {
            Yo_MusicPlayer.this.xOffset = event.getSceneX();
            Yo_MusicPlayer.this.yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - Yo_MusicPlayer.this.xOffset);
            stage.setY(event.getScreenY() - Yo_MusicPlayer.this.yOffset);
        });
        scene.setOnKeyPressed(e ->{
            if(e.getCode() == H){
                if(stage.isIconified()){
                    stage.setAlwaysOnTop(true);
                }else{
                    stage.setIconified(true); 
                }
            }else if (e.getCode() == X){
                stage.close();
            }
        });
        scene.setFill((Paint)null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image(Yo_MusicPlayer.class.getResource("music.png").toString()));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
