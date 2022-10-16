// Kush Soni Dhiren Vazirani
package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	//Create Accordion
	static Accordion accordion = new Accordion();
	static ArrayList<Song> list = new ArrayList<Song>();
	static ScrollPane sp = new ScrollPane(accordion);
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Opening out.txt to get SongData
			try {
				BufferedReader in = new BufferedReader(new FileReader("out.txt"));
				String line;
				while((line = in.readLine()) != null) {
				    Song inSong = new Song();
				    inSong.setTitle(line);
				    inSong.setArtist(in.readLine());
				    line = in.readLine();
				    if(line.equals("|")) inSong.setAlbum(null);
				    else inSong.setAlbum(line);
				    line = in.readLine();
				    if(line.equals("|")) inSong.setYear(null);
				    else inSong.setYear(Integer.parseInt(line));
				    
					list.add(inSong);
					accordion.getPanes().add(createTP(inSong));
				}
				in.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			
			// Default Creates BorderPane
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			
			// ScrollPane Setup
			sp.setFitToHeight(true);
			sp.setFitToWidth(true);
			root.setCenter(sp);
			if(list.size()>0) accordion.setExpandedPane(accordion.getPanes().get(0));
			
			// Scene Setup
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// Save songs to out.txt
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		              try {
		            	new File("out.txt").delete();
						FileWriter songData = new FileWriter("out.txt");
						  for(Song s: list) {
							  songData.write(s.title+"\n"+s.artist+"\n");
							  if(s.album == null) songData.write("|\n");
							  else songData.write(s.year+"\n");
							  if(s.year == null) songData.write("|\n");
							  else songData.write(s.album+"\n");
						  }
						  songData.close();
					} catch (IOException e) {
						SampleController.makeError("Error Saving Songs.");
						e.printStackTrace();
					}
		          }
		      });
			
			//To Prevent Pane from Collapsing (Removed Platform.runLater)
			accordion.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> observable, TitledPane oldPane, TitledPane newPane) -> {
		        Boolean expand = true; // This value will change to false if there's (at least) one pane that is in "expanded" state, so we don't have to expand anything manually
		        for(TitledPane pane: accordion.getPanes()) {
		            if(pane.isExpanded()) {
		                expand = false;
		            }
		        }
		        //Here we already know whether we need to expand the old pane again
		        if((expand == true) && (oldPane != null)) {
		            	accordion.setExpandedPane(oldPane);
		        }
		    });
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Main
	public static void main(String[] args) {
		launch(args);
	}
	
	// Creates a TitledPane given a Song
	// Should be moved to SampleController Class
	static TitledPane createTP(Song song) {
		//Setting Labels
		
		VBox vBox = new VBox();

		Label songName = new Label("Title:\t  "+song.title);
		Label songArtist = new Label("Artist:\t  "+song.artist);
		
		vBox.getChildren().addAll(songName,songArtist);

		Label songAlbum;
		if(song.album!=null) {
			songAlbum = new Label("Album:\t  "+song.album);
			if(song.album!=null)vBox.getChildren().add(songAlbum);

		}
		
		Label songYear;
		if(song.year!=null) {
			songYear = new Label("Year:\t  "+song.year);
			if(song.year!=null)vBox.getChildren().add(songYear);

		}
		
		TitledPane newPane = new TitledPane(song.title+" - "+song.artist,vBox);
		
		return newPane;
	}
	
}


