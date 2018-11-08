package com.xingyun.smartusbdeviceapp.model;

public class FileItem {
    private String imageId;
    private String imageName;
    private String imagePath;

    public FileItem() {
    }

    public FileItem(String imageId, String imageName, String imagePath) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
