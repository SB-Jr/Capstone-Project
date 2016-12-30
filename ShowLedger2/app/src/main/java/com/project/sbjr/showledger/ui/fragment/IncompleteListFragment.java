package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;


public class IncompleteListFragment extends Fragment {
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    // TODO: Rename and change types of parameters
    private String userUid;
    private String showType;

    private OnIncompleteFragmentInteractionListener mListener;

    public IncompleteListFragment() {
        // Required empty public constructor
    }

    public static IncompleteListFragment newInstance(String useruid, String showtype) {
        IncompleteListFragment fragment = new IncompleteListFragment();
        Bundle args = new Bundle();
        fragment.userUid = useruid;
        fragment.showType = showtype;
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
        return inflater.inflate(R.layout.fragment_incomplete_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIncompleteFragmentInteractionListener) {
            mListener = (OnIncompleteFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnIncompleteFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnIncompleteFragmentInteractionListener {
        void onIncompleteFragmentItemClickListener(TvShowModel tvShowModel);
    }
}
