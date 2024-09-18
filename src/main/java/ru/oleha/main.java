package ru.oleha;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import okhttp3.*;
import ru.oleha.api.DiscordWebhook;
import ru.oleha.configs.ConfigBuyItems;
import ru.oleha.configs.ConfigSettings;
import ru.oleha.thread.ThreadCreateImg;
import ru.oleha.thread.ThreadDiscordLogs;
import ru.oleha.thread.ThreadItemParser;
import ru.oleha.thread.ThreadStart;
import ru.oleha.utils.BuyItems;
import ru.oleha.utils.ItemInfo;
import ru.oleha.utils.Storage;
import ru.oleha.utils.Utils;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class main extends Application implements NativeKeyListener {
    public static ThreadStart threadStart;
    static final DateTimeFormatter DAYS = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    static int sleepCounter = 0;
    public static  CheckBox loop = new CheckBox("Loop");
    static CheckBox logs = new CheckBox("Logs");
    static Label timeScan = new Label("Time item scan: 0");
    static Label timeFindItem = new Label("Time find item: 0");
    public static Tesseract tesseractItemName = new Tesseract();
    public static Tesseract tesseractConnection = new Tesseract();
    public static Tesseract tesseractFirstItemPrice = new Tesseract();
    public static Tesseract tesseractLastItemPrice = new Tesseract();
    public static Collection<ItemInfo> itemInfos = Collections.synchronizedCollection(new ArrayList<>());
    public static void main(String[] args) throws IOException, AWTException {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new KeyHandler());
        tesseractItemName.setDatapath("./tessdata");
        tesseractItemName.setLanguage("rusf");
        tesseractFirstItemPrice.setDatapath("./tessdata");
        tesseractFirstItemPrice.setLanguage("rusf");
        tesseractLastItemPrice.setDatapath("./tessdata");
        tesseractLastItemPrice.setLanguage("rusf");
        tesseractConnection.setDatapath("./tessdata");
        tesseractConnection.setLanguage("rusb+engb");
        ConfigBuyItems.init();
        ConfigSettings.init();
        loop.setSelected(ConfigSettings.isLoop());
        logs.setSelected(ConfigSettings.isLogs());
        DiscordWebhook discordWebhook = setupDiscordWebhook("Бот для покупки предметов был запушен",new Color(0x0000FF));
        discordWebhook.execute();
        Application.launch();
    }
    private static int getBuyStatus() throws Exception {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(755,483,412,93));
        String text = tesseractItemName.doOCR(image);
        if (text.contains("Недостаточно")) {
            return 3;
        }
        if (text.contains("лот более")) {
            return 2;
        }
        if (text.contains("успешно")) {
            return 1;
        }
        return 0;
    }
    private static void buyItem(int x,int y, ItemInfo itemInfo) throws Exception {
        sleepCounter = 0;
        itemInfos.clear();
        if (confirmBuyItem(x, y, itemInfo)) {
            return;
        }
        ClickMouse(x,y,true);
        ClickMouse(x, y + 28,false);
        if (logs.isSelected()) {
            ThreadCreateImg createImg = new ThreadCreateImg(x,y - 18);
            createImg.start();
        }
        boolean isActive = isButtonBuyItemActive(x,y + 18);
        while (!isActive) {
            Thread.sleep(10);
            if (sleepCounter > 100) {
                return;
            }
            isActive = isButtonBuyItemActive(x,y + 18);
            sleepCounter++;
        }
        if (confirmBuyItem(x, y, itemInfo)) {
            return;
        }
        ClickMouse(x, y + 28,true);
        int bS = getBuyStatus();
        while (bS == 0) {
            Thread.sleep(10);
            if (sleepCounter > 100) {
                return;
            }
            bS = getBuyStatus();
            sleepCounter++;
        }
        if (logs.isSelected()) {
            if (bS == 1) {
                ThreadDiscordLogs threadDiscordLogs = new ThreadDiscordLogs("Успешно купили", new Color(0x00FF00), itemInfo);
                threadDiscordLogs.start();
                Logs.log("Успешно купили", itemInfo);
            }
            if (bS == 2) {
                ThreadDiscordLogs threadDiscordLogs = new ThreadDiscordLogs("Не успели купить", new Color(0xFF0000), itemInfo);
                threadDiscordLogs.start();
                Logs.log("Не успели купить", itemInfo);
            }
            if (bS == 3) {
                ThreadDiscordLogs threadDiscordLogs = new ThreadDiscordLogs("Вы не смогли купить из-за не хватки денег",new Color(0xFFDE00), itemInfo);
                threadDiscordLogs.start();
                Logs.log("Вы не смогли купить из-за не хватки денег", itemInfo);
            }
        }
        closeGui();
    }

    public static void debugProgramTime() throws Exception {
        itemInfos.clear();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        ClickMouse((int) (screen.getWidth() / 4), (int) (screen.getHeight() / 2),true);
        long timeNew;
        long time;
        if (!Utils.isGuiOpenAuction()) {
            if (Utils.isSocketException()) {
                closeGui();
                Thread.sleep(500);
                ClickMouse(900,900,true);
            }
            boolean isInGame = Utils.isInGame();
            while (!isInGame) {
                Thread.sleep(10);
                isInGame = Utils.isInGame();
            }
            openAuction();
        }
        try {
            time = System.currentTimeMillis();
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(889,405,494,370));
            int y = 0;
            CountDownLatch countDownLatch = new CountDownLatch(9);
            for (int i = 0; i < 9; i++) {
                ThreadItemParser threadItemParser = new ThreadItemParser(countDownLatch,image.getSubimage(0,y,494,37));
                threadItemParser.start();
                y += 37;
            }
            countDownLatch.await();
            timeNew = System.currentTimeMillis();
            timeScan.setText("Time item scan: " + (timeNew - time));
            time = System.currentTimeMillis();
            ArrayList<BuyItems> buyItemsArrayList = Storage.getBuyItems();
            mark:
            for (ItemInfo itemInfo : itemInfos) {
                for (BuyItems buyItems : buyItemsArrayList) {
                    if (itemInfo.getName().contains(buyItems.getItemName())) {
                        if (itemInfo.getPrice() > 0 && itemInfo.getStack() >= buyItems.getMinStack()) {
                            if (itemInfo.getPrice() <= (buyItems.getMinPrice() * itemInfo.getStack())) {
                                itemInfos.clear();
                                break mark;
                            }
                        }
                    }
                }
            }
            timeNew = System.currentTimeMillis();
            timeFindItem.setText("Time find item: " + (timeNew - time));
            Thread.sleep(500);
            clickSearch();
            while (Utils.isSameScreen()) {
                Thread.sleep(25);
                clickSearch();
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
    public static void debugItemInformation() throws Exception {
        itemInfos.clear();
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(889,405,494,370));
        int y = 0;
        CountDownLatch countDownLatch = new CountDownLatch(9);
        for (int i = 0; i < 9; i++) {
            ThreadItemParser threadItemParser = new ThreadItemParser(countDownLatch,image.getSubimage(0,y,494,37));
            threadItemParser.start();
            y += 37;
        }
        countDownLatch.await();
        Debug.log(itemInfos);
    }

    public static void start() throws Exception {
        itemInfos.clear();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        ClickMouse((int) (screen.getWidth() / 4), (int) (screen.getHeight() / 2), true);
        if (!Utils.isGuiOpenAuction()) {
            if (Utils.isSocketException()) {
                DiscordWebhook discordWebhook = setupDiscordWebhook("Бот поймал Socket Exception и закрыл его!",new Color(0xFF00FF));
                discordWebhook.execute();
                closeGui();
                Thread.sleep(500);
            }
            if (Utils.isGuiMainMenu()) {
                ClickMouse(900,900,true);
                Thread.sleep(500);
            }
            boolean isInGame = Utils.isInGame();
            while (!isInGame) {
                Thread.sleep(10);
                isInGame = Utils.isInGame();
            }
            openAuction();
        }
        try {
            BufferedImage image = Utils.robot.createScreenCapture(new Rectangle(889, 405, 494, 370));
            int y = 0;
            CountDownLatch countDownLatch = new CountDownLatch(9);
            for (int i = 0; i < 9; i++) {
                ThreadItemParser threadItemParser = new ThreadItemParser(countDownLatch, image.getSubimage(0, y, 494, 37));
                threadItemParser.start();
                y += 37;
            }
            countDownLatch.await();
            ArrayList<BuyItems> buyItemsArrayList = Storage.getBuyItems();
            mark:
            for (ItemInfo itemInfo : itemInfos) {
                for (BuyItems buyItems : buyItemsArrayList) {
                    if (itemInfo.getName().contains(buyItems.getItemName())) {
                        if (itemInfo.getPrice() > 0 && itemInfo.getStack() >= buyItems.getMinStack()) {
                            if (itemInfo.getPrice() <= (buyItems.getMinPrice() * itemInfo.getStack())) {
                                if (buyItems.getRarity() != null && itemInfo.getRarity() != buyItems.getRarity()) {
                                    continue;
                                }
                                buyItem(itemInfo.getX(), itemInfo.getY(), itemInfo);
                                itemInfos.clear();
                                break mark;
                            }
                        }
                    }
                }
            }
            Thread.sleep(500);
            clickSearch();
            while (Utils.isSameScreen()) {
                Thread.sleep(25);
                clickSearch();
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        Label startBuy = new Label("Start/Stop buy `" + NativeKeyEvent.getKeyText(ConfigSettings.getKeyIDStart())+"`");
        Label exit = new Label("Exit `" + NativeKeyEvent.getKeyText(ConfigSettings.getKeyIDExit())+"`");
        Label size = new Label("Item size " + ConfigBuyItems.size());
        Button start = new Button();
        start.setText("Start");
        start.setOnAction(event -> {
            try {
                if (main.threadStart == null || !main.threadStart.isAlive()) {
                    main.threadStart = new ThreadStart();
                    main.threadStart.start();
                } else {
                    main.threadStart.stopRunning();
                    main.threadStart = null;
                }
            } catch (Exception e) {
                e.fillInStackTrace();
                throw new RuntimeException(e);
            }
        });
        Button debugButton = new Button();
        debugButton.setText("Debug");
        debugButton.setOnAction(event -> {
            try {
                debugItemInformation();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Button testTime = new Button();
        testTime.setText("Test time");
        testTime.setOnAction(event -> {
            try {
                debugProgramTime();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Button sendConfigButton = new Button();
        sendConfigButton.setText("Send cfg");
        sendConfigButton.setOnAction(event -> {
            try {
                OkHttpClient client = new OkHttpClient();

                File file = new File("./buyItems.json");
                RequestBody fileBody = RequestBody.create(file, MediaType.parse("text/plain"));
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("content", "Конфиг")
                        .addFormDataPart("file", file.getName(), fileBody)
                        .build();

                Request request = new Request.Builder()
                        .url(Storage.webHookConfig)
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        System.out.println("Файл успешно отправлен!");
                    } else {
                        System.out.println("Ошибка: " + response.code());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Button clearLogs = new Button();
        clearLogs.setText("Clear logs");
        clearLogs.setOnAction(event -> {
            try {
                Path path = Paths.get("./logs.json");
                Files.delete(path);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Button clearImg = new Button();
        clearImg.setText("Clear img");
        clearImg.setOnAction(event -> {
            Path path = Paths.get("./imgs");
            try (Stream<Path> files = Files.list(path)) {
                files.forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        HBox hBox = new HBox(10);
        VBox vBoxButton = new VBox(10);
        VBox vBoxButton2 = new VBox(10);
        VBox vBinding = new VBox(10);
        VBox vCheckBox = new VBox(10);
        VBox vText = new VBox(10);
        logs.selectedProperty().addListener((observable, oldValue, newValue) -> ConfigSettings.setLogs(newValue));
        loop.selectedProperty().addListener((observable, oldValue, newValue) -> ConfigSettings.setLoop(newValue));
        vBoxButton.getChildren().addAll(start,debugButton,testTime);
        vBoxButton2.getChildren().addAll(sendConfigButton,clearLogs,clearImg);
        vBinding.getChildren().addAll(startBuy,exit, size);
        vCheckBox.getChildren().addAll(loop,logs);
        vText.getChildren().addAll(timeScan,timeFindItem);
        hBox.getChildren().addAll(vBoxButton, vBoxButton2, vBinding, vCheckBox, vText);
        Scene scene = new Scene(hBox, 500, 500);
//        scene.getStylesheets().add("https://raw.githubusercontent.com/antoniopelusi/JavaFX-Dark-Theme/main/style.css");
        stage.setScene(scene);
        stage.setTitle("Minecraft 1.7.10");
        stage.setWidth(500);
        stage.setHeight(135);
        stage.show();
        stage.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    DiscordWebhook discordWebhook = setupDiscordWebhook("Бот для покупки предметов был закрыт",new Color(0xFF0000));
                    discordWebhook.execute();
                    GlobalScreen.removeNativeKeyListener(new KeyHandler());
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void ClickMouse(int x,int y,boolean b) {
        try {
            Utils.robot.mouseMove(x, y);
            if (b) {
                Thread.sleep(45);
                Utils.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(75);
                Utils.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void clickSearch() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        ClickMouse((int)(dimension.getWidth() / 2) + 380, (int)(dimension.getHeight()/ 2) - 185,true);
    }
    public static boolean confirmBuyItem(int x, int y, ItemInfo itemInfo) {
        BufferedImage originalImage = itemInfo.getImage();
        originalImage = originalImage.getSubimage(0,0,originalImage.getWidth(),18);
        BufferedImage image = Utils.robot.createScreenCapture(new Rectangle(x - 431,y - 25,494,18));
        for (int pX = 0; pX < originalImage.getWidth(); pX++) {
            for (int pY = 0; pY < originalImage.getHeight(); pY++) {
                if (originalImage.getRGB(pX,pY) != image.getRGB(pX,pY)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isButtonBuyItemActive(int x, int y) {
        BufferedImage image = Utils.robot.createScreenCapture(new Rectangle(x,y,1,1));
        Color color = new Color(37,40,37);
        Color colorImage = new Color(image.getRGB(0,0),false);
        return colorImage.equals(color);
    }
    public static void closeGui() {
        try {
            Utils.robot.keyPress(27);
            Thread.sleep(250);
            Utils.robot.keyRelease(27);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void openAuction() {
        try {
            Utils.robot.keyPress(80);
            Thread.sleep(250);
            Utils.robot.keyRelease(80);
            ClickMouse(550,430,true);
            Thread.sleep(200);
            ClickMouse(1050,390,true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static DiscordWebhook setupDiscordWebhook(String title, Color color) {
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setColor(color);
        embedObject.setTitle(title);
        String timestamp = String.format("[%s : %s]", LocalDateTime.now().format(DAYS),LocalDateTime.now().format(TIME));
        embedObject.addField("Время: " + timestamp, "", false);

        DiscordWebhook discordWebhook = new DiscordWebhook(Storage.webHookLogs);
        discordWebhook.addEmbed(embedObject);
        return discordWebhook;
    }
}