package uoft.p4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private DatabaseHelper db = null;
    private TextView myTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize DB

        myTV = (TextView)findViewById(R.id.textView2);

        db = DatabaseHelper.getInstance(getApplicationContext());
        Button mPopulate = (Button) findViewById(R.id.btnPopulate);
        // populate
        mPopulate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String url = ((EditText) findViewById(R.id.edtDBURL)).getText().toString();
                if (!url.isEmpty()) {
                    try {
                        URL dbUrl = new URL(url);
                        new PopulateTask().execute(dbUrl, null, null);
                    } catch (MalformedURLException e) {
                        showStatus(R.string.invalidUrl);
                    }
                }
            }
        });
        // search
        Button mSearch = (Button) findViewById(R.id.btnSearch);
        // populate
        mSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                new SearchTask().execute(null, null, null);
            }
        });

        Button mResult = (Button) findViewById(R.id.btnResult);
        mResult.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                Intent explicitIntent = new Intent(MainActivity.this, resultActivity.class);
//                startActivity(explicitIntent);
                new loadDataBase().execute(null, null, null);
            }
        });
    }


    private class PopulateTask extends AsyncTask<URL, Void, Boolean> {
        @Override
        protected Boolean doInBackground(URL... urls) {
            boolean success = true;
            if (urls.length < 1) {
                // should never happen, called internally
                showStatus(R.string.unexpectedError);
            } else {
                // read the file from the URL and populate DB
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(
                            urls[0].openStream()));
                    // Read all the text returned by the server
                    String name;
                    while ((name = in.readLine()) != null) {
                        // store name in the database
                        ContentValues values = new ContentValues(1);
                        values.put(DatabaseHelper.NAME, name);
                        values.put(DatabaseHelper.Bio, in.readLine());
                        values.put(DatabaseHelper.LocalFilePathToPicture, in.readLine());
                        db.getWritableDatabase().insert(DatabaseHelper.TABLE,
                                null, values);
                    }
                } catch (IOException e) {
                    success = false;
                } finally {
                    try {
                        if (in != null)
                            in.close();
                    } catch (IOException e) {
                        // stream not open, ignore
                    }

                }
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar) findViewById(R.id.pbrLoading))
                    .setVisibility(View.VISIBLE);
            findViewById(R.id.btnPopulate).setEnabled(false);
            findViewById(R.id.btnSearch).setEnabled(false);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            ((ProgressBar) findViewById(R.id.pbrLoading))
                    .setVisibility(View.INVISIBLE);
            findViewById(R.id.btnPopulate).setEnabled(true);

            if (success) {
                findViewById(R.id.btnSearch).setEnabled(true);
                showStatus(R.string.loaded);
            } else {
                showStatus(R.string.unreadableUrl);
            }
        }
    }


    private class SearchTask extends AsyncTask<Void, String, Void> {
        private static final String SELECT_QUERY = "SELECT DISTINCT "
                + DatabaseHelper.NAME + " FROM " + DatabaseHelper.TABLE
                + " ORDER BY " + DatabaseHelper.NAME + " DESC";
        private Cursor cursor = null;

        @Override
        protected Void doInBackground(Void... unused) {
            cursor = db.getReadableDatabase().rawQuery(SELECT_QUERY, null);
            cursor.getCount();
            // go through the names
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                publishProgress(name);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values.length >= 1) {
                Intent lookup = new Intent(MainActivity.this, WebViewActivity.class);
                lookup.putExtra(DatabaseHelper.NAME, values[0]);
                startActivity(lookup);
            }
        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar) findViewById(R.id.pbrLoading))
                    .setVisibility(View.VISIBLE);
            findViewById(R.id.btnPopulate).setEnabled(false);
            findViewById(R.id.btnSearch).setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void unused) {
            ((ProgressBar) findViewById(R.id.pbrLoading))
                    .setVisibility(View.INVISIBLE);
            findViewById(R.id.btnPopulate).setEnabled(true);
            findViewById(R.id.btnSearch).setEnabled(true);
            cursor.close();
        }
    }

    private class CleanTask extends AsyncTask<Void, Void, Void> {
        private static final String DELETE_QUERY = "DELETE FROM "
                + DatabaseHelper.TABLE;

        @Override
        protected Void doInBackground(Void... unused) {
            db.getWritableDatabase().execSQL(DELETE_QUERY);
            return null;
        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar) findViewById(R.id.pbrLoading))
                    .setVisibility(View.VISIBLE);
            findViewById(R.id.btnPopulate).setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void unused) {
            ((ProgressBar) findViewById(R.id.pbrLoading))
                    .setVisibility(View.INVISIBLE);
            findViewById(R.id.btnPopulate).setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    protected void onDestroy() {
        getBaseContext().deleteDatabase("P4.db");
    }

    private void showStatus(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private class loadDataBase extends AsyncTask<Void, String, Void> {
        private static final String SELECT_QUERY = "SELECT DISTINCT "
                + DatabaseHelper.NAME + " FROM " + DatabaseHelper.TABLE
                + " ORDER BY " + DatabaseHelper.NAME + " DESC";
        String query = "SELECT  * FROM "+ DatabaseHelper.TABLE;
        private Cursor cursor = null;
        String resultText = "";
        @Override
        protected Void doInBackground(Void... unused) {
            cursor = db.getReadableDatabase().rawQuery(query, null);

//            return null;
            if (cursor.moveToFirst()) {
                do {
                    resultText+=cursor.getString(1);
                } while (cursor.moveToNext());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values.length >= 1) {
                myTV.setText(resultText.toString());
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void unused) {
            myTV.setText(resultText.toString());

        }
    }
}
