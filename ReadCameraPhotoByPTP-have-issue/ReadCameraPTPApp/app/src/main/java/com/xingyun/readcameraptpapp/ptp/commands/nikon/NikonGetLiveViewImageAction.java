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
package com.xingyun.readcameraptpapp.ptp.commands.nikon;

import com.xingyun.readcameraptpapp.ptp.NikonCamera;
import com.xingyun.readcameraptpapp.ptp.PtpAction;
import com.xingyun.readcameraptpapp.ptp.PtpCamera;
import com.xingyun.readcameraptpapp.ptp.PtpConstants;
import com.xingyun.readcameraptpapp.ptp.commands.SimpleCommand;
import com.xingyun.readcameraptpapp.ptp.model.LiveViewData;

public class NikonGetLiveViewImageAction implements PtpAction {

    private final NikonCamera camera;
    private final LiveViewData reuse;

    public NikonGetLiveViewImageAction(NikonCamera camera, LiveViewData reuse) {
        this.camera = camera;
        this.reuse = reuse;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        SimpleCommand simpleCmd = new SimpleCommand(camera, PtpConstants.Operation.NikonStartLiveView);
        io.handleCommand(simpleCmd);

        if (simpleCmd.getResponseCode() != PtpConstants.Response.Ok) {
            return;
        }

        SimpleCommand deviceReady = new SimpleCommand(camera, PtpConstants.Operation.NikonDeviceReady);
        for (int i = 0; i < 10; ++i) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // nop
            }

            deviceReady.reset();
            io.handleCommand(deviceReady);
            if (deviceReady.getResponseCode() == PtpConstants.Response.DeviceBusy) {
                // still waiting
            } else if (deviceReady.getResponseCode() == PtpConstants.Response.Ok) {
                camera.onLiveViewRestarted();
                io.handleCommand(new NikonGetLiveViewImageCommand(camera, reuse));
                return;
            } else {
                return;
            }
        }
    }

    @Override
    public void reset() {
    }
}
