public class MineEscape {
	//3 instance variables: the map of the current mine, a count variable that holds the amount of gold being held, and an array for the amount of each specific key is being held if any
    private Map map;
    private int numGold;
    private int[] numKeys;

    public MineEscape(String filename) {
        try {
            map = new Map(filename);
            numGold = 0;
            numKeys = new int[3]; 
          } catch (Exception e) {
        	  System.out.println(e.getMessage());
          }
    }
    
    private MapCell findNextCell(MapCell cell) {
        // Rule 1: if curr is adjacent to the exit cell, go to the exit cell
        for (int i = 0; i < 4; i++) {
            MapCell cellNeighnour = cell.getNeighbour(i);
            
            if (cellNeighnour != null && cellNeighnour.isExit() && !cellNeighnour.isMarked()  && !cellNeighnour.isLava() && !cellNeighnour.isWall()) {
            	return cellNeighnour;
            }
        }

        // Rule 2: if curr is adjacent to one or more cells that contain a collectible item
        // (a key or gold), go to the neighbor with the smallest index containing a collectible
        MapCell smallestCollectibleIndex = null;
        for (int i = 0; i < 4; i++) {
            MapCell cellNeighbour = cell.getNeighbour(i);
            //if the neighbour cell exists and the neighbouring cell is a key cell or a gold cell and the respective neighbour is not yet marked...
            if (cellNeighbour != null && (cellNeighbour.isKeyCell() || cellNeighbour.isGoldCell()) && !cellNeighbour.isMarked()) {
            	  //the first time a valid neighbor meeting the conditions is found, it will be assigned to the cellNeighbour 
            	  if (smallestCollectibleIndex == null){
                    smallestCollectibleIndex = cellNeighbour;
                }
            }
        }
        //checks the colour of the key cell and properly updates the amount of that colour of key if necessary
        if (smallestCollectibleIndex != null) {
            if (smallestCollectibleIndex.isKeyCell()) {
            	int keyColour;
            	if (smallestCollectibleIndex.isRed()) {
            	    keyColour = 0;
            	    numKeys[keyColour]++;
            	} else if (smallestCollectibleIndex.isGreen()) {
            	    keyColour = 1;
            	    numKeys[keyColour]++;
            	} else if (smallestCollectibleIndex.isBlue()) {
            	    keyColour = 2;
            	    numKeys[keyColour]++;
            	} 
            	
            //if the cell is a gold cell increase the count of gold	
            } else if (smallestCollectibleIndex.isGoldCell()) {
                numGold++;
            }
            //change a collectible item cell to a floor cell
            smallestCollectibleIndex.changeToFloor();
            return smallestCollectibleIndex;
        }

        // Rule 3: if curr is adjacent to one or more floor cells, go to the neighbor with the smallest index that is a floor cell
        MapCell smallestFloorIndex = null;
        for (int i = 0; i < 4; i++) {
            MapCell cellNeighbour = cell.getNeighbour(i);
            //if the neighbour cell is not null, it is a floor cell, and it has not been marked yet...
            if (cellNeighbour != null && cellNeighbour.isFloor() && !cellNeighbour.isMarked()) {
            	  if (smallestFloorIndex == null) {
                    smallestFloorIndex = cellNeighbour;
                }
            }
        }
        if (smallestFloorIndex != null) {
            return smallestFloorIndex;
        }

        // Rule 4: if curr is adjacent to one or more locked door cells, go to the neighbor with the smallest index
        // that is a locked door cell for which you have a key of the same color.
        MapCell smallestLockedDoorIndex = null;
        for (int i = 0; i < 4; i++) {
            MapCell cellNeighbour = cell.getNeighbour(i);
            //if the cell neighbour is not null, it is a lock cell,and it is not marked yet...
            if (cellNeighbour != null && cellNeighbour.isLockCell() && !cellNeighbour.isMarked()) {
                int lockColour;
                //find the colour of the respective lock
                if (cellNeighbour.isRed()) {
                    lockColour = 0;
                } else if (cellNeighbour.isGreen()) {
                    lockColour = 1;
                } else {
                    lockColour = 2;
                }
                //if there is a key that has been picked up that matches the lock colour
                if (numKeys[lockColour] > 0) {
                	//if the next cell is null, keep track of the smallest index door that can be unlocked.
                	  if (smallestLockedDoorIndex == null) {
                        smallestLockedDoorIndex = cellNeighbour;
                    }
                }
            }
        }
        //this code determines the colour of the locked door and decrements the count of the key for that lock colour
        //a collectible item cell must be chnaged back to a floor cell
        if (smallestLockedDoorIndex != null) {
            int lockColour;

            if (smallestLockedDoorIndex.isRed()) {
                lockColour = 0;
            } else if (smallestLockedDoorIndex.isGreen()) {
                lockColour = 1;
            } else {
                lockColour = 2;
            }

            numKeys[lockColour]--;
            smallestLockedDoorIndex.changeToFloor();
            return smallestLockedDoorIndex;
        }

        // Rule 5: if none of these conditions are met, return null to indicate that you cannot proceed and must backtrack
        return null;
    }

