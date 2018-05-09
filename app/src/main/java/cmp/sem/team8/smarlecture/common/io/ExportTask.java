package cmp.sem.team8.smarlecture.common.io;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

/**
 * Represents an export task with failure or success
 */
public final class ExportTask {
    private boolean isSuccess;
    private String message;

    public ExportTask(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public ExportTask addOnSuccessListener(OnSuccessListener callback) {
        if (isSuccess)
            callback.onSuccess(message);
        return this;
    }

    public ExportTask addOnFailureListener(OnFailureListener callback) {
        if (!isSuccess)
            callback.onFailure(message);
        return this;
    }

    public interface OnSuccessListener {
        void onSuccess(String message);
    }

    public interface OnFailureListener {
        void onFailure(String message);
    }
}