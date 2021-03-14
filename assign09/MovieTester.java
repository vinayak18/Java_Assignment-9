package assign09;

import java.io.IOException;
import java.util.List;

public class MovieTester {

	public static void main(String[] args) throws IOException {
		Movie ms = new Movie();
		
		String path = "C:\\Users\\VINAYAK\\OneDrive\\Desktop\\VINAYAK\\Persistent\\JAVA\\Solution\\assign09\\movies.txt";
		ms.movieList = ms.populateMovies(path);
		
		ms.addAllMoviesInDB(ms.movieList);
		
		List<Movie> list = ms.getMoviesByYear(2012);
		System.out.println("Movie released in the year 2012 -- ");
		for(Movie m: list) {
			System.out.println(m.getMovieName());
		}	
	}
}