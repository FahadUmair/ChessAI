import java.util.ArrayList;
import java.util.List;

public class Piece {
    List<Integer> offLimitNums;
    List<Character> whitePieces;
    List<Character> blackPieces;
    List<Integer> whitePawnStartingPosition;
    List<Integer> blackPawnStartingPosition;
    List<PieceAndItsMove> whitePieceAndItsMoves;
    List<PieceAndItsMove> blackPieceAndItsMoves;
    char[] aBoard;
    boolean blackIsUnderCheck;
    boolean whiteIsUnderCheck;

    public Piece(char[] aBoard){
        offLimitNumbers();
        setPieces();
        this.aBoard=aBoard;
    }

    private void setPieces() {
        whitePieces=new ArrayList<>();
        blackPieces=new ArrayList<>();
        whitePawnStartingPosition=new ArrayList<>();
        blackPawnStartingPosition=new ArrayList<>();
        whitePieces.add('R');whitePieces.add('N');whitePieces.add('B');
        whitePieces.add('Q');whitePieces.add('K');whitePieces.add('P');

        blackPieces.add('r');blackPieces.add('n');blackPieces.add('b');
        blackPieces.add('q');blackPieces.add('k');blackPieces.add('p');

        //let's predetermine the starting positions for both the white and black pawns
        for (int i = 31; i < 39; i++) {
            whitePawnStartingPosition.add(i);
        }
        for (int i = 81; i < 89; i++) {
            blackPawnStartingPosition.add(i);
        }

    }


    //check all white pieces for their moves for a colour
    public void checkAllWhiteMoves(char[] tempBoard,boolean addToArray){
        whitePieceAndItsMoves=new ArrayList<>();
        List<Moves> allWhiteMoves=new ArrayList<>();
        blackIsUnderCheck=false;
        for (int i = 21; i < 99; i++) {
            if(tempBoard[i]!='-'){ //we encounter a white piece
                if(whitePieces.contains(tempBoard[i])){
                    allWhiteMoves=getMove(i,"white");
                    for (int j = 0; j < allWhiteMoves.size(); j++) {
                        int aTarget=allWhiteMoves.get(j).target;
                        if(tempBoard[aTarget]=='k') {
                            blackIsUnderCheck = true;
                        }
                        if(addToArray){
                            if (islegalMove(allWhiteMoves.get(j).start, allWhiteMoves.get(j).target, "white"))
                                whitePieceAndItsMoves.add(new PieceAndItsMove(tempBoard[i], i, aTarget));
                        }
                    }
                }
            }
        }

    }

    //check if move legal
    private boolean islegalMove(int start,int target,String colour){
        char[] tempBoard=new char[120];
        for (int j = 0; j < 120; j++) {
            tempBoard[j]=aBoard[j];
        }

        char temp=tempBoard[start];
        tempBoard[start]='-';
        tempBoard[target]=temp;
        if(colour.equals("white")){
            checkAllBlackMoves(tempBoard,false);
            if(whiteIsUnderCheck){return false;}
            else{ return true;}
        }else{
            checkAllWhiteMoves(tempBoard,false);
            if(blackIsUnderCheck) {
                    return false;
            }
            else {
                return true;
            }
        }


    }


    //check all black pieces for their moves for a colour
    public void checkAllBlackMoves(char[] tempBoard,boolean addToArray){
        blackPieceAndItsMoves=new ArrayList<>();
        List<Moves> allBlackMoves=new ArrayList<>();
        whiteIsUnderCheck=false;
        for (int i = 21; i < 99; i++) {
            if(tempBoard[i]!='-'){ //we encounter a white piece
                if(blackPieces.contains(tempBoard[i])){
                    allBlackMoves = getMove(i, "black");
                    for (int j = 0; j < allBlackMoves.size(); j++) {
                        int aTarget = allBlackMoves.get(j).target;
                        if (tempBoard[aTarget] == 'K') {
                            whiteIsUnderCheck = true;

                        }
                        if(addToArray){
                            if (islegalMove(allBlackMoves.get(j).start, allBlackMoves.get(j).target, "black"))
                                blackPieceAndItsMoves.add(new PieceAndItsMove(tempBoard[i], i, aTarget));
                        }
                    }
                }

            }
        }

    }


    public List<Moves> getMove(int index,String colour){
        List<Moves> moves=new ArrayList<>();
        char piece=aBoard[index];
        if(piece=='R' || piece=='r'){
            moves=searchUpwards(index, colour);
            moves.addAll(searchDownwards(index, colour));
            moves.addAll(searchLeft(index, colour));
            moves.addAll(searchRight(index, colour));
            return moves;
        }
        else if(piece=='N' || piece=='n'){
            moves=moveKnight(index,colour);
            return moves;
        }
        else if(piece=='B' || piece=='b'){
            moves=searchLeftUpwards(index, colour);
            moves.addAll(searchRightUpwards(index, colour));
            moves.addAll(searchLeftDownwards(index, colour));
            moves.addAll(searchRightDownwards(index, colour));
            return moves;
        }
        else if(piece=='Q' || piece=='q'){
            moves=searchUpwards(index, colour);
            moves.addAll(searchDownwards(index, colour));
            moves.addAll(searchLeft(index, colour));
            moves.addAll(searchRight(index, colour));
            moves.addAll(searchLeftUpwards(index, colour));
            moves.addAll(searchRightUpwards(index, colour));
            moves.addAll(searchLeftDownwards(index, colour));
            moves.addAll(searchRightDownwards(index, colour));
            return moves;
        }
        else if(piece=='K' || piece=='k'){
            moves=moveKing(index, colour);
            return moves;
        }
        else if(piece=='P' || piece=='p'){
            moves=movePawn(index,colour);
            return moves;
        }
        return moves;
    }

