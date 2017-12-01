package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.ILoginExtendedView;

/**
 * Created by Enzo on 16/6/1.
 */
public interface ILoginExtendedPresenter <V extends ILoginExtendedView> extends ILoginPresenter<V> {
    void sayHello();
}
