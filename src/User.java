import szte.mi.Move;

import java.util.Random;

/** represents user playing Othello*/
public class User {

    private int disc;
    private Random random;
    private long t;
    private Move prevMove;

    public int getDisc() {
        return disc;
    }

    public void setDisc(int disc) {
        this.disc = disc;
    }

    public Move getPrevMove() {
        return prevMove;
    }

    public void setPrevMove(Move move) {
        prevMove = move;
    }

    public long getRemainingTime() {
        return t;
    }

    public void subtractTime(long time) {
        t -= time;
    }

    // order - 0 for Black and 1 for White, t is init time in ms
    public void init(int order, long t, Random rnd) {
        this.disc = order;
        this.t = t;
        this.random = rnd;
    }

    // t is remaining time of user and tOpponent remaining time of opponent
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        return null;
    }
}