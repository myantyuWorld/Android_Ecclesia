package com.example.yuichi_oba.ecclesia.tools;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;

/**
 * Created by Yuichi-Oba on 2017/07/24.
 */

public class Util {

    /***
     * 項目引数に渡すと、その項目のインデックスを返すUtilityメソッド
     * @param spinner   スピナー
     * @param item      インデックスを検索したい文字列
     * @return          引数の文字列がそのスピナーの何番目にあるかを返す
     */
    public static int setSelection(Spinner spinner, String item)
    {
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(item)) {
                return i;
            }
        }
        // なければ０を返す
        return 0;
    }

}
