/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import mini_game.MiniGameDataModel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import pathx.PathX.PathXPropertyType;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.SpriteType;
//import mini_game.Viewport1;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.ui.PathXCarState;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXPanel;
import pathx.ui.Player;

/**
 *
 * @author zeb
 */
public class PathXDataModel extends MiniGameDataModel {
    private MiniGame miniGame;
    private String currentLevel;
    private int levelNum;
    PathXGameGraph graph;
    // THIS IS THE LEVEL CURRENTLY BEING EDITING
    PathXLevel level;
    //This is for the player
    Player player;
    // DATA FOR RENDERING
    //Viewport viewport;

    // WE ONLY NEED TO TURN THIS ON ONCE
    boolean levelBeingEdited;
    Image backgroundImage;
    Image startingLocationImage;
    Image destinationImage;

    // THE SELECTED INTERSECTION OR ROAD MIGHT BE EDITED OR DELETED
    // AND IS RENDERED DIFFERENTLY
    Intersection selectedIntersection;
    Road selectedRoad;
    
    // WE'LL USE THIS WHEN WE'RE ADDING A NEW ROAD
    Intersection startRoadIntersection;

    // IN CASE WE WANT TO TRACK MOVEMENTS
    int lastMouseX;
    int lastMouseY;    
    
    // THESE BOOLEANS HELP US KEEP TRACK OF
    // @todo DO WE NEED THESE?
    boolean isMousePressed;
    boolean isDragging;
    boolean dataUpdatedSinceLastSave;

    // THIS IS THE UI, WE'LL NOTIFY IT WHENEVER THE DATA CHANGES SO
    // THAT THE UI RENDERING CAN BE UPDATED AT THAT TIME
  //  PathXMiniGame view;
    public PathXDataModel(MiniGame initMiniGame){
        miniGame= initMiniGame;
        level = new PathXLevel();
         //levelBeingEdited = false;
        startRoadIntersection = null;
        player=new Player();
        //graph=new PathXGameGraph(initMiniGame);
      //  view = miniGame
    }

       // ACCESSOR METHODS
    public Player  getPlayer()                          {return player;}
    public PathXLevel       getLevel()                  {   return level;                   }
    public PathXGameGraph getGraph()                    {return graph;}
    public void setGraph (PathXGameGraph gr){
        graph=gr;
    }
    //POSSIBLE SOURCE OF ERROR!!!
   // public PathXMiniGame    getView()                   {   return view;                    }

    //public Viewport1    getViewport1()             {   return viewport;                }
    public boolean          isLevelBeingEdited()        {   return levelBeingEdited;        }
    public Image            getBackgroundImage()        {   return backgroundImage;         }
    public Image            getStartingLocationImage()  {   return startingLocationImage;   }
    public Image            getDesinationImage()        {   return destinationImage;        }
    public Intersection     getSelectedIntersection()   {   return selectedIntersection;    }
    public Road             getSelectedRoad()           {   return selectedRoad;            }
    public Intersection     getStartRoadIntersection()  {   return startRoadIntersection;   }
    //public int              getLastMouseX()             {   return lastMouseX;              }
    //public int              getLastMouseY()             {   return lastMouseY;              }
    public Intersection     getStartingLocation()       {   return level.startingLocation;  }
    public Intersection     getDestination()            {   return level.destination;       }
    public boolean          isDataUpdatedSinceLastSave(){   return dataUpdatedSinceLastSave;}    
    public boolean          isStartingLocation(Intersection testInt)  
    {   return testInt == level.startingLocation;           }
    public boolean isDestination(Intersection testInt)
    {   return testInt == level.destination;                }
    public boolean isSelectedIntersection(Intersection testIntersection)
    {   return testIntersection == selectedIntersection;    }
    public boolean isSelectedRoad(Road testRoad)
    {   return testRoad == selectedRoad;                    }

    // ITERATOR METHODS FOR GOING THROUGH THE GRAPH

