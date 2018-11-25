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

import com.xingyun.readcameraptpapp.ptp.*;
import com.xingyun.readcameraptpapp.ptp.PtpCamera;

import java.nio.ByteBuffer;


public class EosSetPropertyCommand extends EosCommand {

    private final int property;
    private final int value;

    public EosSetPropertyCommand(EosCamera camera, int property, int value) {
        super(camera);
        hasDataToSend = true;
        this.property = property;
        this.value = value;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        io.handleCommand(this);
        if (responseCode == PtpConstants.Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.EosSetDevicePropValue);
    }

    @Override
    public void encodeData(ByteBuffer b) {
        // header
        b.putInt(24);
        b.putShort((short) PtpConstants.Type.Data);
        b.putShort((short) PtpConstants.Operation.EosSetDevicePropValue);
        b.putInt(camera.currentTransactionId());
        // specific block
        b.putInt(0x0C);
        b.putInt(property);
        b.putInt(value);
    }
}
