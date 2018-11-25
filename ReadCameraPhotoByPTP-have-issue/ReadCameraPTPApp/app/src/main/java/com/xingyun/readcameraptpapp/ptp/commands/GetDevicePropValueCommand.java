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

import java.nio.ByteBuffer;

import com.xingyun.readcameraptpapp.ptp.PtpCamera;
import com.xingyun.readcameraptpapp.ptp.PtpConstants;

public class GetDevicePropValueCommand extends Command {

    private final int property;
    private final int datatype;
    private int value;

    public GetDevicePropValueCommand(PtpCamera camera, int property, int datatype) {
        super(camera);
        this.property = property;
        this.datatype = datatype;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        io.handleCommand(this);
        if (responseCode == PtpConstants.Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        }
        if (responseCode == PtpConstants.Response.Ok) {
            camera.onPropertyChanged(property, value);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.GetDevicePropValue, property);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        if (datatype == PtpConstants.Datatype.int8) {
            value = b.get();
        } else if (datatype == PtpConstants.Datatype.uint8) {
            value = b.get() & 0xFF;
        } else if (datatype == PtpConstants.Datatype.uint16) {
            value = b.getShort() & 0xFFFF;
        } else if (datatype == PtpConstants.Datatype.int16) {
            value = b.getShort();
        } else if (datatype == PtpConstants.Datatype.int32 || datatype == PtpConstants.Datatype.uint32) {
            value = b.getInt();
        }
    }
}
