package com.example.yuichi_oba.ecclesia.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.yuichi_oba.ecclesia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 1; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("TITLE", "title" + Integer.toString(i));

            maps.add(map);
        }

        int CHILD_DATA = 3;
// 子要素全体用のリスト
        List<List<Map<String, String>>> allChildList = new ArrayList<List<Map<String, String>>>();

// 子要素として表示する文字を生成
        for (int i = 0; i < 1; i++) {
            // 各グループ別のリスト項目用のリスト
            List<Map<String, String>> childList = new ArrayList<Map<String, String>>();

            // リスト項目用データ格納
            for (int j = 0; j < CHILD_DATA; j++) {
                Map<String, String> childData = new HashMap<String, String>();
                childData.put("TITLE", "子要素" + Integer.toString(j));
                childData.put("SUMMARY", "概要" + Integer.toString(j));
                // リストに文字を格納
                childList.add(childData);
            }
            // 子要素全体用のリストに各グループごとデータを格納
            allChildList.add(childList);
        }

        // アダプタを作る
        SimpleExpandableListAdapter adapter =
                new SimpleExpandableListAdapter(
                        this,
                        maps,
                        android.R.layout.simple_expandable_list_item_1,
                        new String[] { "TITLE" },
                        new int[] { android.R.id.text1, android.R.id.text2 },
                        allChildList,
                        android.R.layout.simple_expandable_list_item_2,
                        new String[] { "TITLE", "SUMMARY" },
                        new int[] { android.R.id.text1, android.R.id.text2 }
                );
//生成した情報をセット
        ExpandableListView lv = (ExpandableListView)findViewById(R.id.elv);
        lv.setAdapter(adapter);
    }
}
