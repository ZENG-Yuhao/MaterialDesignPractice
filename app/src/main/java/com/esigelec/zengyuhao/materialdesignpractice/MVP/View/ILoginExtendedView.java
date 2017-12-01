package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.ILoginExtendedPresenter;

/**
 * Created by Enzo on 16/6/1.
 */
public interface ILoginExtendedView<P extends ILoginExtendedPresenter> extends ILoginView<P> {
    void showToast(String str);
}
