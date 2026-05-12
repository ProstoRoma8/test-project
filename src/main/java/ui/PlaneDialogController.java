package ui;

import dao.PlaneDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.AppContext;
import models.*;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaneDialogController implements Initializable {

    // ── Заголовок ──
    @FXML private Label dialogTitle;

    // ── Тип апарату ──
    @FXML private ToggleGroup typeGroup;
    @FXML private RadioButton rbPassenger;
    @FXML private RadioButton rbCargo;
    @FXML private RadioButton rbHeli;

    // ── Загальні поля ──
    @FXML private TextField modelField;
    @FXML private TextField fuelField;
    @FXML private TextField rangeField;
    @FXML private TextField speedField;

    // ── Специфічний блок (генерується динамічно) ──
    @FXML private VBox specificFields;

    // ── Помилка / Збереження ──
    @FXML private Label  errorLabel;
    @FXML private Button saveBtn;

    // ── Стан ──
    private Plane        existingPlane;   // null → режим «Додати»
    private MainController mainController;

    // Специфічні поля для PassengerPlane
    private TextField capacityField;
    private CheckBox  businessCheckBox;
    private TextField businessSeatsField;

    // Специфічні поля для CargoPlane
    private TextField payloadField;

    // Специфічні поля для Helicopter
    private TextField heliCapField;
    private TextField altitudeField;
    private CheckBox  hoistCheckBox;

    private Stage dialogStage;
    private final PlaneDao planeDao = new PlaneDao();

    // ─────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Перебудовуємо блок специфічних полів при зміні типу
        rbPassenger.setOnAction(e -> buildSpecificFields());
        rbCargo    .setOnAction(e -> buildSpecificFields());
        rbHeli     .setOnAction(e -> buildSpecificFields());

        buildSpecificFields(); // за замовч. — Пасажирський
    }

    /** Викликається з MainController перед відкриттям діалогу редагування. */
    public void loadPlane(Plane plane) {
        this.existingPlane = plane;
        dialogTitle.setText("Редагувати апарат");
        saveBtn.setText("Зберегти зміни");

        // Загальні поля
        modelField.setText(plane.getModel());
        fuelField .setText(String.valueOf(plane.getFuelConsumption()));
        rangeField.setText(String.valueOf(plane.getFlightRangeKm()));
        speedField.setText(String.valueOf(plane.getCruiseSpeedKmh()));

        // Обираємо тип та заповнюємо специфічні поля
        if (plane instanceof PassengerPlane pp) {
            rbPassenger.setSelected(true);
            buildSpecificFields();
            capacityField.setText(String.valueOf((int) pp.getCapacity()));
            businessCheckBox.setSelected(pp.isHasBusinessClass());
            businessSeatsField.setText(String.valueOf(pp.getBusinessSeats()));
            businessSeatsField.setDisable(!pp.isHasBusinessClass());

        } else if (plane instanceof CargoPlane cp) {
            rbCargo.setSelected(true);
            buildSpecificFields();
            payloadField.setText(String.valueOf(cp.getCapacity()));

        } else if (plane instanceof Helicopter h) {
            rbHeli.setSelected(true);
            buildSpecificFields();
            heliCapField.setText(String.valueOf((int) h.getCapacity()));
            altitudeField.setText(String.valueOf(h.getMaxAltitudeM()));
            hoistCheckBox.setSelected(h.isHasHoist());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(MainController mc) {
        this.mainController = mc;
    }

    // ══════════════════════════════════════════════
    //  Динамічна форма специфічних полів
    // ══════════════════════════════════════════════

    private void buildSpecificFields() {
        specificFields.getChildren().clear();

        if (rbPassenger.isSelected()) {
            buildPassengerFields();
        } else if (rbCargo.isSelected()) {
            buildCargoFields();
        } else {
            buildHeliFields();
        }
    }

    private void buildPassengerFields() {
        capacityField = new TextField();
        capacityField.setPromptText("Наприклад: 180");

        businessCheckBox   = new CheckBox("Є бізнес-клас");
        businessSeatsField = new TextField();
        businessSeatsField.setPromptText("Кількість місць бізнес-класу");
        businessSeatsField.setDisable(true);

        businessCheckBox.selectedProperty().addListener((obs, old, val) ->
                businessSeatsField.setDisable(!val));

        specificFields.getChildren().addAll(
                labeled("Кількість місць *", capacityField),
                businessCheckBox,
                labeled("Місця бізнес-класу", businessSeatsField)
        );
    }

    private void buildCargoFields() {
        payloadField = new TextField();
        payloadField.setPromptText("Наприклад: 50.0");
        specificFields.getChildren().add(labeled("Вантажопідйомність (тонн) *", payloadField));
    }

    private void buildHeliFields() {
        heliCapField  = new TextField();
        heliCapField.setPromptText("Наприклад: 24");
        altitudeField = new TextField();
        altitudeField.setPromptText("Наприклад: 6000");
        hoistCheckBox = new CheckBox("Є рятувальна лебідка");

        specificFields.getChildren().addAll(
                labeled("Кількість місць *", heliCapField),
                labeled("Макс. висота (м) *", altitudeField),
                hoistCheckBox
        );
    }

    /** Створює VBox «label + control» для використання у формі. */
    private VBox labeled(String labelText, Control control) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("field-label");
        VBox box = new VBox(4, lbl, control);
        return box;
    }

    // ══════════════════════════════════════════════
    //  Обробники кнопок
    // ══════════════════════════════════════════════

    @FXML
    private void onSave() {
        hideError();

        // Загальна валідація
        String model = modelField.getText().trim();
        if (model.isEmpty()) { showError("Вкажіть модель апарату."); return; }

        int fuel, range;
        double speed;
        try { fuel  = Integer.parseInt(fuelField.getText().trim()); }
        catch (NumberFormatException e) { showError("Витрата пального — ціле число."); return; }
        try { range = Integer.parseInt(rangeField.getText().trim()); }
        catch (NumberFormatException e) { showError("Дальність — ціле число."); return; }
        try { speed = Double.parseDouble(speedField.getText().trim().replace(",", ".")); }
        catch (NumberFormatException e) { showError("Швидкість — числове значення."); return; }

        if (fuel <= 0 || range <= 0 || speed <= 0) {
            showError("Витрата, дальність і швидкість мають бути > 0.");
            return;
        }

        // Залежно від режиму — редагуємо або додаємо
        if (existingPlane != null) {
            applyEdits(existingPlane, model, fuel, range, speed);
            // Тут варто додати planeDao.updatePlane(existingPlane), якщо створите такий метод
        } else {
            Plane newPlane = createPlane(model, fuel, range, speed);
            if (newPlane != null) {
                // 1. Зберігаємо в базу даних
                planeDao.savePlane(newPlane);

                // 2. Додаємо в локальний список (опціонально, бо refreshAll все одно перечитає БД)
                AppContext.airline.addPlane(newPlane);
            }
        }

        if (mainController != null) mainController.refreshAll();
        closeDialog();
    }

    @FXML
    private void onCancel() {
        closeDialog();
    }

    // ══════════════════════════════════════════════
    //  Приватні хелпери
    // ══════════════════════════════════════════════

    /** Редагує поля існуючого літака (без зміни типу). */
    private void applyEdits(Plane plane, String model, int fuel, int range, double speed) {
        plane.setModel(model);
        plane.setFuelConsumption(fuel);
        plane.setFlightRangeKm(range);
        plane.setCruiseSpeedKmh(speed);

        if (plane instanceof PassengerPlane pp) {
            double cap = parseDoubleOrShow("Кількість місць — ціле число."); if (cap < 0) return;
            pp.setCapacity(cap);
            pp.setHasBusinessClass(businessCheckBox.isSelected());
            if (businessCheckBox.isSelected()) {
                try { pp.setBusinessSeats(Integer.parseInt(businessSeatsField.getText().trim())); }
                catch (NumberFormatException ignored) {}
            }
        } else if (plane instanceof CargoPlane cp) {
            double payload = parseDoubleOrShow("Вантажопідйомність — числове значення."); if (payload < 0) return;
            cp.setCapacity(payload);
        } else if (plane instanceof Helicopter h) {
            double cap = parseDoubleField(heliCapField, "Кількість місць — ціле число."); if (cap < 0) return;
            h.setCapacity(cap);
            try { h.setMaxAltitudeM(Integer.parseInt(altitudeField.getText().trim())); }
            catch (NumberFormatException e) { showError("Висота — ціле число."); return; }
            h.setHasHoist(hoistCheckBox.isSelected());
        }
        AppContext.logger.info("Відредаговано апарат: " + model);
    }

    /** Створює новий літак за обраним типом. */
    private Plane createPlane(String model, int fuel, int range, double speed) {
        // 1. Оголошуємо ID один раз на початку методу
        int id = (existingPlane != null) ? existingPlane.getId() : 0;

        if (rbPassenger.isSelected()) {
            double cap = parseDoubleField(capacityField, "Місткість — число."); if (cap < 0) return null;
            boolean biz = businessCheckBox.isSelected();
            int bSeats = 0;
            if (biz) {
                try { bSeats = Integer.parseInt(businessSeatsField.getText().trim()); }
                catch (NumberFormatException e) { showError("Бізнес-місця — ціле число."); return null; }
            }
            return new PassengerPlane(id, model, fuel, range, speed, cap, biz, bSeats);

        } else if (rbCargo.isSelected()) {
            double pld = parseDoubleOrShow("Вантажопідйомність — число."); if (pld < 0) return null;
            return new CargoPlane(id, model, fuel, range, speed, pld);

        } else { // Гелікоптер
            double cap = parseDoubleField(heliCapField, "Кількість місць — ціле число."); if (cap < 0) return null;
            int alt;
            try { alt = Integer.parseInt(altitudeField.getText().trim()); }
            catch (NumberFormatException e) { showError("Висота — ціле число."); return null; }
            // Тепер id доступний і тут
            return new Helicopter(id, model, fuel, range, speed, (int) cap, alt, hoistCheckBox.isSelected());
        }
    }
    /* private Plane createPlane(String model, int fuel, int range, double speed, int id) {
        if (rbPassenger.isSelected()) {
            double cap = parseDoubleField(capacityField, "Кількість місць — ціле число."); if (cap < 0) return null;
            boolean hasBiz = businessCheckBox.isSelected();
            int bizSeats = 0;
            if (hasBiz) {
                try { bizSeats = Integer.parseInt(businessSeatsField.getText().trim()); }
                catch (NumberFormatException e) { showError("Місця бізнес-класу — ціле число."); return null; }
            }
            return new PassengerPlane(id, model, fuel, range, speed, cap, hasBiz, bizSeats);

        } else if (rbCargo.isSelected()) {
            double payload = parseDoubleField(payloadField, "Вантажопідйомність — числове значення."); if (payload < 0) return null;
            return new CargoPlane(0, model, fuel, range, speed, payload);

        } else { // Helicopter
            double cap = parseDoubleField(heliCapField, "Кількість місць — ціле число."); if (cap < 0) return null;
            int alt;
            try { alt = Integer.parseInt(altitudeField.getText().trim()); }
            catch (NumberFormatException e) { showError("Висота — ціле число."); return null; }
            return new Helicopter(id, model, fuel, range, speed, (int) cap, alt, hoistCheckBox.isSelected());
        }
    } */

    private double parseDoubleField(TextField field, String errorMsg) {
        try {
            double v = Double.parseDouble(field.getText().trim().replace(",", "."));
            if (v <= 0) { showError("Значення має бути > 0."); return -1; }
            return v;
        } catch (NumberFormatException e) { showError(errorMsg); return -1; }
    }

    /** Зчитує payloadField (використовується при редагуванні CargoPlane). */
    private double parseDoubleOrShow(String msg) {
        try {
            return Double.parseDouble(payloadField.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) { showError(msg); return -1; }
    }

    private void showError(String msg) {
        errorLabel.setText("⚠  " + msg);
        errorLabel.setManaged(true);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);
    }

    private void closeDialog() {
        ((Stage) saveBtn.getScene().getWindow()).close();
    }
}
