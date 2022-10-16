// Kush Soni Dhiren Vazirani

package application;

public class Song implements Comparable<Song>{
	String title;
	String artist;
	String album;
	Integer year;
	
	public Song() {
	}
	
	public Song(String title,String artist,String album, Integer year) {
		this.title=title;
		this.artist=artist;
		this.album=album;
		this.year=year;
	}
	
	public void setTitle(String title) {
		this.title=title;
	}
	
	public void setArtist(String artist) {
		this.artist=artist;
	}
	
	public void setAlbum(String album) {
		this.album=album;
	}
	
	public void setYear(Integer year) {
		this.year=year;
	}

	@Override
	public int compareTo(Song song) {
		if(!(this.title.toLowerCase().equals(song.title.toLowerCase()))) {
			return this.title.toLowerCase().compareTo(song.title.toLowerCase());
		}
		return this.artist.toLowerCase().compareTo(song.artist.toLowerCase());
	}
	
	public Song copy() {
		return new Song(this.title,this.artist,this.album,this.year);
	}
}
