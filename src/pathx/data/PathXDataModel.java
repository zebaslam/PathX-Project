/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import mini_game.MiniGameDataModel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import pathx.PathX.PathXPropertyType;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXPanel;
/**
 *
 * @author zeb
 */
public class PathXDataModel extends MiniGameDataModel {
    private MiniGame miniGame;
    private String currentLevel;
    
    public PathXDataModel(MiniGame initMiniGame){
        miniGame= initMiniGame;
        
    }

    
    // GAME GRID AND TILE DATA
    private int gameTileWidth;
    private int gameTileHeight;
    private int numGameGridColumns;
    private int numGameGridRows;

    // THESE ARE THE TILES STACKED AT THE START OF THE GAME
    //private ArrayList<SortingHatTile> stackTiles;
    //private int stackTilesX;
    //private int stackTilesY;

    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    //private ArrayList<SortingHatTile> movingTiles;

    // THIS IS THE TILE THE USER IS DRAGGING
    //private SortingHatTile selectedTile;
   // private int selectedTileIndex;

    // THIS IS THE TEMP TILE
    //private SortingHatTile tempTile;

    // KEEPS TRACK OF HOW MANY BAD SPELLS WERE CAST
    private int badSpellsCounter;

    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;


    // THE SORTING ALGORITHM WHICH GENERATES THE PROPER TRANSACTIONS
    //private SortingHatAlgorithm sortingAlgorithm;

    // THE PROPER TRANSACTIONS TO USE FOR COMPARISION AGAINST PLAYER MOVES
    //private ArrayList<SortTransaction> properTransactionOrder;
    //private int transactionCounter;

  

    

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
        SortingHatRecord playerRecord = ((PathXMiniGame) miniGame).getPlayerRecord();
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
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset(MiniGame game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
