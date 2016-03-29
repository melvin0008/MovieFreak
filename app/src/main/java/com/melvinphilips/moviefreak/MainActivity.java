package com.melvinphilips.moviefreak;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

//    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, Integer.toString(position));
                startActivity(intent);
            }
        });

    }

//    private void updateOptionsMenu() {
//        if (mOptionsMenu != null) {
//            onPrepareOptionsMenu(mOptionsMenu);
//        }
//    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            GetMovieTask movieTask = new GetMovieTask();
            movieTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetMovieTask extends AsyncTask<Void,Void,Void> implements Constants{

        private final String LOG_TAG = GetMovieTask.class.getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            String baseUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key="+Constants.API_KEY;

            try {
                URL url = new URL(baseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null){
                    return null;
                }

                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine())!=null){
                    buffer.append(line+"\n");
                }

                if (buffer.length() == 0) {
                    return  null;
                }
                movieJsonStr = buffer.toString();
                Log.d(LOG_TAG,movieJsonStr);
            }
            catch (IOException e){
                Log.e(LOG_TAG, "Error ", e);
                return null;
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
                    }
                }

            }

            return null;
        }
    }
}
