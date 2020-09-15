/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Yohannes Tz
 * May-12-2019E.C
 */
public class FXMLDocumentController implements Initializable{
    private final Image PlayButtonImage1;
    private final Image PauseButtonImage1;

    //------------------------------FXML Nodes-----------------------------------
    @FXML
    private AnchorPane mainpane;

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane manepane;

    @FXML
    private BarChart<String, Number> visualizer;

    @FXML
    private AnchorPane yellow;

    @FXML
    private Button close1;

    @FXML
    private JFXToggleButton toggle;

    @FXML
    private JFXToggleButton butn;

    @FXML
    private Label playTime;

    @FXML
    private Button play;

    @FXML
    private Label album;

    @FXML
    private ImageView songbage;

    @FXML
    private Label artist;

    @FXML
    private Label title;

    @FXML
    private Button stop1;

    @FXML
    private AnchorPane browse;

    @FXML
    private Label time;

    @FXML
    private JFXSlider musicSlider;

    @FXML
    private JFXSlider volumer;
    
    @FXML
    private NumberAxis NumberAxis;
    
    @FXML
    private CategoryAxis catagoryAxis;
    
    @FXML
    private JFXToggleButton auto_play;
    
    @FXML
    private Button previous;
    @FXML
    private Button music_fol_chsr;
    @FXML
    private AnchorPane highgraphics;
    @FXML
    private ListView<String> music_listviw;
    
    @FXML
    private Button next;
    
    //------------------Global Variables ------------------------------
    private Stage stage;
    private File file;
    public String url;
    private MediaPlayer music = null;
    public Label current;
    private String title1;
    private Duration duration;
    private String filename;
    private Double value;
    private boolean atEndOfMedia = false;
    private boolean auto_play_mode = false;
    private final Image PlayButtonImage = new Image(FXMLDocumentController.class.getResourceAsStream("Play.png"));
    private final Image PauseButtonImage = new Image(FXMLDocumentController.class.getResourceAsStream("Pause.png"));
    ImageView imagePlay;
    ImageView imagePause;
    ImageView imagePlay1;
    ImageView imagePause1;
    private final Image albumcover;
    private final Image notifyimage;
    private XYChart.Data<String, Number>[] series1Data;
    private AudioSpectrumListener audioSpectrumListener;
    private final boolean PLAY_AUDIO;
    private Media media;
    private File dataFolder;
    private File startPath;
    private File[] media_files_list;
    private List<MediaPlayer> play_list;
    private RotateTransition rotation_trans;
    private Duration rotation_duration;
   
    
    
    public FXMLDocumentController() {
        this.imagePlay = new ImageView(this.PlayButtonImage);
        this.imagePause = new ImageView(this.PauseButtonImage);
        this.PlayButtonImage1 = new Image(FXMLDocumentController.class.getResourceAsStream("Play.png"));
        this.PauseButtonImage1 = new Image(FXMLDocumentController.class.getResourceAsStream("Pause.png"));
        this.imagePlay1 = new ImageView(this.PlayButtonImage1);
        this.imagePause1 = new ImageView(this.PauseButtonImage1);
        this.albumcover = new Image(FXMLDocumentController.class.getResourceAsStream("album.png"));
        this.notifyimage = new Image(FXMLDocumentController.class.getResourceAsStream("music.png"));
        this.PLAY_AUDIO = Boolean.parseBoolean(System.getProperty("demo.play.audio", "true"));
        this.play_list = new ArrayList<MediaPlayer>();
    }
    
    public void init(Stage stage) {
        this.stage = stage;
    }
    
   

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainpane.setStyle("-fx-background-color: transparent;");
        
