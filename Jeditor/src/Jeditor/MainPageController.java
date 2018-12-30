/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeditor;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.Duration;
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
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    @FXML
    CodeArea codearea;
    @FXML
    BorderPane editpane;
    @FXML
    Label viewer;
    @FXML
    TextField input;
    @FXML
    ChoiceBox choosebox;

    private Jeditor editor;
    private Stage stage;
    private Path path = null;
    private File file = null;
    Charset charset = Charset.forName("UTF-8");
    private static final String sampleCode = String.join("\n", new String[]{"Hellow,World"});
    private static String tosearch = null;
    private static int codesize = 15;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
                choosebox.setItems(FXCollections.observableArrayList(
                "Dark",
                new Separator(),
                "Light")
        );
choosebox.setTooltip(new Tooltip("Select the language"));
        initCodearea();
        darkmode(new ActionEvent());
//        InputStream is = getClass().getResourceAsStream("f29.png");
//        BackgroundImage myBI = new BackgroundImage(
//                new Image(is),
//                BackgroundRepeat.REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.DEFAULT,
//                BackgroundSize.DEFAULT);
//        editpane.setBackground(new Background(myBI));

        search.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Image/search.png"), 23, 23, true, true)));
        replace.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Image/replace.png"), 23, 23, true, true)));
    }

    public void setEditor(Jeditor edt, Stage stage) {
        this.editor = edt;
        this.stage = stage;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    private void initCodearea() {
        codearea.setPadding(new Insets(10, 8, 10, 8));
        codearea.setParagraphGraphicFactory(LineNumberFactory.get(codearea));
        Subscription cleanupWhenNoLongerNeedIt = codearea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(256))
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
                case 0:{
                    darkmode(event);System.out.println("Dark");
                    break;
                }
                case 2:{
                    lightmode(event);System.out.println("Light");
                    break;
                }
            }

        });
    }

    @FXML
    private void openaction(ActionEvent event) {
        codearea.clear();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
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
                    TextInputDialog input = new TextInputDialog("/home/");
                    input.setTitle("Create new file");
                    input.setHeaderText("Please input the path and your filename you want to save your file");
                    Optional<String> newpath = input.showAndWait();
                    newpath.ifPresent(pathstring -> {
                        try {
                            Files.createFile(Paths.get(pathstring));
                            savefile(Paths.get(pathstring), codearea.getText());
                        } catch (FileAlreadyExistsException ex) {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("File existed error");
                            alert.setHeaderText("Fail to create new file since it's existence");
                            alert.showAndWait();
                        } catch (IOException ex) {
                            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
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
        codearea.setBackground(new Background(new BackgroundFill(Color.web("#232323", 0.64f), new CornerRadii(16), Insets.EMPTY)));
        //        codearea.setStyle(" -fx-font:   bold 14px sans-serif;");
        editpane.getStylesheets().add(getClass().getResource("DarkMode.css").toExternalForm());
    }

    @FXML
    private void lightmode(ActionEvent event) {
        codearea.setBackground(new Background(new BackgroundFill(Color.web("#AAAAAA", 0.64f), new CornerRadii(16), Insets.EMPTY)));
        //        codearea.setStyle(" -fx-font:   bold 14px sans-serif;");
        editpane.getStylesheets().add(getClass().getResource("LightMode.css").toExternalForm());

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
        Desktop.getDesktop().browse(new URI("https://github.com/blinderjay/jeditor"));
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
}
