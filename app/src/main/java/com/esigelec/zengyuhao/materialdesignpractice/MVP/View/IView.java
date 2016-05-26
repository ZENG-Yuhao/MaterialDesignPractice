package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.IPresenter;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public interface IView <P extends IPresenter> {
    /**
     * Normally, the presenter is private, we use this method to get the presenter.
     * @return the presenter bound of this view.
     */
    P getPresenter();
}
