
package jloader;

import javafx.application.Preloader;
import javafx.application.Preloader.PreloaderNotification;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Jloader2 extends Preloader {
    private Stage stage;
    private ProgressBar bar;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage=primaryStage;
        //这里可以看到和Application一样，也有舞台，我们可以定制自己的界面
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:transparent;");
        ImageView iv=new ImageView();
        iv.setImage(new Image(getClass().getResourceAsStream("LinuxFamily.png")));
        p.setCenter(iv);
        bar = new ProgressBar(0);
        p.setBottom(bar);
        Scene scene = new Scene(p);
        scene.setFill(null);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification info) {
        System.out.println("handleProgressNotification="+info.getProgress());
        if (info.getProgress() != 1.0) {
            bar.setProgress(info.getProgress() / 2);
        }
    }


    /**
     * 重载这个方法可以处理应用通知
     * @param info
     */
    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification) {
            //提取应用程序发送过来的进度值
            double v = ((ProgressNotification) info).getProgress();
            System.out.println("handleApplicationNotification="+v);
            bar.setProgress(v);
        } else if (info instanceof StateChangeNotification) {
            //隐藏/或者关闭preloader
            stage.hide();
            stage.close();
        }
    }

} 