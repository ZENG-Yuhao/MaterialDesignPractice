package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.ILoginView;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public interface ILoginPresenter extends IPresenter<ILoginView> {
    void doLogin(String account, String password);
}
