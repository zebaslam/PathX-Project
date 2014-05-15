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
    boolean sound;
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
        data.beginGame();
        
    }
    public void respondToTryAgain(){
        int lvl =game.getLevel();
        game.setWin(false);
        game.MakeWinDialogInvisible();
        respondToGameScreenSelect(lvl);
    }
    public void respondToQuitLevel(){
        game.setWin(false);
        game.MakeWinDialogInvisible();
        game.switchToLevelSelectScreen();
    }
    public void respondToMusicSelected(){
        game.setEnableMusic(false);
         game.update();
    }
     public void respondToMusicUnSelected(){
        game.setEnableMusic(true);
        game.update();
    }
      public void respondToSoundSelected(){
        game.setEnableSound(false);
         game.update();
    }
     public void respondToSoundUnSelected(){
        game.setEnableSound(true);
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
         sound= game.getEnableSound();
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_GREEN_LIGHT.toString(), false);
        
        
    }
      public void respondToRedLight(){
        int x=5;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_RED_LIGHT.toString(), false);
    }
     public void respondToFreeze(){
        int x=10;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
         game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_TIME_FREEZE.toString(), false);
    }
      public void respondToSpeedLimit(){
        int x=15;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_DECREASE_SPEED_LIMIT.toString(), false);
    }
       public void respondToIncreaseSpeedLimit(){
        int x=15;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_INCREASE_SPEED_LIMIT.toString(), false);
    }
         public void respondToPlayerSpeed(){
        int x=20;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_PLAYER_SPEED.toString(), false);
    }
        public void respondToFlatTire(){
        int x=20;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
         game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_FLAT_TIRE.toString(), false);
    }
         public void respondToEmptyGas(){
        int x=20;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_GAS.toString(), false);
    }
         public void respondToCloseRoad(){
        int x=25;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        sound= game.getEnableSound();
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_CLOSE_ROAD.toString(), false);
    }
         public void respondToCloseIntersection(){
        int x=25;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
         if(sound==true)
         game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_CLOSE_INTERSECTION.toString(), false);
    }
         public void respondToOpenIntersection(){
        int x=25;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
         if(sound==true)
         game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_OPEN_INTERSECTION.toString(), false);
    }
            public void respondToSteal(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
          if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_STEAL.toString(), false);
    }
          public void respondToMindControl(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
          if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_MIND_CONTROL.toString(), false);
    }
         public void respondToIntangibility(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_INTANGIBILITY.toString(), false);
    }
         public void respondToMindlessTerror(){
        int x=30;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
         if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_MIND_TERROR.toString(), false);
    }
         public void respondToFlying(){
        int x=40;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
     if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_FLYING.toString(), false);
    }
         public void respondToInvincibility(){
        int x=40;
        ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-x);
        if(sound==true)
        game.getAudio().play(PathX.PathXPropertyType.AUDIO_CUE_INVINCIBILITY.toString(), false);
        
    }
     public void respondToWinSelect(int level){
       
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
        game.switchToWinScreen();
      
    }     
      public void respondToLoseSelect(int level){
       
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
        game.switchToLoseScreen();
      
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
        else if(keyCode==KeyEvent.VK_G){
            respondToGreenLight();
        }
        else if(keyCode==KeyEvent.VK_R){
            respondToRedLight();
        }
        else if(keyCode==KeyEvent.VK_Z){
            respondToSpeedLimit();
        }
        else if(keyCode==KeyEvent.VK_X){
            respondToIncreaseSpeedLimit();
        }
        else if(keyCode==KeyEvent.VK_P){
            respondToPlayerSpeed();
        }
        else if(keyCode==KeyEvent.VK_T){
            respondToFlatTire();
        }
        else if(keyCode==KeyEvent.VK_E){
            respondToEmptyGas();
        }
        else if(keyCode==KeyEvent.VK_H){
            respondToCloseRoad();
        }
        else if(keyCode==KeyEvent.VK_C){
            respondToCloseIntersection();
        }
        else if(keyCode==KeyEvent.VK_O){
            respondToOpenIntersection();
        }
        else if(keyCode==KeyEvent.VK_Q){
            respondToSteal();
        }
        else if(keyCode==KeyEvent.VK_M){
            respondToMindControl();
        }
        else if(keyCode==KeyEvent.VK_B){
            respondToIntangibility();
        }
        else if(keyCode==KeyEvent.VK_L){
            respondToMindlessTerror();
        }
         else if(keyCode==KeyEvent.VK_Y){
            respondToFlying();
        }
          else if(keyCode==KeyEvent.VK_V){
            respondToInvincibility();
        }
        else if(keyCode==KeyEvent.VK_1){
            game.increasePlayerMoney();
            game.getCanvas().repaint();
            
        }
          else if(keyCode==KeyEvent.VK_2){
              if(game.isCurrentScreenState(GAME_SCREEN_STATE)){
            game.winLevel();
            game.getCanvas().repaint();
              }
        }
       
    }
}
