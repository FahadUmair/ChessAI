import java.util.*;

import static java.lang.Double.max;

public class Chess {
    char[] Board;
    List<String> possibleMovesOfSelectedPiece;
    Boolean check;
    PieceWeights pieceWeights;
    int depth;
    List<Character> theWhitePieces;
    List<Character> theBlackPieces;
    List<Moves> whiteMovesValid;

    public Chess(){
        pieceWeights=new PieceWeights();
        Board=new char[120];
        initializeBoard();
        depth=3;
        specifyPieces();
        userInput();



        //print some empty lines
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

    }

    //notice that the fen string's is at the opposite side. i.e. last row is at the first and so on
    private void initializeBoard(){
        String startingFen="RNBQKBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbqkbnr w KQkq - 0 1";
        fenToBoard(startingFen);
    }

    private void specifyPieces(){
        theWhitePieces=new ArrayList<>();
        theBlackPieces=new ArrayList<>();

        theWhitePieces.add('R');theWhitePieces.add('N');theWhitePieces.add('B');
        theWhitePieces.add('Q');theWhitePieces.add('K');theWhitePieces.add('P');

        theBlackPieces.add('r');theBlackPieces.add('n');theBlackPieces.add('b');
        theBlackPieces.add('q');theBlackPieces.add('k');theBlackPieces.add('p');
    }

    //this will be the main working of our program as it deals with the user
    private void userInput(){
        check=false;
        Scanner scanner=new Scanner(System.in);
        System.out.println();
        System.out.println("White is CAPITAL, and black is lowercase for both versions");


        System.out.print("Type 'two' for two players, and type 'ai' to play vs the computer (don't type the apostrophe) : ");
        String whichGame=scanner.next();
        whichGame=whichGame.toLowerCase();
        while(!whichGame.equals("two")&& !whichGame.equals("ai")){
            System.out.print("Type again, only type two or ai: ");
            whichGame=scanner.next();
        }
        if(whichGame.equals("two")){twoPlayer(scanner);}
        else{
            System.out.print("Specify depth (works good at 3 or above): ");
            while(!scanner.hasNextInt()){
                System.out.println("Please give an integer: ");
                scanner.nextLine();
            }
            depth = scanner.nextInt();
            playerVsAi(scanner);}


    }

    //player vs ai
    private void playerVsAi(Scanner scanner){
        String colour="white";
        int counter=1;
        printBoard();
        System.out.println("Specify a piece like a2 or h7, etc");

        while(true){
            Piece piece = new Piece(Board);
            System.out.println(colour+"'s turn");
            if(check){
                System.out.println("CHECK");
                check=false;
            }
            System.out.print("Select piece to move: ");
            String input = scanner.next();
            input = input.toLowerCase();
            while (!correctInput(input)|| userSelectedWrongColour(colour,input,piece)) {
                System.out.print("Please provide correct input (check colour and point eg. a2): ");
                input = scanner.next();
                input = input.toLowerCase();
            }

            List<Moves> moves=piece.getMove(getPieceAtSelected(input), colour);
            while(moves.size()==0 || userSelectedWrongColour(colour,input,piece)){
                System.out.print("No possible moves for this piece, select another: ");
                input = scanner.next();
                input = input.toLowerCase();
                while (!correctInput(input)) {
                    System.out.print("Please provide correct input (eg. a2): ");
                    input = scanner.next();
                    input = input.toLowerCase();
                }
                moves=piece.getMove(getPieceAtSelected(input), colour);


            }
            legalMovesOfPiece(moves);
            ourKingInCheck(colour,moves);

            while(possibleMovesOfSelectedPiece.size()==0 || userSelectedWrongColour(colour,input,piece)){
                System.out.print("No possible moves for this piece, select another: ");
                input = scanner.next();
                input = input.toLowerCase();
                while (!correctInput(input)) {
                    System.out.print("Please provide correct input (eg. a2): ");
                    input = scanner.next();
                    input = input.toLowerCase();
                }
                moves=piece.getMove(getPieceAtSelected(input), colour);
                legalMovesOfPiece(moves);
                ourKingInCheck(colour,moves);

            }
            System.out.print("Possible moves: ");
            possibleMovesOfSelectedPiece.forEach(value -> System.out.print(value+ " "));
            System.out.println();
            System.out.print("Where do you want to move this piece: ");
            input = scanner.next();
            input = input.toLowerCase();
            while (!possibleMovesOfSelectedPiece.contains(input)){
                System.out.print("Invalid move, select another: ");
                input = scanner.next();
                input = input.toLowerCase();
            }


            movePiece(input,moves,Board);

            //convert pawns to queens, because that is the best option
            pawnToQueen();


            checkForCheck(colour,piece);
            if(checkForCheckmate(colour,piece)){
                System.out.println();
                if(check){
                    System.out.println("CHECKMATE");
                    System.out.println(colour+" wins the game!");
                }else{
                    System.out.println("STALEMATE");
                    System.out.println("The game ends in a draw");
                }

                return;
            }


            counter++;
            printBoard();

            //now it is the ai's turn
            System.out.println("It is the AI's turn now");
            colour="black";

            ScoreAndMove aiMove=minimax(piece,depth,Double.MIN_VALUE,Double.MAX_VALUE,true);
            int someStart=aiMove.start;
            int someTarget=aiMove.target;
            char tempChar=Board[someStart];
            Board[someStart]='-';
            Board[someTarget]=tempChar;
            pawnToQueen(); //check if pawn transforms to Queen
            check=false;
            checkForCheck(colour,piece);
            checkForCheckmate(colour,piece);
            legalMovesOfWhiteWithAI(piece);

            if(whiteMovesValid.size()==0){
                printBoard();
                System.out.println();
                if(check){
                    System.out.println("CHECKMATE");
                    System.out.println(colour+" wins the game!");
                    return;
                }else{
                    System.out.println("STALEMATE");
                    System.out.println("The game ends in a draw");
                    return;
                }


            }

            colour="white";
            printBoard();
        }
    }

