package com.k2.mobile.app.controller.activity.menu.feedback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.IntentConstant;
import com.k2.mobile.app.model.album.ImageGridAdapter;
import com.k2.mobile.app.model.album.ImageItem;
import com.k2.mobile.app.model.album.ImageGridAdapter.TextCallback;
import com.k2.mobile.app.model.event.SelectEvent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;


public class ImageGridActivity extends Activity implements OnTouchListener {
    private List<ImageItem> dataList = null;
    private GridView gridView = null;
    private TextView title = null;
    private static TextView cancel = null;
    private static TextView finish = null;
    private TextView preview = null;
    private String name = null;
    private ImageView leftBtn = null;
    private static Context context = null;
    private static ImageGridAdapter adapter = null;
    private CheckBox checkBox = null;
	private int canSelectCount;
	private static String confimStr;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                	Toast.makeText(ImageGridActivity.this,
                            getString(R.string.max_check) + canSelectCount + getString(R.string.num_photo),
                                    Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_FLING:
                    adapter.lock();
                    break;
                case OnScrollListener.SCROLL_STATE_IDLE:
                    adapter.unlock();
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    adapter.lock();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
        }
    };
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_activity_image_grid);
        context = this;
        name = (String) getIntent().getSerializableExtra(
                IntentConstant.EXTRA_ALBUM_NAME);
        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                IntentConstant.EXTRA_IMAGE_LIST);
        canSelectCount = getIntent().getIntExtra(IntentConstant.EXTRA_IMAGE_LIST_COUNT, 0);
        initView();
        initAdapter();
    }

    private void initAdapter() {
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        adapter.setCanSelectCount(canSelectCount);
        adapter.setTextCallback(new TextCallback() {
            @Override
			public void onListen(int count) {
            	setSureText(count);
            }
        });
        gridView.setAdapter(adapter);
        gridView.setOnScrollListener(onScrollListener);
    }

    private void initView() {
    	confimStr = getString(R.string.confim);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });

        title = (TextView) findViewById(R.id.base_fragment_title);
        if (name.length() > 12) {
            name = name.substring(0, 11) + "...";
        }
        title.setText(name);
        leftBtn = (ImageView) findViewById(R.id.back_btn);
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageGridActivity.this.finish();
            }
        });

        //取消
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setSelectMap(null);
                ImageGridActivity.this.finish();
            }
        });

        //确定
        finish = (TextView) findViewById(R.id.finish);
        finish.setOnClickListener(new OnClickListener() {

            @Override
			public void onClick(View v) {
                if (adapter.getSelectMap().size() > 0) {
                    List<ImageItem> itemList = new ArrayList<ImageItem>();
                    Iterator<Integer> iter = adapter.getSelectMap().keySet()
                            .iterator();

                    for(Map.Entry<Integer,ImageItem> entity :adapter.getSelectMap().entrySet()){
                        int position = entity.getKey();
                        ImageItem imageItem = entity.getValue();
                    }

                    while (iter.hasNext()) {
                        int position = iter.next();
                        ImageItem imgItem = adapter.getSelectMap()
                                .get(position);
                        itemList.add(imgItem);
                    }
                    EventBus.getDefault().post(new SelectEvent(SelectEvent.Event.SELECT_PHOTO_LIST,itemList));
                    ImageGridActivity.this.setResult(RESULT_OK, null);
                }
                  ImageGridActivity.this.finish();
            }
        });

        //预览
        preview = (TextView) findViewById(R.id.preview);
        preview.setOnClickListener(new OnClickListener() {

            @Override
			public void onClick(View v) {
                //选择的数量
                if (adapter.getSelectMap().size() > 0) {
                    Intent intent = new Intent(ImageGridActivity.this,
                            PreviewActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ImageGridActivity.this,
                            getString(R.string.have_not_check_photo), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        setAdapterSelectedMap(null);
        super.onStop();
    }

    public static void setSureText(int selNum) {
        if (selNum == 0) {
            finish.setText(confimStr);
        } else {
            finish.setText(confimStr+"(" + selNum + ")");
        }
    }

    public static void setAdapterSelectedMap(Map<Integer, ImageItem> map) {
        Iterator<Integer> it = adapter.getSelectMap().keySet().iterator();
        if (map != null) {
            while (it.hasNext()) {
                int key = it.next();
                if (map.containsKey(key)) {
                    adapter.updateSelectedStatus(key, true);
                } else {
                    adapter.updateSelectedStatus(key, false);
                }
            }
            adapter.setSelectMap(map);
            adapter.setSelectTotalNum(map.size());
        } else {
            while (it.hasNext()) {
                int key = it.next();
                adapter.updateSelectedStatus(key, false);
            }
            adapter.setSelectMap(null);
            adapter.setSelectTotalNum(0);
        }
        adapter.notifyDataSetChanged();
    }

    public static ImageGridAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                adapter.unlock();
                break;
        }
        return false;
    }
}
