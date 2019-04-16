module TicTacToe {
    requires javafx.fxml;
    requires javafx.controls;
    opens client;
    opens client.gui_resources;
    opens client.gui_controllers;
    opens client.raw_data;
    opens pubnubWrappers;
}