    public Iterator intersectionsIterator()
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        return intersections.iterator();
    }
    public Iterator roadsIterator()
    {
        ArrayList<Road> roads = level.roads;
        return roads.iterator();
    }
    
   //  public void setView(PathXMiniGame initView)
   /// {   view = initView;    }  
     
   //      public void setLevelBeingEdited(boolean initLevelBeingEdited)
   // {   levelBeingEdited = initLevelBeingEdited;    }
    public void setLastMousePosition(int initX, int initY)
    {
        lastMouseX = initX;
        lastMouseY = initY;
        miniGame.getCanvas().repaint();
    }    
    public void setSelectedIntersection(Intersection i)
    {
        selectedIntersection = i;
        selectedRoad = null;
        miniGame.getCanvas().repaint();
    }    
    public void setSelectedRoad(Road r)
    {
        selectedRoad = r;
        selectedIntersection = null;
        miniGame.getCanvas().repaint();
    }
    
     public void selectStartRoadIntersection(int canvasX, int canvasY)
    {
        startRoadIntersection = findIntersectionAtCanvasLocation(canvasX, canvasY);
        if (startRoadIntersection != null)
        {
            // NOW WE NEED THE SECOND INTERSECTION
            //switchEditMode(PXLE_EditMode.ADDING_ROAD_END);
        }
    }
    
    /**
     * For selecting the second intersection when making a road. It will
     * find the road at the (canvasX, canvasY) location.
     */
    public void selectEndRoadIntersection(int canvasX, int canvasY)
    {
        Intersection endRoadIntersection = findIntersectionAtCanvasLocation(canvasX, canvasY);
        if (endRoadIntersection != null)
        {
            // MAKE AND ADD A NEW ROAD
            Road newRoad = new Road();
            newRoad.node1 = startRoadIntersection;
            newRoad.node2 = endRoadIntersection;
            newRoad.oneWay = false;
            newRoad.speedLimit = DEFAULT_SPEED_LIMIT;
            level.roads.add(newRoad);
            
            // AND LET'S GO BACK TO ADDING ANOTHER ROAD
            //switchEditMode(PXLE_EditMode.ADDING_ROAD_START);
            startRoadIntersection = null;

            // RENDER
            miniGame.getCanvas().repaint();
        }
    }
    
     public void startNewLevel(String levelName)
    {
        // CLEAR OUT THE OLD GRAPH
        level.reset();
        
        // FIRST INITIALIZE THE LEVEL
        // WE ALWAYS START WITH A DEFAULT BACKGROUND,
        // AND START AND END LOCATIONS
        level.init( levelName,
                    DEFAULT_BG_IMG,
                    DEFAULT_START_IMG,
                    DEFAULT_START_X,
                    DEFAULT_START_Y,
                    DEFAULT_DEST_IMG,
                    DEFAULT_DEST_X, 
                    DEFAULT_DEST_Y);
        
        // NOW MAKE THE LEVEL IMAGES
        //backgroundImage = view.loadImage(LEVELS_PATH + DEFAULT_BG_IMG);
        //startingLocationImage = view.loadImage(LEVELS_PATH + DEFAULT_START_IMG);
        //destinationImage = view.loadImage(LEVELS_PATH + DEFAULT_DEST_IMG);

        // NOW RESET THE VIEWPORT
        //viewport.reset();
        //viewport.setLevelDimensions(backgroundImage.getWidth(null), backgroundImage.getHeight(null)); 

        // INTERACTIVE SETTINGS
        isMousePressed = false;
        isDragging = false;
        selectedIntersection = null;
        selectedRoad = null;
        dataUpdatedSinceLastSave = false;

        // THIS LETS THE LEVEL BE RENDERED
        levelBeingEdited = true;     
        
        // AND NOW MAKE SURE IT GETS RENDERED FOR THE FIRST TIME
        miniGame.getCanvas().repaint();
    }
     
      /**
     * Updates the background image.
     */
    public void updateBackgroundImage(String newBgImage)
    {
        // UPDATE THE LEVEL TO FIT THE BACKGROUDN IMAGE SIZE
        level.backgroundImageFileName = newBgImage;
        backgroundImage = miniGame.loadImage(PATH_DATA + level.backgroundImageFileName);
        int levelWidth = backgroundImage.getWidth(null);
        int levelHeight = backgroundImage.getHeight(null);
        //viewport.setLevelDimensions(levelWidth, levelHeight);
        miniGame.getCanvas().repaint();
    }

    /**
     * Updates the image used for the starting location and forces rendering.
     */
    public void updateStartingLocationImage(String newStartImage)
    {
        level.startingLocationImageFileName = newStartImage;
        startingLocationImage = miniGame.loadImage(PATH_DATA + level.startingLocationImageFileName);
        miniGame.getCanvas().repaint();
    }

    /**
     * Updates the image used for the destination and forces rendering.
     */
    public void updateDestinationImage(String newDestImage)
    {
        level.destinationImageFileName = newDestImage;
        destinationImage = miniGame.loadImage(PATH_DATA + level.destinationImageFileName);
        miniGame.getCanvas().repaint();
    }

     public void addIntersection(Intersection intToAdd)
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        intersections.add(intToAdd);
        miniGame.getCanvas().repaint();
    }

    /**
     * Calculates and returns the distance between two points.
     */
    public double calculateDistanceBetweenPoints(int x1, int y1, int x2, int y2)
    {
        double diffXSquared = Math.pow(x1 - x2, 2);
        double diffYSquared = Math.pow(y1 - y2, 2);
        return Math.sqrt(diffXSquared + diffYSquared);
    }

    /**
     * Searches the level graph and finds and returns the intersection
     * that overlaps (canvasX, canvasY).
     */
    public Intersection findIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
        for (Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x, i.y, canvasX , canvasY );
            if (distance < INTERSECTION_RADIUS)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                return i;
            }
        }
        return null;
    }
    
    public void unselectEverything()
    {
        selectedIntersection = null;
        selectedRoad = null;
        startRoadIntersection = null;
        miniGame.getCanvas().repaint();
    }

    /**
     * Searches to see if there is a road at (canvasX, canvasY), and if
     * there is, it selects and returns it.
     */
    public Road selectRoadAtCanvasLocation(int canvasX, int canvasY)
    {
        Iterator<Road> it = level.roads.iterator();
        Line2D.Double tempLine = new Line2D.Double();
        while (it.hasNext())
        {
            Road r = it.next();
            tempLine.x1 = r.node1.x;
            tempLine.y1 = r.node1.y;
            tempLine.x2 = r.node2.x;
            tempLine.y2 = r.node2.y;
            double distance = tempLine.ptSegDist(canvasX, canvasY);
            
            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE)
            {
                // SELECT IT
                this.selectedRoad = r;
                //this.switchEditMode(PXLE_EditMode.ROAD_SELECTED);
                return selectedRoad;
            }
        }
        return null;
    }
    
     /**
     * Checks to see if (canvasX, canvasY) is free (i.e. there isn't
     * already an intersection there, and if not, adds one.
     */
    public void addIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // FIRST MAKE SURE THE ENTIRE INTERSECTION IS INSIDE THE LEVEL
        if ((canvasX - INTERSECTION_RADIUS) < 0) return;
        if ((canvasY - INTERSECTION_RADIUS) < 0) return;
        //if ((canvasX + INTERSECTION_RADIUS) > viewport.levelWidth) return;
        //if ((canvasY + INTERSECTION_RADIUS) > viewport.levelHeight) return;
        
        // AND ONLY ADD THE INTERSECTION IF IT DOESN'T OVERLAP WITH
        // AN EXISTING INTERSECTION
        for(Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x, i.y, canvasX, canvasY);
            if (distance < INTERSECTION_RADIUS)
                return;
        }          
        
        // LET'S ADD A NEW INTERSECTION
        int intX = canvasX ;
        int intY = canvasY ;
        Intersection newInt = new Intersection(intX, intY);
        level.intersections.add(newInt);
        miniGame.getCanvas().repaint();
    }
    
    /**
     * Increases the speed limit on the selected road.
     */
    public void increaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit < MAX_SPEED_LIMIT)
            {
                speedLimit += SPEED_LIMIT_STEP;
                selectedRoad.setSpeedLimit(speedLimit);
                miniGame.getCanvas().repaint();
            }
        }
    }

    /**
     * Decreases the speed limit on the selected road.
     */
    public void decreaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit > MIN_SPEED_LIMIT)
            {
                speedLimit -= SPEED_LIMIT_STEP;
                selectedRoad.setSpeedLimit(speedLimit);
                miniGame.getCanvas().repaint();
            }
        }
    }    
    // GAME GRID AND TILE DATA
    private int balance=0;
    private int goal=0;

    public String getCurrentLevel()
    {
        return currentLevel;
    }

    
  
    // MUTATOR METHODS
    
   
    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
    }

    
    // INIT METHODS - AFTER CONSTRUCTION, THESE METHODS SETUP A GAME FOR USE
    // - initLevel
    // - initTiles
    // - initTile
    /**
     * Called after a level has been selected, it initializes the grid so that
     * it is the proper dimensions.
     */
    public void initLevel(String levelName)
    {
        // KEEP THE TILE ORDER AND SORTING ALGORITHM FOR LATER
       

        // UPDATE THE VIEWPORT IF WE ARE SCROLLING (WHICH WE'RE NOT)
        viewport.updateViewportBoundaries();

        // INITIALIZE THE PLAYER RECORD IF NECESSARY
        /**
        PathXRecord playerRecord = ((PathXMiniGame) miniGame).getPlayerRecord();
        if (!playerRecord.hasLevel(levelName))
        {
            playerRecord.addLevel(levelName, initSortingAlgorithm.name);
        }
        */ 
    }



    // OVERRIDDEN METHODS
    // - checkMousePressOnSprites
    // - endGameAsWin
    // - reset
    // - updateAll
    // - updateDebugText

    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on grid cells.
     *
     * @param game The Sorting Hat game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */

    
    
    /**
     * Updates the player record, adding a game without a win.
  

    /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
  
    @Override
    public void updateAll(MiniGame game)
    {
        try
        {
            // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
            game.beginUsingData();

            

            // IF THE GAME IS STILL ON, THE TIMER SHOULD CONTINUE
           
        } finally
        {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
    }

    
    public void setBalance (int x){
        balance=x;
    }
    
    public int getBalance (){
        return balance;
    }
    
    public void setGoal (int x){
        goal=x;
    }
    
    public int getGoal(){
        return goal;
    }
    


    /**
     * This method is for updating any debug text to present to the screen. In a
     * graphical application like this it's sometimes useful to display data in
     * the GUI.
     *
     * @param game The Sorting Hat game about which to display info.
     */
    @Override
    public void updateDebugText(MiniGame game)
    {
    }

    @Override
    /**
     * this doesnt work!!!
     */
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        
        
       
        ArrayList<Intersection> intersections = level.getIntersections();
        Sprite banana= miniGame.getGUIDecor().get(PLAYER_TYPE);
        Sprite police1= miniGame.getGUIDecor().get(POLICE_TYPE);
        Sprite police2= miniGame.getGUIDecor().get(POLICE_TYPE_2);
        Sprite police3= miniGame.getGUIDecor().get(POLICE_TYPE_3);
        Sprite police4= miniGame.getGUIDecor().get(POLICE_TYPE_4);
        Sprite police5= miniGame.getGUIDecor().get(POLICE_TYPE_5);
        Sprite police6= miniGame.getGUIDecor().get(POLICE_TYPE_6);
         
                    
       if (((PathXMiniGame)miniGame).isCurrentScreenState(GAME_SCREEN_STATE)){         
       
            //&& y==intersections.get(i).getY()
            if ((Math.abs(x-banana.getX())< INTERSECTION_RADIUS || (Math.abs(x+banana.getX())> INTERSECTION_RADIUS)) && (Math.abs(y-banana.getY())< INTERSECTION_RADIUS || (Math.abs(y+banana.getY())> INTERSECTION_RADIUS))){
         
                     banana.setX(x);
                    banana.setY(y);
            }
            
              for (int i=0; i<intersections.size(); i++){
            if ((intersections.get(i).isOpen()==false) && (intersections.get(i).getX()<=x+40)){
                banana.setX(x);
                 banana.setY(y);
            }
        }
        if (((x+120)> level.getDestination().getX())){
            ((PathXMiniGame)miniGame).winLevel();
        }
           System.out.println("x: "+ x);
           System.out.println("police x: "+ police1.getX() );
        if ((x+30)>=police1.getX() && !(x>police1.getX()) || x==police1.getX()){
            ((PathXMiniGame)miniGame).loseLevel();
        }
        if (((x+30)>=police2.getX() && !(x>police2.getX()) || x==police2.getX()) && ( police2.getState().equals(VISIBLE_STATE.toString())) ){
            ((PathXMiniGame)miniGame).loseLevel();
        }
        if (((x+30)>=police3.getX() && !(x>police3.getX()) || x==police3.getX() ) && ( police3.getState().equals(VISIBLE_STATE.toString()))){
            ((PathXMiniGame)miniGame).loseLevel();
        }
        if (((x+30)>=police4.getX() && !(x>police4.getX()) || x==police4.getX()) && ( police4.getState().equals(VISIBLE_STATE.toString()))){
            ((PathXMiniGame)miniGame).loseLevel();
        }
         if (((x+30)>=police5.getX() && !(x>police5.getX()) || x==police5.getX()) &&( police5.getState().equals(VISIBLE_STATE.toString()))){
            ((PathXMiniGame)miniGame).loseLevel();
        }
          if (((x+30)>=police6.getX() && !(x>police6.getX()) || x==police6.getX())  && ( police6.getState().equals(VISIBLE_STATE.toString()))){
            ((PathXMiniGame)miniGame).loseLevel();
        }
        //if (police2.getState().equals(VISIBLE_STATE.toString()) && ((x+20)> police2.getX())){
           // ((PathXMiniGame)miniGame).loseLevel();
        //}
          }
        miniGame.getCanvas().repaint();
    }

    @Override
    public void reset(MiniGame game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
