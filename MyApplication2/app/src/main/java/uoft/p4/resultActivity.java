package uoft.p4;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class resultActivity extends ActionBarActivity {
    private DatabaseHelper db = null;
    private TextView mTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        db = DatabaseHelper.getInstance(getApplicationContext());
        mTV = (TextView)findViewById(R.id.textView);
        new loadDataBase().execute(null, null, null);
    }

    private class loadDataBase extends AsyncTask<Void, String, Void> {
        private static final String SELECT_QUERY = "SELECT DISTINCT "
                + DatabaseHelper.NAME + " FROM " + DatabaseHelper.TABLE
                + " ORDER BY " + DatabaseHelper.NAME + " DESC";
        String query = "SELECT  * FROM "+ DatabaseHelper.TABLE;
        private Cursor cursor = null;

        @Override
        protected Void doInBackground(Void... unused) {
            cursor = db.getReadableDatabase().rawQuery(query, null);
//            cursor.getCount();
//            // go through the names
//            while (cursor.moveToNext()) {
//                String name = cursor.getString(0);
//                publishProgress(name);
//            }
//            return null;
            if (cursor.moveToFirst()) {
                do {
                    mTV.append(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values.length >= 1) {
                mTV.append(values.toString());
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void unused) {


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
