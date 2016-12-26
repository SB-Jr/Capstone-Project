package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sbjr.showledger.R;


public class WatchedListFragment extends Fragment {
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnFragmentInteractionListener mListener;

    public WatchedListFragment() {
        // Required empty public constructor
    }

    public static WatchedListFragment newInstance(String useruid, String showtype) {
        WatchedListFragment fragment = new WatchedListFragment();
        Bundle args = new Bundle();
        args.putString(USER_UID, useruid);
        args.putString(SHOW_TYPE, showtype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            showType = getArguments().getString(SHOW_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watched_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavigationDrawerFragmentListener");
        }*/
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
