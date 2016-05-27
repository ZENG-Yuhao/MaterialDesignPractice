package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.Utils;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.IPresenter;

/**
 * Created by Enzo on 16/5/27.
 */
public interface IPresenterFactory<P extends IPresenter> {
    P create();
}
