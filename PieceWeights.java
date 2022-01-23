import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PieceWeights {
    int Pawn=5;//pawn
    int Rook=50;//rook
    int Knight=20;//knight
    int Bishop=20;//bishop
    int Queen=100;//queen
    int King=1500;//king
    double[] knightWeights;
    double[] whitePawnWeights;
    double[] blackPawnsWeights;


    public PieceWeights(){
        setKnightWeights();
        setWhitePawnWeights();
        setBlackPawnWeights();
    }



    public int getWeights(char piece){
        int result=switch(piece){
            case 'r', 'R' -> Rook;
            case 'n', 'N' -> Knight;
            case 'b', 'B' -> Bishop;
            case 'q', 'Q' -> Queen;
            case 'k', 'K' -> King;
            default -> Pawn;
        };
          return result;
    }
    public double getKnightWeight(int index){
        return knightWeights[index];
    }

    public double getWhitePawnWeight(int index){
        return whitePawnWeights[index];
    }
    public double getBlackPawnWeight(int index){
        return blackPawnsWeights[index];
    }


    private void setWhitePawnWeights(){
        whitePawnWeights=new double[120];
        List<Double> eighthRow =new ArrayList<>(Arrays.asList(12.0,12.0,12.0,12.0,12.0,12.0,12.0,12.0));
        List<Double> seventhRow=new ArrayList<>(Arrays.asList(7.0,7.0,7.0,7.0,7.0,7.0,7.0,7.0));
        List<Double> sixthRow  =new ArrayList<>(Arrays.asList(5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0));
        List<Double> fifthRow  =new ArrayList<>(Arrays.asList(4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0));
        List<Double> fourthRow =new ArrayList<>(Arrays.asList(3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0));
        List<Double> thirdRow  =new ArrayList<>(Arrays.asList(2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0));
        List<Double> secondRow =new ArrayList<>(Arrays.asList(1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0));
        List<Double> firstRow  =new ArrayList<>(Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0));
        int index=0;
        for (int i = 21; i < 29; i++) {
            whitePawnWeights[i]=firstRow.get(index);
        }
        index=0;
        for (int i = 31; i < 39; i++) {
            whitePawnWeights[i]=secondRow.get(index);
        }
        index=0;
        for (int i = 41; i < 49; i++) {
            whitePawnWeights[i]=thirdRow.get(index);
        }
        index=0;
        for (int i = 51; i < 59; i++) {
            whitePawnWeights[i]=fourthRow.get(index);
        }
        index=0;
        for (int i = 61; i < 69; i++) {
            whitePawnWeights[i]=fifthRow.get(index);
        }
        index=0;
        for (int i = 71; i < 79; i++) {
            whitePawnWeights[i]=sixthRow.get(index);
        }
        index=0;
        for (int i = 81; i < 89; i++) {
            whitePawnWeights[i]=seventhRow.get(index);
        }
        index=0;
        for (int i = 91; i < 99; i++) {
            whitePawnWeights[i]=eighthRow.get(index);
        }


    }
    private void setBlackPawnWeights(){
        blackPawnsWeights=new double[120];
        List<Double> firstRow =new ArrayList<>(Arrays.asList(12.0,12.0,12.0,12.0,12.0,12.0,12.0,12.0));
        List<Double> secondRow=new ArrayList<>(Arrays.asList(7.0,7.0,7.0,7.0,7.0,7.0,7.0,7.0));
        List<Double> thirdRow  =new ArrayList<>(Arrays.asList(5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0));
        List<Double> fourthRow  =new ArrayList<>(Arrays.asList(4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0));
        List<Double> fifthRow =new ArrayList<>(Arrays.asList(3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0));
        List<Double> sixthRow  =new ArrayList<>(Arrays.asList(2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0));
        List<Double> seventhRow =new ArrayList<>(Arrays.asList(1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0));
        List<Double> eighthRow  =new ArrayList<>(Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0));
        int index=0;
        for (int i = 21; i < 29; i++) {
            blackPawnsWeights[i]=firstRow.get(index);
        }
        index=0;
        for (int i = 31; i < 39; i++) {
            blackPawnsWeights[i]=secondRow.get(index);
        }
        index=0;
        for (int i = 41; i < 49; i++) {
            blackPawnsWeights[i]=thirdRow.get(index);
        }
        index=0;
        for (int i = 51; i < 59; i++) {
            blackPawnsWeights[i]=fourthRow.get(index);
        }
        index=0;
        for (int i = 61; i < 69; i++) {
            blackPawnsWeights[i]=fifthRow.get(index);
        }
        index=0;
        for (int i = 71; i < 79; i++) {
            blackPawnsWeights[i]=sixthRow.get(index);
        }
        index=0;
        for (int i = 81; i < 89; i++) {
            blackPawnsWeights[i]=seventhRow.get(index);
        }
        index=0;
        for (int i = 91; i < 99; i++) {
            blackPawnsWeights[i]=eighthRow.get(index);
        }
    }



    private void setKnightWeights(){
        knightWeights=new double[120];
        List<Double> eighthRow =new ArrayList<>(Arrays.asList(0.5,0.8,1.0,1.2,1.2,1.0,0.8,0.5));
        List<Double> seventhRow=new ArrayList<>(Arrays.asList(0.8,1.4,1.6,1.9,1.9,1.6,1.4,0.8));
        List<Double> sixthRow  =new ArrayList<>(Arrays.asList(1.5,2.1,3.5,4.0,4.0,3.5,2.1,1.5));
        List<Double> fifthRow  =new ArrayList<>(Arrays.asList(1.8,2.3,4.5,5.0,5.0,4.5,2.3,1.8));
        List<Double> fourthRow =new ArrayList<>(Arrays.asList(1.8,2.3,4.5,5.0,5.0,4.5,2.3,1.8));
        List<Double> thirdRow  =new ArrayList<>(Arrays.asList(1.5,2.1,3.5,4.0,4.0,3.5,2.1,1.5));
        List<Double> secondRow =new ArrayList<>(Arrays.asList(0.8,1.4,1.6,1.9,1.9,1.6,1.4,0.8));
        List<Double> firstRow  =new ArrayList<>(Arrays.asList(0.5,0.8,1.0,1.2,1.2,1.0,0.8,0.5));

        int index=0;
        for (int i = 21; i < 29; i++) {
            knightWeights[i]=firstRow.get(index);
        }
        index=0;
        for (int i = 31; i < 39; i++) {
            knightWeights[i]=secondRow.get(index);
        }
        index=0;
        for (int i = 41; i < 49; i++) {
            knightWeights[i]=thirdRow.get(index);
        }
        index=0;
        for (int i = 51; i < 59; i++) {
            knightWeights[i]=fourthRow.get(index);
        }
        index=0;
        for (int i = 61; i < 69; i++) {
            knightWeights[i]=fifthRow.get(index);
        }
        index=0;
        for (int i = 71; i < 79; i++) {
            knightWeights[i]=sixthRow.get(index);
        }
        index=0;
        for (int i = 81; i < 89; i++) {
            knightWeights[i]=seventhRow.get(index);
        }
        index=0;
        for (int i = 91; i < 99; i++) {
            knightWeights[i]=eighthRow.get(index);
        }


    }






}
