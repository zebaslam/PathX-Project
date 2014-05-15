/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.file;
import java.awt.HeadlessException;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mini_game.Viewport;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import pathx.PathX.PathXPropertyType;
import pathx.data.PathXDataModel;
import pathx.data.PathXRecord;
import pathx.ui.PathXMiniGame;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.data.Intersection;
import pathx.data.PathXLevel;
import pathx.data.Road;
import xml_utilities.XMLUtilities;

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
    PathXMiniGame minigame;

    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    private File currentFile;
    private String currentFileName;
    private File levelSchema;
     private XMLUtilities xmlUtil;
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    
    //potential source of ERROR
    //bacon
    public PathXFileManager(PathXMiniGame initMiniGame, PathXDataModel initmodel)
    {
        // KEEP IT FOR LATER
        minigame=initMiniGame;
        //minigame=view;
        //model= minigame.getDataModel();
        levelIO = new PathX_XMLLevelIO(new File(LEVELS_PATH + LEVEL_SCHEMA));
        
        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
        
        // THIS KNOWS HOW TO READ AND ACCESS XML FILES
        xmlUtil = new XMLUtilities();
        
        // WE'LL USE THE SCHEMA FILE TO VALIDATE THE XML FILES
       
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
            //System.out.println(test);
            // GET THE FILE THE USER ENTERED
            File testFile = new File(test);
            if (testFile == null)
            {
                // TELL THE USER ABOUT THE ERROR
            System.out.println("TESTFILE IS NULL");
                return false;
            }

            // AND LOAD THE LEVEL (XML FORMAT) FILE
            boolean loadedSuccessfully = load(name);

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
                //System.out.println(model.getCurrentLevel());
                //System.out.println("Graph:"+model.getGraph());
                //System.out.println(model.getStartingLocation().toString());
                /**
                Intersection print=model.getGraph().findCheapestNeighbor(model.findIntersectionAtCanvasLocation(511, 226));
                if (print == null) {
                    System.out.println("It's null");
                } else {
                    System.out.println("CHEAPEST NEIGHBOR:"+print.toString());
                }
                */
                minigame.getCanvas().repaint();

                // TELL THE USER ABOUT OUR SUCCESS
               

                return true;
            } 
            else
            {
                // The file did not load properly
              System.out.println("ERROR");
                return false;
            }
    }
   
    
    private boolean load(String testFile)
    {
        return loadLevel(testFile);
    }
    
    
    public boolean loadLevel(String levelFile)
    {
        
        
        try
        {
            
            // WE'LL FILL IN SOME OF THE LEVEL OURSELVES
           //PathXDataModel theData = (PathXDataModel)minigame.getDataModel();
            File fileToOpen = new File(levelFile);
            model = (PathXDataModel)minigame.getDataModel();
            PathXLevel levelToLoad = model.getLevel();
            levelSchema= new File(PATH_DATA+"PathXLevelSchema.xsd");
            levelToLoad.reset();
            
            // FIRST LOAD ALL THE XML INTO A TREE
            Document doc = xmlUtil.loadXMLDocument( PATH_DATA+fileToOpen, 
                                                    levelSchema.getAbsolutePath());
            
            // FIRST LOAD THE LEVEL INFO
            Node levelNode = doc.getElementsByTagName(LEVEL_NODE).item(0);
            NamedNodeMap attributes = levelNode.getAttributes();
            String levelName = attributes.getNamedItem(NAME_ATT).getNodeValue();
            levelToLoad.setLevelName(levelName);
            String bgImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            model.updateBackgroundImage(bgImageName);

            // THEN LET'S LOAD THE LIST OF ALL THE REGIONS
            loadIntersectionsList(doc, levelToLoad);
            ArrayList<Intersection> intersections = levelToLoad.getIntersections();
            
            // AND NOW CONNECT ALL THE REGIONS TO EACH OTHER
            loadRoadsList(doc, levelToLoad);
            
            // LOAD THE START INTERSECTION
            Node startIntNode = doc.getElementsByTagName(START_INTERSECTION_NODE).item(0);
            attributes = startIntNode.getAttributes();
            String startIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int startId = Integer.parseInt(startIdText);
            String startImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            Intersection startingIntersection = intersections.get(startId);
            levelToLoad.setStartingLocation(startingIntersection);
            model.updateStartingLocationImage(startImageName);
            
            // LOAD THE DESTINATION
            Node destIntNode = doc.getElementsByTagName(DESTINATION_INTERSECTION_NODE).item(0);
            attributes = destIntNode.getAttributes();
            String destIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int destId = Integer.parseInt(destIdText);
            String destImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            levelToLoad.setDestination(intersections.get(destId));
            model.updateDestinationImage(destImageName);
            
            // LOAD THE MONEY
            Node moneyNode = doc.getElementsByTagName(MONEY_NODE).item(0);
            attributes = moneyNode.getAttributes();
            String moneyText = attributes.getNamedItem(AMOUNT_ATT).getNodeValue();
            int money = Integer.parseInt(moneyText);
            levelToLoad.setMoney(money);
            
            // LOAD THE NUMBER OF POLICE
            Node policeNode = doc.getElementsByTagName(POLICE_NODE).item(0);
            attributes = policeNode.getAttributes();
            String policeText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numPolice = Integer.parseInt(policeText);
            //System.out.println("Police:" +numPolice);
            levelToLoad.setNumPolice(numPolice);
            
            // LOAD THE NUMBER OF BANDITS
            Node banditsNode = doc.getElementsByTagName(BANDITS_NODE).item(0);
            attributes = banditsNode.getAttributes();
            String banditsText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numBandits = Integer.parseInt(banditsText);
            levelToLoad.setNumBandits(numBandits);
            System.out.println("Bandits:" +numBandits);
            
            // LOAD THE NUMBER OF ZOMBIES
            Node zombiesNode = doc.getElementsByTagName(ZOMBIES_NODE).item(0);
            attributes = zombiesNode.getAttributes();
            String zombiesText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numZombies = Integer.parseInt(zombiesText);
            levelToLoad.setNumZombies(numZombies);    
            //System.out.println("Zombies:" +numZombies);
            
           
        }
        catch(Exception e)
        {
            // LEVEL DIDN'T LOAD PROPERLY
            return false;
        }
        // LEVEL LOADED PROPERLY
        return true;
    }
    
    // PRIVATE HELPER METHOD FOR LOADING INTERSECTIONS INTO OUR LEVEL
    private void loadIntersectionsList( Document doc, PathXLevel levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node intersectionsListNode = doc.getElementsByTagName(INTERSECTIONS_NODE).item(0);
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> intersectionsList = xmlUtil.getChildNodesWithName(intersectionsListNode, INTERSECTION_NODE);
        for (int i = 0; i < intersectionsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node intersectionNode = intersectionsList.get(i);
            NamedNodeMap intersectionAttributes = intersectionNode.getAttributes();
            String idText = intersectionAttributes.getNamedItem(ID_ATT).getNodeValue();
            String openText = intersectionAttributes.getNamedItem(OPEN_ATT).getNodeValue();
            String xText = intersectionAttributes.getNamedItem(X_ATT).getNodeValue();
            int x = Integer.parseInt(xText);
            String yText = intersectionAttributes.getNamedItem(Y_ATT).getNodeValue();
            int y = Integer.parseInt(yText);
            
            // NOW MAKE AND ADD THE INTERSECTION
            Intersection newIntersection = new Intersection(x, y);
            newIntersection.open = Boolean.parseBoolean(openText);
            intersections.add(newIntersection);
        }
    }

    // PRIVATE HELPER METHOD FOR LOADING ROADS INTO OUR LEVEL
    private void loadRoadsList( Document doc, PathXLevel levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node roadsListNode = doc.getElementsByTagName(ROADS_NODE).item(0);
        ArrayList<Road> roads = levelToLoad.getRoads();
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> roadsList = xmlUtil.getChildNodesWithName(roadsListNode, ROAD_NODE);
        for (int i = 0; i < roadsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node roadNode = roadsList.get(i);
            NamedNodeMap roadAttributes = roadNode.getAttributes();
            String id1Text = roadAttributes.getNamedItem(INT_ID1_ATT).getNodeValue();
            int int_id1 = Integer.parseInt(id1Text);
            String id2Text = roadAttributes.getNamedItem(INT_ID2_ATT).getNodeValue();
            int int_id2 = Integer.parseInt(id2Text);
            String oneWayText = roadAttributes.getNamedItem(ONE_WAY_ATT).getNodeValue();
            boolean oneWay = Boolean.parseBoolean(oneWayText);
            String speedLimitText = roadAttributes.getNamedItem(SPEED_LIMIT_ATT).getNodeValue();
            int speedLimit = Integer.parseInt(speedLimitText);
            
            // NOW MAKE AND ADD THE ROAD
            Road newRoad = new Road();
            newRoad.setNode1(intersections.get(int_id1));
            newRoad.setNode2(intersections.get(int_id2));
            newRoad.setOneWay(oneWay);
            newRoad.setSpeedLimit(speedLimit);
            roads.add(newRoad);
        }
    }
    
    /**
     * This method saves the level currently being edited to the levelFile. Note
     * that it will be saved as an .xml file, which is an XML-format that will
     * conform to the schema.
     */
    public boolean saveLevel(File levelFile, PathXLevel levelToSave)
    {
        try 
        {
            // THESE WILL US BUILD A DOC
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // FIRST MAKE THE DOCUMENT
            Document doc = docBuilder.newDocument();
            
            // THEN THE LEVEL (i.e. THE ROOT) ELEMENT
            Element levelElement = doc.createElement(LEVEL_NODE);
            doc.createAttribute(NAME_ATT);
            levelElement.setAttribute(NAME_ATT, levelToSave.getLevelName());
            doc.appendChild(levelElement);
            doc.createAttribute(IMAGE_ATT);
            levelElement.setAttribute(IMAGE_ATT, levelToSave.getBackgroundImageFileName());
 
            // THEN THE INTERSECTIONS
            Element intersectionsElement = makeElement(doc, levelElement, INTERSECTIONS_NODE, "");
            
            // AND LET'S ADD EACH INTERSECTION
            int id = 0;
            doc.createAttribute(ID_ATT); 
            doc.createAttribute(X_ATT);
            doc.createAttribute(Y_ATT);
            doc.createAttribute(OPEN_ATT);
            for (Intersection i : levelToSave.getIntersections())
            {
                // MAKE AN INTERSECTION NODE AND ADD IT
                Element intersectionNodeElement = makeElement(doc, intersectionsElement,
                        INTERSECTION_NODE, "");
                
                // NOW LET'S FILL IN THE INTERSECTION'S DATA. FIRST MAKE THE ATTRIBUTES
                intersectionNodeElement.setAttribute(ID_ATT,    "" + id);
                intersectionNodeElement.setAttribute(X_ATT,     "" + i.x);
                intersectionNodeElement.setAttribute(Y_ATT,     "" + i.y);
                intersectionNodeElement.setAttribute(OPEN_ATT,  "" + i.open);
             }

            // AND NOW ADD ALL THE ROADS
            Element roadsElement = makeElement(doc, levelElement, ROADS_NODE, "");
            doc.createAttribute(INT_ID1_ATT);
            doc.createAttribute(INT_ID2_ATT);
            doc.createAttribute(SPEED_LIMIT_ATT);
            doc.createAttribute(ONE_WAY_ATT);
            ArrayList<Intersection> intersections = levelToSave.getIntersections();
            for (Road r : levelToSave.getRoads())
            {
                // MAKE A ROAD NODE AND ADD IT TO THE LIST
                Element roadNodeElement = makeElement(doc, roadsElement, ROAD_NODE, "");
                int intId1 = intersections.indexOf(r.getNode1());
                roadNodeElement.setAttribute(INT_ID1_ATT, "" + intId1);
                int intId2 = intersections.indexOf(r.getNode2());
                roadNodeElement.setAttribute(INT_ID2_ATT, "" + intId2);
                roadNodeElement.setAttribute(SPEED_LIMIT_ATT, "" + r.getSpeedLimit());
                roadNodeElement.setAttribute(ONE_WAY_ATT, "" + r.isOneWay());
            }
            
            // NOW THE START INTERSECTION
            Element startElement = makeElement(doc, levelElement, START_INTERSECTION_NODE, "");
            int startId = intersections.indexOf(levelToSave.getStartingLocation());
            startElement.setAttribute(ID_ATT, "" + startId);
            startElement.setAttribute(IMAGE_ATT, levelToSave.getStartingLocationImageFileName());
            
            // AND THE DESTINATION
            Element destElement = makeElement(doc, levelElement, DESTINATION_INTERSECTION_NODE, "");
            int destId = intersections.indexOf(levelToSave.getDestination());
            destElement.setAttribute(ID_ATT, "" + destId);
            destElement.setAttribute(IMAGE_ATT, levelToSave.getDestinationImageFileName());
            
            // NOW THE MONEY
            Element moneyElement = makeElement(doc, levelElement, MONEY_NODE, "");
            doc.createAttribute(AMOUNT_ATT);
            moneyElement.setAttribute(AMOUNT_ATT, "" + levelToSave.getMoney());
            
            // AND THE POLICE COUNT
            Element policeElement = makeElement(doc, levelElement, POLICE_NODE, "");
            doc.createAttribute(NUM_ATT);
            policeElement.setAttribute(NUM_ATT, "" + levelToSave.getNumPolice());
            
            // AND THE BANDIT COUNT
            Element banditElement = makeElement(doc, levelElement, BANDITS_NODE, "");
            banditElement.setAttribute(NUM_ATT, "" + levelToSave.getNumBandits());
            
            // AND FINALLY THE ZOMBIES COUNT
            Element zombiesElement = makeElement(doc, levelElement, ZOMBIES_NODE, "");
            zombiesElement.setAttribute(NUM_ATT, "" + levelToSave.getNumZombies());

            // THE TRANSFORMER KNOWS HOW TO WRITE A DOC TO
            // An XML FORMATTED FILE, SO LET'S MAKE ONE
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(levelFile);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);    

            // SUCCESS
            return true;
        }
        catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG
            return false;
        }    
    }   
    
    // THIS HELPER METHOD BUILDS ELEMENTS (NODES) FOR US TO HELP WITH
    // BUILDING A Doc WHICH WE WOULD THEN SAVE TO A FILE.
    private Element makeElement(Document doc, Element parent, String elementName, String textContent)
    {
        Element element = doc.createElement(elementName);
        element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }
}
