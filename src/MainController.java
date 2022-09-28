import data.InfoList;
import fileView.XLXSOpen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {
    public InfoList infoList;
    AlgOpen alg;
    ArrayList<String> content_list = new ArrayList<>();
    List<File> samplePath;
    MainLoader docLoad;
    XLXSOpen xlxsOpen;
    File saveSampleDir;
    boolean checkLoad, checkUnload, checkStart = false;
    int counter, counter_files;
    public static String errorMessageStr = "";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button dirLoadButton;

    @FXML
    private Button algsTable;

    @FXML
    private Button dirUnloadButton;

    @FXML
    private Text loadStatus;

    @FXML
    private Text loadStatus_end;

    @FXML
    private Text loadStatusFileNumber;

    @FXML
    private Button startButton;

    @FXML
    public Label lowLoadText = new Label("");

    @FXML
    private AnchorPane mainPanel;

    @FXML
    public Button closeButton;

    @FXML
    private ToggleButton maleSampleToggle;

    @FXML
    private ToggleButton femaleSampleToggle;

    public MainController() throws IOException, InvalidFormatException {
    }

    int getCounter(int rowCount, int currentNumber) {
        Double temp = new Double(100/rowCount);
        return temp.intValue() + currentNumber;
    }

    boolean maleSample = false;
    boolean femaleSample = false;

    public void addHinds(){

        Tooltip tipAlgsTable = new Tooltip();
        tipAlgsTable.setText("Нажмите, для того, чтобы перейти к редактированию таблицы алгоритмов");
        tipAlgsTable.setStyle("-fx-text-fill: turquoise;");
        algsTable.setTooltip(tipAlgsTable);

        Tooltip tipLoad = new Tooltip();
        tipLoad.setText("Выберите папку, в которой находятся xlsx файлы");
        tipLoad.setStyle("-fx-text-fill: turquoise;");
        dirLoadButton.setTooltip(tipLoad);

        Tooltip tipUnLoad = new Tooltip();
        tipUnLoad.setText("Выберите папку, в которую должны сохраняться готовые отчеты");
        tipUnLoad.setStyle("-fx-text-fill: turquoise;");
        dirUnloadButton.setTooltip(tipUnLoad);

        Tooltip tipStart = new Tooltip();
        tipStart.setText("Нажмите, для того, чтобы получить готовые отчеты");
        tipStart.setStyle("-fx-text-fill: turquoise;");
        startButton.setTooltip(tipStart);

        Tooltip closeStart = new Tooltip();
        closeStart.setText("Нажмите, для того, чтобы закрыть приложение");
        closeStart.setStyle("-fx-text-fill: turquoise;");
        closeButton.setTooltip(closeStart);

    }

    public void removeHinds(){
        algsTable.setTooltip(null);
        dirLoadButton.setTooltip(null);
        dirUnloadButton.setTooltip(null);
        startButton.setTooltip(null);
        closeButton.setTooltip(null);
    }

    public static boolean tempHints = true;

    @FXML
    void initialize() throws FileNotFoundException, InterruptedException {
        addHinds();

        if (maleSample){
            maleSampleToggle.setStyle("-fx-background-color: #00c7c7");
            maleSampleToggle.setText("Выбран");
        } else
        {
            maleSampleToggle.setStyle("-fx-background-color: #b8faff");
            maleSampleToggle.setText("Не выбран");
        }

        if (femaleSample){
            femaleSampleToggle.setStyle("-fx-background-color: #00c7c7");
            femaleSampleToggle.setText("Выбран");
        } else
        {
            femaleSampleToggle.setStyle("-fx-background-color: #b8faff");
            femaleSampleToggle.setText("Не выбран");
        }

        maleSampleToggle.setOnAction(ActionEvent -> {
            if(maleSampleToggle.isSelected()){
                maleSampleToggle.setStyle("-fx-background-color: #00c7c7");
                maleSampleToggle.setText("Выбран");
                femaleSampleToggle.setStyle("-fx-background-color: #b8faff");
                femaleSampleToggle.setText("Не выбран");
                femaleSample = false;
                maleSample = true;
            } else {
                maleSampleToggle.setStyle("-fx-background-color: #b8faff");
                maleSampleToggle.setText("Не выбран");
                maleSample = false;
            }
        });

        femaleSampleToggle.setOnAction(ActionEvent -> {
            if(femaleSampleToggle.isSelected()){
                femaleSampleToggle.setStyle("-fx-background-color: #00c7c7");
                femaleSampleToggle.setText("Выбран");
                maleSampleToggle.setStyle("-fx-background-color: #b8faff");
                maleSampleToggle.setText("Не выбран");
                maleSample = false;
                femaleSample = true;
            } else {
                femaleSampleToggle.setStyle("-fx-background-color: #b8faff");
                femaleSampleToggle.setText("Не выбран");
                femaleSample = false;
            }
        });

        FileInputStream loadStream = new FileInputStream("C:\\Program Files\\genpass_obr\\load.png");
        Image loadImage = new Image(loadStream);
        ImageView loadView = new ImageView(loadImage);
        dirLoadButton.graphicProperty().setValue(loadView);

        FileInputStream unloadStream = new FileInputStream("C:\\Program Files\\genpass_obr\\unload.png");
        Image unloadImage = new Image(unloadStream);
        ImageView unloadView = new ImageView(unloadImage);
        dirUnloadButton.graphicProperty().setValue(unloadView);

        FileInputStream startStream = new FileInputStream("C:\\Program Files\\genpass_obr\\start.png");
        Image startImage = new Image(startStream);
        ImageView startView = new ImageView(startImage);
        startButton.graphicProperty().setValue(startView);

        FileInputStream closeStream = new FileInputStream("C:\\Program Files\\genpass_obr\\logout.png");
        Image closeImage = new Image(closeStream);
        ImageView closeView = new ImageView(closeImage);
        closeButton.graphicProperty().setValue(closeView);

        FileInputStream algsTableStream = new FileInputStream("C:\\Program Files\\genpass_obr\\algsTable.png");
        Image algsTableImage = new Image(algsTableStream);
        ImageView algsTableView = new ImageView(algsTableImage);
        algsTable.graphicProperty().setValue(algsTableView);

        int r = 60;
        startButton.setShape(new Circle(r));
        startButton.setMinSize(r*2, r*2);
        startButton.setMaxSize(r*2, r*2);

        checkLoad = false;
        checkUnload = false;

        closeButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

        dirLoadButton.setOnAction(actionEvent -> {
            if(!checkStart)
            {
                loadStatus.setText("");
                loadStatus_end.setText("");
                loadStatusFileNumber.setText("");
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File dir = directoryChooser.showDialog(new Stage());
                File[] file = dir.listFiles();
                samplePath = Arrays.asList(file);
                checkLoad = true;
            }
            else
            {
                errorMessageStr = "Происходит обработка файлов. Повторите попытку попытку позже...";
                ErrorController errorController = new ErrorController();
                try {
                    errorController.start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dirUnloadButton.setOnAction(actionEvent -> {
                    if(!checkStart)
                    {
                        loadStatus.setText("");
                        loadStatus_end.setText("");
                        loadStatusFileNumber.setText("");
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        saveSampleDir = directoryChooser.showDialog(new Stage());
                        checkUnload = true;

                    }
                    else
                    {
                        errorMessageStr = "Происходит обработка файлов. Повторите попытку попытку позже...";
                        ErrorController errorController = new ErrorController();
                        try {
                            errorController.start(new Stage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        startButton.setOnAction(actionEvent -> {
                    if(!checkStart){
                        loadStatus.setText("");
                        loadStatus_end.setText("");
                        loadStatusFileNumber.setText("");
                        if(checkLoad & checkUnload){
                            if(femaleSample || maleSample)
                            {
                                if(samplePath.size() != 0)
                                {
                                    checkStart = true;
                                    if(maleSample){
                                        new Thread(){
                                            @Override
                                            public void run(){
                                                counter_files = 0;
                                                for (int i = 0; i<samplePath.size();i++)
                                                {
                                                    if(samplePath.get(i).getPath().contains(".xlsx"))
                                                    {
                                                        loadStatusFileNumber.setText("Обработка " + (i+1) + " файла");
                                                        counter = 0;
                                                        infoList = new InfoList();
                                                        try {
                                                            xlxsOpen = new XLXSOpen(samplePath.get(i));
                                                            docLoad = new MainLoader("obr");
                                                            alg = new AlgOpen(infoList);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            xlxsOpen.getPhylum(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(21, counter)) + " %");
                                                            xlxsOpen.getGenus(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(21, counter)) + " %");
                                                            xlxsOpen.getFileName(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(21, counter)) + " %");
                                                            xlxsOpen.getSpecies(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(21, counter)) + " %");
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        counter_files++;
                                                    }
                                                }
                                                loadStatusFileNumber.setText("");
                                                loadStatus_end.setText("Успешно обработано " + counter_files + " файла(ов)!");
                                                checkStart = false;
                                            }
                                        }.start();
                                    } else if(femaleSample){
                                        new Thread(){
                                            @Override
                                            public void run(){
                                                counter_files = 0;
                                                for (int i = 0; i<samplePath.size();i++) {
                                                    if(samplePath.get(i).getPath().contains(".xlsx"))
                                                    {
                                                        loadStatusFileNumber.setText("Обработка " + (i+1) + " файла");
                                                        counter = 0;
                                                        infoList = new InfoList();
                                                        try {
                                                            xlxsOpen = new XLXSOpen(samplePath.get(i));
                                                            docLoad = new MainLoader("obr_1");
                                                            alg = new AlgOpen(infoList);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InvalidFormatException e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            xlxsOpen.getPhylum(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(10, counter)) + " %");
                                                            xlxsOpen.getGenus(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(10, counter)) + " %");
                                                            xlxsOpen.getFileName(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(10, counter)) + " %");
                                                            xlxsOpen.getSpecies(infoList);
                                                            loadStatus.setText("Загрузка: " + (counter=getCounter(10, counter)) + " %");
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        counter_files++;
                                                    }
                                                }
                                                loadStatusFileNumber.setText("");
                                                loadStatus_end.setText("Успешно обработано " + counter_files + " файла(ов)!");
                                                checkStart = false;
                                            }
                                        }.start();
                                    }
                                } else
                                {
                                    errorMessageStr = "Выбранная папка загрузки является пустой...";
                                    ErrorController errorController = new ErrorController();
                                    try {
                                        errorController.start(new Stage());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                errorMessageStr = "Вы не выбрали шаблон для создания отчета...";
                                ErrorController errorController = new ErrorController();
                                try {
                                    errorController.start(new Stage());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            errorMessageStr = "Вы не указаали директорию загрузки или директорию выгрузки...";
                            ErrorController errorController = new ErrorController();
                            try {
                                errorController.start(new Stage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else
                    {
                        errorMessageStr = "Происходит обработка файлов. Повторите попытку попытку позже...";
                        ErrorController errorController = new ErrorController();
                        try {
                            errorController.start(new Stage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
