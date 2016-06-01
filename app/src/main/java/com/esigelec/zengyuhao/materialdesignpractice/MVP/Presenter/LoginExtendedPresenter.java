package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.ILoginExtendedView;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.ILoginView;

/**
 * Created by Enzo on 16/6/1.
 */
public class LoginExtendedPresenter<V extends ILoginExtendedView> extends LoginPresenter<V> implements
        ILoginExtendedPresenter<V> {

    @Override
    public void sayHello() {
        mView.showToast("Hello MVP!");
    }
}
