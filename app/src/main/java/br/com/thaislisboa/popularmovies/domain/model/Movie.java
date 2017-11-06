package br.com.thaislisboa.popularmovies.domain.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private long id;
    private boolean video;
    private double vote_average;
    private String title;
    private String poster;
    private String backdrop;
    private String overview;
    private String date;

    public Movie(long id, boolean video, double vote_average,
                 String title, String poster, String backdrop, String overview, String date) {
        this.id = id;
        this.video = video;
        this.vote_average = vote_average;
        this.title = title;
        this.poster = poster;
        this.backdrop = backdrop;
        this.overview = overview;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getGrade() {
        return String.format("%s/10", getVote_average());
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return "http://image.tmdb.org/t/p/w780/".concat(poster);
    }

    public String getBackdrop() {
        return "http://image.tmdb.org/t/p/w342/".concat(backdrop);
    }

    public String getOverview() {
        return overview;
    }

    public String getDate() {
        return date;
    }

    public String getYear() {
        String[] date = getDate().split("-");
        return date[0];
    }

    @Override
    public String toString() {
        return "Movie{" + "id=" + id +
                ", video=" + video +
                ", vote_average=" + vote_average +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", backdrop='" + backdrop + '\'' +
                ", overview='" + overview + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