    //moving the king
    private List<Moves> moveKing(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int[] offsets={-11,-10,-9,-1,1,9,10,11};
        for (int i = 0; i < offsets.length; i++) {
            int newPosition=offsets[i]+index;
            if(!existsInOffLimitNumbers(newPosition)){
                //there is a piece here
                if(aBoard[newPosition]!='-'){
                    moves.addAll(positionTaken(index,newPosition,ourColour));
                }
                else{
                    moves.add(new Moves(index,newPosition));
                }
            }

        }
        return moves;
    }


    private List<Moves> movePawn(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int whiteNum=index;
        whiteNum += 10;

        int blackNum=index;
        blackNum -= 10;

        if(ourColour.equals("white")) {
            if (!existsInOffLimitNumbers(whiteNum)) {
                    if (aBoard[whiteNum] == '-'){
                        moves.add(new Moves(index,whiteNum));
                        //if it is the first move,then it can move 2 ahead
                        int secondNum = whiteNum+10;
                        if (whitePawnStartingPosition.contains(index) && !existsInOffLimitNumbers(secondNum)) {
                            if (aBoard[secondNum] == '-') moves.add(new Moves(index,secondNum));
                        }
                    }

                    if(blackPieces.contains(aBoard[index+9]))moves.add(new Moves(index,index+9));
                    if(blackPieces.contains(aBoard[index+11]))moves.add(new Moves(index,index+11));
            }
            return moves;
        }else{ //we have a black pawn
            if (!existsInOffLimitNumbers(blackNum)) {
                if (aBoard[blackNum] == '-') {
                    moves.add(new Moves(index, blackNum));
                    //if it is the first move,then it can move 2 ahead
                    int secondNum = blackNum - 10;
                    if (blackPawnStartingPosition.contains(index) && !existsInOffLimitNumbers(secondNum)) {
                        if (aBoard[secondNum] == '-') moves.add(new Moves(index, secondNum));
                    }
                }
                if(whitePieces.contains(aBoard[index-9]))moves.add(new Moves(index,index-9));
                if(whitePieces.contains(aBoard[index-11]))moves.add(new Moves(index,index-11));
            }
            return moves;
        }
    }

    //moving a knight
    private List<Moves> moveKnight(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int[] offsets={-21,-19,-12,-8,8,12,19,21};
        for (int i = 0; i < offsets.length; i++) {
            int newPosition=offsets[i]+index;
            if(!existsInOffLimitNumbers(newPosition)){
                //there is a piece here
                if(aBoard[newPosition]!='-'){
                    moves.addAll(positionTaken(index,newPosition,ourColour));
                }
                else{
                    moves.add(new Moves(index,newPosition));
                }
            }

        }
        return moves;
    }


    //search to left
    private List<Moves> searchLeft(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum--;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum--;
            }
        }
        return moves;
    }
    //search to right
    private List<Moves> searchRight(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum++;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum++;
            }
        }
        return moves;
    }

    //search upwards
    private List<Moves> searchUpwards(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum+=10;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum+=10;
            }
        }
        return moves;
    }

    //search downwards
    private List<Moves> searchDownwards(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum-=10;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum-=10;
            }
        }
        return moves;
    }


    //search right upwards
    private List<Moves> searchRightUpwards(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum+=11;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum+=11;
            }
        }
        return moves;
    }

    //search left upwards
    private List<Moves> searchLeftUpwards(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum+=9;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum+=9;
            }
        }
        return moves;
    }

    //search right downwards
    private List<Moves> searchRightDownwards(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum-=9;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum-=9;
            }
        }
        return moves;
    }

    //search left downwards
    private List<Moves> searchLeftDownwards(int index, String ourColour){
        List<Moves> moves=new ArrayList<>();
        int aNum=index;
        aNum-=11;
        while(!existsInOffLimitNumbers(aNum)){
            //there is a piece here
            if(aBoard[aNum]!='-'){
                moves.addAll(positionTaken(index,aNum,ourColour));
                return moves;
            }
            //there is an empty space
            else{
                moves.add(new Moves(index,aNum));
                aNum-=11;
            }
        }
        return moves;
    }


    private List<Moves> positionTaken(int index, int position,String ourColour){
        List<Moves> moves=new ArrayList<>();
            //our colour is white
            if(ourColour.equals("white")){
                //if we encounter a black piece then add to list and return
                if(blackPieces.contains(aBoard[position])) {
                    moves.add(new Moves(index,position));
                }
            }else{ //now when our colour is black
                //if we encounter a white piece then add to list and return
                if(whitePieces.contains(aBoard[position])) {
                    moves.add(new Moves(index,position));
                }
            }
            return moves;

    }


    //check if an index is off bounds
    public boolean existsInOffLimitNumbers(int num){
        if(offLimitNums.contains(num))
            return true;

        return false;
    }


    //these are all the indexes where our chess board doesn't exist
    private void offLimitNumbers(){
        offLimitNums=new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            offLimitNums.add(i);
        }
        int aNum=30;
        while(aNum<=90) {
            offLimitNums.add(aNum);
            aNum+=10;

        }
        aNum=29;
        while(aNum<=89) {
            offLimitNums.add(aNum);
            aNum+=10;

        }
        for (int i = 99; i < 120; i++) {
            offLimitNums.add(i);
        }

    }

}



