import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;


class Client {
	public static void main(String[] args) {
         
        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[9][9];
        BigBoard bigBoard = new BigBoard();
        CPUPlayer cpuPlayer= new CPUPlayer(Mark.O); //initie CPUPlayer
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        String ipAddress = "127.0.0.1"; // Default IP
        int port = 8888; // Default port
        
        try {
            // Process command-line arguments
            if (args.length > 0) {
                // First argument is IP address
                ipAddress = args[0];

                // Second argument is port (if provided)
                if (args.length > 1) {
                    try {
                        port = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid port number, using default (8888)");
                    }
                }
                System.out.println("Using IP: " + ipAddress + ", port: " + port);
            } else {
                // If no command-line args, use console input
                System.out.println("Choose connection type:");
                System.out.println("1. Localhost (127.0.0.1)");
                System.out.println("2. Custom IP address");
                System.out.print("Enter your choice (1-2): ");

                String choice = console.readLine().trim();

                switch (choice) {
                    case "1":
                        ipAddress = "127.0.0.1";
                        break;
                    case "2":
                        System.out.print("Enter custom IP address: ");
                        ipAddress = console.readLine().trim();
                        System.out.print("Enter port (default 8888): ");
                        String portInput = console.readLine().trim();
                        if (!portInput.isEmpty()) {
                            try {
                                port = Integer.parseInt(portInput);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid port number, using default (8888)");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid choice, using default server (127.0.0.1)");
                }
            }

            System.out.println("Connecting to " + ipAddress + ":" + port + "...");
            MyClient = new Socket(ipAddress, port);
            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
//            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(1 == 1){
                char cmd = 0;
                
                cmd = (char)input.read();
                System.out.println(cmd);
                // Debut de la partie en joueur blanc
                if(cmd == '1'){
					cpuPlayer = new CPUPlayer(Mark.X);
                    byte[] aBuffer = new byte[1024];
                    
					int size = input.available();
					//System.out.println("size " + size);
					input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    /*String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 9){
                            x = 0;
                            y++;
                        }
                    }*/

                    System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
                    
					String move = "D6";
					bigBoard.play(move, Mark.X);
					output.write(move.getBytes(), 0, move.length());
                    output.flush();
					System.out.println(bigBoard); //affiche le bigBoard
					
					/*String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();*/
                }
                // Debut de la partie en joueur Noir
                if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    byte[] aBuffer = new byte[1024];
                    
                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
					System.out.println(bigBoard); //affiche le bigBoard
                    /*String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 9){
                            x = 0;
                            y++;
                        }
                    } */
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
//            if(cmd == '3'){
//                byte[] aBuffer = new byte[16];
//
//                int size = input.available();
//                System.out.println("size :" + size);
//                input.read(aBuffer,0,size);
//
//                String lastMove = new String(aBuffer).trim();
//                System.out.println("Dernier coup :"+ lastMove);
//                bigBoard.play(lastMove, Mark.X);
//                System.out.println("Valid moves list: ");
//                for (Move m : bigBoard.getValidMoves(lastMove.trim())) {
//                    System.out.print(m + ", ");
//                }
//                System.out.println();
//
//                ArrayList<Move> bestMoves = cpuPlayer.getNextMoveAB(bigBoard, lastMove);
//                System.out.println("Number of explored nodes: " + cpuPlayer.getNumOfExploredNodes());
//                System.out.println("Suggested moves by alphaBeta: ");
//                for (Move m : bestMoves) {
//                    System.out.print(m+ ", ");
//                }
//
//                System.out.println();
//
//                System.out.println("Entrez votre coup : ");
//                String move = null;
//                move = console.readLine();
//                bigBoard.play(move.trim(), Mark.O);
//                output.write(move.getBytes(),0,move.length());
//                output.flush();
//
//            }
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    System.out.println("size :" + size);
                    input.read(aBuffer,0,size);

                    String lastMove = new String(aBuffer).trim();
                    System.out.println("Dernier coup :"+ lastMove);
                    bigBoard.play(lastMove, cpuPlayer.getOpponentMark());
                    System.out.println("Valid moves list: ");
                    Iterator<Move> iterator = bigBoard.getValidMoves(lastMove.trim()).iterator();
                    while (iterator.hasNext()) {
                        System.out.print(iterator.next());
                        if (iterator.hasNext()) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                    ArrayList<Move> bestMoves = cpuPlayer.getNextMoveAB(bigBoard, lastMove);
                    System.out.println("Number of explored nodes: " + cpuPlayer.getNumOfExploredNodes());
                    System.out.println("Suggested moves by alphaBeta: ");
                    for (Move m : bestMoves) {
                        System.out.print(m + " (score: " + m.getScore() + "), ");
                    }
                    System.out.println();

                    if (!bestMoves.isEmpty()) {
                        Move bestMove = bestMoves.get(0);
                        System.out.println("Sending move: " + bestMove.toString());
                        bigBoard.play(bestMove.toString(), cpuPlayer.getMark());
                        cpuPlayer.incrementMoveCount();
                        output.write(bestMove.toString().getBytes(), 0, bestMove.toString().length());
                        output.flush();
                    }

//                    System.out.println(bigBoard);

                }
                // Le dernier coup est invalide
            if(cmd == '4'){
                System.out.println("Coup invalide, entrez un nouveau coup : ");
                String move = null;
                move = console.readLine();
                output.write(move.getBytes(),0,move.length());
                output.flush();

            }
                // La partie est terminée
            if(cmd == '5'){
                byte[] aBuffer = new byte[16];
                int size = input.available();
                input.read(aBuffer,0,size);
                String s = new String(aBuffer);
                System.out.println("Partie Terminé. Le dernier coup joué est: "+s);
                // Print the total number of moves made by the CPU player
                System.out.println("Nombre total de coups joués: " + (cpuPlayer.getMoveCount()));
                String move = null;
                move = console.readLine();
                output.write(move.getBytes(),0,move.length());
                output.flush();
                    
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
	
    }
}