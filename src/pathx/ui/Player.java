/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static pathx.PathXConstants.*;

/**
 *
 * @author zeb
 */
public class Player extends Sprite {
private float targetx;
private float targety;
boolean movingToTarget;

 public boolean isMovingToTarget() 
    { 
        return movingToTarget; 
    }

    public float getTargetX() {
        return targetx;
    }

    public void setTargetX(float x) {
        this.x = x;
    }

    public float getTargetY() {
        return targety;
    }

    public void setTargetY(float y) {
        this.y = targety;
    }

    public Player(SpriteType initSpriteType, float initX, float initY, float initVx, float initVy, String initState) {
        super(initSpriteType, initX, initY, initVx, initVy, initState);
    }
    
        /**
     * This method calculates the distance from this tile's current location
     * to the target coordinates on a direct line.
     * 
     * @return The total distance on a direct line from where the tile is
     * currently, to where its target is.
     */
    public float calculateDistanceToTarget()
    {
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = targetx - x;
        
        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = targety - y;
        
        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
        
        // AND RETURN THE DISTANCE
        return distance;
    }
    
        /**
     * Allows the tile to start moving by initializing its properly
     * scaled velocity vector pointed towards it target coordinates.
     * 
     * @param maxVelocity The maximum velocity of this tile, which
     * we'll then compute the x and y axis components for taking into
     * account the trajectory angle.
     */
    public void startMovingToTarget(int maxVelocity)
    {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;
        
        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetx - x;
        float diffY = targety - y;
        float tanResult = diffY/diffX;
        float angleInRadians = (float)Math.atan(tanResult);
        
        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float)(maxVelocity * Math.cos(angleInRadians));
        
        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) vX *= -1;
        if ((diffX > 0) && (vX < 0)) vX *= -1;
        
        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float)(maxVelocity * Math.sin(angleInRadians));        
        
        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) vY *= -1;
        if ((diffY > 0) && (vY < 0)) vY *= -1;
    }
    
    /**
     * Called each frame, this method ensures that this tile is updated
     * according to the path it is on.
     * 
     * @param game The Sorting Hat game this tile is part of.
     */
    @Override
    public void update(MiniGame game)
    {
      
        // IF NOT, IF THIS TILE IS ALMOST AT ITS TARGET DESTINATION,
        // JUST GO TO THE TARGET AND THEN STOP MOVING
         if (calculateDistanceToTarget() < MAX_MOVE_VELOCITY)
        {
            vX = 0;
            vY = 0;
            x = targetx;
            y = targety;
            movingToTarget = false;
        }
        // OTHERWISE, JUST DO A NORMAL UPDATE, WHICH WILL CHANGE ITS POSITION
        // USING ITS CURRENT VELOCITY.
        else
        {
            super.update(game);
        }
    }
}
