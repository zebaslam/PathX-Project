/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.ui;

import java.awt.event.KeyEvent;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.GAME_SCREEN_STATE;
import static pathx.PathXConstants.MENU_SCREEN_STATE;
import static pathx.PathXConstants.VIEWPORT_INC;
import pathx.PathX;
import pathx.data.PathXDataModel;
import pathx.file.PathXFileManager;

/**
 *
 * @author zeb
 */
public class PathXEventHandler {

    private PathXMiniGame game;
 private     PathXFileManager filemanager;

    public PathXEventHandler(PathXMiniGame initGame) {
        game = initGame;
      filemanager = game.getFileManager();
    }

    public void respondToExitRequest() {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        if (game.isCurrentScreenState(GAME_SCREEN_STATE)){
            game.switchToLevelSelectScreen();
        }
        else{
        System.exit(0);
        }
    }
    
    public void respondToCloseDialog(){
        game.makeDialogInvisible();
    }
    public void respondToHomeRequest() {
        // IF A GAME IS IN PROGRESS, THE PLAYER LOSES
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }

        // SWITCH BACK TO THE MENU SCREEN SO THE 
        // USER MAY CHOOSE ANOTHER LEVEL
        game.switchToSplashScreen();
    }

    public void respondToHelpRequest() {
        // IF A GAME IS IN PROGRESS, THE PLAYER LOSES
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }

        // SWITCH BACK TO THE MENU SCREEN SO THE 
        // USER MAY CHOOSE ANOTHER LEVEL
        game.switchToHelpScreen();
    }

    public void respondToSettingsSelect() {
        game.switchToSettingsScreen();
        game.displaySettings();
    }

    public void respondToLevelSelect() {
        game.switchToLevelSelectScreen();

    }
   
    public void respondToGameScreenSelect(int level){
        String name="";
        if(level==1){
            name="Portland.xml";
        }
        if (level==2){
            name="Sacremento.xml";
        }
        if(level==3){
            name="San Jose.xml";
        }
        if (level==4){
            name="Boise.xml";
        }
        if (level==5){
            name="Las Vegas.xml";
        }
        if (level==6){
            name="Wyoming.xml";
        }
        if (level==7){
            name="Salt Lake City.xml";
        }
        if (level==8){
            name="Phoenix.xml";
        }
        if (level==9){
            name="Denver.xml";
        }
        if (level==10){
            name="Santa Fe.xml";
        }
        if (level==11){
            name="Bismark.xml";
        }
        game.switchToGameScreen();
        filemanager.promptToOpen(name);
    }
    public void respondToKeyPress(int keyCode) {
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        if (keyCode == KeyEvent.VK_UP) {
            game.ScrollUp();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            game.ScrollDown();
        } else if (keyCode == KeyEvent.VK_LEFT) {
            game.ScrollLeft();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            game.ScrollRight();
        }
       
    }
}
