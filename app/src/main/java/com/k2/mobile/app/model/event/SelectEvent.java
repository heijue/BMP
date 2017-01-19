package com.k2.mobile.app.model.event;

import java.util.List;

import com.k2.mobile.app.model.album.ImageItem;

/**
 * @ClassName: SelectEvent
 * @Description: 选择图片事件传递
 * @author force
 * @date 2015年8月3日 上午9:19:35
 *
 */
public class SelectEvent {
    private List<ImageItem> list;
    private Event event;

    public SelectEvent(Event event,List<ImageItem> list){
        this.list = list;
        this.event = event;
    }

    public List<ImageItem> getList() {
        return list;
    }

    public void setList(List<ImageItem> list) {
        this.list = list;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public enum Event{
        SELECT_PHOTO_LIST
    }
}