    public String findEscapePath() {

        ArrayStack<MapCell> S = new ArrayStack<>();
        MapCell startCell = map.getStart();
        S.push(startCell);
        startCell.markInStack();
        boolean running = true;
        String path = "Path: " + startCell.getID() + " ";

        //while the stack is not empty and the maze path is still ongoing...
        while (!S.isEmpty() && running) {
        	//view what the cell at the top of the stack says
            MapCell currentCell = S.peek();
            
            // if curr is the exit cell, set running = false and end the loop immediately
            if (currentCell.isExit()) {
                running = false;
                break;
            }

            // if curr is a key cell, determine its colour and update numKeys accordingly
            // (see note below) if curr is a gold cell, update numGold accordingly
            if (currentCell.isKeyCell()) {
            	//helper method 1 is called to determine the key's colour
                int keyColour = getKeyColor(currentCell);
                if (keyColour >= 0 && numKeys[keyColour] > 0) {
                    numKeys[keyColour]--;
                    currentCell.changeToFloor();
                }
            }
            //if the current cell is a gold one pick up the gold and change it to a floor cell
            if (currentCell.isGoldCell()) {
                numGold++;
                currentCell.changeToFloor();
            }

            // if curr is adjacent to lava, reset numGold to 0
            //helper method 2 ic called to determine if a cell is adjacent to lava
            if (isAdjacentToLava(currentCell)) {
                numGold = 0;
            }
            //initialize an object to determine current's next cell
            MapCell nextNeighbour = findNextCell(currentCell);

            if (nextNeighbour == null) {
                //set curr = pop off stack and mark curr as out-of-stack
                currentCell = S.pop();
                currentCell.markOutStack();
            } else {
                // update path string by adding next
            	// push next onto S
            	// mark next as in-stack
                path += nextNeighbour.getID() + " ";
                S.push(nextNeighbour);
                nextNeighbour.markInStack();

                // if next is a locked door cell
                if (nextNeighbour.isLockCell()) {
                    // determine colour of locked door
                	//helper method 3 is called to determine the lock's colour
                    int lockColour = getLockColor(nextNeighbour);
                    //unlock the door** and update numKeys accordingly
                    //checks whether the lockColour is a valid index for the numKeys array 
                    //also checks whether there are keys of the specified color available 
                    if (lockColour >= 0 && numKeys[lockColour] > 0) {
                    	//
                        numKeys[lockColour]--;
                        nextNeighbour.changeToFloor();
                    }
                } 
            }
        }
        //the path is complete or no path is possible so set running to false
        //gold count is returned along with the specific cells in the stack
        if (!running) {
            path += numGold + "G";
            return path;
        } else {
            return "No solution found";
        }
    }
    
    private int getKeyColor(MapCell cell) {
        if (cell.isRed()) {
            return 0;
        } else if (cell.isGreen()) {
            return 1;
        } else if (cell.isBlue()) {
            return 2;
        } else {
            return -1;
        }
    }
    
    // Helper method to check if a cell is adjacent to lava
    //if one or more of the cell's neighbour is adjacent to lava return true
    private boolean isAdjacentToLava(MapCell cell) {
        for (int i = 0; i < 4; i++) {
            MapCell cellNeighbour = cell.getNeighbour(i);
            if (cellNeighbour != null && cellNeighbour.isLava()) {
                return true;
            }
        }
        return false;
    }

    private int getLockColor(MapCell cell) {
        if (cell.isRed()) {
            return 0;
        } else if (cell.isGreen()) {
            return 1;
        } else if (cell.isBlue()) {
            return 2;
        } else {
            return -1;
        }
    }
}



