package sandro.hobayan.applicaitonbiblepresenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {
    @FXML
    private Label lbl;
    @FXML
    private TextField book, chapter, verse;
    @FXML
    private Button btn;
    @FXML
    private FlowPane booksPane;

    @FXML
    public void initialize() {
        // Load the JSON in a background thread to avoid blocking the FX thread
        new Thread(() -> {
            try {
                Path jsonPath = Path.of(System.getProperty("user.dir"), "Bible versions", "kjv.json");
                File jsonFile = jsonPath.toFile();
                if (!jsonFile.exists()) {
                    updateLabel("kjv.json not found at: " + jsonFile.getAbsolutePath());
                    return;
                }

                String text = Files.readString(jsonPath);
                List<String> bookNames = extractBookNames(text);

                if (bookNames.isEmpty()) {
                    updateLabel("No books found in kjv.json (unexpected format)");
                } else {
                    Platform.runLater(() -> populateBookButtons(bookNames));
                }

            } catch (IOException e) {
                updateLabel("kjv.json file error: " + e.getMessage());
            } catch (Exception e) {
                updateLabel("Error reading kjv.json: " + e.getMessage());
            }
        }).start();
    }

    private List<String> extractBookNames(String jsonText) {
        List<String> books = new ArrayList<>();

        // Pattern 1: look for objects with "name": "Genesis" style
        Pattern namePattern = Pattern.compile("\"name\"\s*:\s*\"([^]+)\"");
        Matcher m = namePattern.matcher(jsonText);
        while (m.find()) {
            String name = m.group(1).trim();
            if (!books.contains(name)) books.add(name);
        }

        if (!books.isEmpty()) return books;

        // Pattern 2: top-level keys like "Genesis": { ... }
        Pattern keyPattern = Pattern.compile("\"([A-Za-z0-9 _-]+)\"\s*:\s*\\{");
        m = keyPattern.matcher(jsonText);
        while (m.find()) {
            String key = m.group(1).trim();
            // skip common metadata keys
            if (key.equalsIgnoreCase("copyright") || key.equalsIgnoreCase("license") || key.equalsIgnoreCase("books")) continue;
            if (!books.contains(key)) books.add(key);
        }

        return books;
    }

    private void updateLabel(String text) {
        Platform.runLater(() -> lbl.setText(text));
    }

    private void populateBookButtons(List<String> books) {
        booksPane.getChildren().clear();
        for (String bookName : books) {
            Button b = new Button(bookName);
            b.setOnAction(evt -> lbl.setText("Selected: " + bookName));
            b.setPrefWidth(180);
            booksPane.getChildren().add(b);
        }
    }

    @FXML
    public void btnClick() {
        lbl.setText("Book: " + book.getText() + "\nChapter: " + chapter.getText() + "\nVerse: " + verse.getText());
    }
}