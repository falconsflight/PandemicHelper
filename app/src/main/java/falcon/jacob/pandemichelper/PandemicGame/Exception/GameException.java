package falcon.jacob.pandemichelper.PandemicGame.Exception;

public class GameException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public static final int DECK_EMPTY = 0;
    public static final String DECK_EMPTY_MESSAGE = "Deck is empty!";

    private static final String[] MESSAGES = {DECK_EMPTY_MESSAGE};
    public static String getMessage(int index){
        return MESSAGES[index];
    }

    protected int exceptionCode;

    public GameException(int exceptionCode){
        this.exceptionCode = exceptionCode;
    }

    public int getErrorCode(){
        return this.exceptionCode;
    }

    @Override
    public String getMessage(){
        return MESSAGES[this.exceptionCode];
    }
}