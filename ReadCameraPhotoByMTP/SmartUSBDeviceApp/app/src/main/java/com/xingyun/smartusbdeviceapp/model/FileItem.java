package com.xingyun.smartusbdeviceapp.model;

import java.io.Serializable;

public class FileItem  implements Serializable {

    private String fileName;
    private String filePath;

    public FileItem() {
    }
    public FileItem(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
