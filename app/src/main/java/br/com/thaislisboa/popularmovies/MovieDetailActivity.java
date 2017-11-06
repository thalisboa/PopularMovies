package br.com.thaislisboa.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.thaislisboa.popularmovies.domain.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_view);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        TextView mTitle = findViewById(R.id.title);
        ImageView mPicture = findViewById(R.id.iv_picture);
        TextView mYear = findViewById(R.id.tv_year);
        TextView mDetails = findViewById(R.id.tv_details);
        TextView mGrade = findViewById(R.id.tv_grade);

        mTitle.setText(movie.getTitle());
        Picasso.with(this).load(movie.getPoster()).into(mPicture);
        mDetails.setText(movie.getOverview());
        mYear.setText(movie.getYear());

        mGrade.setText(movie.getGrade());
    }
}
