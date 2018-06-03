package com.gotowork.msghash;

import java.security.KeyPair;
import java.util.List;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bitcoinj.core.ECKey;

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Message> objects;
    ECKey keyPair;
    MessageAdapter(Context context, List<Message> messages, ECKey k) {
        ctx = context;
        keyPair = k;
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
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.messageView);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(ctx.getString(R.string.message));
                final String address = Sawtooth.getAddress(message.getHash());
                String dialogMessage = ctx.getString(R.string.hash) + ": \n" + message.getHash() + "\n\n" + ctx.getString(R.string.address) + ": \n" + address;
                dialogMessage += "\n\n" + message.getText()+message.getName()+message.getFullTime(); //TODO: remove this
                builder.setMessage(dialogMessage);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton(R.string.copy_hash, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager)ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", message.getHash());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ctx, R.string.copied, Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNeutralButton(R.string.copy_address, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager)ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", address);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ctx, R.string.copied, Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
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
                if (message.pin(keyPair)) {
                    buttonDelete.setEnabled(false);
                    buttonPin.setEnabled(false);
                    update();
                }
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