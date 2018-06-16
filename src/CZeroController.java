import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

/* controller  */
public class CZeroController {

    private MyFileManager myFileManager;
    private CZeroLogicModule logicModule;
    private CZeroView view;
    private CZeroModel model;

    // constructor
    public CZeroController(CZeroView view, CZeroModel model) {
        myFileManager = MyFileManager.getInstance();
        logicModule = CZeroLogicModule.getInstance();
        // set view and model
        this.view = view;
        this.model = model;

        // add listeners
        view.addLoadListener(new LoadListener());
        view.addConvertListener(new ConvertListener());
        view.addResetListener(new ResetListener());
    }

    // load listener - works when load button clicked
    class LoadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            /* if input file exists in path ; "rule.txt",
             set inputFile to the corresponding file. */

            File file = model.getInputFile();

            // if loaded already OR "rule.txt" does not exist, select by file chooser
            try {
                if (model.getInputText() != null || !file.isFile()) {
                    file = myFileManager.load();
                    model.setInputFile(file);
                    if (file == null)   // if user canceled to load or failed to load
                        return;
                }
            } catch(NullPointerException ex) {  // when file == null
                file = myFileManager.load();
                model.setInputFile(file);
                if (file == null)   // if user canceled to load or failed to load
                    return;
            }


            StringBuilder contentBuilder = new StringBuilder();

            try (Stream<String> stream = Files.lines( file.toPath(),
                    StandardCharsets.UTF_8)) {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return;
            }

            String content = contentBuilder.toString();

            // set text of view
            model.setInputText(content);
            view.loadPanel.setText(content);

            // enable convert
            view.enableConvertButton();

            // reset output view and logic
            logicModule.reset();
            view.resultPanel.setText(null);
            model.setOutputText(null);
        }
    }

    // convert listener - works when covert button clicked
    class ConvertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getOutputText() != null)
                return;

            String inputText = model.getInputText();
            String errMsg = "Wrong input format!!";

            try {
                // error handling for "|" in string
                if (inputText.contains("|")) {
                    errMsg = "Please write rules without \'|\' ";
                    view.showErrorInputDialog(errMsg);
                    throw new Exception();
                }

                String output = logicModule.logic(inputText);
                model.setOutputText(output);
                view.resultPanel.setText(output);
                // write file out
                view.showSaveDialog(myFileManager.save(output));
            }  catch (Exception err) {
                err.printStackTrace();
                view.showErrorInputDialog(errMsg);
                logicModule.reset();
            }
        }
    }
    // reset listener - works when reset button clicked
    class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logicModule.reset();

            view.loadPanel.setText(null);
            view.resultPanel.setText(null);
            view.disableConvertButton();

            model.setInputFile(new File("rule.txt"));
            model.setInputText(null);
            model.setOutputText(null);

        }
    }


}
