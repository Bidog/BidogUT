package uoft.wuyuep2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import SupportClass.ActivityCommunicator;
import SupportClass.FragmentCommunicator;
import SupportClass.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnterName_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnterName_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterName_Fragment extends Fragment implements FragmentCommunicator {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Context context;
    // TODO: Rename and change types of parameters

    private GlobalClass globalVariable;
    private EditText mNameEditText;
    private EditText mAgeEditText;
    private Spinner mFoodSpinner;
    private OnFragmentInteractionListener mListener;

    //interface via which we communicate to hosting Activity
    private ActivityCommunicator activityCommunicator;
    private String activityAssignedValue ="";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterName_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterName_Fragment newInstance(String param1, String param2) {
        EnterName_Fragment fragment = new EnterName_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EnterName_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enter_name_, container, false);
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setup();
    }

    private void setup() {

        globalVariable = (GlobalClass)getActivity().getApplicationContext();
        this.mNameEditText = (EditText) getActivity().findViewById(R.id.EnterName_EditText);
        this.mAgeEditText = (EditText) getActivity().findViewById(R.id.EnterAge_EditText);
        this.mFoodSpinner = (Spinner) getActivity().findViewById(R.id.FoodType_Spinner);

        Button addButton = (Button) getActivity().findViewById(R.id.Add_Button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNameEditText.getText().toString().matches("") || mAgeEditText.getText().toString().matches("")){
                    Toast toast = Toast.makeText(getActivity(), "Please fill out both name and age.",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    addPerson();
                    Toast toast = Toast.makeText(getActivity(), "Person has been added to the list",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Button DoneButton = (Button) getActivity().findViewById(R.id.Done_Button);
        DoneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getFragmentManager().popBackStackImmediate();
            }
        });
    }
    public void addPerson(){
        Log.d("PERSON","PERSON + " + this.mNameEditText.getText().toString());
        Log.d("FOOD","FOOD + " + this.mFoodSpinner.getSelectedItem().toString());
        Log.d("PERSON","PERSON + " + this.mAgeEditText.getText().toString());
        Person person = new Person(this.mNameEditText.getText().toString(),this.mFoodSpinner.getSelectedItem().toString(),this.mAgeEditText.getText().toString());
        globalVariable.addPerson(person);
        Log.d("the person we just add", "the person we add is" + person.toString());
        activityCommunicator.passDataToActivity(person);
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
        context = getActivity();
        activityCommunicator =(ActivityCommunicator)context;

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

    public void passDataToFragment(String someValue){
        activityAssignedValue = someValue;

        //textView.setText(activityAssignedValue);
    }
}
