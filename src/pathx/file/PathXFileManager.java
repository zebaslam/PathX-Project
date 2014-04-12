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
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import pathx.data.PathXDataModel;
import pathx.data.PathXRecord;
import pathx.ui.PathXMiniGame;
import properties_manager.PropertiesManager;

/**
 *
 * @author zeb
 */
public class PathXFileManager {
    public static String SPACE = " ";
    public static String NEW_LINE = "\n";
    public static String DELIMITER = "|";
    private PathXMiniGame miniGame;
    
    public PathXFileManager(PathXMiniGame initMiniGame)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }
    
    public static String loadTextFile(String textFile) throws IOException
    {
        // ADD THE PATH TO THE FILE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        textFile = props.getProperty(PathXPropertyType.PATH_DATA) + textFile;

        // WE'LL ADD ALL THE CONTENTS OF THE TEXT FILE TO THIS STRING
        String textToReturn = "";

        // OPEN A STREAM TO READ THE TEXT FILE
        FileReader fr = new FileReader(textFile);
        BufferedReader reader = new BufferedReader(fr);

        // READ THE FILE, ONE LINE OF TEXT AT A TIME
        String inputLine = reader.readLine();
        while (inputLine != null)
        {
            // APPEND EACH LINE TO THE STRING
            textToReturn += inputLine + NEW_LINE;

            // READ THE NEXT LINE
            inputLine = reader.readLine();
        }

        // RETURN THE TEXT
        return textToReturn;
    }
    
      public static void loadHTMLDocument(String htmlFile,
            HTMLDocument doc) throws IOException
    {
        // OPEN THE STREAM
        FileReader fr = new FileReader(htmlFile);
        BufferedReader br = new BufferedReader(fr);

        // WE'LL USE A SWING PARSER FOR PARSING THE
        // HTML DOCUMENT SUCH THAT IT PROPERLY LOADS
        // THE DOCUMENT
        HTMLEditorKit.Parser parser = new ParserDelegator();

        // THE CALLBACK KEEPS TRACK OF WHEN IT COMPLETES LOADING
        HTMLEditorKit.ParserCallback callback = doc.getReader(0);

        // LOAD AND PARSE THE WEB PAGE
        parser.parse(br, callback, true);
    }
}
