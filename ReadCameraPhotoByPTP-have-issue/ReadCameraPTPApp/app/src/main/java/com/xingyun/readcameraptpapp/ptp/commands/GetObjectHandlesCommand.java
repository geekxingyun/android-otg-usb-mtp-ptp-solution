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

import com.xingyun.readcameraptpapp.ptp.Camera;
import com.xingyun.readcameraptpapp.ptp.PacketUtil;
import com.xingyun.readcameraptpapp.ptp.PtpCamera;
import com.xingyun.readcameraptpapp.ptp.PtpConstants;

import java.nio.ByteBuffer;

public class GetObjectHandlesCommand extends Command {

    private final int storageId;
    private final int objectFormat;
    private final int associationHandle;
    private int[] objectHandles;
    private final Camera.StorageInfoListener listener;

    public int[] getObjectHandles() {
        if (objectHandles == null) {
            return new int[0];
        }
        return objectHandles;
    }

    public GetObjectHandlesCommand(PtpCamera camera, Camera.StorageInfoListener listener, int storageId) {
        this(camera, listener, storageId, 0, 0);
    }

    public GetObjectHandlesCommand(PtpCamera camera, Camera.StorageInfoListener listener, int storageId, int objectFormat) {
        this(camera, listener, storageId, objectFormat, 0);
    }

    public GetObjectHandlesCommand(PtpCamera camera, Camera.StorageInfoListener listener, int storageId, int objectFormat,
                                   int associationHandle) {
        super(camera);
        this.listener = listener;
        this.storageId = storageId;
        this.objectFormat = objectFormat;
        this.associationHandle = associationHandle;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        io.handleCommand(this);
        if (getResponseCode() != PtpConstants.Response.Ok) {
            // error
            listener.onImageHandlesRetrieved(new int[0]);
            return;
        }
        listener.onImageHandlesRetrieved(objectHandles);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        super.encodeCommand(b, PtpConstants.Operation.GetObjectHandles, storageId, objectFormat, associationHandle);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        objectHandles = PacketUtil.readU32Array(b);
    }
}
