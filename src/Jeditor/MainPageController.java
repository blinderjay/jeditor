/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeditor;

import Jeditor.cloud.Go.Godrive;
import Jeditor.console.ConsoleView;
import de.jensd.fx.glyphs.GlyphsFactory;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

/**
 *
 * @author blinderjay
 */
public class MainPageController implements Initializable {

    @FXML
    Button saveit, saveas, open, zoomin, zoomout, gentime, search, replace;
//    @FXML
//    CodeArea codearea;
    @FXML
    BorderPane consolepane, codepane;
    @FXML
    StackPane editpane;
    @FXML
    Label viewer;
    @FXML
    TextField input;
    @FXML
    ChoiceBox choosebox;
    @FXML
    WebView webview;
    @FXML
    SplitPane splitpane, funcpane;
    @FXML
    TreeView treelist;
    @FXML
    VBox menupane;
    @FXML
    ToggleButton slider;

    private SplitPane.Divider divider;
    private jeditor editor;
    private Scene scene;
    private Stage stage;
    private Path path = null;
    private File file = null;
    Charset charset = Charset.forName("UTF-8");
    private static final String sampleCode = String.join("\n", new String[]{"Hellow,World"});
    private static String tosearch = "";
    private ConsoleView console;
    CodeArea codearea = new CodeArea();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        console = new ConsoleView();
        console.minWidthProperty().bind(funcpane.widthProperty());
        consolepane.setCenter(console);

        console.invokeMain(() -> {

            System.out.print(Godrive.INSTANCE.goauth());

        });

        codepane.setCenter(codearea);
        codepane.setPadding(new Insets(7));
        consolepane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0));
        splitpane.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0);"
        );
        editpane.setBackground(null);
        initTreeView();
        choosebox.setItems(FXCollections.observableArrayList(
                "Dark Mode",
                new Separator(),
                "Light Mode")
        );
        choosebox.setValue(choosebox.getItems().get(0));
        choosebox.setStyle("-fx-font: bold 9.0  sans-serif;-fx-text-alignment: center;-fx-text-alignment: center;");
        choosebox.setTooltip(new Tooltip("Select your Preferred Mode"));
        darkmode(new ActionEvent());
        initCodearea();
        initslider();

//        InputStream is = getClass().getResourceAsStream("f29.png");
//        BackgroundImage myBI = new BackgroundImage(
//                new Image(is),
//                BackgroundRepeat.REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.DEFAULT,
//                BackgroundSize.DEFAULT);
//        editpane.setBackground(new Background(myBI)); 
        search.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Jeditor/image/icon/search.png"),
                23, 23, true, true)));
        replace.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Jeditor/image/icon/replace.png"),
                23, 23, true, true)));
    }

    private void initslider() {
        int flag = 0;
//        SplitDividerSlider slide = new SplitDividerSlider(splitpane,0,SplitDividerSlider.Direction.Right, 1,0.7);
//funcpane.setMaxWidth(0);
        divider = splitpane.getDividers().get(0);
        GlyphsFactory gf = new GlyphsFactory("/de/jensd/fx/glyphs/fontawesome/fontawesome-webfont.ttf");
        gf.setIcon(slider, FontAwesomeIcon.ARROW_CIRCLE_LEFT, "2.2em", "green");
        Timeline slidein = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.81),
                        "hellow",
                        //                        (event) -> {gf.setIcon(slider, FontAwesomeIcon.TOGGLE_LEFT, "2em");funcpane.setMaxWidth(0);},
                        (event) -> {
                            funcpane.setMaxWidth(0);
                        },
                        new KeyValue(divider.positionProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(slider.rotateProperty(), (flag++) * 180, Interpolator.EASE_OUT)
                ));
        Timeline slideout = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.81),
                        "hellow",
                        //                        (event) ->{ gf.setIcon(slider, FontAwesomeIcon.TOGGLE_RIGHT, "2em");},
                        new KeyValue(divider.positionProperty(), 0.7, Interpolator.EASE_IN),
                        new KeyValue(slider.rotateProperty(), (flag++) * 180, Interpolator.EASE_OUT)
                ));

        slider.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            if (t1) {
                funcpane.setMaxWidth(splitpane.getWidth());
                slideout.play();
//                slide.setStaticStatus(false);
//                gf.setIcon(slider, FontAwesomeIcon.TOGGLE_RIGHT, "1.5em");

            } else {
                slidein.play();
//                slide.setStaticStatus(true);
//                gf.setIcon(slider, FontAwesomeIcon.TOGGLE_LEFT, "1.5em");

            }
        });
    }

    private void initTreeView() {
        TreeItem<String> item = new TreeItem<>("hellow");
        treelist.setRoot(item);
        item.setExpanded(false);
        TreeItem<String> i1 = new TreeItem<>("Chinese");
        TreeItem<String> i2 = new TreeItem<>("America");
        TreeItem<String> i3 = new TreeItem<>("European");
        item.getChildren().addAll(i1, i2, i3);
        i3.setExpanded(false);
        TreeItem<String> i4 = new TreeItem<>("Chinese");
        TreeItem<String> i5 = new TreeItem<>("Chinese");
        i3.getChildren().addAll(i4, i5);
    }

    public void setEditor(jeditor edt, Stage stage) {
        this.editor = edt;
        this.stage = stage;
    }

    // Create a shadow effect as a halo around the pane and not within
    // the pane's content area.
