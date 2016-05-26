package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.IView;

/**
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public interface IPresenter<V extends IView> {
    /**
     * The method used generics type (V) rather than an specific interface (IView), that make the type expandable,
     * otherwise, we can only apply an instance of this specific interface, and we would not be able to use those
     * methods that are newly defined in other descendant interfaces (like ILoginPresenter in this example).
     */
    void onViewAttach(V view);

    void onViewDetach();

    void onDestroy();
}
