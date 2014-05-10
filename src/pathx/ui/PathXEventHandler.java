/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.ui;

import java.awt.event.KeyEvent;
import mini_game.MiniGameDataModel;
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
private MiniGameDataModel data;
    public PathXEventHandler(PathXMiniGame initGame) {
        game = initGame;
      filemanager = game.getFileManager();
      data= game.getDataModel();
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
    public void respondToMusicSelected(){
        game.setEnableMusic(false);
         game.update();
    }
     public void respondToMusicUnSelected(){
        game.setEnableMusic(true);
        game.update();
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
   
    //all the special event handlers
    public void respondToGreenLight(){
        int x=5;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
     public void respondToFreeze(){
        int x=10;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
      public void respondToSpeedLimit(){
        int x=15;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToPlayerSpeed(){
        int x=20;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
        public void respondToFlatTire(){
        int x=20;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToEmptyGas(){
        int x=20;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToCloseRoad(){
        int x=25;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToCloseIntersection(){
        int x=25;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToOpenIntersection(){
        int x=25;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
            public void respondToSteal(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
          public void respondToMindControl(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToIntangibility(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToMindlessTerror(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToFlying(){
        int x=40;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
    }
         public void respondToInvincibility(){
        int x=40;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        
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
        if (level==12){
            name="Pierre.xml";
        }
        if (level==13){
            name="Lincoln.xml";
        }
        if (level==14){
            name="Topeka.xml";
        }
        if(level==15){
            name="oaklahoma.xml";
        }
        if (level==16){
            name="Dallas.xml";
        }
        if(level==17){
            name="San antonio.xml";
        }
        if(level==18){
            name="Minnesota.xml";
        }
        if(level==19){
            name="iowa.xml";
        }
        if (level==20){
            name="Missouri.xml";
        }
        ((PathXDataModel)game.getDataModel()).setCurrentLevel(name);
        filemanager.promptToOpen(name);
        game.switchToGameScreen();
        
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
        else if(keyCode==KeyEvent.VK_1){
            game.increasePlayerMoney();
            game.getCanvas().repaint();
            
        }
       
    }
}
