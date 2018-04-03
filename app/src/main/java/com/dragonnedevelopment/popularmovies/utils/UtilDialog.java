package com.dragonnedevelopment.popularmovies.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dragonnedevelopment.popularmovies.R;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 */
public class UtilDialog {

    private static boolean isDialogVisible;

    public static Dialog dialog;

    public static boolean showDialog(String dialogMessage, Context context) {
        TextView textViewDialogTitle;

        final TextView textViewDialogCaption;
        Button buttonDialog;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);

        textViewDialogTitle = dialog.findViewById(R.id.tv_dialog_title);
        textViewDialogCaption = dialog.findViewById(R.id.tv_dialog_caption);
        buttonDialog = dialog.findViewById(R.id.dismiss_dialog_btn);

        Utils.setCustomTypeFace(context, textViewDialogCaption);
        Utils.setCustomTypeFace(context, textViewDialogTitle);
        Utils.setCustomTypeFace(context, buttonDialog);

        textViewDialogCaption.setText(dialogMessage);

        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDialogCaption.setText("");
                dialog.dismiss();
                isDialogVisible = false;
            }
        });

        dialog.show();
        isDialogVisible = true;
        return isDialogVisible;
    }

    public static void dismissDialog() {

        dialog.dismiss();
    }
}
