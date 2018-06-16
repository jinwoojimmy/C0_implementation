import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/* view  */
public class CZeroView extends JFrame {

    // components
    private JPanel container;
    private JPanel middlePanel;
    private JButton loadBtn, convertBtn, resetBtn;
    // components - panel
    public PanelView loadPanel, resultPanel;

    private CZeroModel model;

    // panel view
    public class PanelView extends JPanel {

        private JLabel label;
        private JScrollPane scrollTextArea;
        private JTextArea textArea;

        public PanelView(String labelName) {

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(135, 206, 235));    // skyblue color

            // create label, buttons, text
            label = new JLabel(labelName);

            textArea = new JTextArea();
            textArea.setBackground(Color.WHITE);
            textArea.setEditable(false);    // disable edit as default
            scrollTextArea = new JScrollPane(textArea);

            // add to maincontainer
            this.add(label);
            this.add(scrollTextArea);

        }
        // getter
        public String getText() {
            return textArea.getText();
        }
        // setter
        public void setText(String str) {
            this.textArea.setText(str);
        }

    }

    // constructor
    public CZeroView(CZeroModel model) {
        // set model
        this.model = model;

        // create container for layout
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setAlignmentX(Component.CENTER_ALIGNMENT);

        // create middle panel
        middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

        loadBtn = new JButton("Load");
        convertBtn = new JButton("Convert");
        disableConvertButton();
        resetBtn = new JButton("Reset");


        // add elements to middle panel
        middlePanel.add(loadBtn);
        middlePanel.add(convertBtn);
        middlePanel.add(resetBtn);

        // instantiate two panels
        loadPanel = new PanelView("Load Panel");
        resultPanel = new PanelView("Result Panel");

        // add components to container
        container.add(loadPanel);
        container.add(middlePanel);
        container.add(resultPanel);

        // mainView settings
        this.add(container);    // add container
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

    }

    public void showSaveDialog(boolean success) {
        String msg;
        if (success) {
            msg = "Save success!";
        } else {
            msg = "Save failed! Please try again";
        }
        JOptionPane.showMessageDialog(this, msg);
    }

    public void showErrorInputDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    /* add listeners  */
    public void addLoadListener(ActionListener al) {
        loadBtn.addActionListener(al);
    }

    public void addConvertListener(ActionListener al) {
        convertBtn.addActionListener(al);
    }

    public void addResetListener(ActionListener al) {
        resetBtn.addActionListener(al);
    }

    public void enableConvertButton() {
        this.convertBtn.setEnabled(true);
    }

    public void disableConvertButton() {
        this.convertBtn.setEnabled(false);
    }


}
