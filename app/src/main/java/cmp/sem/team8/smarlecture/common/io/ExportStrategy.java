package cmp.sem.team8.smarlecture.common.io;

import java.util.List;

import cmp.sem.team8.smarlecture.common.data.model.FileModel;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

public interface ExportStrategy {
    ExportTask exportOneFile(FileModel fileData);
    ExportTask exportFiles(List<FileModel> filesData);
}