//    private Pane createShadowPane() {
//        Pane shadowPane = new Pane();
//        // a "real" app would do this in a CSS stylesheet.
//        shadowPane.setStyle(
//                "-fx-background-color: white;"
//                + "-fx-effect: dropshadow(gaussian, red, " + shadowSize + ", 0, 0, 0);"
//                + "-fx-background-insets: " + shadowSize + ";"
//        );
//
//        Rectangle innerRect = new Rectangle();
//        Rectangle outerRect = new Rectangle();
//        shadowPane.layoutBoundsProperty().addListener(
//                (observable, oldBounds, newBounds) -> {
//                    innerRect.relocate(
//                            newBounds.getMinX() + shadowSize,
//                            newBounds.getMinY() + shadowSize
//                    );
//                    innerRect.setWidth(newBounds.getWidth() - shadowSize * 2);
//                    innerRect.setHeight(newBounds.getHeight() - shadowSize * 2);
//
//                    outerRect.setWidth(newBounds.getWidth());
//                    outerRect.setHeight(newBounds.getHeight());
//
//                    Shape clip = Shape.subtract(outerRect, innerRect);
//                    shadowPane.setClip(clip);
//                }
//        );
//
//        return shadowPane;
//    }
    public void setPath(Path path) {
        this.path = path;
    }

    private void initCodearea() {
        codearea.setPadding(new Insets(36, 8, 10, 8));
        codearea.setParagraphGraphicFactory(LineNumberFactory.get(codearea));
        Subscription cleanupWhenNoLongerNeedIt = codearea
                .multiPlainChanges()
                .successionEnds(java.time.Duration.ofMillis(256))
                .subscribe(ignore -> codearea.setStyleSpans(0, computeHighlighting(codearea.getText())));
        codearea.replaceText(0, 0, sampleCode);
    }

    @FXML
    private void findsearch(ActionEvent event) {
        tosearch = input.getText();
        PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                + "|(?<PAREN>" + PAREN_PATTERN + ")"
                + "|(?<BRACE>" + BRACE_PATTERN + ")"
                + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                + "|(?<STRING>" + STRING_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                + "|(?<TOSEARCH>" + tosearch + ")"
        );
        codearea.appendText("\0");
    }

    @FXML
    private void findreplace(ActionEvent event) {
        TextInputDialog newString = new TextInputDialog("new String");
        newString.setTitle("Replace all the word" + input.getText());
        newString.setHeaderText("Please input the new word that you want you want to replace the old word \"" + input.getText() + "\"");
        Optional<String> newstring = newString.showAndWait();
        newstring.ifPresent(value -> {
            String context = codearea.getText();
            codearea.clear();
            codearea.appendText(context.replace(input.getText(), value));
        });
    }

    @FXML
    private void chooseMode(ActionEvent event) {

        choosebox.getSelectionModel().selectedIndexProperty().addListener((ov, oldv, newv) -> {
            switch (newv.intValue()) {
                case 0: {
                    darkmode(event);
                    break;
                }
                case 2: {
                    lightmode(event);
                    break;
                }
            }

        });
    }

    @FXML
    private void openaction(ActionEvent event) {
        codearea.clear();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Editable File", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        file = fileChooser.showOpenDialog(stage);
        path = Paths.get(file.getPath());
        String context;

        //读入文件，这里使用了常规的readine方法，下面还有两种方案可供参考；
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            while ((context = reader.readLine()) != null) {
                codearea.appendText(context + "\n");
            }
        } catch (IOException io) {
        }
    }

    @FXML
    private void saveitaction(ActionEvent event) {
        try {
            savefile(path, codearea.getText());
        } catch (NullPointerException n) {

            ChoiceDialog<String> choice = new ChoiceDialog<>("to select", "to create");
            choice.setTitle("Find no file");
            choice.setHeaderText("elect a file existed or create a new one");
            Optional<String> result = choice.showAndWait();
            result.ifPresent(value -> {
                if (value.equals("to select")) {
                    saveasaction(event);
                } else {

                    final DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Select one Directories");
                    directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                    File dir = directoryChooser.showDialog(stage);
                    if (dir != null) {
                        TextInputDialog input = new TextInputDialog("Hellow.txt");
                        input.setTitle("Create new file");
                        input.setHeaderText("Please input the filename of the new file you want to create");
                        Optional<String> filename = input.showAndWait();
                        filename.ifPresent(namevalue -> {
                            try {
                                path = Paths.get(dir.getPath() + File.separator + namevalue);
                                System.out.println(path);

                                Files.createFile(path);
                                savefile(path, codearea.getText());
                            } catch (FileAlreadyExistsException ex) {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("File existed error");
                                alert.setHeaderText("Fail to create new file since it's existence");
                                alert.showAndWait();
                            } catch (IOException ex) {
                                Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } else {
                    }

                }
            });
        }
    }

    @FXML
    private void saveasaction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.*"));
        file = fileChooser.showOpenDialog(stage);
        savefile(Paths.get(file.getPath()), codearea.getText());
    }

    private void savefile(Path path, String context) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            writer.write(context, 0, context.length());
        } catch (IOException x) {
            System.err.format("IOException Error : %s%n", x);
        }
    }

    @FXML
    private void darkmode(ActionEvent event) {
        codearea.setBackground(new Background(new BackgroundFill(Color.web("#232323", 0.81f), new CornerRadii(16), Insets.EMPTY)));
        splitpane.getStylesheets().remove(getClass().getResource("/Jeditor/effect/css/LightMode.css").toExternalForm());
        splitpane.getStylesheets().add(getClass().getResource("/Jeditor/effect/css/DarkMode.css").toExternalForm());
    }

    @FXML
    private void lightmode(ActionEvent event) {
        codearea.setBackground(new Background(new BackgroundFill(Color.web("#B9E8BA", 0.9f), new CornerRadii(16), Insets.EMPTY)));
        splitpane.getStylesheets().remove(getClass().getResource("/Jeditor/effect/css/DarkMode.css").toExternalForm());
        splitpane.getStylesheets().add(getClass().getResource("/Jeditor/effect/css/LightMode.css").toExternalForm());

    }

    @FXML
    private void genTime(ActionEvent event) {
        codearea.appendText(
                " [ "
                + DateFormat.getDateTimeInstance(
                        DateFormat.LONG, DateFormat.LONG, Locale.CHINESE).
                        format(new java.util.Date()).toString()
                + " ] ");
    }

    @FXML
    private void visitgithub(ActionEvent event) throws URISyntaxException, IOException {
//        try {
//            Desktop.getDesktop().browse(new URI("https://github.com/blinderjay/jeditor"));
//        } catch (Exception e) 
        {
            /*never happened :success or crashed*/
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Fail to Open Local Browster");
            alert.setHeaderText("Would you like to open the webview controlor?");

            Optional<ButtonType> value = alert.showAndWait();
            if (value.get() == ButtonType.OK) {

//                Timer animTimer = new Timer();
//                animTimer.scheduleAtFixedRate(new TimerTask() {
//                    int i = 0;
//                    @Override
//                    public void run() {
//                        if (i < 100) {
//                            stage.setWidth(stage.getWidth() + 3);
//                            stage.setHeight(stage.getHeight() + 3);                                    
//                        } else {
//                            this.cancel();
//                        }
//                        i++;
//                    }
//                }, 2000, 5);
                WebEngine eng = webview.getEngine();
//                eng.load(getClass().getResource("/blinderjay_jeditor.html").toString());
                splitpane.prefWidthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        stage.setWidth(newValue.doubleValue());
                        stage.centerOnScreen();
                    }

                });

                Timeline larger = new Timeline(
                        new KeyFrame(javafx.util.Duration.millis(666), new KeyValue(splitpane.getDividers().get(0).positionProperty(), 0.7))
                //                        new KeyFrame(javafx.util.Duration.millis(900), new KeyValue(splitpane.prefWidthProperty(), 1400))
                );
                larger.play();
