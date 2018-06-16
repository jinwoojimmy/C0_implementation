public class CZeroMain {
    public static void main(String[] args) {
        // model
        CZeroModel model = new CZeroModel();
        // view
        CZeroView view = new CZeroView(model);
        // controller
        CZeroController controller = new CZeroController(view, model);
        // show GUI
        view.setVisible(true);
    }
}
