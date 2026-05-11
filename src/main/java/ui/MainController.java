package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.AppContext;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // ── Header ──
    @FXML private Label airlineNameLabel;
    @FXML private Label fleetCountLabel;

    // ── Fleet tab ──
    @FXML private TableView<PlaneRow>          fleetTable;
    @FXML private TableColumn<PlaneRow,String>  colModel;
    @FXML private TableColumn<PlaneRow,String>  colType;
    @FXML private TableColumn<PlaneRow,Integer> colRange;
    @FXML private TableColumn<PlaneRow,Integer> colFuel;
    @FXML private TableColumn<PlaneRow,Double>  colSpeed;
    @FXML private TableColumn<PlaneRow,String>  colCapacity;
    @FXML private TextField searchField;
    @FXML private Label statPassenger;
    @FXML private Label statCargo;
    @FXML private Label statHeli;
    @FXML private Label statTotalCap;
    @FXML private Label statTotalPayload;

    // ── Calculator tab ──
    @FXML private ComboBox<String> calcPlaneCombo;
    @FXML private TextField        calcDistanceField;
    @FXML private TextField        calcPriceField;
    @FXML private Label            resTime;
    @FXML private Label            resFuel;
    @FXML private Label            resCost;
    @FXML private Label            resStatus;

    // ── Analytics tab ──
    @FXML private ComboBox<String>             sortCombo;
    @FXML private TextField                    filterMinField;
    @FXML private TextField                    filterMaxField;
    @FXML private TextField                    filterRangeField;
    @FXML private TableView<PlaneRow>          sortTable;
    @FXML private TableColumn<PlaneRow,String>  sColModel;
    @FXML private TableColumn<PlaneRow,String>  sColType;
    @FXML private TableColumn<PlaneRow,Integer> sColRange;
    @FXML private TableColumn<PlaneRow,Integer> sColFuel;
    @FXML private TableColumn<PlaneRow,String>  sColCapacity;

    // ── Data ──
    private final ObservableList<PlaneRow> masterList = FXCollections.observableArrayList();
    private FilteredList<PlaneRow> filteredList;

    // ─────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupFleetTable();
        setupAnalyticsTable();
        setupSearch();
        setupSortCombo();
        setupCalcCombo();
        refreshAll();
    }

    // ══════════════════════════════════════════════
    //  FLEET TABLE
    // ══════════════════════════════════════════════

    private void setupFleetTable() {
        colModel   .setCellValueFactory(new PropertyValueFactory<>("model"));
        colType    .setCellValueFactory(new PropertyValueFactory<>("type"));
        colRange   .setCellValueFactory(new PropertyValueFactory<>("range"));
        colFuel    .setCellValueFactory(new PropertyValueFactory<>("fuel"));
        colSpeed   .setCellValueFactory(new PropertyValueFactory<>("speed"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        // Виділяємо рядки з малою дальністю (< 500 км) жовтим
        fleetTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(PlaneRow row, boolean empty) {
                super.updateItem(row, empty);
                getStyleClass().removeAll("row-warning");
                if (!empty && row != null && row.getPlane().getFlightRangeKm() < 500) {
                    getStyleClass().add("row-warning");
                }
            }
        });

        filteredList = new FilteredList<>(masterList, p -> true);
        fleetTable.setItems(filteredList);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, val) -> {
            String lower = val == null ? "" : val.toLowerCase();
            filteredList.setPredicate(row ->
                    lower.isEmpty() || row.getPlane().getModel().toLowerCase().contains(lower)
            );
        });
    }

    // ══════════════════════════════════════════════
    //  ANALYTICS TABLE
    // ══════════════════════════════════════════════

    private void setupAnalyticsTable() {
        sColModel   .setCellValueFactory(new PropertyValueFactory<>("model"));
        sColType    .setCellValueFactory(new PropertyValueFactory<>("type"));
        sColRange   .setCellValueFactory(new PropertyValueFactory<>("range"));
        sColFuel    .setCellValueFactory(new PropertyValueFactory<>("fuel"));
        sColCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        sortTable.setItems(FXCollections.observableArrayList());
    }

    private void setupSortCombo() {
        sortCombo.setItems(FXCollections.observableArrayList(
                "За дальністю (зростання)",
                "За витратою пального (зростання)"
        ));
    }

    @FXML
    private void onApplyFilter() {
        List<Plane> result;
        String sortVal = sortCombo.getValue();

        // Базова вибірка
        if ("За дальністю (зростання)".equals(sortVal)) {
            result = AppContext.airline.getSortedByRange();
        } else if ("За витратою пального (зростання)".equals(sortVal)) {
            result = AppContext.airline.getSortedByFuelConsumption();
        } else {
            result = AppContext.airline.getFleet();
        }

        // Фільтр по пальному
        String minS = filterMinField.getText().trim();
        String maxS = filterMaxField.getText().trim();
        if (!minS.isEmpty() || !maxS.isEmpty()) {
            int min = minS.isEmpty() ? 0      : parseInt(minS, 0);
            int max = maxS.isEmpty() ? 999999 : parseInt(maxS, 999999);
            result = result.stream()
                    .filter(p -> p.getFuelConsumption() >= min && p.getFuelConsumption() <= max)
                    .toList();
        }

        // Фільтр по дальності
        String rangeS = filterRangeField.getText().trim();
        if (!rangeS.isEmpty()) {
            int minRange = parseInt(rangeS, 0);
            result = result.stream()
                    .filter(p -> p.getFlightRangeKm() >= minRange)
                    .toList();
        }

        ObservableList<PlaneRow> rows = FXCollections.observableArrayList();
        result.forEach(p -> rows.add(new PlaneRow(p)));
        sortTable.setItems(rows);
    }

    @FXML
    private void onResetFilter() {
        sortCombo.setValue(null);
        filterMinField.clear();
        filterMaxField.clear();
        filterRangeField.clear();
        sortTable.setItems(FXCollections.observableArrayList());
    }

    // ══════════════════════════════════════════════
    //  CALCULATOR
    // ══════════════════════════════════════════════

    private void setupCalcCombo() {
        refreshCalcCombo();
    }

    private void refreshCalcCombo() {
        calcPlaneCombo.setItems(FXCollections.observableArrayList(
                AppContext.airline.getFleet().stream()
                        .map(Plane::getModel)
                        .toList()
        ));
    }

    @FXML
    private void onCalculate() {
        String selectedModel = calcPlaneCombo.getValue();
        if (selectedModel == null) {
            setCalcError("Оберіть літальний апарат.");
            return;
        }

        Plane plane = AppContext.airline.getFleet().stream()
                .filter(p -> p.getModel().equals(selectedModel))
                .findFirst().orElse(null);
        if (plane == null) return;

        int distance;
        try { distance = Integer.parseInt(calcDistanceField.getText().trim()); }
        catch (NumberFormatException e) { setCalcError("Введіть коректну дистанцію (ціле число)."); return; }

        if (distance <= 0) { setCalcError("Дистанція має бути більше 0."); return; }

        if (distance > plane.getFlightRangeKm()) {
            resTime.setText("—");
            resFuel.setText("—");
            resCost.setText("—");
            resStatus.setText("🚫 Рейс неможливий! Дистанція " + distance +
                    " км перевищує макс. дальність " + plane.getFlightRangeKm() + " км.");
            resStatus.getStyleClass().setAll("result-status", "status-error");
            return;
        }

        double hours     = (double) distance / plane.getCruiseSpeedKmh();
        double fuelNeeded = hours * plane.getFuelConsumption();

        resTime.setText(String.format("%.2f год (%d хв)", hours, (int)(hours * 60)));
        resFuel.setText(String.format("%.1f кг", fuelNeeded));

        String priceText = calcPriceField.getText().trim();
        if (!priceText.isEmpty()) {
            try {
                double price = Double.parseDouble(priceText.replace(",", "."));
                resCost.setText(String.format("$%.2f", fuelNeeded * price));
            } catch (NumberFormatException e) {
                resCost.setText("Невірна ціна");
            }
        } else {
            resCost.setText("—");
        }

        resStatus.setText("✅ Рейс технічно можливий.");
        resStatus.getStyleClass().setAll("result-status", "status-ok");
        AppContext.logger.info("Калькулятор: " + plane.getModel() + " → " + distance + " км");
    }

    private void setCalcError(String msg) {
        resStatus.setText("⚠  " + msg);
        resStatus.getStyleClass().setAll("result-status", "status-error");
    }

    // ══════════════════════════════════════════════
    //  ADD / EDIT / DELETE
    // ══════════════════════════════════════════════

    @FXML
    private void onAddPlane() {
        openPlaneDialog(null);
    }

    @FXML
    private void onEditPlane() {
        PlaneRow selected = fleetTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Оберіть апарат у таблиці для редагування.");
            return;
        }
        openPlaneDialog(selected.getPlane());
    }

    @FXML
    private void onDeletePlane() {
        PlaneRow selected = fleetTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Оберіть апарат у таблиці для видалення.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Підтвердження");
        confirm.setHeaderText("Видалити «" + selected.getPlane().getModel() + "»?");
        confirm.setContentText("Цю дію неможливо скасувати.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int idx = AppContext.airline.getFleet().indexOf(selected.getPlane());
            AppContext.airline.deletePlane(idx);
            AppContext.logger.info("Видалено: " + selected.getPlane().getModel());
            refreshAll();
        }
    }

    private void openPlaneDialog(Plane existingPlane) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/PlaneDialog.fxml"));
            VBox root = loader.load();
            PlaneDialogController ctrl = loader.getController();
            ctrl.setMainController(this);
            if (existingPlane != null) ctrl.loadPlane(existingPlane);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(existingPlane == null ? "Додати апарат" : "Редагувати апарат");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            AppContext.logger.severe("Помилка відкриття діалогу: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════
    //  REFRESH
    // ══════════════════════════════════════════════

    public void refreshAll() {
        masterList.clear();
        AppContext.airline.getFleet().forEach(p -> masterList.add(new PlaneRow(p)));

        int total = AppContext.airline.getFleet().size();
        fleetCountLabel.setText(total + " апаратів");
        airlineNameLabel.setText(AppContext.airline.getName());

        statPassenger.setText("Пасажирських: "  + AppContext.airline.getTotalPassengerPlanes());
        statCargo    .setText("Вантажних: "      + AppContext.airline.getTotalCargoPlanes());
        statHeli     .setText("Гелікоптерів: "   + AppContext.airline.getTotalHelicopters());
        statTotalCap .setText("Загальна місткість: " + (int) AppContext.airline.getTotalCapacity() + " осіб");
        statTotalPayload.setText("Вантаж: " + AppContext.airline.getTotalPayload() + " тонн");

        refreshCalcCombo();
    }

    // ── Utils ──
    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private int parseInt(String s, int fallback) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return fallback; }
    }
}
