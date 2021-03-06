package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.ILoginPresenter;
import com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils.IView;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public interface ILoginView<P extends ILoginPresenter> extends IView<P> {
    void clearTextFields();

    void onLoginResult(int code, String message);

}
