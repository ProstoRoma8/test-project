module airline.project {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;

    // Дозволяємо JavaFX доступ до ваших класів
    opens main to javafx.graphics, javafx.fxml;
    opens ui to javafx.fxml, javafx.base ;
    opens models to javafx.base; // якщо будете використовувати таблиці (TableView)

    // Якщо ваші класи в інших пакетах - додайте їх сюди
    exports main;
    exports ui;
    exports models;
}