package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.ILoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.LoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IPresenterFactory;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IView;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.PLCManager;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class Login3Activity<P extends ILoginPresenter> extends Activity implements ILoginView<P> {
    protected static final int LOADER_ID = 111;
    protected P mPresenter;
    protected EditText editxt_account, editxt_pswd;
    protected Button btn_login, btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(getViewRes());
        init();
    }

    protected int getViewRes(){
        return R.layout.activity_login;
    }

    protected void init() {
        initLayout();
        initLoader();
    }

    protected void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, getPLCManager());
    }

    protected PLCManager<? extends IPresenter, ? extends IView> getPLCManager() {
        return new PLCManager<>(getApplicationContext(), this, getFactory());
    }

    protected IPresenterFactory<? extends IPresenter> getFactory() {
        // Factory class being used to provide a method to create instance.
        IPresenterFactory<ILoginPresenter> factory = new IPresenterFactory<ILoginPresenter>() {
            @Override
            public ILoginPresenter create() {
                return new LoginPresenter();
            }
        };
        return factory;
    }

    protected void initLayout() {
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
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
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
