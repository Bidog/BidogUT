package uoft.wuyuep2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class Load_Fragment_List extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FILENAME="notes.txt";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static Load_Fragment_List newInstance(String param1, String param2) {
        Load_Fragment_List fragment = new Load_Fragment_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Load_Fragment_List() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
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
        Button mDone_Button = (Button)getActivity().findViewById(R.id.Done_Button_Load);
        mDone_Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getFragmentManager().popBackStackImmediate();
            }
        });
        try {
            Loadfile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Loadfile() throws IOException {
        final File root = getActivity().getFilesDir();
        //File target = new File(root, FILENAME);

        File file[] = root.listFiles();
        Log.d("Files", "Size: " + file.length);
        final ArrayList<String> fileName = new ArrayList<String>();
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
            fileName.add(file[i].getName().toString());
        }
        // adapter a list to show files name
        final ListView lv = (ListView)getActivity().findViewById(R.id.LoadList);
        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,fileName);
          lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selectedFile = fileName.get(position);
                File target = new File(root, selectedFile);


                    try {
                        FileInputStream in = new FileInputStream(target);
                        ObjectInputStream ois = new ObjectInputStream(in);
                        ArrayList<String> returnlist = (ArrayList<String>) ois.readObject();
                        Log.d("load file", fileName +" the load file result " + returnlist);
                        mAdapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1,returnlist);
                        lv.setAdapter(mAdapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }





//                try {
//                    InputStream in = new FileInputStream(target);
//                    if (in != null) {
//                        try {
//                            InputStreamReader tmp = new InputStreamReader(in);
//                            BufferedReader reader = new BufferedReader(tmp);
//                            String str;
//                            StringBuilder buf = new StringBuilder();
//                            while ((str = reader.readLine()) != null) {
//                                buf.append(str);
//                                buf.append("\n");
//                            }
//                            String result = buf.toString();
//
//                            Log.d("load file", fileName +" the load file result " + result);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            in.close();
//                        }
//                    }
//                } catch (IOException e) {
//                    // that's OK, we probably haven't created it yet
//                }
            }

//                //create a Fragment
//                Fragment detailFragment = new FragmentDetail();
//
//
//                Bundle mBundle = new Bundle();
//                mBundle.putString("arg", mDataSourceList.get(position));
//                detailFragment.setArguments(mBundle);
//
//                final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                //check if the device is landscape or portrait
//                Configuration configuration = getActivity().getResources().getConfiguration();
//                int ori = configuration.orientation;
//
//                fragmentTransaction.replace(R.id.detail_container, detailFragment);
//
//                if(ori == configuration.ORIENTATION_PORTRAIT){
//                    fragmentTransaction.addToBackStack(null);
//                }
//
//                fragmentTransaction.commit();
//
//            }
        });


//        try {
//            InputStream in=new FileInputStream(target);
//            if (in != null) {
//                try {
//                    InputStreamReader tmp=new InputStreamReader(in);
//                    BufferedReader reader=new BufferedReader(tmp);
//                    String str;
//                    StringBuilder buf=new StringBuilder();
//                    while ((str=reader.readLine()) != null) {
//                        buf.append(str);
//                        buf.append("\n");
//                    }
//                    result=buf.toString();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    in.close();
//                }
//            }
//        }
//        catch (java.io.FileNotFoundException e) {
//            // that's OK, we probably haven't created it yet
//        }
//    }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load, container, false);
        return view;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.

        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
        public void onFragmentInteraction(String id);
    }

}
