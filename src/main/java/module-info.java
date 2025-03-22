module com.rocs.infirmaryapplication {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires org.slf4j;
    requires org.apache.logging.log4j.slf4j2.impl;
    requires com.oracle.database.jdbc;


    opens com.rocs.infirmary.application to javafx.fxml;
    exports com.rocs.infirmary.application;
}