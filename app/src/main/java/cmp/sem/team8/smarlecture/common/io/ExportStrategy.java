package cmp.sem.team8.smarlecture.common.io;

import java.util.List;

import cmp.sem.team8.smarlecture.common.data.model.FileModel;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

/**
 * Define the interface of exporting data that should every file format implement in its way.
 */
public interface ExportStrategy {
    ExportTask exportOneFile(FileModel fileData, String fileName);
    ExportTask exportFiles(List<FileModel> filesData);
}
