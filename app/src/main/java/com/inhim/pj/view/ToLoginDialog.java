package com.inhim.pj.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.inhim.pj.R;

public class ToLoginDialog extends Dialog {
    public ToLoginDialog(Context context, int theme) {
        super(context, theme);
    }
    public static class Builder {
        private Context context;
        private String title;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener callButtonClickListener;
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(
                OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }
        public Builder setCallButton(int callButtonText,
                                         OnClickListener listener) {
            this.callButtonClickListener = listener;
            return this;
        }

        public Builder setCallButton(
                OnClickListener listener) {
            this.callButtonClickListener = listener;
            return this;
        }

        public ToLoginDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme  
            final ToLoginDialog dialog = new ToLoginDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_to_login, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title  
            ((TextView) layout.findViewById(R.id.content)).setText(title);
            // set the confirm button
                if (positiveButtonClickListener != null) {
                    ( layout.findViewById(R.id.btn_ok))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            if (positiveButtonClickListener != null) {
                ( layout.findViewById(R.id.btn_call))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }

            dialog.setContentView(layout);  
            return dialog;  
        }  
    }  
}
