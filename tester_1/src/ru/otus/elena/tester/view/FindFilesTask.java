package ru.otus.elena.tester.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class FindFilesTask extends Task<Void> {

    final private ObservableList<String> paths;
    final private String pathToFind;
    final private String packageName;
    final private List<String> list = new ArrayList<>();
    final private int[] counter = new int[1];

    public FindFilesTask(ObservableList<String> paths, String pathToFind, String packageName) {

        this.paths = paths;
        this.pathToFind = pathToFind;
        this.packageName = packageName.replace(".",File.separator);      
    }

    @Override
    protected Void call() /*throws Exception*/ {
        try {
            Path path = Paths.get(pathToFind);
            updateMessage("has been found " + 0 + " files");
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attr) throws IOException {
                    if (path.toString()
                            .contains(File.separator+packageName+File.separator)&&(path.toString().contains(".class")
                            /*||path.toString().contains(".java")*/)) {
                        updateMessage("has been found " + ++counter[0] + " files");
                        updateProgress(counter[0],100);
                        list.add(path.toString());                      
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path t, IOException ioe) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());           
            return null;
        } 
        return null;
    }

    @Override
    protected void succeeded() {        
        paths.addAll(list);
        updateProgress(100,100);
        
    }
}
