package com.guheng.duoduo.Object;

import com.guheng.duoduo.Tool.Paths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EntityList {
    private static final String TAG = "EntityList";

    private List<EntityItem> mItemEntityList = new ArrayList<>();
    private List<Boolean> mIsClickList = new ArrayList<>();//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public EntityList() {
        File f = new File(Paths.mSDCardPath + File.separator + Paths.mDataPath);
        if (!f.exists()) {
            boolean b = f.mkdirs();
        }

        load();
    }

    public int size() {
        if (this.mItemEntityList == null) {
            return -1;
        }

        return this.mItemEntityList.size();
    }

    public boolean isEmpty() {
        if (this.mItemEntityList == null) {
            return true;
        }

        return this.mItemEntityList.isEmpty();
    }

    public void clear() {
        if (this.mItemEntityList == null) {
            return;
        }

        this.mItemEntityList.clear();
        this.mIsClickList.clear();
    }

    public EntityItem get(int index) {
        if (this.mItemEntityList == null) {
            return null;
        }

        return this.mItemEntityList.get(index);
    }

    public void add(EntityItem e) {
        if (this.mItemEntityList == null) {
            return;
        }

        this.mItemEntityList.add(0, e);
        this.mIsClickList.add(0, false);
    }

    public EntityItem set(int index, EntityItem e) {
        if (this.mItemEntityList == null) {
            return null;
        }

        return this.mItemEntityList.set(index, e);
    }

    public EntityItem remove(int index) {
        if (this.mItemEntityList == null) {
            return null;
        }

        this.mIsClickList.remove(index);

        return this.mItemEntityList.remove(index);
    }

    public List<Boolean> getIsClickList() {
        return this.mIsClickList;
    }

    private void load() {
        try {
            File f = new File(Paths.mSDCardPath + File.separator + Paths.mDataFile);
            if (!f.exists()) {
                return;
            }

            BufferedReader bfr = new BufferedReader(new FileReader(f));
            String line = bfr.readLine();
            while (line != null) {
                String[] strs = line.split(",");
                if (strs.length == 2) {
                    if (!strs[0].isEmpty() && !strs[1].isEmpty()) {
                        this.add(new EntityItem(strs[0], strs[1]));
                    }
                }

                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            File f = new File(Paths.mSDCardPath + File.separator + Paths.mDataFile);
            if (!f.exists()) {
                boolean b = f.createNewFile();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(f, false));
            for (int i = this.mItemEntityList.size() - 1; i >= 0; i--) {
                EntityItem entity = this.mItemEntityList.get(i);
                bfw.write(entity.getDate() + "," + entity.getSummarized());
                bfw.newLine();
            }
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
