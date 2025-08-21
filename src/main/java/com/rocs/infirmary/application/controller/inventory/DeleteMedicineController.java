package com.rocs.infirmary.application.controller.inventory;

import com.rocs.infirmary.application.data.model.inventory.medicine.Medicine;
import com.rocs.infirmary.application.module.inventory.management.application.InventoryManagementApplication;
import static com.rocs.infirmary.application.controller.helper.ControllerHelper.showDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code DeleteMedicineController} is used to handle event processes of the Medicine when deleting Items
 **/
public class DeleteMedicineController {
    @FXML
    private Label inventoryDeleteLabelA;
    @FXML
    private GridPane medicineListContainer;
    private AddInventoryController parentController;
    private final InventoryManagementApplication inventoryManagementApplication = new InventoryManagementApplication();
    private List<Medicine> medicineList = new ArrayList<>();
    /**
     * this method display the medicine from medicine table to be deleted
     * @param selectedMedicines is a list that provide attributes the selected medicine
     **/
    public void showMedicineList(List<Medicine> selectedMedicines) {
        medicineListContainer.getChildren().clear();
        medicineListContainer.getRowConstraints().clear();
        medicineListContainer.getColumnConstraints().clear();

        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.SOMETIMES);
        column.setMinWidth(10.0);
        column.setPrefWidth(100.0);
        medicineListContainer.getColumnConstraints().add(column);

        int rowIndex = 0;
        for (Medicine med : selectedMedicines) {
            GridPane innerGrid = new GridPane();
            innerGrid.setPrefWidth(441);
            innerGrid.setPrefHeight(103);
            innerGrid.setHgap(10);
            innerGrid.setVgap(5);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setHgrow(Priority.SOMETIMES);
            col1.setMaxWidth(219.67);
            col1.setMinWidth(10.0);
            col1.setPrefWidth(33.67);

            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.SOMETIMES);
            col2.setMaxWidth(446.3333377838135);
            col2.setMinWidth(10.0);
            col2.setPrefWidth(428.33);

            innerGrid.getColumnConstraints().addAll(col1, col2);

            RowConstraints row1 = new RowConstraints();
            row1.setVgrow(Priority.SOMETIMES);
            row1.setMaxHeight(27.33);
            row1.setPrefHeight(20.0);

            RowConstraints row2 = new RowConstraints();
            row2.setVgrow(Priority.SOMETIMES);
            row2.setMaxHeight(68.0);
            row2.setPrefHeight(68.0);

            RowConstraints row3 = new RowConstraints();
            row3.setVgrow(Priority.SOMETIMES);
            row3.setMaxHeight(29.67);
            row3.setPrefHeight(14.33);

            innerGrid.getRowConstraints().addAll(row1, row2, row3);

            Label medLabel = new Label(med.getItemName());
            medLabel.setPrefWidth(220.0);
            medLabel.setPrefHeight(73.0);
            medLabel.setStyle("-fx-font-weight: 700; -fx-font-size: 30px;");

            GridPane.setColumnIndex(medLabel, 1);
            GridPane.setRowIndex(medLabel, 1);

            innerGrid.getChildren().add(medLabel);

            medicineListContainer.add(innerGrid, 0, rowIndex);

            RowConstraints outerRow = new RowConstraints();
            outerRow.setPrefHeight(103);
            outerRow.setVgrow(Priority.SOMETIMES);
            medicineListContainer.getRowConstraints().add(outerRow);
            medicineList.add(med);
            rowIndex++;
        }
    }
    private List<Medicine> findMatchingMedicineFromInventory(List<Medicine> selected, List<Medicine> inventoryMedicine) {
        List<Long> selectedIds = selected.stream()
                .map(Medicine::getMedicineId)
                .toList();

        return inventoryMedicine.stream()
                .filter(med -> selectedIds.contains(med.getMedicineId()))
                .collect(Collectors.toList());
    }
    private boolean deleteMedicines() {

            List<Medicine> inventoryItem = inventoryManagementApplication
                    .getMedicineInventoryFacade()
                    .getAllMedicine();

            List<Medicine> medicinesToDelete = findMatchingMedicineFromInventory(medicineList, inventoryItem);

            boolean medicineDeleted = inventoryManagementApplication
                    .getMedicineInventoryFacade()
                    .deleteMedicineByItemName(medicineList);

            boolean inventoryDeleted = inventoryManagementApplication
                    .getMedicineInventoryFacade()
                    .deleteInventory(medicinesToDelete);

            return medicineDeleted && inventoryDeleted;
    }
    /**
     * this method handles the action triggered when the confirm button is clicked.
     * @param actionEvent the event triggered by the confirm button click
     */
    public void onConfirmButtonClick(ActionEvent actionEvent) {
        if(deleteMedicines()){
            showDialog("Notification","Medicine successfully Deleted");
            if(parentController != null){
                parentController.refresh();
                if(parentController.getParentController() != null) {
                    parentController.getParentController().refresh();
                }
            }
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    /**
     * this method handles the action triggered when the cancel button is clicked.
     * @param actionEvent the event triggered by the confirm button click
     */
    public void onCancelBtnClick(ActionEvent actionEvent) {
        if(parentController != null){
            parentController.refresh();
        }
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    /**
     * this method setup's the parent controller
     * @param parentController the parent AddInventoryController instance to be associated with this controller
     * */
    public void setParentController(AddInventoryController parentController) {
        this.parentController = parentController;
    }
}
