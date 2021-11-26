package falcon.jacob.pandemichelper.PandemicGame.Interface;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;

public interface ICard {
    public void play();
    public String getName();
    public CardType getType();
}