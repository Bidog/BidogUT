package uoft.wuyuep2;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String FILENAME="notes.txt";
    private OnFragmentInteractionListener mListener;

    private TextView mTextView;

    //private ArrayList<Person>

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoadFragment newInstance(String param1, String param2) {
        LoadFragment fragment = new LoadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoadFragment() {
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


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setup() throws IOException {
        // get from preference //
//     //   ArrayList<Displayable> list = this.mService.getStoredCatalogFiles(getActivity());
//
//        final ListView listView = (ListView) getActivity().findViewById(R.id.list);
//        TitleDetailAdapter adapter = new TitleDetailAdapter(getActivity(), list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                loadSelectedCatalog(listView, position);
//            }
//        });
        Button mDone_Button = (Button)getActivity().findViewById(R.id.Done_Button_Load);
        mDone_Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getFragmentManager().popBackStackImmediate();
            }
        });
        mTextView = (TextView)getActivity().findViewById(R.id.loadView);
        //new LoadTask().execute(getTarget());
        try {
            Loadfile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Loadfile() throws IOException {
        File root = getActivity().getFilesDir();
        File target = new File(root, FILENAME);
        String result="";


      //  String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
//        Log.d("Files", "Path: " + root);
//        File f = new File(root);
        File file[] = root.listFiles();
        Log.d("Files", "Size: "+ file.length);
        ArrayList<String> fileName = new ArrayList<String>();
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
            fileName.add(file[i].getName().toString());
        }
        // adapter a list to show files name
        ListView lv = (ListView)this.getActivity().findViewById(R.id.LoadList);

        ArrayAdapter<String> adapter;

//        adapter = new ArrayAdaptergetActivity().(this,fileName);
//        lv.setAdapter(adapter);


        try {
            InputStream in=new FileInputStream(target);
            if (in != null) {
                try {
                    InputStreamReader tmp=new InputStreamReader(in);
                    BufferedReader reader=new BufferedReader(tmp);
                    String str;
                    StringBuilder buf=new StringBuilder();
                    while ((str=reader.readLine()) != null) {
                        buf.append(str);
                        buf.append("\n");
                    }
                    result=buf.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in.close();
                }
            }
        }
        catch (java.io.FileNotFoundException e) {
            // that's OK, we probably haven't created it yet
        }
        mTextView.setText(result);
    }
//    private File getTarget() {
//        File root=null;
//        return(new File(root, "name.txt"));
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load, container, false);
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
    private void boom(Exception e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
                .show();
        Log.e(getClass().getSimpleName(), "Exception saving file", e);
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
//    private class LoadTask extends AsyncTask<File, Void, String> {
//        private Exception e=null;
//
//        @Override
//        protected String doInBackground(File... args) {
//            String result="";
//
//            try {
//                result=load(args[0]);
//            }
//            catch (Exception e) {
//                this.e=e;
//            }
//            return(result);
//        }
//
//        @Override
//        protected void onPostExecute(String text) {
//            if (e == null) {
//                mTextView.setText(text);
//            }
//            else {
//                boom(e);
//            }
//        }
//    }
//    private String load(File target) throws IOException {
//        String result="";
//
//        try {
//            InputStream in=new FileInputStream(target);
//
//            if (in != null) {
//                try {
//                    InputStreamReader tmp=new InputStreamReader(in);
//                    BufferedReader reader=new BufferedReader(tmp);
//                    String str;
//                    StringBuilder buf=new StringBuilder();
//
//                    while ((str=reader.readLine()) != null) {
//                        buf.append(str);
//                        buf.append("\n");
//                    }
//
//                    result=buf.toString();
//                }
//                finally {
//                    in.close();
//                }
//            }
//        }
//        catch (java.io.FileNotFoundException e) {
//            // that's OK, we probably haven't created it yet
//        }
//
//        return(result);
//    }

}
