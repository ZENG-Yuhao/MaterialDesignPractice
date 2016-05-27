package com.esigelec.zengyuhao.materialdesignpractice.MVP.Utils;

/**
 * Created by Enzo on 16/5/27.
 */
public interface IPresenterFactory<P extends IPresenter> {
    P create();
}
