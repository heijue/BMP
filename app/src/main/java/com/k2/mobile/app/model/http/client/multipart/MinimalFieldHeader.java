/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.multipart;

import java.util.*;

/**
 * @ClassName: MinimalFieldHeader
 * @Description: MIME报头（see RFC 2045）
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 *
 */
class MinimalFieldHeader implements Iterable<MinimalField> {

    private final List<MinimalField> fields;
    private final Map<String, List<MinimalField>> fieldMap;

    public MinimalFieldHeader() {
        super();
        this.fields = new LinkedList<MinimalField>();
        this.fieldMap = new HashMap<String, List<MinimalField>>();
    }

    public void addField(final MinimalField field) {
        if (field == null) {
            return;
        }
        String key = field.getName().toLowerCase(Locale.US);
        List<MinimalField> values = this.fieldMap.get(key);
        if (values == null) {
            values = new LinkedList<MinimalField>();
            this.fieldMap.put(key, values);
        }
        values.add(field);
        this.fields.add(field);
    }

    public List<MinimalField> getFields() {
        return new ArrayList<MinimalField>(this.fields);
    }

    public MinimalField getField(final String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase(Locale.US);
        List<MinimalField> list = this.fieldMap.get(key);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<MinimalField> getFields(final String name) {
        if (name == null) {
            return null;
        }
        String key = name.toLowerCase(Locale.US);
        List<MinimalField> list = this.fieldMap.get(key);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        } else {
            return new ArrayList<MinimalField>(list);
        }
    }

    public int removeFields(final String name) {
        if (name == null) {
            return 0;
        }
        String key = name.toLowerCase(Locale.US);
        List<MinimalField> removed = fieldMap.remove(key);
        if (removed == null || removed.isEmpty()) {
            return 0;
        }
        this.fields.removeAll(removed);
        return removed.size();
    }

    public void setField(final MinimalField field) {
        if (field == null) {
            return;
        }
        String key = field.getName().toLowerCase(Locale.US);
        List<MinimalField> list = fieldMap.get(key);
        if (list == null || list.isEmpty()) {
            addField(field);
            return;
        }
        list.clear();
        list.add(field);
        int firstOccurrence = -1;
        int index = 0;
        for (Iterator<MinimalField> it = this.fields.iterator(); it.hasNext(); index++) {
            MinimalField f = it.next();
            if (f.getName().equalsIgnoreCase(field.getName())) {
                it.remove();
                if (firstOccurrence == -1) {
                    firstOccurrence = index;
                }
            }
        }
        this.fields.add(firstOccurrence, field);
    }

    @Override
	public Iterator<MinimalField> iterator() {
        return Collections.unmodifiableList(fields).iterator();
    }

    @Override
    public String toString() {
        return this.fields.toString();
    }

}

