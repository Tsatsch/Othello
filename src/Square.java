import java.util.Arrays;
import java.util.HashSet;

/** each board square is an object Square with value (BLACK, WHITE, EMPTY or SUGGESTON) and coordinates*/
public class Square {

    private final String coordinates;
    private int value;
    public static final HashSet<Integer> allowedValues = new HashSet<>
            (Arrays.asList(Discs.EMPTY,Discs.BLACK,Discs.WHITE,Discs.SUGGESTION));

    public Square(String coordinates, int value){
        this.coordinates = coordinates;

        if(allowedValues.contains(value)) this.value = value;
        else this.value = Discs.EMPTY;
    }

    public void setValue(int value) {
        if(allowedValues.contains(value)) this.value = value;
        else this.value = Discs.EMPTY;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public int getValue() {
        return value;
    }


    }
