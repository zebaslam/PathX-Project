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

/**
 *
 * @author zeb
 */
public class PathXEventHandler {

    private PathXMiniGame game;

    public PathXEventHandler(PathXMiniGame initGame) {
        game = initGame;
    }

    public void respondToExitRequest() {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);
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
    }

    public void respondToLevelSelect() {
        game.switchToLevelSelectScreen();

    }

    public void respondToKeyPress(int keyCode) {
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        if (keyCode == KeyEvent.VK_W) {
            data.getViewport().scroll(0, -VIEWPORT_INC);
        } else if (keyCode == KeyEvent.VK_A) {
            data.getViewport().scroll(-VIEWPORT_INC, 0);
        } else if (keyCode == KeyEvent.VK_S) {
            data.getViewport().scroll(0, VIEWPORT_INC);
        } else if (keyCode == KeyEvent.VK_D) {
            data.getViewport().scroll(VIEWPORT_INC, 0);
        }
    }
}
