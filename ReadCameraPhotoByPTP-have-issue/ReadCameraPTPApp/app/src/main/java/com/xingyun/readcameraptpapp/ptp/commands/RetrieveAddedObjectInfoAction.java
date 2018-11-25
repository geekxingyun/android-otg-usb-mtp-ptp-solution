/**
 * Copyright 2013 Nils Assbeck, Guersel Ayaz and Michael Zoech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xingyun.readcameraptpapp.ptp.commands;

import com.xingyun.readcameraptpapp.ptp.PtpAction;
import com.xingyun.readcameraptpapp.ptp.PtpCamera;
import com.xingyun.readcameraptpapp.ptp.PtpConstants;

public class RetrieveAddedObjectInfoAction implements PtpAction {

    private final PtpCamera camera;
    private final int objectHandle;

    public RetrieveAddedObjectInfoAction(PtpCamera camera, int objectHandle) {
        this.camera = camera;
        this.objectHandle = objectHandle;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        GetObjectInfoCommand getInfo = new GetObjectInfoCommand(camera, objectHandle);
        io.handleCommand(getInfo);

        if (getInfo.getResponseCode() != PtpConstants.Response.Ok) {
            return;
        }
        if (getInfo.getObjectInfo() == null) {
            return;
        }

        camera.onEventObjectAdded(objectHandle, getInfo.getObjectInfo().objectFormat);
    }

    @Override
    public void reset() {
    }
}
