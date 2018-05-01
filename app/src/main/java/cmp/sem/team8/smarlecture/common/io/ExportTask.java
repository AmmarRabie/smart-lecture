package cmp.sem.team8.smarlecture.common.io;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

public final class ExportTask {
    private boolean isSuccess;

    public ExportTask(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ExportTask addOnSuccessListener(OnSuccessListener callback) {
        if (isSuccess)
            callback.onSuccess();
        return this;
    }

    public ExportTask addOnFailureListener(OnFailureListener callback) {
        if (!isSuccess)
            callback.onFailure();
        return this;
    }


    public interface OnSuccessListener {
        void onSuccess();
    }

    public interface OnFailureListener {
        void onFailure();
    }
}