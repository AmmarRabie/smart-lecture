package cmp.sem.team8.smarlecture.session.join.attendance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.DataService.AttendanceStatus;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;

public class AttendanceMonitorService extends Service {
    private String sessionId;
    private boolean s = false;
    private boolean isFirstStatus = true;

    public AttendanceMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final FirebaseRepository repo = FirebaseRepository.getInstance();
        sessionId = intent.getStringExtra(getString(R.string.IKey_sessionId));
        repo.listenAttendanceStatus(sessionId, new DataService.Listen<DataService.AttendanceStatus>() {
            @Override
            public void onDataReceived(DataService.AttendanceStatus currStatus) {
                if (isFirstStatus) {
                    if (currStatus.equals(AttendanceStatus.OPEN)) {
                        stopSelf();
                        repo.forget(this);
                    }
                    isFirstStatus = false;
                    return;
                }
                if (currStatus.equals(AttendanceStatus.OPEN)) {
                    stopSelf();
                    repo.forget(this);
                    pushNotification();
                    startActivity();
                }
            }
        });
        return START_NOT_STICKY;
    }

    private void pushNotification() {
        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder
                (getApplicationContext()).setContentTitle("Attendance Begin").setContentText("go to verify secret").
                setContentTitle("Attendance Begin").setSmallIcon(android.R.drawable.ic_dialog_info).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }

    private void startActivity() {
        Intent writeAttendance = new Intent(this, AttendanceActivity.class);
        writeAttendance.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        writeAttendance.putExtra(getString(R.string.IKey_sessionId), sessionId);
        startActivity(writeAttendance);
    }
}
