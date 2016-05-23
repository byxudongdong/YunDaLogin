package com.Fajian;

/**
 * Created by byxdd on 2016/5/23 0023.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.YunDaLogin.R;
import com.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class zhongzhuan_main extends Activity {
    public int unSend_count_msg = 0;
    DatabaseHelper dbsqlite;
    private SQLiteDatabase db = null;
    private Cursor cursor = null;
    //private SimpleCursorAdapter adapter = null;

    private ListView lv;
    private List<Map<String, Object>> data;
    MyAdapter adapter;

    Button fajian_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhongzhuan_main);
        lv = (ListView)this.findViewById(R.id.zhongzhuan_main_list);
        //获取将要绑定的数据设置到data中
        data = getdbData();
        adapter = new MyAdapter(this);
        lv.setAdapter(adapter);

        fajian_back = (Button) findViewById(R.id.zhongzhuan_main_back);

        //处理Item的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> getObject = data.get(position);	//通过position获取所点击的对象
                //int infoId = getObject.;	//获取信息id
                String infoTitle = (String) getObject.get("title");	//获取信息标题
                String infoDetails = String.valueOf( getObject.get("info"));	//获取信息详情

                //Toast显示测试
                Toast.makeText(zhongzhuan_main.this, "信息ID:"+ position + infoTitle, Toast.LENGTH_SHORT).show();
                switch ( Integer.parseInt( String.valueOf( getObject.get("info"))))
                {
                    case 1:
                        startActivity( new Intent( zhongzhuan_main.this,
                                com.Fajian.zhongzhuan_fachu.class));
                        break;
                }
            }
        });

        //长按菜单显示
        lv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu conMenu, View view , ContextMenu.ContextMenuInfo info) {
                conMenu.setHeaderTitle("菜单");
                conMenu.add(0, 0, 0, "清空列表");
                conMenu.add(0, 1, 1, "增加条目");
                conMenu.add(0, 2, 2, "删减条目");

            }
        });


//            public void onClick(View v) {
//                showDialog(zhongzhuan_main.this, "确认退出当前界面？");
//            }
//        });
    }

    public void zhongzhuan_main_back(View target)
    {
        zhongzhuan_main.this.finish();
    }

//    public void showDialog(Context context, final String msg) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(msg);
//        builder.setTitle("询问");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (msg.equals("确认退出当前界面？")) {
//                    if (unSend_count_msg != 0)
//                        showDialog(zhongzhuan_main.this, "还有未上传数据，请上传");
//                    else {
//                        dialog.dismiss();
//                        zhongzhuan_main.this.finish();
//                    }
//                } else if (msg.equals("确认更改站点？")) {
////                    show_dialog_ing = true;
////                    dialog_next_click_button(NEXT_SITECODE);
//                } else if (msg.equals("确认更改任务单？")) {
////                    show_dialog_ing = true;
////                    dialog_work_click_button();
//                } else if (msg.equals("还有未上传数据，请上传")) {
////                    befor_destroy();
////                    send_up_destroy = true;
//                }
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (msg.equals("还有未上传数据，请上传")) {
//                    zhongzhuan_main.this.finish();
//                } else if (msg.equals("确认更改站点？")) {
////                    nextname.setText(PRE_STATION);
////                    show_dialog_ing = true;
////                    check_sitecode_flag = true;
//                } else if (msg.equals("确认更改任务单？")) {
////                    workorder.setText(WORK_PRE);
////                    check_sitecode_flag = true;
////                    show_dialog_ing = true;
//                } else {
////                    show_dialog_ing = true;
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//    public boolean KEYCODE_BACK_flag = false;
//    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (!KEYCODE_BACK_flag) {
//                showDialog(zhongzhuan_main.this, "确认退出当前界面？");
//            }
//            return KEYCODE_BACK_flag ? true : super.onKeyDown(keyCode, event);
//        }
//        return super.onKeyDown(keyCode, event);
//    };
    private List<Map<String, Object>> getdbData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        int db_id,db_number;
        String db_name;
        //每个程序都有自己的数据库
        //通过openOrCreateDatabase来打开或创建一个数据库,返回SQLiteDatabase对象
        /**
         *  openOrCreateDatabase(String name,int mode,SQLiteDatabase.CursorFactory factory)
         *  name: 数据库名
         *  mode: 数据库权限，MODE_PRIVATE为本应用程序私有，MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE分别为全局可读和可写。
         *  factory: 可以用来实例化一个cusor对象的工厂类
         */

        db = openOrCreateDatabase("zhongzhuan_main.db",MODE_PRIVATE,null);
        //创建一个表
        db.execSQL("create table if not exists userTb (" +
                "_id integer primary key," +
                "name text not null," +
                "number integer not null," +
                "addition text not null)");
        //向表中插入记录
        db.execSQL("insert into userTb (name,number,addition) values ('中转发出[1]',1,'null')");
        db.execSQL("insert into userTb (name,number,addition) values ('格口查询[2]',2,'null')");
