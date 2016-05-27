package com.esigelec.zengyuhao.materialdesignpractice.MVP.View;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter.IPresenter;

/**
 * Basic interface of Views in pattern MVP
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public interface IView<P extends IPresenter> {
    /**
     * Normally, the presenter is private, we use this method to get the presenter.
     * <p/>
     * The method used generics type (P) rather than an specific interface (IPresenter), that make the type expandable,
     * otherwise, we can only apply an instance of this specific interface, and we would not be able to use those
     * methods that are newly defined in other descendant interfaces (like ILoginView in this example).
     *
     * @return the presenter being bound to this view.
     */
    P getPresenter();
}
