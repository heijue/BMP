/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.multipart;

/**
 * @ClassName: MinimalField
 * @Description: MIME实体类
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 * @since 4.0
 */
class MinimalField {

    private final String name;
    private final String value;

    MinimalField(final String name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getBody() {
        return this.value;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.name);
        buffer.append(": ");
        buffer.append(this.value);
        return buffer.toString();
    }

}
