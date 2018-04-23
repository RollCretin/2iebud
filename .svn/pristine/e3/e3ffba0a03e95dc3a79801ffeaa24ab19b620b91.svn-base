package com.hxjf.dubei.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hxjf.dubei.R;

/**
 * Created by Chen_Zhang on 2017/6/20.
 */

public class AddLabelDialog extends Dialog {
    Context mContext;
    private EditText edittext;
    private Button negativeButton;
    private Button positiveButton;
    private TextView title;
    private TextView descript;

    public AddLabelDialog(Context context) {
        super(context);
        this.mContext = context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_add_label, null);
        title = (TextView) view.findViewById(R.id.add_label_title);
        descript = (TextView) view.findViewById(R.id.add_label_descript);
        edittext = (EditText) view.findViewById(R.id.add_label_et);
        negativeButton = (Button) view.findViewById(R.id.add_label_cacel);
        positiveButton = (Button) view.findViewById(R.id.add_label_add);
        super.setContentView(view);
    }

    public void setTitle(String str){
        title.setText(str);
    }

    public void goneDes(){
        descript.setVisibility(View.GONE);
    }

    public String getEditTextString(){
        return edittext.getText().toString();
    }
    public void setEdittextString(String str){
        edittext.setText(str);
    }

    public void setNegativeButtonName(String str){
        negativeButton.setText(str);
    }

    public void setPositiveButtonName(String str){
        positiveButton.setText(str);
    }
    public void setOnPositiveListener(View.OnClickListener listener){
        positiveButton.setOnClickListener(listener);
    }

    public void setOnNegativeListener(View.OnClickListener listener){
        negativeButton.setOnClickListener(listener);
    }

}
