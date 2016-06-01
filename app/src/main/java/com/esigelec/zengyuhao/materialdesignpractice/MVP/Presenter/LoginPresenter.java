package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IView;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.ILoginView;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public class LoginPresenter <V extends ILoginView> implements ILoginPresenter<V> {
    protected V mView;

    @Override
    public void doLogin(String account, String password) {
        if (account.equals("admin") && password.equals("123456")) {
            mView.onLoginResult(0, "Login succeed.");
        } else {
            mView.onLoginResult(1, "Login failed.");
        }
        mView.clearTextFields();
    }

    @Override
    public void onViewAttach(V view) {
        mView = view;
    }


    @Override
    public void onViewDetach() {
        mView = null;
    }

    @Override
    public void onDestroy() {
        onViewDetach();
    }


}
