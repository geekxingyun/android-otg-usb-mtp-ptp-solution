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
package com.xingyun.readcameraptpapp.ptp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.xingyun.readcameraptpapp.R;
import com.xingyun.readcameraptpapp.util.NotificationIds;

public class WorkerNotifier implements Camera.WorkerListener {

    private final NotificationManager notificationManager;
    private final Notification notification;
    private final int uniqueId;


    public WorkerNotifier(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


//        notification = new Notification(R.drawable.icon, context.getString(R.string.worker_ticker),
//                System.currentTimeMillis());
//        notification.setLatestEventInfo(context.getApplicationContext(), context.getString(R.string.worker_content_title),
//                        context.getString(R.string.worker_content_text), null);

        notification= new Notification.Builder(context.getApplicationContext())
                .setContentTitle(context.getString(R.string.worker_content_title))
                .setContentText(context.getString(R.string.worker_content_text))
//                .setSmallIcon(R.drawable.icon_pro_white)
                .build();
        uniqueId = NotificationIds.getInstance().getUniqueIdentifier(WorkerNotifier.class.getName() + ":running");
    }

    @Override
    public void onWorkerStarted() {
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(uniqueId, notification);
    }

    @Override
    public void onWorkerEnded() {
        notificationManager.cancel(uniqueId);
    }

}
