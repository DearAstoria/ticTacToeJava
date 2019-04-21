package sceneLoader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class SceneLoader {



    public static void loadFXML(MouseEvent click, Object object, String file) throws IOException
    {

        // load the next scene
        Parent GameScreenParent = FXMLLoader.load(object.getClass().getResource(file));


        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)((Node)click.getSource()).getScene().getWindow();

        // set stage to display the next scene
        window.setScene(new Scene(GameScreenParent));

        window.show();
    }


    public static void loadFXML(Node node, URL file) throws IOException
    {


        // load the next scene
        Parent parent = FXMLLoader.load(file);


        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)node.getScene().getWindow();

        // set stage to display the next scene
        window.setScene(new Scene(parent));

        window.show();
    }

    public static void loadFXML(Node node, Parent parent) throws IOException
    {

        // get the stage... getSource: get object that was clicked on (the button) from the event, getScene: get the scene the button is a part of, getWindow: get the stage the scene is a part of
        Stage window = (Stage)node.getScene().getWindow();

        // set stage to display the next scene
        window.setScene(new Scene(parent));

        window.show();
    }



}
