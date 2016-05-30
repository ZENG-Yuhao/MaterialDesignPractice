package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.ILoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.LoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IPresenterFactory;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.PLCManager;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class Login3Activity extends Activity implements ILoginView {
    private static final int LOADER_ID = 111;
    private ILoginPresenter mPresenter;
    private EditText editxt_account, editxt_pswd;
    private Button btn_login, btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Factory class being used to provide a method to create instance.
        IPresenterFactory<ILoginPresenter> factory = new IPresenterFactory<ILoginPresenter>() {
            @Override
            public ILoginPresenter create() {
                return new LoginPresenter();
            }
        };
        // There is an unknown error, we can not apply "this" which will be recognized as Login3Activity for 2nd
        // param.  It must be cast to ILoginView
        PLCManager<ILoginPresenter, ILoginView> plc = new PLCManager<>(this, (ILoginView) this, factory);

        getLoaderManager().initLoader(LOADER_ID, null, plc);


        editxt_account = (EditText) findViewById(R.id.editxt_account);
        editxt_pswd = (EditText) findViewById(R.id.editxt_pswd);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_account = editxt_account.getText().toString();
                String str_pswd = editxt_pswd.getText().toString();
                if (!str_account.equals("") && !str_pswd.equals("")) {
                    mPresenter.doLogin(str_account, str_pswd);
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
    public void setPresenter(ILoginPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onViewAttach(this);
    }

    @Override
    protected void onStop() {
        // In fact, for an activity, we need just one presenter in all his lifecycle, and normally it won't change
        // the view so there is no need to detach the view/activity when onStop()
        mPresenter.onViewDetach();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

}
