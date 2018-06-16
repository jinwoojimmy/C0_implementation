/*
    Load and save file
*/
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileWriter;

public class MyFileManager {

    // singleton design pattern
    private static MyFileManager instance;

    public static MyFileManager getInstance() {
        if (instance == null)
            instance = new MyFileManager();
        return instance;
    }

    /* load file using JFileChooser */
    public File load() {
        File file = null;
        JFileChooser chooser = new JFileChooser();
        // contrain user to select only text file
        chooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        // chooser.setCurrentDirectory(new File("C:/test"));
        int returnVal = chooser.showOpenDialog(null);   // below code will not be executed until user action
        if (returnVal == JFileChooser.APPROVE_OPTION)   // user selected file
            file = chooser.getSelectedFile();

        return file;

    }
    public boolean save(String content) {
        boolean flag = true;

        File file = new File("output.txt");

        try {
            FileWriter fw = new FileWriter(file, false);
            fw.write(content);
            fw.close();
        } catch (Exception ex) {
            flag = false;
            ex.printStackTrace();
        }
        return flag;
    }


}
