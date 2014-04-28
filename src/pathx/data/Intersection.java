/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

/**
 *
 * @author zeb
 */
public class Intersection {
    // INTERSECTION LOCATION
    public int x;
    public int y;
    
    // IS IT OPEN OR NOT
    public boolean open;

    /**
     * Constructor allows for a custom location, note that all
     * intersections start as open.
     */
    public Intersection(int initX, int initY)
    {
        x = initX;
        y = initY;
        open = true;
    }

    // ACCESSOR METHODS
    public int getX()       {   return x;       }
    public int getY()       {   return y;       }
    public boolean isOpen() {   return open;    }
    
    // MUTATOR METHODS
    public void setX(int x)
    {   this.x = x;         }
    public void setY(int y)
    {   this.y = y;         }
    public void setOpen(boolean open)
    {   this.open = open;   }
    
    /**
     * This toggles the intersection open/closed.
     */
    public void toggleOpen()
    {
        open = !open;
    }
    
    /**
     * Returns a textual representation of this intersection.
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
