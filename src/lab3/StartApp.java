package lab3;
import javafx.application.Application;
import javafx.stage.Stage;
import lab3.view.FxUi;

/**
 * Main class where program starts.
 */
public class StartApp extends Application {

    /**
     * Start point of the application
     *
     * @param args command line arguments
     */
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FxUi ui=new FxUi(primaryStage);
        ui.displayUi();
    }
}
