package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.IView;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public interface IPresenter <V extends IView> {
    void onViewAttach(V view);
    void onViewDetach();
    void onDestroy();
}
