/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

/**
 *
 * @author zeb
 */
public class PathXEventHandler {
        private PathXMiniGame game;
        
        public PathXEventHandler(PathXMiniGame initGame)
    {
        game = initGame;
    }
        
           public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }
           
           public void respondToHomeRequest()
    {
        // IF A GAME IS IN PROGRESS, THE PLAYER LOSES
        if (game.getDataModel().inProgress())
            game.getDataModel().endGameAsLoss();
        
        // SWITCH BACK TO THE MENU SCREEN SO THE 
        // USER MAY CHOOSE ANOTHER LEVEL
        game.switchToSplashScreen();        
    }
}
