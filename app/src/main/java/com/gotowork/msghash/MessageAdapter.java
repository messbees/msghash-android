package com.gotowork.msghash;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Message> objects;

    MessageAdapter(Context context, List<Message> messages) {
        ctx = context;
        objects = messages;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }

        final Message message = getMessage(position);

        final Button buttonDelete = (Button) view.findViewById(R.id.buttonDelete);
        final Button buttonPin = (Button) view.findViewById(R.id.buttonPin);
        ((TextView) view.findViewById(R.id.messageName)).setText(message.getName());
        ((TextView) view.findViewById(R.id.messageText)).setText(message.getText());
        if (message.checkOld())
            ((TextView) view.findViewById(R.id.messageTime)).setText(message.getFullTime());
        else
            ((TextView) view.findViewById(R.id.messageTime)).setText(message.getTime());

        if (message.checkPinned()) {
            buttonDelete.setEnabled(false);
            buttonPin.setEnabled(false);
            update();
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!message.checkPinned()) {
                    objects.remove(message);
                    update();
                    message.delete();
                }
            }
        });

        buttonPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.pin();
                buttonDelete.setEnabled(false);
                buttonPin.setEnabled(false);
                update();
            }
        });

        return view;
    }

    Message getMessage(int position) {
        return ((Message) getItem(position));
    }

    public void update() {
        notifyDataSetChanged();
    }
}