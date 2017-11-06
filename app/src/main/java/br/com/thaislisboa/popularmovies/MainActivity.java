package br.com.thaislisboa.popularmovies;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.thaislisboa.popularmovies.domain.model.Movie;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String appKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        try {
            Bundle b = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            appKey = b.getString("appkey");
            mRecyclerView = findViewById(R.id.rv_main);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            fetchMostPopular();
        } catch (Exception cause) {
            Log.e("MV", cause.getMessage(), cause);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        try {
            //order it by most popular
            if (id == R.id.action1) {
                fetchMostPopular();
            }

            //order it by top rated
            if (id == R.id.action2) {
                fetchTopRated();
            }
        } catch (Exception cause) {
            Log.e("", cause.getMessage(), cause);
        }
        return true;
    }

    private void fetchTopRated() throws Exception {
        MovieAsyncTask mat = new MovieAsyncTask();
        List<Movie> movies = mat.execute("top_rated").get();
        mRecyclerView.setAdapter(new MovieAdapter(movies));
    }

    private void fetchMostPopular() throws Exception {
        MovieAsyncTask mat = new MovieAsyncTask();
        List<Movie> movies = mat.execute("popular").get();
        mRecyclerView.setAdapter(new MovieAdapter(movies));
    }

    class MovieAsyncTask extends AsyncTask<String, Movie, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... strings) {
            List<Movie> movies = new ArrayList<>();

            try {
                URL url = new URL(Uri.parse("http://api.themoviedb.org/3/movie/" + strings[0])
                        .buildUpon()
                        .appendQueryParameter("api_key", appKey)
                        .build().toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                StringBuilder content = new StringBuilder();
                try (InputStream in = urlConnection.getInputStream();
                     Scanner data = new Scanner(in)) {
                    data.useDelimiter("\n");
                    while (data.hasNext()) {
                        content.append(data.next());
                    }

                    JSONObject json = new JSONObject(content.toString());
                    JSONArray results = json.getJSONArray("results");
                    Movie movie;

                    for (int i = 0; i < results.length(); i++) {
                        json = results.getJSONObject(i);
                        movie = new Movie(json.getLong("id"),
                                json.getBoolean("video"),
                                json.getDouble("vote_average"),
                                json.getString("title"),
                                json.getString("poster_path"),
                                json.getString("backdrop_path"),
                                json.getString("overview"),
                                json.getString("release_date"));

                        movies.add(movie);
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception cause) {
                Log.e("MV", cause.getMessage(), cause);
            }

            return movies;
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        MovieViewHolder(View itemView) {
            super(itemView);
        }
    }

    class MovieAdapter extends RecyclerView.Adapter {

        private List<Movie> movies;

        MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.movie_item_view, parent, false);
            return new MovieViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView mImageView = holder.itemView.findViewById(R.id.image);
            final Movie movie = movies.get(position);

            Picasso.with(MainActivity.this).load(movie.getPoster()).into(mImageView);

            mImageView.setOnClickListener(e -> {
                Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);

                i.putExtra("movie", movie);

                startActivity(i);
            });
        }

        @Override
        public int getItemCount() {

            return movies.size();
        }
    }

}




