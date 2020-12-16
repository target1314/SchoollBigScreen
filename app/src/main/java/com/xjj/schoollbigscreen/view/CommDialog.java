package com.xjj.schoollbigscreen.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xjj.schoollbigscreen.R;
import com.xjj.schoollbigscreen.utils.LoggerUtils;

import androidx.annotation.NonNull;

/**
 * TODO 类描述:自定义Dialog，实现了Dialog提示框，
 */
public class CommDialog extends Dialog {
    private static int messageGravity = Gravity.LEFT;
    private static Button negativeButton;
    private static Button positiveButton;


    public CommDialog(Context context, int theme) {
        super(context, theme);
    }

    public CommDialog(Context context) {
        super(context);
    }

    private static View viewDialogView;

    public static class Builder {

        private Context context;
        private String title;
        private CharSequence message;
        private String positiveButtonText;
        private String negativeButtonText;

        // private boolean isCancelable = true;
        private boolean isPositiveDismiss = true;// 默认点击确定按钮关闭对话框

        private OnClickListener positiveButtonClickListener, negativeButtonClickListener;
        private OnCancelListener onCancelListener;

        public Builder(Context context) {
            this.context = context;
        }


        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = context.getText(message);
            return this;
        }

        public Builder setMessageGravity(int gravity) {
            messageGravity = gravity;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener cancelListener) {
            this.onCancelListener = cancelListener;
            return this;
        }

        // public Builder setCancelable(boolean flag) {
        // this.isCancelable = flag;
        // return this;
        // }

        // 设置点击确定按钮关闭对话框
        public Builder setPositiveDismiss(boolean flag) {
            this.isPositiveDismiss = flag;
            return this;
        }

        public CommDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
             CommDialog dialog = new CommDialog(context, R.style.MyDialog);
            viewDialogView = inflater.inflate(R.layout.dialog, null);
            View line = viewDialogView.findViewById(R.id.line);
            DisplayMetrics dm = new DisplayMetrics();
            dialog.addContentView(viewDialogView, new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title

            if (TextUtils.isEmpty(title)) {
                ((TextView) viewDialogView.findViewById(R.id.textViewTitle)).setVisibility(View.GONE);
            } else {
                ((TextView) viewDialogView.findViewById(R.id.textViewTitle)).setVisibility(View.VISIBLE);
                ((TextView) viewDialogView.findViewById(R.id.textViewTitle)).setTextSize(40);
                ((TextView) viewDialogView.findViewById(R.id.textViewTitle)).setText(title);
            }
            if (positiveButtonText != null) {
                positiveButton = (Button) viewDialogView.findViewById(R.id.dialog_ok);
                positiveButton.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            if (isPositiveDismiss) {
                                dialog.dismiss();
                            }
                        }
                    });
                }
                if (negativeButtonText == null) {
                    line.setVisibility(View.GONE);
                    positiveButton.setBackgroundResource(R.drawable.selector_button_dialog_single);
                }
            } else {
                // if no confirm button just set the visibility to GONE
                viewDialogView.findViewById(R.id.dialog_ok).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                negativeButton = (Button) viewDialogView.findViewById(R.id.dialog_cancel);
                negativeButton.setFocusable(true);
                negativeButton.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
                if (positiveButtonText == null) {
                    line.setVisibility(View.GONE);
                    negativeButton.setBackgroundResource(R.drawable.selector_button_dialog_single);
                }
            } else {
                // if no confirm button just set the visibility to GONE
                viewDialogView.findViewById(R.id.dialog_cancel).setVisibility(View.GONE);
            }

            // set the content message
            if (message != null) {

//				((TextView) viewDialogView.findViewById(R.id.text_dialog_tipText)).setGravity(messageGravity);
                ((TextView) viewDialogView.findViewById(R.id.text_dialog_tipText)).setText(message);
            }
            dialog.setContentView(viewDialogView);
            dialog.getWindow().setGravity(Gravity.CENTER);
            // dialog.setCancelable(isCancelable);
            // dialog.setCanceledOnTouchOutside(isCancelable);
            // 设置对话框的宽度为屏幕的8/10.
//			setDialogWidth(dialog, 0.8F);
            return dialog;
        }

    }

    public static Button getNegativeButton() {
        return negativeButton;
    }


    public static Button getPositiveButton() {
        return positiveButton;
    }


    @Override
    public void show() {
        super.show();
    }

    /**
     * 设置对话框的宽度为(屏幕宽度*factor).
     *
     * @param dialog
     * @param factor 对话框宽度站屏幕宽度的比例[0, 1].
     */
    public static void setDialogWidth(Dialog dialog, float factor) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        dialogWindow.getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * factor);
        dialogWindow.setAttributes(lp);
    }
}