        visualizer.getStylesheets().add(FXMLDocumentController.class.getResource("chart.css").toExternalForm());
        NumberAxis.setOpacity(0);
        catagoryAxis.setOpacity(0);
        songbage.setImage(albumcover);
        //music_fol_chsr.setVisible(false);//due to beta
        music_fol_chsr.setOnAction(e ->{
            handle_musicfloder();
        });
        NumberAxis.setTickMarkVisible(false);
        catagoryAxis.setTickMarkVisible(false);
        handleVisualise();
        visualizer.setCategoryGap(0.2);
        visualizer.setBarGap(0);
        
               
        this.volumer.setValue(25.0D);
        this.songbage.setImage(this.albumcover);
        this.volumer.setTooltip(new Tooltip("Drag and Slide to Increase The volume."));
        this.musicSlider.setValue(0);
        this.musicSlider.setTooltip(new Tooltip("Drag and Slide to Seek Media"));
        this.value = Double.valueOf(this.musicSlider.getValue());
        this.play.setGraphic(this.imagePlay);
        this.volumer.valueProperty().addListener((ov) -> {
        });
        this.volumer.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(this.volumer.isValueChanging()) {
                this.music.setVolume(this.volumer.getValue() / 100.0D);
            }

        });
        
        manepane.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.CONTROL.P){
                handleplay();
            }else if(e.getCode() == KeyCode.CONTROL.S){
                handleStop();
            }else if(e.getCode() == KeyCode.CONTROL.O){
                if(browse.isVisible()){
                    try {
                        handlebrowsebutton();
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    System.out.println("Cannot initialize file chooser!");
                }
            }
        });
        
        //-----------------bar chart -------------------------------
        XYChart.Series<String, Number> series1 = new XYChart.Series();
        series1.setName((String)null);
        this.series1Data = new XYChart.Data[127];
        String[] categories = new String[127];

        for(int i = 0; i < this.series1Data.length; ++i) {
            categories[i] = Integer.toString(i + 1);
            this.series1Data[i] = new XYChart.Data(categories[i], Integer.valueOf(0));
            series1.getData().add(this.series1Data[i]);
        }

        visualizer.getData().add(series1);
        
        //------------------------Visualizer-------------------------------------- 
        this.audioSpectrumListener = (timestamp, duration, magnitudes, phases) -> {
            for(int i = 0; i < this.series1Data.length; ++i) {
                this.series1Data[i].setYValue(Float.valueOf(magnitudes[i] + 60.0F));
            }

        }; 
        
        auto_play.selectedProperty().addListener((observable, oldValue, newValue) ->{ 
           if(auto_play.isSelected()){
              auto_play_mode = true;
              System.out.println("Auto play turned on!");
           }else{
              auto_play_mode = false;
              System.out.println("Auto play turned off!");
           }
        });
                
        

    }
    //Storing the music on Array
    private void store_music(File directory)   {
        media_files_list = directory.listFiles(new FilenameFilter() {
             @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mp3");//all files that ends with .png is added to the Array 
            }
        });
    }
 
    
    public void handlebrowsebutton() throws IOException,IllegalArgumentException{
        FileChooser filechooser = new FileChooser();
        dataFolder = new File(System.getenv("APPDATA"), "yohannes.yo");
        if(dataFolder.exists()) {
            try {
                System.out.println("the data file exits.");
                FileInputStream fileInputStream = new FileInputStream(dataFolder);
                byte[] data = new byte[fileInputStream.available()];
                fileInputStream.read(data);
                String path = new String(data);
                File pathFolder = new File(path);
                if(pathFolder.exists() && pathFolder.isDirectory()) {
                    startPath = pathFolder;
                }

                fileInputStream.close();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }else{
            System.out.println("the data file do not exist");
        }
        filechooser.setInitialDirectory(startPath);
        filechooser.setTitle("Open Music to play");
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("Music Files", new String[]{"*mp3", "*m4a", "*mp4"})});
        this.file = filechooser.showOpenDialog(this.stage);
        Alert memusic;
        if(this.file != null) {
            memusic = null;
            saveDir();
            this.filename = this.file.getName();
            this.album.setText((String)null);
            this.artist.setText((String)null);
            this.title.setText((String)null);
            this.playTime.setText((String)null);
            this.songbage.setImage(this.albumcover);
            this.url = "file:///" + this.file.getAbsolutePath().replace("\\", "/").replaceAll(" ", "%20");
                        
            try{
                media = new Media(url.replaceAll(url, filename));
                this.rotation_duration = media.getDuration();
            }catch(Exception ex){
                
                ex.printStackTrace();
                memusic = new Alert(Alert.AlertType.ERROR);
                memusic.setTitle("Illegal character");
                memusic.setHeaderText((String)null);
                memusic.setContentText("erorr: the Item selected contains Illegal character try renaming it!");
                memusic.showAndWait();
                
            }
            if(media == null){
                this.music = new MediaPlayer(media);
            }else{
                System.out.println("Media is null");
            }
            this.musicSlider.setMinWidth(50.0D);
            this.music.setOnReady(new Runnable() {
                @Override
                public void run() {
                    FXMLDocumentController.this.duration = FXMLDocumentController.this.music.getMedia().getDuration();
                    FXMLDocumentController.this.updateValues();
                    handleplay();
                }
            });
            music.statusProperty().addListener((observable, oldValue, newValue) ->{
                System.out.println("State changed! to " + music.getStatus().toString());
                if(music.getStatus() == Status.PLAYING){
                 this.browse.setVisible(false);
                } 
            });
            this.music.setOnEndOfMedia(() -> {
                if(auto_play_mode){
                    Duration rewind = new Duration(0.00);
                    music.seek(rewind);
                    time.setText(null);
                }else{
                    System.out.print("no replay applied returning to default state!");
                    FXMLDocumentController.this.browse.setVisible(true);
                    FXMLDocumentController.this.play.setGraphic(this.imagePlay);
                    FXMLDocumentController.this.play.setText("Play");
                }
               });
           // this.musicSlider.setMaxWidth(1.7976931348623157E308D);
            this.music.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                this.updateValues();
            });
            this.playTime.setText(this.filename);
            media.getMetadata().addListener(new MapChangeListener<String, Object>() {
                public void onChanged(Change<? extends String, ? extends Object> ch) {
                    if(ch.wasAdded()) {
                        this.handleMetadata((String)ch.getKey(), ch.getValueAdded());
                    }

                }

                private void handleMetadata(String key, Object value) {
                    if(key.equals("album")) {
                        FXMLDocumentController.this.album.setText(value.toString());
                    } else if(key.equals("artist")) {
                        FXMLDocumentController.this.artist.setText(value.toString());
                    }

                    if(key.equals("title")) {
                        FXMLDocumentController.this.title.setText(value.toString());
                    }else{
                        FXMLDocumentController.this.title.setText(value.toString());
                    }

                    if(key.equals("year")) {
                       // FXMLDocumentController.this.year.setText(value.toString());
                    }

                    if(key.equals("image")) {
                        FXMLDocumentController.this.songbage.setImage((Image)value);
                    }
                }
            });
        } else {
            memusic = new Alert(Alert.AlertType.ERROR);
            memusic.setTitle("No Media Found");
            memusic.setHeaderText((String)null);
            memusic.setContentText("You have Selected nothing or you have cancelled the Operation");
            memusic.showAndWait();
        }
        
        

    }
    
      
    public void play() {
        this.startAudio();
    }

    public void stop() {
        this.stopAudio();
    }

    private void startAudio() {
        if(this.PLAY_AUDIO) {
            this.getAudioMediaPlayer().setAudioSpectrumListener(this.audioSpectrumListener);
            this.getAudioMediaPlayer().play();
        }

    }

    private void stopAudio() {
        if(this.getAudioMediaPlayer().getAudioSpectrumListener() == this.audioSpectrumListener) {
            this.getAudioMediaPlayer().pause();
        }

    }
    
    private MediaPlayer getAudioMediaPlayer() {
        if(this.music == null) {
            this.music = new MediaPlayer(new Media(this.url));
        }

        return this.music;
    }

    protected void updateValues() {
        if(this.time != null && this.musicSlider != null && this.volumer != null && this.duration != null) {
            Platform.runLater(() -> {
                Duration currentTime = this.music.getCurrentTime();
                this.time.setText(this.formatTime(currentTime, this.duration));
                this.musicSlider.setDisable(this.duration.isUnknown());
                if(!this.musicSlider.isDisabled() && this.duration.greaterThan(Duration.ZERO) && !this.musicSlider.isValueChanging()) {
                    this.musicSlider.setValue(currentTime.divide(this.duration).toMillis() * 100.0D);
                }

            });
        }

    }

    private String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / 3600;
        if(elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }

        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;
        if(duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / 3600;
            if(durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }

            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
            return durationHours > 0?String.format("%d:%02d:%02d/%d:%02d:%02d", new Object[]{Integer.valueOf(elapsedHours), Integer.valueOf(elapsedMinutes), Integer.valueOf(elapsedSeconds), Integer.valueOf(durationHours), Integer.valueOf(durationMinutes), Integer.valueOf(durationSeconds)}):String.format("%02d:%02d/%02d:%02d", new Object[]{Integer.valueOf(elapsedMinutes), Integer.valueOf(elapsedSeconds), Integer.valueOf(durationMinutes), Integer.valueOf(durationSeconds)});
        } else {
            return elapsedHours > 0?String.format("%d:%02d:%02d", new Object[]{Integer.valueOf(elapsedHours), Integer.valueOf(elapsedMinutes), Integer.valueOf(elapsedSeconds)}):String.format("%02d:%02d", new Object[]{Integer.valueOf(elapsedMinutes), Integer.valueOf(elapsedSeconds)});
        }
    }

    public void slide() {
        this.musicSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if(FXMLDocumentController.this.musicSlider.isValueChanging()) {
                    if(FXMLDocumentController.this.duration != null) {
                      FXMLDocumentController.this.music.seek(FXMLDocumentController.this.duration.multiply(FXMLDocumentController.this.musicSlider.getValue() / 100.0D));
                    }

                    FXMLDocumentController.this.updateValues();
                }

            }
        });
    }
    
    public void saveDir(){
            System.out.println("writing Value to data file.");
            if(file != null && file.exists()) {
            startPath = file;
            byte[] pato = startPath.getParent().getBytes();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(dataFolder);
                fileOutputStream.write(pato);
                fileOutputStream.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
    }
    
    
    public void handleplay() {
     if(media == null){
            Alert nomedia = new Alert(Alert.AlertType.ERROR);
            nomedia.setHeaderText(null);
            nomedia.setContentText("No media to play");
            nomedia.showAndWait();
        }else{
           if(this.file == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No Media Found");
                    alert.setHeaderText((String)null);
                    alert.setContentText("You have Nothing Selected to play!");
                    alert.showAndWait();
                    
                }
           if(songbage.getImage() == this.albumcover){
            rotation_trans = new RotateTransition(rotation_duration, songbage);
            rotation_trans.setFromAngle(0);
            rotation_trans.setToAngle(360);
            rotation_trans.setCycleCount(Timeline.INDEFINITE);           
            }
           if(music != null){
                if(music.getStatus() == Status.PLAYING){
                    this.play.setText("Pause");
                    this.play.setGraphic(this.imagePlay);
                    music.pause();
                    this.browse.setVisible(true);
                }else if(music.getStatus() == Status.PAUSED || music.getStatus() == Status.READY){
                    this.play.setText("Play");
                    this.play.setGraphic(this.imagePause);
                    music.play();


                    this.slide();
                    this.play();
                    this.browse.setVisible(false);
                }
           }
      }
    }

    
    public void handleStop() {
      if(media == null){
          Alert nomedia = new Alert(Alert.AlertType.ERROR);
            nomedia.setHeaderText(null);
            nomedia.setContentText("No media to Stop");
            nomedia.showAndWait();
      }else{
        this.music.stop();
     //   this.notifyuser2();
        if(!this.browse.isVisible()) {
            this.browse.setVisible(true);
        }
        
        if("Pause".equals(this.play.getText())) {
            this.play.setText("Play");
            this.play.setGraphic(this.imagePlay);
        }
      }
    }
    
    @FXML
    public void handleHelp(){
        
    }
    
    @FXML
    public void handleClose(){     
        System.out.println("System is exiting!!");
        System.exit(0);
    }
    
    
    @FXML
    public void handleVisualise() {
        if(this.butn.isSelected()) {
            this.pane.setVisible(true);
        } else {
            this.pane.setVisible(false);
        }

    }
    @FXML
    public void handleMini(){
        if(this.toggle.isSelected()){
            this.highgraphics.setVisible(true);
        }else{
            this.highgraphics.setVisible(false);
        }
    }
//----------------------- expermental caution:- May not work please do not uncomment!!!!-------------------------
     @FXML
    private void handle_musicfloder() {
      DirectoryChooser dirchsr = new DirectoryChooser();
      dirchsr.setTitle("Open folder Which contains musics");
      File chosen_dir = dirchsr.showDialog(stage);
      for(File file : chosen_dir.listFiles()){
          System.out.println(file.getAbsolutePath() + "--------------------" +file.getPath());
      }
      System.out.println("\n" + "------------------------Finished!!!--------------------");
    //chosen_dir.renameTo(file);
      for(String file : chosen_dir.list(new FilenameFilter(){
          @Override
          public boolean accept(File dir, String name) {
              return name.endsWith(".mp3");
          }
      }))this.play_list.add(createPlayer("file:///" + (chosen_dir + "\\" + file).replace("\\", "/").replaceAll(" ", "%20")));
      System.out.println("Sucessfully created " + this.play_list.size());
    } 
    private MediaPlayer createPlayer(String aMediaSrc) {
        
        final MediaPlayer player = new MediaPlayer(new Media(aMediaSrc));
        player.setOnError(new Runnable() {
          @Override public void run() {
            System.out.println("Media error occurred: " + player.getError());
          }
        });
        return player;
    }
    
}