package com.melvinphilips.moviefreak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        LinearLayout layout = (LinearLayout) findViewById(R.id.movielayout);
        Intent intent = getIntent();
        String title = intent.getStringExtra(getString(R.string.title));
        String overview = intent.getStringExtra(getString(R.string.overview));
        String voteAverage = intent.getStringExtra(getString(R.string.vote_average))+"/10 ";
        Date releaseDate = null;
        String releaseDateString = null;
        try {
            releaseDate = new SimpleDateFormat("yyyy-mm-dd").parse(intent.getStringExtra(getString(R.string.release_date)));
            releaseDateString = new SimpleDateFormat("MMM dd, yyyy").format(releaseDate);
            Log.d("Date",releaseDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String imageUrl = intent.getStringExtra(getString(R.string.image));

        TextView titleDetail= (TextView) layout.findViewById(R.id.detail_movie_title);
        TextView releaseDetail= (TextView) layout.findViewById(R.id.detail_movie_release);
        TextView overviewDetail= (TextView) layout.findViewById(R.id.detail_movie_overview);
        TextView voteDetail= (TextView) layout.findViewById(R.id.detail_movie_vote);
        ImageView imageDetail = (ImageView) layout.findViewById(R.id.detail_movie_image);

        titleDetail.setText(title);
        overviewDetail.setText(overview);
        voteDetail.setText(voteAverage);
        releaseDetail.setText(releaseDateString);
        Picasso.with(DetailActivity.this).load(imageUrl).into(imageDetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
