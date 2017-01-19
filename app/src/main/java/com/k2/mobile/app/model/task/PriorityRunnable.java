/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.task;

/**
 * @ClassName: PriorityRunnable
 * @Description: 线程优先级
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 *
 */
public class PriorityRunnable extends PriorityObject<Runnable> implements Runnable {

    public PriorityRunnable(Priority priority, Runnable obj) {
        super(priority, obj);
    }

    @Override
    public void run() {
        this.obj.run();
    }
}
