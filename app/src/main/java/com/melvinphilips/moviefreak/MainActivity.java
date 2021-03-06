package com.melvinphilips.moviefreak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView mGridView;
    private ArrayList<GridItem> mGridData;
    private GridViewAdapter mGridAdapter;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.gridView);

        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this,R.layout.grid_item_layout,mGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                GridItem item = mGridData.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(getString(R.string.title),item.getDetailName());
                intent.putExtra(getString(R.string.image),item.getDetailImage());
                intent.putExtra(getString(R.string.overview),item.getDetailOverView());
                intent.putExtra(getString(R.string.release_date),item.getDetailReleaseDate());
                intent.putExtra(getString(R.string.vote_average), String.valueOf(item.getDetailVoteAverage()));

                startActivity(intent);
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sortingTypeExists = preferences.contains(getString(R.string.sorting_method));
        if(!sortingTypeExists){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getString(R.string.sorting_method),getString(R.string.top_rated));
            editor.apply();
        }
        new GetMovieTask().execute(preferences.getString(getString(R.string.sorting_method),""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_top_rated && preferences.getString(getString(R.string.sorting_method),"").equals(getString(R.string.popular))) {
            mGridData.clear();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getString(R.string.sorting_method),getString(R.string.top_rated));
            editor.apply();

            GetMovieTask movieTask = new GetMovieTask();
            movieTask.execute(getString(R.string.top_rated));
            return true;
        }
        else if( id == R.id.action_popular && preferences.getString(getString(R.string.sorting_method),"").equals(getString(R.string.top_rated))){
            mGridData.clear();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(getString(R.string.sorting_method),getString(R.string.popular));
            editor.apply();

            GetMovieTask movieTask = new GetMovieTask();
            movieTask.execute(getString(R.string.popular));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public class GetMovieTask extends AsyncTask<String,Void,Integer>{

        private final String LOG_TAG = GetMovieTask.class.getSimpleName();
        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            String baseUrl = getString(R.string.movie_db_url)+params[0]+"?api_key="+getString(R.string.api_key);

            try {
                URL url = new URL(baseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null){
                    return 0;
                }

                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine())!=null){
                    buffer.append(line+"\n");
                }

                if (buffer.length() == 0) {
                    return  0;
                }
                movieJsonStr = buffer.toString();
                populateGridData(movieJsonStr);
                Log.d(LOG_TAG,movieJsonStr);
            }
            catch (IOException e){
                Log.e(LOG_TAG, "Error ", e);
                return 0;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error", e);
                        return 0;
                    }
                }

            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer result){
            if(result==1){
                mGridAdapter.setGridData(mGridData);
            }
        }

    }

    private void populateGridData(String response){
        final String LOG_TAG = GetMovieTask.class.getSimpleName();
        try{
            JSONObject responseObj =  new JSONObject(response);
            JSONArray moviesArray = responseObj.optJSONArray(getString(R.string.results));
            GridItem item;

            for(int i = 0; i < moviesArray.length();i++){
                JSONObject movie = moviesArray.optJSONObject(i);
                item = new GridItem();
                item.setDetailImage(getString(R.string.image_base_url) + movie.getString(getString(R.string.poster_path)));
                item.setDetailName(movie.getString(getString(R.string.title)));
                item.setDetailOverView(movie.getString(getString(R.string.overview)));
                item.setDetailReleaseDate(movie.getString(getString(R.string.release_date)));
                item.setDetailVoteAverage(movie.getDouble(getString(R.string.vote_average)));
                mGridData.add(item);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
