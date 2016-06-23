package com.example.googleplay.domain;

/**
 * Created by LIANGSE on 2016/3/22.
 */
public class HomePagerInfo {
    //app图片地址
    public String pictureUrl;

    public String id;

    public String name;

    public String packageName;

    public String iconUrl;

    public String size;

    public String downloadUrl;

    public String des;

    @Override
    public String toString() {
        return "HomePagerInfo{" +
                "pictureUrl='" + pictureUrl + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", size='" + size + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", des='" + des + '\'' +
                '}';
    }

//    public HomePagerInfo(String pictureUrl, String id, String name, String packageName, String iconUrl, String size, String downloadUrl, String des) {
//        this.pictureUrl = pictureUrl;
//        this.id = id;
//        this.name = name;
//        this.packageName = packageName;
//        this.iconUrl = iconUrl;
//        this.size = size;
//        this.downloadUrl = downloadUrl;
//        this.des = des;
//    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
