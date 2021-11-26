package falcon.jacob.pandemichelper.PandemicGame;

public class GameRules {
    public int getCardsForPlayerCount(int count){
        return 6 - count;
    }
}