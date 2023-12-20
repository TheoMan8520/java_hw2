module app.todolist {
    requires javafx.controls;
    requires javafx.fxml;


    opens app.todolist to javafx.fxml;
    exports app.todolist;
}