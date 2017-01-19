/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.task;

/**
 * @ClassName: PriorityObject
 * @Description: 线程优先级对象
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 *
 * @param <E>
 */
public class PriorityObject<E> {

    public final Priority priority;
    public final E obj;

    public PriorityObject(Priority priority, E obj) {
        this.priority = priority == null ? Priority.DEFAULT : priority;
        this.obj = obj;
    }
}
