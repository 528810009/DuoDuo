package com.guheng.duoduo.Object;

import java.io.Serializable;

public class MsgData implements Serializable {
    private int mIndex;
    private EntityItem mEntityItem;

    public MsgData(int index, EntityItem entityItem) {
        this.mIndex = index;
        this.mEntityItem = entityItem;
    }

    public int getIndex() {
        return mIndex;
    }

    public EntityItem getRecyclerViewItemEntity() {
        return mEntityItem;
    }
}
