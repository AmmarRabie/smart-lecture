package cmp.sem.team8.smarlecture.common.io;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.model.FileModel;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

public class ExportContext {

    private ExportStrategy strategy;

    public ExportContext(ExportStrategy exportStrategy) {
        System.setProperty("android.javax.xml.stream.XMLInputFactory", "com.sun.xml.stream.ZephyrParserFactory");
        System.setProperty("android.javax.xml.stream.XMLOutputFactory", "com.sun.xml.stream.ZephyrWriterFactory");
        System.setProperty("android.javax.xml.stream.XMLEventFactory", "com.sun.xml.stream.events.ZephyrEventFactory");
        strategy = exportStrategy;
    }

    public ExportTask export(FileModel file) {
       return strategy.exportOneFile(file);
    }

    public ExportTask export(ArrayList<FileModel> files) {
        return strategy.exportFiles(files);
    }
}

