package com.example.sample_recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class ItemData{
    public int number;
    public String label;
}

class ItemAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    interface OnItemClickListener {
        void onItemClick(ItemData item);
    }

    //表示用データの保存用
    List<ItemData> mItemList;
    public void setItemList(List<ItemData> itemList){
        mItemList = itemList;
    }

    //アイテムがタップされた時のイベント処理用
    OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //レイアウトを読み出す
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        view.setOnClickListener(this);  //アイテムがタップされたイベントを拾う

        //一般的なやり方だと、下の部分でアイテムレイアウト内の項目のインスタンスを取り出しておくことになるが、
        //後から取り出しても大して遅くはならないので、あえて処理しない
        return new RecyclerView.ViewHolder(view){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //番号に対応したアイテムデータを取り出す
        ItemData item = mItemList.get(i);
        viewHolder.itemView.setTag(item);

        //レイアウトにデータを出力
        TextView no = viewHolder.itemView.findViewById(R.id.item_no);
        TextView label = viewHolder.itemView.findViewById(R.id.item_label);
        no.setText(String.valueOf(item.number));
        label.setText(item.label);

    }

    @Override
    public int getItemCount() {
        return mItemList!=null?mItemList.size():0;
    }

    @Override
    public void onClick(View view) {
        ItemData item = (ItemData) view.getTag();
        if(mItemClickListener != null)
            mItemClickListener.onItemClick(item);

    }
}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //サンプル用データ(ひたすら足し算)を作成
        ArrayList<ItemData> itemList = new ArrayList();
        for(int i=1,a=0;i<=100;i++) {
            ItemData item = new ItemData();
            item.number = i;
            item.label = String.format("%d+%d=%d",a,i,a+=i);
            itemList.add(item);
        }

        //アダプターの作成
        ItemAdapter adapter = new ItemAdapter();
        adapter.setItemList(itemList);
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemData item) {
                Toast.makeText(MainActivity.this,
                        String.format("%d番の%sが押されました",item.number,item.label),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //RecyclerViewの処理
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //縦方向にアイテムを表示
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerViewにアダプターを設定
        recyclerView.setAdapter(adapter);
    }
}
