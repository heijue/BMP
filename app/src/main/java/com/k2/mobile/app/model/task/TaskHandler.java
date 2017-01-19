/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.task;

/**
 * @ClassName: TaskHandler
 * @Description: 任务抽象接口
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 *
 */
public interface TaskHandler {

    boolean supportPause();

    boolean supportResume();

    boolean supportCancel();

    void pause();

    void resume();

    void cancel();

    boolean isPaused();

    boolean isCancelled();
}
