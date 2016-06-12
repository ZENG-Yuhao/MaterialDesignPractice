package com.esigelec.zengyuhao.materialdesignpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialogActivity extends Activity {
    private Button btn_normal_dialog, btn_custom_view_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        btn_normal_dialog = (Button) findViewById(R.id.btn_normal_dialog);
        btn_normal_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NormalDialogFragment().show(getFragmentManager(), "myNormalDialog");
            }
        });

        btn_custom_view_dialog = (Button) findViewById(R.id.btn_custom_view_dialog);
        btn_custom_view_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomViewDialogFragment().show(getFragmentManager(), "myCustomViewDialog");
            }
        });

    }

    public static class NormalDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Normal AlertDialog")
                    .setMessage("This is a normal AlertDialog")
                    .setPositiveButton("I know", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "I know", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("I don't care", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "I don't care", Toast.LENGTH_SHORT).show();
                        }
                    });
            return builder.create();
        }
    }

    public static class CustomViewDialogFragment extends DialogFragment {
        private OnActionsListener mOnActionsListener;
        protected EditText editxt_account, editxt_pswd;
        protected Button btn_login, btn_clear;

//        @Override
//        public void onAttach(Context context) {
//            super.onAttach(context);
//            try {
//                mOnActionsListener = (OnActionsListener) context;
//            } catch (ClassCastException e){
//                throw new ClassCastException(context.toString()+" does not implement OnActionsListener.");
//            }
//        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.activity_login, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Normal AlertDialog")
                    .setMessage("This is a normal AlertDialog")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Save", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setView(view);


            editxt_account = (EditText) view.findViewById(R.id.editxt_account);
            editxt_pswd = (EditText) view.findViewById(R.id.editxt_pswd);
            btn_login = (Button) view.findViewById(R.id.btn_login);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "login clicked", Toast.LENGTH_SHORT).show();
                }
            });
            return builder.create();
        }

        public interface OnActionsListener {
            void onLogin(String account, String password);
            void onDissmiss();
        }
    }
}
