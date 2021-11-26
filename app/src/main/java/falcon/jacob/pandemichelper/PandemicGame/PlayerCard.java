package falcon.jacob.pandemichelper.PandemicGame;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;
import falcon.jacob.pandemichelper.PandemicGame.Interface.ICard;

public abstract class PlayerCard implements ICard {
    protected String name;

    protected CardType type;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CardType getType() {
        return this.type;
    }

    public abstract PlayerCard deepCopy();

    public String toString(){
        return this.name;
    }
}