package in.icebreakerapp.icebreaker.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.icebreakerapp.icebreaker.R;

/**
 * Created by anip on 26/08/16.
 */
public class ChatFragment extends Fragment {
    public ChatFragment(){

    }
    public static ChatFragment newInstance(){
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        return rootView;
    }
}
