package com.k2.mobile.app.view.widget;

import android.content.Context; 
import android.util.AttributeSet; 
import android.view.Gravity; 
import android.view.LayoutInflater; 
import android.view.View; 
import android.widget.AdapterView; 
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.ArrayAdapter; 
import android.widget.AutoCompleteTextView; 
import android.widget.ImageView; 
import android.widget.LinearLayout; 
import android.widget.ScrollView; 
import android.widget.TextView; 
 


import java.util.ArrayList; 
import java.util.List; 

import com.k2.mobile.app.R;
 
public class EmailAddrScrollView extends ScrollView implements OnItemClickListener{ 
 
    private Context mContext; 
    private LayoutInflater mInflater; 
 
    private LinearLayout mainLayout;                    //ScrollView下的主LinearLayout 
    private LinearLayout lastLinearLayout;          //记录最后一个LinearLayout 
    private List<LinearLayout> subLayoutList = new ArrayList<LinearLayout>();  //LinearLayout数组(mainLayout除外) 
 
    private AutoCompleteTextView autoCompleteTv;        //自动匹配输入框 
    private int autoCompleteTvWidth = 30; 
 
    private int lineLayoutIndex = -1;                   //主LinaerLayout下的子LinearLayout总数:也就是总行数 
    private int lineWidth; 
 
    private int itemLayoutResourceId;               //每行的LinearLayout的布局文件id 
    private int currLineWidth = 0; 
    private boolean haveNewLine = true; 
 