//        db.execSQL("insert into userTb (name,number,addition) values ('中转发出(称重)[3]',3,'null')");
//        db.execSQL("insert into userTb (name,number,addition) values ('中转发出(保价)[4]',4,'null')");
        //db.execSQL("insert into userTb (name,number,addition) values ('集包扫描',105,'null')");
//        db.execSQL("insert into userTb (name,number,addition) values ('集包扫描',106,'null')");
//        db.execSQL("insert into userTb (name,number,addition) values ('装车扫描',107,'null')");
//        db.execSQL("insert into userTb (name,number,addition) values ('回流件扫描',108,'null')");
//        db.execSQL("insert into userTb (name,number,addition) values ('车辆调度',109,'null')");



        //dbsqlite.getWritableDatabase();
        //db= (new dbsqlite(getApplicationContext())).getWritableDatabase();
        //Cursor为查询结果对象，类似于JDBC中的ResultSet
        Cursor queryResult = db.rawQuery("select * from userTb", null);
        if (queryResult != null) {
            while ( queryResult.moveToNext() )
            {
                db_id = queryResult.getInt(queryResult.getColumnIndex("_id"));
                db_name = queryResult.getString(queryResult.getColumnIndex("name"));
                db_number = queryResult.getInt(queryResult.getColumnIndex("number"));
                Log.i("info", "id: " + db_id
                        + " 流程: " + db_name
                        + " 序号: " + db_number
                        + " 备注: " + queryResult.getString(queryResult.getColumnIndex("addition")));

                map = new HashMap<String, Object>();
                map.put("image", R.mipmap.lan);
                map.put("title", db_name);
                map.put("info", db_number);
                list.add(map);
                db.delete("userTb","_id=?",new String[]{String.valueOf(db_id)} );
            }
            //关闭游标对象
            queryResult.close();
        }
        //关闭数据库
        db.close();

//        for(int i=0;i<88;i++)
//        {
//
//        }
        return list;
    }

    //长按菜单处理函数
    public boolean onContextItemSelected(MenuItem aItem) {
        int MID;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)aItem.getMenuInfo();
        Map<String, Object> map;

        MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值

        switch (aItem.getItemId()) {
            case 0:
                data.removeAll(data);
                map = new HashMap<String, Object>();
                map.put("image", R.mipmap.hong);
                map.put("title", "条码" + "add");
                map.put("info", "问题件..." + "add");
                data.add(0,map);
                adapter.notifyDataSetChanged();
                lv.invalidate();
                Toast.makeText(zhongzhuan_main.this, "清空列表",Toast.LENGTH_SHORT).show();
                return true;
            case 1:
                map = new HashMap<String, Object>();
                map.put("image", R.mipmap.hong);
                map.put("title", "条码" + "add");
                map.put("info", "问题件..." + "add");
                data.add(0,map);
                adapter.notifyDataSetChanged();
                lv.invalidate();
                Toast.makeText(zhongzhuan_main.this, "增加条码",Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                data.remove(MID);
                adapter.notifyDataSetChanged();
                lv.invalidate();
                Toast.makeText(zhongzhuan_main.this, "删除条码",Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<88;i++)
        {
            map = new HashMap<String, Object>();
            map.put("image", R.mipmap.lan);
            map.put("title", "条码" + i);
            map.put("info", "包裹信息..." + i);
            list.add(map);
        }
        return list;
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public ImageView image;
        public TextView title;
        public TextView info;
    }

    public class MyAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater = null;
        private MyAdapter(Context context)
        {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }
        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        public int counter=0;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder.image = (ImageView)convertView.findViewById(R.id.image);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.image.setBackgroundResource((Integer)data.get(position).get("image"));
            holder.title.setText((String)data.get(position).get("title"));
            holder.info.setText( String.valueOf(data.get(position).get("info") ) );
            Log.i("Count", String.valueOf(counter ++));
            return convertView;
        }

    }

}