    private void legalMovesOfWhiteWithAI(Piece piece){
        whiteMovesValid=new ArrayList<>();
        for (int i = 21; i < 99; i++) {
            if(theWhitePieces.contains(Board[i])){
                List<Moves> moves=piece.getMove(i, "white");
                legalMovesOfPiece(moves);
                ourKingInCheck("white",moves);
                whiteMovesValid.addAll(moves);
            }
        }

    }




    private ScoreAndMove minimax (Piece piece,int depth,double alpha,double beta,boolean maximizingPlayer){
        if (depth==0){
            return new ScoreAndMove(score(),0,0);
        }
        ScoreAndMove theBestMove = new ScoreAndMove(0,0,0);

        if(maximizingPlayer){
            piece.checkAllBlackMoves(Board,true);
            List<PieceAndItsMove> blackPieceMove=piece.blackPieceAndItsMoves;
            double maxScore=Double.MIN_VALUE;
            for (int i = 0; i < blackPieceMove.size(); i++) {
                // make a move
                char tempStarting = Board[blackPieceMove.get(i).start];
                char tempTarget = Board[blackPieceMove.get(i).target];
                Board[blackPieceMove.get(i).start] = '-';
                Board[blackPieceMove.get(i).target] = tempStarting;
                pawnToQueen(); //check if pawn transforms to Queen


                ScoreAndMove tempScore = minimax(piece, depth - 1, alpha, beta, false);

                //un make a move
                Board[blackPieceMove.get(i).start] = tempStarting;
                Board[blackPieceMove.get(i).target] = tempTarget;

                if (tempScore.score > maxScore) {
                    maxScore = tempScore.score;
                    int aStart = blackPieceMove.get(i).start;
                    int aTarget = blackPieceMove.get(i).target;
                    theBestMove = new ScoreAndMove(maxScore, aStart, aTarget);
                }
                alpha = max(alpha, tempScore.score);
                if (beta <= alpha) break;

            }
                return theBestMove;

        }else{
            piece.checkAllWhiteMoves(Board,true);
            List<PieceAndItsMove> whitePieceMove=piece.whitePieceAndItsMoves;
            double minScore=Double.MAX_VALUE;
            for (int i = 0; i < whitePieceMove.size(); i++) {

                //make a move
                char tempStarting = Board[whitePieceMove.get(i).start];
                char tempTarget = Board[whitePieceMove.get(i).target];
                Board[whitePieceMove.get(i).start] = '-';
                Board[whitePieceMove.get(i).target] = tempStarting;
                pawnToQueen(); //check if pawn transforms to Queen

                ScoreAndMove tempScore = minimax(piece, depth - 1, alpha, beta, true);
                Board[whitePieceMove.get(i).start] = tempStarting;
                Board[whitePieceMove.get(i).target] = tempTarget;

                if(tempScore.score<minScore){
                    minScore=tempScore.score;
                    int aStart = whitePieceMove.get(i).start;
                    int aTarget = whitePieceMove.get(i).target;
                    theBestMove = new ScoreAndMove(minScore, aStart, aTarget);
                }
                beta=Double.min(beta,tempScore.score);
                if(beta<=alpha)break;

            }
            return theBestMove;
        }


    }

