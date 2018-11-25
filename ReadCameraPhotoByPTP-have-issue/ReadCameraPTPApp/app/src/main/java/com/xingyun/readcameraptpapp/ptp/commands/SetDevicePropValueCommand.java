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

import com.xingyun.readcameraptpapp.ptp.PtpCamera;
import com.xingyun.readcameraptpapp.ptp.PtpConstants;

import java.nio.ByteBuffer;

public class SetDevicePropValueCommand extends Command {

    private final int property;
    private final int value;
    private final int datatype;

    public SetDevicePropValueCommand(PtpCamera camera, int property, int value, int datatype) {
        super(camera);
        this.property = property;
        this.value = value;
        this.datatype = datatype;
        hasDataToSend = true;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        io.handleCommand(this);
        if (responseCode == PtpConstants.Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        } else if (responseCode == PtpConstants.Response.Ok) {
            camera.onPropertyChanged(property, value);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.SetDevicePropValue, property);
    }

    @Override
    public void encodeData(ByteBuffer b) {
        // header
        b.putInt(12 + PtpConstants.getDatatypeSize(datatype));
        b.putShort((short) PtpConstants.Type.Data);
        b.putShort((short) PtpConstants.Operation.SetDevicePropValue);
        b.putInt(camera.currentTransactionId());
        // specific block
        if (datatype == PtpConstants.Datatype.int8 || datatype == PtpConstants.Datatype.uint8) {
            b.put((byte) value);
        } else if (datatype == PtpConstants.Datatype.int16 || datatype == PtpConstants.Datatype.uint16) {
            b.putShort((short) value);
        } else if (datatype == PtpConstants.Datatype.int32 || datatype == PtpConstants.Datatype.uint32) {
            b.putInt(value);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
