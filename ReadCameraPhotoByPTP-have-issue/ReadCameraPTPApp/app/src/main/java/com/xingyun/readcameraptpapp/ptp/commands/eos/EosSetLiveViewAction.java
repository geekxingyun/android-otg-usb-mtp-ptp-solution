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
package com.xingyun.readcameraptpapp.ptp.commands.eos;

import com.xingyun.readcameraptpapp.ptp.EosCamera;
import com.xingyun.readcameraptpapp.ptp.EosConstants;
import com.xingyun.readcameraptpapp.ptp.PtpAction;
import com.xingyun.readcameraptpapp.ptp.PtpCamera;
import com.xingyun.readcameraptpapp.ptp.PtpConstants;

public class EosSetLiveViewAction implements PtpAction {

    private final EosCamera camera;
    private final boolean enabled;

    public EosSetLiveViewAction(EosCamera camera, boolean enabled) {
        this.camera = camera;
        this.enabled = enabled;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        int evfMode = camera.getPtpProperty(PtpConstants.Property.EosEvfMode);

        if (enabled && evfMode != EosConstants.EvfMode.ENABLE || !enabled && evfMode != EosConstants.EvfMode.DISABLE) {
            EosSetPropertyCommand setEvfMode = new EosSetPropertyCommand(camera, PtpConstants.Property.EosEvfMode,
                    enabled ? EosConstants.EvfMode.ENABLE : EosConstants.EvfMode.DISABLE);
            io.handleCommand(setEvfMode);

            if (setEvfMode.getResponseCode() == PtpConstants.Response.DeviceBusy) {
                camera.onDeviceBusy(this, true);
                return;
            } else if (setEvfMode.getResponseCode() != PtpConstants.Response.Ok) {
                camera.onPtpWarning("Couldn't open live view");
                return;
            }
        }

        int outputDevice = camera.getPtpProperty(PtpConstants.Property.EosEvfOutputDevice);

        if (enabled) {
            outputDevice |= EosConstants.EvfOutputDevice.PC;
        } else {
            outputDevice &= ~EosConstants.EvfOutputDevice.PC;
        }

        EosSetPropertyCommand setOutputDevice = new EosSetPropertyCommand(camera, PtpConstants.Property.EosEvfOutputDevice,
                outputDevice);
        io.handleCommand(setOutputDevice);

        if (setOutputDevice.getResponseCode() == PtpConstants.Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        } else if (setOutputDevice.getResponseCode() == PtpConstants.Response.Ok) {
            if (!enabled) {
                camera.onLiveViewStopped();
            } else {
                camera.onLiveViewStarted();
            }
            return;
        } else {
            camera.onPtpWarning("Couldn't open live view");
        }

    }

    @Override
    public void reset() {
    }
}