//                    stage.setWidth(1600);

            }
        }
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass
                    = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("COMMENT") != null ? "comment"
                    : matcher.group("TOSEARCH") != null ? "tosearch"
                    : null;
            /* never happens */ assert styleClass != null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private static final String[] KEYWORDS = new String[]{
        "abstract", "assert", "boolean", "break", "byte",
        "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else",
        "enum", "extends", "final", "finally", "float",
        "for", "goto", "if", "implements", "import",
        "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final int shadowSize = 36;

    private static Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
            + "|(?<TOSEARCH>" + tosearch + ")"
    );

//    /**
//     * 问题是怎么把stream转为string;
//     *
//     * @param file
//     */
//    public void readTextFile(String file) {
//        try (Stream<String> stream = Files.lines(Paths.get(file))) {
//            stream.forEach(System.out::println);//输出重定向
//        } catch (IOException e) {
//        }
//
//    }
//    /**
//     * en: This method reads all the documents in at one time. Using methods
//     * such as readLine () requires repeated access to files, and every time
//     * readLine () calls code conversion, which reduces the speed. Therefore, in
//     * the case of known encoding, it is the fastest way to read the files into
//     * memory first by byte stream, and then by one-time encoding conversion.
//     * zh: 这个方法一次把文件全部读进来。用readline()之类的方法需要反复访问文件，每次readline()都会调用编码转换，
//     * 降低了速度，所以，在已知编码的情况下，按字节流方式先将文件都读入内存，再一次性编码转换是最快的方式。
//     *
//     * @param fileName
//     * @return
//     */ 
//    public String readToString(String fileName) {
//        String encoding = "UTF-8";
//        File file = new File(fileName);
//        Long filelength = file.length();
//        byte[] filecontent = new byte[filelength.intValue()];
//        try (FileInputStream in = new FileInputStream(file)) {
//            in.read(filecontent);
//            return new String(filecontent, encoding);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    /**
//     * 使用了string builder,可参考
//     *
//     * @param fileName
//     * @return
//     * @throws IOException
//     */
//    private static String readFileContent(String fileName) throws IOException {
//        File file = new File(fileName);
//        BufferedReader bf = new BufferedReader(new FileReader(file));
//        String content = "";
//        StringBuilder sb = new StringBuilder();
//        while (content != null) {
//            content = bf.readLine();
//            if (content == null) {
//                break;
//            }
//            sb.append(content.trim());
//        }
//        bf.close();
//        return sb.toString();
//    }
    void setScene(Scene scene) {
        this.scene = scene;
    }
}