    private double score(){
        int surroundingWhiteKing=0,surroundingBlackKing=0;
        double totalWhiteScore=0,whitePieceScore=0,whiteQueens=0,whiteKnightScores=0,whitePawnScore=0;
        double totalBlackScore=0,blackPieceScore=0,blackQueens=0,blackKnightScores=0,blackPawnScore=0;


        for (int i = 21; i < 99; i++) {
            if(Board[i]!='-'){
                char thePiece=Board[i];
                double tempWeightOfPiece=pieceWeights.getWeights(thePiece);
                //check if piece is white
                if(theWhitePieces.contains(thePiece)){
                    whitePieceScore+=tempWeightOfPiece;
                    if(thePiece=='Q'){//check how many queens white has
                        whiteQueens+=1;
                    }else if(thePiece=='K'){//check how many pieces surround the king
                        int index=i+9;
                        for (int j = index; j < index+4; j++) {
                            if(Board[j]!='-'){if(theWhitePieces.contains(Board[j]))surroundingWhiteKing+=1;}
                        }
                        if(Board[i-1]!='-'){if(theWhitePieces.contains(Board[i-1]))surroundingWhiteKing+=1;}
                        if(Board[i+1]!='-'){if(theWhitePieces.contains(Board[i+1]))surroundingWhiteKing+=1;}
                        index=i-11;
                        for (int j = index; j < index+4; j++) {
                            if(Board[j]!='-'){if(theWhitePieces.contains(Board[j]))surroundingWhiteKing+=1;}
                        }
                    }else if(thePiece=='N'){
                        whiteKnightScores+=pieceWeights.getKnightWeight(i);
                    }
                    else if(thePiece=='P'){
                        whitePawnScore+=pieceWeights.getWhitePawnWeight(i);
                    }
                }
                //else the piece is black
                else{
                    blackPieceScore+=tempWeightOfPiece;
                    if(thePiece=='q'){//check how many queens white has
                        blackQueens+=1;
                    }else if(thePiece=='k'){//check how many pieces surround the king
                        int index=i+9;
                        for (int j = index; j < index+4; j++) {
                            if(Board[j]!='-'){if(theBlackPieces.contains(Board[j]))surroundingBlackKing+=1;}
                        }
                        if(Board[i-1]!='-'){if(theBlackPieces.contains(Board[i-1]))surroundingBlackKing+=1;}
                        if(Board[i+1]!='-'){if(theBlackPieces.contains(Board[i+1]))surroundingBlackKing+=1;}
                        index=i-11;
                        for (int j = index; j < index+4; j++) {
                            if(Board[j]!='-'){if(theBlackPieces.contains(Board[j]))surroundingBlackKing+=1;}
                        }
                    }else if(thePiece=='n'){
                        blackKnightScores+=pieceWeights.getKnightWeight(i);
                    }
                    else if(thePiece=='p'){
                        blackPawnScore+=pieceWeights.getBlackPawnWeight(i);
                    }
                }
            }
        }
        totalWhiteScore=(14*whitePieceScore) +(4*whiteQueens)+(0.05*surroundingWhiteKing)+(1.5*whiteKnightScores)
                + (0.3*whitePawnScore);
        totalBlackScore=(14*blackPieceScore) +(4*blackQueens)+(0.05*surroundingBlackKing)+(1.5*blackKnightScores)
                + (0.3*blackPawnScore);
        double finalScore=totalBlackScore-totalWhiteScore;
        return finalScore;

    }



