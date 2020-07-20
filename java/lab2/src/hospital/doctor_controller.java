package hospital;

import hospital.module.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class doctor_controller implements Initializable {
    static Statement stmt = null, stmt2 = null;

    ObservableList<PatientForList> PatientList = FXCollections.observableArrayList();
    ObservableList<Income> IncomeList = FXCollections.observableArrayList();

    @FXML
    TableView<PatientForList> table_reg;
    @FXML
    TableView<Income> table_income;
    @FXML
    private Button btn_logout, btn_exit;
    @FXML
    private TableColumn<?, ?> col_regnum, col_patname, col_regtime, col_regtype, col_officename, col_docnum, col_docname, col_regtype2, col_regcount, col_income, col_valid;
    @FXML
    TextField text_begin, text_end;
    @FXML
    DatePicker date_end, date_begin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date_begin.setValue(LocalDate.now());
        date_end.setValue(LocalDate.now());
        col_regnum.setCellValueFactory(new PropertyValueFactory<>("regNum"));
        col_patname.setCellValueFactory(new PropertyValueFactory<>("patName"));
        col_regtime.setCellValueFactory(new PropertyValueFactory<>("regTime"));
        col_regtype.setCellValueFactory(new PropertyValueFactory<>("regType"));
        col_valid.setCellValueFactory(new PropertyValueFactory<>("valid"));

        col_officename.setCellValueFactory(new PropertyValueFactory<>("officeName"));
        col_docnum.setCellValueFactory(new PropertyValueFactory<>("docNum"));
        col_docname.setCellValueFactory(new PropertyValueFactory<>("docName"));
        col_regtype2.setCellValueFactory(new PropertyValueFactory<>("regType"));
        col_regcount.setCellValueFactory(new PropertyValueFactory<>("regCount"));
        col_income.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
    }

    @FXML
    private void on_mouse_entered(Event event) {
        try {
            String time_begin, time_end;
            LocalDate datetmp = date_begin.getValue();
            LocalDate datetmp2 = date_end.getValue();
            if (datetmp == null || datetmp2 == null) {
                time_begin = LocalDate.now().toString();
                time_end = LocalDate.now().toString();
            } else {
                time_begin = datetmp.toString();
                time_end = datetmp2.toString();
            }
            time_begin += " 00:00:00";
            time_end += " 23:59:59";
            PatientList.clear();
            IncomeList.clear();
            stmt = MainApp.conn.createStatement();
            stmt2 = MainApp.conn.createStatement();
            String docid = MainApp.pat_doc_num;
            String sql = "select reg_id,catid,pid,"
                    + "reg_datetime,unreg from register where "
                    + "docid= '" + docid + "'";
            ResultSet rs = stmt.executeQuery(sql);
            String reg_id = null, catid = null, pid = null;
            String reg_datetime = null, pat_name = null, expertstr = null;
            String office_name = null, doc_name = null, unreg_str = null;
            boolean expert = true, unreg = false;
            ResultSet rs2 = null;
            int regcount = 0;
            double totalcost = 0;
            ;
            while (rs.next()) {
                reg_id = rs.getString("reg_id");
                catid = rs.getString("catid");
                pid = rs.getString("pid");
                reg_datetime = rs.getString("reg_datetime");
                unreg = rs.getBoolean("unreg");
                unreg_str = unreg ? "否" : "是";

                sql = "select speciallist from register_category where "
                        + "catid= '" + catid + "'";
                rs2 = stmt2.executeQuery(sql);
                if (rs2.next()) {
                    expert = rs2.getBoolean("speciallist");
                }

                sql = "select name from patient where"
                        + " pid= '" + pid + "'";
                rs2 = stmt2.executeQuery(sql);
                if (rs2.next()) {
                    pat_name = rs2.getString("name");
                }
                expertstr = expert ? "专家号" : "普通号";
                PatientList.add(new PatientForList(reg_id, pat_name,
                        reg_datetime, expertstr, unreg_str));
            }

            sql = "select department.name,register.docid,"
                    + "doctor.name,register_category.speciallist,"
                    + "count(register.docid),sum(register.reg_fee) "
                    + "from register,register_category,doctor,department "
                    + "where "
                    + "register.catid=register_category.catid "
                    + "and doctor.docid=register.docid "
                    + "and department.depid=register_category.depid "
                    + "and register.reg_datetime>='" + time_begin + "' "
                    + "and register.reg_datetime<='" + time_end + "'"
                    + "group by register.docid,register_category.speciallist";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                office_name = rs.getString("department.name");
                docid = rs.getString("register.docid");
                doc_name = rs.getString("doctor.name");

                expert = rs.getBoolean("register_category.speciallist");
                regcount = rs.getInt("count(register.docid)");
                totalcost = rs.getDouble("sum(register.reg_fee)");
                expertstr = expert ? "专家号" : "普通号";
                IncomeList.add(new Income(office_name, docid, doc_name,
                        expertstr, Integer.toString(regcount), Double.toString(totalcost)));
            }
            if (rs != null)
                rs.close();
            if (rs2 != null)
                rs2.close();
            stmt2.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            table_reg.setItems(PatientList);
            table_income.setItems(IncomeList);
        }
    }

    @FXML
    private void on_btn_exit_clicked(ActionEvent event) {
        Event.fireEvent(MainApp.getPrimaryStage(),
                new WindowEvent(MainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    private void on_btn_logout_clicked(ActionEvent event) {
        MainApp.setLoginUi();
    }
}
