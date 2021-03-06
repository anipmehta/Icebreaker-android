package in.icebreakerapp.icebreaker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.IcebreakerNotification;
import in.icebreakerapp.icebreaker.R;

/**
 * Created by siddharth on 23-08-2016.
 */
public class ChatAdapter extends BaseAdapter {

    private final List<IcebreakerNotification> chatMessages;
    private Activity context;
    private String title;
    MessageDb db;

    public ChatAdapter(Activity context, List<IcebreakerNotification> chatMessages,String title) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.title = title;
        db = new MessageDb(this.context);
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public IcebreakerNotification getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        IcebreakerNotification chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        boolean myMsg = chatMessage.getIsme() ;//Just a dummy check
        //to simulate whether it me or other sender
        SharedPreferences sp = context.getSharedPreferences("user", 0);
        if(db.lastRead()==chatMessage.getId())
            holder.unread.setVisibility(View.VISIBLE);
        else holder.unread.setVisibility(View.GONE);
        db.updateRead(chatMessage.getId());
        Log.i("hell","kjkjk"+chatMessage.getFrom()+chatMessage.getTo());
        if(chatMessage.getDeliver()==1)
        holder.txtInfo.setText("Delivered");


        else if(chatMessage.getDeliver()==0)
            holder.txtInfo.setText("Sent");

        if(chatMessage.getSendType()==0) {
            setAlignment(holder, true);
            holder.txtInfo.setVisibility(View.GONE);
        }
        else {
            holder.txtInfo.setVisibility(View.VISIBLE);
            if(chatMessage.getDeliver()==1)
                holder.txtInfo.setText("Delivered");

            else if(chatMessage.getDeliver()==0)
                holder.txtInfo.setText("Sent");
            setAlignment(holder, false);
        }
        if(compareDate(chatMessage.getTime()))
            holder.time.setText(convertDate(chatMessage.getTime(),"hh:mm a"));
        else
            holder.time.setText(convertDate(chatMessage.getTime(),"dd/MM/yy ")+"  "+convertDate(chatMessage.getTime(),"hh:mm a"));
            holder.contentWithBG.setVisibility(View.VISIBLE);
            holder.txtMessage.setText(chatMessage.getMessage());
            holder.txtMessage.setMovementMethod(LinkMovementMethod.getInstance());

        //        holder.txtInfo.setText(chatMessage.getDate());

        return convertView;
    }

    public void add(IcebreakerNotification message) {
        chatMessages.add(message);
    }

    public void add(List<IcebreakerNotification> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (!isMe) {

            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);
            int tint = Color.parseColor("#0000FF"); // R.color.blue;

            holder.contentWithBG.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
//            holder.contentWithBG.getBackground().setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        holder.time = (TextView) v.findViewById(R.id.time);
        holder.unread = (TextView) v.findViewById(R.id.unread);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public TextView time;
        public TextView unread;
    }
    public String convertDate(long dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }
    public boolean compareDate(long date){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date).equals(fmt.format(System.currentTimeMillis()));
    }
}