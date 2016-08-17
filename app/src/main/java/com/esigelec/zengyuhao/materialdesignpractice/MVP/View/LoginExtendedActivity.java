package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.ILoginExtendedPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.ILoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.LoginExtendedPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.LoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IPresenterFactory;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IView;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.PLCManager;
import com.esigelec.zengyuhao.materialdesignpractice.R;

public class LoginExtendedActivity<P extends ILoginExtendedPresenter> extends Login3Activity<P> implements
        ILoginExtendedView<P> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPresenter will be created in the loader, it's not available in onCreate();
        //mPresenter.sayHello();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.sayHello();
    }

    @Override
    protected IPresenterFactory<? extends IPresenter> getFactory() {
        return new IPresenterFactory<ILoginExtendedPresenter>() {
            @Override
            public ILoginExtendedPresenter create() {
                return new LoginExtendedPresenter();
            }
        };
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