    public EmailAddrScrollView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
        init(context); 
    } 
 
    public EmailAddrScrollView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        init(context); 
    } 
 
    public EmailAddrScrollView(Context context) { 
        super(context); 
        init(context); 
    } 
 
    /**
     * 初始化ScrollView下的主LinearLayout,第一个LinearLayout,和AutoCompleteTextView
     * @param context
     */ 
    private void init(Context context){ 
        mContext = context; 
        mInflater = LayoutInflater.from(context); 
 
        mainLayout  = new LinearLayout(mContext); 
        mainLayout.setOrientation(LinearLayout.VERTICAL); 
        mainLayout.setLayoutParams( new LinearLayout.LayoutParams( 
                        android.view.ViewGroup.LayoutParams.FILL_PARENT, 
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT 
                )); 
 
        this.addView(mainLayout); 
 
        //创建第一个horizontal 的 LinaerLayout 
        LinearLayout firstLayout = newSubLinearLayout(); 
        subLayoutList.add(firstLayout); 
        lastLinearLayout = firstLayout; 
        lastLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_gray)); 
 
        //将AutoCompleteTextView加入到第一个LinearLayout 
        autoCompleteTv = new AutoCompleteTextView(mContext); 
        autoCompleteTv.setOnItemClickListener(this); 
        autoCompleteTv.setWidth(autoCompleteTvWidth); 
        firstLayout.addView(autoCompleteTv); 
 
        //将创建的第一个linearLayout 加入到mainLayout中 
        mainLayout.addView(firstLayout); 
    } 
 
    /**
     * 新建一行:新建一个LinearLayout
     * @return
     */ 
    private LinearLayout newSubLinearLayout(){ 
        LinearLayout layout = new LinearLayout(mContext); 
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams( 
                android.view.ViewGroup.LayoutParams.FILL_PARENT, 
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT); 
        layout.setOrientation(LinearLayout.HORIZONTAL); 
        layout.setLayoutParams(lp1); 
        layout.setGravity(Gravity.CENTER_VERTICAL); 
 
        lineLayoutIndex++; 
        return layout; 
    } 
 
    public void setHeight(int height){ 
 
    } 
 
    /**
     * 设置Item布局资源id
     * @param resourceId
     */ 
    public void setItemLayout(int resourceId){ 
        this.itemLayoutResourceId = resourceId; 
    } 
 
 
    /**
     * 初始化AutoCompleteTextView的数据源
     * @param data
     */ 
    public void initAutoCompleteTextView(String[] data){ 
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, data); 
        autoCompleteTv.setAdapter(adapter); 
    } 
 
    /**
     *
     * 重新添加所有的Item
     *
     * @param itemList
     */ 
    private void reAddAllItem(List<View> itemList){ 
        LinearLayout firstLayout = newSubLinearLayout(); 
        subLayoutList.add(firstLayout); 
        lastLinearLayout = firstLayout; 
        mainLayout.addView(firstLayout); 
        currLineWidth = 0; 
        for(View item:itemList){ 
            int childCount = lastLinearLayout.getChildCount(); 
            int itemWidth = item.getWidth(); 
            if(childCount==0){ 
                    lastLinearLayout.addView(item, 0); 
                    item.setTag(lastLinearLayout); 
                    currLineWidth += itemWidth+10; 
            }else{ 
                    if(lineWidth-currLineWidth<itemWidth+10){  //如果当前行不足以显示该Item,要做换行处理 
                            lastLinearLayout = newSubLinearLayout(); 
                            lastLinearLayout.addView(item); 
                            item.setTag(lastLinearLayout); 
 
                            mainLayout.addView(lastLinearLayout); 
                            subLayoutList.add(lastLinearLayout); 
 
                            currLineWidth = itemWidth+10; 
                            haveNewLine = true; 
                    }else{ 
 
                            lastLinearLayout.addView(item, childCount); 
                            item.setTag(lastLinearLayout); 
                            currLineWidth += itemWidth+10; 
                            haveNewLine = false; 
                    } 
            } 
        } 
 
        //最后添加autocompleteTextView 
        lastLinearLayout.addView(autoCompleteTv); 
    } 
 
    /**
     * 添加一个Item:自动去判断是在当前lastLinearLayout中添加，还是要再new 一个LinearLayout
     * @param value
     */ 
    int currLayoutIndex = 0; 
    private void addItem(String value){ 
        lineWidth  = lastLinearLayout.getWidth(); 
        final LinearLayout item = (LinearLayout)mInflater.inflate(itemLayoutResourceId,null); 
 
        final ImageView deleteIv = (ImageView)item.findViewById(R.id.iv_delete); 
        deleteIv.setVisibility(View.GONE); 
        deleteIv.setOnClickListener(new OnClickListener() { 
            @Override 
            public void onClick(View v) { 
                LinearLayout layout = (LinearLayout)item.getTag(); 
                layout.removeView(item); 
                List<View> itemList =new ArrayList<View>(); 
                int mainLayoutChildCount = mainLayout.getChildCount(); 
                for(int i = 0 ;i< mainLayoutChildCount;i++){ 
                    LinearLayout linearLayout = (LinearLayout)mainLayout.getChildAt(i); 
                    int count = linearLayout.getChildCount(); 
                    for(int j =0;j<count;j++){ 
                        View itemLayout = linearLayout.getChildAt(j); 
                        if(!(itemLayout instanceof AutoCompleteTextView)){ 
                            itemList.add(itemLayout); 
                        } 
                    } 
                    linearLayout.removeAllViews(); 
                } 
                mainLayout.removeAllViews(); 
                subLayoutList.clear(); 
                reAddAllItem(itemList); 
            } 
        }); 
 
        item.setOnClickListener(new OnClickListener(){ 
            @Override 
            public void onClick(View v) { 
                if(deleteIv.getVisibility() == View.GONE){ 
                    deleteIv.setVisibility(View.VISIBLE); 
                    item.setBackgroundColor(mContext.getResources().getColor(R.color.color_orange)); 
                }else{ 
                    deleteIv.setVisibility(View.GONE); 
                    item.setBackgroundColor(mContext.getResources().getColor(R.color.color_light)); 
                } 
            } 
        }); 
 
 
        TextView tv_item = (TextView)item.findViewById(R.id.tv_item); 
//        int itemWidth = (int) LableCloudUtlis.getFontWidth(value, (int)itemTv.getTextSize(), mContext)+30; 
        int itemWidth = tv_item.length() * ((int)tv_item.getTextSize()) + 230;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, android.view.ViewGroup.LayoutParams.WRAP_CONTENT); 
        params.setMargins(10, 10, 10, 10); 
 
        item.setLayoutParams(params); 
        tv_item.setText(value); 
 
        int childCount = lastLinearLayout.getChildCount(); 
        if(childCount==1){ 
                lastLinearLayout.addView(item, 0); 
                currLineWidth += itemWidth+10; 
        }else{ 
                if(lineWidth-currLineWidth<itemWidth+10){  //如果当前行不足以显示该Item,要做换行处理 
                    lastLinearLayout.removeView(autoCompleteTv); 
                        lastLinearLayout = newSubLinearLayout(); 
                        lastLinearLayout.addView(item); 
                        lastLinearLayout.addView(autoCompleteTv); 
                        mainLayout.addView(lastLinearLayout); 
                        subLayoutList.add(lastLinearLayout); 
                        currLineWidth = itemWidth+10; 
                        haveNewLine = true; 
                }else{ 
                        lastLinearLayout.addView(item, childCount-1); 
                        currLineWidth += itemWidth+10; 
                        haveNewLine = false; 
                } 
        } 
 
        lastLinearLayout.setTag(lineLayoutIndex); 
        item.setTag(lastLinearLayout); 
 
 
        //如果目前该行剩余的宽度不足以显示autoCompleteTextView就要做换行处理 
        if( !haveNewLine && lineWidth-currLineWidth<autoCompleteTv.getWidth()-100){ 
                //将autoCompleteTextView从lastLinearLayout中remove掉 
                lastLinearLayout.removeView(autoCompleteTv); 
                //新建一个LinearLayout,将autoCompleteTextView添加到此LinaerLayout 
                lastLinearLayout = newSubLinearLayout(); 
                lastLinearLayout.addView(autoCompleteTv); 
                mainLayout.addView(lastLinearLayout); 
 
                subLayoutList.add(lastLinearLayout); 
                currLineWidth = 0; 
        } 
    } 
 
    @Override 
    public void onItemClick(AdapterView<?> parent, View view, int position, 
            long id) { 
        //选中后，往当前lastLinearLayout中添加一个Item 
        String value = autoCompleteTv.getText().toString(); 
        addItem(value); 
        autoCompleteTv.setText(""); 
    } 
 
} 
