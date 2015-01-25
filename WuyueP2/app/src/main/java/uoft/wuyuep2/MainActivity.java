package uoft.wuyuep2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import SupportClass.Person;


public class MainActivity extends ActionBarActivity {


    private final QuotesFragment mQuoteFragment = new QuotesFragment();
    public static String[] mTitleArray;
    public static String[] mQuoteArray;
    ArrayList<Person> unSavePersonList;

  //  private final QuotesFragment mQuoteFragment = new QuotesFragment();
    private FragmentManager mFragmentManager;
    private FrameLayout mLeftFrameLayout, mRightFrameLayout;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "QuoteViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");

        super.onCreate(savedInstanceState);

        // Get the string arrays with the titles and qutoes


        setContentView(R.layout.main);

        unSavePersonList = new ArrayList<Person>();
        // Get references to the TitleFragment and to the QuotesFragment
        mLeftFrameLayout = (FrameLayout) findViewById(R.id.left_fragment_container);
        mRightFrameLayout = (FrameLayout) findViewById(R.id.right_fragment_container);


        // Get a reference to the FragmentManager
        mFragmentManager = getFragmentManager();

        // Start a new FragmentTransaction
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();

        // Add the TitleFragment to the layout
        fragmentTransaction.add(R.id.left_fragment_container,
                new LaunchPage());

        // Commit the FragmentTransaction
        fragmentTransaction.commit();

        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        mFragmentManager
                .addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
              }
        });
        if (isTablet(getBaseContext())){
            Log.d("here","this is a large screen");
            // Start a new FragmentTransaction
            fragmentTransaction = mFragmentManager
                    .beginTransaction();
            // Add the QuoteFragment to the layout
            fragmentTransaction.add(R.id.right_fragment_container,
                    mQuoteFragment);
            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(null);
            // Commit the FragmentTransaction
            fragmentTransaction.commit();
            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }
    }


    private void setLayout() {

        // Determine whether the QuoteFragment has been added
        if (!isTablet(getBaseContext())) {

            // Make the TitleFragment occupy the entire layout
            mLeftFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mRightFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));
        } else {
            // Make the TitleLayout take 1/3 of the layout's width
            mLeftFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 1f));

            // Make the QuoteLayout take 2/3's of the layout's width
            mRightFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 2f));
        }
    }
    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }
}
