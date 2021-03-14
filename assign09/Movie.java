package assign09;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

enum Category {
  ROMCOM,
  ROMANCE,
  DRAMA,
  COMEDY,
  ACTION,
  SCIENCE_FICTION,
  THRILLER,
  ANIMATION,
  HORROR;
}

enum Language {
  HINDI,
  ENGLISH,
  MARATHI,
  TELGU,
  TAMIL,
  JAPANESE,
  KOREAN,
  THAI;
}
public class Movie implements java.io.Serializable {
	private int movieId;
	private String movieName; 
	private Category movieType;
	private Language language; 
	private Date releaseDate;
	private  List<String> casting;
	private double rating;
	private double totalBusinessDone;
	
	public List<Movie> movieList;
	
	public Movie() {
		super();
	}
	
	public Movie(int movieId, String movieName, Category movieType, Language language, Date releaseDate,
			List<String> casting, double rating, double totalBusinessDone) {
		this.movieId = movieId;
		this.movieName = movieName;
		this.movieType = movieType;
		this.language = language;
		this.releaseDate = releaseDate;
		this.casting = casting;
		this.rating = rating;
		this.totalBusinessDone = totalBusinessDone;
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public Category getMovieType() {
		return movieType;
	}
	public void setMovieTye(Category movieType) {
		this.movieType = movieType;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public List<String> getCasting() {
		return casting;
	}
	public void setCasting(List<String> casting) {
		this.casting = casting;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public double getTotalBusinessDone() {
		return totalBusinessDone;
	}
	public void setTotalBusinessDone(double totalBusinessDone) {
		this.totalBusinessDone = totalBusinessDone;
	}
	
	public String toString() {
	    return movieId + " " + movieName + " " + movieType + " " + language + " " + releaseDate + " " + casting + " " + rating + " " + totalBusinessDone;
	  }
	
	public List<Movie> populateMovies(String file) {
	    List<Movie> list = new ArrayList<>();
	    try {
	      Scanner scan = new Scanner(new File(file));
	      SimpleDateFormat format = new SimpleDateFormat("dd/MM/YY");
	      while (scan.hasNext()) {
			Movie movie = new Movie();
			String[] line = scan.nextLine().split(",");
			movie.movieId = Integer.parseInt(line[0]);
			movie.movieName = line[1];
			movie.movieType = Category.valueOf(line[2].toUpperCase());
			movie.language = Language.valueOf(line[3].toUpperCase());
			movie.releaseDate = new Date(format.parse(line[4]).getTime());
			movie.casting = List.of(line[5].split(";"));
			movie.rating = Double.parseDouble(line[6]);
			movie.totalBusinessDone = Double.parseDouble(line[7]);
			list.add(movie);
	      }
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return list;
	}
	public boolean addAllMoviesInDB(List<Movie> movies) {
		Connection cn = DBConnectionUtil.getConnection();
		for(Movie m: movies) {
			try {
				PreparedStatement pstmt = cn.prepareStatement("Insert into Movies values(?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, m.getMovieId());
				pstmt.setString(2, m.getMovieName());
				pstmt.setString(3, m.getMovieType().toString());
				pstmt.setString(4, m.getLanguage().toString());
				pstmt.setDate(5, m.getReleaseDate());
				String cast="";
				for(String s: m.getCasting()) {
					cast+=s;
				}
				pstmt.setString(6, cast);
				pstmt.setDouble(7, m.getRating());
				pstmt.setDouble(8, m.getTotalBusinessDone());
				
				pstmt.executeUpdate();
				pstmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public void addMovie(Movie movie, List<Movie> movies) {
		movies.add(movie);
	}

	public void serializeMovies(List<Movie> movies, String fileName) {
	    try {
	      FileOutputStream fileOut = new FileOutputStream(fileName);
	      ObjectOutputStream out = new ObjectOutputStream(fileOut);
	      out.writeObject(movies);   
	      out.close();
	      fileOut.close();
	    } catch (IOException i) {
	      i.printStackTrace();
	    }
    }
	  
	@SuppressWarnings("unchecked")
	public List<Movie> deserializeMovie(String fileName) {
	    List<Movie> movies = new ArrayList<>();
	    try {
	      FileInputStream fileIn = new FileInputStream(fileName);
	      ObjectInputStream in = new ObjectInputStream(fileIn);
	      movies = (List<Movie>)in.readObject();
	      in.close();
	      fileIn.close();
	    } catch (IOException i) {
	    	System.out.println(i);
	    } catch (ClassNotFoundException c) {
		      System.out.println("Movie class not found");
		      c.printStackTrace();
	    }
	    return movies;
	}
	  
	public List<Movie> getMoviesByYear(int year){
		List<Movie> movie_list = new ArrayList<Movie>();
		for(Movie m: this.movieList) {
			Date d = m.getReleaseDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			int y = cal.get(Calendar.YEAR);
			
			if(y == year) {
				movie_list.add(m);
			}
		}
		return movie_list;
	}
	  
	public List<Movie> getMoviesByActor(String... actorNames){
		Set<Movie> movieset = new HashSet<Movie>();
		for(String actor:actorNames) {
			for(Movie m: this.movieList) {
				List<String> cast = m.getCasting();
				for(String c: cast) {
					if(c.equals(actor)) {
						movieset.add(m);
					}
				}
			}
		}
		List<Movie>finallist = new ArrayList<Movie>();
		finallist.addAll(movieset);
		return finallist;
	}
	
	public void updateRating(Movie movie, double rating, List<Movie> movies) {
		for(Movie m: movies) {
			if(m.equals(movie)) {
				m.setRating(rating);
			}
		}
	}
	
	public void updateBusiness(Movie movie, double amount, List<Movie> movies) {
		for(Movie m: movies) {
			if(m.equals(movie)) {
				m.setTotalBusinessDone(amount);
			}
		}
	}
	
	public Map<Language,Set<Movie>> businessDone(double amount){
		Map<Language,Set<Movie>> result = new HashMap<Language,Set<Movie>>();
		
		for(Movie m: this.movieList) {
			if(m.getTotalBusinessDone() > amount) {
				Language lang = m.getLanguage();
				if(result.containsKey(lang)) {
					result.get(lang).add(m);
				}else {
					Set<Movie>temp = new HashSet<Movie>();
					temp.add(m);
					result.put(lang,temp);
				}
			}
		}
		return result;
	}
}