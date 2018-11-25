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
import com.xingyun.readcameraptpapp.ptp.model.StorageInfo;

import java.nio.ByteBuffer;

public class GetStorageInfoCommand extends Command {

    private StorageInfo storageInfo;
    private final int storageId;

    public StorageInfo getStorageInfo() {
        return storageInfo;
    }

    public GetStorageInfoCommand(PtpCamera camera, int storageId) {
        super(camera);
        this.storageId = storageId;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        io.handleCommand(this);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        super.encodeCommand(b, PtpConstants.Operation.GetStorageInfo, storageId);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        storageInfo = new StorageInfo(b, length);
    }
}
