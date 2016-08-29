package in.icebreakerapp.icebreaker.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import in.icebreakerapp.icebreaker.ChatActivity;
import in.icebreakerapp.icebreaker.R;
import in.icebreakerapp.icebreaker.adapters.HomeChatAdapter;
import in.icebreakerapp.icebreaker.helpers.MessageDb;

/**
 * Created by anip on 26/08/16.
 */
public class ChatFragment extends Fragment {
    private ListView recyclerView;
    private HomeChatAdapter adapter;
    private MessageDb db;

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
        Log.i("hell","entered fragment");
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = (ListView) rootView.findViewById(R.id.chat_list);
        db =new MessageDb(getActivity());
        adapter = new HomeChatAdapter(db.getChats(),getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
