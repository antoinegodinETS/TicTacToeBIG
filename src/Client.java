import java.io.*;
import java.net.*;
import java.util.ArrayList;


class Client {
	public static void main(String[] args) {
         
        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[9][9];
        BigBoard bigBoard = new BigBoard();
        CPUPlayer cpuPlayer= new CPUPlayer(Mark.O);; //initie CPUPlayer
        
        try {
            MyClient = new Socket("localhost", 8888);

            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
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
                    bigBoard.play(lastMove, cpuPlayer.getOponentMark());
                    System.out.println("Valid moves list: ");
                    for (Move m : bigBoard.getValidMoves(lastMove.trim())) {
                        System.out.print(m + ", ");
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
                        char col = (char) ('A' + bestMove.getCol());
                        int row = 9- bestMove.getRow();
                        String move = "" + col + row;
                        System.out.println("Sending move: " + move);
                        bigBoard.play(move, cpuPlayer.getMark());
                        output.write(move.getBytes(), 0, move.length());
                        output.flush();
                    }
					System.out.println(bigBoard); //affiche le bigBoard
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