    //two player game
    private void twoPlayer(Scanner scanner){

        String colour="white";
        int counter=1;
        printBoard();
        System.out.println("Specify a piece like a2 or h7, etc");

        while(true){
            Piece piece = new Piece(Board);
            System.out.println(colour+"'s turn");
            if(check){
                System.out.println("CHECK");
                check=false;
            }
            System.out.print("Select piece to move: ");
            String input = scanner.next();
            input = input.toLowerCase();
            while (!correctInput(input)|| userSelectedWrongColour(colour,input,piece)) {
                System.out.print("Please provide correct input (check colour and point eg. a2): ");
                input = scanner.next();
                input = input.toLowerCase();
            }






            List<Moves> moves=piece.getMove(getPieceAtSelected(input), colour);
            while(moves.size()==0 || userSelectedWrongColour(colour,input,piece)){
                System.out.print("No possible moves for this piece, select another: ");
                input = scanner.next();
                input = input.toLowerCase();
                while (!correctInput(input)) {
                    System.out.print("Please provide correct input (eg. a2): ");
                    input = scanner.next();
                    input = input.toLowerCase();
                }
                moves=piece.getMove(getPieceAtSelected(input), colour);


            }
            legalMovesOfPiece(moves);
            ourKingInCheck(colour,moves);

            while(possibleMovesOfSelectedPiece.size()==0 || userSelectedWrongColour(colour,input,piece)){
                System.out.print("No possible moves for this piece, select another: ");
                input = scanner.next();
                input = input.toLowerCase();
                while (!correctInput(input)) {
                    System.out.print("Please provide correct input (eg. a2): ");
                    input = scanner.next();
                    input = input.toLowerCase();
                }
                moves=piece.getMove(getPieceAtSelected(input), colour);
                legalMovesOfPiece(moves);
                ourKingInCheck(colour,moves);

            }
            System.out.print("Possible moves: ");
            possibleMovesOfSelectedPiece.forEach(value -> System.out.print(value+ " "));
            System.out.println();
            System.out.print("Where do you want to move this piece: ");
            input = scanner.next();
            input = input.toLowerCase();
            while (!possibleMovesOfSelectedPiece.contains(input)){
                System.out.print("Invalid move, select another: ");
                input = scanner.next();
                input = input.toLowerCase();
            }


            movePiece(input,moves,Board);

            //convert pawns to queens, because that is the best option
            pawnToQueen();

            checkForCheck(colour,piece);
            if(checkForCheckmate(colour,piece)){
                System.out.println();
                if(check){
                    System.out.println("CHECKMATE");
                    System.out.println(colour+" wins the game!");
                }else{
                    System.out.println("STALEMATE");
                    System.out.println("The game ends in a draw");
                }

                return;
            }


            counter++;
            if(counter%2==0)colour="black";
            else colour="white";

            printBoard();
        }
    }



    //the best choice is converting a pawn into a queen
    private void pawnToQueen() {
        //check if white pawn exists at black's starting
        for(int i=91;i<99;i++){
            if(Board[i]=='P'){Board[i]='Q';}
        }

        //check if black pawn exists at white's starting
        for (int i = 21; i < 29; i++) {
            if(Board[i]=='p'){Board[i]='q';}
        }
    }

    //game ends over here
    private boolean checkForCheckmate(String aColour, Piece piece) {
        //check if white is under checkmate
        validMovesProtectingKing(aColour,piece);
        if(aColour.equals("black")) {
            if (piece.whitePieceAndItsMoves.size() == 0) return true;
            else {
                return false;
            }
        }
        else{//check if black is under checkmate
            if(piece.blackPieceAndItsMoves.size()==0)return true;
            else{
            return false;
        }
        }


    }

    //these are all valid moves that could be made
    private void validMovesProtectingKing(String aColour,Piece piece) {
        if(aColour.equals("black"))
        piece.checkAllWhiteMoves(Board,true);
        else
        piece.checkAllBlackMoves(Board,true);

    }

    //check if opposition king is under check
    private void checkForCheck(String aColour,Piece piece) {
        if(aColour.equals("white")){
            piece.checkAllWhiteMoves(Board,false);
            if(piece.blackIsUnderCheck)check=true;
        }else{
            piece.checkAllBlackMoves(Board,false);
            if(piece.whiteIsUnderCheck)check=true;
        }
    }

    //check if user selected correct colour
    private boolean userSelectedWrongColour(String colour, String input, Piece piece) {
        int position=getPieceAtSelected(input);
        char temp=Board[position];
        if(colour.equals("white")){
            if(piece.whitePieces.contains(temp))return false;
        }
        else{
            if(piece.blackPieces.contains(temp))return false;
        }

        return true;
    }

    //display the moves of the selected piece
    private void legalMovesOfPiece(List<Moves> movesList){
        possibleMovesOfSelectedPiece=new ArrayList<>();
        for (int i = 0; i < movesList.size(); i++) {
            String aNum=String.valueOf(movesList.get(i).target);
            char[] numArray=aNum.toCharArray();
            String result=indexToCoordinate(numArray);
//            System.out.print(result+" ");
            possibleMovesOfSelectedPiece.add(result);
        }
//        System.out.println();
    }





