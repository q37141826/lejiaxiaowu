package com.qixiu.adapter.myrecycler;

import android.view.View;

/**
 * Created by HuiHeZe on 2017/9/25.
 */

public interface OnRecyclerItemLongListener <T>{

     void onItemLongClick(View v, T data);

}
