package app.nwrfc.saprfc.command;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "logout", description = "delete cached connection details",mixinStandardHelpOptions = true)
public class Logout implements Runnable{
    @Override
    public void run() {
        File folder = new File(new File(System.getProperty("user.home")),File.separator+".saprfc");
        if (folder.exists()) {
            deleteDirectory(folder);
            folder.delete();
        }
    }
    private void deleteDirectory(File file)
    {
        for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            subfile.delete();
        }
    }
}