    //check if we put our king into check
    private void ourKingInCheck(String aColour,List<Moves> movesList){
        int i=0;
        while(i!=possibleMovesOfSelectedPiece.size()){

            char[] tempBoard=new char[120];
            for (int j = 0; j < 120; j++) {
                tempBoard[j]=Board[j];
            }

            movePiece(possibleMovesOfSelectedPiece.get(i),movesList,tempBoard);
            Piece aPiece=new Piece(tempBoard);
            if(aColour.equals("white")){
                aPiece.checkAllBlackMoves(tempBoard,false);
                if(aPiece.whiteIsUnderCheck){
                    possibleMovesOfSelectedPiece.remove(i);
                    movesList.remove(i);
                }else{i++;}
            }else{
                aPiece.checkAllWhiteMoves(tempBoard,false);
                if(aPiece.blackIsUnderCheck) {
                    possibleMovesOfSelectedPiece.remove(i);
                    movesList.remove(i);
                }else{i++;}
            }
        }
    }


    //if we give a value like 21, we should have 'a1' back
    private String indexToCoordinate(char[] anArray){
        String dig1=switch (anArray[1]){
            case '1' -> "a";
            case '2' -> "b";
            case '3' -> "c";
            case '4' -> "d";
            case '5' -> "e";
            case '6' -> "f";
            case '7' -> "g";
            default -> "h";
        };
        int aDig=Character.getNumericValue(anArray[0]);
        aDig--;
        String dig2=String.valueOf(aDig);
        return dig1+dig2;
    }


    //what piece exists at the user's selected point
    private int getPieceAtSelected(String point){
        char[] points=point.toCharArray();

        int aNum=Character.getNumericValue(points[1]);
        aNum++;
        String num1= String.valueOf(aNum);
        String num2 = switch (points[0]) {
            case 'a' -> "1";
            case 'b' -> "2";
            case 'c' -> "3";
            case 'd' -> "4";
            case 'e' -> "5";
            case 'f' -> "6";
            case 'g' -> "7";
            case 'h' -> "8";
            default -> null;
        };
        String s=num1+num2;
        return Integer.parseInt(s);
    }


    //move piece to new position of piece on the board
    private void movePiece(String position,List<Moves> movesList, char[] aBoard){
        int indexInArray=possibleMovesOfSelectedPiece.indexOf(position);
        int startingPoint=movesList.get(indexInArray).start;
        int targetPoint=movesList.get(indexInArray).target;
        char temp=aBoard[startingPoint];
        aBoard[startingPoint]='-';
        aBoard[targetPoint]=temp;

    }



    //check if user provided the correct input
    private boolean correctInput(String input) {
        List<String> correctInputs=new ArrayList<>();
        for(char alphabet='a';alphabet<='h';alphabet++){
            for (int i = 1; i <=8 ; i++) {
                correctInputs.add(alphabet+Integer.toString(i));
            }
        }
        return correctInputs.contains(input);

    }


    private void fenToBoard(String aFen){
        String[] fenArray=aFen.split(" ");
        char[] placement=fenArray[0].toCharArray();
        //use first array to place pieces on board
        placePiecesOnBoard(placement);

    }

    private void placePiecesOnBoard(char[] anArray){
        int boardPosition=21;
        int startingElementOfRow=21;
        for (int i = 0; i < anArray.length; i++) {

            //check if char is '/'
            if(anArray[i]=='/'){
                startingElementOfRow+=10;
                boardPosition=startingElementOfRow;
                continue;
            }
            //check if we encounter a number
            else if(Character.isDigit(anArray[i])){
                int num=Character.getNumericValue(anArray[i]);
                for (int j = 1; j <= num; j++) {
                    Board[boardPosition]='-';
                    boardPosition++;
                }
            }
            //place the piece on the board
            else{
                Board[boardPosition]=anArray[i];
                boardPosition +=1;
            }

        }
    }

    private void printBoard(){
        System.out.println();
        int sideNum=8;
        System.out.print(sideNum+" |  ");
        sideNum--;
        for (int i = 9; i>=2 ; i--) {
            String s1 = Integer.toString(i);


            for (int j = 1; j <= 8; j++) {
                String s2 = Integer.toString(j);
                String s=s1+s2;
                int index=Integer.parseInt(s);
                System.out.print(Board[index]+  "  ");

            }
            System.out.println();
            if(sideNum!=0) {
                System.out.print(sideNum + " |  ");
                sideNum--;
            }
        }
        char alphabet;
        System.out.print("   __");
        for (int i = 0; i < 8; i++) {
            if(i!=7)System.out.print("___");
            else{System.out.print("_");}
        }
        System.out.println();
        System.out.print("     ");
        for(alphabet='a';alphabet<='h';alphabet++){
            System.out.print(alphabet+"  ");
        }
        System.out.println();
        System.out.println();
    }













    public static void main(String[] args) {
        new Chess();
    }
}
