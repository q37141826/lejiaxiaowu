package com.qixiu.lejia.mvp;

/**
 * Created by Long on 2017/5/18
 */
public final class PresenterUtil {

    private PresenterUtil() {
    }

    public static void destroy(BasePresenter presenter) {
        if (presenter != null) {
            presenter.onDestroy();
        }
        presenter = null;
    }

}
