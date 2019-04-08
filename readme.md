# Jeditor

开放，简单，高度可定制的文本编辑器（基于javafx）

**我的github项目网址：[Jeditor]("https://github.com/blinderjay/jeditor")**


> 注意：
> - 由于业务需求，页面正在变动，一些功能按钮暂时被移动到右侧隐藏栏，如果想要完善界面的下载最新的发行源码；
> - 由于javafx现在用的比较少，暂时可能不会再更新，除非后面Godrive库被写好动态链接库用这个项目来做测试，可能会加上云同步；
> - 其实javafx还是挺优秀的，尤其是桌面开发，可能现在更多的人会去考虑兼容移动端，使用前端的技术，用electron或nwjs打包吧；
> - 之所以放弃是因为很多库和说明文档不到位，还有打包的体积，虽然比较electron还好，但是就像我不喜欢每打开一个应用都要耗费一个浏览器的资源，我也不会为了一个文本编辑器而耗费启动一个jvm主进程的资源；
> - 还有，不喜欢java....
> - 文本编辑器是我的一个小小执念吧，最新的是用golang写的一个markdown编辑器，基于BS结构，给自己锻炼web编程的 [Goditor](http://www.github.com/blinderjay/Goditor),时间允许后续我会在里面加一些有趣的特性。

[toc]

## 一：特点

正如上所言，单纯的目的是想要开发一个简单、自由、优雅的文本编辑器。

而且我希望自己可以自定义所想要的功能：比如我的初衷是写一个markdown编辑器，可以自动实现云同步。
- 这是我一直想要的功能，可惜目前并没有足够优秀的桌面程序能满足我的功能和审美要求；
- 至于选用javafx的原因主要是为了实现以下几个目的：

    1. 全平台支持：Windows/Linux/Mac/Web/Mobile;
    2. 优秀的审美效果：JavaFx支持GPU物理渲染;
    3. 更好的线程调度
    4. 自由


> Add: 有人会认为node JS可能会更合适，我不反对这一点，但是相比而言，javafx的开发时间和学习成本会更少：
- 通过SceneBuilder所见所得，自动生成fxml实现页面布局；
- 通过css实现样式设置，避免了传统javaGUI冗余代码；
- 通过java代码来定义行为；

So easy~~!!


## 二：截图


## 三：安装和运行

鉴于我们使用的是javafx技术，除了普通的运行方式，javafx支持的运行方式有：
- 本地应用
- java web start（暂时并没有设置证书，所以无法达到javaws的"高"安全等级要求）
- 浏览器中运行（最新浏览器已经取消对jvm的支持）
- 理论上Gluon软件也可以将其打包成移动应用：android、IOS，但是这是一项付费的服务，so 。。。


所以，我们推荐的运行方式有：
- 首先，如果您不介意下载一个70-80M的安装包的话，最简单的方式当然是直接安装已经打包好的[本地应用程序包](https://github.com/blinderjay/jeditor/releases),
在最新的版本中我已经打包了windows(exe)、linux(rpm,deb)、Mac（dmg）安装包，你可以像普通应用一样直接下载安装，而不必进行复杂的java运行环境配置。

- 其次，Jeditor归根到底还是java应用，可以用传统的方式在jvm中运行[jre压缩包](https://github.com/blinderjay/jeditor/releases) 。
关于java环境的要求请参考**开发要求**中**java环境**章节。

> 注意：下载完Jeditor-x.x.x.jre.zip后解压，目录中的目录结构不能改变（包含一个jre和另一个包含5个jre包的文件lib文件夹）
你可以进入目录后，然后在命令行运行：
```
cd  /Path/To/Your/Jre
java -jar jeditor-x.x.x.jar
```

- 最后，你可以下载源码包，自己编译运行，关于一些依赖包的配置请参考**开发要求**中的**依赖包**的小节。

## 四：功能说明

### 4.1 现有功能

工具栏：文件打开，存储，新建，另存为        搜索高亮、搜索替代
编辑框：复制、粘贴、撤回、java语法高亮
状态栏：    浏览readme文档       主题更改（目前仅支持明、暗两种主题）

### 4.2 后续功能

markdown高亮及预览
googleDrive同步


现在已经实现的

## 五：开发要求

### 5.1 java环境:jdk>=8;
> 注意:
    - jdk(jre)11已经把javafx 独立出去，javafx不再单独由oracle公司维护，而改由交给openjfx社区维护，这对于开源社区而言当然是一件幸事；
    - 但是对于普通的使用者而言，意味着如果你想要用最新的jdk/jre11(oraclejdk,openjdk)来运行这个javafx程序，需要进行更为复杂的配置；
    - 我的建议是使用最新版的jdk8

### 5.2 IDE

- 我使用的是netbeans8.2，如果你对于javafx不熟悉的话我也建议你使用这个IDE。

> 注意：netbeans的维护和开发已经移交给了apache:
代码正在逐渐的交付，最新的netbeans9、10便是由apache构建出的，虽然有很多的改进，但是对javafx的支持并**没有8.2**稳定。

- 除了netbeans，eclipse 和 IntelliJ IDEA 也是非常优秀的 IDE , 也都可以支持javafx。

- 最后，在开发过程中我使用了Scene builder来帮助我生成javafx的fxml标记语言脚本，fxml可以实现javafx程序的布局设置，
而与之唯一对应的相关java控制器代码则只需专注于行为的控制，这真的是非常棒的一种开发方式。

### 5.3 依赖包

为了实现文本区域的语法高亮，我借用了[RichTextFX包](https://github.com/FXMisc/RichTextFX),我已经把他们下载在了源码包的"jeditor/Jeditor/lib"目录中

javafx的默认TextArea控件并不支持内部局部的样式设置，虽然我可以在内部引入swing控件，但是我并不想这样：
- 固然是因为这样会引入swing的一些缺陷（比如响应速度，冗长的界面控制代码，分散的侦听器管理，界面效果不一致等等）;
- 更为重要的是javafx在底层和swing有着相当大的区别，你可以理解以下[javafx的架构](https://blog.csdn.net/maosijunzi/article/details/42967057),
简单的理解，如果你在javafx中同时调用swing，可能需要同时调用两套图形管理系统（比如图形渲染、线程处理），虽然jvm会自动对其优化，显然这并非我所愿。

## 六：测试与评估

- 未打包jar压缩文件16-20M，java运行时环境内存占用>500M;
- 打包本地安装包70-90M，本地运行内存占用100-140M;

总结：以体积换内存，打包成本地后，能调用更多的GPU资源。

## 七：作者


联系方式：blinderjay@gmail.com

注：如果您有什么好的建议请发送邮件或者联系qq:3031341534
 还是学生，平时可能不会i太多回复issues专栏



## 八： 资料与参考

开始开发时对javafx完全没有认知，以下的博文对我理解javafx有很大帮助：

- [ 褚金辉的CSDN javafx专栏(翻译)](https://blog.csdn.net/maosijunzi/column/info/javafx);

- [亿百教程javafx](https://www.yiibai.com/javafx/);

- [JavaFX CSS Reference Guide](https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html);

- [github FXMisc的Wiki](https://github.com/FXMisc/RichTextFX/wiki)

灵感来源于github的Moeditor工程：

-  [Your all-purpose markdown editor - Built with Electron.](Moeditor/Moeditor)

## 九：版权声明

- Jeditor仅用作学习交流，不允许直接使用源码和二进制包用作商业用途。

- 本程序使用了RichTextFX的代码，其获得了以下的双重授权：
    - BSD 2-Clause License 
    - GPLv2 with the Classpath Exception




## 十：已知问题

1. 无法安装

如果你选择本地安装，请注意：目前仅支持x64架构主机

2. 输入法

在linux下部分ibus架构输入法无法输入中文，windows和mac基本正常

3. 卡顿

由于是java打包而来，安装包和运行时内存占用都不会太小(内存基本120M左右)，所以建议内存不能小于1G。





