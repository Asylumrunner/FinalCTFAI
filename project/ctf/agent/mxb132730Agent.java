package ctf.agent;

import ctf.common.*;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class mxb132730Agent extends Agent {

	/* Mean Joe Green is a Hall of Fame defensive tackle who played for the Steelers
	in the 70s. In his spirit, this AI is centered on defending our flag from opponents.
	
	Mean Joe Green's goals are to initially run for our own flag. Since the flag is against
	a wall, the goal is to get to the flag, then plant a bomb on the flag's north and south
	sides, if possible. Then, Mean Joe will plant himself on the left side of the flag, pacing
	a patrol path which consists of going up and down the three spaces adjacent to the flag and
	bombs, forming an inpenetrable defense.
	
*/
	int mode;
	int defenderState; //state stores which state the Defender is currently in. 
	//0 indicates MOVING TO THE FLAG, 1 indicates SENTRY

	int movesSinceAdjacency;
	
	boolean firstMove;
	
	
	public mxb132730Agent(){
		defenderState = 0;
		movesSinceAdjacency = 0;
		firstMove = true;
	}
	
	/* Stephen's Nonsense. Clean for final */
	
	private int size = 0;
    private int steps = 0;
    // private int x = 0;
    // private int y = 0;
    private boolean isEast;
    private boolean isNorth;
    
    private boolean empty = false;
    private boolean x = false;
    private boolean traps = false;
    private boolean wall = false;
    private boolean simple = false;
    
    private int[] xMoveEast = {AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
            AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
            AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
            AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
            AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_NORTH,
            AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
            AgentAction.MOVE_NORTH, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
            AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
            AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
            AgentAction.MOVE_EAST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
            AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH};
    
    private int[] xMoveWest = {AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
            AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
            AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
            AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
            AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_NORTH,
            AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
            AgentAction.MOVE_NORTH, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
            AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
            AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
            AgentAction.MOVE_WEST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
            AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH};
    
    private int[] wallMoveEast =
            {AgentAction.MOVE_WEST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST};
    
    private int[] wallMoveWest =
            {AgentAction.MOVE_EAST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST};
    
    private int[] simpleMoveEast =
            {AgentAction.MOVE_WEST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_EAST,
                    AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_SOUTH};
    
    private int[] simpleMoveWest =
            {AgentAction.MOVE_EAST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_WEST,
                    AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_SOUTH};
    
    private int[] trapsMoveEast =
            {AgentAction.MOVE_WEST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_EAST};
    
    private int[] trapsMoveWest =
            {AgentAction.MOVE_EAST, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_EAST, AgentAction.MOVE_EAST, AgentAction.MOVE_EAST,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH,
                    AgentAction.MOVE_SOUTH, AgentAction.MOVE_SOUTH, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_WEST, AgentAction.MOVE_WEST,
                    AgentAction.MOVE_WEST, AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH,
                    AgentAction.MOVE_NORTH, AgentAction.MOVE_NORTH, AgentAction.MOVE_WEST};
	
	@SuppressWarnings("static-access")
	@Override
	public int getMove(AgentEnvironment inEnvironment) {
		
		if(firstMove){
			if(inEnvironment.isObstacleNorthImmediate()){
				mode = 2;
				firstMove = false;
			}
			else{
				mode = 1;
				firstMove = false;
			}
		}
		
		if(mode == 2){
			return getAggressorMove(inEnvironment);
		}
		
		Random rand = new Random();
		
		if(defenderState == 0){
			if(inEnvironment.isBaseNorth(inEnvironment.OUR_TEAM, true)){
				if(inEnvironment.isBaseEast(inEnvironment.ENEMY_TEAM, false)){
					return AgentAction.MOVE_EAST;
				}
				else{
					return AgentAction.MOVE_WEST;
				}
			}
			
			else if(inEnvironment.isBaseWest(inEnvironment.OUR_TEAM, true) || inEnvironment.isBaseEast(inEnvironment.OUR_TEAM, true)){
				defenderState = 1;
				return AgentAction.DO_NOTHING;
				
			}
			
			return AgentAction.MOVE_NORTH;
		}
		else if (defenderState == 1){
			if(movesSinceAdjacency > 1){
				defenderState = 0;
				return AgentAction.MOVE_SOUTH;
			}
			
			if(inEnvironment.isBaseSouth(inEnvironment.OUR_TEAM, true) || inEnvironment.isBaseNorth(inEnvironment.OUR_TEAM, true)){
				movesSinceAdjacency = 0;
				if(inEnvironment.isBaseEast(inEnvironment.ENEMY_TEAM, false)){
					return AgentAction.MOVE_EAST;
				}
				else{
					return AgentAction.MOVE_WEST;
				}
			}
			
			else if(inEnvironment.isBaseWest(inEnvironment.OUR_TEAM, true) || inEnvironment.isBaseEast(inEnvironment.OUR_TEAM, true)){
				movesSinceAdjacency = 0;
				
				if(rand.nextInt(2) == 0){
					return AgentAction.MOVE_NORTH;
				}
				else{
					return AgentAction.MOVE_SOUTH;
				}
			}
			
			else{
				movesSinceAdjacency++;
				if(rand.nextInt(2) == 0){
					if(inEnvironment.isBaseSouth(inEnvironment.OUR_TEAM, false)){
						if(!inEnvironment.isObstacleSouthImmediate()){
							return AgentAction.MOVE_SOUTH;
						}
						else{
							if(inEnvironment.isBaseEast(inEnvironment.ENEMY_TEAM, false)){
								return AgentAction.MOVE_WEST;
							}
							else{
								return AgentAction.MOVE_EAST;
							}
						}
						
					}
					else{
						if(!inEnvironment.isObstacleNorthImmediate()){
							return AgentAction.MOVE_NORTH;
						}
						else{
							if(inEnvironment.isBaseEast(inEnvironment.ENEMY_TEAM, false)){
								return AgentAction.MOVE_WEST;
							}
							else{
								return AgentAction.MOVE_EAST;
							}
						}
					}
				}
				
				else{
					if(inEnvironment.isBaseEast(inEnvironment.ENEMY_TEAM, false)){
						if(!inEnvironment.isObstacleWestImmediate()){
							return AgentAction.MOVE_WEST;
						}
						else{
							if(inEnvironment.isBaseSouth(inEnvironment.OUR_TEAM, false)){
								return AgentAction.MOVE_SOUTH;
							}
							else{
								return AgentAction.MOVE_SOUTH;
							}
						}
					}
					else{
						if(!inEnvironment.isObstacleEastImmediate()){
							return AgentAction.MOVE_EAST;
						}
						else{
							if(inEnvironment.isBaseSouth(inEnvironment.OUR_TEAM, false)){
								return AgentAction.MOVE_SOUTH;
							}
							else{
								return AgentAction.MOVE_SOUTH;
							}
						}
					}
				}
			}
			
		}
		

		
		return AgentAction.DO_NOTHING;
	}
	
@SuppressWarnings("static-access")
    public int getAggressorMove(AgentEnvironment env) {
        if(this.isReset(env)) {
            this.steps = 0;
        }
        
        if(!x && !empty && !traps && !wall && !simple) {
            if(steps == 0) {
                this.isEast = env.isObstacleEastImmediate();
                this.isNorth = env.isObstacleNorthImmediate();
                
                if(!this.isNorth)
                    return AgentAction.DO_NOTHING;
                
                if(this.isEast) {
                    steps++;
                    return AgentAction.MOVE_WEST;
                } else {
                    steps++;
                    return AgentAction.MOVE_EAST;
                }
            } else if(this.isEast) {
                if(steps == 1) {
                    if(env.isObstacleSouthImmediate()) {
                        this.x = true;
                    } else {
                        steps++;
                        return AgentAction.MOVE_SOUTH;
                    }
                } else if(steps == 2) {
                    if(env.isObstacleWestImmediate()) {
                        traps = true;
                    } else {
                        steps++;
                        return AgentAction.MOVE_WEST;
                    }
                } else if(steps == 3) {
                    if(env.isObstacleSouthImmediate()) {
                        simple = true;
                    } else {
                        steps++;
                        return AgentAction.MOVE_WEST;
                    }
                } else if(steps == 4) {
                    if(env.isObstacleWestImmediate()) {
                        wall = true;
                    } else {
                        empty = true;
                    }
                }
            } else {
                if(steps == 1) {
                    if(env.isObstacleSouthImmediate()) {
                        this.x = true;
                    } else {
                        steps++;
                        return AgentAction.MOVE_SOUTH;
                    }
                } else if(steps == 2) {
                    if(env.isObstacleEastImmediate()) {
                        traps = true;
                    } else {
                        steps++;
                        return AgentAction.MOVE_EAST;
                    }
                } else if(steps == 3) {
                    if(env.isObstacleSouthImmediate()) {
                        simple = true;
                    } else {
                        steps++;
                        return AgentAction.MOVE_EAST;
                    }
                } else if(steps == 4) {
                    if(env.isObstacleEastImmediate()) {
                        wall = true;
                    } else {
                        empty = true;
                    }
                }
            }
        }
        
        if(!this.isNorth) {
            return AgentAction.DO_NOTHING;
        }
        if(this.isEast) {
            if(x) {
                int action = xMoveEast[steps % xMoveEast.length];
                steps++;
                return action;
            }
            if(traps) {
                int action = trapsMoveEast[steps % trapsMoveEast.length];
                steps++;
                return action;
            }
            if(wall) {
                int action = wallMoveEast[steps % wallMoveEast.length];
                steps++;
                return action;
            }
            if(simple) {
                int action = simpleMoveEast[steps % simpleMoveEast.length];
                steps++;
                return action;
            }
        } else {
            if(x) {
                int action = xMoveWest[steps % xMoveWest.length];
                steps++;
                return action;
            }
            if(traps) {
                int action = trapsMoveWest[steps % trapsMoveWest.length];
                steps++;
                return action;
            }
            if(wall) {
                int action = wallMoveWest[steps % wallMoveWest.length];
                steps++;
                return action;
            }
            if(simple) {
                int action = simpleMoveWest[steps % simpleMoveWest.length];
                steps++;
                return action;
            }
        }
        
        if(empty) {
            return getSimpleMove(env);
        }
        return AgentAction.DO_NOTHING;
        
    }
	    
    public boolean isReset(AgentEnvironment env) {
        if(env.hasFlag())
            return false;
        if(this.isEast) {
            if(this.isNorth) {
                return env.isObstacleNorthImmediate() && env.isObstacleEastImmediate()
                        && env.isBaseSouth(AgentEnvironment.OUR_TEAM, false);
            } else {
                return env.isObstacleSouthImmediate() && env.isObstacleEastImmediate()
                        && env.isBaseNorth(AgentEnvironment.OUR_TEAM, false);
            }
        } else {
            if(this.isNorth) {
                return env.isObstacleNorthImmediate() && env.isObstacleWestImmediate()
                        && env.isBaseSouth(AgentEnvironment.OUR_TEAM, false);
            } else {
                return env.isObstacleSouthImmediate() && env.isObstacleWestImmediate()
                        && env.isBaseNorth(AgentEnvironment.OUR_TEAM, false);
            }
        }
    }
    
    public int getSimpleMove(AgentEnvironment inEnvironment) {
        
        // booleans describing direction of goal
        // goal is either enemy flag, or our base
        boolean goalNorth;
        boolean goalSouth;
        boolean goalEast;
        boolean goalWest;
        
        
        if(!inEnvironment.hasFlag()) {
            // make goal the enemy flag
            goalNorth = inEnvironment.isFlagNorth(AgentEnvironment.ENEMY_TEAM, false);
            
            goalSouth = inEnvironment.isFlagSouth(AgentEnvironment.ENEMY_TEAM, false);
            
            goalEast = inEnvironment.isFlagEast(AgentEnvironment.ENEMY_TEAM, false);
            
            goalWest = inEnvironment.isFlagWest(AgentEnvironment.ENEMY_TEAM, false);
        } else {
            // we have enemy flag.
            // make goal our base
            goalNorth = inEnvironment.isBaseNorth(AgentEnvironment.OUR_TEAM, false);
            
            goalSouth = inEnvironment.isBaseSouth(AgentEnvironment.OUR_TEAM, false);
            
            goalEast = inEnvironment.isBaseEast(AgentEnvironment.OUR_TEAM, false);
            
            goalWest = inEnvironment.isBaseWest(AgentEnvironment.OUR_TEAM, false);
        }
        
        // now we have direction booleans for our goal
        
        // check for immediate obstacles blocking our path
        boolean obstNorth = inEnvironment.isObstacleNorthImmediate();
        boolean obstSouth = inEnvironment.isObstacleSouthImmediate();
        boolean obstEast = inEnvironment.isObstacleEastImmediate();
        boolean obstWest = inEnvironment.isObstacleWestImmediate();
        
        
        // if the goal is north only, and we're not blocked
        if(goalNorth && !goalEast && !goalWest && !obstNorth) {
            // move north
            return AgentAction.MOVE_NORTH;
        }
        
        // if goal both north and east
        if(goalNorth && goalEast) {
            // pick north or east for move with 50/50 chance
            if(Math.random() < 0.5 && !obstNorth) {
                return AgentAction.MOVE_NORTH;
            }
            if(!obstEast) {
                return AgentAction.MOVE_EAST;
            }
            if(!obstNorth) {
                return AgentAction.MOVE_NORTH;
            }
        }
        
        // if goal both north and west
        if(goalNorth && goalWest) {
            // pick north or west for move with 50/50 chance
            if(Math.random() < 0.5 && !obstNorth) {
                return AgentAction.MOVE_NORTH;
            }
            if(!obstWest) {
                return AgentAction.MOVE_WEST;
            }
            if(!obstNorth) {
                return AgentAction.MOVE_NORTH;
            }
        }
        
        // if the goal is south only, and we're not blocked
        if(goalSouth && !goalEast && !goalWest && !obstSouth) {
            // move south
            return AgentAction.MOVE_SOUTH;
        }
        
        // do same for southeast and southwest as for north versions
        if(goalSouth && goalEast) {
            if(Math.random() < 0.5 && !obstSouth) {
                return AgentAction.MOVE_SOUTH;
            }
            if(!obstEast) {
                return AgentAction.MOVE_EAST;
            }
            if(!obstSouth) {
                return AgentAction.MOVE_SOUTH;
            }
        }
        
        if(goalSouth && goalWest && !obstSouth) {
            if(Math.random() < 0.5) {
                return AgentAction.MOVE_SOUTH;
            }
            if(!obstWest) {
                return AgentAction.MOVE_WEST;
            }
            if(!obstSouth) {
                return AgentAction.MOVE_SOUTH;
            }
        }
        
        // if the goal is east only, and we're not blocked
        if(goalEast && !obstEast) {
            return AgentAction.MOVE_EAST;
        }
        
        // if the goal is west only, and we're not blocked
        if(goalWest && !obstWest) {
            return AgentAction.MOVE_WEST;
        }
        
        // otherwise, make any unblocked move
        if(!obstNorth) {
            return AgentAction.MOVE_NORTH;
        } else if(!obstSouth) {
            return AgentAction.MOVE_SOUTH;
        } else if(!obstEast) {
            return AgentAction.MOVE_EAST;
        } else if(!obstWest) {
            return AgentAction.MOVE_WEST;
        } else {
            // completely blocked!
            return AgentAction.DO_NOTHING;
        }
    }
    
    @Override
    public void drawIcon(Graphics inGraphics, int inWide, int inHigh) {
        int xVerts[] = new int[16];
        xVerts[0] = xVerts[8] = inWide / 2;
        xVerts[1] = xVerts[7] = inWide / 3;
        xVerts[2] = xVerts[6] = inWide / 8;
        xVerts[3] = xVerts[5] = inWide / 6;
        xVerts[12] = inWide;
        xVerts[9] = xVerts[15] = 2 * (inWide / 3);
        xVerts[10] = xVerts[14] = 7 * (inWide / 8);
        xVerts[11] = xVerts[13] = 5 * (inWide / 6);
        xVerts[4] = 0;
        
        int yVerts[] = new int[16];
        yVerts[0] = inHigh;
        yVerts[1] = yVerts[15] = 5 * (inHigh / 6);
        yVerts[2] = yVerts[14] = 7 * (inHigh / 8);
        yVerts[3] = yVerts[13] = 2 * (inHigh / 3);
        yVerts[12] = yVerts[4] = inHigh / 2;
        yVerts[9] = yVerts[7] = inHigh / 6;
        yVerts[10] = yVerts[6] = inHigh / 8;
        yVerts[11] = yVerts[5] = inHigh / 3;
        yVerts[8] = 0;
        
        int xInner[] = new int[16];
        int yInner[] = new int[16];
        
        for(int i = 0; i < 16; i++) {
            xInner[i] = (xVerts[i] / 2) + (inWide / 4);
            yInner[i] = (yVerts[i] / 2) + (inHigh / 4);
        }
        
        // fill the outside
        inGraphics.setColor(Color.magenta);
        inGraphics.fillPolygon(xVerts, yVerts, 16);
        
        // fill the inside
        Random r = new Random();
        Color innerColor = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        inGraphics.setColor(innerColor);
        inGraphics.fillPolygon(xInner, yInner, 16);
        
        
        // line with black
        inGraphics.setColor(Color.black);
        inGraphics.drawPolygon(xVerts, yVerts, 16);
        inGraphics.drawPolygon(xInner, yInner, 16);
    }
    

}
