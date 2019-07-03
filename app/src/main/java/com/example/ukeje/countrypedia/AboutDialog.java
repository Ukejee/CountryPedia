package com.example.ukeje.countrypedia;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/28/19
 */
public class AboutDialog extends Dialog {

    public Activity activity;
    public Dialog dialog;

    public AboutDialog(Activity activity) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_dialog);

    }

}
