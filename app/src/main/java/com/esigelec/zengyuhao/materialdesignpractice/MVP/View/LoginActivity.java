package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.ILoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.LoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class LoginActivity extends Activity implements ILoginView {

    private ILoginPresenter mPresenter;
    private EditText editxt_account, editxt_pswd;
    private Button btn_login, btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter();

        editxt_account = (EditText) findViewById(R.id.editxt_account);
        editxt_pswd = (EditText) findViewById(R.id.editxt_pswd);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_account = editxt_account.getText().toString();
                String str_pswd = editxt_pswd.getText().toString();
                if (str_account != "" && str_pswd != "") {
                    mPresenter.doLogin(str_account, str_pswd);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTextFields();
            }
        });
    }

    @Override
    public void clearTextFields() {
        editxt_account.clearComposingText();
        editxt_pswd.clearComposingText();
    }

    @Override
    public void onLoginResult(int code, String message) {
        Toast.makeText(this, String.valueOf(code) + ": " + message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public ILoginPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onViewAttach(this);
    }

    @Override
    protected void onStop() {
        mPresenter.onViewDetach();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }


}
