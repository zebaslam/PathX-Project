/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.file;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import pathx.data.PathXDataModel;
import pathx.data.PathXRecord;
import pathx.ui.PathXMiniGame;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;

/**
 *
 * @author zeb
 */
public class PathXFileManager {
    public static String SPACE = " ";
    public static String NEW_LINE = "\n";
    public static String DELIMITER = "|";
    private PathXMiniGame view;
    
        // THIS DOEST THE ACTUAL FILE I/O
    PathX_XMLLevelIO levelIO;

    // THE VIEW AND DATA TO BE UPDATED DURING LOADING
  
    PathXDataModel model;

    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    private File currentFile;
    private String currentFileName;

    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    
    //potential source of ERROR
    //bacon
    public PathXFileManager(PathXMiniGame initMiniGame, PathXDataModel initmodel)
    {
        // KEEP IT FOR LATER
        view = initMiniGame;
        model= (PathXDataModel)view.getDataModel();
        levelIO = new PathX_XMLLevelIO(new File(LEVELS_PATH + LEVEL_SCHEMA));
        
        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
    }
    
     /**
     * This method lets the user open a level saved to a file. It will also make
     * sure data for the current level is not lost.
     */
    public void processOpenLevelRequest(String name)
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            //continueToOpen = promptToSave();
        }

        // IF THE USER REALLY WANTS TO OPEN A LEVEL
        if (continueToOpen)
        {
            // GO AHEAD AND PROCEED MAKING A NEW LEVEL
            //continueToOpen = promptToOpen();
            if (continueToOpen)
            {
                //view.enableSaveButton(true);
                //view.enableSaveAsButton(true);
                view.getCanvas().repaint();
            }
        }
    }
    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    public boolean promptToOpen(String name)
    {
        // ASK THE USER FOR THE LEVEL TO OPEN
        //JFileChooser levelFileChooser = new JFileChooser(LEVELS_PATH);
        //levelFileChooser.setFileFilter(new XMLFilter());
        //int buttonPressed = levelFileChooser.showOpenDialog(view);

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
            String test=LEVELS_PATH+name;
            System.out.println(test);
            // GET THE FILE THE USER ENTERED
            File testFile = new File(test);
            if (testFile == null)
            {
                // TELL THE USER ABOUT THE ERROR
            System.out.println("TESTFILE IS NULL");
                return false;
            }

            // AND LOAD THE LEVEL (XML FORMAT) FILE
            boolean loadedSuccessfully = load(testFile);

            if (loadedSuccessfully)
            {
                currentFile = testFile;
                currentFileName = currentFile.getName();
                saved = true;

                // AND PUT THE FILE NAME IN THE TITLE BAR
                //view.setTitle(APP_NAME + APP_NAME_FILE_NAME_SEPARATOR + currentFileName);
                
                // MAKE SURE THE SPINNERS HAVE THE CORRRECT INFO
                //view.refreshSpinners(model.getLevel());
                
                //view.enableEditButtons(true);
                //model.setLevelBeingEdited(true);
                
                view.getCanvas().repaint();

                // TELL THE USER ABOUT OUR SUCCESS
               

                return true;
            } 
            else
            {
                // TELL THE USER ABOUT THE ERROR
              System.out.println("ERROR");
                return false;
            }
    }
   
    private boolean load(File testFile)
    {
        return levelIO.loadLevel(testFile, model, view);
    }
}
