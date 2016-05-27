package com.esigelec.zengyuhao.materialdesignpractice.MVP.Presenter;

import com.esigelec.zengyuhao.materialdesignpractice.MVP.View.IView;

/**
 * Basic interface of Presenters in pattern MVP
 * Created by Enzo(ZENG Yuhao) on 16/5/26.
 */
public interface IPresenter<V extends IView> {
    /**
     * The method used generics type (V) rather than an specific interface (IView), that make the type expandable,
     * otherwise, we can only apply an instance of this specific interface, and we would not be able to use those
     * methods that are newly defined in other descendant interfaces (like ILoginPresenter in this example).
     *
     * @param view the view (activity that implements IView) to be attached.
     */
    void onViewAttach(V view);

    void onViewDetach();

    void onDestroy();

    /**
     * ## Notes ##
     * When building MVP project and using generics type, there are some tips and notions:
     *
     * 1. Generics type are designed and are used only in compile-time, all generics type will be erased before
     * run-time. So, the purpose is just making sure that programmers do not make unmatched-type errors when
     * designing and programming.
     *
     * 2. In pattern MVP, since a specific presenter (ILoginPresenter) has a strong correlation with a specific view
     * (ILoginView), It'd better to make some liaison between these interfaces. Such like, We make ILoginPresnter to
     * extends IPresenter being limited by a generics type V who extends IView.
     *
     *    It's also very useful we constraint the generics type using <.. extends ..> just like we used here and in
     * {@link IView}, because we have interface method like onViewAttach() here and getPresenter() in IView. This
     * kind of binding make programmers to make less errors.
     *
     *    Once the implementations were created, the compiler will apply automatically
     * {@link com.esigelec.zengyuhao.materialdesignpractice.MVP.View.ILoginView} as a parameter of onViewAttach(),
     * and apply {@link IPresenter} as a return of getPresenter().
     *
     *
     */
}
