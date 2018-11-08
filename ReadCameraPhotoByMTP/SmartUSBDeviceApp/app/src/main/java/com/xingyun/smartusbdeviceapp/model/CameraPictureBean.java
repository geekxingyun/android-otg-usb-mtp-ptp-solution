package com.xingyun.smartusbdeviceapp.model;

import java.io.Serializable;

public class CameraPictureBean implements Serializable {

    String cameraPicturePath;

    public String getCameraPicturePath() {
        return cameraPicturePath;
    }

    public void setCameraPicturePath(String cameraPicturePath) {
        this.cameraPicturePath = cameraPicturePath;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
