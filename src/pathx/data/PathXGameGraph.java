
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.data;

import java.util.ArrayList;
import mini_game.MiniGame;
import pathx.data.PathXLevel;
import pathx.data.PathXDataModel;
import pathx.ui.PathXMiniGame;

/**
 *
 * @author zeb
 */

public class PathXGameGraph {

    private PathXDataModel data;

    public PathXGameGraph(MiniGame minigame) {
        data = (PathXDataModel) minigame.getDataModel();
    }

    /**
     * This helper method returns all the neighbors for a particular
     * intersection
     *
     * @param source This is the source intersection for whom we are finding
     * neighbors
     * @return This is an arraylist of intersections which are neighbors to the
     * source
     * @todo implement checks for one way roads and all that jazz
     */
    public ArrayList<Intersection> getNeighbors(Intersection source) {
        
        ArrayList<Road> roads = data.getLevel().getRoads();
        //System.out.println("Road size: " + roads.size());
        ArrayList<Intersection> connected = new ArrayList<>();
        //System.out.println("Source: " + source.toString());
        for (int i = 0; i < roads.size(); i++) {
            //get the first node in the road list 
            Intersection one = roads.get(i).getNode1();
            Intersection two = roads.get(i).getNode2();
            //System.out.println(one.getX() + " " + one.getY());
            //System.out.println(two.getX() + " " + two.getY());
            if (two.getX() == source.getX() && two.getY() == source.getY()) {
                //System.out.println("equals");
                connected.add(one);
            }
            if (one.getX() == source.getX() && one.getY() == source.getY()) {
                //System.out.println("equals");
                connected.add(two);
            }
        }
        return connected;

    }

    public Intersection findCheapestNeighbor(Intersection source) {
        //using an intersection find all of the neighbors for that intersection
        ArrayList<Intersection> neighbors = getNeighbors(source);
        //get all the roads for a level
        ArrayList<Road> roads = data.getLevel().getRoads();
        //the speed limit of a road
        int speed = 0;
        //a temporary value
        int temp = 0;
        Intersection answer=null;
        System.out.println(neighbors.size());
        for (int i = 0; i < neighbors.size(); i++) {
            for(int j=0; j<roads.size(); j++){
            Intersection first = roads.get(j).getNode1();
            Intersection second = roads.get(j).getNode2();
              
            if (neighbors.get(i) != null) {
                //System.out.println("HEY I GOT HERE!!!!!1111");
               
                if (neighbors.get(i).getX()==first.getX() && neighbors.get(i).getY()==first.getY()) {
                    temp = roads.get(j).getSpeedLimit();
                    if (temp > speed) {
                        speed = temp;
                    
                        answer=first;
                    }
                   
                }
                
                if (neighbors.get(i).getX()==second.getX() && neighbors.get(i).getY()==second.getY()) {
                    temp = roads.get(j).getSpeedLimit();
                    if (temp >speed) {
                        speed = temp;
                        answer=second;
                    }
                }
            }
            }
        }
        System.out.println(speed);
        return answer;
    }
}
