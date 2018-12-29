package com.qixiu.lejia.core.sign;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Long on 2018/5/3
 * <pre>
 *     用户类型：普通用户，企业用户
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({UserType.PERSONAL, UserType.CORPORATE})
public @interface UserType {

    /**普通用户*/
    int PERSONAL = 1;
    /**企业用户*/
    int CORPORATE = 2;

}
