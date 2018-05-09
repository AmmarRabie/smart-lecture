package cmp.sem.team8.smarlecture.common.io;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.model.FileModel;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

/**
 * Service or context that outer layers interact with it to export the data
 */
public class ExportContext {

    private ExportStrategy strategy;

    public ExportContext(ExportStrategy exportStrategy) {
        System.setProperty("android.javax.xml.stream.XMLInputFactory", "com.sun.xml.stream.ZephyrParserFactory");
        System.setProperty("android.javax.xml.stream.XMLOutputFactory", "com.sun.xml.stream.ZephyrWriterFactory");
        System.setProperty("android.javax.xml.stream.XMLEventFactory", "com.sun.xml.stream.events.ZephyrEventFactory");
        strategy = exportStrategy;
    }

    /**
     * @param file     data representation of an exported file
     * @param fileName custom file name, if null the file name should take the group name
     * @return The task (Promise in other language) of the export task
     * You can assign to the task listeners you want
     * @see ExportTask#addOnSuccessListener(ExportTask.OnSuccessListener)
     * @see ExportTask#addOnFailureListener(ExportTask.OnFailureListener)
     */
    public ExportTask export(FileModel file, String fileName) {
        return strategy.exportOneFile(file, fileName);
    }

    /**
     * export groups in one file
     *
     * @param files data representation of an exported file
     * @return The task (Promise in other language) of the export task
     * You can assign to the task listeners you want
     * @see ExportTask#addOnSuccessListener(ExportTask.OnSuccessListener)
     * @see ExportTask#addOnFailureListener(ExportTask.OnFailureListener)
     */
    public ExportTask export(ArrayList<FileModel> files) {
        return strategy.exportFiles(files);
    }
}

