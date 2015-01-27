package uoft.wuyuep2;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import SupportClass.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FILENAME="notes.txt";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText mSaveName;
    private GlobalClass globalVariable;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setup();
    }

    private void setup() {
        globalVariable = (GlobalClass)getActivity().getApplicationContext();
        this.mSaveName = (EditText) getActivity().findViewById(R.id.Save_Name);
        final Button mSaveButton = (Button) getActivity().findViewById(R.id.Save_Button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mSaveName.getText().toString().matches("")){
                    Toast toast = Toast.makeText(getActivity(), "Please enter the file name.",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    // getActivity unsaved list and store to the phone;
                    ArrayList<Person> readytoStore = globalVariable.getUnSaveList();
                    File root = getActivity().getFilesDir();
                    String saveFileName = mSaveName.getText().toString() + ".txt";
                    File target = new File(root, saveFileName);
                    try {
                        saveList(globalVariable.getPersonListString(), target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getActivity(), "file was saved",Toast.LENGTH_SHORT);
                    toast.show();
                    // TODO: CLEAN THE GLOBAL VARIABLE
                    globalVariable.CleanUnSaveList();
                }
            }
        });
        Button mDone_Button = (Button)getActivity().findViewById(R.id.DoneButton);
        mDone_Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getFragmentManager().popBackStackImmediate();
            }
        });
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

    private void saveList(ArrayList<String> personlist, File target) throws  IOException{
        FileOutputStream fos = new FileOutputStream((target));
        ObjectOutputStream o1 = new ObjectOutputStream((fos));
        o1.writeObject(personlist);
        o1.close();
        fos.close();
    }

}
