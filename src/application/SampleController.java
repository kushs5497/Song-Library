// Kush Soni Dhiren Vazirani

package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Optional;

//import Main;


public class SampleController {
	@FXML
	private Button addButton;
	@FXML
	private Button delButton;
	@FXML
	private Button editButton;
	
	private boolean inEvent;
	TitledPane eventPane;
		
	// Event Listener on Button[#addButton].onAction
	@FXML
	public void songadd(ActionEvent event) {
		if(inEvent) {
			Main.accordion.setExpandedPane(eventPane);
			return;
		}
		inEvent = true;
		
		TextField songNameField = new TextField();
		songNameField.setPromptText("Title*");
		TextField songArtistField = new TextField();
		songArtistField.setPromptText("Artist*");
		TextField songAlbumField = new TextField();
		songAlbumField.setPromptText("Album");
		TextField songYearField = new TextField();
		songYearField.setPromptText("Year");
		
		VBox fieldsBox = new VBox(5);
		fieldsBox.getChildren().addAll(songNameField,songArtistField,songAlbumField,songYearField);
		
		VBox buttonBox = new VBox(5);
		    Button saveButton = new Button("  Save  ");
		    	
		    Button cancelButton = new Button("Cancel");
		        
		buttonBox.getChildren().addAll(saveButton,cancelButton);
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(fieldsBox,buttonBox);
		hbox.setSpacing(5);
	    
		TitledPane newPane = new TitledPane("NewSong",hbox);
		Main.accordion.getPanes().add(newPane);
		Main.accordion.setExpandedPane(newPane);
		Main.sp.setVvalue(Main.sp.getHmax()*2);
		eventPane=newPane;
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                Optional<ButtonType> result = makeAlert("Save?","Are you sure you want to add this song?");
                if (result.get() == ButtonType.CANCEL) return;
                Song newSong = new Song();
                if(songArtistField.getText().isBlank() || songNameField.getText().isBlank() || (songArtistField.getText()+songNameField.getText()).contains("|")) {
                    makeError("Please enter a name and artist without the \'|\' character!");
                    return;
                }
                newSong.setArtist(songArtistField.getText().trim());
                newSong.setTitle(songNameField.getText().trim());
                if(!songAlbumField.getText().isBlank()) newSong.setAlbum(songAlbumField.getText().trim());
                if (!songYearField.getText().isBlank()) {
                    try {
                        newSong.setYear(Integer.parseInt(songYearField.getText().trim()));
                    } catch (NumberFormatException e1) {
                        makeError("Please enter an Integer for year!");
                        e1.printStackTrace();
                        return;
                    } 
                }
                if(exists(newSong)) {
                    makeError("Song Already Exists!");
                    return;
                }
                TitledPane newSongPane = Main.createTP(newSong);
                Main.accordion.getPanes().remove(newPane);
                insert(newSongPane,newSong);
                Main.accordion.setExpandedPane(newSongPane);
                inEvent=false;
            }
        });

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Main.accordion.getPanes().remove(newPane);
                inEvent=false;
            }
        });
		
	}

	// Event Listener on Button[#delButton].onAction
	@FXML
	public void songdel(ActionEvent event) {
		if(Main.accordion.getPanes().isEmpty()) return;
	    
	    // Make Alert
		Optional<ButtonType> result = makeAlert("Delete?","Are you sure you want remove this song?");
		
		// Alert Action
		if(result.get()==ButtonType.YES) {
			TitledPane temp = Main.accordion.getExpandedPane();
			int index = Main.accordion.getPanes().indexOf(Main.accordion.getExpandedPane());
			Main.list.remove(index);
			Main.accordion.getPanes().remove(temp);
			if(Main.list.size()==0) return;
			if(Main.list.size()==index) index--;
			Main.accordion.setExpandedPane(Main.accordion.getPanes().get(index));
		}
	}
	
	// Event Listener on Button[#editButton].onAction
	@FXML
	public void songedit(ActionEvent event) {
	    if(Main.accordion.getPanes().isEmpty()) return;
	    
	    if(inEvent) {
			Main.accordion.setExpandedPane(eventPane);
			return;
		}
		inEvent=true;
		
		TitledPane editingPane = Main.accordion.getExpandedPane();
		int index = Main.accordion.getPanes().indexOf(editingPane);
		Song editingSong = Main.list.remove(index);
		Song copy = editingSong.copy();
		
		TextField titleField = new TextField(editingSong.title);
		TextField artistField = new TextField(editingSong.artist);
		TextField albumField = new TextField();
			if(editingSong.album != null) albumField.setText(editingSong.album);
			else albumField.setPromptText("Album");
		TextField yearField = new TextField();
			if(editingSong.year != null) yearField.setText(""+editingSong.year);
			else yearField.setPromptText("Year");
		Button save = new Button("Save");
		Button cancel = new Button("Cancel");
		
		VBox fieldBox = new VBox(5);
		fieldBox.getChildren().addAll(titleField,artistField,albumField,yearField);
		VBox buttonBox = new VBox(5);
		buttonBox.getChildren().addAll(save,cancel);
		HBox fullContent = new HBox(10);
		fullContent.getChildren().addAll(fieldBox,buttonBox);
		
		TitledPane tempEditPane = new TitledPane("Editing: "+editingSong.title+" - "+editingSong.artist,fullContent);
		Main.accordion.getPanes().remove(index);
		Main.accordion.getPanes().add(index, tempEditPane);
		Main.accordion.setExpandedPane(tempEditPane);
		
		
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Song newEditSong = new Song();
				if(titleField.getText().isBlank() || artistField.getText().isBlank() || (titleField.getText()+albumField.getText()).contains("|")) {
					makeError("Please enter a name and artist without the following characters: \n \'|\'");
					return;
				}
				else {
					newEditSong.title=titleField.getText().trim();
					newEditSong.artist=artistField.getText().trim();
				}
				if(exists(newEditSong)) {
					makeError("Song already exists!");
					return;
				}
				if(albumField.getText().isBlank()) newEditSong.setAlbum(null);
				else newEditSong.setAlbum(albumField.getText().trim());
				if(yearField.getText().isBlank()) newEditSong.setYear(null);
				else {
					try {
						newEditSong.setYear(Integer.parseInt(yearField.getText().trim()));
					} catch (NumberFormatException e) {
						makeError("Please enter a number for the year.");
						e.printStackTrace();
						return;
					}
				}
				
				TitledPane editedSongPane = Main.createTP(newEditSong);
				Main.accordion.getPanes().remove(tempEditPane);
				Main.list.remove(editingSong);
				insert(editedSongPane,newEditSong);
				Main.accordion.setExpandedPane(editedSongPane);
				
				inEvent=false;
			}
		});
		
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Main.accordion.getPanes().remove(tempEditPane);
				insert(editingPane,copy);
				Main.accordion.setExpandedPane(editingPane);
				
				inEvent=false;
			}
		});
		
	}
	
	// Makes Custom Alert
	public Optional<ButtonType> makeAlert(String title, String confirmation) {
		//Create Alert
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(title);
		alert.setContentText(confirmation);
		alert.setTitle("Confirmation");
		//Create Buttons & Add to Alert
		ButtonType ok = ButtonType.YES;
		ButtonType cancel = ButtonType.CANCEL;
		alert.getButtonTypes().setAll(ok,cancel);
		//Display and save Alert Output
		return alert.showAndWait();
	}
	
	static void makeError(String errorMessage) {
		Alert error = new Alert(AlertType.ERROR);
		error.setContentText(errorMessage);
		error.showAndWait();
	}
	
	public void insert(TitledPane pan, Song song) {
		for(int i=0;i<Main.list.size();i++) {
			if(song.compareTo(Main.list.get(i))<0) {
				Main.list.add(i, song);
				Main.accordion.getPanes().add(i, pan);
				return;
			}
		}
		Main.list.add(song);
		Main.accordion.getPanes().add(pan);
		
	}
	
	public boolean exists(Song song) {
		for(Song s: Main.list) {
			if(s.compareTo(song)==0) {
				return true;
			}
		}
		return false;
	}
	
}
