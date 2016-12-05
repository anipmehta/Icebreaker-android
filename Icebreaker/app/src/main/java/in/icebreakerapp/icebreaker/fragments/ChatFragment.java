package in.icebreakerapp.icebreaker.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import in.icebreakerapp.icebreaker.RecyclerItemClickListener;
import in.icebreakerapp.icebreaker.adapters.HomeChatAdapter;
import in.icebreakerapp.icebreaker.helpers.MessageDb;

/**
 * Created by anip on 26/08/16.
 */
public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private HomeChatAdapter adapter;
    private MessageDb db;
    private RecyclerView.LayoutManager mLayoutManager;


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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.contact_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        db =new MessageDb(getActivity());
        adapter = new HomeChatAdapter(getActivity(),db.getChats(getActivity().getSharedPreferences("user",0).getString("enroll","")),getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                Log.i("hell_selected",

                        String.valueOf(intent.putExtra("title",db.getChats(getActivity().getSharedPreferences("user",0).getString("enroll","")).get(position).getEnroll())));
                startActivityForResult(intent,80);
            }
        }));

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 80) {
            recyclerView.removeAllViews();
            adapter = new HomeChatAdapter(getActivity(),db.getChats(getActivity().getSharedPreferences("user",0).getString("enroll","")),getActivity());
            recyclerView.setAdapter(adapter);
            Log.i("hell_on result", "entered");
        }

    }
}
