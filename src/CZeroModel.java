import java.io.File;

public class CZeroModel {

    private File inputFile;
    private String inputText;
    private String outputText;

    public CZeroModel() {
        inputFile = new File("rule.txt");
    }

    public File getInputFile() {
        return this.inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void setInputText(String content) {
        this.inputText = content;
    }

    public String getInputText() {
        return this.inputText;
    }

    public String getOutputText() {
        return this.outputText;
    }

    public void setOutputText(String content) {
        this.outputText = content;
    }
}
