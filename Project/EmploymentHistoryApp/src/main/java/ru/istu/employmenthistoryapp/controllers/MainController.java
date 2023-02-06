package ru.istu.employmenthistoryapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.istu.employmenthistoryapp.DataBase;
import ru.istu.employmenthistoryapp.Employee;
import ru.istu.employmenthistoryapp.Job;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label seniority;
    @FXML
    private TextField surName, name, patronymic, profession, jobInfo, groundsJob;
    @FXML
    private DatePicker dateOfBirth, dateAddJobInfo;
    @FXML
    private ChoiceBox<String> education;
    @FXML
    private TableView<Job> jobInfoTable, existJobInfoTable;
    @FXML
    private TableView<Employee> existEmployeesTable;

    private final String[] formatEducation = {"Начальное общее", "Основное общее", "Среднее общее", "Среднее профессиональное", "Высшее - бакалавриат", "Высшее - специалитет", "Высшее - магистратура", "Высшее - подготовка кадров высше квалификации"};
    private DataBase db;
    private List<Job> jobList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        education.getItems().addAll(formatEducation);
        education.getSelectionModel().select(2);

        dateOfBirth.setValue(LocalDate.now().minusYears(16));
        dateAddJobInfo.setValue(LocalDate.now());

        db = new DataBase();

        TableColumn<Employee, String> surNameCol = new TableColumn<>("Фамилия");
        TableColumn<Employee, String> nameCol = new TableColumn<>("Имя");
        TableColumn<Employee, String> patronymicCol = new TableColumn<>("Отчество");
        TableColumn<Employee, String> dateBirthCol = new TableColumn<>("Дата рождения");
        TableColumn<Employee, String> educationCol = new TableColumn<>("Образование");
        TableColumn<Employee, String> professionCol = new TableColumn<>("Профессия, специальность");

        surNameCol.setCellValueFactory(new PropertyValueFactory<>("surName"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymicCol.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        dateBirthCol.setCellValueFactory(new PropertyValueFactory<>("dateBirth"));
        educationCol.setCellValueFactory(new PropertyValueFactory<>("education"));
        professionCol.setCellValueFactory(new PropertyValueFactory<>("profession"));

        surNameCol.setPrefWidth(70);
        nameCol.setPrefWidth(70);
        patronymicCol.setPrefWidth(70);
        dateBirthCol.setPrefWidth(70);
        educationCol.setPrefWidth(100);
        professionCol.setPrefWidth(100);

        existEmployeesTable.getColumns().add(surNameCol);
        existEmployeesTable.getColumns().add(nameCol);
        existEmployeesTable.getColumns().add(patronymicCol);
        existEmployeesTable.getColumns().add(dateBirthCol);
        existEmployeesTable.getColumns().add(educationCol);
        existEmployeesTable.getColumns().add(professionCol);

        TableColumn<Job, String> dateCol = new TableColumn<>("Дата");
        TableColumn<Job, String> jobInfoCol = new TableColumn<>("Сведения о приём на работу, о переводах на другую работу и об увольнениях");
        TableColumn<Job, String> groundsCol = new TableColumn<>("На основании чего внесена запись");

        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        jobInfoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
        groundsCol.setCellValueFactory(new PropertyValueFactory<>("grounds"));

        dateCol.setPrefWidth(80);
        jobInfoCol.setPrefWidth(500);
        groundsCol.setPrefWidth(240);

        jobInfoTable.getColumns().add(dateCol);
        jobInfoTable.getColumns().add(jobInfoCol);
        jobInfoTable.getColumns().add(groundsCol);

        TableColumn<Job, String> dateCol_1 = new TableColumn<>("Дата");
        TableColumn<Job, String> jobInfoCol_1 = new TableColumn<>("Сведения о приём на работу, о переводах на другую работу и об увольнениях");
        TableColumn<Job, String> groundsCol_1 = new TableColumn<>("На основании чего внесена запись");

        dateCol_1.setCellValueFactory(new PropertyValueFactory<>("date"));
        jobInfoCol_1.setCellValueFactory(new PropertyValueFactory<>("info"));
        groundsCol_1.setCellValueFactory(new PropertyValueFactory<>("grounds"));

        dateCol_1.setPrefWidth(80);
        jobInfoCol_1.setPrefWidth(500);
        groundsCol_1.setPrefWidth(240);

        existJobInfoTable.getColumns().add(dateCol_1);
        existJobInfoTable.getColumns().add(jobInfoCol_1);
        existJobInfoTable.getColumns().add(groundsCol_1);

        TableView.TableViewSelectionModel<Employee> selectionModel = existEmployeesTable.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if (newVal != null) {
                List<Job> jobList = db.getJobHistory(newVal.getId());
                seniority.setText(CalculateSeniority(jobList));
                ObservableList<Job> observableList = FXCollections.observableArrayList(jobList);
                existJobInfoTable.setItems(observableList);
//                System.out.println("Selected: " + newVal.getId());
            }
        });

        surName.setTextFormatter(new TextFormatter<String>(change -> {
            if (!change.getControlNewText().matches("^[a-zA-Zа-яА-Я]{0,128}$")) return null;
            else return change;
        }));
        name.setTextFormatter(new TextFormatter<String>(change -> {
            if (!change.getControlNewText().matches("^[a-zA-Zа-яА-Я]{0,128}$")) return null;
            else return change;
        }));
        patronymic.setTextFormatter(new TextFormatter<String>(change -> {
            if (!change.getControlNewText().matches("^[a-zA-Zа-яА-Я]{0,128}$")) return null;
            else return change;
        }));

        jobList = new ArrayList<>();
    }

    public void addEmployeeHistory() {
        if ((surName != null && surName.getText().trim().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Не введены данные в поле \"Фамилия\"!");
            surName.requestFocus();
            return;
        }
        if ((name != null && name.getText().trim().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Не введены данные в поле \"Имя\"!");
            name.requestFocus();
            return;
        }
        if (jobList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не введены сведения о работе!");
            return;
        }

        Integer lid = db.getLastEmployeeId();
        Employee tempEmployee = new Employee(++lid, surName.getText(), name.getText(), patronymic.getText(), dateOfBirth.getValue().toString(), String.valueOf(education.getValue()), profession.getText());
        db.addEmployee(tempEmployee);
        db.addJobHistory(tempEmployee.getId(), jobList);

        surName.clear();
        name.clear();
        patronymic.clear();
        dateOfBirth.setValue(LocalDate.now().minusYears(16));
        education.getSelectionModel().select(2);
        profession.clear();

        jobList.clear();
        jobInfoTable.getItems().clear();
    }

    public void addJobDetails() {
        if ((jobInfo != null && jobInfo.getText().trim().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Не введены данные в поле \"Сведения о приеме на работу\"!");
            jobInfo.requestFocus();
            return;
        }
        if ((groundsJob != null && groundsJob.getText().trim().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Не введены данные в поле \"На основании чего внесена запись\"!");
            groundsJob.requestFocus();
            return;
        }

        jobList.add(new Job(dateAddJobInfo.getValue().toString(), jobInfo.getText(), groundsJob.getText()));

        ObservableList<Job> observableList = FXCollections.observableArrayList(jobList);
        jobInfoTable.setItems(observableList);

        dateAddJobInfo.setValue(LocalDate.now());
        jobInfo.clear();
        groundsJob.clear();
    }

    public void refreshJobsHistoryTable() {
        existEmployeesTable.getItems().clear();
        List<Employee> emplList = db.getAllEmployee();

        ObservableList<Employee> observableList = FXCollections.observableArrayList(emplList);
        existEmployeesTable.setItems(observableList);
    }

    private String CalculateSeniority(List<Job> jobList) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        int years = 0, months = 0, days = 0;

        Period period = Period.between(LocalDate.parse(jobList.get(0).getDate(), formatter), LocalDate.parse(jobList.get(jobList.size() - 1).getDate(), formatter));
        years = period.getYears();
        months = period.getMonths();
        days = period.getDays();

//        for (int i = 0; i < jobList.size() - 1; i++) {
//            firstDate = LocalDate.parse(jobList.get(i).getDate(), formatter);
//            lasDate = LocalDate.parse(jobList.get(i + 1).getDate(), formatter);
//
//            Period period = Period.between(firstDate, lasDate);
//            years += period.getYears();
//            months += period.getMonths();
//            days += period.getDays();
//        }
        return "Трудовой стаж:\n" + years + " лет\n" + months + " месяцев\n" + days + " дней";
    }
}