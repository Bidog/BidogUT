package uoft.wuyuep2;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import SupportClass.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LaunchPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LaunchPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LaunchPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GlobalClass globalVariable;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LaunchPage.
     */
    // TODO: Rename and change types and number of parameters
    public static LaunchPage newInstance(String param1, String param2) {
        LaunchPage fragment = new LaunchPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LaunchPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        globalVariable = (GlobalClass)getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_launch_page, container, false);
        Button mEnterNameButton = (Button) view.findViewById(R.id.EnterName_button);
        mEnterNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                Log.d("button", "I just click the button");
                EnterName_Fragment fragment2 = new EnterName_Fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(isTablet(getActivity().getBaseContext())){
                    fragmentTransaction.replace(R.id.right_fragment_container, fragment2);
                }else {
                    fragmentTransaction.replace(R.id.left_fragment_container, fragment2);
                }
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

        Button mViewButton = (Button) view.findViewById(R.id.View_Button);
        mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                // here should start a new activity
                Intent explicitIntent = new Intent(getActivity(), ViewActivity.class);
                ArrayList<Person> readytoSend = ((MainActivity)getActivity()).getUnSaveList();

                Bundle bundleObject = new Bundle();
                //bundleObject.putParcelableArrayList("personlist", (ArrayList<? extends android.os.Parcelable>) readytoSend);

// Put Bundle in to Intent and call start Activity
                explicitIntent.putExtras(bundleObject);
                startActivity(explicitIntent);


             }
        });
        Button mLoadButton = (Button) view.findViewById(R.id.Load_Button);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                Log.d("button", "I just click the button");
                Load_Fragment_List newFragment = new Load_Fragment_List();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(isTablet(getActivity().getBaseContext())){
                    fragmentTransaction.replace(R.id.right_fragment_container, newFragment);
                }else {
                    fragmentTransaction.replace(R.id.left_fragment_container, newFragment);
                }
                fragmentTransaction.addToBackStack(null).commit();
            }
        });


        final Button mStoreButton = (Button) view.findViewById(R.id.Store_Button);
        mStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                Log.d("button", "I just click the button");
                StoreFragment newFragment = new StoreFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(isTablet(getActivity().getBaseContext())){
                    fragmentTransaction.replace(R.id.right_fragment_container, newFragment);
                }else {
                    fragmentTransaction.replace(R.id.left_fragment_container, newFragment);
                }
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

        // this is done
        Button mExitButton = (Button) view.findViewById(R.id.Exit_Button);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalVariable.getUnSaveListSize()==0){
                    getActivity().finish();
                }else{
                    Toast toast = Toast.makeText(getActivity(), "There is a unsaved list, please store first",Toast.LENGTH_SHORT);
                    toast.show();
                    mStoreButton.performClick();
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
