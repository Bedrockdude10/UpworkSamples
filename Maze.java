//import javalib.impworld.World;
//import javalib.impworld.WorldScene;
//import javalib.worldimages.*;
//import tester.Tester;
//import java.awt.*;
//import java.util.*;
//
////------------------------------- GameWorld Class -----------------------------------------
//
//// Represents the game world
//class GameWorld extends World {
//  // static constants for cell size & tick rate
//  static final int CELL_SCALE = 20;
//  // tick rate doesn't actually do anything yet but is necessary for BigBang
//  static final double TICK_RATE = 0.01;
//  // horizontal and vertical size for the game board
//  int xSize;
//  int ySize;
//  // hash map for all the vertices on the board
//  HashMap<Vertex, Vertex> hashMap = new HashMap<Vertex, Vertex>();
//  // the weighted list of edges
//  ArrayList<Edge> edgeList = new ArrayList<Edge>();
//  // the minimum spanning tree
//  ArrayList<Edge> minSpanTree = new ArrayList<Edge>();
//  // the path for searched vertices
//  ArrayList<Vertex> searchPath = new ArrayList<Vertex>();
//  // blank world scene to display to the player
//  WorldScene gameScene = new WorldScene(0, 0);
//  // the list of vertices on the board
//  ArrayList<ArrayList<Vertex>> gameBoard;
//  // object representing the player
//  Player player;
//  //the vertex that causes an end game
//  Vertex end;
//  // boolean to track whether or not the maze has been solved
//  boolean solved;
//  // time elapsed since game was last reset
//  int time;
//
//  // standard constructor
//  GameWorld(int xSize, int ySize) {
//    this.xSize = xSize;
//    this.ySize = ySize;
//    this.gameBoard = this.makeBoard(xSize, ySize);
//    this.makeEdges(this.gameBoard);
//    this.makeHashMap(gameBoard);
//    this.kruskalsAlgo();
//    this.player = new Player(gameBoard.get(0).get(0));
//    this.end = this.gameBoard.get(ySize - 1).get(xSize - 1);
//    this.solved = false;
//    this.time = 0;
//    this.drawGameWorld();
//  }
//
//  // blank constructor for testing purposes
//  GameWorld() {
//
//  }
//
//  // ------------------------------ Make gameBoard methods ---------------------------
//
//   //creates the gameBoard and connects all the vertices and edges 
//   //creates a hashMap for the board
//  ArrayList<ArrayList<Vertex>> makeBoard(int xSize, int ySize) {
//    ArrayList<ArrayList<Vertex>> gameBoard = new ArrayList<ArrayList<Vertex>>();
//    for (int i = 0; i < ySize; i++) {
//      gameBoard.add(new ArrayList<Vertex>());
//      ArrayList<Vertex> r = gameBoard.get(i);
//      for (int j = 0; j < xSize; j++) {
//        r.add(new Vertex(new Posn(j, i)));
//      }
//    }
//    this.connectVertices(gameBoard);
//    this.makeEdges(gameBoard);
//    this.makeHashMap(gameBoard);
//    return gameBoard;
//  }
//
//  // connects all vertices on a gameBoard to one another in the right order
//  void connectVertices(ArrayList<ArrayList<Vertex>> board) {
//    for (int i = 0; i < this.ySize; i++) {
//      for (int j = 0; j < this.xSize; j++) {
//        if (j + 1 < this.xSize) {
//          board.get(i).get(j).right = board.get(i).get(j + 1);
//        }
//        if (j - 1 >= 0) {
//          board.get(i).get(j).left = board.get(i).get(j - 1);
//        }
//        if (i + 1 < this.ySize) {
//          board.get(i).get(j).bottom = board.get(i + 1).get(j);
//        }
//        if (i - 1 >= 0) {
//          board.get(i).get(j).top = board.get(i - 1).get(j);
//        }
//      }
//    }
//  }
//
//  // returns a sorted ArrayList for all the edges in the game world
//  ArrayList<Edge> makeEdges(ArrayList<ArrayList<Vertex>> board) {
//    Random r = new Random();
//    for (int i = 0; i < board.size(); i++) {
//      for (int j = 0; j < board.get(i).size(); j++) {
//        if (j < board.get(i).size() - 1) {
//          edgeList.add(new Edge(board.get(i).get(j), board.get(i).get(j).right, r.nextInt(50)));
//        }
//        if (i < board.size() - 1) {
//          edgeList.add(new Edge(board.get(i).get(j), board.get(i).get(j).bottom,
//              (int) r.nextInt(50)));
//        }
//      }
//    }
//    Collections.sort(edgeList, new CompareWeight());
//    return edgeList;
//  }
//
//  // creates a hash map where every vertex is connected to itself
//  HashMap<Vertex, Vertex> makeHashMap(ArrayList<ArrayList<Vertex>> board) {
//    for (int i = 0; i < board.size(); i++) {
//      for (int j = 0; j < board.get(i).size(); j++) {
//        this.hashMap.put(board.get(i).get(j), board.get(i).get(j));
//      }
//    }
//    return hashMap;
//  }
//
//  // method using Kruskal's algo to create the mst
//  ArrayList<Edge> kruskalsAlgo() {
//    int i = 0;
//    while (this.minSpanTree.size() < this.edgeList.size() && i < this.edgeList.size()) {
//      Edge edge = edgeList.get(i);
//      if (this.findVertex(this.findVertex(edge.from))
//          .equals(this.findVertex(this.findVertex(edge.to)))) {
//        // no action
//      }
//      else {
//        minSpanTree.add(edge);
//        union(this.findVertex(edge.from), this.findVertex(edge.to));
//      }
//      i ++;
//    }
//    // adds border edges for each vertex in the mst
//    for (int y = 0; y < this.ySize; y += 1) {
//      for (int x = 0; x < this.xSize; x += 1) {
//        for (Edge edge : this.minSpanTree) {
//          if (this.gameBoard.get(y).get(x).equals(edge.from) 
//              || this.gameBoard.get(y).get(x).equals(edge.to)) {
//            this.gameBoard.get(y).get(x).borderList.add(edge);
//          }
//        }
//      }
//    }
//    return this.minSpanTree;
//  }
//
//  // Changes two vertices to be the union of themselves in the hashmap
//  // The first input will now be the key for the second input
//  void union(Vertex input1, Vertex input2) {
//    this.hashMap.put(this.findVertex(input1), this.findVertex(input2));
//  }
//
//  // Finds the vertex in the hash map
//  Vertex findVertex(Vertex v) {
//    if (v.equals(this.hashMap.get(v))) {
//      return v;
//    }
//    else {
//      return this.findVertex(this.hashMap.get(v));
//    }
//  }
//
//  //---------------------------------------- Draw GameWorld methods ---------------------
//
//  @Override
//  // updates the game scene every 
//  public WorldScene makeScene() {
//
//    // if there are two or more vertices left in the search path, find the next vertex
//    if (searchPath.size() >= 2) {
//      this.findEndVertex();
//    }
//    // when there is only one vertex left in the search path, draw it and mark the maze as solved
//    else if (searchPath.size() == 1) {
//      this.drawEndVertex();
//    }
//    // trace the solution when the maze is solved
//    if (this.solved && end.previous != null) {
//      this.traceSolution();
//    }
//    // increments the time
//    this.time++;
//    return this.gameScene;
//  }
//
//  // Draws the gameWorld upon startup
//  WorldScene drawGameWorld() {
//    // Draw canvas
//    this.gameScene.placeImageXY(new RectangleImage(this.xSize * 2, this.ySize * 2, 
//        OutlineMode.SOLID, Color.WHITE), xSize, ySize);
//    // Draw starting square
//    this.gameScene.placeImageXY(gameBoard.get(0).get(0).draw(new Posn(this.xSize, this.ySize), 
//        Color.GREEN),
//        this.xSize, this.ySize);
//    // Draw ending square
//    this.gameScene.placeImageXY(gameBoard.get(this.ySize - 1).get(this.xSize - 1)
//        .draw(new Posn(this.xSize, this.ySize), Color.RED),
//        (xSize - 1) * CELL_SCALE + this.xSize, (ySize - 1) * CELL_SCALE + this.ySize);
//    // Draw the grid
//    for (int i = 0; i < ySize; i++) {
//      for (int j = 0; j < xSize; j++) {
//        this.switchWallBottom(this.gameBoard.get(i).get(j));
//        this.switchWallRight(this.gameBoard.get(i).get(j));
//        if (this.gameBoard.get(i).get(j).traveled) {
//          this.gameScene.placeImageXY(gameBoard.get(i).get(j).draw(new Posn(this.xSize,
//              this.ySize), Color.YELLOW), j * CELL_SCALE, i * CELL_SCALE);
//        }
//        if (gameBoard.get(i).get(j).wallRight) {
//          this.gameScene.placeImageXY(gameBoard.get(i).get(j).drawWallRight(),
//              (GameWorld.CELL_SCALE * j),
//              (GameWorld.CELL_SCALE * i));
//        }
//        if (gameBoard.get(i).get(j).wallBottom) {
//          this.gameScene.placeImageXY(gameBoard.get(i).get(j).drawWallBottom(),
//              (GameWorld.CELL_SCALE * j),
//              (GameWorld.CELL_SCALE * i));
//        }
//      }
//    }
//    // Draw the player
//    this.gameScene.placeImageXY(player.drawPlayer(), this.player.location.pos.x * CELL_SCALE, 
//        this.player.location.pos.y * CELL_SCALE);
//    return gameScene;
//  }
//
//  // changes a vertex's boolean for whether to draw a wall below the vertex
//  void switchWallRight(Vertex v) {
//    for (Edge e : this.minSpanTree) {
//      if (e.to.pos.y == e.from.pos.y) {
//        e.from.wallRight = false;
//      }
//    }
//  }
//
//  // changes a vertex's boolean for whether to draw a wall to the right of the vertex
//  void switchWallBottom(Vertex v) {
//    for (Edge e : this.minSpanTree) {
//      if (e.to.pos.x == e.from.pos.x) {
//        e.from.wallBottom = false;
//      }
//    }
//  }
//
//  //------------------------------- onKeyEvent Method --------------------------------------------`
//  public void onKeyEvent(String input) {
//    // reset the game using r or R keys
//    if (input.equals("r") || input.equals("R")) {
//      this.gameScene = this.getEmptyScene();
//      GameWorld newGame = new GameWorld(this.xSize, this.ySize);
//      this.gameBoard = newGame.gameBoard;
//      this.player = newGame.player;
//      this.end = newGame.end;
//      this.drawGameWorld();
//    }
//    // enables manual movement with the arrow keys
//    else if (input.equals("up") && player.allowed("up")) {
//      player.location.traveled = true;
//      player.location = player.location.top;
//    }
//    else if (input.equals("down") && player.allowed("down")) {
//      player.location.traveled = true;
//      player.location = player.location.bottom;
//    }
//    else if (input.equals("left") && player.allowed("left")) {
//      player.location.traveled = true;
//      player.location = player.location.left;
//    }
//    else if (input.equals("right") && player.allowed("right")) {
//      player.location.traveled = true;
//      player.location = player.location.right;
//    }    
//    else if (input.equals("d")) {
//      this.end = this.gameBoard.get(this.ySize - 1).get(this.xSize - 1);
//      this.searchPath = new SearchAlgo().pathDFS(this.gameBoard.get(0).get(0), this.gameBoard.get(this.ySize - 1)
//              .get(this.xSize - 1));
//    }
//    else if (input.equals("b")) {
//      this.end = this.gameBoard.get(this.ySize - 1).get(this.xSize - 1);
//      this.searchPath = new SearchAlgo().pathBFS(this.gameBoard.get(0).get(0), this.gameBoard.get(this.ySize - 1)
//              .get(this.xSize - 1));
//    }
////    // enables search through DFS
////    else if (input.equals("d")) {
////      this.end = this.gameBoard.get(this.ySize - 1).get(this.xSize - 1);
////      DFS DFS = new DFS(this.gameBoard.get(0).get(0), end);
////      this.searchPath = DFS.searchPath;
////    }
////    // enables search through BFS
////    else if (input.equals("b")) {
////      this.end = this.gameBoard.get(this.ySize - 1).get(this.xSize - 1);
////      BFS BFS = new BFS(this.gameBoard.get(0).get(0), end);
////      this.searchPath = BFS.searchPath;
////    }
//    // draws the player after any key presses
//    this.gameScene.placeImageXY(player.drawPlayer(), 
//        player.location.pos.x * GameWorld.CELL_SCALE, 
//        player.location.pos.y * GameWorld.CELL_SCALE);
//    this.drawGameWorld();
//  }
//
//  //-------------------------- Solution Trace Methods -------------------------------------------
//  // on reaching the victory vertex, traces the solution path back to the start of the maze
//  void traceSolution() {
//    if (this.end.pos.x == this.xSize - 1 && this.end.pos.y == this.ySize - 1) {
//      this.gameScene.placeImageXY(this.end.draw(new Posn(this.xSize, this.ySize),
//          Color.magenta), this.end.pos.x * GameWorld.CELL_SCALE,
//          this.end.pos.y * GameWorld.CELL_SCALE);
//    }
//    this.gameScene.placeImageXY(this.end.previous.draw(new Posn(this.xSize, this.ySize),
//        Color.magenta), this.end.previous.pos.x * GameWorld.CELL_SCALE,
//        this.end.previous.pos.y * GameWorld.CELL_SCALE);
//    this.end = this.end.previous;
//  }
//
//
//  void findEndVertex() {
//    Vertex v = searchPath.remove(0);
//    this.gameScene.placeImageXY(v.draw(new Posn(this.xSize, this.ySize), Color.CYAN),
//        v.pos.x * CELL_SCALE, v.pos.y * CELL_SCALE);
//  }
//
//  // removes the first item from the search path and draws it
//  void drawEndVertex() {
//    Vertex next = searchPath.remove(0);
//    this.gameScene.placeImageXY(next.draw(new Posn(this.xSize, this.ySize), Color.CYAN),
//            next.pos.x * CELL_SCALE, next.pos.y * CELL_SCALE);
//    if (!this.end.left.wallRight && this.end.left.previous != null) {
//      this.end.previous = this.end.left;
//    }
//    else if (!this.end.top.wallBottom && this.end.top.previous != null) {
//      this.end.previous = this.end.top;
//    }
//    else {
//      this.end.previous = next;
//    }
//    this.solved = true;
//  }
//}
//
////--------------------------- Vertex Class -----------------------------------------------------
////represents a vertex in the game world
//class Vertex {
//  // this vertex's position
//  Posn pos;
//  // neighboring vertices
//  Vertex left;
//  Vertex top;
//  Vertex right;
//  Vertex bottom;
//  // the vertex before this vertex in the graph
//  Vertex previous;
//  // boolean for whether to draw a wall to the right or below this vertex
//  boolean wallRight;
//  boolean wallBottom;
//  // boolean for whether or not this vertex has been traveled in the search method
//  Boolean traveled;
//  // list for all the outer edges
//  ArrayList<Edge> borderList = new ArrayList<Edge>();
//
//
//  // standard constructor
//  Vertex(Posn pos) {
//    this.pos = pos;
//    this.left = null;
//    this.top = null;
//    this.right = null;
//    this.bottom = null;
//    this.wallRight = true;
//    this.wallBottom = true;
//    this.previous = null;
//    this.traveled = false;
//  }
//
//  // draws a wall on the vertex to the right of this one
//  WorldImage drawWallRight() {
//    return new LineImage(new Posn(0, GameWorld.CELL_SCALE), Color.DARK_GRAY)
//        .movePinhole(-1 * GameWorld.CELL_SCALE, GameWorld.CELL_SCALE / -2);
//  }
//
//  // draws a wall on the vertex below this one
//  WorldImage drawWallBottom() {
//    return new LineImage(new Posn(GameWorld.CELL_SCALE, 0), Color.DARK_GRAY)
//        .movePinhole(GameWorld.CELL_SCALE / -2, GameWorld.CELL_SCALE * -1);
//  }
//
//  // draws this vertex
//  WorldImage draw(Posn pos, Color color) {
//    return new RectangleImage(GameWorld.CELL_SCALE - 2, GameWorld.CELL_SCALE - 2,
//        OutlineMode.SOLID, color).movePinhole(-pos.x * GameWorld.CELL_SCALE / pos.x / 2,
//            -pos.x * GameWorld.CELL_SCALE / pos.x / 2);
//  }
//}
//
////--------------------------- Player Class -----------------------------------------------------
//// represents the player
//class Player {
//  Vertex location;
//  int mistakes;
//
//  Player(Vertex location) {
//    this.location = location;
//  }
//
//  // draws the player
//  WorldImage drawPlayer() {
//    return new RectangleImage(GameWorld.CELL_SCALE - 3, GameWorld.CELL_SCALE - 3,
//        OutlineMode.SOLID, Color.MAGENTA).movePinhole(-GameWorld.CELL_SCALE / 2,
//            -GameWorld.CELL_SCALE / 2);
//  }
//
//  // checks if a string input for manual movement is valid for this player's current location
//  boolean allowed(String input) {
//    if (input.equals("up") && this.location.top != null) {
//      return !this.location.top.wallBottom;
//    }
//    else if (input.equals("down") && this.location.bottom != null) {
//      return !this.location.wallBottom;
//    }
//    else if (input.equals("left") && this.location.left != null) {
//      return !this.location.left.wallRight;
//    }
//    else if (input.equals("right") && this.location.right != null) {
//      return !this.location.wallRight;
//    }
//    else {
//      // add 1 to the mistake tally if the player attempts an invalid move
//      mistakes++;
//      return false;
//    }
//  }
//}
//
////--------------------------- Edge Class -----------------------------------------------------
//// represents an edge in a graph
//class Edge {
//  Vertex from;
//  Vertex to;
//  int weightValue;
//
//  // standard constructor
//  Edge(Vertex from, Vertex to, int weightValue) {
//    this.from = from;
//    this.to = to;
//    this.weightValue = weightValue;
//  }
//}
//
////--------------------------- Comparator<Edge> Class ----------------------------------------------
//// Comparator for the weights of two edges
//class CompareWeight implements Comparator<Edge> {
//
//  @Override
//  // compares the weightValue of two edges
//  public int compare(Edge o1, Edge o2) {
//    return o1.weightValue - o2.weightValue;
//  }
//}
//
////--------------------------- Bespoke List Objects -----------------------------------------------
//
//interface ICollection<T> {
//  // adds an item to this
//  void add(T item);
//  // removes an item from this
//  T remove();
//  // returns the size of this 
//  int size();
//}
//
//// represents a queue
//class Queue<T> implements ICollection<T> {
//  Deque<T> items;
//
//  // standard constructor
//  Queue() {
//    this.items = new Deque<T>();
//  }
//
//  // adds an item to this Queue
//  public void add(T item) {
//    this.items.addAtTail(item);
//  }
//
//  // removes an item from this Queue
//  public T remove() {
//    return this.items.removeFromHead();
//  }
//
//  // returns the size of this Queue
//  public int size() {
//    return this.items.size();
//  }
//}
//
//// represents a one-way stack
//class Stack<T> implements ICollection<T> {
//  Deque<T> items;
//
//  Stack() {
//    this.items = new Deque<T>();
//  }
//
//  // Adds an item to a Stack
//  public void add(T item) {
//    this.items.addAtHead(item);
//  }
//
//  // Removes and item to a Stack
//  public T remove() {
//    return this.items.removeFromHead();
//  }
//
//  // Returns the size of this Stack
//  public int size() {
//    return this.items.size();
//  }
//}
//
////---------------------------- Graph Search Algos -----------------------------------------------
//
//// represents graph algos
//class SearchAlgo {
//  ArrayList<Vertex> searchPath = new ArrayList<Vertex>();
//
//  SearchAlgo() {
//    
//  }
//  
//  // Finds the path using a Stack
//  // Is an implementation of DFS
//  ArrayList<Vertex> pathDFS(Vertex from, Vertex to) {
//    return this.createPath(from, to, new Stack<Vertex>());
//  }
//
//  // Finds the path using a Queue
//  // Is an implementation of BFSD
//  ArrayList<Vertex> pathBFS(Vertex from, Vertex to) {
//    return this.createPath(from, to, new Queue<Vertex>());
//  }
//
//  // FInds the path using an ICollection
//  ArrayList<Vertex> createPath(Vertex from, Vertex to, ICollection<Vertex> worklist) {
//    ArrayList<Vertex> path = new ArrayList<Vertex>();
//
//    worklist.add(from);
//    while (worklist.size() > 0) {
//      Vertex next = worklist.remove();
//      if (next == to) {
//        return path;
//      }
//      else if (path.contains(next)) {
//        // Do nothing
//      }
//      else {
//        for (Edge e : next.borderList) {
//          worklist.add(e.from);
//          worklist.add(e.to);
//          if (path.contains(e.from)) {
//            next.previous = e.from;
//          }
//          else if (path.contains(e.to)) {
//            next.previous = e.to;
//          }
//        }
//        path.add(next);
//      }
//    }
//    return path;
//  }
//}
//
////  // creates the path from the start of the maze to the end
////  ArrayList<Vertex> makePath(Vertex start, Vertex end, ICollection <Vertex> list) {
////
////    list.add(start);
////    while (list.size() > 0) {
////      Vertex v = list.remove();
////      if (v == end) {
////        return this.searchPath;
////      }
////      else if (this.searchPath.contains(v)) {
////        // no action
////      }
////      else {
////        for (Edge edge : v.borderList) {
////          list.add(edge.from);
////          list.add(edge.to);
////          if (this.searchPath.contains(edge.from)) {
////            v.previous = edge.from;
////          }
////          else if (this.searchPath.contains(edge.to)) {
////            v.previous = edge.to;
////          }
////        }
////        this.searchPath.add(v);
////      }
////    }
////    return this.searchPath;
////  }
////}
//
////class Manual extends SearchAlgo {
////  // constructor for left arrow key
////  Manual(Vertex start, Vertex end) {
////    this.makePath(start, end, new Queue<Vertex>());
////  }
////}
////
////// class to represent a depth first search
////class DFS extends SearchAlgo {
////  // dfs constructor
////  DFS(Vertex start, Vertex end) {
////    this.makePath(start, end, new Stack<Vertex>());
////  }
////}
////
////// class to represent a breadth first search
////class BFS extends SearchAlgo {
////  // bfs constructor
////  BFS(Vertex start, Vertex end) {
////    this.makePath(start, end, new Queue<Vertex>());
////  }
////}
//
////--------------------------- Examples & Test Class ----------------------------------------------
////tests all elements of the maze game
//
////methods to test:
////makeScene()
////switchWallRight()
////switchWallBottom()
////makeBoard
////connectVertices
////makeEdges
////makeHashMap
////kruskalsAlgo
////union
////findVertex
//
//// all draw methods are tested by starting the game (last test)
//
//class ExamplesMaze {
//  GameWorld testGame; 
//  GameWorld testGame2;
//  GameWorld blankGame;
//  WorldScene testScene;
//  WorldScene testScene2; 
//
//  // void method to reset example objects for testing
//  void reset() {
//    testGame = new GameWorld(2, 2);
//    testScene = new WorldScene(0, 0);
//    blankGame = new GameWorld();
//  }
//
//  // manually constructed GameWorld for testing purposes
//  void manualGameWorld() {
//    testGame2 = new GameWorld();
//    testGame2.gameBoard = testGame2.makeBoard(2, 2);
//    testGame2.gameBoard.get(0).get(0).wallRight = false;
//    testGame2.gameBoard.get(0).get(1).wallRight = true;
//    testGame2.gameBoard.get(1).get(0).wallRight = true;
//    testGame2.gameBoard.get(1).get(1).wallRight = true;
//    testGame2.hashMap.put(testGame2.gameBoard.get(0).get(0), testGame2.gameBoard.get(0).get(0));
//    testGame2.hashMap.put(testGame2.gameBoard.get(0).get(1), testGame2.gameBoard.get(0).get(1));
//    testGame2.hashMap.put(testGame2.gameBoard.get(1).get(0), testGame2.gameBoard.get(1).get(0));
//    testGame2.hashMap.put(testGame2.gameBoard.get(1).get(1), testGame2.gameBoard.get(1).get(1));
//
//    testGame2.gameBoard.get(0).get(0).wallBottom = false;
//    testGame2.gameBoard.get(0).get(1).wallBottom = false;
//    testGame2.gameBoard.get(1).get(0).wallBottom = false;
//    testGame2.gameBoard.get(1).get(1).wallBottom = false;
//
//    testGame2.edgeList = new ArrayList<Edge>(Arrays.asList(
//        new Edge(new Vertex(new Posn(0, 0)), new Vertex(new Posn(1, 0)), 1),
//        new Edge(new Vertex(new Posn(0, 0)), new Vertex(new Posn(0, 1)), 2),
//        new Edge(new Vertex(new Posn(1, 0)), new Vertex(new Posn(1, 1)), 3),
//        new Edge(new Vertex(new Posn(0, 1)), new Vertex(new Posn(1, 1)), 4)));
//
//    testGame2.minSpanTree = new ArrayList<Edge>(Arrays.asList(
//        new Edge(new Vertex(new Posn(0, 0)), new Vertex(new Posn(1, 0)), 1),
//        new Edge(new Vertex(new Posn(0, 0)), new Vertex(new Posn(0, 1)), 2),
//        new Edge(new Vertex(new Posn(1, 0)), new Vertex(new Posn(1, 1)), 3)));
//
//    testGame2.player = new Player(testGame2.gameBoard.get(0).get(0));
//    testGame2.searchPath = new ArrayList<Vertex>();
//    testGame2.end = testGame2.gameBoard.get(1).get(1);
//  }
//
//  // method to contain repeated makeBoard(2,2) call on emptyGame
//  void remakeBoard() {
//    reset();
//    blankGame.gameBoard = blankGame.makeBoard(2, 2);
//  }
//
//  // testing that make scene is not doing anything
//  boolean testMakeScene(Tester t) {
//    reset();
//    manualGameWorld();
//    testGame2.makeScene();
//    return t.checkExpect(testGame2.makeScene(), testScene);
//  }
//
//  // testing that the right wallRight booleans are being flipped
//  void testSwitchWallRight(Tester t) {
//    manualGameWorld();
//    testGame2.switchWallRight(testGame2.gameBoard.get(0).get(0));
//    t.checkExpect(testGame2.gameBoard.get(0).get(0).wallRight, false);
//    testGame2.switchWallRight(testGame2.gameBoard.get(0).get(1));
//    t.checkExpect(testGame2.gameBoard.get(0).get(1).wallRight, true);
//  }
//
//  // testing that the correct wallBottom booleans are being flipped
//  void testSwitchWallBottom(Tester t) {
//    manualGameWorld();
//    testGame2.switchWallBottom(testGame2.gameBoard.get(0).get(0));
//    t.checkExpect(testGame2.gameBoard.get(0).get(0).wallRight, false);
//    testGame2.switchWallBottom(testGame2.gameBoard.get(0).get(1));
//    t.checkExpect(testGame2.gameBoard.get(0).get(1).wallRight, true);
//  }
//
//  // testing that a board is being made with the right amount and placement of vertices
//  void testMakeBoard(Tester t) {
//    reset();
//    t.checkExpect(blankGame.gameBoard, null);
//    remakeBoard();
//    // check that the board is no longer null
//    t.checkFail(blankGame.gameBoard, null);
//    // check that all the vertices are in the right place
//    t.checkExpect(blankGame.gameBoard.get(0).get(0).pos, new Posn(0,0));
//    t.checkExpect(blankGame.gameBoard.get(0).get(1).pos, new Posn(1,0));
//    t.checkExpect(blankGame.gameBoard.get(1).get(0).pos, new Posn(0,1));
//    t.checkExpect(blankGame.gameBoard.get(1).get(1).pos, new Posn(1,1));
//    // check that there are not extra vertices
//    t.checkExpect(blankGame.gameBoard.size(), 2);
//    t.checkExpect(blankGame.gameBoard.get(0).size(), 2);
//    t.checkExpect(blankGame.gameBoard.get(1).size(), 2);
//  }
//
//  // tests that vertices are connected via connectVertices() 
//  // when a gameWorld gameBoard is constructed
//  void testConnectVertices(Tester t) {
//    // check that makeBlank call alone is not enough to connect the vertices 
//    // (weird edge case in case someone incorrectly implemented this method)
//    reset();
//    t.checkExpect(blankGame.gameBoard, null);
//    remakeBoard();
//    t.checkExpect(blankGame.gameBoard.get(0).get(0).right, null);
//    // check that each vertice is connected correctly to its neighbors
//    reset();
//    t.checkExpect(testGame.gameBoard.get(0).get(0).right, testGame.gameBoard.get(0).get(1));
//    t.checkExpect(testGame.gameBoard.get(0).get(0).bottom, testGame.gameBoard.get(1).get(0));
//    t.checkExpect(testGame.gameBoard.get(0).get(1).left, testGame.gameBoard.get(0).get(0));
//    t.checkExpect(testGame.gameBoard.get(0).get(1).bottom, testGame.gameBoard.get(1).get(1));
//    t.checkExpect(testGame.gameBoard.get(1).get(0).top, testGame.gameBoard.get(0).get(0));
//    t.checkExpect(testGame.gameBoard.get(1).get(0).right, testGame.gameBoard.get(1).get(1));
//    t.checkExpect(testGame.gameBoard.get(1).get(1).top, testGame.gameBoard.get(0).get(1));
//    t.checkExpect(testGame.gameBoard.get(1).get(1).left, testGame.gameBoard.get(1).get(0));   
//  }
//
//  // test that makeEdges() returns a viable list of edges
//  void testMakeEdges(Tester t) {
//    // tests that makeEdges() did something
//    reset();
//    t.checkExpect(blankGame.edgeList, new ArrayList<Edge>());
//    remakeBoard();
//    t.checkFail(blankGame.edgeList, new ArrayList<Edge>());
//    // tests that makeEdges() made the right number of edges for the size of the game
//    t.checkExpect(blankGame.edgeList.size(), 4);
//  }
//
//  // tests that our hash maps are being made correctly
//  void testMakeHashMap(Tester t) {
//    reset();
//    // tests that blankGame started with an empty HashMap and did not finish that way
//    t.checkExpect(blankGame.hashMap, new HashMap<Vertex, Vertex>());
//    remakeBoard();
//    t.checkFail(blankGame.hashMap, new HashMap<Vertex, Vertex>());
//    // tests that makeHashMap() made the right number of edges for the size of the game
//    t.checkExpect(blankGame.hashMap.size(), 4);
//    // tests that all the vertex keys match the vertex hashes
//    t.checkExpect(blankGame.hashMap.get(blankGame.gameBoard.get(0).get(0)), 
//        blankGame.gameBoard.get(0).get(0));
//    t.checkExpect(blankGame.hashMap.get(blankGame.gameBoard.get(0).get(1)), 
//        blankGame.gameBoard.get(0).get(1));
//    t.checkExpect(blankGame.hashMap.get(blankGame.gameBoard.get(1).get(0)), 
//        blankGame.gameBoard.get(1).get(0));
//    t.checkExpect(blankGame.hashMap.get(blankGame.gameBoard.get(1).get(1)), 
//        blankGame.gameBoard.get(1).get(1));
//
//  }
//
//  // tests that our implementation of Kruskal's is working properly
//  void testKruskalsAlgo(Tester t) {
//    reset();
//    t.checkExpect(blankGame.minSpanTree, new ArrayList<Edge>());
//    // tests the tree contains the right number of edges
//    t.checkExpect(testGame.minSpanTree.size(), 3);
//  }
//
//  // tests the union method
//  void testUnion(Tester t) {
//    reset();
//    remakeBoard();
//    t.checkExpect(blankGame.hashMap.get(blankGame.gameBoard.get(0).get(0)), 
//        blankGame.gameBoard.get(0).get(0));
//    blankGame.union(blankGame.gameBoard.get(0).get(0), blankGame.gameBoard.get(1).get(1));
//    t.checkExpect(blankGame.hashMap.get(blankGame.gameBoard.get(0).get(0)), 
//        blankGame.gameBoard.get(1).get(1));
//    // test that unioning a vertex to itself does nothing
//    t.checkExpect(blankGame.hashMap.get(blankGame.gameBoard.get(0).get(1)), 
//        blankGame.gameBoard.get(0).get(1));
//  }
//
//  // test the findVertex method 
//  boolean testFindVertex(Tester t) {
//    reset();
//    remakeBoard();
//    // all vertices are hashed to themselves (as expected)
//    return t.checkExpect(blankGame.findVertex(blankGame.gameBoard.get(0).get(0)), 
//        blankGame.gameBoard.get(0).get(0))
//        && t.checkExpect(blankGame.findVertex(blankGame.gameBoard.get(0).get(1)), 
//            blankGame.gameBoard.get(0).get(1))
//        && t.checkExpect(blankGame.findVertex(blankGame.gameBoard.get(1).get(0)), 
//            blankGame.gameBoard.get(1).get(0))
//        && t.checkExpect(blankGame.findVertex(blankGame.gameBoard.get(1).get(1)), 
//            blankGame.gameBoard.get(1).get(1));
//  }
//
//  GameWorld startGame = new GameWorld(20, 20);
//
//  void testGame(Tester t) {
//    this.startGame.bigBang(500, 
//        500, 
//        GameWorld.TICK_RATE);
//  }
//}
